package com.example.openeyes.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.openeyes.adapter.FragmentAdapter;
import com.example.openeyes.R;
import com.example.openeyes.util.Value;
import com.google.android.material.tabs.TabLayout;

public class FragmentSecond extends Fragment {
    String[] tabList = {"周排行", "月徘行", "总排行"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("mDebug", "onCreateView: fragment second start!");
        View view = inflater.inflate(R.layout.second_fragment,container,false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager_tab);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager(), Value.fragmentList_rank, tabList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

}
