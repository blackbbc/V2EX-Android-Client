package com.example.sweet.myapplication;

/**
 * Created by sweet on 15-3-24.
 */
public class PostListStruct {
    public String title="";
    public String username="";
    public String time="";
    public String tag="";
    public String imageSrc="";
    public String src="";

    public PostListStruct() {}

    public PostListStruct(String a, String b, String c, String d, String e, String f) {
        title = a;
        username = b;
        time = c;
        tag = d;
        imageSrc = e;
        src = f;
    }

    public void setImageSrc(String src) {
        imageSrc = src;
    }
}
