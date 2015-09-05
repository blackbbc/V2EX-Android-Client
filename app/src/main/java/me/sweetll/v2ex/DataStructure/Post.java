package me.sweetll.v2ex.DataStructure;

import org.parceler.Parcel;

/**
 * Created by sweet on 15-5-9.
 */

@Parcel
public class Post {
    String title = "";
    String userName = "";
    String time = "";
    String tag = "";
    String reply = "";
    String imageSrc = "";
    String src = "";

    public Post(){}

    public Post(String title, String userName, String time, String tag, String reply, String imageSrc, String src) {
        this.title = title;
        this.userName = userName;
        this.time = time;
        this.tag = tag;
        this.reply = reply;
        this.imageSrc = imageSrc;
        this.src = src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
