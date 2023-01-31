package com.example.travelbee.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Memories implements Parcelable {
    private String imageUrl;
    private String title;
    private String location;
    private String description;
    private String date;
    private String keyMemories;

    public Memories(){

    }

    public Memories(String imageUrl, String title, String location, String description, String date, String keyMemories) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.location = location;
        this.description = description;
        this.date = date;
        this.keyMemories = keyMemories;
    }

    public Memories(Parcel in) {
    }

    public String getKeyMemories() {
        return keyMemories;
    }

    public void setKeyMemories(String keyMemories) {
        this.keyMemories = keyMemories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.location);
        dest.writeString(this.date);
        dest.writeString(this.description);
        dest.writeString(this.imageUrl);

    }

    public static final Creator<Memories> CREATOR = new Creator<Memories>() {
        @Override
        public Memories createFromParcel(Parcel in) {
            return new Memories(in);
        }

        @Override
        public Memories[] newArray(int size) {
            return new Memories[size];
        }
    };
}