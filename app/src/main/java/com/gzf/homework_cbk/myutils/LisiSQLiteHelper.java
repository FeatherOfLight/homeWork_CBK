package com.gzf.homework_cbk.myutils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/11/15.
 */

public class LisiSQLiteHelper extends SQLiteOpenHelper {

    private Context context;
    private static String dbName = "MyLisi";
    private static int dbVersion = 1;
    private final String tableName = "mylisi";

    public String getTableName() {
        return tableName;
    }

    public LisiSQLiteHelper(Context context) {
        super(context, dbName, null, dbVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table mylisi (_id integer primary key autoincrement,title varcher unique,id varcher," +
                "create_time varcher,source varcher,author varcher,weiboUrl varcher)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
