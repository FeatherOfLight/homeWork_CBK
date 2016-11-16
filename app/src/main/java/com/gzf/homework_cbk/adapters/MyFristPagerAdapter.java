package com.gzf.homework_cbk.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/11/12.
 */
public class MyFristPagerAdapter extends PagerAdapter {

    private List<View> mImages;
    public MyFristPagerAdapter(List<View> images) {
        mImages = images;
    }

    @Override
    public int getCount() {
        return mImages == null?0:mImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mImages.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView(mImages.get(position));
    }
}
