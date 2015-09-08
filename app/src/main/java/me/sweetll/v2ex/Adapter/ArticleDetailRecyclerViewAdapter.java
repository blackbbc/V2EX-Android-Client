package me.sweetll.v2ex.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.sweetll.v2ex.DataStructure.Detail;
import me.sweetll.v2ex.DataStructure.Post;
import me.sweetll.v2ex.DetailActivity;
import me.sweetll.v2ex.R;

/**
 * Created by sweet on 15-8-17.
 */
public class ArticleDetailRecyclerViewAdapter extends RecyclerView.Adapter<ArticleDetailRecyclerViewAdapter.ViewHolder> {
    ArrayList<Detail> mData;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.content_ps) TextView contentPs;
        @Bind(R.id.content_body) TextView contentBody;
        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public ArticleDetailRecyclerViewAdapter()  {
        mData = new ArrayList<>();
        Detail detail1 = new Detail("aaa", "bbb");
        Detail detail2 = new Detail("ccc", "ddd");
        Detail detail3 = new Detail("eee", "fff");
        add(detail1);
        add(detail2);
        add(detail3);
    }

    public void add(Detail newDetail) {
        mData.add(newDetail);
    }

    public void clear() {
        mData.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_content, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position > 0) {
            holder.contentPs.setText(mData.get(position).getPs());
        } else {
            holder.contentPs.setVisibility(View.GONE);
        }
        holder.contentBody.setText(mData.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
