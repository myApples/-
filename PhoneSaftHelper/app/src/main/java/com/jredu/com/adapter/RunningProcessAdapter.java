package com.jredu.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jredu.com.entity.RunningProcess;
import com.jredu.com.R;
import com.jredu.com.utils.StorageUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/9/24 0024.
 */
public class RunningProcessAdapter extends BaseAdapter {

    private Context mContext;
    private List<RunningProcess> mData;
    private onContactSelectedChangeListener listener;

    public void setListener(onContactSelectedChangeListener listener) {
        this.listener = listener;
    }

    public RunningProcessAdapter(List<RunningProcess> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
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


    public  class ViewHolder{
        private TextView appName,packName,otherInfo;
        private ImageView appIcon;
        private CheckBox ck;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem,null);
            holder.appName = (TextView)convertView.findViewById(R.id.appName);
            holder.appIcon = (ImageView)convertView.findViewById(R.id.appIcon);
            holder.packName=(TextView)convertView.findViewById(R.id.packName);
            holder.ck=(CheckBox)convertView.findViewById(R.id.checkBox);
            holder.otherInfo = (TextView)convertView.findViewById(R.id.otherInfo);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder)convertView.getTag();

        }

        final RunningProcess rp = mData.get(position);

        holder.appName.setText(rp.getAppName());
        holder.appIcon.setImageDrawable(rp.getIcon());
        holder.packName.setText(rp.getProcessName());

        String other = "内存："+ StorageUtil.convertStorage(rp.getMemorySize());
        other = "";
        if (rp.isSystem()) {
            other+="系统应用";
        }else {
            other+="用户应用";
        }
        holder.otherInfo.setText(other);

        if(rp.isSelected()){
            holder.ck.setChecked(true);
        }else{
            holder.ck.setChecked(false);
        }
        holder.ck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cks = (CheckBox)v;
                rp.setSelected(cks.isChecked());
                if (listener!=null) {
                    listener.onContacSelectChanged(rp);
                }
            }
        });

        return convertView;
    }

    public interface onContactSelectedChangeListener{
        public void onContacSelectChanged(RunningProcess runningProcess);
    }
}
