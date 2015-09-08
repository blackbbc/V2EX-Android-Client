package me.sweetll.v2ex.DataStructure;

import java.util.ArrayList;

/**
 * Created by sweet on 15-5-9.
 */
public class Content {

    String ps = "";
    String body = "";

    public Content() {

    }

    public Content(String body) {
        this.body = body;
    }

    public Content(String ps, String body) {
        this.ps = ps;
        this.body = body;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
