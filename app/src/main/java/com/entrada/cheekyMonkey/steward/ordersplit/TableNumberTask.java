package com.entrada.cheekyMonkey.steward.ordersplit;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by csat on 05/06/2015.
 */
public class TableNumberTask extends AsyncTask<String, String, String> {

    private ArrayAdapter<String> adapterTable;
    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;
    private String response = "";

    public TableNumberTask(Context context, ArrayAdapter<String> adapterTable, String parameter, String serverIP, ProgressBar mDialog) {

        this.context = context;
        this.adapterTable = adapterTable;
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

        response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.FATCH_TABLE_STATUS, parameter);
        try {
            Logger.d("ECABS_ExplorerNEWResult", "::" + response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("ECABS_ExplorerNEWResult"));
            ArrayList<String> list = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                String tbl = jsonObject.getString("Tbl");

                if (!list.contains(tbl)) {
                    list.add(tbl);
                    publishProgress(tbl);
                }
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
        adapterTable.add(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (mDialog != null)
            mDialog.setVisibility(View.GONE);
    }

}
