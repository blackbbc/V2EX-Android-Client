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
import android.widget.TextView;

import com.example.sweet.myapplication.transition.RevealTransition;


public class PostActivity extends ActionBarActivity {

    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        float locationX = intent.getFloatExtra("locationX", 0f);
        float locationY = intent.getFloatExtra("locationY", 0f);

        RevealTransition reveal = new RevealTransition(locationX, locationY);
        reveal.addTarget(R.id.card_view);

        getWindow().setEnterTransition(reveal);
        getWindow().setReturnTransition(reveal);

    }

}
