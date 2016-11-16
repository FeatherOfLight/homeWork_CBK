package com.gzf.homework_cbk.myutils;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Administrator on 2016/10/27.
 */

public class MyByteAsyncTask extends AsyncTask<String, Void, byte[]> {

    private MyBytesCallBack mCallBack;
    private Context context;
    private boolean isSave = true;
    private int saveType;
    private String rootFile;
    private String fileName;
    private String fileType;
    public static final int TYPE_COSTOM = 1;
    public static final int TYPE_PUBLIC = 2;
    public static final int TYPE_CHACHE = 3;
    public static final int TYPE_FLIE = 4;

    /**
     * 需要保存数据到SD卡时的使用的构造方法
     *
     * @param callBack 回调接口用于数据传输
     * @param fileType 文件类型（九大共有目录）
     * @param rootFile 文件夹名称 （非自定义目录时为空）
     * @param fileName 文件名称 不可为NULL类型
     * @param saveType 保存类型（四种类型，推荐使用本类定义的常量）
     * @param context  上下文 非私有目录保存时，本参数可以为空
     */
    public MyByteAsyncTask(MyBytesCallBack callBack, String fileType, String rootFile, String fileName, int saveType, Context context) {
        mCallBack = callBack;
        this.fileType = fileType;
        this.rootFile = rootFile;
        this.fileName = fileName;
        this.saveType = saveType;
        this.context = context;
    }

    /**
     * 不需要保存数据到SD卡时的使用的构造方法
     *
     * @param callBack 回调接口用于数据传输
     * @param isSave   输入参数为false
     */
    public MyByteAsyncTask(MyBytesCallBack callBack, boolean isSave) {
        mCallBack = callBack;
        this.isSave = isSave;
    }

    @Override
    protected byte[] doInBackground(String... params) {
        int length = params.length;
        for (int i = 0; i < length; i++) {
            String path = params[i];
            byte[] bt = HttpUtils.loadbyte(path);
            if (isSave) {
                if (bt != null) {

                    switch (saveType) {
                        case 1:
                            SDcarkUtils.saveTosdCustom(bt, rootFile, fileName);
                            break;
                        case 2:
                            SDcarkUtils.saveToPublic(bt, fileType, fileName);
                            break;
                        case 3:
                            SDcarkUtils.saveToPrivateCache(bt, context, fileName);
                            break;
                        case 4:
                            SDcarkUtils.saveToPrivateFiles(bt, context, fileType, fileName);
                            break;
                        default:
                            break;
                    }
                }

            }
            return bt;
        }
        return null;
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        mCallBack.onCallBack(bytes);
    }
}
