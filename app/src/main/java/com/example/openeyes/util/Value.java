package com.example.openeyes.util;

import androidx.fragment.app.Fragment;

import com.example.openeyes.bean.SortItem;
import com.example.openeyes.bean.VideoItem;

import java.util.ArrayList;
import java.util.List;

public class Value {

    public static final int PAGE_MAIN = 1;
    public static final int PAGE_RANK_WEEKLY = 2;
    public static final int PAGE_RANK_MONTHLY = 3;
    public static final int PAGE_RANK_HISTORICAL = 4;
    public static final int PAGE_SEARCH = 5;
    public static final int PAGE_WELCOME = 6;
    public static final int PAGE_SORT = 7;
    public static final int PAGE_SORT_ITEM_HEADER = 8;
    public static final int PAGE_SORT_ITEM_VIDEO = 9;

    public static List<VideoItem> videoItemList = new ArrayList<>();
    public static List<VideoItem> videoItemList_weeklyRank = new ArrayList<>();
    public static List<VideoItem> videoItemList_monthlyRank = new ArrayList<>();
    public static List<VideoItem> videoItemList_historicalRank = new ArrayList<>();
    public static List<VideoItem> videoItemList_search = new ArrayList<>();
    public static List<VideoItem> videoItemList_record = new ArrayList<>();
    public static List<SortItem> sortItemList = new ArrayList<>();
    public static List<VideoItem> videoItemList_sort = new ArrayList<>();
    public static List<Fragment> fragmentList_rank = new ArrayList<>();

    public static String next_main_page_url = null;
    public static String next_weekly_rank_page_url = null;
    public static String next_monthly_rank_page_url = null;
    public static String next_historical_rank_page_url = null;
    public static String next_search_page_url = null;
    public static String next_sort_page_url = null;

//    public static final String  MAIN_PAGE_URL = "http://baobab.kaiyanapp.com/api/v5/index/tab/feed?udid=c5d770b188ae4ef0b2118b6bfa57241ffaee6f2b&vc=561&deviceModel=OPPO%20R11s%20Plus";
//    public static final String WEEKLY_RANK_PAGE_URL = "http://baobab.kaiyanapp.com/api/v4/rankList/videos?strategy=weekly";
//    public static final String MONTHLY_RANK_PAGE_URL = "http://baobab.kaiyanapp.com/api/v4/rankList/videos?strategy=monthly";
//    public static final String HISTORICAL_RANK_PAGE_URL = "http://baobab.kaiyanapp.com/api/v4/rankList/videos?strategy=historical";
//    public static final String SORT_ITEM_URL = "http://baobab.kaiyanapp.com/api/v4/categories/all?udid=c5d770b188ae4ef0b2118b6bfa57241ffaee6f2b&vc=561&deviceModel=OPPO%20R11s%20Plus";
}
