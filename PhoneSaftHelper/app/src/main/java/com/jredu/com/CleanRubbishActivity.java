package com.jredu.com;

import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.StatFs;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jredu.com.R;
import com.jredu.com.adapter.CleanRubbishAdapter;
import com.jredu.com.entity.AppCacheItem;
import com.jredu.com.utils.StorageUtil;

import org.w3c.dom.Text;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class CleanRubbishActivity extends AppCompatActivity {

    private TextView totalCacheSize;
    private ListView cacheListView;

    private CleanRubbishAdapter adapter;
    private List<AppCacheItem> mData;

    private long totalSize = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_rubbish);
        intiActionBar();
        intiView();

    }

    private void intiView(){
        totalCacheSize = (TextView)findViewById(R.id.totalCacheSize);
        cacheListView = (ListView)findViewById(R.id.cacheListView);

        mData = new ArrayList<>();
        adapter = new CleanRubbishAdapter(this,mData);

        cacheListView.setAdapter(adapter);

    }
    private ActionBar actionBar;
    private void intiActionBar(){
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("垃圾清理");

    }


    public void getCacheSize(View view){
        new GetCacheSizeTask().execute();
    }

    private Method getPackageSizeInfo;
    private class GetCacheSizeTask extends AsyncTask
                                    <Void,Void,List<AppCacheItem>>{
        @Override
        protected List<AppCacheItem> doInBackground(Void... params) {
            final PackageManager packageManager = getPackageManager();
            try {
                getPackageSizeInfo =
                        PackageManager.class.getDeclaredMethod(
                                "getPackageSizeInfo",
                                String.class,
                                IPackageStatsObserver.class);
                getPackageSizeInfo.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            List<PackageInfo> packageInfos =
                    packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
            totalSize = 0;
            final CountDownLatch countDownLatch = new CountDownLatch(packageInfos.size());

            try {
                for (PackageInfo info:packageInfos){
                    final PackageInfo packageInfo = info;
                    getPackageSizeInfo.invoke(packageManager, info.packageName,
                            new IPackageStatsObserver.Stub() {
                                @Override
                                public void onGetStatsCompleted(PackageStats pStats,
                                                                boolean succeeded) throws RemoteException {

                                    if (succeeded&&pStats!=null) {
                                        AppCacheItem item = new AppCacheItem();
                                        item.setCacheSize(pStats.cacheSize);
                                        item.setAppName(packageInfo.applicationInfo.
                                                loadLabel(packageManager).toString());
                                        item.setIcon(packageInfo.applicationInfo.loadIcon(packageManager));
                                        totalSize+=pStats.cacheSize;
                                        mData.add(item);



                                    }
                                    countDownLatch.countDown();
                                }
                            });
                }
            } catch (IllegalAccessException e) {
                    e.printStackTrace();
            } catch (InvocationTargetException e) {
                    e.printStackTrace();
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String result = StorageUtil.convertStorage(totalSize);
            Message msg = handler.obtainMessage();
            msg.what = GET_CACHE_SIZE;
            msg.obj = result;
            handler.sendMessage(msg);


            return mData;
        }
    }
    public static final int GET_CACHE_SIZE=1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_CACHE_SIZE:
                    String result = (String)msg.obj;
                    totalCacheSize.setText(result);
                    break;
            }
            adapter.notifyDataSetChanged();

        }
    };



    private Method freeStorageAndNotify;
    public void doClean(View view){
        try {
            freeStorageAndNotify = PackageManager.class.
                    getDeclaredMethod("freeStorageAndNotify", Long.TYPE,
                            IPackageDataObserver.class);
            freeStorageAndNotify.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        PackageManager pm = getPackageManager();
        File localData= Environment.getDataDirectory();
        if (localData==null) {
            return;
        }
        StatFs statFs = new StatFs(localData.getAbsolutePath());
        long total = statFs.getBlockCountLong()*statFs.getBlockSizeLong();
        try {
            freeStorageAndNotify.invoke(pm, total,
                    new IPackageDataObserver.Stub() {
                        @Override
                        public void onRemoveCompleted(String packageName,
                                                      boolean succeeded) throws RemoteException {
                            new GetCacheSizeTask().execute();

                        }
                    });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    
}
