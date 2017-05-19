package com.entrada.cheekyMonkey.steward.billEdit;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by CSAT on 26/10/2015.
 */


public class FetchBillNoTask extends AsyncTask<String, String, String> {

    ProgressDialog progressDialog;
    ArrayAdapter<String> adapter;
    ArrayList<String> billAmtList;
    private String parameter, serverIP;

    public FetchBillNoTask(Context context, ArrayAdapter<String> adapter, ArrayList<String> amtList, String parameter, String serverIP) {

        this.adapter = adapter;
        this.billAmtList = amtList;
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
    protected String doInBackground(String... params) {

        String response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_FETCH_BILLS_DETAILS, parameter);
        Logger.d("DirectSettlement", "::" + response);

        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONObject jsonObject1 = jsonObject.getJSONObject("BillsDetailsResult");
            JSONArray jsonArray1 = jsonObject1.getJSONArray("Bill");

            for (int i = 0; i < jsonArray1.length(); i++) {

                JSONObject jsonObject3 = jsonArray1.getJSONObject(i);

                String BILL_NO = jsonObject3.getString("BILL_NO");
                billAmtList.add(jsonObject3.getString("TOTAL"));

                publishProgress(BILL_NO);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        adapter.add(values[0]);
    }


    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}

