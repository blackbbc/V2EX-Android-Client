package me.sweetll.v2ex.Fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.orhanobut.logger.Logger;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import me.sweetll.v2ex.Adapter.ArticleListRecyclerViewAdapter;
import me.sweetll.v2ex.DataStructure.Post;
import me.sweetll.v2ex.R;
import me.sweetll.v2ex.Utils.GlobalGlass;

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

    private int mPage;
    private StringRequest stringRequest;
    private ArticleListRecyclerViewAdapter recyclerViewAdapter;
    private String url;

    @Bind(R.id.list_swipe) WaveSwipeRefreshLayout refreshLayout;
    @Bind(R.id.article_list) RecyclerView recyclerView;

    private void refreshList() {
        Logger.d(url);
        GlobalGlass.getQueue().add(stringRequest);
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
                        Document document = Jsoup.parse(response);

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
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            recyclerViewAdapter.add(new Post(list_title, list_userName, list_time, list_tag, list_reply, list_imageSrc, list_src));
                        }

                        recyclerViewAdapter.notifyDataSetChanged();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                refreshLayout.setRefreshing(false);
                            }
                        }, 500);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d("Volley Error");
            }
        });
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
                        .color(getResources().getColor(R.color.background_material_light))
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

    //Fix conflict with CoordinatorLayout

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

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
