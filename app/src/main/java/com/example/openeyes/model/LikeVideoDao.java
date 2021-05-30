package com.example.openeyes.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.openeyes.bean.VideoItem;

import java.util.List;

@Dao
public interface LikeVideoDao {

    @Query("SELECT * FROM video_items WHERE user_count IS :count AND operation = 1 ORDER BY like_time DESC")
    List<VideoItem> getAll(String count);

    @Query("SELECT * FROM video_items WHERE video_id IS :videoId AND user_count IS :count AND operation = 1")
    VideoItem queryStatus(int videoId, String count);

    @Insert
    void insert(VideoItem... items);

    @Insert
    void insertList(List<VideoItem> items);

    @Query("DELETE FROM video_items WHERE video_id = :videoId AND user_count IS :count AND operation = 1")
    void delete(int videoId, String count);

}
