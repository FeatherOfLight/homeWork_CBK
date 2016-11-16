package com.gzf.homework_cbk.fragments;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gzf.homework_cbk.R;
import com.gzf.homework_cbk.WebActivity;
import com.gzf.homework_cbk.adapters.MyTTAdapter;
import com.gzf.homework_cbk.beans.Data;
import com.gzf.homework_cbk.myutils.InternetUtils;
import com.gzf.homework_cbk.myutils.MyByteAsyncTask;
import com.gzf.homework_cbk.myutils.MyBytesCallBack;
import com.gzf.homework_cbk.myutils.SDcarkUtils;
import com.gzf.homework_cbk.uri.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {


    private int mPager = 0;
    private final String BASE_URL = Urls.BASE_URL;
    private String type = "";
    private int index = 1;
    private List<Data> mDatas = new ArrayList<>();
    private MyTTAdapter mListAdapter;
    private ListView mListView;
    private ImageView toTop;
    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new MyTTAdapter(getContext(),mDatas);
        Bundle arguments = getArguments();
        mPager = arguments.getInt("pager");
        if (mPager != 0) {
            initData();
        }
    }

    private void initData() {
        switch (mPager) {
            case 1:
                type = Urls.CYCLOPEDIA_TYPE;
                break;
            case 2:
                type = Urls.CONSULT_TYPE;
                break;
            case 3:
                type = Urls.OPERATE_TYPE;
                break;
            case 4:
                type = Urls.DATA_TYPE;
                break;
        }
        initContent(index);

    }

    private void initContent(int index) {
        String path = BASE_URL + type + index;
        String fileName = type + index;
        if (InternetUtils.isConnected(getContext())) {
            new MyByteAsyncTask(new MyBytesCallBack() {
                @Override
                public void onCallBack(byte[] bytes) {
                    loadByteData(bytes);
                }
            }, null, null, fileName, MyByteAsyncTask.TYPE_CHACHE, getContext()).execute(path);
        } else {
            String filePath = getContext().getExternalCacheDir().getAbsolutePath() + File.separator + fileName;
            if (SDcarkUtils.fileIsExists(filePath)){
                byte[] bytes = SDcarkUtils.pickbyteFromSDCard(filePath);
                loadByteData(bytes);
            }
        }
    }

    private void loadByteData(byte[] bytes) {
        if (bytes != null && bytes.length != 0) {
            try {
                JSONObject jsonObject = new JSONObject(new String(bytes));
                JSONArray data = jsonObject.optJSONArray("data");
                mDatas.addAll(JSON.parseArray(data.toString(),Data.class));
                Log.d("TAG", "---------------------->listfragment数据返回: " + mDatas.size());
                mListAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initView(view);
        initFootView();
        initListView();
        initOnClick();
        return view;
    }

    private void initOnClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                                Toast.makeText(getContext(),""+position,Toast.LENGTH_SHORT).show();
                                ((MyTTAdapter) mListAdapter).remove(position);
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

    private void initListView() {
        mListAdapter = new MyTTAdapter(getContext(),mDatas);
        mListView.setAdapter(mListAdapter);
    }

    private void initFootView() {
        TextView mFootView = new TextView(getContext());
        mFootView = new TextView(getContext());
        mFootView.setGravity(Gravity.CENTER);
        mFootView.setText("加载更多");
        mFootView.setTextSize(24);
        mFootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                initContent(index);
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

    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.listview);
        toTop = (ImageView) view.findViewById(R.id.totop);
    }

}
