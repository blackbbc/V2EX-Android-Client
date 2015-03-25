package com.example.sweet.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Handler;

public class SampleFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;

    public static SampleFragment newInstance(int position) {
        SampleFragment f = new SampleFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position = getArguments().getInt(ARG_POSITION);
        View rootView = inflater.inflate(R.layout.page, container, false);

        final RecyclerView mRecyclerView;
        final ArrayAdapter mAdapter;

        final ArrayList<PostListStruct> mDataset;
//        mDataset = Refresh.RefreshList();
        mDataset = new ArrayList<PostListStruct>();
        PostListStruct list;
        list = new PostListStruct("aaa", "aaa", "aaa", "aaa", "aaa", "aaa");
        mDataset.add(list);
        list = new PostListStruct("bbb", "bbb", "bbb", "bbb", "bbb", "bbb");
        mDataset.add(list);


        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        mAdapter = new ArrayAdapter(inflater.getContext(), mDataset);
        mRecyclerView.setAdapter(mAdapter);

        final PullRefreshLayout layout;
        layout = (PullRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<PostListStruct> lists = new ArrayList<PostListStruct>();

                        try {
                            String url = "https://v2ex.com/?tab=hot";
                            URL baseURL = new URL(url);
                            Document doc = Jsoup.connect(url).timeout(4000).get();

                            Elements cellitems = doc.select("div.cell.item");

                            for (Element cellitem : cellitems) {
                                String[] smallFade = cellitem.select("span.small.fade").text().split("•");
                                String a = cellitem.select("span.item_title").text();
                                String b = smallFade[1].replace(String.valueOf((char) 160), " ").trim();
                                String c = smallFade[2].replace(String.valueOf((char) 160), " ").trim();
                                String d = smallFade[0].replace(String.valueOf((char) 160), " ").trim();
                                String e = cellitem.select("img.avatar").attr("src");
                                String f = cellitem.select("span.item_title>a").attr("href");
                                URL temp1 = new URL(baseURL, e);
                                URL temp2 = new URL(baseURL, f);
                                e = temp1.toString();
                                f = temp2.toString();
                                PostListStruct list = new PostListStruct(a, b, c, d, e, f);
                                lists.add(list);
                            }

                            mAdapter.setmArray(lists);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                    layout.setRefreshing(false);
                                }
                            });


                        } catch (IOException e) {
                            e.printStackTrace();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    layout.setRefreshing(false);
                                }
                            });
                        }

                    }
                });
                thread.start();

            }
        });


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabButton);
        fab.setDrawableIcon(getResources().getDrawable(R.drawable.plus));
        switch (position) {
            case 0:
                fab.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                break;
            case 1:
                fab.setBackgroundColor(getResources().getColor(R.color.red));
                break;
            case 2:
                fab.setBackgroundColor(getResources().getColor(R.color.blue));
                break;
            case 3:
                fab.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                break;
        }

        return rootView;
    }

    static class ArrayAdapter extends RecyclerView.Adapter<ViewHolder>{

        private ArrayList<PostListStruct> mArray;
        private Context mContext;

        public ArrayAdapter(Context context, ArrayList<PostListStruct> array) {
            mContext = context;
            mArray = array;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.card, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.title.setText(mArray.get(i).title);
            viewHolder.username.setText(mArray.get(i).username);
            viewHolder.time.setText(mArray.get(i).time);
            viewHolder.tag.setText(mArray.get(i).tag);
        }

        @Override
        public int getItemCount() {
            return mArray.size();
        }

        public void setmArray (ArrayList<PostListStruct> newArray) {
            mArray = newArray;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView username;
        public TextView time;
        public TextView tag;
        public ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.header);
            username = (TextView)itemView.findViewById(R.id.username);
            time = (TextView)itemView.findViewById(R.id.time);
            tag = (TextView)itemView.findViewById(R.id.tag);
            image = (ImageView)itemView.findViewById(R.id.imageview);
        }
    }


}