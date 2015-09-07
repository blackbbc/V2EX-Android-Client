package me.sweetll.v2ex;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.sweetll.v2ex.DataStructure.Post;
import me.sweetll.v2ex.Utils.Ui;
import me.sweetll.v2ex.Widget.FitWindowView;

public class DetailActivity extends AppCompatActivity implements FitWindowView.OnFitSystemWindowsListener{

    @Bind(R.id.standard) FitWindowView mStandard;
    @Bind(R.id.scrollView) ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mStandard.addOnFitSystemWindowsListener(this);

//        Post post = Parcels.unwrap((Parcelable)getIntent().getExtras().get("article_data"));

//        setSupportActionBar(toolbar);

//        getSupportActionBar().setTitle(post.getTitle());
//
//        testView.setText(post.getTitle());
    }

    @Override
    public void onFitSystemWindows(int l, int t, int r, int b) {
        scrollView.setPadding(scrollView.getPaddingLeft(), t, scrollView.getPaddingRight(), b);

//        Ui.colorStatusBar(this, t - Ui.ACTION_BAR_HEIGHT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
}
