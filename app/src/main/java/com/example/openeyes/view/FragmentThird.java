package com.example.openeyes.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openeyes.adapter.SortAdapter;
import com.example.openeyes.R;
import com.example.openeyes.bean.SortItem;
import com.example.openeyes.util.Value;
import com.example.openeyes.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentThird extends Fragment {
    private SortAdapter adapter;
    private RecyclerView mRecyclerView;
    private List<SortItem> sortItemList = new ArrayList<>();
    private MainViewModel mViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // -> 待优化
        if(mViewModel == null){
            mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_fragment_fix,container,false);
        initView(view);
        initObserver();
        initData();
        setHeaderView();
        return view;
    }

    /*
     * 初始化View
     */
    private void initView(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.sort_recycler_view);
        adapter = new SortAdapter(sortItemList);
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        /*
         * 根据是否是header，动态改变列数
         */
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeaderView(position) ? layoutManager.getSpanCount() : 1;
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    /*
     * 初始化LiveData观察
     */
    private void initObserver(){
        mViewModel.requestSortKindItemResult.observe(this, new Observer<List<SortItem>>() {
            @Override
            public void onChanged(List<SortItem> sortItems) {
                sortItemList.addAll(sortItems);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    /*
     * 初始化数据
     */
    private void initData(){
        if(sortItemList.isEmpty()){
            mViewModel.sendHttpRequest(mViewModel.SORT_ITEM_URL, mViewModel.PAGE_SORT);
        }
    }

    /*
     * 设置RecyclerView headr
     */
    private void setHeaderView() {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.sort_item_header,mRecyclerView,false);
        adapter.setHeaderView(header);
    }
}
