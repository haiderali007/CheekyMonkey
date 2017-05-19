package com.entrada.cheekyMonkey.dynamic.LocalService;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Rahul on 02/05/2016.
 */

public class MyService extends Service {

    //MyWebSocket mws;
    private final IBinder mBinder = new LocalBinder();
    Boolean onCall;
    String telephoneNumber;
    String myTelephoneNumber = null;

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i("bnf", arg0.getStringExtra("number"));
        return mBinder;
    }

    public class LocalBinder extends Binder {
        MyService getService() {
            Log.i("bnf", "localbinder");
            // Return this instance of LocalService so clients can call public methods
            return MyService.this;
        }
    }
}
