package com.example.openeyes.viewmodel;

import android.os.Message;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.openeyes.bean.SortItem;
import com.example.openeyes.bean.SortItemHeader;
import com.example.openeyes.bean.VideoItem;
import com.example.openeyes.util.Utils;
import com.example.openeyes.util.Value;
import com.example.openeyes.view.SortActivity;
import com.example.openeyes.view.WelcomeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainViewModel extends ViewModel {
    private final String TAG = "mDebug";

    /*
     * URL
     */
    public final String  MAIN_PAGE_URL = "http://baobab.kaiyanapp.com/api/v5/index/tab/feed?udid=c5d770b188ae4ef0b2118b6bfa57241ffaee6f2b&vc=561&deviceModel=OPPO%20R11s%20Plus";
    public final String WEEKLY_RANK_PAGE_URL = "http://baobab.kaiyanapp.com/api/v4/rankList/videos?strategy=weekly";
    public final String MONTHLY_RANK_PAGE_URL = "http://baobab.kaiyanapp.com/api/v4/rankList/videos?strategy=monthly";
    public final String HISTORICAL_RANK_PAGE_URL = "http://baobab.kaiyanapp.com/api/v4/rankList/videos?strategy=historical";
    public final String SORT_ITEM_URL = "http://baobab.kaiyanapp.com/api/v4/categories/all?udid=c5d770b188ae4ef0b2118b6bfa57241ffaee6f2b&vc=561&deviceModel=OPPO%20R11s%20Plus";
    public final String SORT_PAGE_HEADER_URL = "http://baobab.kaiyanapp.com/api/v6/tag/index?id=";
    public final String SORT_PAGE_VIDEO_URL = "http://baobab.kaiyanapp.com/api/v1/tag/videos?id=";

    /*
     * 页面标识
     */
    public final int PAGE_MAIN = 1;
    public final int PAGE_RANK_WEEKLY = 2;
    public final int PAGE_RANK_MONTHLY = 3;
    public final int PAGE_RANK_HISTORICAL = 4;
    public final int PAGE_SEARCH = 5;
    public final int PAGE_WELCOME = 6;
    public final int PAGE_SORT = 7;
    public final int PAGE_SORT_ITEM_HEADER = 8;
    public final int PAGE_SORT_ITEM_VIDEO = 9;

    /*
     * 各页面下一页url
     */
    public String next_main_page_url = "null";
    public String next_weekly_rank_page_url = "null";
    public String next_monthly_rank_page_url = "null";
    public String next_historical_rank_page_url = "null";
    public String next_search_page_url = "null";
    public String next_sort_page_url = "null";

    /*
     * livedata
     */
    public MutableLiveData<List<VideoItem>> requestMainItemResult = new MutableLiveData<>();
    public MutableLiveData<List<VideoItem>> requestRankWeeklyItemResult = new MutableLiveData<>();
    public MutableLiveData<List<VideoItem>> requestRankMonthlyItemResult = new MutableLiveData<>();
    public MutableLiveData<List<VideoItem>> requestRankHistoricalItemResult = new MutableLiveData<>();
    public MutableLiveData<List<VideoItem>> requestSearchItemResult = new MutableLiveData<>();
    public MutableLiveData<List<VideoItem>> requestSortItemResult = new MutableLiveData<>();
    public MutableLiveData<List<SortItem>> requestSortKindItemResult = new MutableLiveData<>();
    public MutableLiveData<SortItemHeader> requestSortItemHeaderResult = new MutableLiveData<>();

    /*
     * 发起网络请求
     */
    public void sendHttpRequest(String url,int parseCode){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    if(response.isSuccessful()){
                        //请求成功
                        parseJson(responseData, parseCode);
                    }else{
                        //请求失败
                        // -> 待优化
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*
     * 解析json
     */
    private void parseJson(String jsonData,int parseCode){
        try {
            List<VideoItem> videoItems = new ArrayList<>();
            List<SortItem> sortItemList = new ArrayList<>();
            if(parseCode == PAGE_WELCOME){
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
            }else if(parseCode == PAGE_SORT_ITEM_HEADER){
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
                //post到主线程
                requestSortItemHeaderResult.postValue(new SortItemHeader(name, description, bgPicture, tagFollowCount, lookCount));

            }else {
                Log.d(TAG, "parseJson: is not welcome");
                String itemList = null;
                String data = null;
                String author = null;
                String cover = null;
                JSONArray jsonArray = new JSONArray("[" + jsonData + "]");
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                itemList = jsonObject.getString("itemList");
                if(parseCode==PAGE_MAIN || parseCode==PAGE_RANK_WEEKLY || parseCode==PAGE_RANK_MONTHLY
                        || parseCode==PAGE_RANK_HISTORICAL || parseCode==PAGE_SEARCH || parseCode==PAGE_SORT_ITEM_VIDEO){
                    if(parseCode == PAGE_RANK_WEEKLY){
                        next_weekly_rank_page_url = jsonObject.getString("nextPageUrl");
                    } else if (parseCode == PAGE_RANK_MONTHLY){
                        next_monthly_rank_page_url = jsonObject.getString("nextPageUrl");
                    } else if (parseCode == PAGE_RANK_HISTORICAL){
                        next_historical_rank_page_url = jsonObject.getString("nextPageUrl");
                    } else if (parseCode == PAGE_MAIN){
                        next_main_page_url = jsonObject.getString("nextPageUrl");
                    } else if (parseCode == PAGE_SORT_ITEM_VIDEO){
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
                    if(parseCode==PAGE_MAIN || parseCode==PAGE_SORT_ITEM_VIDEO){
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

                    }else if(parseCode == PAGE_SORT){
                        data = jsonObject_itemList.getString("data");
                    }else {
                        if(!type.equals("video")){//判断type类型
                            continue;
                        }
                        data = jsonObject_itemList.getString("data");
                    }
                    /*
                     * 进入data
                     */
                    JSONArray dataArray = new JSONArray("[" + data + "]");
                    Log.d(TAG, "parseJson: Into data");
                    for(int j=0;j<dataArray.length();j++){
                        JSONObject jsonObject_data = dataArray.getJSONObject(j);
                        if(parseCode == PAGE_SORT){
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
                            sort_ID = parseID(sort_ID_origin);
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
                            if(!author.equals("null")){
                                JSONArray authorArray = new JSONArray("[" + author + "]");
                                for(int k=0;k<authorArray.length();k++){
                                    JSONObject jsonObject_data_author = authorArray.getJSONObject(k);
                                    video_author_head_icon = jsonObject_data_author.getString("icon");
                                    video_author_name = jsonObject_data_author.getString("name");
                                    video_author_description = jsonObject_data_author.getString("description");
                                }
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
                             * 加入VideoItem
                             */
                            VideoItem videoItem = new VideoItem(video_cover_feed_url,video_author_head_icon,
                                    video_title,video_author_name,video_tag,video_play_url,video_description,
                                    video_author_description,video_cover_blurred_url);
                            /*
                             * 放入list
                             */
                            videoItems.add(videoItem);
                        }
                    }
                }
            }
            /*
             * 解析完毕post到主线程
             */
            switch(parseCode){
                case PAGE_MAIN:
                    requestMainItemResult.postValue(videoItems);
                    break;
                case PAGE_RANK_WEEKLY:
                    requestRankWeeklyItemResult.postValue(videoItems);
                    break;
                case PAGE_RANK_MONTHLY:
                    requestRankMonthlyItemResult.postValue(videoItems);
                    break;
                case PAGE_RANK_HISTORICAL:
                    requestRankHistoricalItemResult.postValue(videoItems);
                    break;
                case PAGE_SEARCH:
                    requestSearchItemResult.postValue(videoItems);
                    break;
                case PAGE_SORT_ITEM_VIDEO:
                    requestSortItemResult.postValue(videoItems);
                    break;
                case PAGE_SORT:
                    requestSortKindItemResult.postValue(sortItemList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 解析id
     */
    public String parseID(String origin){
        boolean isDigit = false;
        String[] result = origin.split("/");
        for(String temp : result){
            for(int i=0;i<temp.length();i++){
                if(!Character.isDigit(temp.charAt(i))){
                    isDigit = false;
                    break;
                }
                isDigit = true;
            }
            if(isDigit){
                return temp;
            }
        }
        return null;
    }
}
