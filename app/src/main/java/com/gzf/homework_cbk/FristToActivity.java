package com.gzf.homework_cbk;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gzf.homework_cbk.adapters.MyFristPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FristToActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<View> images = new ArrayList<>();
    private PagerAdapter mPagerAdapter;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frist_to);
        getSupportActionBar().hide();
        initView();
        initData();
    }

    private void initData() {
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.frist_item, mViewPager, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_frist);
            imageView.setImageResource(getResources().getIdentifier("slide" + (i + 1), "mipmap", getPackageName()));
            images.add(view);
        }
        images.get(images.size() - 1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FristToActivity.this, CBKActivity.class);
                FristToActivity.this.startActivity(intent);
                FristToActivity.this.finish();
            }
        });
        mPagerAdapter = new MyFristPagerAdapter(images);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ImageView imageView;
                int count = mLinearLayout.getChildCount();
                for (int i = 0; i < count; i++) {
                    imageView = (ImageView) mLinearLayout.getChildAt(i);
                    imageView.setImageResource(R.mipmap.page);
                }
                imageView = (ImageView) mLinearLayout.getChildAt(position);
                imageView.setImageResource(R.mipmap.page_now);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.fristviewpager);
        mLinearLayout = (LinearLayout) findViewById(R.id.circle_tabs);
    }

    public void c1(View view) {
        mViewPager.setCurrentItem(0);
    }

    public void c2(View view) {
        mViewPager.setCurrentItem(1);
    }

    public void c3(View view) {
        mViewPager.setCurrentItem(2);
    }
}
