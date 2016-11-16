package com.gzf.homework_cbk.fragments;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gzf.homework_cbk.CBKActivity;
import com.gzf.homework_cbk.R;
import com.gzf.homework_cbk.WebActivity;
import com.gzf.homework_cbk.adapters.MyHeaderAdapter;
import com.gzf.homework_cbk.adapters.MyTTAdapter;
import com.gzf.homework_cbk.beans.Data;
import com.gzf.homework_cbk.beans.HeadItem;
import com.gzf.homework_cbk.myutils.InternetUtils;
import com.gzf.homework_cbk.myutils.MyByteAsyncTask;
import com.gzf.homework_cbk.myutils.MyBytesCallBack;
import com.gzf.homework_cbk.myutils.SDcarkUtils;
import com.gzf.homework_cbk.uri.Urls;
import com.gzf.homework_cbk.widget.HeaderViewPager;
import com.handmark.pulltorefresh.library.LoadingLayoutProxy;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RefreshFragment extends Fragment {

    private PullToRefreshListView mPullToRefreshListView;
    private String headName = "headdata";
    private List<HeadItem> mHeadItems;
    private ListView mListView;
    private ImageView toTop;
    private List<ImageView> mHeaderImages = new ArrayList<>();
    private PagerAdapter mHeaderAdapter;
    private String dataName = "TTData";
    private String DataName = "";
    private List<Data> mDatas = new ArrayList<>();
    private BaseAdapter mListAdapter;
    private int pager = 1;
    private HeaderViewPager mHeaderViewPager;
    private View mHeaderView;
    private LoadingLayoutProxy mLoadingLayoutProxy;
    private TextView mFootView;
    private List<String> mStrings = new ArrayList<>();

    public RefreshFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHeaderAdapter = new MyHeaderAdapter(mHeaderImages);
        mListAdapter = new MyTTAdapter(getContext(), mDatas);
        initHeaderViewandData();
        initData(pager);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_refresh, container, false);
        initView(view);
//        initHeaderViewandData();
        initFootView();
        initLoadlayout();
        initListView();
        initPullToRefresh();
        return view;
    }

    private void initPullToRefresh() {
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mDatas.clear();
                pager = 1;
                initData(pager);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFootView.setVisibility(View.GONE);
                pager++;
                initData(pager);
            }
        });
        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), WebActivity.class);
                String path = Urls.CONTENT_URL + id;
                intent.putExtra("path", path);
                getContext().startActivity(intent);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.mipmap.icon_dialog);
                builder.setTitle("确定要删除吗");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<Animator> animators = new ArrayList<Animator>();
                        int firstVisiblePosition = mListView.getFirstVisiblePosition();
                        int index = position - firstVisiblePosition;
                        View removeView = mListView.getChildAt(index);
                        int removeHeight = removeView.getHeight();
                        int dividerHeight = mListView.getDividerHeight();
                        int hight = removeHeight + dividerHeight;
                        ObjectAnimator alpha = ObjectAnimator.ofFloat(removeView, "alpha", 1f, 0f);
                        animators.add(alpha);
                        alpha.setDuration(1000);
                        int delay = 1000;
                        int firstViewToMove = index + 1;
                        for (int i = firstViewToMove; i < mListView.getChildCount(); i++) {
                            View viewToMove = mListView.getChildAt(i);

                            ObjectAnimator moveAnimator = ObjectAnimator.ofFloat(viewToMove, "translationY", 0, -hight);
                            moveAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                            moveAnimator.setStartDelay(delay);

                            delay += 100;

                            animators.add(moveAnimator);
                        }

                        AnimatorSet set = new AnimatorSet();
                        set.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
//                                mDatas.remove(position);
//                                mListAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();
                                ((MyTTAdapter) mListAdapter).remove(position - 2);
