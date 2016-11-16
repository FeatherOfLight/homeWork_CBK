package com.gzf.homework_cbk.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> tabs;

    public MyFragmentAdapter(FragmentManager supportFragmentManager, List<Fragment> fragments, List<String> tabs) {
        super(supportFragmentManager);
        this.fragments = fragments;
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position);
    }
}
