package com.gzf.homework_cbk.myutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetUtils {
/**
 * ?��????��??????????
 * @param context
 * @return
 */
	public static boolean isConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		}
		int type = networkInfo.getType();
		switch (type) {
		case ConnectivityManager.TYPE_MOBILE:return true;
		case ConnectivityManager.TYPE_WIFI:return true;
		}
		return false;
	}
}
