package com.example.openeyes.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openeyes.adapter.VideoAdapter;
import com.example.openeyes.R;
import com.example.openeyes.bean.VideoItem;
import com.example.openeyes.util.Utils;
import com.example.openeyes.util.Value;
import com.example.openeyes.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentSecondTwo extends Fragment {
    private RecyclerView mRecyclerView;
    private MainViewModel mViewModel;
    private List<VideoItem> videoItemList = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(mViewModel == null){
            // -> 待优化
            mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_two_fragment,container,false);
        initView(view);
        initObserver();
        initData();
        initListener();
        return view;
    }

    /*
     * 初始化View
     */
    private void initView(View view){
        mRecyclerView = view.findViewById(R.id.monthly_video_item_recycler_view);
        VideoAdapter adapter = new VideoAdapter(videoItemList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
        mRecyclerView.setAdapter(adapter);
    }

    /*
     * 初始化LiveData观察
     */
    private void initObserver(){
        mViewModel.requestRankMonthlyItemResult.observe(this, new Observer<List<VideoItem>>() {
            @Override
            public void onChanged(List<VideoItem> videoItems) {
                videoItemList.addAll(videoItems);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    /*
     * 初始化数据
     */
    private void initData() {
        if(videoItemList.isEmpty()){
            mViewModel.sendHttpRequest(mViewModel.MONTHLY_RANK_PAGE_URL, mViewModel.PAGE_RANK_MONTHLY);
        }
    }

    /*
     * 初始化监听器
     */
    private void initListener() {
        /*
         * 滑动到底部加载更多
         */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();
                if(newState==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition==totalItemCount-1 &&visibleItemCount>0 ){
                    if(!mViewModel.next_monthly_rank_page_url.equals("null")){
                        mViewModel.sendHttpRequest(mViewModel.next_monthly_rank_page_url, mViewModel.PAGE_RANK_MONTHLY);
                    }else {
                        Toast.makeText(getContext(),"没有更多数据了噢",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
