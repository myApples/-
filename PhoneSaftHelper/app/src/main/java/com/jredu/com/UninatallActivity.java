package com.jredu.com;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jredu.com.adapter.UninatallAdapter;
import com.jredu.com.entity.RunningProcess;
import com.jredu.com.service.UninatallService;
import com.jredu.com.view.RoundProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/30 0030.
 */
public class UninatallActivity extends AppCompatActivity implements UninatallService.OnTaskListener{


    private ListView listView;


    private RelativeLayout progressCon;
    private RoundProgressBar progressBar;

    private List<RunningProcess> list =new ArrayList<>();

    private UninatallService uninatallService;

    private ServiceConnection connection = new ServiceConnection(){
        @Override
        public void onServiceDisconnected(ComponentName name) {
            uninatallService =null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            uninatallService = ((UninatallService.LocalService)service).getService();
            uninatallService.setmTaskListener(UninatallActivity.this);
            uninatallService.ScanAllPregress();

        }
    };

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uninatall);
        intiActionBar();
        listView = (ListView)findViewById(R.id.uninatallListView);

        progressCon = (RelativeLayout)findViewById(R.id.progressCon);
        progressBar = (RoundProgressBar)findViewById(R.id.progressBar);

        bindService(new Intent(this,UninatallService.class),connection,BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    private void intiActionBar(){
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("软件卸载");

    }

    @Override
    public void onStartScan() {

        progressBar.setProgress(0);

    }

    @Override
    public void onScanProgressUpdate(Integer... values) {
        int totalCount=values[0];
        int curCount = values[1];
        int progress=(int)(((curCount*1.0)/totalCount)*100);
        progressBar.setProgress(progress);

    }

    @Override
    public void onScanComplete(List<RunningProcess> runningProgress) {
        UninatallAdapter adapter = new UninatallAdapter(runningProgress,this);
        listView.setAdapter(adapter);
        progressCon.setVisibility(View.GONE);

    }
}
