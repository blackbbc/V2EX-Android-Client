package com.example.sweet.myapplication;

/**
 * Created by sweet on 15-3-26.
 */
public class ReplyDetailStruct extends PostListStruct {
    public String content="";
    public String floor="";

    public ReplyDetailStruct(String username, String time, String imageSrc, String mContent, String mFloor) {
        super(username, time, imageSrc);
        setContent(mContent);
        setFloor(mFloor);
    }

    public void setContent(String a) {
        content = a;
    }

    public void setFloor(String a) {
        floor = a;
    };
}