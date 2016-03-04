package com.jredu.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jredu.com.R;
import com.jredu.com.entity.AppCacheItem;
import com.jredu.com.utils.StorageUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/10/2 0002.
 */
public class CleanRubbishAdapter extends BaseAdapter{

    private List<AppCacheItem> mData;
    private Context context;

    public CleanRubbishAdapter(Context context,List<AppCacheItem> mData) {
        this.mData = mData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        private TextView cacheName;
        private TextView cacheSize;
        private ImageView ivIcon;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.clean_item,null);
            holder.ivIcon = (ImageView)convertView.findViewById(R.id.ivIcon);
            holder.cacheName = (TextView)convertView.findViewById(R.id.cacheName);
            holder.cacheSize = (TextView)convertView.findViewById(R.id.cacheSize);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder)convertView.getTag();
        }

        AppCacheItem items = mData.get(position);

        holder.cacheName.setText(items.getAppName());
        holder.ivIcon.setImageDrawable(items.getIcon());
        holder.cacheSize.setText(StorageUtil.convertStorage(items.getCacheSize()));

        return convertView;
    }
}
