package com.example.openeyes.bean;

public class SortItemHeader {
    private String sortName;
    private String description;
    private String bgPictureUrl;
    private int tagFollowCount;
    private int lookCount;

    public SortItemHeader(String sortName, String description, String bgPictureUrl, int tagFollowCount, int lookCount) {
        this.sortName = sortName;
        this.description = description;
        this.bgPictureUrl = bgPictureUrl;
        this.tagFollowCount = tagFollowCount;
        this.lookCount = lookCount;
    }

    public String getSortName() {
        return sortName;
    }

    public String getDescription() {
        return description;
    }

    public String getBgPictureUrl() {
        return bgPictureUrl;
    }

    public int getTagFollowCount() {
        return tagFollowCount;
    }

    public int getLookCount() {
        return lookCount;
    }
}
