package com.example.openeyes.view;

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

import com.example.openeyes.adapter.VideoAdapter;
import com.example.openeyes.R;
import com.example.openeyes.util.Utils;

public class FragmentFirst extends Fragment {
    private VideoAdapter adapter;
    private RecyclerView mRecyclerView;
    private Handler handler;
    private MainActivity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment,container,false);
        mRecyclerView = view.findViewById(R.id.video_item_recycler_view);
        adapter = new VideoAdapter(MainActivity.videoItemList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                /*
                    滑动到底部自动加载
                 */
                LinearLayoutManager lm = (LinearLayoutManager)recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();
                if(newState==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition==totalItemCount-1 && visibleItemCount>0){
                    activity = (MainActivity) getActivity();
                    if(!activity.next_main_page_url.equals("null")){
                        Utils utils = new Utils();
                        utils.adapterUpdateNotify(activity,mRecyclerView,activity.PAGE_MAIN,activity.next_main_page_url);
                    }
                }
            }
        });
        return view;
    }
}
