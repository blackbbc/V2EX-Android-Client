package me.sweetll.v2ex.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
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
import me.sweetll.v2ex.MainActivity;
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
        Post post = mData.get(position);

        holder.listTitle.setText(post.getTitle());
        holder.listAuthor.setText(post.getUserName());
        holder.listTime.setText(post.getTime());
        holder.listNode.setText(post.getTag());
        holder.listReply.setText(post.getReply());

        holder.listAvatar.setImageURI(Uri.parse(post.getImageSrc()));
        holder.listAvatar.setTransitionName(post.getTitle() + post.getUserName());
        holder.listAvatar.setTag(post.getTitle() + post.getUserName());

        holder.listCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Parcelable articleData = Parcels.wrap(mData.get(position));
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("article_data", articleData);
                intent.putExtra("position", position);
                MainActivity mainActivity = (MainActivity) context;
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                        mainActivity, holder.listAvatar, holder.listAvatar.getTransitionName()).toBundle()
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
