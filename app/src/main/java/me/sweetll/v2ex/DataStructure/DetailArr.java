package me.sweetll.v2ex.DataStructure;

import java.util.ArrayList;

/**
 * Created by sweet on 15-9-8.
 */
public class DetailArr {
    ArrayList<Object> mData;

    public DetailArr() {
        mData = new ArrayList<>();
    }

    public void add(Object data) {
        mData.add(data);
    }

    public Post getPostAt(int position) {
        return (Post)mData.get(position);
    }

    public Content getContentAt(int position) {
        return (Content)mData.get(position);
    }

    public Reply getReplyAt(int position) {
        return (Reply)mData.get(position);
    }

}
