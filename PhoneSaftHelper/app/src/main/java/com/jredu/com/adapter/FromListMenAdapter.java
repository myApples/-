package com.jredu.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jredu.com.R;
import com.jredu.com.entity.ListMen;

import java.util.List;

/**
 * Created by Administrator on 2015/9/28 0028.
 */
public class FromListMenAdapter extends BaseAdapter {


    private Context mcontext;
    private List<ListMen> mData;
    private onListManSelectedChangedListener listener;

    public void setListener(onListManSelectedChangedListener listener) {
        this.listener = listener;
    }

    public FromListMenAdapter(List<ListMen> mData,Context mcontext){
        this.mcontext=mcontext;
        this.mData=mData;
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
        private TextView list_name;
        private TextView list_number;

        private CheckBox ckx;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.listmen_item,null);

            holder.list_name=(TextView)convertView.findViewById(R.id.list_name);
            holder.list_number=(TextView)convertView.findViewById(R.id.list_number);
            holder.ckx=(CheckBox)convertView.findViewById(R.id.ckx);

            convertView.setTag(holder);

        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        final ListMen listMen=mData.get(position);

        holder.list_name.setText(listMen.getListMen());
        holder.list_number.setText(listMen.getListNumber());

        if(listMen.isCheckBox()){
            holder.ckx.setChecked(true);
        }else{
            holder.ckx.setChecked(false);
        }
        holder.ckx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox)v;
                listMen.setIsCheckBox(checkBox.isChecked());
                if (listener!=null) {
                    listener.onListManSelectedChanged(listMen);
                }
            }
        });


        return convertView;
    }

    public interface  onListManSelectedChangedListener{
        public void onListManSelectedChanged(ListMen listMen);
    }


}
