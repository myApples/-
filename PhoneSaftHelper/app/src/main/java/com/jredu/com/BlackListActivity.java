package com.jredu.com;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.j256.ormlite.dao.Dao;
import com.jredu.com.adapter.FromListMenAdapter;

import com.jredu.com.entity.ListMen;
import com.jredu.com.helper.DBListManHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BlackListActivity extends AppCompatActivity{


    private ActionBar actionBar;
    private TextView addList;
    private ListView blackListView;
    private List<ListMen> mData;

    private FromListMenAdapter adapter;

    private Dao<ListMen,Integer> listManDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        addList=(TextView)findViewById(R.id.addList);

        intiListView();
        intiActionBar();
    }



    public void addBlack(View view){
        AlertDialog dialog=new AlertDialog.Builder(this).
        setTitle("选择添加的模式").setItems(R.array.choss_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whitch) {
                startToChoss(whitch);
                dialog.dismiss();

            }
        }).create();
        dialog.show();
    }

    private void startToChoss(int idx){
        switch (idx){
            case 0:
                //从联系人中获取
                Intent intent=new Intent(this,FromListMenActivity.class);
                startActivity(intent);
                finish();
                break;
            case 1:
                //从通话记录中获取
                break;
            case 2:
                //从短信记录中获取
                break;
            case 3:
                //手动输入号码
                break;
        }

    }


    private List<ListMen> list=null;
    private void intiListView(){
        blackListView=(ListView)super.findViewById(R.id.blackListView);
        mData= new ArrayList<>();
        listManDao = DBListManHelper.getInstance(this).getListDao();
        try {
            list = listManDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mData.addAll(list);
        if (mData.size()==0) {
            addList.setText("添加黑名单，拦截电话和短信");
        }
        adapter = new FromListMenAdapter(mData,this);
        blackListView.setAdapter(adapter);
        adapter.setListener(new FromListMenAdapter.onListManSelectedChangedListener() {
           @Override
           public void onListManSelectedChanged(final ListMen listMen) {
               if (listMen.isCheckBox()) {
                   mData.add(listMen);

               } else {
                   AlertDialog dialog = new AlertDialog.Builder(BlackListActivity.this).
                           setTitle("提示").setMessage("确认取消黑名单设置吗？").
                           setPositiveButton("确定",
                                   new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                           mData.remove(listMen);
                                           //从数据库中删掉
                                           listManDao = DBListManHelper
                                                   .getInstance(getApplicationContext())
                                                   .getListDao();
                                           try {
                                               listManDao.delete(listMen);
                                           } catch (SQLException e) {
                                               e.printStackTrace();
                                           }
                                           Toast.makeText(BlackListActivity.this, "已移除黑名单", Toast.LENGTH_SHORT).show();
                                           adapter.notifyDataSetChanged();


                                       }
                                   }).setNegativeButton("取消",
                           new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.dismiss();

                               }
                           }).create();
                   dialog.show();
               }

           }
       });


    }


    public void intiActionBar(){
        actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("黑名单");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();

        }
        return super.onOptionsItemSelected(item);
    }

}
