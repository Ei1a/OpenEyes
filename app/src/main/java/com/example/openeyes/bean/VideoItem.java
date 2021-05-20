package com.example.openeyes.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "video_items")
public class VideoItem {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "video_id")
    private int videoId;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "head_icon_url")
    private String headIconUrl;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "author_name")
    private String authorName;

    @ColumnInfo(name = "tag")
    private String tag;

    @ColumnInfo(name = "play_url")
    private String playUrl;

    @ColumnInfo(name = "video_description")
    private String videoDescription;

    @ColumnInfo(name = "author_description")
    private String authorDescription;

    @ColumnInfo(name = "background_url")
    private String backgroundUrl;

    @ColumnInfo(name = "like_count")
    private int likeCounts;

    @ColumnInfo(name = "share_count")
    private int shareCounts;

    @ColumnInfo(name = "time")
    private long time;

    @ColumnInfo(name = "user_count")
    private String userCount;

    /*
     * 0 = 历史记录
     * 1 = 收藏
     */
    @ColumnInfo(name = "operation")
    private int operation;

    public VideoItem(int videoId, String imageUrl, String headIconUrl, String title, String authorName, String tag, String playUrl, String videoDescription, String authorDescription, String backgroundUrl, int likeCounts, int shareCounts){
        this.videoId = videoId;
        this.imageUrl = imageUrl;
        this.headIconUrl = headIconUrl;
        this.title = title;
        this.authorName = authorName;
        this.tag = tag;
        this.playUrl = playUrl;
        this.videoDescription = videoDescription;
        this.authorDescription = authorDescription;
        this.backgroundUrl = backgroundUrl;
        this.likeCounts = likeCounts;
        this.shareCounts = shareCounts;
    }

    public int getId() {
        return id;
    }

    public int getVideoId() {
        return videoId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getHeadIconUrl() {
        return headIconUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getTag() {
        return tag;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public String getAuthorDescription() {
        return authorDescription;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public int getLikeCounts() {
        return likeCounts;
    }

    public int getShareCounts() {
        return shareCounts;
    }

    public long getTime() {
        return time;
    }

    public String getUserCount() {
        return userCount;
    }

    public int getOperation() {
        return operation;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }
}
