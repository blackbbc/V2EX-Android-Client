package me.sweetll.v2ex;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.sweetll.v2ex.DataStructure.Detail;
import me.sweetll.v2ex.Utils.PicassoImageGetter;
import me.sweetll.v2ex.Utils.URLImageParser;

/**
 * Created by sweet on 15-5-13.
 */
public class UlRecyclerviewAdapter extends UltimateViewAdapter {

    private Context mContext;
    private List<Detail> mArray;

    public UlRecyclerviewAdapter(Context context, List<Detail> array) {
        this.mContext = context;
        mArray = array;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= mArray.size() : position < mArray.size()) && (customHeaderView != null ? position > 0 : true)) {
            ((ViewHolder) viewHolder).userName.setText(mArray.get(customHeaderView != null ? position - 1 : position).userName);
            ((ViewHolder) viewHolder).time.setText(mArray.get(customHeaderView != null ? position - 1 : position).time);
            ((ViewHolder) viewHolder).floor.setText(mArray.get(customHeaderView != null ? position - 1 : position).floor);
//            ((ViewHolder) viewHolder).content.setHtmlFromString(mArray.get(customHeaderView != null ? position - 1 : position).content, false);
            TextView textView= ((ViewHolder) viewHolder).content;
//            URLImageParser p = new URLImageParser(textView, mContext);
            PicassoImageGetter p = new PicassoImageGetter(textView, mContext.getResources(), Picasso.with(mContext));
            Spanned htmlSpan = Html.fromHtml(mArray.get(customHeaderView != null ? position - 1 : position).content, p, null);
            ((ViewHolder) viewHolder).content.setText(htmlSpan);
            ((ViewHolder) viewHolder).avatar.setImageURI(Uri.parse(mArray.get(customHeaderView != null ? position - 1 : position).imageSrc));
        }
    }

    @Override
    public int getAdapterItemCount() {return mArray.size(); }

    public void setData(List<Detail> data) {
        mArray = data;
    }

    public void addDataAll(List<Detail> data) {
        mArray.addAll(data);
    }

    public void addData(Detail data) {
        mArray.add(data);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ultimate_recycler_view_adapter, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    class ViewHolder extends UltimateRecyclerviewViewHolder {
        @Bind(R.id.detail_userName) TextView userName;
        @Bind(R.id.detail_time) TextView time;
        @Bind(R.id.detail_floor) TextView floor;
        @Bind(R.id.detail_content) TextView content;
        @Bind(R.id.detail_avatar) SimpleDraweeView avatar;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }



}
