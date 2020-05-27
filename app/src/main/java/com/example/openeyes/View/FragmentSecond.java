package com.example.openeyes.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.openeyes.Adapter.FragmentAdapter;
import com.example.openeyes.R;
import com.google.android.material.tabs.TabLayout;

public class FragmentSecond extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("mDebug", "onCreateView: fragment second start!");
        View view = inflater.inflate(R.layout.second_fragment,container,false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager_tab);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        MainActivity mainActivity = (MainActivity) getActivity();
        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager(),mainActivity.fragmentList_rank);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

}
