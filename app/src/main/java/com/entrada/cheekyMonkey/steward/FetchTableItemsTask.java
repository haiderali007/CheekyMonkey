package com.entrada.cheekyMonkey.steward;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.takeorder.adapter.TakeOrderAdapter;
import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by csat on 15/06/2015.
 */
public class FetchTableItemsTask extends AsyncTask<String, OrderItem, OrderItem> {

    private TakeOrderAdapter takeOrderAdapter;
    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;
    private String guest = "";
    private String guest_code = "";


    public FetchTableItemsTask(Context context, TakeOrderAdapter takeOrderAdapter, String parameter,
                               String serverIP, ProgressBar mDialog) {

        this.context = context;
        this.takeOrderAdapter = takeOrderAdapter;
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
    protected OrderItem doInBackground(String... params) {

        String response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_FATCH_ORDER_ITEM_BILL, parameter);

        try {

            Logger.d("ECABS_PullRunOrderResult", "::" + response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("ECABS_PullRunOrderResult"));

            OrderItem obj_order;

            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObject = jsonArray.getJSONObject(i);

                obj_order = new OrderItem();
                obj_order.o_code = jsonObject.getString("Code");
                obj_order.o_name = jsonObject.getString("Name");
                obj_order.o_quantity = Float.parseFloat(jsonObject.getString("Qty"));
                obj_order.o_price = Float.parseFloat(jsonObject.getString("Itemprice"));
                obj_order.o_amount = obj_order.o_quantity * obj_order.o_price;
                guest = jsonObject.getString("guest");
                guest_code = jsonObject.getString("GstCode");

                publishProgress(obj_order);

            }

        } catch (JSONException e) {
            e.printStackTrace();


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }


    @Override
    protected void onProgressUpdate(OrderItem... values) {
        super.onProgressUpdate(values);
        takeOrderAdapter.addDataSetItem(values[0]);

    }

    @Override
    protected void onPostExecute(OrderItem result) {
        super.onPostExecute(result);
        if (mDialog != null)
            mDialog.setVisibility(View.GONE);

    }
}
