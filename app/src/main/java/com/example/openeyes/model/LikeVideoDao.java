package com.example.openeyes.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.openeyes.bean.VideoItem;

import java.util.List;

@Dao
public interface LikeVideoDao {

    @Query("SELECT * FROM video_items ORDER BY time DESC")
    List<VideoItem> getAll();

    @Query("SELECT * FROM video_items WHERE video_id IS :videoId")
    VideoItem query(int videoId);

    @Insert
    void insertAll(VideoItem... items);

    @Insert
    void insertList(List<VideoItem> items);

    @Delete
    void delete(VideoItem item);

}
