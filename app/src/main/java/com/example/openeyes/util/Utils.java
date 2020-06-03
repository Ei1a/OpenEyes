package com.example.openeyes.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.example.openeyes.view.MainActivity;

public class Utils {
    public Utils(){

    }

    public void adapterUpdateNotify(Context context, final RecyclerView recyclerView, int code ,String url){
        Handler handler = new Handler();
        MainActivity.parseJson_code = code;
        MainActivity.sendHttpRequest(url);
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

}
