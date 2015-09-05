package me.sweetll.v2ex.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.sweetll.v2ex.R;

/**
 * Created by sweet on 15-8-17.
 */
public class ArticleListRecyclerViewAdapter extends RecyclerView.Adapter<ArticleListRecyclerViewAdapter.ViewHolder> {
    private String[] titles;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.list_card) CardView listCard;
        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public ArticleListRecyclerViewAdapter(String[] dataSet)  {
        titles = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.textView.setText(titles[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
