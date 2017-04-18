package com.kuk.myreceipt.model;

import java.io.Serializable;

/**
 * Created by KUK on 9/4/2560.
 */

public class ReceiptModel implements Serializable {
    int rid;
    String title;
    String dateTime;
    String filePath;
    int price;
    String description;
    String thumbnailPath;




    public ReceiptModel(int rid, String title, String dateTime, String filePath, int price, String description, String thumbnailPath) {
        this.rid = rid;
        this.title = title;
        this.dateTime = dateTime;
        this.filePath = filePath;
        this.price = price;
        this.description = description;
        this.thumbnailPath = thumbnailPath;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}
