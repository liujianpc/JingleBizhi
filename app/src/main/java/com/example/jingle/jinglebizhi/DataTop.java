package com.example.jingle.jinglebizhi;

import java.util.List;

/**
 * Created by liujian on 2017/9/3.
 */

public class DataTop {
    String fileHost;
    List<Model> list;

    public DataTop(String fileHost, List<Model> list) {
        this.fileHost = fileHost;
        this.list = list;
    }

    public String getFileHost() {
        return fileHost;
    }

    public void setFileHost(String fileHost) {
        this.fileHost = fileHost;
    }

    public List<Model> getList() {
        return list;
    }

    public void setList(List<Model> list) {
        this.list = list;
    }
}
