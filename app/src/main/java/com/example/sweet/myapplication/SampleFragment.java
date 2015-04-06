package com.example.sweet.myapplication;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.Handler;

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

        final SwipeRefreshLayout layout;
        layout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<PostListStruct> lists = new ArrayList<PostListStruct>();

                        try {
                            URL baseURL = new URL(url);
                            Document doc = Jsoup.connect(url).timeout(4000).get();

                            Elements cellitems = doc.select("div.cell.item");

                            for (Element cellitem : cellitems) {
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

        return rootView;
    }

    class ArrayAdapter extends RecyclerView.Adapter<ViewHolder>{

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
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            viewHolder.title.setText(mArray.get(position).title);
            viewHolder.username.setText(mArray.get(position).username);
            viewHolder.time.setText(mArray.get(position).time);
            viewHolder.tag.setText(mArray.get(position).tag);
            ImageLoader.getInstance().displayImage(mArray.get(position).imageSrc, viewHolder.image);
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostListStruct list = getItem(position);
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), PostActivity.class);
                    Gson gson = new Gson();
                    String json = gson.toJson(getItem(position));
                    intent.putExtra("json", json);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity());
                    getActivity().startActivity(intent, options.toBundle());
                }
            });
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
        public CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            title = (TextView)itemView.findViewById(R.id.header);
            username = (TextView)itemView.findViewById(R.id.username);
            time = (TextView)itemView.findViewById(R.id.time);
            tag = (TextView)itemView.findViewById(R.id.tag);
            image = (ImageView)itemView.findViewById(R.id.imageview);
        }
    }

}
