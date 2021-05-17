package com.example.openeyes.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.openeyes.bean.VideoItem;
import com.example.openeyes.R;
import com.example.openeyes.util.Value;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerActivity extends AppCompatActivity {
    public static VideoItem videoItem;
    public static boolean isNormalOrDB;
    private StandardGSYVideoPlayer videoPlayer;
    private TextView videoTitle;
    private TextView videoTag;
    private TextView videoDescription;
    private CircleImageView authorHeadIcon;
    private TextView authorName;
    private TextView authorDescription;
    private ImageView videoBackground;
    private OrientationUtils orientationUtils;
    private ConstraintLayout constraintDownload;
    private ConstraintLayout constraintLike;
    private ImageView imageDownload;
    private ImageView imageLike;
    private TextView textDownload;
    private TextView textLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        videoPlayer = (StandardGSYVideoPlayer) findViewById(R.id.gsy_video_player);
        videoTitle = (TextView)findViewById(R.id.video_title_player);
        videoTag = (TextView)findViewById(R.id.video_tag_player);
        videoDescription = (TextView)findViewById(R.id.video_description_player);
        authorHeadIcon = (CircleImageView)findViewById(R.id.head_icon_player);
        authorName = (TextView)findViewById(R.id.video_author_name_player);
        authorDescription = (TextView)findViewById(R.id.video_author_description);
        videoBackground = (ImageView)findViewById(R.id.video_background_player);
        constraintDownload = (ConstraintLayout) findViewById(R.id.constraint_layout_download);
        constraintLike = (ConstraintLayout) findViewById(R.id.constraint_layout_like);
        imageDownload = (ImageView) findViewById(R.id.image_download);
        imageLike = (ImageView) findViewById(R.id.image_like);
        textDownload = (TextView) findViewById(R.id.text_download);
        textLike = (TextView) findViewById(R.id.text_like);

        initPlayerByVideoItem();
        if(isNormalOrDB){
            updateRecord();
        }
    }


    private void initPlayerByVideoItem() {
        /*
        配置GSYVideoPlayer
         */
        videoPlayer.setUp(videoItem.getPlayUrl(),true,null);
        //设置Title
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
//        orientationUtils = new OrientationUtils(this,videoPlayer);
        //设置全屏按键
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                orientationUtils.resolveByClick();
                videoPlayer.startWindowFullscreen(PlayerActivity.this,false,true);
            }
        });
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //是否根据视频尺寸，自动选择竖屏全屏或横屏全屏
        videoPlayer.setAutoFullWithSize(true);
        //全屏动画
        videoPlayer.setShowFullAnimation(true);
        //小屏时触摸滑动
        videoPlayer.setIsTouchWiget(true);
        //循环播放
        videoPlayer.setLooping(true);
        //设置控制UI显示时间
        videoPlayer.setDismissControlTime(1500);
        //播放
        videoPlayer.getStartButton().performClick();

        Glide.with(PlayerActivity.this).load(videoItem.getBackgroundUrl()).into(videoBackground);
        videoBackground.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        videoTitle.setText(videoItem.getTitle());
        videoTag.setText(videoItem.getTag());
        videoDescription.setText(videoItem.getVideoDescription());
        Glide.with(PlayerActivity.this).load(videoItem.getHeadIconUrl()).into(authorHeadIcon);
        authorName.setText(videoItem.getAuthorName());
        authorDescription.setText(videoItem.getAuthorDescription());

        /*
         * 点击监听
         */
        constraintDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textDownload.getText().equals("下载")){
                    imageDownload.setBackground(getDrawable(R.drawable.ic_download_already));
                    textDownload.setText("已下载");
                    textDownload.setTextColor(getResources().getColor(R.color.colorButtonDown));
                }
            }
        });

        constraintLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textLike.getText().equals("收藏")){
                    imageLike.setBackground(getDrawable(R.drawable.ic_like_already));
                    textLike.setText("已收藏");
                }else{
                    imageLike.setBackground(getDrawable(R.drawable.ic_like_bold));
                    textLike.setText("收藏");
                }
            }
        });
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
        Value.videoItemList_record.add(videoItem);
        if(RecordActivity.isRecordListNull){
            RecordActivity.isRecordListNull = false;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fix_open,R.anim.player_to_main);
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
//        if(orientationUtils != null){
//            orientationUtils.releaseListener();
//        }
    }

    @Override
    public void onBackPressed() {

//        if(orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
//            videoPlayer.getFullscreenButton().performClick();
//            return;
//        }
//        //释放所有
//        videoPlayer.setVideoAllCallBack(null);
        if(GSYVideoManager.backFromWindowFull(this)){
            return;
        }
        super.onBackPressed();
    }
}
