package com.gzf.homework_cbk;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gzf.homework_cbk.beans.DataContent;
import com.gzf.homework_cbk.myutils.InternetUtils;
import com.gzf.homework_cbk.myutils.LisiSQLiteHelper;
import com.gzf.homework_cbk.myutils.MyByteAsyncTask;
import com.gzf.homework_cbk.myutils.MyBytesCallBack;
import com.gzf.homework_cbk.myutils.MySQLiteOpenHelper;
import com.gzf.homework_cbk.myutils.SDcarkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class WebActivity extends AppCompatActivity {

    private TextView title, date;
    private WebView mWebView;
    private String mPath;
    private DataContent mDataContent;
    private MySQLiteOpenHelper mMySQLiteOpenHelper;
    private LisiSQLiteHelper mLisiSQLiteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
//        getSupportActionBar().hide();
        mMySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        mLisiSQLiteHelper = new LisiSQLiteHelper(this);
        initView();
        initWebView();
        initData();
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setWebViewClient(new WebViewClient());

    }

    /**
     * string.add(title);
     *string.add(time);
     *string.add(source);
     *string.add(nickname);
     *string.add(wap_thumb);
     */
    private void initData() {
        Intent intent = getIntent();
        mPath = intent.getStringExtra("path");
//        mWebView.loadUrl(mPath);
        String fileName = mPath.substring(mPath.lastIndexOf("/") + 1);
        if (InternetUtils.isConnected(this)){
            new MyByteAsyncTask(new MyBytesCallBack() {
                @Override
                public void onCallBack(byte[] bytes) {
                    loadData(bytes);
                }
            },null,null,fileName,MyByteAsyncTask.TYPE_CHACHE,this).execute(mPath);
        }else {
            String filePath = getExternalCacheDir().getAbsolutePath() + File.separator + fileName;
            if (SDcarkUtils.fileIsExists(filePath)){
                byte[] bytes = SDcarkUtils.pickbyteFromSDCard(filePath);
                loadData(bytes);
            }else {
                Toast.makeText(this,"无详情数据，请检查网络后访问",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void loadData(byte[] bytes) {
        if (bytes != null) {
            try {
                Log.d("TAG", "---------------------->: " +"web数据获得");
                JSONObject jsonObject = new JSONObject(new String(bytes));
//                        JSONObject data = jsonObject.optJSONObject("data");
                mDataContent = JSON.parseObject(jsonObject.toString(), DataContent.class);
                title.setText(mDataContent.getData().getTitle());
                date.setText("时间："+mDataContent.getData().getCreate_time()+"来源："+mDataContent.getData().getSource());
                mWebView.loadDataWithBaseURL(mPath,mDataContent.getData().getWap_content(),"text/html","utf-8",null);
                SQLiteDatabase db = mLisiSQLiteHelper.getReadableDatabase();
                ContentValues values = new ContentValues();
                DataContent.DataBean data = mDataContent.getData();
                values.put("title", data.getTitle());
                values.put("id",data.getId());
                values.put("create_time",data.getCreate_time());
                values.put("source",data.getSource());
                values.put("author",data.getAuthor());
                values.put("weiboUrl",data.getWeiboUrl());
                db.insert(mLisiSQLiteHelper.getTableName(), null, values);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        date = (TextView) findViewById(R.id.date);
        mWebView = (WebView) findViewById(R.id.webview);
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.back:
                this.onBackPressed();
                break;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,mPath);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"分享到："));
                break;
            case R.id.collect:
                long insert = 0;
                if (mDataContent != null) {
                    SQLiteDatabase db = mMySQLiteOpenHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();
                    DataContent.DataBean data = mDataContent.getData();
                    values.put("title", data.getTitle());
                    values.put("id",data.getId());
                    values.put("create_time",data.getCreate_time());
                    values.put("source",data.getSource());
                    values.put("author",data.getAuthor());
                    values.put("weiboUrl",data.getWeiboUrl());
                    insert = db.insert(mMySQLiteOpenHelper.getTableName(), null, values);
                }
                if (insert == 0){
                    Toast.makeText(this,"收藏失败",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,"收藏成功",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
