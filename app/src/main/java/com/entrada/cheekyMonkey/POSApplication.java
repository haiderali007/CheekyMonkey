package com.entrada.cheekyMonkey;

import android.app.Application;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.digits.sdk.android.Digits;
import com.entrada.cheekyMonkey.entity.DataModel;
import com.entrada.cheekyMonkey.entity.DataModelImpl;
import com.entrada.cheekyMonkey.util.Logger;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Administrator on 29/12/2014.
 */
public class POSApplication extends MultiDexApplication {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "7QkBwUZHqUtYsRqIgHfLlJXBY";
    private static final String TWITTER_SECRET = "qmLTE0S0CzmY2jiDmHBIFuQkdLP5hFxDIUQCVQA3bhjdxpFIsE";


    private static POSApplication singleton;
    public DataModel mDataModel;
    public boolean DEVELOPER_MODE = false;

    public POSApplication() {
    }

    public static POSApplication getSingleton() {
        //Logger.i("singleton", "return singleton");
        return singleton;
    }


    @Override
    public void onCreate() {

        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .penaltyDeath()
                    .penaltyFlashScreen()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .detectActivityLeaks()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    //.penaltyDeath()
                    .build());
        }

        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());

        singleton = this;
        mDataModel = new DataModelImpl(this);

        // POSDatabase.getInstanceLogin(getApplicationContext());
        Logger.i("singleton", "singleton =this");

/*        // Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);*/
    }

    public DataModel getmDataModel() {
        return mDataModel;
    }


}
