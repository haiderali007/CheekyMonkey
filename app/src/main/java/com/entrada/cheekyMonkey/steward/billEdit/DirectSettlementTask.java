package com.entrada.cheekyMonkey.steward.billEdit;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.steward.home_del.HomeItem;
import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by CSAT on 26/10/2015.
 */


public class DirectSettlementTask extends AsyncTask<String, HomeItem, HomeItem> {

    ProgressDialog progressDialog;
    PendingBillsAdapter adapter;
    private String parameter, serverIP;

    public DirectSettlementTask(Context context, PendingBillsAdapter adapter, String parameter, String serverIP) {

        this.adapter = adapter;
        this.parameter = parameter;
        this.serverIP = serverIP;
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


        String response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_PENDING_BILLS, parameter);
        Logger.d("DirectSettlement", "::" + response);

        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("SendBillDetailResult");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("Billdet");
            JSONArray jsonArray2 = jsonObject1.getJSONArray("Del");

            boolean b = jsonArray1.length() == jsonArray2.length();
            String posType = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posType;

            for (int i = 0; i < jsonArray1.length(); i++) {

                JSONObject jsonObject3 = jsonArray1.getJSONObject(i);
                HomeItem homeItem = new HomeItem();

                homeItem.BILL_NO = jsonObject3.getString("BILL_NO");
                homeItem.DISCOUNT = String.format("%.2f",jsonObject3.getDouble("DISCOUNT"));
                homeItem.SUBTOTAL = String.format("%.2f", jsonObject3.getDouble("SUBTOTAL"));
                homeItem.TAXES = String.format("%.2f", jsonObject3.getDouble("TAXES"));
                homeItem.TOTAL = String.format("%.2f", jsonObject3.getDouble("TOTAL"));
                homeItem.TABLE = jsonObject3.getString("Tbl");

                if (posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_H)) {

                    JSONObject jsonObject4 = jsonArray2.getJSONObject(i);
                    if (homeItem.BILL_NO.equals(jsonObject4.getString("Billno"))) {

                        homeItem.address = jsonObject4.getString("Add");
                        homeItem.guestName = jsonObject4.getString("GuestName");
                        homeItem.phone = jsonObject4.getString("Phone");
                    }
                }

                publishProgress(homeItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(HomeItem... values) {
        super.onProgressUpdate(values);
        adapter.addDataSetItem(values[0]);
    }


    @Override
    protected void onPostExecute(HomeItem response) {
        super.onPostExecute(response);

        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}

