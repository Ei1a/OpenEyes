package com.example.openeyes.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.example.openeyes.bean.PersonalCount;
import com.example.openeyes.bean.PersonalInformation;
import com.example.openeyes.model.PersonalCountDao;
import com.example.openeyes.model.PersonalInformationDao;
import com.example.openeyes.model.VideoDatabase;
import com.example.openeyes.util.Utils;
import com.google.android.exoplayer2.C;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.ui.EasyPhotosActivity;
import com.huantansheng.easyphotos.utils.result.EasyResult;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
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

    private String count;
    private String password;
    private PersonalInformationDao dao;
    private PersonalInformation user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_imformation);

        initData();
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

        // 加载用户资料
        if(user != null){
            //保存过才加载，没保存过数据库里没有
            if(user.getHeadIcon() != null){
                //设置过头像才加载，否则显示默认头像
                Glide.with(PersonImformationActivity.this).load(user.getHeadIcon()).into(imageHeadIcon);
            }
            editTextName.setText(user.getName());
            textSexResult.setText(user.getSex());
            textDateResult.setText(user.getDate());
            textLocationResult.setText(user.getLocation());
            editTextSchool.setText(user.getSchool());
            editTextWork.setText(user.getWork());
            //设置TextView字体颜色
            if(user.getSex()!=null && !user.getSex().equals("选择性别")){
                textSexResult.setTextColor(Color.BLACK);
            }
            if(user.getDate()!=null && !user.getDate().equals("选择出生日期")){
                textDateResult.setTextColor(Color.BLACK);
            }
            if(user.getLocation()!=null && !user.getLocation().equals("选择所在地")){
                textLocationResult.setTextColor(Color.BLACK);
            }
        }
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
                byte[] headIcon = getHeadIconByte();
                String name = editTextName.getText().toString();
                String sex = textSexResult.getText().toString();
                String date = textDateResult.getText().toString();
                String location = textLocationResult.getText().toString();
                String school = editTextSchool.getText().toString();
                String work = editTextWork.getText().toString();
                //装入数据库
                dao.insert(new PersonalInformation(count, headIcon, name, sex, date, location, school, work));
                //通知资料已修改
                EventBus.getDefault().post(new LoginEvent(true, count, password));
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
                        if(textDateResult.getText().toString().equals("选择出生日期")){
                            textDateResult.setTextColor(Color.BLACK);
                        }
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
                        if(textSexResult.getText().toString().equals("选择性别")){
                            textSexResult.setTextColor(Color.BLACK);
                        }
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
                        if(textLocationResult.getText().toString().equals("选择所在地")){
                            textLocationResult.setTextColor(Color.BLACK);
                        }
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
                //更新最近登陆状态
                PersonalCountDao dao = VideoDatabase.getInstance(getApplicationContext()).getPersonalCountDao();
                dao.insert(new PersonalCount(count, password, 0));
                //通知已退出登陆
                EventBus.getDefault().post(new LoginEvent(false,null,null));
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
        //初始化Dao
        dao = VideoDatabase.getInstance(getApplicationContext()).getPersonalInformationDao();
        //得到账户信息
        count = getIntent().getStringExtra("count");
        password = getIntent().getStringExtra("password");
        //得到用户item
        user = dao.query(count);
    }

    private byte[] getHeadIconByte(){
        Drawable drawable = imageHeadIcon.getDrawable();
        if(drawable != null){
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            /*
             * Bitmap2Byte
             */
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        }
        return null;
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.record_main_enter, R.anim.record_to_main);
    }
}
