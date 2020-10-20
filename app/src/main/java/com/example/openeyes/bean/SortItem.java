package com.example.openeyes.bean;

public class SortItem {
    private String ID;
    private String imageUrl;
    private String sortName;
//    private String description;
//    private String bgPicture;
//    private String headerName;
//    private int tagFollowCount;
//    private int lookCount;

    public SortItem(String ID, String imageUrl, String sortName){
        this.ID = ID;
        this.imageUrl = imageUrl;
        this.sortName = sortName;
    }

    public String getID() {
        return ID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSortName() {
        return sortName;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

//    public String getDescription() {
//        return description;
//    }
//
//    public String getBgPicture() {
//        return bgPicture;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void setBgPicture(String bgPicture) {
//        this.bgPicture = bgPicture;
//    }
//
//    public String getHeaderName() {
//        return headerName;
//    }
//
//    public void setHeaderName(String headerName) {
//        this.headerName = headerName;
//    }
//
//    public int getTagFollowCount() {
//        return tagFollowCount;
//    }
//
//    public int getLookCount() {
//        return lookCount;
//    }
//
//    public void setTagFollowCount(int tagFollowCount) {
//        this.tagFollowCount = tagFollowCount;
//    }
//
//    public void setLookCount(int lookCount) {
//        this.lookCount = lookCount;
//    }
}
