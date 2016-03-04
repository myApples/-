package com.jredu.com.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.jredu.com.R;
import com.jredu.com.db.DbManager;
import com.jredu.com.model.AppModel;
import com.jredu.com.model.DateTrafficModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2015/10/6 0006.
 */
public class TrafficUtils {
    //获取流量间隔时间
    public static final int INTERVAL = 30;
    //所有流量和每天流量的数据的下标
    public static final int INDEX_TOTAL_MOBILE = 0;
    public static final int INDEX_DAY_MOBILE = 1;
    public static final int INDEX_TOTAL_WIFI = 2;
    public static final int INDEX_DAY_WIFI = 3;

    //获取流量的数据广播
    public static final String ACTION_UPDATE_TRAFFIC = "action_update_traffic";

    public static  String netWorkTypeToString(Context context){

        ConnectivityManager connectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isConnected()){
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                return  context.getString(R.string.wifi);
            }else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = android.net.Proxy.getDefaultHost();

                return TextUtils.isEmpty(proxyHost)
                        ?(isFastMobileNetwork(context)?
                            context.getString(R.string._3g_above):
                            context.getString(R.string._2g))
                        :context.getString(R.string.wap);
            }
        }

        return context.getString(R.string.invalid_network);

    }


    private static boolean isFastMobileNetwork(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }

    }



    //得到数据和wifi的流量
    public static long[] getMobileAndWifiData(Context context){
        long[] datas = new long[4];
        datas[INDEX_TOTAL_MOBILE] = DbManager.getInstance(context).getTraffic(DbManager.UID_TOTAL_MOBILE,
                DbManager.TrafficDate.MONTH,0);
        datas[INDEX_TOTAL_WIFI] = DbManager.getInstance(context).getTraffic(DbManager.UID_TOTAL_WIFI,
                DbManager.TrafficDate.MONTH,0);
        datas[INDEX_DAY_MOBILE] = DbManager.getInstance(context).getTraffic(DbManager.UID_TOTAL_MOBILE,
                DbManager.TrafficDate.DAY,0);
        datas[INDEX_DAY_WIFI] = DbManager.getInstance(context).getTraffic(DbManager.UID_TOTAL_WIFI,
                DbManager.TrafficDate.DAY,0);
        return datas;

    }

    public static List<AppModel> getAllAppTraffic (Context context){
        List<AppModel> datas = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent,0);
        for (ResolveInfo resolveInfo:resolveInfos){
            String packageName = resolveInfo.activityInfo.packageName;
            String name = resolveInfo.loadLabel(packageManager).toString();
            int uid;
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(packageName,0);
                uid = packageInfo.applicationInfo.uid;
                long traffic = DbManager.getInstance(context)
                        .getTraffic(uid, DbManager.TrafficDate.MONTH, 0);
                if (traffic==0) {
                    continue;
                }
                AppModel model=new AppModel();
                model.setPkgName(packageName);
                model.setAppName(name);
                model.setTraffic(traffic);
                model.setUid(uid);
                datas.add(model);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(datas, new Comparator<AppModel>() {
            @Override
            public int compare(AppModel lhs, AppModel rhs) {
                return (int) (rhs.getTraffic()-lhs.getTraffic());
            }
        });

        return datas;
    }

    /**
     * 获取每秒的流量
     */

    public static long getTrafficPerSec(Context context) {

        long perSecData = DbManager.getInstance(context).getTraffic(0, DbManager.TrafficDate.SEC, 0);
        if (perSecData < 0) {
            perSecData = 0;
        }
        return perSecData;
    }

    public  static void fetchTraffic(Context context) {

        fetchTotalTraffic(context);
        fetchAppTraffic(context);
    }
    //更新所有数据变化
    private static  void fetchTotalTraffic(Context context){
        long mobileData = TrafficStats.getMobileRxBytes()+TrafficStats.getMobileTxBytes();
        long wifiData = TrafficStats.getTotalRxBytes()+TrafficStats.getTotalTxBytes()-mobileData;

        long mobileDbData = DbManager.getInstance(context)
                .getTrafficTotal(DbManager.UID_TOTAL_MOBILE);

        long wifiDbData = DbManager.getInstance(context)
                .getTrafficTotal(DbManager.UID_TOTAL_WIFI);
        if (mobileData-mobileDbData>2047) {
            DbManager.getInstance(context).setTrafficTotal(DbManager.UID_TOTAL_MOBILE,
                    mobileData);
            DbManager.getInstance(context).setTrafficChange(DbManager.UID_TOTAL_MOBILE,
                    mobileData - mobileDbData);

        }

        if (wifiData - wifiDbData > 2047) {
            DbManager.getInstance(context)
                    .setTrafficTotal(DbManager.UID_TOTAL_WIFI, wifiData);
            DbManager.getInstance(context)
                    .setTrafficChange(DbManager.UID_TOTAL_WIFI, wifiData - wifiDbData);

        }
    }

    //更新App流量

    private static void fetchAppTraffic(Context context){
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(
                intent, 0);
        for (ResolveInfo resolveInfo:resolveInfos){
            String packageName = resolveInfo.activityInfo.packageName;
            String name = resolveInfo.loadLabel(packageManager).toString();
            int uid;
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(
                        packageName, 0);
                uid = packageInfo.applicationInfo.uid;
                long received = TrafficStats.getUidRxBytes(uid);
                long transmitted = TrafficStats.getUidTxBytes(uid);
                long total = received + transmitted;
                if (total==0) {
                    continue;
                }
                long totalInDb = DbManager.getInstance(context).getTrafficTotal(uid);
                if (total-totalInDb>2047) {
                    DbManager.getInstance(context).setTrafficTotal(uid,total);
                    DbManager.getInstance(context).setTrafficChange(uid, total - totalInDb);

                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }


    }

    //指定uid的day前的数据（包括今天）
    public static List<DateTrafficModel> fetchDayTrrafic(Context context, int uid, int day){
        long[] days = getDaysTimestamp(day);
        List<DateTrafficModel> datas = new ArrayList<>();
        for (int i=0;i<day;i++){
            DateTrafficModel model = new DateTrafficModel();
            model.setDate(days[i]);
            long traffic = DbManager.getInstance(context).getTraffic(uid,days[i]);
            model.setTraffic(traffic);
            datas.add(model);
        }
        return datas;
    }

    private static long[] getDaysTimestamp(int day){
        long[] days = new long[day];
        long cur = currentTime();
        for (int i = 1; i <= day; i++) {
            days[day - i] = cur - (i - 1) * 24 * 60 * 60 * 1000;
        }
        return days;

    }

    public static long currentTime() {
        return System.currentTimeMillis();
    }

    //定时更新流量的服务
    public static void startRepeatingService(Context context,
                                             int seconds, Class<?> cls, String action){

        AlarmManager alarmManager =
                (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long triggerAtTime = SystemClock.elapsedRealtime();
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime,
                seconds * 1000, pendingIntent);
    }
    //停止定时更新
    public static void stopRepeatingService(Context context, Class<?> cls, String action) {
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
    }



}
