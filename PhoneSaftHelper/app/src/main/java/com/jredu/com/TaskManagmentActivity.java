package com.jredu.com;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jredu.com.adapter.RunningProcessAdapter;
import com.jredu.com.adapter.RunningProcessAdapter.onContactSelectedChangeListener;
import com.jredu.com.entity.RunningProcess;
import com.jredu.com.R;
import com.jredu.com.service.CoreService;
import com.jredu.com.service.CoreService.OnScanTaskListener;
import com.jredu.com.utils.StorageUtil;

import java.util.ArrayList;
import java.util.List;

public class TaskManagmentActivity extends AppCompatActivity implements OnScanTaskListener {


    private ActivityManager activityManager;
    private PackageManager packageManager;
    private ListView listView;

    private RelativeLayout progressBarContainer;
    private ProgressBar progressBar;
    private TextView progressBarText;

    private Button clear;

    private CoreService coreService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            coreService = ((CoreService.LocalService)service).getService();
            coreService.setmScanTaskListener(TaskManagmentActivity.this);
            coreService.ScanRunningPregress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            coreService=null;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_managment);
        intiActionBar();
        listView=(ListView)findViewById(R.id.listView);

        progressBarContainer=(RelativeLayout)findViewById(R.id.progressBarContainer);
        progressBarText=(TextView)findViewById(R.id.progressBarText);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        clear=(Button)findViewById(R.id.claer);

        activityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        packageManager=getPackageManager();

        ActivityManager.MemoryInfo memory=new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memory);

        //绑定Service
        bindService(new Intent(this,CoreService.class),connection,BIND_AUTO_CREATE);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }



    private ActionBar actionBar;
    private void intiActionBar(){
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("任务管理");


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartScan() {
        progressBarText.setText("开始扫描...");
        progressBar.setProgress(0);

    }

    @Override
    public void onScanProgressUpdate(Integer... values) {
        int totalCount=values[0];
        int curCount = values[1];
        progressBarText.setText(String.format("%d/%d", curCount,totalCount));

        int progress = values[2];

        progressBar.setProgress(progress);

    }

    private long sice=0;
    private List<RunningProcess> rpList=new ArrayList<>();
    @Override
    public void onScanComplete(final List<RunningProcess> runningProgress) {
        final RunningProcessAdapter adapter =
                new RunningProcessAdapter(runningProgress,this);
        listView.setAdapter(adapter);
        progressBarContainer.setVisibility(View.GONE);

        adapter.setListener(new onContactSelectedChangeListener() {
            @Override
            public void onContacSelectChanged(RunningProcess runningProcess) {
                rpList.add(runningProcess);

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rpList.size()==0&&runningProgress.size()>0) {
                    Toast.makeText(TaskManagmentActivity.this,
                            "请选择要清理的进程", Toast.LENGTH_SHORT).show();
                }else{
                    for (RunningProcess rp:rpList){
                        activityManager.killBackgroundProcesses(rp.getProcessName());
                        runningProgress.remove(rp);
                        sice+=rp.getMemorySize();
                    }
                    Toast.makeText(TaskManagmentActivity.this,
                            "共释放"+ StorageUtil.convertStorage(sice)+"内存",Toast.LENGTH_SHORT ).show();
                    adapter.notifyDataSetChanged();
                    sice=0;
                    rpList.clear();
                }
            }
        });


    }
}
