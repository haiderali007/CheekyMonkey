package com.entrada.cheekyMonkey.steward.ordersplit;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by csat on 31/08/2015.
 */
public class FetchOrderNoTask extends AsyncTask<String, Void, String> {

    private ArrayAdapter<String> table_order_no;
    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;
    private String response = "";
    private ArrayList<String> Order_list = new ArrayList<>();
    private Spinner spinnerOrderNo;

    public FetchOrderNoTask(Context context, Spinner spinnerOrderNo, ArrayAdapter<String> table_order_no, String parameter,
                            String serverIP, ProgressBar mDialog, ArrayList<String> Order_list) {

        this.context = context;
        this.spinnerOrderNo = spinnerOrderNo;
        this.table_order_no = table_order_no;
        this.mDialog = mDialog;
        this.parameter = parameter;
        this.serverIP = serverIP;
        this.Order_list = Order_list;
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

            Logger.d("ECABS_Order", "::" + response);


            JSONArray jsonArray ;
            JSONObject jsonObject = new JSONObject(response);
            jsonArray = new JSONArray(jsonObject.getString("ECABS_OrderResult"));
            if (jsonArray.length() > 0) Order_list.add("");

            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObject = jsonArray.getJSONObject(i);
                Order_list.add(jsonObject.getString("Code"));
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        table_order_no.notifyDataSetChanged();

        if (mDialog != null)
            mDialog.setVisibility(View.GONE);

        if (! table_order_no.isEmpty())
            spinnerOrderNo.setSelection(0);
    }
}
