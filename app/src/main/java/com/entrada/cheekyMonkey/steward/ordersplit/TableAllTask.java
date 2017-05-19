package com.entrada.cheekyMonkey.steward.ordersplit;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by csat on 05/06/2015.
 */
public class TableAllTask extends AsyncTask<String, TableStatus, TableStatus> {

    private TableStatusAdapter tableStatusAdapter;
    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;
    private String response = "";

    public TableAllTask(Context context, TableStatusAdapter tableStatusAdapter, String parameter, String serverIP, ProgressBar mDialog) {

        this.context = context;
        this.tableStatusAdapter = tableStatusAdapter;
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
    protected TableStatus doInBackground(String... params) {

        response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.FATCH_TABLE_STATUS, parameter);
        try {
            Logger.d("ECABS_ExplorerNEWResult", "::" + response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("ECABS_ExplorerNEWResult"));
            TableStatus obj_list;
            ArrayList<String> list = new ArrayList<>();


            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);

                if (!list.contains(jsonObject.getString("Tbl"))) {
                    obj_list = new TableStatus();
                    obj_list.Table_l = jsonObject.getString("Tbl");
                    list.add(obj_list.Table_l);

                    publishProgress(obj_list);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(TableStatus... values) {
        super.onProgressUpdate(values);
        tableStatusAdapter.addDataSetItem(values[0]);
    }

    @Override
    protected void onPostExecute(TableStatus result) {
        super.onPostExecute(result);
        if (mDialog != null)
            mDialog.setVisibility(View.GONE);

        tableStatusAdapter.notifyDataSetChanged();
    }

}
