package com.jredu.com;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.jredu.com.R;

public class WelcomeActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        intiActionBar();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this,LBESofeActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);

    }

    private void intiActionBar(){
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable=
                new ColorDrawable(Color.TRANSPARENT);
        actionBar.setBackgroundDrawable(colorDrawable);
    }


}
