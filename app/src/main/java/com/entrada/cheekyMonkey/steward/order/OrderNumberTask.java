package com.entrada.cheekyMonkey.steward.order;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.entrada.cheekyMonkey.steward.ordersplit.OrderNumberAdapter;
import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONObject;


public class OrderNumberTask extends AsyncTask<String, String, String> {

    private OrderNumberAdapter adapter;
    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;
    private String response = "";


    public OrderNumberTask(Context context, OrderNumberAdapter adapter, String parameter, String serverIP, ProgressBar mDialog) {

        this.context = context;
        this.adapter = adapter;
        this.mDialog = mDialog;
        this.parameter = parameter;
        this.serverIP = serverIP;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mDialog != null)
            mDialog.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {

        response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_ECABS_Order, parameter);

        try {

            Logger.d("OrderNumberResponse", "::" + response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("ECABS_OrderResult"));

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                publishProgress(jsonObject.getString("Code"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        adapter.addDataSetItem(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (mDialog != null)
            mDialog.setVisibility(View.GONE);

    }

}
