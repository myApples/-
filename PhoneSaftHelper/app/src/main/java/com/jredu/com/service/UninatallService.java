package com.jredu.com.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jredu.com.entity.RunningProcess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/30 0030.
 */
public class UninatallService extends Service{


    private PackageManager packageManager;
    private OnTaskListener mTaskListener;

    public void setmTaskListener(OnTaskListener mTaskListener) {
        this.mTaskListener = mTaskListener;
    }

    public class LocalService extends Binder {
        public UninatallService getService() {
            return UninatallService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        packageManager = getPackageManager();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalService();
    }

    public void  ScanAllPregress(){
        new ScanAllPregressTask().execute();
    }

    private class ScanAllPregressTask extends AsyncTask<Void, Integer, List<RunningProcess>>{
        @Override
        protected List<RunningProcess> doInBackground(Void... params) {
            List<RunningProcess> list = new ArrayList<>();
            List<ApplicationInfo> applicationInfos =
                    packageManager.getInstalledApplications
                            (PackageManager.GET_UNINSTALLED_PACKAGES);
            int count = 0;
            publishProgress(applicationInfos.size(),count);
            for(ApplicationInfo info:applicationInfos){
                publishProgress(applicationInfos.size(),++count);
                RunningProcess rp = new RunningProcess();
                rp.setProcessName(info.packageName);
                rp.setAppName(info.loadLabel(packageManager).toString());
                rp.setIcon(info.loadIcon(packageManager));
                if((info.flags&ApplicationInfo.FLAG_SYSTEM)!=0){
                    rp.setSystem(true);

                }else{
                    rp.setSystem(false);
                }

                list.add(rp);

            }

            return list;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int totalCount=values[0];
            int curCount=values[1];
            int progress=(int)(((curCount*1.0)/totalCount)*100);
            if (mTaskListener!=null) {
                mTaskListener.onScanProgressUpdate(totalCount,curCount,progress);
            }
        }

        @Override
        protected void onPostExecute(List<RunningProcess> runningProcesses) {
            super.onPostExecute(runningProcesses);
            if (mTaskListener!=null) {
                mTaskListener.onScanComplete(runningProcesses);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mTaskListener!=null) {
                mTaskListener.onStartScan();
            }
        }
    }


    public interface OnTaskListener{

        //扫描之前
        public void onStartScan();

        //扫描过程中
        public void onScanProgressUpdate(Integer... values);

        //扫描完成
        public void onScanComplete(List<RunningProcess> runningProgress);


    }
}
