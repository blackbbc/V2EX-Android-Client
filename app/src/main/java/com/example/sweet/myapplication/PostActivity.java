package com.example.sweet.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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


public class PostActivity extends ActionBarActivity {

    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        getWindow().setEnterTransition(new Slide());
        getWindow().setReturnTransition(new Slide());

        tv1 = (TextView)findViewById(R.id.textView2);

    }

}
