package com.entrada.cheekyMonkey;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entrada.cheekyMonkey.appInterface.ICallBackFinish;
import com.entrada.cheekyMonkey.appInterface.OnBackPressInterface;
import com.entrada.cheekyMonkey.dynamic.BaseFragmentActivity;
import com.entrada.cheekyMonkey.dynamic.Network_Info.NetworkChangeReceiver;
import com.entrada.cheekyMonkey.dynamic.Network_Info.NetworkUtil;
import com.entrada.cheekyMonkey.dynamic.start.MainScreenFragment;
import com.entrada.cheekyMonkey.dynamic.syncData.FetchAndStoreMenuItemsTask;
import com.entrada.cheekyMonkey.dynamic.syncData.ICallResponse;
import com.entrada.cheekyMonkey.staticData.PrefHelper;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.steward.notificationUI.AdminActivity;
import com.entrada.cheekyMonkey.steward.notificationUI.KitchenActivity;
import com.entrada.cheekyMonkey.uiDialog.ExitDialog;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;


/**
 * Created by Tanuj.Sareen on 31/12/2014.
 */
public class IntroductionScreen extends FragmentActivity implements ICallBackFinish, ICallResponse {

    ImageView comp_logo;
    OnBackPressInterface backPressInterface;
    NetworkChangeReceiver receiver;
    public RelativeLayout loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.introductionscreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadingScreen = (RelativeLayout)findViewById(R.id.layout_loading);
        //comp_logo = (ImageView) findViewById(R.id.image_csat_logo);

        ProgressBar progress_loading = (ProgressBar) findViewById(R.id.progress_loading);
        progress_loading.getIndeterminateDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
        ProgressBar progress_failure = (ProgressBar) findViewById(R.id.progress_failure);
        progress_failure.getIndeterminateDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);

        TextView tv_retry = (TextView) findViewById(R.id.tv_retry);
        tv_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCurrentPrice();
            }
        });

        if (Build.VERSION.SDK_INT >= 19){
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        /*String tempURl = PrefHelper.getStoredString(this,PrefHelper.PREF_FILE_NAME,PrefHelper.TEMP_URL);
        if (! tempURl.isEmpty())
            UserInfo.ServerIP = tempURl ;*/

        setNetStatusController();
        goToMainScreen();
    }


    public void setNetStatusController(){

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");

        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
    }

    public void goToMainScreen(){

        if (NetworkUtil.getConnectivityStatus(this) != 0) {

           /* String serverIP =  PrefHelper.getStoredString(this, PrefHelper.PREF_FILE_NAME, PrefHelper.SERVER_IP);
            if (! serverIP.isEmpty())
                UserInfo.ServerIP = serverIP;*/

            if (PrefHelper.getStoredString(IntroductionScreen.this,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.FIRST_TIME_LUNCHAPP)
                    .equalsIgnoreCase(PrefHelper.FIRST_TIME_LUNCHAPP_DONE)) {

                //fetchCurrentPrice();
                Intent intent = new Intent(IntroductionScreen.this, BaseFragmentActivity.class);
                startActivity(intent);
                finish();

            } else if (PrefHelper.getStoredString(IntroductionScreen.this,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.ADMIN_LOGIN)
                    .equalsIgnoreCase(PrefHelper.ADMIN_LOGIN_DONE)) {

                Intent intent = new Intent(IntroductionScreen.this, AdminActivity.class);
                startActivity(intent);
                finish();

            } else if (PrefHelper.getStoredString(IntroductionScreen.this,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.KITCHEN_LOGIN)
                    .equalsIgnoreCase(PrefHelper.KITCHEN_LOGIN_DONE)) {

                Intent intent = new Intent(this, KitchenActivity.class);
                startActivity(intent);
                finish();

            }else
                showLoginScreen();
        }
    }

    public void showLoginScreen() {

//        FragmentTransaction transaction;
//        transaction = getSupportFragmentManager().beginTransaction();
//        MainScreenFragment manageFrag = new MainScreenFragment();
//        transaction.replace(R.id.introductionScreenFrameLayout, manageFrag,
//                StaticConstants.LOGIN_FRAGMENT_TAG);
//
//        transaction.commit();
//        backPressInterface = manageFrag;
        Intent intent = new Intent(IntroductionScreen.this, Add_Outlet.class);
        startActivity(intent);

    }


    public void fetchCurrentPrice(){

        if (NetworkUtil.getConnectivityStatus(this) != 0){

            loadingScreen.setVisibility(View.VISIBLE);

            String parameter = UtilToCreateJSON.createToken();
            String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
            FetchAndStoreMenuItemsTask menuItemsTask = new FetchAndStoreMenuItemsTask(
                    IntroductionScreen.this, parameter, serverIP, null, this);
            menuItemsTask.execute();
        }
    }


    @Override
    public void getResponseFromServer(String response) {

        if (response.equals("success1")) {

            Intent intent = new Intent(IntroductionScreen.this, BaseFragmentActivity.class);
            startActivity(intent);
            finish();
            //loadingScreen.setVisibility(View.GONE);
        }

    }


    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        if (backPressInterface == null)
            super.onBackPressed();

        else if (! backPressInterface.onBackPress()) {
            ExitDialog exitDialog = new ExitDialog(this, this);
            exitDialog.show();
        }
    }

    @Override
    public void onFinishDialog() {
        finish();
    }
}
