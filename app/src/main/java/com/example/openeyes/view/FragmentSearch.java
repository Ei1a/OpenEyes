package com.example.openeyes.view;

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

import com.example.openeyes.adapter.VideoAdapter;
import com.example.openeyes.R;
import com.example.openeyes.util.Utils;
import com.example.openeyes.util.Value;

public class FragmentSearch extends Fragment {
    private RecyclerView mRecyclerView;
    private VideoAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_result_fragment,container,false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.search_result_video_item_recycler_view);
        adapter = new VideoAdapter(Value.videoItemList_search);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();
                if(newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition==totalItemCount-1 && visibleItemCount>0){
                    if(!Value.next_search_page_url.equals("null")){
                        Utils utils = new Utils();
                        utils.adapterUpdateNotify(getActivity(),mRecyclerView,Value.PAGE_SEARCH,Value.next_search_page_url);
                    }else {
                        Toast.makeText(getActivity(),"没有更多数据了噢",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }
}
