package com.example.openeyes.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.openeyes.R;
import com.example.openeyes.util.Utils;
import com.example.openeyes.util.Value;

public class WelcomeActivity extends AppCompatActivity {
    public static String imagesUrl = null;
    public static String imagesTitle = null;
    public final String WELCOME_PAGE_URL = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
    private Handler handler;
    private LinearLayout linearLayout;
    private ImageView welcomeImageView;
    private TextView imageTitleTextView;
    private Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        linearLayout = (LinearLayout)findViewById(R.id.image_welcome_layout);
        welcomeImageView = (ImageView)findViewById(R.id.image_welcome);
        imageTitleTextView = (TextView)findViewById(R.id.image_title);
        animation = AnimationUtils.loadAnimation(WelcomeActivity.this,R.anim.welcome_in);

        linearLayout.startAnimation(animation);
        animation.setFillAfter(true);
        Utils.sendHttpRequest(WELCOME_PAGE_URL,Value.PAGE_WELCOME);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Glide.with(WelcomeActivity.this).load(imagesUrl).into(welcomeImageView);
            }
        },500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageTitleTextView.setVisibility(View.VISIBLE);

            }
        },500);
        handler.postDelayed(new Runnable() {
            @Override

            public void run() {
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.welcome_to_main_enter, R.anim.welcome_to_main_exit);
                finish();
            }
        },3000);
    }
}
