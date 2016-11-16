package com.gzf.homework_cbk.myutils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	private Context context;
	private static String dbName = "MyCollect";
	private static int dbVersion = 1;
	private final String tableName = "mycollect";

	public String getTableName() {
		return tableName;
	}

	/**
	 * 
	 * @param context ??????
	 * @param name ?????????
	 * @param factory ????Cursor?????????????????????????????????cursor????????null?????????????????
	 * @param version ????????
	 */
	public MySQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	

	public MySQLiteOpenHelper(Context context) {
		super(context, dbName, null,dbVersion);
		this.context = context;
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table mycollect (_id integer primary key autoincrement,title varcher unique,id varcher," +
				"create_time varcher,source varcher,author varcher,weiboUrl varcher)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("create table mysql_tow (_id integer primary key autoincrement,name varcher,sex varcher,adderss verchar)");
		
		db.execSQL("alter table mysql_one add salary integer");
		
		//		db.execSQL("drop table mysql_one if exists");



	}

}
