package com.gzf.homework_cbk.myutils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.RequiresApi;
import android.util.Log;

public class SDcarkUtils {

	
	//�ж�SD���Ƿ����
	public static boolean isMounted() {
		String state = Environment.getExternalStorageState();//��ȡSD����״̬
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static int getTotalSize() {
		if (isMounted()) {
			StatFs statfs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
			long blockSizeLong = statfs.getBlockSizeLong();
			long blockCountLong = statfs.getBlockCountLong();
			int totalSize = (int) (blockSizeLong*blockCountLong/1024/1024);
			return totalSize;
		}
		return 0;
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static int getFreeSize() {
		if (isMounted()) {
			StatFs statfs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
			long blockSizeLong = statfs.getBlockSizeLong();
			long blockCountLong = statfs.getFreeBlocksLong();
			int freeSize = (int) (blockSizeLong*blockCountLong/1024/1024);
			return freeSize;
		}
		return 0;
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static int getAvailabaleSize() {
		if (isMounted()) {
			StatFs statfs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
			long blockSizeLong = statfs.getBlockSizeLong();
			long blockCountLong = statfs.getAvailableBlocksLong();
			int availableSize = (int) (blockSizeLong*blockCountLong/1024/1024);
			return availableSize;
		}
		return 0;
	}
/**
 * 
 * @param data Ҫ���������
 * @param type �����ļ����� 
 * @param file �ļ���
 * @return �����Ƿ�ɹ�
 */
	public static boolean saveToPublic(byte[] data, String type,
			String fileName) {
		if (!isMounted()) {
			return false;
		}
		File root = Environment.getExternalStoragePublicDirectory(type);
		File name = new File(root,fileName);
		FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(name);
				fos.write(data, 0, data.length);
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		return false;
	}

public static boolean saveTosdCustom(byte[] data, String rootfile, String fileName) {
	if(!isMounted()){
		return false;
	}
	File root = Environment.getExternalStorageDirectory();
	File customFile = new File(root,rootfile);
	if (!customFile.exists()) {
		customFile.mkdirs();
	}
	File name = new File(customFile,fileName);
	FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(name);
			fos.write(data, 0, data.length);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	return false;
}

public static boolean saveToPrivateCache(byte[] data, Context context,
		String fileName) {
	if (!isMounted()) {
		return false;
	}
	File root = context.getExternalCacheDir();
	File name = new File(root,fileName);
	FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(name);
			fos.write(data, 0, data.length);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	return false;
}

public static boolean saveToPrivateFiles(byte[] data,Context context,String type,
		String fileName) {
	if (!isMounted()) {
		return false;
	}
	File root = context.getExternalFilesDir(type);
	File name = new File(root,fileName);
	FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(name);
			Log.d("TAG", "---------------------->图片存储地址: " +name.getAbsolutePath());
			fos.write(data, 0, data.length);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	return false;
}

public static byte[] pickbyteFromSDCard(String path) {
	FileInputStream fis = null;
	try {
		fis = new FileInputStream(path);
		return converInputStreamToByte(fis);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}finally{
		close(fis);
	}
	return null;
}

private static byte[] converInputStreamToByte(FileInputStream fis) throws IOException {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	int len = 0;
	byte[] buf = new byte[1024*8];
	while ((len = fis.read(buf)) != -1) {
		baos.write(buf, 0, len);
	}
	close(baos);
	return baos.toByteArray();
}

private static void close(OutputStream os) {
	if (os != null) {
		try {
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

private static void close(InputStream is) {
	if (is != null) {
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
	public static boolean fileIsExists(String filePath){
		try{
			File f=new File(filePath);
			if(!f.exists()){
				return false;
			}
		}catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
}
