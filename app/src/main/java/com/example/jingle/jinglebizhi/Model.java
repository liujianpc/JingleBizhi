package com.example.jingle.jinglebizhi;

/**
 * Created by liujian on 2017/9/3.
 */

class Model {
    String url;
    String who;

    public Model(String url, String who) {
        this.url = url;
        this.who = who;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
