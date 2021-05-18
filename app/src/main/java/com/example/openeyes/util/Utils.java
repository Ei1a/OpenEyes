package com.example.openeyes.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openeyes.bean.LocationReuslt;
import com.example.openeyes.bean.SortItem;
import com.example.openeyes.bean.VideoItem;
import com.example.openeyes.view.WelcomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Utils {
    private static final String TAG = "mDebug";
    private static final int PARSE_DATA = 1;

    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case PARSE_DATA:
                    parseJson((String)msg.obj,msg.arg1);
            }
        }
    };
    public Utils(){

    }

    public void adapterUpdateNotify(Context context, final RecyclerView recyclerView, int code ,String url){
        Handler handler = new Handler();
        sendHttpRequest(url,code);
        final Toast toast = Toast.makeText(context,"玩命加载中...", Toast.LENGTH_SHORT);
        toast.show();
        /*
        因为请求和解析需要耗时，而且sendHttpRequest是在子线程中执行的，所以如果立即notify的话，数据还并没有加载好
        因此需要适当延时再notify
        */
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.getAdapter().notifyDataSetChanged();
                toast.setText("好咯");
            }
        },300);
    }

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

    public static void sendHttpRequest(String url,int parseCode){
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
//                    Log.d(TAG,responseData);
                    //发送解析请求
                    Message message = new Message();
                    message.what = PARSE_DATA;
                    message.obj = responseData;
                    message.arg1 = parseCode;
                    handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void parseJson(String jsonData,int parseCode){
        try {
            if(parseCode == Value.PAGE_WELCOME){
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
            }else if(parseCode == Value.PAGE_SORT_ITEM_HEADER){
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
//                /*
//                存入Sort Activity
//                 */
//                Log.d(TAG, "parseJson: test sortItemList.size " + Value.sortItemList.size());
//                for(int i=0;i<Value.sortItemList.size();i++){
//                    SortItem temp = Value.sortItemList.get(i);
//                    if(temp.getSortName().equals("#"+name)){
//                        temp.setHeaderName(name);
//                        temp.setBgPicture(bgPicture);
//                        temp.setDescription(description);
//                        temp.setTagFollowCount(tagFollowCount);
//                        temp.setLookCount(lookCount);
//                        SortActivity.sortItem = temp;
//                        break;
//                    }
//                }
            }else {
                Log.d(TAG, "parseJson: is not welcome");
                String itemList = null;
                String data = null;
                String author = null;
                String cover = null;
                String consumption = null;
                JSONArray jsonArray = new JSONArray("[" + jsonData + "]");
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                itemList = jsonObject.getString("itemList");
                if(parseCode==Value.PAGE_MAIN || parseCode==Value.PAGE_RANK_WEEKLY || parseCode==Value.PAGE_RANK_MONTHLY
                        || parseCode==Value.PAGE_RANK_HISTORICAL || parseCode==Value.PAGE_SEARCH || parseCode==Value.PAGE_SORT_ITEM_VIDEO){
                    if(parseCode == Value.PAGE_RANK_WEEKLY){
                        Value.next_weekly_rank_page_url = jsonObject.getString("nextPageUrl");
                    } else if (parseCode == Value.PAGE_RANK_MONTHLY){
                        Value.next_monthly_rank_page_url = jsonObject.getString("nextPageUrl");
                    } else if (parseCode == Value.PAGE_RANK_HISTORICAL){
                        Value.next_historical_rank_page_url = jsonObject.getString("nextPageUrl");
                    } else if (parseCode == Value.PAGE_MAIN){
                        Value.next_main_page_url = jsonObject.getString("nextPageUrl");
                    } else if (parseCode == Value.PAGE_SORT_ITEM_VIDEO){
                        Value.next_sort_page_url = jsonObject.getString("nextPageUrl");
                    } else {
                        Value.next_search_page_url = jsonObject.getString("nextPageUrl");
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
                    if(parseCode==Value.PAGE_MAIN || parseCode==Value.PAGE_SORT_ITEM_VIDEO){
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

                    }else if(parseCode == Value.PAGE_SORT){
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
                        if(parseCode == Value.PAGE_SORT){
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
                            Value.sortItemList.add(new SortItem(sort_ID,sort_image,sort_name));
                        }
                        else {
                            String video_author_head_icon = null;
                            String video_author_name = null;
                            String video_author_description = null;
                            String video_cover_feed_url = null;
                            String video_cover_blurred_url = null;
                            int video_like_counts = -1;
                            int video_share_counts = -1;
                            int video_id = jsonObject_data.getInt("id");
                            String video_title = jsonObject_data.getString("title");
                            String video_description = jsonObject_data.getString("description");
                            String video_tag = jsonObject_data.getString("category");
                            author = jsonObject_data.getString("author");
                            cover = jsonObject_data.getString("cover");
                            consumption = jsonObject_data.getString("consumption");
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
                             * 进入consumption
                             */
                            JSONObject jsonObject_data_consumption = new JSONObject(consumption);
                            video_like_counts = jsonObject_data_consumption.getInt("collectionCount");
                            video_share_counts = jsonObject_data_consumption.getInt("shareCount");

                            /*
                            加入VideoItem
                            */
                            VideoItem videoItem = new VideoItem(video_id,video_cover_feed_url,video_author_head_icon,
                                    video_title,video_author_name,video_tag,video_play_url,video_description,
                                    video_author_description,video_cover_blurred_url,video_like_counts
                                    ,video_share_counts);
                            if(parseCode == Value.PAGE_MAIN){
                                Value.videoItemList.add(videoItem);
                                Log.d(TAG, "parseJson: videoItemList add");
                            }
                            if(parseCode == Value.PAGE_RANK_WEEKLY){
                                Value.videoItemList_weeklyRank.add(videoItem);
                                Log.d(TAG, "parseJson: weekly rank list add");
                            }
                            if(parseCode == Value.PAGE_RANK_MONTHLY){
                                Value.videoItemList_monthlyRank.add(videoItem);
                                Log.d(TAG, "parseJson: monthly rank list add");
                            }
                            if(parseCode == Value.PAGE_RANK_HISTORICAL){
                                Value.videoItemList_historicalRank.add(videoItem);
                                Log.d(TAG, "parseJson: historical rank list add");
                            }
                            if(parseCode == Value.PAGE_SEARCH){
                                Value.videoItemList_search.add(videoItem);
                                Log.d(TAG, "parseJson: search list add");
                            }
                            if(parseCode == Value.PAGE_SORT_ITEM_VIDEO){
                                Value.videoItemList_sort.add(videoItem);
                            }
                        }
                    }
                }
            }
//            parseJson_code = -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 解析全国地区名
     */
    public static LocationReuslt parseLocation(){
        try {
            //读入全国地区Json
            String jsonData = readTxtFile();
            //变量声明
            String province;
            String city;
            String county;

            //关联省级
            List<String> provincesList = new ArrayList<>();
            //关联区级
            List<List<List<String>>> countiesList = new ArrayList<>();
            //关联市级
            List<List<String>> citiesList = new ArrayList<>();

            /*
             * 开始解析
             */
            JSONArray jsonArray = new JSONArray("[" + jsonData + "]");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String provinceList = jsonObject.getString("provinceList");
            /*
             * 进入provinceList
             */
            JSONArray provinceListArray = new JSONArray(provinceList);
            for(int i=0;i<provinceListArray.length();i++){
                JSONObject object_province = provinceListArray.getJSONObject(i);
                province = object_province.getString("name");
                //关联省级
                provincesList.add(province);
                String cityList = object_province.getString("cityList");
                /*
                 * 进入cityList
                 */
                JSONArray cityListArray = new JSONArray(cityList);
                //关联区级
                List<List<String>> list2 = new ArrayList<>();
                //关联市级
                List<String> list3 = new ArrayList<>();
                for(int j=0;j<cityListArray.length();j++){
                    JSONObject object_city = cityListArray.getJSONObject(j);
                    city = object_city.getString("name");
                    list3.add(city);
                    String countyList = object_city.getString("countyList");
                    /*
                     * 进入countyList
                     */
                    JSONArray countyListArray = new JSONArray(countyList);
                    //关联区级
                    List<String> list1 = new ArrayList<>();
                    for(int z=0;z<countyListArray.length();z++){
                        JSONObject object_county = countyListArray.getJSONObject(z);
                        county = object_county.getString("name");
                        if(!county.equals("市辖区")){
                            //关联区级
                            list1.add(county);
                        }
                    }
                    //关联区级
                    list2.add(list1);
                }
                //关联区级
                countiesList.add(list2);
                //关联市级
                citiesList.add(list3);
            }
            //完成解析，返回结果
            return new LocationReuslt(provincesList, citiesList, countiesList);
        } catch (JSONException e) {
            e.printStackTrace();
            //非正常结束，返回null
            return null;
        }
    }

    /*
     * 读取文件
     */
    private static String readTxtFile(){
        String fileName = "/sdcard/Download/LocationApi.txt";
        File file = new File(fileName);
        StringBuffer content = new StringBuffer();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while((line = bufferedReader.readLine()) != null){
                content.append(line);
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

}
