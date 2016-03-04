package com.jredu.com;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jredu.com.service.TrafficFetchService;
import com.jredu.com.utils.StorageUtil;
import com.jredu.com.utils.TrafficUtils;

public class DataShowActivity extends AppCompatActivity {

    private TextView cur_type;
    private TextView cur_interval;
    private TextView total_Mobile;
    private TextView today_Mobile;
    private TextView total_wifi;
    private TextView today_wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_show);
        intiView();
        //设置网络类型
        setCurNetType();
        //更新流量界面
        new InitTotalInterfaceTask().execute();
        //定时更新流量
        TrafficUtils.startRepeatingService(this,TrafficUtils.INTERVAL, TrafficFetchService.class,"");
        //监听网络变化和流量更新
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mFilter.addAction(TrafficUtils.ACTION_UPDATE_TRAFFIC);
        registerReceiver(mReceiver, mFilter);

        intiActionBar();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void intiView(){
        cur_type = (TextView) findViewById(R.id.cur_type);
        cur_interval = (TextView) findViewById(R.id.cur_interval);
        total_Mobile = (TextView) findViewById(R.id.total_mobile);
        today_Mobile = (TextView) findViewById(R.id.today_mobile);
        total_wifi = (TextView) findViewById(R.id.total_wifi);
        today_wifi = (TextView) findViewById(R.id.today_wifi);
        //显示更新时间
        cur_interval.setText(String.format(getString(R.string.cur_interval), TrafficUtils.INTERVAL));


    }
    private void setCurNetType(){
        //网络类型
        String type = TrafficUtils.netWorkTypeToString(this);
        cur_type.setText(String.format(getString(R.string.cur_type),type));

    }


    private class InitTotalInterfaceTask extends AsyncTask<Void, Void, long[]> {
        @Override
        protected long[] doInBackground(Void... params) {
            return TrafficUtils.getMobileAndWifiData(DataShowActivity.this);
        }

        @Override
        protected void onPostExecute(long[] longs) {
     //       Toast.makeText(DataShowActivity.this,"界面更新",Toast.LENGTH_SHORT).show();
            total_Mobile.setText(StorageUtil.convertStorage(longs[TrafficUtils.INDEX_TOTAL_MOBILE]));
            total_wifi.setText(StorageUtil.convertStorage(longs[TrafficUtils.INDEX_TOTAL_WIFI]));
            today_Mobile.setText(StorageUtil.convertStorage(longs[TrafficUtils.INDEX_DAY_MOBILE]));
            today_wifi.setText(StorageUtil.convertStorage(longs[TrafficUtils.INDEX_DAY_WIFI]));

        }
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TrafficUtils.ACTION_UPDATE_TRAFFIC.equals(intent.getAction())) {
                new InitTotalInterfaceTask().execute();
            }else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                setCurNetType();
            }
        }
    };


    private ActionBar actionBar;
    private void intiActionBar(){
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("流量监控");
    }

    public void getAppData(View view){
        Intent intent = new Intent(this,DataManagerActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id ==android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
