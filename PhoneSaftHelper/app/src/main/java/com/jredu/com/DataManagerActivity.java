package com.jredu.com;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.jredu.com.adapter.DataManagerAdapter;
import com.jredu.com.model.AppModel;
import com.jredu.com.utils.TrafficUtils;

import java.util.ArrayList;
import java.util.List;

public class DataManagerActivity extends AppCompatActivity {

    private ListView wifiDataListView;
    private DataManagerAdapter adapter;

    private List<AppModel> mList;

  //  private boolean isFirstIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_manager);
        intiActionBar();

        //控制加载对话框显示
    //    isFirstIn = true;
        intiListView();
        //获得流量数据
        new FetchAllAppDataTask().execute();

        //监听流量更新
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(TrafficUtils.ACTION_UPDATE_TRAFFIC);
        registerReceiver(mReceiver,mFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private  BroadcastReceiver mReceiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new FetchAllAppDataTask().execute();
        }
    };

    private void intiListView(){
        wifiDataListView = (ListView)findViewById(R.id.wifiDataListView);
//        mList = new ArrayList<>();
//        adapter = new DataManagerAdapter(this,mList);
//        wifiDataListView.setAdapter(adapter);
    }


    private class FetchAllAppDataTask extends AsyncTask<Void, Void, List<AppModel>> {

        @Override
        protected List<AppModel> doInBackground(Void... params) {
            return TrafficUtils.getAllAppTraffic(DataManagerActivity.this);
        }

        @Override
        protected void onPostExecute(List<AppModel> appModels) {
            mList = appModels;
            adapter = new DataManagerAdapter(DataManagerActivity.this,mList);

            ListAdapter listAdapter = wifiDataListView.getAdapter();
            if (listAdapter!=null&&wifiDataListView.getAdapter() instanceof DataManagerAdapter) {
                ((DataManagerAdapter)listAdapter).notifyDataSetChanged();
            }else{
                wifiDataListView.setAdapter(adapter);
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
    private ActionBar actionBar;
    private void intiActionBar(){
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("APP流量");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
