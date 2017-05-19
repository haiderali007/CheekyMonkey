package com.entrada.cheekyMonkey.steward.bill;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.entrada.cheekyMonkey.steward.home_del.HomeItem;
import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CSAT on 26/10/2015.
 */


public class CreditDetailTask extends AsyncTask<String, HomeItem, HomeItem> {

    ProgressDialog progressDialog;
    CreditDetailsAdapter adapter;
    private String parameter, serverIP;

    public CreditDetailTask(Context context, CreditDetailsAdapter adapter, String parameter, String serverIP) {

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


        String response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_CREDIT_DETAILS, parameter);
        Logger.d("CreditDetail", "::" + response);

        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("settlementResult");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                HomeItem homeItem = new HomeItem();

                homeItem.creditCode = jsonObject3.getString("Code");
                homeItem.creditDesc = jsonObject3.getString("Desc");
                homeItem.creditType = jsonObject3.getString("About");

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

