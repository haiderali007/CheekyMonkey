package com.entrada.cheekyMonkey.steward.ordersplit;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by csat on 31/08/2015.
 */
public class TableItemTask extends AsyncTask<String, OrderItem, OrderItem> {

    private OrderSplitAdapter orderSplitAdapter;
    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;
    private String response = "";
    private ArrayList<OrderItem> arrayList_order_split;

    public TableItemTask(Context context, OrderSplitAdapter orderSplitAdapter, String parameter, String serverIP, ProgressBar mDialog, ArrayList<OrderItem> arrayList_order_split) {

        this.context = context;
        this.orderSplitAdapter = orderSplitAdapter;
        this.mDialog = mDialog;
        this.parameter = parameter;
        this.serverIP = serverIP;
        this.arrayList_order_split = arrayList_order_split;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mDialog != null)
            mDialog.setVisibility(View.VISIBLE);
    }

    @Override
    protected OrderItem doInBackground(String... params) {

        response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_ECABS_ORDER_DETAIL, parameter);

        try {

            Logger.d("ECABS_PullOrderForModify", "::" + response);


            JSONArray jsonArray;
            JSONObject jsonObject = new JSONObject(response);
            jsonArray = new JSONArray(jsonObject.getString("ECABS_PullOrderForModifyResult"));

            OrderItem obj_order;
            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObject = jsonArray.getJSONObject(i);

                obj_order = new OrderItem();
                obj_order.orderNo = jsonObject.getString("orderno");
                obj_order.o_code = jsonObject.getString("Code");
                obj_order.o_itm_rmrk = jsonObject.getString("remark");
                obj_order.o_addon_code = jsonObject.getString("addon");
                obj_order.o_mod = jsonObject.getString("modifier");
                obj_order.o_combo_code = jsonObject.getString("zcomboItem");

                if (jsonObject.getString("zcomboItem").equals("Y")
                        || jsonObject.getString("zcomboItem").equals(""))
                    obj_order.o_name = jsonObject.getString("Name");
                else
                    obj_order.o_name = "##" + jsonObject.getString("Name");

                obj_order.o_happy_hour = jsonObject.getString("Name").equals("FREE") ? "Y" : "";
                obj_order.o_subunit = "";
                obj_order.o_quantity = Float.parseFloat(jsonObject.getString("qty"));
                obj_order.o_amount = Float.parseFloat(jsonObject.getString("dat"));
                obj_order.o_price = Float.parseFloat(jsonObject.getString("dat"));
                obj_order.o_disc = jsonObject.getString("Disc");

                arrayList_order_split.add(obj_order);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }


    @Override
    protected void onPostExecute(OrderItem result) {
        super.onPostExecute(result);

        if (mDialog != null)
            mDialog.setVisibility(View.GONE);
        orderSplitAdapter.notifyDataSetChanged();
    }
}