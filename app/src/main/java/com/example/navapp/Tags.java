package com.example.navapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class Tags {

    @PrimaryKey@NonNull
    private String tag_name;





    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    @Ignore
    public Tags(String tag_name) {
        this.tag_name = tag_name;
    }

       public Tags() { }

}
