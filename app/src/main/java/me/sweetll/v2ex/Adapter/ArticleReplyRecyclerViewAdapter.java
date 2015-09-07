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
import me.sweetll.v2ex.DataStructure.Post;
import me.sweetll.v2ex.DataStructure.Reply;
import me.sweetll.v2ex.DetailActivity;
import me.sweetll.v2ex.R;

/**
 * Created by sweet on 15-8-17.
 */
public class ArticleReplyRecyclerViewAdapter extends RecyclerView.Adapter<ArticleReplyRecyclerViewAdapter.ViewHolder> {
    ArrayList<Reply> mData;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.reply_author) TextView replyAuthor;
        @Bind(R.id.reply_time) TextView replyTime;
        @Bind(R.id.reply_content) TextView replyContent;
        @Bind(R.id.reply_avatar) SimpleDraweeView replyAvatar;
        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public ArticleReplyRecyclerViewAdapter()  {
        mData = new ArrayList<>();
    }

    public void add(Reply newReply) {
        mData.add(newReply);
    }

    public void clear() {
        mData.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_reply, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.replyAuthor.setText(mData.get(position).getAuthor());
        holder.replyTime.setText(mData.get(position).getTime());
        holder.replyContent.setText(mData.get(position).getContent());
        holder.replyAvatar.setImageURI(Uri.parse(mData.get(position).getImageSrc()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
