package me.sweetll.v2ex.Utils;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;

import me.sweetll.v2ex.DetailPageActivity;
import me.sweetll.v2ex.R;

public class Navigator {

    public static int ANIM_DURATION = 350;

    public static void launchDetail(Activity fromActivity, View fromView, String json, View backgroundView) {
        ViewCompat.setTransitionName(fromView, "detail_element");
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        fromActivity,
                        Pair.create(fromView, "detail_element")
                );
        Intent intent = new Intent(fromActivity, DetailPageActivity.class);
        intent.putExtra("json", json);

        if (backgroundView != null) BitmapUtil.storeBitmapInIntent(BitmapUtil.createBitmap(backgroundView), intent);

        ActivityCompat.startActivity(fromActivity, intent, options.toBundle());

    }

}
