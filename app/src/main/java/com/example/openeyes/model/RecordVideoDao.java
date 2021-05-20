package com.example.openeyes.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.openeyes.bean.VideoItem;

import java.util.List;

@Dao
public interface RecordVideoDao {

    @Query("SELECT * FROM video_items WHERE operation = 0 AND user_count IS :count")
    List<VideoItem> getAll(String count);

    @Insert
    void insert(VideoItem item);
}
