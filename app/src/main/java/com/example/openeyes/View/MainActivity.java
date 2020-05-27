package com.example.openeyes.View;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.openeyes.Bean.SortItem;
import com.example.openeyes.Bean.VideoItem;
import com.example.openeyes.R;
import com.example.openeyes.util.RecordDatabaseHelper;
import com.example.openeyes.util.Utils;
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

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "mDebug";
    private DrawerLayout drawerLayout;
    private BottomNavigationView navView;
    private Fragment fragment_1;
    private Fragment fragment_2;
    private Fragment fragment_3;
    public static List<VideoItem> videoItemList = new ArrayList<>();
    public static List<VideoItem> videoItemList_weeklyRank = new ArrayList<>();
    public static List<VideoItem> videoItemList_monthlyRank = new ArrayList<>();
    public static List<VideoItem> videoItemList_historicalRank = new ArrayList<>();
    public static List<VideoItem> videoItemList_search = new ArrayList<>();
    public static List<VideoItem> videoItemList_record = new ArrayList<>();
    public static List<SortItem> sortItemList = new ArrayList<>();
    public static List<VideoItem> videoItemList_sort = new ArrayList<>();
    public static List<Fragment> fragmentList_rank = new ArrayList<>();
    public static final int PARSE_DATA = 1;
    public static int parseJson_code = -1;
    public static final int PAGE_MAIN = 1;
    public static final int PAGE_RANK_WEEKLY = 2;
    public static final int PAGE_RANK_MONTHLY = 3;
    public static final int PAGE_RANK_HISTORICAL = 4;
    public static final int PAGE_SEARCH = 5;
    public static final int PAGE_WELCOME = 6;
    public static final int PAGE_SORT = 7;
    public static final int PAGE_SORT_ITEM_HEADER = 8;
    public static final int PAGE_SORT_ITEM_VIDEO = 9;
    public static String next_main_page_url = null;
    public static String next_weekly_rank_page_url = null;
    public static String next_monthly_rank_page_url = null;
    public static String next_historical_rank_page_url = null;
    public static String next_search_page_url = null;
    public static String next_sort_page_url = null;
//    public static final String  MAIN_PAGE_URL = "http://baobab.wandoujia.com/api/v2/feed?num=2&udid=26868b32e808498db32fd51fb422d00175e179df&vc=83";
//    public static final String WEEKLY_RANK_PAGE_URL = "http://baobab.wandoujia.com/api/v3/ranklist?num=10&strategy=weekly";
//    public static final String MONTHLY_RANK_PAGE_URL = "http://baobab.wandoujia.com/api/v3/ranklist?num=10&strategy=monthly";
//    public static final String HISTORICAL_RANK_PAGE_URL = "http://baobab.wandoujia.com/api/v3/ranklist?num=10&strategy=historical";
    public static final String  MAIN_PAGE_URL = "http://baobab.kaiyanapp.com/api/v5/index/tab/feed?udid=c5d770b188ae4ef0b2118b6bfa57241ffaee6f2b&vc=561&deviceModel=OPPO%20R11s%20Plus";
    public static final String WEEKLY_RANK_PAGE_URL = "http://baobab.kaiyanapp.com/api/v4/rankList/videos?strategy=weekly";
    public static final String MONTHLY_RANK_PAGE_URL = "http://baobab.kaiyanapp.com/api/v4/rankList/videos?strategy=monthly";
    public static final String HISTORICAL_RANK_PAGE_URL = "http://baobab.kaiyanapp.com/api/v4/rankList/videos?strategy=historical";
    public static final String SORT_ITEM_URL = "http://baobab.kaiyanapp.com/api/v4/categories/all?udid=c5d770b188ae4ef0b2118b6bfa57241ffaee6f2b&vc=561&deviceModel=OPPO%20R11s%20Plus";
    public static final String SORT_HEADER_URL = "http://baobab.kaiyanapp.com/api/v6/tag/index?id=";
    public static final String SORT_CONTENT_URL = "http://baobab.kaiyanapp.com/api/v1/tag/videos?id=";
    public static RecordDatabaseHelper dbHelper;
    public static boolean isMainOrSearch = true;
