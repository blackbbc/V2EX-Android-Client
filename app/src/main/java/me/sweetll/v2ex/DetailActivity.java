package me.sweetll.v2ex;

import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.sweetll.v2ex.Adapter.ArticleDetailRecyclerViewAdapter;
import me.sweetll.v2ex.Adapter.ArticleReplyRecyclerViewAdapter;
import me.sweetll.v2ex.DataStructure.Post;
import me.sweetll.v2ex.Utils.GlobalGlass;
import me.sweetll.v2ex.Utils.Ui;
import me.sweetll.v2ex.Widget.FitWindowView;

public class DetailActivity extends AppCompatActivity implements FitWindowView.OnFitSystemWindowsListener{
    @Bind(R.id.standard) FitWindowView mStandard;
    @Bind(R.id.main) ScrollView mainLayout;
    @Bind(R.id.detail_content) RecyclerView detailRecyclerView;
    @Bind(R.id.detail_reply) RecyclerView replyRecyclerView;
    @Bind(R.id.detail_title) TextView detailTitle;
    @Bind(R.id.detail_author) TextView detailAuthor;
    @Bind(R.id.detail_time) TextView detailTime;
    @Bind(R.id.detail_avatar) SimpleDraweeView detailAvatar;

    private StringRequest stringRequest;
    private ArticleDetailRecyclerViewAdapter detailRecyclerViewAdapter;
    private ArticleReplyRecyclerViewAdapter replyRecyclerViewAdapter;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mStandard.addOnFitSystemWindowsListener(this);

        Post post = Parcels.unwrap((Parcelable)getIntent().getExtras().get("article_data"));
        getSupportActionBar().setTitle(post.getTitle());
        detailTitle.setText(post.getTitle());
        detailAuthor.setText(post.getUserName());
        detailTime.setText(post.getTime());
        detailAvatar.setImageURI(Uri.parse(post.getImageSrc()));


        detailRecyclerViewAdapter = new ArticleDetailRecyclerViewAdapter();
        detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailRecyclerView.setAdapter(detailRecyclerViewAdapter);

        replyRecyclerViewAdapter = new ArticleReplyRecyclerViewAdapter();
        replyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        replyRecyclerView.setAdapter(replyRecyclerViewAdapter);

        url = post.getSrc();
        stringRequest = new StringRequest(Request.Method.GET, url,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);

//                        recyclerViewAdapter.clear();
//
//                        String list_title;
//                        String list_userName;
//                        String list_time;
//                        String list_tag;
//                        String list_reply;
//                        String list_imageSrc;
//                        String list_src;
//
//                        Elements cells = document.select("div.cell.item");
//                        for (Element cell : cells) {
//                            list_src = cell.select("span.item_title>a").first().attr("href").split("#")[0];
//                            list_imageSrc = cell.select("img.avatar").first().attr("src");
//                            list_title = cell.select("span.item_title>a").first().text();
//                            list_tag = cell.select("a.node").first().text();
//                            list_userName = cell.select("strong").first().text();
//                            list_reply = cell.select("a.count_livid").text();
//                            list_reply = list_reply.isEmpty()? "0": list_reply;
//                            String small_fade = cell.select("span.small.fade").eq(1).text();
//                            list_time = small_fade.split(" \u00a0•\u00a0 ")[0];
//
//                            try {
//                                list_src = new URL(new URL(url), list_src).toString();
//                                list_imageSrc = new URL(new URL(url), list_imageSrc).toString();
//                            } catch (MalformedURLException e) {
//                                e.printStackTrace();
//                            }
//                            recyclerViewAdapter.add(new Post(list_title, list_userName, list_time, list_tag, list_reply, list_imageSrc, list_src));
//                        }
//
//                        recyclerViewAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d("Volley Error");
            }
        });

        GlobalGlass.getQueue().add(stringRequest);

    }

    @Override
    public void onFitSystemWindows(int l, int t, int r, int b) {
        mainLayout.setPadding(mainLayout.getPaddingLeft(), t, mainLayout.getPaddingRight(), b);

//        Ui.colorStatusBar(this, t - Ui.ACTION_BAR_HEIGHT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
