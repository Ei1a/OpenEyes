package com.example.openeyes.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.openeyes.R;
import com.example.openeyes.adapter.FragmentAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    String[] tabList = {"登陆", "注册"};

    private ConstraintLayout root;
    private SlidingTabLayout tabLayout;
    private ImageView buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initListener();
    }

    private void initView(){
        //设置状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //得到View引用
        root = (ConstraintLayout) findViewById(R.id.constraint_layout_login);
        buttonBack = (ImageView) findViewById(R.id.button_back);

        //初始化头部 header
        ImageView imageView = (ImageView) findViewById(R.id.image_bg_login);
        Glide.with(LoginActivity.this).load(getDrawable(R.drawable.bg_login_header)).into(imageView);

        //初始化Fragment
        Fragment fragment_login = new FragmentLogin();
        Fragment fragment_register = new FragmentRegister();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(fragment_login);
        fragmentList.add(fragment_register);

        //初始化选项卡
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager_login);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList, tabList);
        tabLayout = (SlidingTabLayout) findViewById(R.id.tab_layout_login);
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager, tabList);
    }

    private void initListener(){
        /*
         * 监听空白点击
         */
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求焦点
                root.requestFocus();
            }
        });

        /*
         * 监听tab选择
         */
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                //请求焦点
                root.requestFocus();
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        /*
         * 监听返回按钮点击
         */
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //若为Enter键，剥夺焦点
        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            root.requestFocus();
        }
        return super.dispatchKeyEvent(event);
    }
}
