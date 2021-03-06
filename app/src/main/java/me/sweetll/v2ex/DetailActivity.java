package me.sweetll.v2ex;

import android.app.SharedElementCallback;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orhanobut.logger.Logger;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.parceler.Parcels;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import me.sweetll.v2ex.Adapter.ArticleDetailRecyclerViewAdapter;
import me.sweetll.v2ex.DataStructure.Content;
import me.sweetll.v2ex.DataStructure.Post;
import me.sweetll.v2ex.DataStructure.Reply;
import me.sweetll.v2ex.Utils.GlobalClass;
import me.sweetll.v2ex.Widget.FitWindowView;

public class DetailActivity extends AppCompatActivity implements FitWindowView.OnFitSystemWindowsListener{
    @Bind(R.id.standard) FitWindowView mStandard;
    @Bind(R.id.article_detail) RecyclerView detailRecyclerView;

    private StringRequest stringRequest;
    private ArticleDetailRecyclerViewAdapter detailRecyclerViewAdapter;
    private String url;

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

    private Transition makeEnterTransition() {
        TransitionSet enterTransition = new TransitionSet();

        // Slide the cards in through the top of the screen.
        Transition cardSlideBottom = new Slide(Gravity.TOP);
        cardSlideBottom.addTarget(findViewById(R.id.article_detail));
        enterTransition.addTransition(cardSlideBottom);

        enterTransition.setDuration(getResources().getInteger(R.integer.transition_duration_millis));
        return enterTransition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setEnterSharedElementCallback(mCallback);

        mStandard.addOnFitSystemWindowsListener(this);

        Post post = Parcels.unwrap((Parcelable)getIntent().getExtras().get("article_data"));
        getSupportActionBar().setTitle(post.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        detailRecyclerViewAdapter = new ArticleDetailRecyclerViewAdapter();
        detailRecyclerViewAdapter.add(post);
        detailRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .build());
        detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailRecyclerView.setAdapter(detailRecyclerViewAdapter);
        detailRecyclerView.setItemAnimator(new FadeInUpAnimator());

        postponeEnterTransition();
        getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                GlobalClass.getQueue().add(stringRequest);
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

        url = post.getSrc();
        stringRequest = new StringRequest(Request.Method.GET, url,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);

                String detail_ps;
                String detail_body;

                //首先分析content
                detail_body = document.select("div.topic_content").html();
                if (!detail_body.isEmpty()) {
                    Content body = new Content(detail_body);
                    detailRecyclerViewAdapter.add(body);
                }
                //然后分析附言
                Elements subtles = document.select("div.subtle");
                for (Element subtle : subtles) {
                    detail_ps = subtle.select("span.fade").text();
                    detail_body = subtle.select("div.topic_content").text();
                    Content newContent = new Content(detail_ps, detail_body);
                    detailRecyclerViewAdapter.add(newContent);
                }

                //最后分析reply

                String reply_author;
                String reply_time;
                String reply_content;
                String reply_imageSrc;

                Elements cells = document.select("div.box").eq(1);
                cells = cells.select("div.box>div");
                cells.remove(0);
                for (Element cell : cells) {
                    try {
                        reply_author = cell.select("a").first().text();
                        reply_time = cell.select("span.fade.small").first().text();
                        reply_imageSrc = cell.select("img").first().attr("src");
                        reply_content = cell.select("div.reply_content").first().text();
                    } catch (NullPointerException e) {
                        continue;
                    }

                    try {
                        reply_imageSrc = new URL(new URL(url), reply_imageSrc).toString();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    detailRecyclerViewAdapter.add(new Reply(reply_imageSrc, reply_author, reply_time, reply_content));
                }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.d("Volley Error");
            }
        });

    }

    @Override
    public void onFitSystemWindows(int l, int t, int r, int b) {
        detailRecyclerView.setPadding(detailRecyclerView.getPaddingLeft(), t, detailRecyclerView.getPaddingRight(), b);

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
        onBackPressed();

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
