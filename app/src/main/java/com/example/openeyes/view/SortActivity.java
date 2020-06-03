package com.example.openeyes.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.openeyes.adapter.SortVideoAdapter;
import com.example.openeyes.bean.SortItem;
import com.example.openeyes.R;
import com.example.openeyes.util.Utils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

public class SortActivity extends AppCompatActivity {
    private final String TAG = "mDebug";
    public static SortItem sortItem = null;
    private ImageView imageView_header;
    private TextView textView_header_name;
    private TextView textView_header_description;
    private TextView textview_header_tag_follow_count;
    private TextView textView_header_look_count;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerView_sort_video;
    private Context mContext = SortActivity.this;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        //隐藏状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置状态栏UI为黑色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sort);
        setSupportActionBar(toolbar);
        ActionBar actionBar = (ActionBar) getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);

        }

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.coolapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);
        collapsingToolbarLayout.setScrimsShown(false);


        imageView_header = (ImageView) findViewById(R.id.image_sort_header);
        textView_header_name = (TextView) findViewById(R.id.sort_item_header_name);
        textView_header_description = (TextView) findViewById(R.id.sort_item_header_description);
        textview_header_tag_follow_count = (TextView) findViewById(R.id.sort_item_header_tag_follow_count);
        textView_header_look_count = (TextView) findViewById(R.id.sort_item_header_look_count);
        recyclerView_sort_video = (RecyclerView) findViewById(R.id.sort_item_recycler_view);
        final SortVideoAdapter adapter = new SortVideoAdapter(MainActivity.videoItemList_sort);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) new LinearLayoutManager(mContext,RecyclerView.VERTICAL,false);
        recyclerView_sort_video.setLayoutManager(linearLayoutManager);
        recyclerView_sort_video.setAdapter(adapter);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sortItem != null){
                    Glide.with(mContext).load(sortItem.getBgPicture()).into(imageView_header);
                    textView_header_name.setText(sortItem.getHeaderName());
                    textView_header_description.setText(sortItem.getDescription());
                    textview_header_tag_follow_count.setText(String.valueOf(sortItem.getTagFollowCount()) + "人关注 | ");
                    textView_header_look_count.setText(String.valueOf(sortItem.getLookCount()) + "人参与");
                    collapsingToolbarLayout.setTitle(sortItem.getHeaderName());
                }
            }
        },200);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        },400);
        recyclerView_sort_video.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItem = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                if(newState==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem==totalItemCount-1 && visibleItemCount>0){
                    if(MainActivity.next_sort_page_url != null){
                        Utils utils = new Utils();
                        utils.adapterUpdateNotify(mContext,recyclerView_sort_video,MainActivity.PAGE_SORT_ITEM_VIDEO,MainActivity.next_sort_page_url);
                    }
                }
                //停下时自动播放第一个完整显示的Item的视频
                switch (newState){
                    case RecyclerView.SCROLL_STATE_IDLE:
                        int firstCompletelyVisibleItemPosition = lm.findFirstCompletelyVisibleItemPosition();
                        Log.d(TAG, "onScrollStateChanged: first position = " + firstCompletelyVisibleItemPosition);
                        if(firstCompletelyVisibleItemPosition >= 0){
                            GSYVideoPlayer videoPlayer = (GSYVideoPlayer) lm.findViewByPosition(firstCompletelyVisibleItemPosition).findViewById(R.id.gsy_video_player);
                            if(!videoPlayer.isInPlayingState()){
                                videoPlayer.getStartButton().performClick();
                            }
                        }

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItem = lm.findFirstVisibleItemPosition();
                int lastVisibleItem = lm.findLastVisibleItemPosition();
//                int visibleItem = lastVisibleItem - firstVisibleItem;

                //大于0说明有播放
                if(GSYVideoManager.instance().getPlayPosition() >= 0){
                    //当前播放位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应播放列表的TAG
                    if(GSYVideoManager.instance().getPlayTag().equals(SortVideoAdapter.TAG_RECYCLERVIEW) && (position < firstVisibleItem || position > lastVisibleItem)){
                        if(GSYVideoManager.isFullState(SortActivity.this)){
                            return;
                        }
                        GSYVideoManager.releaseAllVideos();
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.videoItemList_sort.clear();
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if(GSYVideoManager.backFromWindowFull(this)){
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }
}
