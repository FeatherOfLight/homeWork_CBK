package com.gzf.homework_cbk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class YiJianActivity extends AppCompatActivity {

    private ImageView goBack, goHome,submit;
    private EditText title,content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yi_jian);
        getSupportActionBar().hide();
        initView();
        initClick();
    }

    private void initClick() {
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YiJianActivity.this.onBackPressed();
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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_str = title.getText().toString();
                String content_str = content.getText().toString();
                if (title_str.equals("") || content_str.equals("")) {
                    Toast.makeText(YiJianActivity.this,"内容和标题不可以为空！",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(YiJianActivity.this,"意见反馈提交失败，请重新尝试！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        goBack = (ImageView) findViewById(R.id.back);
        goHome = (ImageView) findViewById(R.id.gohome);
        submit = (ImageView) findViewById(R.id.submit);
        title = (EditText) findViewById(R.id.edit_title);
        content = (EditText) findViewById(R.id.edit_content);
    }
}
