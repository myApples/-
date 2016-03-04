package com.jredu.com.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Debug;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jredu.com.entity.RunningProcess;
import com.jredu.com.R;

import java.util.ArrayList;
import java.util.List;

import static com.jredu.com.R.*;

/**
 * Created by Administrator on 2015/9/24 0024.
 */
public class CoreService extends Service {


    private OnScanTaskListener mScanTaskListener;
    private ActivityManager activityManager;
    private PackageManager packageManager;

    public void setmScanTaskListener(OnScanTaskListener mScanTaskListener) {
        this.mScanTaskListener = mScanTaskListener;
    }

    public class LocalService extends Binder {
        public CoreService getService() {
            return CoreService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalService();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        packageManager = getPackageManager();
    }

    public void  ScanRunningPregress(){
        new ScanRunningPregressTask().execute();
    }



    private class ScanRunningPregressTask extends
            AsyncTask<Void, Integer, List<RunningProcess>> {
        @Override
        protected List<RunningProcess> doInBackground(Void... params) {
            List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo =
                    activityManager.getRunningAppProcesses();
            int count = 0;
            publishProgress(runningAppProcessInfo.size(), count);

            List<RunningProcess> list = new ArrayList<>();
            for (ActivityManager.RunningAppProcessInfo info : runningAppProcessInfo) {
                publishProgress(runningAppProcessInfo.size(), ++count);
                RunningProcess rp = new RunningProcess();

                rp.setProcessName(info.processName);
                rp.setPid(info.pid);
                rp.setUid(info.uid);

                try {
                    ApplicationInfo applicationInfo =
                            packageManager.getApplicationInfo(info.processName, 0);
                    String appName=
                            applicationInfo.loadLabel(packageManager).toString();

                    Drawable icon =
                            applicationInfo.loadIcon(packageManager);

                    rp.setAppName(appName);
                    rp.setIcon(icon);

                    if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)!=0){
                        rp.setSystem(true);
                    }else{
                        rp.setSystem(false);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    //   e.printStackTrace();

                    if (info.processName.indexOf(":")!=-1) {
                        String packageName =
                                info.processName.substring(0,info.processName.indexOf(":"));

                        ApplicationInfo applicationInfo=null;
                        List<ApplicationInfo> apps =
                                packageManager.getInstalledApplications(
                                        packageManager.GET_UNINSTALLED_PACKAGES);
                        for (ApplicationInfo i:apps) {
                            if (packageName.equals(i.processName)) {
                                applicationInfo = i;
                                break;
                            }
                            if(applicationInfo!=null){
                                Drawable icon=applicationInfo.loadIcon(packageManager);
                                String label = applicationInfo.loadLabel(packageManager).toString();

                                rp.setIcon(icon);
                                rp.setAppName(label);
                            }else{
                                rp.setAppName(info.processName);
                                rp.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
                            }
                        }

                    }else{
                        rp.setAppName(info.processName);
                        rp.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
                    }

                }
                Debug.MemoryInfo memory[] =
                        activityManager.getProcessMemoryInfo(new int[]{info.pid});
                long memorySize = memory[0].getTotalPrivateDirty()*1024;
                rp.setMemorySize(memorySize);

                list.add(rp);
            }

            return list;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mScanTaskListener!=null) {
                mScanTaskListener.onStartScan();
            }
        }

        @Override
        protected void onPostExecute(List<RunningProcess> runningProcesses) {
            super.onPostExecute(runningProcesses);
            if (mScanTaskListener!=null) {
                mScanTaskListener.onScanComplete(runningProcesses);
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int totalCount=values[0];
            int curCount=values[1];
            int progress=(int)(((curCount*1.0)/totalCount)*100);

            if (mScanTaskListener!=null) {
                mScanTaskListener.onScanProgressUpdate(totalCount,curCount,progress);
            }
        }
    }


    public interface OnScanTaskListener {
        //扫描之前
        public void onStartScan();

        //扫描过程中
        public void onScanProgressUpdate(Integer... values);

        //扫描完成
        public void onScanComplete(List<RunningProcess> runningProgress);
    }
}
