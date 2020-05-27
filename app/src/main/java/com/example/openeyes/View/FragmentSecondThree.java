package com.example.openeyes.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openeyes.Adapter.VideoAdapter;
import com.example.openeyes.R;
import com.example.openeyes.util.Utils;

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
                        Utils utils = new Utils();
                        utils.adapterUpdateNotify(activity,mRecyclerView,activity.PAGE_RANK_MONTHLY,activity.next_historical_rank_page_url);
                    }else {
                        Toast.makeText(activity,"没有更多数据了噢",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }
}
