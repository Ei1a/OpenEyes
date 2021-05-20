package com.example.openeyes.bean;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "personal_count")
public class PersonalCount {

    @PrimaryKey
    @ColumnInfo(name = "count")
    @NonNull
    private String count;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "recently_login")
    private int recentlyLogin;

    public PersonalCount(String count, String password, int recentlyLogin){
        this.count = count;
        this.password = password;
        this.recentlyLogin = recentlyLogin;
    }

    public int getRecentlyLogin() {
        return recentlyLogin;
    }

    public String getCount() {
        return count;
    }

    public String getPassword() {
        return password;
    }
}
