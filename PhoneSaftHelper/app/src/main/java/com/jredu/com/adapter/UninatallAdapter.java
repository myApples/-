package com.jredu.com.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jredu.com.R;
import com.jredu.com.entity.RunningProcess;
import com.jredu.com.utils.StorageUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/9/30 0030.
 */
public class UninatallAdapter extends BaseAdapter{

    private Context mContext;
    private List<RunningProcess> mData;


    public  UninatallAdapter(List<RunningProcess> mData,Context mContext){
        this.mData=mData;
        this.mContext=mContext;

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
        private TextView appName,  otherInfo;
        private ImageView appIcon;
        private ImageButton button;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.uninatallitem,null);
            holder.appName=(TextView)convertView.findViewById(R.id.appNames);
            holder.otherInfo = (TextView)convertView.findViewById(R.id.otherInfos);

            holder.appIcon=(ImageView)convertView.findViewById(R.id.appIcons);
            holder.button = (ImageButton)convertView.findViewById(R.id.checkDelede);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        final RunningProcess rp =mData.get(position);

        holder.appName.setText(rp.getAppName());
        holder.appIcon.setImageDrawable(rp.getIcon());

        String other = "内存："+ StorageUtil.convertStorage(rp.getMemorySize());
        other=" ";
        if (rp.isSystem()) {
            other+="系统应用";
        }else {
            other+="用户应用";
        }
        holder.otherInfo.setText(other);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setAction(Intent.ACTION_DELETE);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + rp.getProcessName()));
                mContext.startActivity(intent);
            }
        });


        return convertView;
    }







}
