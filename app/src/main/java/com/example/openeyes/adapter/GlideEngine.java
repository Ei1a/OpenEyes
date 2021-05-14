package com.example.openeyes.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.huantansheng.easyphotos.engine.ImageEngine;

public class GlideEngine implements ImageEngine {
    //单例
    private static GlideEngine instance = null;
    //单例模式,私有构造方法
    private GlideEngine(){

    }
    //单例模式
    public static GlideEngine getInstance(){
        if(instance == null){
            synchronized (GlideEngine.class){
                if(instance == null){
                    instance = new GlideEngine();
                }
            }
        }
        return instance;
    }

    @Override
    public void loadPhoto(@NonNull Context context, @NonNull Uri uri, @NonNull ImageView imageView) {
        Glide.with(context).load(uri).into(imageView);
    }

    @Override
    public void loadGifAsBitmap(@NonNull Context context, @NonNull Uri gifUri, @NonNull ImageView imageView) {

    }

    @Override
    public void loadGif(@NonNull Context context, @NonNull Uri gifUri, @NonNull ImageView imageView) {

    }

    @Override
    public Bitmap getCacheBitmap(@NonNull Context context, @NonNull Uri uri, int width, int height) throws Exception {
        return null;
    }
}
