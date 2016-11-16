package com.gzf.homework_cbk.myutils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class HttpUtils {

	public static byte[] loadbyte(String path) {
		InputStream is = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(path).openConnection();
			
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				is = con.getInputStream();
				byte[] buf = new byte[1024];
				int len =0;
				while ((len = is.read(buf)) != -1) {
					baos.write(buf, 0, len);
				}
				//Log.i("TAG", "�������");
			return baos.toByteArray();
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
		}
		
		return null;
	}

}
