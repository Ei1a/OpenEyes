package com.example.openeyes.Bean;

public class VideoItem {
    private String imageUrl;
    private String headIconUrl;
    private String title;
    private String authorName;
    private String tag;
    private String playUrl;
    private String videoDescription;
    private String authorDescription;
    private String backgroundUrl;

    public VideoItem(String imageUrl, String headIconUrl, String title, String authorName, String tag, String playUrl, String videoDescription, String authorDescription, String backgroundUrl){
        this.imageUrl = imageUrl;
        this.headIconUrl = headIconUrl;
        this.title = title;
        this.authorName = authorName;
        this.tag = tag;
        this.playUrl = playUrl;
        this.videoDescription = videoDescription;
        this.authorDescription = authorDescription;
        this.backgroundUrl = backgroundUrl;
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
}
