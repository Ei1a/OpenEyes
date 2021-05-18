package com.example.openeyes.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.openeyes.bean.VideoItem;

@Database(entities = {VideoItem.class}, version = 1, exportSchema = false)
public abstract class VideoDatabase extends RoomDatabase {
    private static final String databaseName = "OpenEyesRoom.db";
    private static volatile VideoDatabase instance;

    public static VideoDatabase getInstance(Context context){
        if(instance == null){
            synchronized (VideoDatabase.class){
                if(instance == null){
                    instance = Room.databaseBuilder(context, VideoDatabase.class, databaseName)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return instance;
    }

    public abstract LikeVideoDao getLikeVideoDao();
}
