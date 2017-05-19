package com.entrada.cheekyMonkey.dynamic.google.gcm;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Set;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.dynamic.syncData.FetchAndStoreMenuItemsTask;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.staticData.PrefHelper;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;


public class GcmService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        JSONObject jsonObject = new JSONObject();
        Set<String> keys = data.keySet();
        for (String key : keys) {
            try {
                jsonObject.put(key, data.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            sendNotification(jsonObject.toString(5));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeletedMessages() {
        sendNotification("Deleted messages on server");
    }

    @Override
    public void onMessageSent(String msgId) {
        sendNotification("Upstream message sent. Id=" + msgId);
    }

    @Override
    public void onSendError(String msgId, String error) {
        sendNotification("Upstream message send error. Id=" + msgId + ", error" + error);
    }

    private void sendNotification(final String msg) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject(msg);
                    String message = jsonObject.getString("message");

                    if (message.contains("Rate Change")) {
                        message = message.replace("Rate Change", "");

                        String messageData[] = message.split("\6");

                        String itemCode = messageData[0];
                        String itemName = messageData[1];
                        String prevPrice = messageData[2];
                        String currPrice = messageData[3];

                        updateItemPrice(itemCode, currPrice);
                        showMessage("Price of " + itemName + " changes from " + prevPrice + " to " + currPrice + ".");

                    } else if (message.contains("Market Crash")) {

                        String parameter = UtilToCreateJSON.createToken();
                        String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
                        FetchAndStoreMenuItemsTask menuItemsTask = new FetchAndStoreMenuItemsTask(getApplicationContext(),
                                parameter, serverIP, null, null);
                        menuItemsTask.execute();
                        showMessage(message);

                    } else if (message.contains("Order Status")) {

                        message = message.replaceAll("Order Status", "");
                        String orderDetail[] = message.split("\6");
                        String odrNo = orderDetail[0];
                        String odrStatus = orderDetail[1];
                        updateOrderStatus(odrNo, odrStatus);

                        odrStatus = odrStatus.equals("B") ? "approved." : "rejected.";
                        showMessage("Your order " + odrNo + " is " + odrStatus);

                        /*if (getApplicationContext() instanceof BaseFragmentActivity){
                            if (odrStatus.equals("B"))
                                ((BaseFragmentActivity)getApplicationContext()).sendNotification(getString(R.string.accepted_noti, UserInfo.guest_name, odrNo));
                            else
                                ((BaseFragmentActivity)getApplicationContext()).sendNotification(getString(R.string.rejected_noti, UserInfo.guest_name, odrNo));
                        }*/

                    } else {
                        String eventDate = jsonObject.getString("contentTitle");
                        message = "New offer : " + message + " on " + eventDate + ".";
                        showMessage(message);
                        PrefHelper.storeString(getApplicationContext(), PrefHelper.PREF_FILE_NAME, PrefHelper.EVENT_MESSAGE, message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    showMessage(msg);
                }

            }
        });
    }


    public void showMessage(String message){

        if (!UserInfo.appIsRunning)
            return;

        boolean appIsInForeground = true;

        try {

            if (Build.VERSION.SDK_INT < 21){

                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                // The first in the list of RunningTasks is always the foreground task.
                ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);

                String foregroundTaskPackageName = foregroundTaskInfo .topActivity.getPackageName();
                PackageManager pm = getPackageManager();
                PackageInfo foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);
                String foregroundTaskAppName = foregroundAppPackageInfo.applicationInfo.loadLabel(pm).toString();

                if (! foregroundTaskAppName.equals(getResources().getString(R.string.app_name)))
                    appIsInForeground = false;

            }else {

                ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();
                String foregroundProcessName = tasks.get(0).processName;
                PackageManager pm = getPackageManager();
                PackageInfo foregroundAppPackageInfo = pm.getPackageInfo(foregroundProcessName, 0);
                String foregroundTaskAppName = foregroundAppPackageInfo.applicationInfo.loadLabel(pm).toString();

                if (! foregroundTaskAppName.equals(getResources().getString(R.string.app_name)))
                    appIsInForeground = false;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        if (appIsInForeground)
            Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }



    public void updateItemPrice(String code, String currPrice) {

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(this).getWritableDatabase();
        mdb.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(DBConstants.Current_Rate, currPrice);
            mdb.update(DBConstants.ITEMS_DETAIL_TABLE, cv, DBConstants.Item_Code + "=" + code, null);
            mdb.setTransactionSuccessful();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }
    }

    public void updateOrderStatus(String orderNum, String status){

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(getApplicationContext()).getWritableDatabase();
        mdb.beginTransaction();
        try {

            ContentValues cv = new ContentValues();
            cv.put(DBConstants.KEY_GUEST_ORDER_NUMBER, orderNum);
            cv.put(DBConstants.KEY_GUEST_ORDER_STATUS, status);

            mdb.update(DBConstants.KEY_GUEST_ORDERS_TABLE, cv, DBConstants.KEY_GUEST_ORDER_NUMBER + "= '"+orderNum+"' ", null);
            mdb.setTransactionSuccessful();

        } catch (Exception ex) {ex.printStackTrace();}
        finally {mdb.endTransaction();}
    }

}