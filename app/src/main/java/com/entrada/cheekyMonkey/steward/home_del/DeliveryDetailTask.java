package com.entrada.cheekyMonkey.steward.home_del;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by CSAT on 26/10/2015.
 */


public class DeliveryDetailTask extends AsyncTask<String, HomeItem, HomeItem> {

    ICallDelDetail iCallDelDetail;
    ProgressDialog progressDialog;
    DelLogAdapter delLogAdapter;
    private Context context;
    private String serverIP;
    private String parameter;

    public DeliveryDetailTask(Context context, DelLogAdapter delLogAdapter, String parameter, String serverIP,
                              ICallDelDetail iCallDelDetail) {
        this.context = context;
        this.serverIP = serverIP;
        this.parameter = parameter;
        this.iCallDelDetail = iCallDelDetail;
        this.delLogAdapter = delLogAdapter;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    protected HomeItem doInBackground(String... params) {

        Logger.i(serverIP, "::" + serverIP);
        String response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_HOME_DELIVERY_DETAIL, parameter);

        Logger.i("DelLog" + "::", response);
        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("deliverydetailResult");

            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObject = jsonArray.getJSONObject(i);

                HomeItem homeItem = new HomeItem();
                homeItem.amt = jsonObject.getString("Amount");
                homeItem.billNo = jsonObject.getString("Billno");
                homeItem.changAmt = jsonObject.getString("Camt");
                homeItem.guestName = jsonObject.getString("GuestName");
                homeItem.OdrTime = jsonObject.getString("Otime");
                homeItem.Pos = jsonObject.getString("Pos");
                homeItem.retAmt = jsonObject.getString("Ramt");
                homeItem.setAddress(jsonObject.getString("Add"));
                homeItem.phone = jsonObject.getString("Phone");
                homeItem.delBoy = jsonObject.getString("Del");
                homeItem.delBoyName = jsonObject.getString("Delname");

                if (!jsonObject.getString("Dtime").contains("00:00"))
                    homeItem.del_time = jsonObject.getString("Dtime");

                if (!jsonObject.getString("Rtime").contains("00:00"))
                    homeItem.retTime = jsonObject.getString("Rtime");

                publishProgress(homeItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(HomeItem... values) {
        super.onProgressUpdate(values);
        delLogAdapter.addDataSetItem(values[0]);
    }

    @Override
    protected void onPostExecute(HomeItem response) {
        super.onPostExecute(response);

        iCallDelDetail.getDelDetail("");
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}

