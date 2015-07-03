package me.sweetll.v2ex;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.sweetll.v2ex.Utils.Navigator;

public class MainActivity extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    @InjectView(R.id.base_fragment_background) View background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        Fresco.initialize(this);

        initTransition();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("技术", PageFragment.class)
                        .add("创意", PageFragment.class)
                        .add("好玩", PageFragment.class)
                        .add("Apple", PageFragment.class)
                        .add("酷工作", PageFragment.class)
                        .add("交易", PageFragment.class)
                        .add("城市", PageFragment.class)
                        .add("问与答", PageFragment.class)
                        .add("最热", PageFragment.class)
                        .add("全部", PageFragment.class)
                        .add("R2", PageFragment.class)
                        .create());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(10);

        SmartTabLayout viewPagerTab = (SmartTabLayout)findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initTransition()
    {
        Transition transition = new Fade();
        transition.setDuration(Navigator.ANIM_DURATION);

        getWindow().setEnterTransition(transition);
        getWindow().setExitTransition(transition);


        getWindow().getSharedElementEnterTransition().setDuration(Navigator.ANIM_DURATION);
        getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                if (background.getScaleX() == 1) {
                    background.animate().scaleX(.92f).scaleY(.92f).alpha(.6f).setDuration(Navigator.ANIM_DURATION).setInterpolator(new AccelerateInterpolator()).start();
                } else {
                    background.animate().scaleX(1).scaleY(1).alpha(1).translationY(0).setDuration(Navigator.ANIM_DURATION).setInterpolator(new DecelerateInterpolator()).start();
                }

            }

            @Override
            public void onTransitionEnd(Transition transition) {

            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }
}
