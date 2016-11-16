package com.gzf.homework_cbk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.gzf.homework_cbk.widget.HeaderViewPager;

public class MainActivity extends AppCompatActivity {

    private TextView djs;
    private int s = 3;
    private SharedPreferences mSharedPreferences;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            switch (msg.what) {
                case 0:
                    intent.setClass(MainActivity.this, FristToActivity.class);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                    break;
                case 1:
                    intent.setClass(MainActivity.this, CBKActivity.class);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                    break;
                case 2:
                    djs.setText(s + "秒后自动跳转");
                    s--;
                    sendEmptyMessageDelayed(2, 1000);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initView();
        mSharedPreferences = getSharedPreferences("Frist", MODE_PRIVATE);
        boolean isFrist = mSharedPreferences.getBoolean("isFrist", true);
        if (isFrist) {
            SharedPreferences.Editor edit = mSharedPreferences.edit();
            edit.putBoolean("isFrist", false);
            edit.commit();
            mHandler.sendEmptyMessageDelayed(0, 3000);
        } else {
            mHandler.sendEmptyMessageDelayed(1, 3000);
        }
        mHandler.sendEmptyMessage(2);
    }

    private void initView() {
        djs = (TextView) findViewById(R.id.djs);
    }
}
