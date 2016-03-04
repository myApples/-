package com.jredu.com.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jredu.com.R;
import com.jredu.com.model.AppModel;
import com.jredu.com.utils.StorageUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/10/5 0005.
 */
public class DataManagerAdapter extends BaseAdapter{
    private Context context;
    private List<AppModel> mData;

    public DataManagerAdapter(Context context, List<AppModel> mData) {
        this.context = context;
        this.mData = mData;
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

    public class ViewHolder{
        private TextView appName;
        private TextView allSize;
        private ImageView appIcon;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.wifidata_items,null);
            holder.allSize = (TextView)convertView.findViewById(R.id.data_app_allSize);
            holder.appIcon = (ImageView)convertView.findViewById(R.id.data_app_icon);
            holder.appName = (TextView)convertView.findViewById(R.id.data_app_name);


           convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        AppModel items = mData.get(position);

        holder.appName.setText(items.getAppName());

        holder.allSize.setText(StorageUtil.convertStorage(items.getTraffic()));


        try {
            PackageManager packageManager= context.getPackageManager();
            Drawable icon  = packageManager.getApplicationIcon(items.getPkgName());
            holder.appIcon.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
