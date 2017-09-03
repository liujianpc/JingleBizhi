package com.example.jingle.jinglebizhi;

import java.util.List;

/**
 * Created by liujian on 2017/9/3.
 */

public class DataTop {
    boolean error;
    List<Model> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Model> getResults() {
        return results;
    }

    public void setResults(List<Model> results) {
        this.results = results;
    }
}
