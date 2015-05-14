package me.sweetll.v2ex;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.sweetll.v2ex.DataStructure.Detail;

/**
 * Created by sweet on 15-5-13.
 */
public class UlRecyclerviewAdapter extends UltimateViewAdapter {

    private Context mContext;
    private List<Detail> mArray;

    public UlRecyclerviewAdapter(Context context, List<Detail> array) {
        mContext = context;
        mArray = array;
    }

    public void setData(List<Detail> data) {
        mArray = data;
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.ultimate_recycler_view_adapter, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        ((ViewHolder)viewHolder).userName.setText(mArray.get(position).userName);
        ((ViewHolder)viewHolder).time.setText(mArray.get(position).time);
        ((ViewHolder)viewHolder).floor.setText(mArray.get(position).floor);
        ((ViewHolder)viewHolder).content.setText(mArray.get(position).content);
        ((ViewHolder)viewHolder).avatar.setImageURI(Uri.parse(mArray.get(position).imageSrc));
    }

    @Override
    public int getItemCount() {
        return mArray.size();
    }

    @Override
    public int getAdapterItemCount() {return mArray.size(); }

    public Detail getItem(int position) {
        return mArray.get(position);
    }

    class ViewHolder extends UltimateRecyclerviewViewHolder {
        @InjectView(R.id.detail_userName) TextView userName;
        @InjectView(R.id.detail_time) TextView time;
        @InjectView(R.id.detail_floor) TextView floor;
        @InjectView(R.id.detail_content) TextView content;
        @InjectView(R.id.detail_avatar) SimpleDraweeView avatar;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

}
