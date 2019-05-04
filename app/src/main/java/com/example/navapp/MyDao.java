package com.example.navapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface MyDao {

    @Insert
    public void addtag(Tags tags);

    @Query("select * from tags")
    public List<Tags> getTags();

    @Delete
    public void deleteTag(Tags tags);
}
