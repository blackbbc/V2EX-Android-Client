package com.example.sweet.myapplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sweet on 15-3-24.
 */
public class Refresh {
    static public ArrayList<PostListStruct> RefreshList () {
        ArrayList<PostListStruct> lists = new ArrayList<PostListStruct>();

        try {
            Document doc = Jsoup.connect("https://v2ex.com/?tab=hot").get();

            Elements cellitems = doc.select("div.cell.item");

            for (Element cellitem : cellitems) {
                String a = cellitem.select("span.item_title").text();
                String b = "";
                String c = "";
                String d = "";
                String e = "";
                String f = cellitem.select("span.item_title>a").attr("href");
                PostListStruct list = new PostListStruct(a, b, c, d, e, f);
                lists.add(list);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return lists;

    }
}
