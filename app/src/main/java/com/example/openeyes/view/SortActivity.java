package com.example.openeyes.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.openeyes.adapter.SortVideoAdapter;
import com.example.openeyes.bean.SortItem;
import com.example.openeyes.R;
import com.example.openeyes.bean.SortItemHeader;
import com.example.openeyes.bean.VideoItem;
import com.example.openeyes.util.CollapsingToolbarLayoutListener;
import com.example.openeyes.util.Utils;
import com.example.openeyes.util.Value;
import com.example.openeyes.viewmodel.MainViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.util.ArrayList;
import java.util.List;

public class SortActivity extends AppCompatActivity {
    private final String TAG = "mDebug";
    private AppBarLayout mAppBarLayout;
    private ImageView imageView_header;
    private TextView textView_header_name;
    private TextView textView_header_description;
    private TextView textview_header_tag_follow_count;
    private TextView textView_header_look_count;
    private TextView textView_title;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerView_sort_video;
    private Context mContext = SortActivity.this;
    private MainViewModel mViewModel;
    private List<VideoItem> videoItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        initView();
        initListener();
        initViewModel();
        initData();
        initObserver();
    }

    /*
     * 初始化View
     */
    private void initView(){
        //隐藏状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置状态栏UI为黑色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        /*
         * 配置Toolbar
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sort);
        setSupportActionBar(toolbar);
        ActionBar actionBar = (ActionBar) getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        /*
         * 配置CollapsingToolbarLayout
         */
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.coolapsing_toolbar);
        collapsingToolbarLayout.setScrimsShown(false);

        /*
         * 得到View引用
         */
        imageView_header = (ImageView) findViewById(R.id.image_sort_header);
        textView_header_name = (TextView) findViewById(R.id.sort_item_header_name);
        textView_header_description = (TextView) findViewById(R.id.sort_item_header_description);
        textview_header_tag_follow_count = (TextView) findViewById(R.id.sort_item_header_tag_follow_count);
        textView_header_look_count = (TextView) findViewById(R.id.sort_item_header_look_count);
        textView_title = (TextView) findViewById(R.id.text_collapsing_tile);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout_sort);

        /*
         * 配置RecyclerView
         */
        recyclerView_sort_video = (RecyclerView) findViewById(R.id.sort_item_recycler_view);
        SortVideoAdapter adapter = new SortVideoAdapter(videoItemList);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) new LinearLayoutManager(mContext,RecyclerView.VERTICAL,false);
        recyclerView_sort_video.setLayoutManager(linearLayoutManager);
        recyclerView_sort_video.setAdapter(adapter);

        /*
         * 监听标题栏状态
         */
        mAppBarLayout.addOnOffsetChangedListener(new CollapsingToolbarLayoutListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, int state) {
                Drawable backIcon = getResources().getDrawable(R.drawable.ic_action_name);
                if(state == STATE_EXPANDED){
                    // 展开状态
                    Log.d("ToolbarLayoutDebug", "展开状态");
                    // 调整返回按钮为白色
                    backIcon.setColorFilter(getResources().getColor(R.color.backIconWhite), PorterDuff.Mode.SRC_ATOP);
                }else if(state == STATE_COLLAPSED){
                    // 折叠状态
                    Log.d("ToolbarLayoutDebug", "折叠状态");
                    textView_title.setVisibility(View.VISIBLE);
                    // 调整返回按钮为黑色
                    backIcon.setColorFilter(getResources().getColor(R.color.backIconBlack), PorterDuff.Mode.SRC_ATOP);
                }else{
                    // 中间状态
                    Log.d("ToolbarLayoutDebug", "中间状态");
                    textView_title.setVisibility(View.GONE);
                    // 调整返回按钮为白色
                    backIcon.setColorFilter(getResources().getColor(R.color.backIconWhite), PorterDuff.Mode.SRC_ATOP);
                }
                getSupportActionBar().setHomeAsUpIndicator(backIcon);
            }
        });
    }

    /*
     * 初始化监听器
     */
    private void initListener(){
        /*
         * RecyclerView滑动监听
         */
        recyclerView_sort_video.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                /*
                 * 滑动到底部加载更多
                 */
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItem = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                if(newState==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem==totalItemCount-1 && visibleItemCount>0){
                    if(!mViewModel.next_sort_page_url.equals("null")){
                        mViewModel.sendHttpRequest(mViewModel.next_sort_page_url, mViewModel.PAGE_SORT_ITEM_VIDEO);
//                        Toast.makeText(SortActivity.this, "正在努力加载...", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SortActivity.this,"没有更多数据了噢",Toast.LENGTH_SHORT).show();
                    }
                }

                /*
                 * 停下时自动播放第一个完整显示的Item的视频
                 */
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
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
                        recyclerView_sort_video.getAdapter().notifyDataSetChanged();
                    }
                }
            }
        });
    }

    /*
     * 初始化ViewModel
     */
    private void initViewModel(){
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    /*
     * 初始化数据
     */
    private void initData(){
        String id = getIntent().getStringExtra("sortId");
        if(id != null){
            //请求header
            mViewModel.sendHttpRequest(mViewModel.SORT_PAGE_HEADER_URL + id, mViewModel.PAGE_SORT_ITEM_HEADER);
            //请求Video Item
            mViewModel.sendHttpRequest(mViewModel.SORT_PAGE_VIDEO_URL + id, mViewModel.PAGE_SORT_ITEM_VIDEO);
        }
    }

    /*
     * 初始化LiveData观察
     */
    private void initObserver(){
        /*
         * 请求分类头部结果观察
         */
        mViewModel.requestSortItemHeaderResult.observe(this, sortItemHeader -> {
            Glide.with(mContext).load(sortItemHeader.getBgPictureUrl()).into(imageView_header);
            textView_header_name.setText(sortItemHeader.getSortName());
            textView_header_description.setText(sortItemHeader.getDescription());
            textview_header_tag_follow_count.setText(sortItemHeader.getTagFollowCount() + "人关注 | ");
            textView_header_look_count.setText(sortItemHeader.getLookCount() + "人参与");
//            collapsingToolbarLayout.setTitle(sortItemHeader.getSortName());
            textView_title.setText(sortItemHeader.getSortName());
        });

        /*
         * 请求视频item结果观察
         */
        mViewModel.requestSortItemResult.observe(this, videoItems -> {
            videoItemList.addAll(videoItems);
            recyclerView_sort_video.getAdapter().notifyDataSetChanged();
//            Toast.makeText(SortActivity.this, "加载好咯~", Toast.LENGTH_SHORT).show();
        });
    }

    /*
     * 配置返回按钮
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Value.videoItemList_sort.clear();
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
