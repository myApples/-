package com.jredu.com.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jredu.com.entity.ListMen;

import java.sql.SQLException;

/**
 * Created by Administrator on 2015/10/9 0009.
 */
public class DBListManHelper extends OrmLiteSqliteOpenHelper{

    private static DBListManHelper DbHelper;
    private static String DB_NAME= "saft_db";
    private static int DB_VERSION=1;

    public DBListManHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);

    }


    public static DBListManHelper getInstance(Context context){
        synchronized (DBListManHelper.class) {
            if (DbHelper==null) {
                synchronized (DBListManHelper.class) {
                    if (DbHelper==null) {
                        DbHelper = new DBListManHelper(context);
                    }
                }
            }
        }
        return DbHelper;

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ListMen.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

    }

    private Dao<ListMen,Integer> listDao=null;
    public Dao<ListMen,Integer> getListDao(){
        if (listDao==null){
            try {
                listDao = getDao(ListMen.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return listDao;
    }

    @Override
    public void close() {
        super.close();
        listDao=null;
    }
}