//    public static boolean isFirstLoadmoreOrNot = true;
    private static Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case PARSE_DATA:
                    parseJson((String)msg.obj);
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_4);
        //设置状态栏内容为黑色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //底部导航拦
        initBottomNavigation();
        //toolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //侧滑菜单
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer_nav_view);
        navigationView.setCheckedItem(R.id.guanzhu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.jilu:
//                        Toast.makeText(MainActivity.this,"selected",Toast.LENGTH_SHORT).show();
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
        //初始化Fragment
        initFragment();
        /*
            道理同notifyDataChanged，需要延时
            否则设置默认选中主页也不会显示出item
         */

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navView.setSelectedItemId(R.id.navigation_home);    //设置默认选中主页
            }
        },450);
//        navView.setSelectedItemId(R.id.navigation_home);
        //初始化排行榜界面
        initRecord();

    }

    private void initRecord() {
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
            videoItemList_record.add(videoItem);
            if(RecordActivity.isRecordListNull){
               RecordActivity.isRecordListNull = false;
            }
        }
        cursor.close();
    }

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
//        videoItemList.clear();
//        videoItemList_weeklyRank.clear();
//        videoItemList_monthlyRank.clear();
//        videoItemList_historicalRank.clear();
//        videoItemList_record.clear();
    }

    public static void initRankVideoItem() {
        parseJson_code = PAGE_RANK_WEEKLY;
        sendHttpRequest(WEEKLY_RANK_PAGE_URL);
        Log.d(TAG, "run: code = PAGE RANK WEEKLY");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                parseJson_code = PAGE_RANK_MONTHLY;
                sendHttpRequest(MONTHLY_RANK_PAGE_URL);
                Log.d(TAG, "run: code = PAGE RANK MONTHLY");
            }
        },800);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                parseJson_code = PAGE_RANK_HISTORICAL;
                sendHttpRequest(HISTORICAL_RANK_PAGE_URL);
            }
        },1600);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initSortItem();
            }
        },2000);
    }

    public static void initVideoItem() {
        parseJson_code = PAGE_MAIN;
        sendHttpRequest(MAIN_PAGE_URL);
        Log.d(TAG, "initVideoItem: code = PAGE MAIN");
    }

    public static void initSortItem(){
        parseJson_code = PAGE_SORT;
        sendHttpRequest(SORT_ITEM_URL);
    }

    public static void parseJson(String jsonData) {
        Log.d(TAG, "parseJson: ");
        try {
            if(parseJson_code == PAGE_WELCOME){
                Log.d("welcomeTest", "get in");
                String images = null;
                String imagesUrl = null;
                String imagesTitle = null;
                JSONArray jsonArray = new JSONArray("[" + jsonData + "]");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    images = jsonObject.getString("images");
                }
                /*
                    进入images
                 */
                JSONArray imagesArray = new JSONArray(images);
                for(int i=0;i<imagesArray.length();i++){
                    JSONObject jsonObject = imagesArray.getJSONObject(i);
                    imagesUrl = jsonObject.getString("url");
                    imagesTitle = jsonObject.getString("title");
                }
                WelcomeActivity.imagesUrl = "http://s.cn.bing.net" + imagesUrl;
                Log.d("welcomeTest", WelcomeActivity.imagesUrl);
                WelcomeActivity.imagesTitle = imagesTitle;
            }else if(parseJson_code == PAGE_SORT_ITEM_HEADER){
                Log.d(TAG, "parseJson: into PAGE_SORT_ITEM_HEADER");
                String tagInfo = null;
                String name = null;
                String description = null;
                String bgPicture = null;
                int tagFollowCount = -1;
                int lookCount = -1;
                JSONArray jsonArray = new JSONArray("[" + jsonData + "]");
                tagInfo = jsonArray.getJSONObject(0).getString("tagInfo");
                /*
                进入tagInfo
                 */
                JSONArray tagInfoArray = new JSONArray("[" + tagInfo + "]");
                name = tagInfoArray.getJSONObject(0).getString("name");
                description = tagInfoArray.getJSONObject(0).getString("description");
                bgPicture = tagInfoArray.getJSONObject(0).getString("headerImage");
                tagFollowCount = tagInfoArray.getJSONObject(0).getInt("tagFollowCount");
                lookCount = tagInfoArray.getJSONObject(0).getInt("lookCount");
                Log.d(TAG, "parseJson: PAGE_SORT_ITEM_HEADER " + bgPicture);
                /*
                存入Sort Activity
                 */
                Log.d(TAG, "parseJson: test sortItemList.size " + sortItemList.size());
                for(int i=0;i<sortItemList.size();i++){
                    SortItem temp = sortItemList.get(i);
                    if(temp.getSortName().equals("#"+name)){
                        temp.setHeaderName(name);
                        temp.setBgPicture(bgPicture);
                        temp.setDescription(description);
                        temp.setTagFollowCount(tagFollowCount);
                        temp.setLookCount(lookCount);
                        SortActivity.sortItem = temp;
                        break;
                    }
                }
            }else {
                Log.d(TAG, "parseJson: is not welcome");
//                String issueList = null;
                String itemList = null;
                String data = null;
                String author = null;
                String cover = null;
                JSONArray jsonArray = new JSONArray("[" + jsonData + "]");
//                if (parseJson_code == PAGE_MAIN){
//                    Log.d(TAG, "parseJson: PAGE MAIN");
//                    for(int i=0;i<jsonArray.length();i++){
//                        JSONObject jsonObject =jsonArray.getJSONObject(i);
//                        next_main_page_url = jsonObject.getString("nextPageUrl"); //下一页视频的URL
//                        issueList = jsonObject.getString("issueList");
//                    }
//                    /*
//                        进入issueList
//                    */
//                    JSONArray issueListArray = new JSONArray(issueList);
//                    for(int i=0;i<issueListArray.length();i++){
//                        JSONObject jsonObject = issueListArray.getJSONObject(i);
//                        itemList = jsonObject.getString("itemList");
//                    }
//                }
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                itemList = jsonObject.getString("itemList");
                if(parseJson_code==PAGE_MAIN || parseJson_code==PAGE_RANK_WEEKLY || parseJson_code==PAGE_RANK_MONTHLY
                        || parseJson_code==PAGE_RANK_HISTORICAL || parseJson_code==PAGE_SEARCH || parseJson_code==PAGE_SORT_ITEM_VIDEO){
                    if(parseJson_code == PAGE_RANK_WEEKLY){
                        next_weekly_rank_page_url = jsonObject.getString("nextPageUrl");
                    } else if (parseJson_code == PAGE_RANK_MONTHLY){
                        next_monthly_rank_page_url = jsonObject.getString("nextPageUrl");
                    } else if (parseJson_code == PAGE_RANK_HISTORICAL){
                        next_historical_rank_page_url = jsonObject.getString("nextPageUrl");
                    } else if (parseJson_code == PAGE_MAIN){
                        next_main_page_url = jsonObject.getString("nextPageUrl");
                    } else if (parseJson_code == PAGE_SORT_ITEM_VIDEO){
                        next_sort_page_url = jsonObject.getString("nextPageUrl");
                    } else {
                        next_search_page_url = jsonObject.getString("nextPageUrl");
                    }
                }
                /*
                进入itemList
                */
                JSONArray itemListArray = new JSONArray(itemList);
                Log.d(TAG, "parseJson: into itemlist");
                for(int i=0;i<itemListArray.length();i++){
                    JSONObject jsonObject_itemList = itemListArray.getJSONObject(i);
                    String type = jsonObject_itemList.getString("type");
                    if(parseJson_code==PAGE_MAIN || parseJson_code==PAGE_SORT_ITEM_VIDEO){
                        String content = null;
                        if(!type.equals("followCard")){//判断type类型
                            continue;
                        }
                        data = jsonObject_itemList.getString("data");
                        /*
                        进入PAGE_MAIN的data
                         */
                        JSONArray mainDataArray = new JSONArray("[" + data + "]");
                        content = mainDataArray.getJSONObject(0).getString("content");
                        /*
                        进入content
                         */
                        JSONArray contentArray = new JSONArray("[" + content + "]");
                        data = contentArray.getJSONObject(0).getString("data");

                    }else if(parseJson_code == PAGE_SORT){
                        data = jsonObject_itemList.getString("data");
                    }else {
                        if(!type.equals("video")){//判断type类型
                            continue;
                        }
                        data = jsonObject_itemList.getString("data");
                    }
                /*
                    进入data
                 */
                    JSONArray dataArray = new JSONArray("[" + data + "]");
                    Log.d(TAG, "parseJson: Into data");
                    for(int j=0;j<dataArray.length();j++){
                        JSONObject jsonObject_data = dataArray.getJSONObject(j);
                        if(parseJson_code == PAGE_SORT){
                            Log.d(TAG, "parseJson: PAGE SORT");
                            String sort_image = null;
                            String sort_name = null;
                            String sort_ID_origin = null;
                            String sort_ID = null;
                            sort_name = jsonObject_data.getString("title");
                            if(sort_name.equals("")){
                                continue;
                            }
                            sort_image = jsonObject_data.getString("image");
                            sort_ID_origin = jsonObject_data.getString("actionUrl");
                            /*
                            提取ID
                             */
                            sort_ID = new Utils().parseID(sort_ID_origin);
                            sortItemList.add(new SortItem(sort_ID,sort_image,sort_name));
                        }
                        else {
                            String video_author_head_icon = null;
                            String video_author_name = null;
                            String video_author_description = null;
                            String video_cover_feed_url = null;
                            String video_cover_blurred_url = null;
                            String video_title = jsonObject_data.getString("title");
                            String video_description = jsonObject_data.getString("description");
                            String video_tag = jsonObject_data.getString("category");
                            author = jsonObject_data.getString("author");
                            cover = jsonObject_data.getString("cover");
                            String video_play_url = jsonObject_data.getString("playUrl");
                            /*
                            进入author
                            */
                            JSONArray authorArray = new JSONArray("[" + author + "]");
                            for(int k=0;k<authorArray.length();k++){
                                JSONObject jsonObject_data_author = authorArray.getJSONObject(k);
                                video_author_head_icon = jsonObject_data_author.getString("icon");
                                video_author_name = jsonObject_data_author.getString("name");
                                video_author_description = jsonObject_data_author.getString("description");
                            }
                            /*
                            进入cover
                            */
                            JSONArray coverArray = new JSONArray("[" + cover + "]");
                            for(int k=0;k<coverArray.length();k++){
                                JSONObject jsonObject_data_cover = coverArray.getJSONObject(k);
                                video_cover_feed_url = jsonObject_data_cover.getString("feed");
                                video_cover_blurred_url = jsonObject_data_cover.getString("blurred");
                            }
                            /*
                            加入VideoItem
                            */
                            VideoItem videoItem = new VideoItem(video_cover_feed_url,video_author_head_icon,
                                    video_title,video_author_name,video_tag,video_play_url,video_description,
                                    video_author_description,video_cover_blurred_url);
                            if(parseJson_code == PAGE_MAIN){
                                videoItemList.add(videoItem);
                                Log.d(TAG, "parseJson: videoItemList add");
                            }
                            if(parseJson_code == PAGE_RANK_WEEKLY){
                                videoItemList_weeklyRank.add(videoItem);
                                Log.d(TAG, "parseJson: weekly rank list add");
                            }
                            if(parseJson_code == PAGE_RANK_MONTHLY){
                                videoItemList_monthlyRank.add(videoItem);
                                Log.d(TAG, "parseJson: monthly rank list add");
                            }
                            if(parseJson_code == PAGE_RANK_HISTORICAL){
                                videoItemList_historicalRank.add(videoItem);
                                Log.d(TAG, "parseJson: historical rank list add");
                            }
                            if(parseJson_code == PAGE_SEARCH){
                                videoItemList_search.add(videoItem);
                                Log.d(TAG, "parseJson: search list add");
                            }
                            if(parseJson_code == PAGE_SORT_ITEM_VIDEO){
                                videoItemList_sort.add(videoItem);
                            }
                        }
                    }
                }
            }
            parseJson_code = -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendHttpRequest(final String page_url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Log.d(TAG, page_url);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(page_url)
                            .build();
                    Response response = client.newCall(request).execute();
                    Log.d(TAG, "run: ");
                    String responseData = response.body().string();
                    Log.d(TAG,responseData);
                    //发送解析请求
                    Message message = new Message();
                    message.what = PARSE_DATA;
                    message.obj = responseData;
                    handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void initFragment() {
        fragment_1 = new FragmentFirst();
        fragment_2 = new FragmentSecond();
        fragment_3 = new FragmentThird();
        fragmentList_rank.add(new FragmentSecondOne());
        fragmentList_rank.add(new FragmentSecondTwo());
        fragmentList_rank.add(new FragmentSecondThree());
    }

    private void repalceFragment(Fragment fragment){
        Log.d(TAG, "repalceFragment: ");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.wait_replace,fragment);
        transaction.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

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
}
