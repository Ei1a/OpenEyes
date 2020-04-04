package com.example.openeyes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.SearchManager;
import android.drm.DrmStore;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {
    private String KEYWORD = null;
    private Handler handler = new Handler();
    private FragmentSearch fragmentSearch;
    private TextView textTips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        MainActivity.isMainOrSearch = false;
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //初始化Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_search,menu);
        SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("请输入关键字");
        EditText searchEditText = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(Color.GRAY);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fragmentSearch = new FragmentSearch();
                textTips = (TextView)findViewById(R.id.text_tips);
                KEYWORD = query;
                String KEYWORD_URL="http://baobab.kaiyanapp.com/api/v1/search?num=10&query=" + KEYWORD + "&start=10";
                MainActivity.videoItemList_search.clear();  //避免叠加之前搜素的数据
                MainActivity.parseJson_code = MainActivity.PAGE_SEARCH;
                Log.d("mDebug", KEYWORD_URL);
                MainActivity.sendHttpRequest(KEYWORD_URL);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textTips.setVisibility(View.GONE);
                        replaceFragment(fragmentSearch);
                    }
                },200);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void replaceFragment(FragmentSearch fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.wait_replace_in_search,fragment);
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
        MainActivity.isMainOrSearch = true;
        overridePendingTransition(R.anim.fix_open,R.anim.activity_close);
    }
}
