package com.gzf.homework_cbk;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gzf.homework_cbk.adapters.MyFragmentAdapter;
import com.gzf.homework_cbk.fragments.ListFragment;
import com.gzf.homework_cbk.fragments.RefreshFragment;
import com.gzf.homework_cbk.uri.Urls;
import com.softpo.viewpagertransformer.CubeInTransformer;
import com.softpo.viewpagertransformer.CubeOutTransformer;
import com.softpo.viewpagertransformer.DepthPageTransformer;
import com.softpo.viewpagertransformer.StackTransformer;
import com.softpo.viewpagertransformer.TabletTransformer;

import java.util.ArrayList;
import java.util.List;

public class CBKActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private ImageView more,back;
    private List<String> tabs = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private ImageView goHome,seacher;
    private EditText mEditText;
    private TextView shoucang,lishijilu,banquan,yijian,denglu,tuichu;

    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbk);
        getSupportActionBar().hide();
        initView();
        initData();
        initClick();
        initViewPAger();
    }

    private void initViewPAger() {
        mViewPager.setPageTransformer(true,new CubeOutTransformer());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initClick() {
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
            }
        });
        seacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = mEditText.getText().toString();
                if (string != null&&!string.equals("")) {
                    //TODO 搜索

                    Intent intent = new Intent(CBKActivity.this,SeachActivity.class);
                    intent.putExtra("path",string);
                    CBKActivity.this.startActivity(intent);
                }else {
                    Toast.makeText(CBKActivity.this,"关键字不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CBKActivity.this,CollectActivity.class);
                CBKActivity.this.startActivity(intent);
            }
        });
        lishijilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CBKActivity.this,LisiActivity.class);
                CBKActivity.this.startActivity(intent);
            }
        });
        yijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CBKActivity.this,YiJianActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        tabs.add("头条");
        tabs.add("百科");
        tabs.add("资讯");
        tabs.add("经营");
        tabs.add("数据");
        RefreshFragment refreshFragment = new RefreshFragment();
        mFragments.add(refreshFragment);
        ListFragment listFragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt("pager",1);
        listFragment.setArguments(args);
        mFragments.add(listFragment);

        listFragment = new ListFragment();
        Bundle args1 = new Bundle();
        args1.putInt("pager",2);
        listFragment.setArguments(args1);
        mFragments.add(listFragment);

        listFragment = new ListFragment();
        Bundle args2 = new Bundle();
        args2.putInt("pager",3);
        listFragment.setArguments(args2);
        mFragments.add(listFragment);

        listFragment = new ListFragment();
        Bundle args3 = new Bundle();
        args3.putInt("pager",4);
        listFragment.setArguments(args3);
        mFragments.add(listFragment);

        mAdapter = new MyFragmentAdapter(getSupportFragmentManager(),mFragments,tabs);
        }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.openmenu);
        more = (ImageView) findViewById(R.id.open_menu);
        back = (ImageView) findViewById(R.id.menu_back);
        goHome = (ImageView) findViewById(R.id.gohome);
        seacher = (ImageView) findViewById(R.id.image_search);
        mEditText = (EditText) findViewById(R.id.edit_search);
        shoucang = (TextView) findViewById(R.id.myshoucang);
        lishijilu = (TextView) findViewById(R.id.lishijilu);
        banquan = (TextView) findViewById(R.id.banquan);
        yijian = (TextView) findViewById(R.id.yijian);
        denglu = (TextView) findViewById(R.id.denglu);
        tuichu = (TextView) findViewById(R.id.tuichu);
    }
}
