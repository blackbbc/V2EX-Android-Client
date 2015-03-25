package com.example.sweet.myapplication;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sweet on 15-3-24.
 */
public class Refresh {
    static public ArrayList<PostListStruct> RefreshList (PullRefreshLayout layout) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


            }
        });

        thread.start();

        ArrayList<PostListStruct> lists = new ArrayList<PostListStruct>();

        try {
            String url = "https://v2ex.com/?tab=hot";
            URL baseURL = new URL(url);
            Document doc = Jsoup.connect(url).get();

            Elements cellitems = doc.select("div.cell.item");

            for (Element cellitem : cellitems) {
                String[] smallFade = cellitem.select("span.small.fade").text().split("•");
                String a = cellitem.select("span.item_title").text();
                String b = smallFade[1].replace(String.valueOf((char) 160), " ").trim();
                String c = smallFade[2].replace(String.valueOf((char) 160), " ").trim();
                String d = smallFade[0].replace(String.valueOf((char) 160), " ").trim();
                String e = cellitem.select("img.avatar").attr("src");
                String f = cellitem.select("span.item_title>a").attr("href");
                URL temp1 = new URL(baseURL, e);
                URL temp2 = new URL(baseURL, f);
                e = temp1.toString();
                f = temp2.toString();
                PostListStruct list = new PostListStruct(a, b, c, d, e, f);
                lists.add(list);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lists;

    }

}
