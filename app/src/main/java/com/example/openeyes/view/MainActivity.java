package com.example.openeyes.view;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.openeyes.bean.LoginEvent;
import com.example.openeyes.bean.SortItem;
import com.example.openeyes.bean.VideoItem;
import com.example.openeyes.R;
import com.example.openeyes.util.RecordDatabaseHelper;
import com.example.openeyes.util.Utils;
import com.example.openeyes.util.Value;
import com.example.openeyes.viewmodel.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    /*
     * Tag
     */
    private static final String TAG = "mDebug";
    private boolean isSignIn = false;

    /*
     * View
     */
    private DrawerLayout drawerLayout;
    private BottomNavigationView navView;

    /*
     * Fragment
     */
    private Fragment fragment_1;
    private Fragment fragment_2;
    private Fragment fragment_3;
    private Fragment preFragment;

    public static RecordDatabaseHelper dbHelper;
    public static boolean isMainOrSearch = true;
    public List<Fragment> fragmentList_rank = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_4);
        initView();
        initData();
    }

    /*
     * 初始化View
     */
    private void initView(){
        //设置状态栏内容为黑色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //注册Eventbus
        EventBus.getDefault().register(this);

        //toolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //侧滑菜单
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置Actionbar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        /*
         * 侧滑菜单
         */
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer_nav_view);
        navigationView.setCheckedItem(R.id.guanzhu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.jilu:
                        Intent intent = new Intent(MainActivity.this,RecordActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.mian_to_record,R.anim.fix_close);
                        break;
                    default:
                        Toast.makeText(MainActivity.this,"正在完善嗷~",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        /*
         * 侧滑headr头像点击监听
         */
//        Log.d("headViewTest", navigationView.getHeaderView(0)+"");
        CircleImageView headIconImage = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.icon_image);
        headIconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                /*
                 * 判断是否登陆
                 */
                if(isSignIn){
                    //已登陆，跳转个人资料
                    intent = new Intent(MainActivity.this, PersonImformationActivity.class);
                }else{
                    //未登录，跳转登陆界面
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                startActivity(intent);
            }
        });
        //初始化Fragment
        initFragment();
        //底部导航拦
        initBottomNavigation();
    }

    /*
     * 初始化数据
     */
    private void initData(){
        dbHelper = new RecordDatabaseHelper(MainActivity.this,"Record.db",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        db.delete("RecordItem",null,null);    //清楚记录
        Cursor cursor = db.query("RecordItem",null,null,null,null,null,null);
        while(cursor.moveToNext()){
            String imageUrl = cursor.getString(cursor.getColumnIndex("imageUrl"));
            String headIconUrl = cursor.getString(cursor.getColumnIndex("headIconUrl"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String authorName = cursor.getString(cursor.getColumnIndex("authorName"));
            String tag = cursor.getString(cursor.getColumnIndex("tag"));
            String playUrl = cursor.getString(cursor.getColumnIndex("playUrl"));
            String videoDescription = cursor.getString(cursor.getColumnIndex("videoDescription"));
            String authorDescription = cursor.getString(cursor.getColumnIndex("authorDescription"));
            String backgroundUrl = cursor.getString(cursor.getColumnIndex("backgroundUrl"));
            VideoItem videoItem = new VideoItem(imageUrl,headIconUrl,title,authorName,tag,playUrl,videoDescription,authorDescription,backgroundUrl);
            Value.videoItemList_record.add(videoItem);
            if(RecordActivity.isRecordListNull){
                RecordActivity.isRecordListNull = false;
            }
        }
        cursor.close();
    }

    /*
     * 初始化BottomNavigationView
     */
    private void initBottomNavigation() {
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        repalceFragment(fragment_1);
                        return true;
                    case R.id.navigation_dashboard:
                        repalceFragment(fragment_2);
                        return true;
                    case R.id.navigation_notifications:
                        repalceFragment(fragment_3);
                        return true;
                }
                return false;
            }
        });
        //默认选中首页
        navView.setSelectedItemId(R.id.navigation_home);
    }

    /*
     * 初始化Fragment
     */
    private void initFragment() {
        fragment_1 = new FragmentFirst();
        fragment_2 = new FragmentSecond();
        fragment_3 = new FragmentThird();
        Value.fragmentList_rank.add(new FragmentSecondOne());
        Value.fragmentList_rank.add(new FragmentSecondTwo());
        Value.fragmentList_rank.add(new FragmentSecondThree());
    }

    /*
     * 替换Fragment
     */
    private void repalceFragment(Fragment fragment){
        Log.d(TAG, "repalceFragment: ");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(preFragment != null){
            transaction.hide(preFragment);
        }
        if(fragment.isAdded()){
            transaction.show(fragment);
        }else{
            transaction.add(R.id.wait_replace,fragment);
//            transaction.replace(R.id.wait_replace,fragment);
        }
        transaction.commit();
        preFragment = fragment;
    }

    /*
     * Toolbar菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    /*
     * Toolbar菜单项点击事件
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_open:
                Log.d(TAG, "onOptionsItemSelected: clicked search");
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open,R.anim.fix_close);
                break;
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: clicked home");
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event){
        isSignIn = event.getStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销EventBus
        EventBus.getDefault().unregister(this);
    }
}
