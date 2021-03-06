package me.sweetll.v2ex.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orhanobut.logger.Logger;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import me.sweetll.v2ex.Adapter.ArticleListRecyclerViewAdapter;
import me.sweetll.v2ex.DataStructure.Post;
import me.sweetll.v2ex.R;
import me.sweetll.v2ex.Utils.GlobalClass;

/**
 * Created by sweet on 15-8-17.
 */
public class ArticleListFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private static final String[] TAB_URLS = {
            "http://www.v2ex.com/?tab=tech",
            "http://www.v2ex.com/?tab=creative",
            "http://www.v2ex.com/?tab=play",
            "http://www.v2ex.com/?tab=apple",
            "http://www.v2ex.com/?tab=jobs",
            "http://www.v2ex.com/?tab=deals",
            "http://www.v2ex.com/?tab=city",
            "http://www.v2ex.com/?tab=qna",
            "http://www.v2ex.com/?tab=hot",
            "http://www.v2ex.com/?tab=all",
            "http://www.v2ex.com/?tab=r2"
    };

    @Bind(R.id.list_swipe) WaveSwipeRefreshLayout refreshLayout;
    @Bind(R.id.article_list) RecyclerView recyclerView;

    private int mPage;
    private StringRequest stringRequest;
    private ArticleListRecyclerViewAdapter recyclerViewAdapter;
    private String url;

    HandlerThread thread;
    Handler handler;
    boolean isRunning;
    class RefreshListRunnable implements Runnable {
        String response;

        public RefreshListRunnable(String response) {
            this.response = response;
        }

        @Override
        public void run() {
//            Logger.d("I'm on runnable");

            Document document = Jsoup.parse(response);

            recyclerViewAdapter.clear();

            String list_title;
            String list_userName;
            String list_time;
            String list_tag;
            String list_reply;
            String list_imageSrc;
            String list_src;

            Elements cells = document.select("div.cell.item");
            for (Element cell : cells) {
                list_src = cell.select("span.item_title>a").first().attr("href").split("#")[0];
                list_imageSrc = cell.select("img.avatar").first().attr("src");
                list_title = cell.select("span.item_title>a").first().text();
                list_tag = cell.select("a.node").first().text();
                list_userName = cell.select("strong").first().text();
                list_reply = cell.select("a.count_livid").text();
                list_reply = list_reply.isEmpty()? "0": list_reply;
                String small_fade = cell.select("span.small.fade").eq(1).text();
                list_time = small_fade.split(" \u00a0•\u00a0 ")[0];

                try {
                    list_src = new URL(new URL(url), list_src).toString();
                    list_imageSrc = new URL(new URL(url), list_imageSrc).toString();
                    recyclerViewAdapter.add(new Post(list_title, list_userName, list_time, list_tag, list_reply, list_imageSrc, list_src));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerViewAdapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                }
            });

        }
    }

    private void refreshList() {
        GlobalClass.getQueue().add(stringRequest);
    }

    public static ArticleListFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ArticleListFragment fragment = new ArticleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        url = TAB_URLS[mPage - 1];
        stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handler.post(new RefreshListRunnable(response));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d("Volley Error");
            }
        });

        thread = new HandlerThread("refresh_list");
        thread.start();
        handler = new Handler(thread.getLooper());
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_list, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerViewAdapter = new ArticleListRecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(getResources().getColor(R.color.divider))
                        .sizeResId(R.dimen.divider_size)
                        .build()
        );

        refreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        refreshLayout.setWaveColor(getResources().getColor(R.color.Primary));
        refreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.isRunning = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        this.isRunning = false;
    }

    //Fix conflict with CoordinatorLayout

    @Override
    public void onPause() {
        super.onPause();
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
            refreshLayout.setEnabled(false);
            refreshLayout.destroyDrawingCache();
            refreshLayout.clearAnimation();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setSwipeToRefreshEnabled(boolean enabled) {
        refreshLayout.setEnabled(enabled);
    }
}
