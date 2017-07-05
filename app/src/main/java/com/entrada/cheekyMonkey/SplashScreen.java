package com.entrada.cheekyMonkey;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entrada.cheekyMonkey.admin.ShowEmployeeActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import com.entrada.cheekyMonkey.dynamic.Network_Info.NetworkUtil;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.staticData.PrefHelper;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.TimeHandler;

public class SplashScreen extends Activity {

    private boolean mTimerExpired = false;
    private long delayMilliSec = 1000;

    private RelativeLayout relativeLayout;
    private CustomTextview txtAppTitle, txtVersion;
    private ImageView image_app;
    private Runnable mCloseSplashScreen = new Runnable() {
        @Override
        public void run() {

            if (mTimerExpired) {

                /*Intent intent = new Intent(SplashScreen.this, IntroductionScreen.class);
                startActivity(intent);*/
                Intent intent = new Intent(SplashScreen.this, ShowEmployeeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }
    };
    private Runnable mTimerTask = new Runnable() {
        @Override
        public void run() {
            mTimerExpired = true;
            closeSplash();
        }

        private void closeSplash() {
            runOnUiThread(mCloseSplashScreen);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        image_app = (ImageView) findViewById(R.id.imageView2);
        txtAppTitle = (CustomTextview) findViewById(R.id.txtAppTitle);
        txtVersion = (CustomTextview) findViewById(R.id.txtVersion);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            txtVersion.setText(version);

            /*String versionName = BuildConfig.VERSION_NAME;
            int versionCode = BuildConfig.VERSION_CODE;
            txtVersion.setText(versionName);*/

        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }

        /*UserInfo userInfo = POSApplication.getSingleton().getmDataModel().getUserInfo();
        String backgrnd = PrefHelper.getStoredString(this,
                PrefHelper.PREF_FILE_NAME, PrefHelper.BACKGROUND_COLOR);

        if (!TextUtils.isEmpty(backgrnd)) {
            relativeLayout.setBackgroundColor(Color.parseColor(backgrnd));
            txtAppTitle.setTextColor(Color.parseColor(PrefHelper
                    .getStoredString(this, PrefHelper.PREF_FILE_NAME,
                            PrefHelper.FONT_COLOR)));
            txtVersion.setTextColor(Color.parseColor(PrefHelper
                    .getStoredString(this, PrefHelper.PREF_FILE_NAME,
                            PrefHelper.FONT_COLOR)));
            txtAppTitle.setTextSize(Float.parseFloat(userInfo.getText_Size()) + 30);

        }*/

        UserInfo.PREV_NET_Status= "";
        UserInfo.error_message = "";
        getDeviceID();

        //StartAnimations();
        TimeHandler timeHandler = new TimeHandler(mTimerTask);
        timeHandler.sleep(delayMilliSec);

    }

    public void getDeviceID(){

        String token = PrefHelper.getStoredString(this, PrefHelper.PREF_FILE_NAME, PrefHelper.GCM_TOKEN);
        if (token.isEmpty()) {
            try {
                if (NetworkUtil.getConnectivityStatus(this) != 0)
                    getGCMToken();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
            UserInfo.deviceid = token;
    }

    private void getGCMToken() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {

                    String SENDER_ID = UserInfo.SenderID; // Sender ID for Dynamic Rate App provided by GCM Services
                    InstanceID instanceID = InstanceID.getInstance(SplashScreen.this);
                    String token = instanceID.getToken(SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                    if (token != null && !token.isEmpty()){
                        PrefHelper.storeString(SplashScreen.this, PrefHelper.PREF_FILE_NAME, PrefHelper.GCM_TOKEN, token);
                        UserInfo.deviceid = token;
                    }
                    Log.i("GCM", token);
                } catch (IOException e) {
                    e.printStackTrace();
                    UserInfo.error_message = e.getMessage();
                }
                return null;
            }
        }.execute();
    }


    private void StartAnimations() {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        TextView txtViewTitle = (TextView) findViewById(R.id.txtAppTitle);
        txtViewTitle.clearAnimation();
        txtViewTitle.startAnimation(anim);

    }


}
