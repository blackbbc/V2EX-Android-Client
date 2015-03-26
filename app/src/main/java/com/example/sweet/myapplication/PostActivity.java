package com.example.sweet.myapplication;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sweet.myapplication.widget.FloatingActionButton;
import com.example.sweet.myapplication.widget.ProgressBarCircular;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class PostActivity extends ActionBarActivity {

    public TextView title;
    public TextView username;
    public TextView time;
    public TextView tag;
    public TextView postcontent;
    public ImageView image;

    public RecyclerView mRecyclerView;
    public ArrayAdapter mAdapter;
    public ArrayList<ReplyDetailStruct> mDataSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        title = (TextView)findViewById(R.id.textView2);
        username = (TextView)findViewById(R.id.textView3);
        time = (TextView)findViewById(R.id.textView4);
        tag = (TextView)findViewById(R.id.textView5);
        postcontent = (TextView)findViewById(R.id.postcontent);
        image = (ImageView)findViewById(R.id.imageView);

        final ProgressBarCircular progressBarCircular = (ProgressBarCircular)findViewById(R.id.progress);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fabButton);
        fab.setDrawableIcon(getResources().getDrawable(R.drawable.plus));
        fab.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));


        mDataSet = new ArrayList<ReplyDetailStruct>();
        mRecyclerView = (RecyclerView)findViewById(R.id.post_recyclerview);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ArrayAdapter(this, mDataSet);
        mRecyclerView.setAdapter(mAdapter);

        Intent intent = getIntent();

        String json = intent.getStringExtra("json");
        Gson gson = new Gson();
        final PostListStruct jsonData = gson.fromJson(json, PostListStruct.class);

        title.setText(jsonData.title);
        username.setText(jsonData.username);
        time.setText(jsonData.time);
        tag.setText(jsonData.tag);
        ImageLoader.getInstance().displayImage(jsonData.imageSrc, image);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = jsonData.src;

                    Document doc = Jsoup.connect(url).get();

                    final String content = doc.select("div.topic_content").text();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            postcontent.setText(content);
                            progressBarCircular.setVisibility(View.GONE);
                            postcontent.setVisibility(View.VISIBLE);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<ReplyDetailStruct> lists = new ArrayList<ReplyDetailStruct>();

                        try {
                            String url = jsonData.src;
                            URL baseURL = new URL(url);
                            Document doc = Jsoup.connect(url).timeout(4000).get();

                            Elements cells = doc.select("div.cell");

                            int length = cells.size();
                            for (int position=length-1; position>=0; position--) {
                                if (!cells.get(position).hasAttr("id")) {
                                    cells.remove(position);
                                }
                            }

                            for (Element cell : cells) {
                                String replyContent = cell.select("div.reply_content").text();
                                String imageSrc = cell.select("img.avatar").attr("src");
                                String username = cell.select("a.dark").text();
                                String time = cell.select("span.fade.small").text();
                                String floor = cell.select("span.no").text();
                                URL temp = new URL(baseURL, imageSrc);
                                imageSrc = temp.toString();
                                ReplyDetailStruct list = new ReplyDetailStruct(username, time, imageSrc, replyContent, floor);
                                lists.add(list);
                            }

                            mAdapter.setmArray(lists);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                }
                            });


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                thread.start();
            }
        });

//
//        getWindow().setEnterTransition(reveal);
//        getWindow().setReturnTransition(reveal);

    }

    class ArrayAdapter extends RecyclerView.Adapter<ViewHolder>{

        private ArrayList<ReplyDetailStruct> mArray;
        private Context mContext;

        public ArrayAdapter(Context context, ArrayList<ReplyDetailStruct> array) {
            mContext = context;
            mArray = array;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.reply, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.content.setText(mArray.get(i).content);
            viewHolder.username.setText(mArray.get(i).username);
            viewHolder.time.setText(mArray.get(i).time);
            viewHolder.floor.setText(mArray.get(i).floor);
            ImageLoader.getInstance().displayImage(mArray.get(i).imageSrc, viewHolder.image);
        }

        @Override
        public int getItemCount() {
            return mArray.size();
        }

        public PostListStruct getItem(int position) {
            return mArray.get(position);
        }


        public void setmArray (ArrayList<ReplyDetailStruct> newArray) {
            mArray = newArray;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView content;
        public TextView username;
        public TextView time;
        public TextView floor;
        public ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView)itemView.findViewById(R.id.reply_content);
            username = (TextView)itemView.findViewById(R.id.reply_username);
            time = (TextView)itemView.findViewById(R.id.reply_time);
            floor = (TextView)itemView.findViewById(R.id.reply_floor);
            image = (ImageView)itemView.findViewById(R.id.reply_avatar);
        }
    }

}
