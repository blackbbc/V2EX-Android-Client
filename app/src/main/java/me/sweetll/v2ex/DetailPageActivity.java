package me.sweetll.v2ex;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.marshalchen.ultimaterecyclerview.CustomUltimateRecyclerview;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.sweetll.v2ex.DataStructure.Detail;
import me.sweetll.v2ex.DataStructure.Post;


public class DetailPageActivity extends ActionBarActivity {
    @InjectView(R.id.ultimate_recycler_view) CustomUltimateRecyclerview recyclerView;
    @InjectView(R.id.detail_title) TextView title;

    UlRecyclerviewAdapter ulRecyclerviewAdapter;
    LinearLayoutManager linearLayoutManager;

    String url;
    URL baseURL;
    int currentReply = 0;
    boolean isRefreshing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);
        ButterKnife.inject(this);

        recyclerView.setHasFixedSize(false);

        List<Detail> mData = new ArrayList<>();

        ulRecyclerviewAdapter = new UlRecyclerviewAdapter(this, mData);

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ulRecyclerviewAdapter);

        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .sizeResId(R.dimen.divider)
                .colorResId(R.color.divider_dark)
                .marginResId(R.dimen.lefrmargin, R.dimen.rightmargin)
                .build());

        Intent intent = getIntent();
        String json = intent.getStringExtra("json");
        Gson gson = new Gson();
        final Post jsonData = gson.fromJson(json, Post.class);
        title.setText(jsonData.title);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Detail> lists = new ArrayList<>();

                    url = jsonData.src;
                    baseURL = new URL(url);

                    String reg = "#reply(\\d)+";
                    String rep = "";
                    Pattern pattern = Pattern.compile(reg);
                    Matcher matcher = pattern.matcher(url);
                    int nextPage =  getNextPage();
                    url = matcher.replaceAll(rep);

                    Document doc = Jsoup.connect(url+"?p="+nextPage).get();

                    //Fetch the 0-th floor content
                    String topic_content = doc.select("div.topic_content").html();
                    Detail topic = new Detail(jsonData.userName, jsonData.time, topic_content, "楼主", jsonData.imageSrc);
                    lists.add(topic);

                    //Fetch the 1st page replies
                    Elements cells = doc.select("div.box>div");

                    int length = cells.size();
                    for (int index = length - 1; index >= 0; index--) {
                        if (!cells.get(index).hasAttr("id")) {
                            cells.remove(index);
                        }
                    }

                    for (Element cell:cells) {
                        String content = cell.select("div.reply_content").html();
                        String userName = cell.select("a.dark").text();
                        String time = cell.select("span.fade.small").text();
                        String floor = cell.select("span.no").text();
                        String imageSrc = cell.select("img.avatar").attr("src");
                        URL temp = new URL(baseURL, imageSrc);
                        imageSrc = temp.toString();
                        Detail list = new Detail(userName, time, content, floor, imageSrc);
                        lists.add(list);
                    }

                    currentReply = lists.size() - 1;

                    ulRecyclerviewAdapter.addDataAll(lists);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ulRecyclerviewAdapter.notifyDataSetChanged();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        recyclerView.enableLoadmore();
        ulRecyclerviewAdapter.setCustomLoadMoreView(LayoutInflater.from(this).inflate(R.layout.custom_bottom_progressbar, null));
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                Thread NPThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int nextReply = currentReply + 1;
                        int nextPage = getNextPage();

                        try {
                            List<Detail> lists = new ArrayList<>();

                            Document doc = Jsoup.connect(url+"?p="+nextPage).get();

                            //Fetch the page replies
                            Elements cells = doc.select("div.box>div");

                            int length = cells.size();
                            for (int index = length - 1; index >= 0; index--) {
                                if (!cells.get(index).hasAttr("id")) {
                                    cells.remove(index);
                                }
                            }

                            for (Element cell:cells) {
                                String floor = cell.select("span.no").text();

                                if (Integer.parseInt(floor) < nextReply) {
                                    continue;
                                }

                                String content = cell.select("div.reply_content").html();
                                String userName = cell.select("a.dark").text();
                                String time = cell.select("span.fade.small").text();
                                String imageSrc = cell.select("img.avatar").attr("src");
                                URL temp = new URL(baseURL, imageSrc);
                                imageSrc = temp.toString();
                                Detail list = new Detail(userName, time, content, floor, imageSrc);
                                lists.add(list);
                            }

                            currentReply += lists.size();

                            if (lists.size() != 0) {
                                isRefreshing = false;
                            }

                            ulRecyclerviewAdapter.addDataAll(lists);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ulRecyclerviewAdapter.notifyDataSetChanged();
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

                if (!isRefreshing) {
                    isRefreshing = true;
                    NPThread.start();
                }

            }
        });

    }

    public int getNextPage() {
        return currentReply / 100 + 1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
