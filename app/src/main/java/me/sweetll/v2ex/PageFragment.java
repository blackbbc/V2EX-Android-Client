package me.sweetll.v2ex;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.sweetll.v2ex.DataStructure.Post;

/**
 * Created by sweet on 15-5-8.
 */
public class PageFragment extends Fragment {
    @InjectView(R.id.recyclerView) RecyclerView mRecyclerView;
    @InjectView(R.id.swipeRefreshLayout) PullRefreshLayout layout;
    private ArrayAdapter mAdapter;
    private ArrayList<Post> mDataset;
    private String[] urls = {
            "https://v2ex.com/?tab=tech",
            "https://v2ex.com/?tab=creative",
            "https://v2ex.com/?tab=play",
            "https://v2ex.com/?tab=apple",
            "https://v2ex.com/?tab=jobs",
            "https://v2ex.com/?tab=deals",
            "https://v2ex.com/?tab=city",
            "https://v2ex.com/?tab=qna",
            "https://v2ex.com/?tab=hot",
            "https://v2ex.com/?tab=all",
            "https://v2ex.com/?tab=r2"
    };


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ButterKnife.inject(this, view);
        final int position = FragmentPagerItem.getPosition(getArguments());

        mDataset = new ArrayList<Post>();
        mAdapter = new ArrayAdapter(getActivity(), mDataset);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);


        layout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Post> lists = new ArrayList<Post>();

                        try {
                            String url = urls[position];
                            URL baseURL = new URL(url);
                            Document doc = Jsoup.connect(url).timeout(4000).get();

                            Elements cellitems = doc.select("div.cell.item");

                            for (Element cellitem : cellitems) {
                                String[] smallFade = cellitem.select("span.small.fade").text().split("•");
                                String title = cellitem.select("span.item_title").text();
                                String userName = smallFade[1].replace(String.valueOf((char) 160), " ").trim();
                                int spacePosition = userName.indexOf(" ");
                                String time = userName.substring(spacePosition);
                                userName = userName.substring(0, spacePosition);

                                String reply = cellitem.select("tr>td").eq(3).text();
                                reply = reply != "" ? reply : "0";

                                String tag = smallFade[0].replace(String.valueOf((char) 160), " ").trim();
                                String imageSrc = cellitem.select("img.avatar").attr("src");
                                String src = cellitem.select("span.item_title>a").attr("href");
                                URL temp1 = new URL(baseURL, imageSrc);
                                URL temp2 = new URL(baseURL, src);
                                imageSrc = temp1.toString();
                                src = temp2.toString();
                                Post list = new Post(title, userName, time, tag, reply, imageSrc, src);
                                lists.add(list);
                            }

                            mAdapter.setmArray(lists);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                    layout.setRefreshing(false);
                                }
                            });


                        } catch (IOException e) {
                            e.printStackTrace();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    layout.setRefreshing(false);
                                }
                            });
                        }

                    }
                });
                thread.start();

            }
        });

    }

    class ArrayAdapter extends RecyclerView.Adapter<ViewHolder>{

        private ArrayList<Post> mArray;
        private Context mContext;

        public ArrayAdapter(Context context, ArrayList<Post> array) {
            mContext = context;
            mArray = array;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.card, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            viewHolder.title.setText(mArray.get(position).title);
            viewHolder.userName.setText(mArray.get(position).userName);
            viewHolder.time.setText(mArray.get(position).time);
            viewHolder.tag.setText(mArray.get(position).tag);
            viewHolder.reply.setText(mArray.get(position).reply);
            viewHolder.avatar.setImageURI(Uri.parse(mArray.get(position).imageSrc));
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Post list = getItem(position);
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), DetailPageActivity.class);
                    Gson gson = new Gson();
                    String json = gson.toJson(getItem(position));
                    intent.putExtra("json", json);
//                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity());
//                    getActivity().startActivity(intent, options.toBundle());
                    getActivity().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mArray.size();
        }

        public Post getItem(int position) {
            return mArray.get(position);
        }

        public void setmArray (ArrayList<Post> newArray) {
            mArray = newArray;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @InjectView(R.id.title) TextView title;
        @InjectView(R.id.avatar) SimpleDraweeView avatar;
        @InjectView(R.id.userName) TextView userName;
        @InjectView(R.id.time) TextView time;
        @InjectView(R.id.tag) TextView tag;
        @InjectView(R.id.reply) TextView reply;
        @InjectView(R.id.card_view) CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

}
