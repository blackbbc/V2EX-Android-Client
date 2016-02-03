package me.sweetll.v2ex;

import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.library.CrashWoodpecker;
import me.sweetll.v2ex.Adapter.ArticleListFragmentAdapter;
import me.sweetll.v2ex.Fragment.ArticleListFragment;
import me.sweetll.v2ex.Utils.GlobalClass;

public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.viewpager) ViewPager viewPager;
    @Bind(R.id.tabs) PagerSlidingTabStrip tabStrip;
    @Bind(R.id.appbar_layout) AppBarLayout appBarLayout;
    @Bind(R.id.search_view) ImageView searchView;
    @Bind(R.id.search_edit_text) EditText searchEditText;
    @Bind(R.id.nvView) NavigationView navigationView;
    private AnimatedVectorDrawable searchToBar;
    private AnimatedVectorDrawable barToSearch;
    private ActionBarDrawerToggle drawerToggle;
    private ArticleListFragmentAdapter viewPagerAdapter;

    //For Search View
    private float offset;
    private Interpolator interpolator;
    private int duration;
    private boolean expanded = false;

    private SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
            super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
        }

        @Override
        public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
            super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
        }

        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            super.onMapSharedElements(names, sharedElements);
            Logger.d("onMapSharedElements");
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        CrashWoodpecker.fly().to(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setExitSharedElementCallback(mCallback);

        GlobalClass.Initialize(getApplicationContext());

        setSupportActionBar(toolbar);
        drawerToggle = setupDrawerToggle();
        drawerLayout.setDrawerListener(drawerToggle);

        viewPagerAdapter = new ArticleListFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(11);
        tabStrip.setViewPager(viewPager);

        View headerView = navigationView.getHeaderView(0);
        CircleImageView circleImageView = (CircleImageView) headerView.findViewById(R.id.circle_image_view);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, GlobalClass.REQUEST_SIGN_IN);
            }
        });

        initSearchView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GlobalClass.REQUEST_SIGN_IN:
                switch (resultCode) {
                    case GlobalClass.RESULT_SUCCESS:
                        break;
                    case GlobalClass.RESULT_FAILURE:
                        break;
                }
                break;
            case GlobalClass.REQUEST_SIGN_UP:
                break;
        }
    }

    void initSearchView() {
        searchToBar = (AnimatedVectorDrawable) getDrawable(R.drawable.anim_search_to_bar);
        barToSearch = (AnimatedVectorDrawable) getDrawable(R.drawable.anim_bar_to_search);

        interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in);
        duration = 700;

        offset = 0f * (int) getResources().getDisplayMetrics().scaledDensity;
        searchView.setTranslationX(offset);
    }

    @OnClick(R.id.search_view)
    void animate() {
        if (!expanded) {
            searchView.setImageDrawable(searchToBar);
            searchToBar.start();
            searchView.animate().translationX(0f).setDuration(duration).setInterpolator(interpolator);
            searchEditText.animate().alpha(1f).setStartDelay(duration - 100).setDuration(100).setInterpolator(interpolator);
            searchEditText.setVisibility(View.VISIBLE);
            searchEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
        } else {
            searchView.setImageDrawable(barToSearch);
            barToSearch.start();
            searchView.animate().translationX(offset).setDuration(duration).setInterpolator(interpolator);
            searchEditText.setAlpha(0f);
            searchEditText.setVisibility(View.INVISIBLE);
        }
        expanded = !expanded;
    }

    @Override
    public void onBackPressed() {
        if (expanded) {
            animate();
        } else {
            this.finish();
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,  R.string.drawer_close);
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
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityReenter(int requestCode, Intent data) {
        super.onActivityReenter(requestCode, data);
        Logger.d("onActivityReenter");
    }

    //Fix CoordinatorLayout and SwipeRefreshLayout conflict

    @Override
    public void onStart() {
        super.onStart();
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onStop() {
        appBarLayout.removeOnOffsetChangedListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        viewPagerAdapter.destroy();
        super.onDestroy();
    }

    int index = 0;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) { index = i;}

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                ArticleListFragment pageFragment = viewPagerAdapter.getFragment(viewPager.getCurrentItem());
                if (pageFragment != null) {
                    if (index == 0) {
                        pageFragment.setSwipeToRefreshEnabled(true);
                    } else {
                        pageFragment.setSwipeToRefreshEnabled(false);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
