package me.sweetll.v2ex.Adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.sweetll.v2ex.DataStructure.Content;
import me.sweetll.v2ex.DataStructure.Post;
import me.sweetll.v2ex.DataStructure.Reply;
import me.sweetll.v2ex.DetailActivity;
import me.sweetll.v2ex.R;

/**
 * Created by sweet on 15-8-17.
 */
public class ArticleDetailRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Object> mData;
    Context context;

    public static enum ITEM_TYPE {
        TYPE_CONTENT,
        TYPE_DETAIL,
        TYPE_REPLY
    }

    ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(
                String id,
                @Nullable ImageInfo imageInfo,
                @Nullable Animatable anim) {
            if (imageInfo == null) {
                return;
            }

            ((DetailActivity)context).startPostponedEnterTransition();
        }

        @Override
        public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
//            FLog.d("Intermediate image received");
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
//            FLog.e(getClass(), throwable, "Error loading %s", id);
        }
    };

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.content_ps) TextView contentPs;
        @Bind(R.id.content_body) TextView contentBody;
        public ContentViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public static class ReplyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.reply_author) TextView replyAuthor;
        @Bind(R.id.reply_time) TextView replyTime;
        @Bind(R.id.reply_content) TextView replyContent;
        @Bind(R.id.reply_avatar) SimpleDraweeView replyAvatar;
        public ReplyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.detail_title) TextView detailTitle;
        @Bind(R.id.detail_author) TextView detailAuthor;
        @Bind(R.id.detail_time) TextView detailTime;
        @Bind(R.id.detail_avatar) SimpleDraweeView detailAvatar;
        public DetailViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }


    public ArticleDetailRecyclerViewAdapter()  {
        mData = new ArrayList<>();
    }

    public void add(Object newObject) {
        mData.add(newObject);
    }

    public void clear() {
        mData.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        if (viewType == ITEM_TYPE.TYPE_CONTENT.ordinal()) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_content, parent, false);
            return new ContentViewHolder(v);
        } else if (viewType == ITEM_TYPE.TYPE_REPLY.ordinal()) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_reply, parent, false);
            return new ReplyViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_detail, parent, false);
            return new DetailViewHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DetailViewHolder) {
            Post post = (Post)mData.get(position);
            ((DetailViewHolder) holder).detailAuthor.setText(post.getUserName());

            Uri uri = Uri.parse(post.getImageSrc());
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setUri(uri)
                    .build();
            ((DetailViewHolder) holder).detailAvatar.setController(controller);
            ((DetailViewHolder) holder).detailAvatar.setTransitionName(post.getTitle() + post.getUserName());
            ((DetailViewHolder) holder).detailAvatar.setTag(post.getTitle() + post.getUserName());

            ((DetailViewHolder) holder).detailTime.setText(post.getTime());
            ((DetailViewHolder) holder).detailTitle.setText(post.getTitle());
        } else if(holder instanceof ContentViewHolder) {
            Content content = (Content)mData.get(position);
            if (content.getPs().isEmpty()) {
                ((ContentViewHolder) holder).contentPs.setVisibility(View.GONE);
            } else {
                ((ContentViewHolder) holder).contentPs.setText(content.getPs());
            }
            ((ContentViewHolder) holder).contentBody.setText(Html.fromHtml(content.getBody()));
        } else if (holder instanceof ReplyViewHolder) {
            Reply reply = (Reply)mData.get(position);
            ((ReplyViewHolder) holder).replyAuthor.setText(reply.getAuthor());
            ((ReplyViewHolder) holder).replyAvatar.setImageURI(Uri.parse(reply.getImageSrc()));
            ((ReplyViewHolder) holder).replyContent.setText(reply.getContent());
            ((ReplyViewHolder) holder).replyTime.setText(reply.getTime());
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object object = mData.get(position);
        if (object instanceof Post) {
            return ITEM_TYPE.TYPE_DETAIL.ordinal();
        } else if (object instanceof Content) {
            return ITEM_TYPE.TYPE_CONTENT.ordinal();
        } else {
            return ITEM_TYPE.TYPE_REPLY.ordinal();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
