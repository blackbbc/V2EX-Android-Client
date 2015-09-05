package me.sweetll.v2ex.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.sweetll.v2ex.DataStructure.Post;
import me.sweetll.v2ex.R;

/**
 * Created by sweet on 15-8-17.
 */
public class ArticleListRecyclerViewAdapter extends RecyclerView.Adapter<ArticleListRecyclerViewAdapter.ViewHolder> {
    ArrayList<Post> mData;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.list_card) CardView listCard;
        @Bind(R.id.list_title) TextView listTitle;
        @Bind(R.id.list_author) TextView listAuthor;
        @Bind(R.id.list_post_time) TextView listTime;
        @Bind(R.id.list_node) TextView listNode;
        @Bind(R.id.list_reply) TextView listReply;
        @Bind(R.id.list_avatar) SimpleDraweeView listAvatar;
        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public ArticleListRecyclerViewAdapter()  {
        mData = new ArrayList<>();
    }

    public void add(Post newPost) {
        mData.add(newPost);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.listTitle.setText(mData.get(position).getTitle());
        holder.listAuthor.setText(mData.get(position).getUserName());
        holder.listTime.setText(mData.get(position).getTime());
        holder.listNode.setText(mData.get(position).getTag());
        holder.listReply.setText(mData.get(position).getReply());
        holder.listAvatar.setImageURI(Uri.parse(mData.get(position).getImageSrc()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
