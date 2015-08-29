package me.sweetll.v2ex.Fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import me.sweetll.v2ex.Adapter.ArticleListRecyclerViewAdapter;
import me.sweetll.v2ex.R;

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
    private RequestQueue queue;

    @Bind(R.id.list_swipe) WaveSwipeRefreshLayout refreshLayout;
    @Bind(R.id.article_list) RecyclerView recyclerView;

    private void refreshList() {
        String url = TAB_URLS[mPage - 1];
        Logger.d(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Document document = Jsoup.parse(response);

                        Elements cells = document.select("div.cell.item");
                        for (Element cell : cells) {
                            String list_url = cell.select("span.item_title>a").first().attr("href");
                            String list_title = cell.select("span.item_title>a").first().text();
                            String list_node = cell.select("a.node").first().text();
                            Element smallFade = cell.select("span.small.fade").first();
                            Logger.d(smallFade.html());
                            Logger.d(smallFade.text());
                        }

                        refreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d("Volley Error");
            }
        });
        queue.add(stringRequest);

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
        queue = Volley.newRequestQueue(getActivity());
        mPage = getArguments().getInt(ARG_PAGE);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_list, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        String[] testSet = {"title1", "title2", "title3"};
        recyclerView.setAdapter(new ArticleListRecyclerViewAdapter(testSet));

        refreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        refreshLayout.setWaveColor(getResources().getColor(R.color.Primary));
        refreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                new RefreshListTask().execute();
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
