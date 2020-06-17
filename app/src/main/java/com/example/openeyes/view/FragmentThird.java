package com.example.openeyes.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openeyes.adapter.SortAdapter;
import com.example.openeyes.R;
import com.example.openeyes.util.Value;

public class FragmentThird extends Fragment {
    private final String TAG = "mDebug";
    private View view;
    private SortAdapter adapter;
    private RecyclerView mRecyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.third_fragment_fix,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.sort_recycler_view);
        adapter = new SortAdapter(Value.sortItemList);
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeaderView(position) ? layoutManager.getSpanCount() : 1;
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);

        setHeaderView();
        return view;
    }


    private void setHeaderView() {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.sort_item_header,mRecyclerView,false);
        adapter.setHeaderView(header);
    }
}
