package com.gzf.homework_cbk;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.gzf.homework_cbk.adapters.MyTTAdapter;
import com.gzf.homework_cbk.beans.Data;
import com.gzf.homework_cbk.myutils.MySQLiteOpenHelper;
import com.gzf.homework_cbk.uri.Urls;

import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends AppCompatActivity {

    private ListView mListView;
    private MySQLiteOpenHelper mMySQLiteOpenHelper;
    private Cursor mCursor;
    private List<Data> mDatas = new ArrayList<>();
    private MyTTAdapter mMyTTAdapter;
    private ImageView goBack, goHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        getSupportActionBar().hide();
        mMySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        initView();
        initOnClick();
        initListView();
        initData();
    }

    private void initOnClick() {
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectActivity.this.onBackPressed();
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

    private void initListView() {
        mMyTTAdapter = new MyTTAdapter(this, mDatas);
        mListView.setAdapter(mMyTTAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CollectActivity.this, WebActivity.class);
                String path = Urls.CONTENT_URL + id;
                intent.putExtra("path", path);
                CollectActivity.this.startActivity(intent);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CollectActivity.this);
                builder.setIcon(R.mipmap.ic_logo);
                final String title = ((Data) mMyTTAdapter.getItem(position)).getTitle();
                builder.setTitle(title);
                View view1 = LayoutInflater.from(CollectActivity.this).inflate(R.layout.delete_or_no, null);
                Button delete = (Button) view1.findViewById(R.id.delelte);
                Button or_no = (Button) view1.findViewById(R.id.or_no);
                builder.setView(view1);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(CollectActivity.this);
                        builder.setIcon(R.mipmap.ic_logo);
                        builder.setTitle("提示");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase db = mMySQLiteOpenHelper.getReadableDatabase();
                                int delete1 = db.delete(mMySQLiteOpenHelper.getTableName(), "title=?", new String[]{title});
                                if (delete1 != 0) {
                                    initData();
                                    Toast.makeText(CollectActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(CollectActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.create().show();
                    }
                });
                or_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                return true;
            }
        });
    }

    private void initData() {
        mDatas.clear();
        SQLiteDatabase db = mMySQLiteOpenHelper.getReadableDatabase();
        mCursor = db.query(mMySQLiteOpenHelper.getTableName(), new String[]{"*"}, null, null, null, null, null);
        while (mCursor.moveToNext()) {
            Data data = new Data();
            data.setTitle(mCursor.getString(mCursor.getColumnIndex("title")));
            data.setSource(mCursor.getString(mCursor.getColumnIndex("source")));
            data.setId(mCursor.getString(mCursor.getColumnIndex("id")));
            data.setCreate_time(mCursor.getString(mCursor.getColumnIndex("create_time")));
            data.setNickname(mCursor.getString(mCursor.getColumnIndex("author")));
            mDatas.add(data);
        }
        mMyTTAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        goBack = (ImageView) findViewById(R.id.collect_back);
        goHome = (ImageView) findViewById(R.id.gohome);
    }
}
