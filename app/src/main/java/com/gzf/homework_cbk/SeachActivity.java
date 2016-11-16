package com.gzf.homework_cbk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gzf.homework_cbk.adapters.MyTTAdapter;
import com.gzf.homework_cbk.beans.Data;
import com.gzf.homework_cbk.myutils.MyByteAsyncTask;
import com.gzf.homework_cbk.myutils.MyBytesCallBack;
import com.gzf.homework_cbk.uri.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SeachActivity extends AppCompatActivity {

    private ListView mListView;
    private ImageView goBack, goHome;
    private TextView gjz;
    private String mSearchPath;
    private String mString;
    private List<Data> mDatas = new ArrayList<>();
    private MyTTAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach);
        initView();
        Intent intent = getIntent();
        mString = intent.getStringExtra("path");
        mSearchPath = Urls.SEARCH_URL + mString;
        initOnClick();
        initListView();
        initData();
    }

    private void initListView() {
        mListAdapter = new MyTTAdapter(this,mDatas);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SeachActivity.this, WebActivity.class);
                String path = Urls.CONTENT_URL + id;
                intent.putExtra("path", path);
                SeachActivity.this.startActivity(intent);
            }
        });
    }

    private void initData() {
        gjz.setText(mString);
        new MyByteAsyncTask(new MyBytesCallBack() {
            @Override
            public void onCallBack(byte[] bytes) {
                loadByteData(bytes);
            }
        }, false).execute(mSearchPath);
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
    
    private void initOnClick() {
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeachActivity.this.onBackPressed();
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
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        goBack = (ImageView) findViewById(R.id.collect_back);
        goHome = (ImageView) findViewById(R.id.gohome);
        gjz = (TextView) findViewById(R.id.gjz);
    }
}
