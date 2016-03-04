package com.jredu.com;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.jredu.com.R;
import com.jredu.com.adapter.FromListMenAdapter;
import com.jredu.com.entity.ListMen;
import com.jredu.com.helper.DBListManHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FromListMenActivity extends AppCompatActivity {

    private ListView fromListMen;
    private List<ListMen> mData = new ArrayList<>();
    private ListMen listMens ;
    private FromListMenAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_list_men);
        fromListMen = (ListView)findViewById(R.id.fromListMen);
        intiGetPhoneNumber();
        intiActionBar();
    }


    private List<ListMen> menList = new ArrayList<>();
    public void intiGetPhoneNumber(){

        ContentResolver contentResolver = getContentResolver();
        Uri uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor=
                contentResolver.query(uri, null, null, null, null);
        if(cursor!=null){
            while(cursor.moveToNext()){
                int nameIndex=
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name=cursor.getString(nameIndex);

                int numberIndex=
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number=cursor.getString(numberIndex);

                listMens=new ListMen(name,number);
                mData.add(listMens);
            }
        }

        adapter = new FromListMenAdapter(mData,this);
        fromListMen.setAdapter(adapter);
        adapter.setListener(new FromListMenAdapter.onListManSelectedChangedListener() {
            @Override
            public void onListManSelectedChanged(ListMen listMen) {
                if (listMen.isCheckBox()) {
                    menList.add(listMen);

                } else {
                    menList.remove(listMen);
                }
            }
        });
        cursor.close();
    }

    private Dao<ListMen,Integer> listMenIntegerDao=null;
    private List<ListMen> man=null;
    String num="";
    String numm="";
    public void addChoss(View view) {
        if (menList.size()==0) {
            Toast.makeText(this,"请选择要加入黑名单的号码",Toast.LENGTH_SHORT).show();
        }else {
            listMenIntegerDao = DBListManHelper.getInstance(this).getListDao();
            try {
                listMenIntegerDao.create(menList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Toast.makeText(this,"添加添加成功",Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(this,BlackListActivity.class);
            startActivity(intent);
            finish();

        }
    }

    private ActionBar actionBar;
    public void intiActionBar(){
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setTitle("从联系人添加");


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
