package com.example.openeyes.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment>fragmentList_rank;
    private String[] tab_list = null;
    public FragmentAdapter(FragmentManager fm,List<Fragment>fragmentList_rank, String[] tab_list) {
        super(fm);
        this.fragmentList_rank = fragmentList_rank;
        this.tab_list = tab_list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList_rank.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList_rank.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tab_list[position];
    }

}
