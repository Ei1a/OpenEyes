package com.example.openeyes.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment>fragmentList_rank;
    private String[] tab_list = {"周排行","月排行","总排行"};
    public FragmentAdapter(FragmentManager fm,List<Fragment>fragmentList_rank) {
        super(fm);
        this.fragmentList_rank = fragmentList_rank;
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
