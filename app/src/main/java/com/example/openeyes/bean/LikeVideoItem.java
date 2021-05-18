package com.example.openeyes.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "liked_video")
public class LikeVideoItem {
    @ColumnInfo(name = "image_url")
    public String imageUrl;

    @ColumnInfo(name = "head_icon_url")
    public String headIconUrl;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "author_name")
    public String authorName;

    @ColumnInfo(name = "tag")
    public String tag;

    @ColumnInfo(name = "play_url")
    public String playUrl;

    @ColumnInfo(name = "video_description")
    public String videoDescription;

    @ColumnInfo(name = "author_description")
    public String authorDescription;

    @ColumnInfo(name = "background_url")
    public String backgroundUrl;

    @ColumnInfo(name = "like_count")
    public int likeCount;

    @ColumnInfo(name = "share_count")
    public int shareCount;


}
