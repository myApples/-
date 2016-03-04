package com.jredu.com.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.ITelephony;
import com.j256.ormlite.dao.Dao;
import com.jredu.com.entity.ListMen;
import com.jredu.com.helper.DBListManHelper;

import android.provider.Telephony;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;


/**
 * Created by Administrator on 2015/9/29 0029.
 */
public class PhoneStateChangedReceiver extends BroadcastReceiver{
    private Dao<ListMen,Integer> manDao;
    private List<ListMen> list=null;
    @Override
    public void onReceive(Context context, Intent intent) {

        manDao = DBListManHelper.getInstance(context).getListDao();
        try {
            list = manDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //拦截电话
        if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String phoneNumber =
                        intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                for(ListMen man:list){
                    String number=man.getListNumber();
                    if (phoneNumber!=null&&number!=null&&
                            number.endsWith(phoneNumber)) {
                        ITelephony iTelephony = getITelephony(context);
                        try {
                            iTelephony.endCall();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }finally {

                        }
                    }
                }

            }
        }




    }


    //反射机制
    private static ITelephony getITelephony(Context  context) {
        ITelephony iTelephony = null;
        TelephonyManager telephonyManager =
                (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
        Class<TelephonyManager> c= TelephonyManager.class;
        Method getITelephoneyMethod = null;
        try {
            getITelephoneyMethod = c.getDeclaredMethod("getITelephony",(Class[])null);
            getITelephoneyMethod.setAccessible(true);
            iTelephony = (ITelephony)getITelephoneyMethod.invoke(telephonyManager,(Object[])null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
        }
        return iTelephony;
    }
}
