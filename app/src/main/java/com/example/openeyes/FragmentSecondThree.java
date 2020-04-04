package com.example.openeyes;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentSecondThree extends Fragment {
    private RecyclerView mRecyclerView;
    private VideoAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_three_fragment,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.historical_video_item_recycler_view);
        adapter = new VideoAdapter(MainActivity.videoItemList_historicalRank);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int visibleItemCount = recyclerView.getChildCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                if(newState==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition==totalItemCount-1 && visibleItemCount>0){
                    MainActivity activity = (MainActivity)getActivity();
                    if(!activity.next_historical_rank_page_url.equals("null")){
                        activity.parseJson_code = activity.PAGE_RANK_HISTORICAL;
                        activity.sendHttpRequest(activity.next_historical_rank_page_url);
                    }
                    /*
                        因为请求和解析需要耗时，而且sendHttpRequest是在子线程中执行的，所以如果立即notify的话，数据还并没有加载好
                        因此需要适当延时再notify
                     */
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    },300);
                }
            }
        });
        return view;
    }
}
