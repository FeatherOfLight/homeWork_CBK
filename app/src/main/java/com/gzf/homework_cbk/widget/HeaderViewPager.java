package com.gzf.homework_cbk.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzf.homework_cbk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/12.
 */

public class HeaderViewPager extends RelativeLayout {

    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;
    private TextView mTextView;
    private List<ImageView> mImageViews;
    private LinearLayout mLinearLayout;
    private List<String> headerText = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    int currentItem = mViewPager.getCurrentItem();
                    int childCount = mViewPager.getChildCount();
                    currentItem++;
                    if (currentItem > childCount - 1) {
                        currentItem = 0;
                    }
                    mViewPager.setCurrentItem(currentItem);
                    sendEmptyMessageDelayed(0, 2000);
                    break;
            }
        }

    };
    private PagerAdapter mAdapter;


    public HeaderViewPager(Context context) {
        this(context, null);
    }

    public HeaderViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
//        init();
        LayoutInflater.from(getContext()).inflate(R.layout.headerviewpager, this, true);
        mTextView = (TextView) findViewById(R.id.textview);
        mImageViews = new ArrayList<>();
        mLinearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);


    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    private void init() {
        mViewPager = new ViewPager(getContext());
        mRelativeLayout = new RelativeLayout(getContext());
        mTextView = new TextView(getContext());
        mImageViews = new ArrayList<>();
        mLinearLayout = new LinearLayout(getContext());
    }

    //    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void init2() {
//        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
//        mViewPager.setLayoutParams(params);
//        LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT,getHeight()/7);
//        params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        mRelativeLayout.setLayoutParams(params1);
//        mRelativeLayout.setBackground( new ColorDrawable(Color.parseColor("#99909090")));
//        LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//        params2.addRule(ALIGN_RIGHT);
//        mLinearLayout.setLayoutParams(params2);
        if (mAdapter != null) {

            int count = mAdapter.getCount();
            Log.d("TAG", "---------------------->viewpager数量: " + count);
            for (int i = 0; i < count; i++) {
                ImageView imageView = new ImageView(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(7, 5, 7, 5);
                imageView.setLayoutParams(layoutParams);
                imageView.setImageResource(R.mipmap.page);
                mImageViews.add(imageView);
                mLinearLayout.addView(imageView);
            }
            mImageViews.get(0).setImageResource(R.mipmap.page_now);
            start();
//        initAddView();
//        setHeaderText("10086");
        }
    }

//    private void initAddView() {
//        mRelativeLayout.addView(mTextView);
//        mRelativeLayout.addView(mLinearLayout);
//        addView(mViewPager);
//        addView(mRelativeLayout);
//    }

    public void stop() {
        mHandler.removeMessages(0);
    }

    public void start() {
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setAdapter(PagerAdapter adapter) {
        mAdapter = adapter;
        Log.d("TAG", "---------------------->adapterssssssss: " + adapter.getCount());
        mViewPager.setAdapter(mAdapter);
//        init2();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (ImageView image : mImageViews) {
                    image.setImageResource(R.mipmap.page);
                }
                ImageView view = (ImageView) mLinearLayout.getChildAt(position);
                view.setImageResource(R.mipmap.page_now);
                mTextView.setText(headerText.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        invalidate();
    }

    public void setHeaderText(List<String> headerText) {
        this.headerText.clear();
        this.headerText.addAll(headerText);
        mTextView.setText(headerText.get(0));
        Log.d("TAG", "---------------------->headerTextheaderText: " + headerText.size());
    }


}
