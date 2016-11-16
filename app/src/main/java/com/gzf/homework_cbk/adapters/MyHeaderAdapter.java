package com.gzf.homework_cbk.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */
public class MyHeaderAdapter extends PagerAdapter {

    private List<ImageView> headerImages;
    public MyHeaderAdapter(List<ImageView> headerImages) {
        this.headerImages = headerImages;
    }

    @Override
    public int getCount() {
        return headerImages == null?0:headerImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView child = headerImages.get(position);
        container.addView(child);
        return child;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView(headerImages.get(position));
    }
}
