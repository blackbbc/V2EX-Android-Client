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
import me.sweetll.v2ex.DetailActivity;
import me.sweetll.v2ex.R;

/**
 * Created by sweet on 15-8-17.
 */
public class ArticleListRecyclerViewAdapter extends RecyclerView.Adapter<ArticleListRecyclerViewAdapter.ViewHolder> {
    ArrayList<Post> mData;
    Context context;

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

    public void clear() {
        mData.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.listTitle.setText(mData.get(position).getTitle());
        holder.listAuthor.setText(mData.get(position).getUserName());
        holder.listTime.setText(mData.get(position).getTime());
        holder.listNode.setText(mData.get(position).getTag());
        holder.listReply.setText(mData.get(position).getReply());
        holder.listAvatar.setImageURI(Uri.parse(mData.get(position).getImageSrc()));
        holder.listCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Parcelable articleData = Parcels.wrap(mData.get(position));
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("article_data", articleData);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
