package com.jredu.com.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.jredu.com.utils.TrafficUtils;

/**
 * Created by Administrator on 2015/10/7 0007.
 */
public class TrafficFetchService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
       //         Toast.makeText(TrafficFetchService.this,"正在获取数据流量",Toast.LENGTH_SHORT).show();
                TrafficUtils.fetchTraffic(TrafficFetchService.this);
                Intent updateScreen = new Intent(TrafficUtils.ACTION_UPDATE_TRAFFIC);
                sendBroadcast(updateScreen);
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);

    }
}
