package me.sweetll.v2ex.DataStructure;

/**
 * Created by sweet on 15-5-9.
 */
public class Post {
    public String title = "";
    public String userName = "";
    public String time = "";
    public String tag = "";
    public String reply = "";
    public String imageSrc = "";
    public String src = "";

    public Post(String title, String userName, String time, String tag, String reply, String imageSrc, String src) {
        this.title = title;
        this.userName = userName;
        this.time = time;
        this.tag = tag;
        this.reply = reply;
        this.imageSrc = imageSrc;
        this.src = src;
    }

}
