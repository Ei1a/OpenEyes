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
import com.example.openeyes.adapter.VideoAdapterRecord;
import com.example.openeyes.bean.VideoItem;
import com.example.openeyes.model.RecordVideoDao;
import com.example.openeyes.model.VideoDatabase;

import java.util.List;

public class RecordFixActivity extends AppCompatActivity {

    private String count;
    private List<VideoItem> recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_fix);

        initData();
        initView();
    }

    /*
     * 初始化数据
     */
    private void initData(){
        count = getIntent().getStringExtra("count");
        //初始化dao
        RecordVideoDao recordDao = VideoDatabase.getInstance(getApplicationContext()).getRecordVideoDao();
        //得到记录视频item列表
        recordList = recordDao.getAll(count);
    }

    /*
     * 初始化View
     */
    private void initView(){
        //设置状态栏黑色字体
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        /*
         * 得到View引用
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_record_fix);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_record_video);
        TextView textTips = (TextView) findViewById(R.id.text_tips_record_fix);
        /*
         * 初始化Toolbar
         */
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //初始化Tips
        if(!recordList.isEmpty()){
            //隐藏
            textTips.setVisibility(View.GONE);
        }
        /*
         * 初始化RecyclerView
         */
        VideoAdapterRecord adapter = new VideoAdapterRecord(recordList);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
    }

    /*
     * 菜单功能按钮点击监听
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

    /*
     * 修改进出场动画
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.record_main_enter, R.anim.record_to_main);
    }
}
