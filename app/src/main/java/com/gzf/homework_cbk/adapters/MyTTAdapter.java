package com.gzf.homework_cbk.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzf.homework_cbk.R;
import com.gzf.homework_cbk.beans.Data;
import com.gzf.homework_cbk.myutils.MyByteAsyncTask;
import com.gzf.homework_cbk.myutils.MyBytesCallBack;
import com.gzf.homework_cbk.myutils.MyLruCache;
import com.gzf.homework_cbk.myutils.SDcarkUtils;

import java.io.File;
import java.util.List;

/**
 */
public class MyTTAdapter extends BaseAdapter {

    private Context context;
    private List<Data> datas;
    private MyLruCache mMyLruCache;

    public MyTTAdapter(Context context, List<Data> datas) {
        this.context = context;
        this.datas = datas;
        mMyLruCache = MyLruCache.obtMyLruCache((int) (Runtime.getRuntime().maxMemory() / 8));
    }

    public void remove(int position){
        if(position < datas.size()&&position>=0){
            datas.remove(position);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(datas.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem, parent, false);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.list_image);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.source = (TextView) convertView.findViewById(R.id.source);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Data data = datas.get(position);
        holder.title.setText(data.getTitle());
        holder.source.setText(data.getSource() + "    " + data.getNickname() + "    " + data.getCreate_time());
        String wap_thumb = data.getWap_thumb();
        if (wap_thumb != null&&!wap_thumb.equals("")) {
            getBipmapToImage(holder.mImageView, wap_thumb);
        }
        return convertView;
    }

    private void getBipmapToImage(final ImageView imageView, String wap_thumb) {
        final String fileName = wap_thumb.substring(wap_thumb.lastIndexOf("/") + 1);
        String filePath = context.getExternalFilesDir(null).getAbsolutePath() +
                File.separator + fileName;
        Log.d("TAG", "---------------------->读取图片文件地址: " +filePath);
        final Bitmap bitmap = mMyLruCache.get(fileName);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.d("TAG", "---------------------->: 图片来自内存" );
        } else if (SDcarkUtils.fileIsExists(filePath)) {
            byte[] bytes = SDcarkUtils.pickbyteFromSDCard(filePath);
            if (bytes != null && bytes.length != 0) {
                Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                mMyLruCache.put(fileName, bitmap1);
                imageView.setImageBitmap(bitmap1);
                Log.d("TAG", "---------------------->: 图片来自SD卡" );
            }
        } else {
            new MyByteAsyncTask(new MyBytesCallBack() {
                @Override
                public void onCallBack(byte[] bytes) {
                    if (bytes != null && bytes.length != 0) {
                        Bitmap bitmap2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        mMyLruCache.put(fileName, bitmap2);
                        imageView.setImageBitmap(bitmap2);
                        Log.d("TAG", "---------------------->: 图片来自网络" );
                    }
                }
            }, null,null, fileName, MyByteAsyncTask.TYPE_FLIE, context)
                    .execute(wap_thumb);

        }
    }

    static class ViewHolder {
        ImageView mImageView;
        TextView title, source;
    }
}
