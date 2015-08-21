package me.sweetll.v2ex.Fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private int mPage;

    @Bind(R.id.list_swipe) WaveSwipeRefreshLayout refreshLayout;
    @Bind(R.id.article_list) RecyclerView recyclerView;

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
