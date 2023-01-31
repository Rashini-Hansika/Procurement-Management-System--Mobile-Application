package com.example.travelbee.models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class ToDo implements Serializable {
   @Exclude
    private String key;

    String title;

    String subtitle;
    String desc;

    public ToDo() {
    }

    public ToDo(String title, String subtitle, String desc) {

        this.title=title;
        this.subtitle=subtitle;
        this.desc=desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
