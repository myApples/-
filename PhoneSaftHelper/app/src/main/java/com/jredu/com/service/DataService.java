package com.jredu.com.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DataService extends Service {
    public DataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
