package com.example.openeyes.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;
import com.bumptech.glide.Glide;
import com.example.openeyes.bean.LoginEvent;
import com.example.openeyes.bean.VideoItem;
import com.example.openeyes.R;
import com.example.openeyes.model.LikeVideoDao;
import com.example.openeyes.model.PersonalCountDao;
import com.example.openeyes.model.RecordVideoDao;
import com.example.openeyes.model.VideoDatabase;
import com.example.openeyes.util.Value;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayerActivity extends AppCompatActivity {
    private VideoItem videoItem;
    private boolean isNormal;

    private StandardGSYVideoPlayer videoPlayer;
    private OrientationUtils orientationUtils;
    private ConstraintLayout constraintDownload;
    private ConstraintLayout constraintLike;
    private ImageView imageDownload;
    private ImageView imageLike;
    private TextView textDownload;
    private TextView textLike;

    private LikeVideoDao dao;
    private RecordVideoDao recordVideoDao;
    private PersonalCountDao countDao;
    private String count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initData();
        initView();
        initListener();
    }

    /*
     * 初始化View
     */
    private void initView(){
        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //注册Aria
        Aria.download(PlayerActivity.this).register();

        /*
         * 得到View引用
         */
        videoPlayer = (StandardGSYVideoPlayer) findViewById(R.id.gsy_video_player);
        TextView videoTitle = (TextView) findViewById(R.id.video_title_player);
        TextView videoTag = (TextView) findViewById(R.id.video_tag_player);
        TextView videoDescription = (TextView) findViewById(R.id.video_description_player);
        CircleImageView authorHeadIcon = (CircleImageView) findViewById(R.id.head_icon_player);
        TextView authorName = (TextView) findViewById(R.id.video_author_name_player);
        TextView authorDescription = (TextView) findViewById(R.id.video_author_description);
        ImageView videoBackground = (ImageView) findViewById(R.id.video_background_player);
        constraintDownload = (ConstraintLayout) findViewById(R.id.constraint_layout_download);
        constraintLike = (ConstraintLayout) findViewById(R.id.constraint_layout_like);
        imageDownload = (ImageView) findViewById(R.id.image_download);
        imageLike = (ImageView) findViewById(R.id.image_like);
        textDownload = (TextView) findViewById(R.id.text_download);
        textLike = (TextView) findViewById(R.id.text_like);
        //初始化GSYVideoPlayer
        initPlayerByVideoItem();

        /*
         * View赋值
         */
        Glide.with(PlayerActivity.this).load(videoItem.getBackgroundUrl()).into(videoBackground);
        videoBackground.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        videoTitle.setText(videoItem.getTitle());
        videoTag.setText(videoItem.getTag());
        videoDescription.setText(videoItem.getVideoDescription());
        Glide.with(PlayerActivity.this).load(videoItem.getHeadIconUrl()).into(authorHeadIcon);
        authorName.setText(videoItem.getAuthorName());
        authorDescription.setText(videoItem.getAuthorDescription());
        if(dao.queryStatus(videoItem.getVideoId(), count) != null){
            imageLike.setBackground(getDrawable(R.drawable.ic_like_already));
            textLike.setText("已收藏");
        }
    }

    /*
     * 配置GSYVideoPlayer
     */
    private void initPlayerByVideoItem() {
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
        //关闭log
        IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT);

    }

    /*
     * 初始化数据
     */
    private void initData(){
        //初始化VideoItem
        Intent intent = getIntent();
        videoItem = (VideoItem) intent.getSerializableExtra("item");
        isNormal = intent.getBooleanExtra("isNormal", false);
        //初始化dao
        dao = VideoDatabase.getInstance(getApplicationContext()).getLikeVideoDao();
        recordVideoDao = VideoDatabase.getInstance(getApplicationContext()).getRecordVideoDao();
        countDao = VideoDatabase.getInstance(getApplicationContext()).getPersonalCountDao();
    }

    /*
     * 初始化监听器
     */
    private void initListener(){
        /*
         * 监听下载按钮点击
         */
        constraintDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textDownload.getText().equals("下载")){
                    //开始下载任务
                    String fileName = videoItem.getTitle() + ".mp4";
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    String path = directory + "/" + fileName;
                    Aria.download(PlayerActivity.this)
                            .load(videoItem.getPlayUrl())
                            .setFilePath(path)
                            .create();
                }
            }
        });

        /*
         * 监听收藏按钮点击
         */
        constraintLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * 收藏视频
                 */
                if(count == null){
                    //未登录状态，则跳登陆界面
                    Intent intent = new Intent(PlayerActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.main_to_login, R.anim.fix_close);
                }else{
                    //登陆状态，才能收藏
                    if(textLike.getText().equals("收藏")){
                        //未收藏才能收藏
                        //设置收藏时间
                        videoItem.setLikeTime(System.currentTimeMillis());
                        //设置收藏的用户帐号
                        videoItem.setUserCount(count);
                        //设置操作类型
                        videoItem.setOperation(1);
                        dao.insert(videoItem);
                        imageLike.setBackground(getDrawable(R.drawable.ic_like_already));
                        textLike.setText("已收藏");
                    }else{
                        dao.delete(videoItem.getVideoId(), count);
                        imageLike.setBackground(getDrawable(R.drawable.ic_like_bold));
                        textLike.setText("收藏");
                    }
                }
            }
        });
    }

    /*
     * 更新观看记录
     */
    private void updateRecord() {
        //设置观看时间
        videoItem.setWatchTime(System.currentTimeMillis());
        //设置观看的用户账号
        videoItem.setUserCount(count);
        //设置操作类型
        videoItem.setOperation(0);
        recordVideoDao.insert(videoItem);
    }

    /*
     * 监听任务下载状态
     */
    @Download.onTaskRunning protected void downloadRunning(DownloadTask task){
        //显示任务百分比
        int percent = task.getPercent();
        Log.d("downloadPercentDebug", "percent: " + percent);
        textDownload.setText(percent + "%");
    }

    /*
     * 监听任务下载完毕
     */
    @Download.onTaskComplete void downloadComplete(DownloadTask task){
        //更新按钮状态
        imageDownload.setBackground(getDrawable(R.drawable.ic_download_already));
        textDownload.setText("已下载");
        textDownload.setTextColor(getResources().getColor(R.color.colorButtonDown));
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
        //得到已登录的账号
        count = countDao.queryRecentlyLogin();
        //更新观看记录
        if(isNormal && count!=null){
            updateRecord();
        }
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
