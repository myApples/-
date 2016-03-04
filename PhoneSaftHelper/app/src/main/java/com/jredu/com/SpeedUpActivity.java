package com.jredu.com;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jredu.com.R;
import com.jredu.com.utils.StorageUtil;

public class SpeedUpActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView percent,opts,yjSpeedUp;
    private RelativeLayout works,speedUp,zqguanjia,clears;


    private ActivityManager activityManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_up);
        percent=(TextView)findViewById(R.id.percent);
        opts=(TextView)findViewById(R.id.opts);

        activityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo=new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        long availableMemory=memoryInfo.availMem;
        long totalMemory=memoryInfo.totalMem;
        int percents=(int)((1-(availableMemory*1.0/totalMemory))*100);

        percent.setText(percents+"%");

        opts.setText("内存:"+ StorageUtil.convertStorage(availableMemory)+
                "/"+StorageUtil.convertStorage(totalMemory));

        intiView();
        intiActionBar();
    }


    public void intiView(){


        works=(RelativeLayout)findViewById(R.id.works);
        speedUp=(RelativeLayout)findViewById(R.id.speedUp);
        zqguanjia=(RelativeLayout)findViewById(R.id.zqguanjia);
        clears=(RelativeLayout)findViewById(R.id.clears);

        yjSpeedUp=(TextView)findViewById(R.id.yjSpeedUp);


        works.setOnClickListener(this);
        speedUp.setOnClickListener(this);
        zqguanjia.setOnClickListener(this);
        clears.setOnClickListener(this);

        yjSpeedUp.setOnClickListener(this);





    }

    private ActionBar actionBar;
    public void intiActionBar(){
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("手机加速");


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_speed_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.works:
                Intent intent=new Intent(this,TaskManagmentActivity.class);
                startActivity(intent);
                break;
            case R.id.speedUp:
                break;
            case R.id.zqguanjia:
                break;
            case R.id.clears:
                Intent intent1 = new Intent(this,CleanRubbishActivity.class);
                startActivity(intent1);
                break;

            case R.id.yjSpeedUp:

                break;
        }
    }


}
