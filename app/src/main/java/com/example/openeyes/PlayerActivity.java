package com.example.openeyes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerActivity extends AppCompatActivity {
    public static VideoItem videoItem;
    public static boolean isNormalOrDB;
    private VideoView videoView;
    private TextView videoTitle;
    private TextView videoTag;
    private TextView videoDescription;
    private CircleImageView authorHeadIcon;
    private TextView authorName;
    private TextView authorDescription;
    private ImageView videoBackground;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        videoView = (VideoView)findViewById(R.id.video_view);
        videoTitle = (TextView)findViewById(R.id.video_title_player);
        videoTag = (TextView)findViewById(R.id.video_tag_player);
        videoDescription = (TextView)findViewById(R.id.video_description_player);
        authorHeadIcon = (CircleImageView)findViewById(R.id.head_icon_player);
        authorName = (TextView)findViewById(R.id.video_author_name_player);
        authorDescription = (TextView)findViewById(R.id.video_author_description);
        videoBackground = (ImageView)findViewById(R.id.video_background_player);

        initPlayerByVideoItem();
        if(isNormalOrDB){
            updateRecord();
        }
    }


    private void initPlayerByVideoItem() {
        videoView.setVideoPath(videoItem.getPlayUrl());
        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(videoView);
        videoView.setMediaController(controller);
        Glide.with(PlayerActivity.this).load(videoItem.getBackgroundUrl()).into(videoBackground);
        videoBackground.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        videoTitle.setText(videoItem.getTitle());
        videoTag.setText(videoItem.getTag());
        videoDescription.setText(videoItem.getVideoDescription());
        Glide.with(PlayerActivity.this).load(videoItem.getHeadIconUrl()).into(authorHeadIcon);
        authorName.setText(videoItem.getAuthorName());
        authorDescription.setText(videoItem.getAuthorDescription());
        videoView.start();
    }

    private void updateRecord() {
        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("imageUrl",videoItem.getImageUrl());
        values.put("headIconUrl",videoItem.getHeadIconUrl());
        values.put("title",videoItem.getTitle());
        values.put("authorName",videoItem.getAuthorName());
        values.put("tag",videoItem.getTag());
        values.put("playUrl",videoItem.getPlayUrl());
        values.put("videoDescription",videoItem.getVideoDescription());
        values.put("authorDescription",videoItem.getAuthorDescription());
        values.put("backgroundUrl",videoItem.getBackgroundUrl());
        db.insert("RecordItem",null,values);
        MainActivity.videoItemList_record.add(videoItem);
        if(RecordActivity.isRecordListNull){
            RecordActivity.isRecordListNull = false;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fix_open,R.anim.player_to_main);
    }
}
