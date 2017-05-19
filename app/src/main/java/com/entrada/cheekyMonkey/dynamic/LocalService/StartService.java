package com.entrada.cheekyMonkey.dynamic.LocalService;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import com.entrada.cheekyMonkey.R;


/**
 * Created by Rahul on 02/05/2016.
 */


public class StartService extends Activity {

    String telephoneNumber;
    TextView statusTextView, numberTextView;
    MyService myService;
    Boolean myBound;

    ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            Log.i("bnf", "qui");
            MyService.LocalBinder binder = (MyService.LocalBinder) arg1;
            myService = binder.getService();
            myBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            myBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.start_service_layout);

        statusTextView = (TextView) this.findViewById(R.id.statusTextView);
        numberTextView = (TextView) this.findViewById(R.id.numberTextView);

        Intent i = this.getIntent();
        telephoneNumber = i.getStringExtra("number");

        numberTextView.setText(telephoneNumber);

        if(isMyServiceRunning()){
            statusTextView.setText("Online");
            statusTextView.setTextColor(Color.GREEN);
        }else{
            statusTextView.setText("Offline");
            statusTextView.setTextColor(Color.RED);
        }

        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        //SupportClass.myService.setNumber(telephoneNumber);

    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.example.internetcall.MyService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
