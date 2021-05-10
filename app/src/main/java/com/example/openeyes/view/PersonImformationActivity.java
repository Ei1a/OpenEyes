package com.example.openeyes.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.openeyes.R;

public class PersonImformationActivity extends AppCompatActivity {

    private TextView textSave;
    private TextView textExitLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_imformation);
        initView();
        initListener();
    }

    /*
     * 初始化View
     */
    private void initView(){
        //设置状态栏内容为黑色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_person);
        setSupportActionBar(toolbar);

        //设置ActionBar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // 得到View引用
        textSave = (TextView) findViewById(R.id.text_save);
        textExitLogin = (TextView) findViewById(R.id.text_exit_login);
    }

    /*
     * 初始化监听器
     */
    private void initListener(){
        /*
         * 监听保存按钮
         */
        textSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonImformationActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });

        /*
         * 监听退出登陆按钮
         */
        textExitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonImformationActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
            }
        });
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
