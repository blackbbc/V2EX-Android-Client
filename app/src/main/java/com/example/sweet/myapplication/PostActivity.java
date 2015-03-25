package com.example.sweet.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sweet.myapplication.transition.RevealTransition;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;


public class PostActivity extends ActionBarActivity {

    public TextView title;
    public TextView username;
    public TextView time;
    public TextView tag;
    public TextView postcontent;
    public ImageView image;

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

        Intent intent = getIntent();
        float locationX = intent.getFloatExtra("locationX", 0f);
        float locationY = intent.getFloatExtra("locationY", 0f);

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
                    URL baseURL = new URL(url);

                    final String content = doc.select("div.topic_content").text();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            postcontent.setText(content);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        RevealTransition reveal = new RevealTransition(locationX, locationY);
        reveal.addTarget(R.id.card_view);

        getWindow().setEnterTransition(reveal);
        getWindow().setReturnTransition(reveal);

    }

}
