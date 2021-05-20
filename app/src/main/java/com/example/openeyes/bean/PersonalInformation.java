package com.example.openeyes.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "personal_information")
public class PersonalInformation {

    @PrimaryKey
    @ColumnInfo(name = "count")
    @NonNull
    private String count;

    @ColumnInfo(name = "head_icon", typeAffinity = ColumnInfo.BLOB)
    private byte[] headIcon;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "sex")
    private String sex;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "school")
    private String school;

    @ColumnInfo(name = "work")
    private String work;

    public PersonalInformation(String count, byte[] headIcon, String name, String sex, String date, String location, String school, String work) {
        this.count = count;
        this.headIcon = headIcon;
        this.name = name;
        this.sex = sex;
        this.date = date;
        this.location = location;
        this.school = school;
        this.work = work;
    }

    public String getCount() {
        return count;
    }

    public byte[] getHeadIcon() {
        return headIcon;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getSchool() {
        return school;
    }

    public String getWork() {
        return work;
    }
}
