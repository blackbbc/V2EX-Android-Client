package com.example.sweet.myapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.example.sweet.myapplication.R;

public class GoogleProgressBar extends ProgressBar {

    private enum ProgressType{
        FOLDING_CIRCLES,
        GOOGLE_MUSIC_DICES,
        NEXUS_ROTATION_CROSS
    }

    public GoogleProgressBar(Context context) {
        this(context, null);
    }

    public GoogleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,android.R.attr.progressBarStyle);
    }

    public GoogleProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Due to some new ADT features, initialing with values from resource file may meet preview problems.
        // If View.isInEditMode() returns true, skip drawing.
        if (isInEditMode())
            return;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GoogleProgressBar, defStyle, 0);
        final int typeIndex = a.getInteger(R.styleable.GoogleProgressBar_type, context.getResources().getInteger(R.integer.default_type));
        final int colorsId = a.getResourceId(R.styleable.GoogleProgressBar_colors, R.array.google_colors);
        a.recycle();

        Drawable drawable = new FoldingCirclesDrawable.Builder(context).colors(getResources().getIntArray(colorsId)).build();
        if(drawable!=null)
        setIndeterminateDrawable(drawable);
    }

}
