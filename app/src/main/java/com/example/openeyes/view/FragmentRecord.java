package com.example.openeyes.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openeyes.adapter.VideoAdapterRecord;
import com.example.openeyes.R;
import com.example.openeyes.model.RecordVideoDao;
import com.example.openeyes.model.VideoDatabase;
import com.example.openeyes.util.Value;

public class FragmentRecord extends Fragment {
    private RecyclerView mRecyclerView;
    private VideoAdapterRecord adapter;
    public String count;
    private RecordVideoDao dao;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_fragment,container,false);
        dao = VideoDatabase.getInstance(getContext().getApplicationContext()).getRecordVideoDao();
        mRecyclerView = (RecyclerView)view.findViewById(R.id.record_video_item_recycler_view);
//        adapter = new VideoAdapterRecord(Value.videoItemList_record);
        adapter = new VideoAdapterRecord(dao.getAll(count));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        mRecyclerView.setAdapter(adapter);
        return view;
    }
}
