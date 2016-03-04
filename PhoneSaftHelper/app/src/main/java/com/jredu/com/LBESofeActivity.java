package com.jredu.com;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jredu.com.R;

public class LBESofeActivity extends AppCompatActivity implements View.OnClickListener{

    private DrawerLayout mDrawerLayout;
    private ActionBar actionBar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private ActivityManager activityManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        internalMemory=(TextView)findViewById(R.id.internalMemory);
        elecyric=(TextView)findViewById(R.id.elecyric);
        todayLL=(TextView)findViewById(R.id.todayLL);


        activityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo=new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        long availableMemory=memoryInfo.availMem;
        long totalMemory=memoryInfo.totalMem;
        int percent=(int)((1-(availableMemory*1.0/totalMemory))*100);
        internalMemory.setText("内存:"+percent+"%");
        intiView();
        intiActionBar();
    }

    private RelativeLayout speedUp,manage,optimize,secret,control;
    private TextView internalMemory,elecyric,todayLL;

    private  RelativeLayout lanjie;
    private void intiView(){
        speedUp=(RelativeLayout)super.findViewById(R.id.speedUp);
        manage=(RelativeLayout)super.findViewById(R.id.manage);
        optimize=(RelativeLayout)super.findViewById(R.id.optimize);
        secret=(RelativeLayout)super.findViewById(R.id.secret);
        control=(RelativeLayout)super.findViewById(R.id.control);

        lanjie=(RelativeLayout)super.findViewById(R.id.lanjie);


        speedUp.setOnClickListener(this);
        manage.setOnClickListener(this);
        optimize.setOnClickListener(this);
        secret.setOnClickListener(this);
        control.setOnClickListener(this);

        lanjie.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.speedUp:
                Intent intent=new Intent(this,SpeedUpActivity.class);
                startActivity(intent);
                break;
            case R.id.manage:
                Intent intentss=new Intent(this,UninatallActivity.class);
                startActivity(intentss);
                break;
            case R.id.optimize:
                break;
            case R.id.secret:
                break;
            case R.id.control:
                Intent in=new Intent(this,DataShowActivity.class);
                startActivity(in);
                break;


            case R.id.lanjie:
                Intent intents=new Intent(this,BlackListActivity.class);
                startActivity(intents);
                break;
        }
    }


    private void intiActionBar() {
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("BlackApple");

        mActionBarDrawerToggle =
                new ActionBarDrawerToggle(this,mDrawerLayout,
                        R.string.drawer_open,
                        R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                break;
            case R.id.advice:
                break;
            case R.id.more:
                break;
        }
        return mActionBarDrawerToggle.onOptionsItemSelected(item)
                |super.onOptionsItemSelected(item);
    }

}
