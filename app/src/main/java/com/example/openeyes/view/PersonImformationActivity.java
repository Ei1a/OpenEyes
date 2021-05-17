package com.example.openeyes.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.example.openeyes.R;
import com.example.openeyes.adapter.GlideEngine;
import com.example.openeyes.bean.LocationReuslt;
import com.example.openeyes.bean.LoginEvent;
import com.example.openeyes.util.Utils;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.ui.EasyPhotosActivity;
import com.huantansheng.easyphotos.utils.result.EasyResult;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonImformationActivity extends AppCompatActivity {

    private TextView textSave;
    private TextView textExitLogin;
    private TextView textDateResult;
    private TextView textSexResult;
    private TextView textLocationResult;
    private ConstraintLayout constraintLayout;
    private EditText editTextName;
    private EditText editTextSchool;
    private EditText editTextWork;
    private Button buttonChangeHeadIcon;
    private ImageView imageHeadIcon;

    private LocationReuslt location;
    private final List<String> sexList = new ArrayList<>();
    private ArrayList<Photo> photoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_imformation);
        initView();
        initListener();
        initData();
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
            Log.d("marginDebug", "margin = " + toolbar.getContentInsetLeft());
        }

        // 得到View引用
        textSave = (TextView) findViewById(R.id.text_save);
        textExitLogin = (TextView) findViewById(R.id.text_exit_login);
        textDateResult = (TextView) findViewById(R.id.text_date_result);
        textSexResult = (TextView) findViewById(R.id.text_sex_result);
        textLocationResult = (TextView) findViewById(R.id.text_location_result);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout_person);
        editTextName = (EditText) findViewById(R.id.edit_person_name);
        editTextSchool = (EditText) findViewById(R.id.edit_person_school);
        editTextWork = (EditText) findViewById(R.id.edit_person_work);
        buttonChangeHeadIcon = (Button) findViewById(R.id.button_person_change_head_icon);
        imageHeadIcon = (ImageView) findViewById(R.id.image_person_head_icon);
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
         * 监听空白点击
         */
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.requestFocus();
                Log.d("clickDebug", "onClick: ");
            }
        });

        /*
         * 监听更换头像按钮
         */
        buttonChangeHeadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonImformationActivity.this, "更换头像", Toast.LENGTH_SHORT).show();
                EasyPhotos.createAlbum(PersonImformationActivity.this, true, false, GlideEngine.getInstance())
                        .setFileProviderAuthority("com.example.openeyes")
                        .start(new SelectCallback() {
                            @Override
                            public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
                                Glide.with(PersonImformationActivity.this).load(photos.get(0).uri).into(imageHeadIcon);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });

        /*
         * 监听EditText失去焦点
         */
        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    InputMethodManager manager = (InputMethodManager)PersonImformationActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(manager != null){
                        manager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        };
        editTextName.setOnFocusChangeListener(focusChangeListener);
        editTextSchool.setOnFocusChangeListener(focusChangeListener);
        editTextWork.setOnFocusChangeListener(focusChangeListener);

        /*
         * 监听日期点击
         */
        textDateResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerView pickerView = new TimePickerBuilder(PersonImformationActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String text = (date.getYear()+1900) + "年" + (date.getMonth()+1) + "月" + date.getDate() + "日";
                        textDateResult.setText(text);
                    }
                }).setSubmitText("确定")
                        .setCancelText("取消")
                        .build();
                pickerView.show();
            }
        });

        /*
         * 监听性别点击
         */
        textSexResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OptionsPickerView optionsPickerView = new OptionsPickerBuilder(PersonImformationActivity.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        textSexResult.setText(sexList.get(options1));
                    }
                }).setSubmitText("确定")
                        .setCancelText("取消")
                        .build();
                optionsPickerView.setPicker(sexList);
                optionsPickerView.show();
            }
        });

        /*
         * 监听所在地点击
         */
        textLocationResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OptionsPickerView optionsPickerView = new OptionsPickerBuilder(PersonImformationActivity.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String result = location.getProvinces().get(options1) + " " + location.getCities().get(options1).get(options2) + " " + location.getCounties().get(options1).get(options2).get(options3);
                        textLocationResult.setText(result);
                    }
                }).setSubmitText("确定")
                        .setCancelText("取消")
                        .build();
                optionsPickerView.setPicker(location.getProvinces(), location.getCities(), location.getCounties());
                optionsPickerView.show();
            }
        });

        /*
         * 监听退出登陆按钮
         */
        textExitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新登陆状态
                EventBus.getDefault().post(new LoginEvent(false));
                finish();
            }
        });
    }

    /*
     * 初始化数据
     */
    private void initData(){
        //初始化性别数据
        sexList.add("男");
        sexList.add("女");
        sexList.add("未知");
        //初始化地区数据
        location = Utils.parseLocation();
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

    /*
     * 监听软件按键事件
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d("keyCodeDebug", "Code: " + event.getKeyCode());
        //若为Enter键，剥夺焦点
        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            constraintLayout.requestFocus();
        }
        return super.dispatchKeyEvent(event);
    }
}
