package com.example.jingle.jinglebizhi;

import java.io.Serializable;

/**
 * Created by liujian on 2017/9/3.
 */

class Model implements Serializable {
    String fileName;//壁纸名  .jpg
    String previewFileName;//壁纸多略图 small-.jpg
    String name;//壁纸介绍
    int downloadCount;//壁纸下载次数

    public Model(String fileName, String previewFileName, String name, int downloadCount) {
        this.fileName = fileName;
        this.previewFileName = previewFileName;
        this.name = name;
        this.downloadCount = downloadCount;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPreviewFileName() {
        return previewFileName;
    }

    public void setPreviewFileName(String previewFileName) {
        this.previewFileName = previewFileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getSmallFilePath() {
        String fileId = fileName.split("\\.")[0];
        String smallFilePath = "http://hitopdl.hicloud.com/dl/hitopdl//hitop/wallpaper/FHD/" + fileId + "/" + previewFileName;
        return smallFilePath;
    }

    public String getBigFilePath() {
        String fileId = fileName.split("\\.")[0];
        String bigFilePath = "http://hitopdl.hicloud.com/dl/hitopdl//hitop/wallpaper/FHD/" + fileId + "/" + fileName;
        return bigFilePath;
    }


}
