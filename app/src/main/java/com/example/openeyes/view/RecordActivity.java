package com.example.openeyes.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.openeyes.R;

public class RecordActivity extends AppCompatActivity {
    public static boolean isRecordListNull = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //设置状态栏黑色字体
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        TextView textTips = (TextView)findViewById(R.id.text_tips_record);
        //初始化Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_record);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        if(!isRecordListNull){
            FragmentRecord fragmentRecord = new FragmentRecord();
            textTips.setVisibility(View.GONE);
            replaceFragment(fragmentRecord);
        }
    }

    private void replaceFragment(FragmentRecord fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.wait_replace_in_record,fragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fix_open,R.anim.record_to_main);
    }
}
