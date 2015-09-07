package me.sweetll.v2ex.Utils;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import me.sweetll.v2ex.R;

/**
 * Created by sweet on 15-9-7.
 */
public class Ui {
    public static int ACTION_BAR_HEIGHT = 50;

    public static void colorStatusBar(Activity activity, int height) {
        View statusBarBgView;
        FrameLayout.LayoutParams lp;
        ViewGroup decorViewGroup = (ViewGroup) activity.getWindow().getDecorView();

        int darkColor = activity.getResources().getColor(R.color.PrimaryDark);

        if (decorViewGroup.getChildCount() > 1 &&
                (statusBarBgView = decorViewGroup.getChildAt(1)) != null &&
                statusBarBgView.getId() == R.id.translucent_view) {
            lp = (FrameLayout.LayoutParams) statusBarBgView.getLayoutParams();
            lp.height = height;
            lp.gravity = Gravity.TOP;
            statusBarBgView.setBackgroundColor(darkColor);
            statusBarBgView.setLayoutParams(lp);
        } else {
            statusBarBgView = new View(activity);
            statusBarBgView.setId(R.id.translucent_view);
            statusBarBgView.setBackgroundColor(darkColor);
            lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
            lp.gravity = Gravity.TOP;
            decorViewGroup.addView(statusBarBgView, lp);
        }
    }
}
