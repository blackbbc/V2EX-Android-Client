package com.example.sweet.myapplication.transition;

import android.animation.Animator;
import android.content.Context;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

/**
 * Created by sweet on 15-3-23.
 */
public class RevealTransition extends Visibility {
    private float locationX;
    private float locationY;

    public RevealTransition(float x, float y) {
        locationX = x;
        locationY = y;
    }

    public RevealTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot, final View view, TransitionValues statr, TransitionValues end) {
        float radius = calculateMaxRadius(view);

        Animator reveal = createAnimator(view, 0, radius);
        return reveal;
    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, final View view, TransitionValues statr, TransitionValues end) {
        float radius = calculateMaxRadius(view);

        Animator reveal = createAnimator(view, radius, 0);
        return reveal;
    }

    private Animator createAnimator(View view, float start, float end) {
//        int cx = view.getWidth() / 2;
//        int cy = view.getHeight() / 2;
        Animator reveal = ViewAnimationUtils.createCircularReveal(view, (int) locationX, (int) locationY, start, end);
        return reveal;
    }

    float calculateMaxRadius(View view) {
        float width = Math.max(locationX - view.getLeft(), view.getRight() - locationX);
        float height = Math.max(locationY - view.getTop(), view.getBottom() - locationY);
        float widthSquared = width * width;
        float heightSquared = height * height;
        float radius = (float) Math.sqrt(widthSquared + heightSquared);
        return radius;
    }

}
