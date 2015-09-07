package me.sweetll.v2ex.DataStructure;

/**
 * Created by sweet on 15-5-9.
 */
public class Reply {
    String imageSrc = "";
    String author = "";
    String time = "";
    String content = "";

    public Reply() {

    }

    public Reply(String imageSrc, String author, String time, String content) {
        this.imageSrc = imageSrc;
        this.author = author;
        this.time = time;
        this.content = content;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
