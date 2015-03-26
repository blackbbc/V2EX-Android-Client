package com.example.sweet.myapplication;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class SampleFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;
    private String url;

    public static SampleFragment newInstance(int position, String url) {
        SampleFragment f = new SampleFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString("url", url);
        f.setArguments(b);
        return f;
    }

    private ArrayList<PostListStruct> getDataFromSqlite() {
        ArrayList<PostListStruct> lists = new ArrayList<PostListStruct>();

        return lists;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position = getArguments().getInt(ARG_POSITION);
        url = getArguments().getString("url");

        View rootView = inflater.inflate(R.layout.page, container, false);

        final RecyclerView mRecyclerView;
        final ArrayAdapter mAdapter;

        final ArrayList<PostListStruct> mDataset;

        mDataset = getDataFromSqlite();

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        mAdapter = new ArrayAdapter(inflater.getContext(), mDataset);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            private static final int MAX_CLICK_DURATION = 200;
            private static final int MAX_CLICK_DISTANCE = 15;
            private long startClickTime;
            private float pressedX, pressedY;

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(), e.getY());
                int position = rv.getChildPosition(childView);

                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        startClickTime = Calendar.getInstance().getTimeInMillis();
                        pressedX = e.getX();
                        pressedY = e.getY();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                        if(childView != null && clickDuration < MAX_CLICK_DURATION && distance(pressedX, pressedY, e.getX(), e.getY()) < MAX_CLICK_DISTANCE) {
                            //click event has occurred
                            onTouchEvent(rv, e);
                            return true;
                        }
                    }
                }

                return false;
            }

            private float distance(float x1, float y1, float x2, float y2) {
                float dx = x1 - x2;
                float dy = y1 - y2;
                float distanceInPx = (float) Math.sqrt(dx * dx + dy * dy);
                return pxToDp(distanceInPx);
            }

            private float pxToDp(float px) {
                return px / getResources().getDisplayMetrics().density;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                PostListStruct list = mAdapter.getItem(position);

                final Intent intent = new Intent();
                intent.setClass(getActivity(), PostActivity.class);
                intent.putExtra("locationX", e.getRawX());
                intent.putExtra("locationY", e.getRawY());
                intent.putExtra("position", position);
                Gson gson = new Gson();
                String json = gson.toJson(mAdapter.getItem(position));
                intent.putExtra("json", json);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity());
                getActivity().startActivity(intent);

            }
        });

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
                            URL baseURL = new URL(url);
                            Document doc = Jsoup.connect(url).timeout(4000).get();

                            Elements cellitems = doc.select("div.cell.item").eq(1);
                            cellitems.remove();

                            for (Element cellitem : cellitems) {
                                String temp = cellitem.select("span.small.fade").text();
                                String[] smallFade = cellitem.select("span.small.fade").text().split("•");
                                String a = cellitem.select("span.item_title").text();
                                String b = smallFade[1].replace(String.valueOf((char) 160), " ").trim();

                                //FUCK!
                                int spacePosition = b.indexOf(" ");
                                String c = b.substring(spacePosition);
                                b = b.substring(0, spacePosition);

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
            ImageLoader.getInstance().displayImage(mArray.get(i).imageSrc, viewHolder.image);
        }

        @Override
        public int getItemCount() {
            return mArray.size();
        }

        public PostListStruct getItem(int position) {
            return mArray.get(position);
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

