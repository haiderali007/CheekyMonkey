package com.entrada.cheekyMonkey.steward.bill;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.takeorder.adapter.TableAdapter;
import com.entrada.cheekyMonkey.takeorder.entity.TableItem;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by csat on 15/06/2015.
 */
public class TableBillGenTask extends AsyncTask<String, TableItem, TableItem> {

    private TableAdapter adapter_bill_gen;
    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;
    private String response = "";


    public TableBillGenTask(Context context, TableAdapter adapter_bill_gen, String parameter, String serverIP, ProgressBar mDialog) {

        this.context = context;
        this.adapter_bill_gen = adapter_bill_gen;
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
    protected TableItem doInBackground(String... params) {

        response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_FATCH_TABLE_BILL, parameter);
        Logger.d("ECABS_TableResult", "::" + response);

        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("ECABS_TableResult"));
            TableItem obj_list;

            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObject = jsonArray.getJSONObject(i);

                obj_list = new TableItem();
                obj_list.code = jsonObject.getString("Code");
                obj_list.name = jsonObject.getString("Desc");
                obj_list.status = "Fill";

                publishProgress(obj_list);

            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }


    @Override
    protected void onProgressUpdate(TableItem... values) {
        super.onProgressUpdate(values);
        adapter_bill_gen.addDataSetItem(values[0]);
    }

    @Override
    protected void onPostExecute(TableItem result) {
        super.onPostExecute(result);
        if (mDialog != null)
            mDialog.setVisibility(View.GONE);
    }
}
