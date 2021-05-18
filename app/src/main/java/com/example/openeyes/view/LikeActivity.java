package com.example.openeyes.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.openeyes.R;
import com.example.openeyes.adapter.LikeAdapter;
import com.example.openeyes.bean.VideoItem;
import com.example.openeyes.model.LikeVideoDao;
import com.example.openeyes.model.VideoDatabase;

import java.util.ArrayList;
import java.util.List;

public class LikeActivity extends AppCompatActivity {

    private List<VideoItem> videoItemList;
    private LikeVideoDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        initData();
        initView();
    }

    private void initView(){
        //设置状态栏为黑色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //初始化Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_like);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        //初始化RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_like_video);
        LikeAdapter adapter = new LikeAdapter(videoItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(LikeActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        //初始化TextTips
        TextView textTips = (TextView) findViewById(R.id.text_tips_like);
        if(videoItemList.isEmpty()){
            textTips.setVisibility(View.VISIBLE);
        }else{
            textTips.setVisibility(View.GONE);
        }
    }

    private void initData(){
        dao = VideoDatabase.getInstance(getApplicationContext()).getLikeVideoDao();
        videoItemList = dao.getAll();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