//                                 动画结束后，恢复ListView所有子View的属性
                                for (int i = 0; i < mListView.getChildCount(); i++) {
                                    View v = mListView.getChildAt(i);
                                    v.setAlpha(1f);
                                    v.setTranslationY(0);
                                }

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        });
                        set.playTogether(animators);
                        set.start();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                return true;
            }
        });
    }

    private void initFootView() {
        mFootView = new TextView(getContext());
        mFootView.setGravity(Gravity.CENTER);
        mFootView.setText("加载更多");
        mFootView.setTextSize(24);
        mFootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager++;
                initData(pager);
            }
        });
        mListView.addFooterView(mFootView);
        toTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.setSelection(0);
            }
        });
    }

    private void initLoadlayout() {
        mLoadingLayoutProxy = (LoadingLayoutProxy) mPullToRefreshListView.getLoadingLayoutProxy();
        mLoadingLayoutProxy.setPullLabel("下拉刷新...");
        mLoadingLayoutProxy.setReleaseLabel("释放刷新");
        mLoadingLayoutProxy.setRefreshingLabel("拼命加载中");
        Drawable drawable = getContext().getResources().getDrawable(R.mipmap.loading_logo);
        mLoadingLayoutProxy.setLoadingDrawable(drawable);
        String time = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_ABBREV_TIME | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE);
        mLoadingLayoutProxy.setLastUpdatedLabel(time);
    }

    private void initListView() {
        mListView.addHeaderView(mHeaderView);
        mPullToRefreshListView.setAdapter(mListAdapter);
    }

    private void initData(int pager) {
        String path = Urls.HEADLINE_URL + Urls.HEADLINE_TYPE + pager;
        DataName = dataName + pager;
        Log.d("TAG", "---------------------->请求数据: " + path);
        if (InternetUtils.isConnected(getContext())) {
            Log.d("TAG", "---------------------->: " + "有网络");
            new MyByteAsyncTask(new MyBytesCallBack() {
                @Override
                public void onCallBack(byte[] bytes) {
                    initListData(bytes);
                }
            }, null, null, DataName, MyByteAsyncTask.TYPE_CHACHE, getContext()).execute(path);
        } else {
            Log.d("TAG", "---------------------->: " + "无网络");
            String filepath = getContext().getExternalCacheDir().getAbsoluteFile() + File.separator + DataName;
            byte[] bytes = SDcarkUtils.pickbyteFromSDCard(filepath);
            initListData(bytes);
        }
    }

    private void initListData(byte[] bytes) {
        if (bytes != null && bytes.length != 0) {
            try {
                JSONObject jsonObject = new JSONObject(new String(bytes));
                JSONArray data = jsonObject.optJSONArray("data");
                mDatas.addAll(JSON.parseArray(data.toString(), Data.class));
                Log.d("TAG", "---------------------->数据返回: " + mDatas.size());
                mListAdapter.notifyDataSetChanged();
                if (mFootView != null) {
                    mFootView.setVisibility(View.VISIBLE);
                }
                if (mPullToRefreshListView != null) {
                    if (mPullToRefreshListView.isRefreshing()) {
                        mPullToRefreshListView.onRefreshComplete();
                        String time = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(),
                                DateUtils.FORMAT_ABBREV_TIME | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE);
                        mLoadingLayoutProxy.setLastUpdatedLabel(time);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initHeaderViewandData() {
        mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.headeritem, mListView, false);
        mHeaderViewPager = (HeaderViewPager) mHeaderView.findViewById(R.id.headerview);
        if (InternetUtils.isConnected(getContext())) {
            new MyByteAsyncTask(new MyBytesCallBack() {
                @Override
                public void onCallBack(byte[] bytes) {
                    initHeaderData(bytes);
                }
            }, null, null, headName, MyByteAsyncTask.TYPE_CHACHE, getContext()).execute(Urls.HEADERIMAGE_URL);
        } else {
            String filepath = getContext().getExternalCacheDir().getAbsoluteFile() + File.separator + headName;
            byte[] bytes = SDcarkUtils.pickbyteFromSDCard(filepath);
            initHeaderData(bytes);
        }
        mHeaderViewPager.setAdapter(mHeaderAdapter);
        //里层的ViewPager
        ViewPager viewPager = mHeaderViewPager.getViewPager();

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((CBKActivity) getContext()).getViewPager().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_DOWN:
                        mHeaderViewPager.stop();
                        break;
                    case MotionEvent.ACTION_UP:
                        mHeaderViewPager.stop();
                        mHeaderViewPager.start();
                        break;
                }
                return false;
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initHeaderData(byte[] bytes) {
        if (bytes != null && bytes.length != 0) {
            try {
                Log.d("TAG", "---------------------->头布局数据返回: " + bytes.length);
                JSONObject jsonObject = new JSONObject(new String(bytes));
                JSONArray data = jsonObject.optJSONArray("data");
                mHeadItems = JSON.parseArray(data.toString(), HeadItem.class);
                initHeaderDataItem();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initHeaderDataItem() {
        int size = mHeadItems.size();
        for (int i = 0; i < size; i++) {
            final ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.mipmap.ic_launcher);
            String imagePath = mHeadItems.get(i).getImage();
            String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            String filePath = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + fileName;
            if (SDcarkUtils.fileIsExists(filePath)) {
                byte[] bytes = SDcarkUtils.pickbyteFromSDCard(filePath);
                if (bytes != null && bytes.length != 0) {
                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                    Log.d("TAG", "---------------------->头布局图片来自SD卡: " + filePath);
                }
            } else {
                new MyByteAsyncTask(new MyBytesCallBack() {
                    @Override
                    public void onCallBack(byte[] bytes) {
                        if (bytes != null && bytes.length != 0) {
                            imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                            Log.d("TAG", "---------------------->头布局图片返回: " + bytes.length);
                        }
                    }
                }, Environment.DIRECTORY_PICTURES, null, fileName, MyByteAsyncTask.TYPE_FLIE, getContext())
                        .execute(imagePath);
            }
            mHeaderImages.add(imageView);
            mStrings.add(mHeadItems.get(i).getTitle());
        }
        mHeaderAdapter.notifyDataSetChanged();
        mHeaderViewPager.setHeaderText(mStrings);
        mHeaderViewPager.init2();
    }

    private void initView(View view) {
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefreshlistview);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        toTop = (ImageView) view.findViewById(R.id.totop);
        mListView = mPullToRefreshListView.getRefreshableView();
    }


}
