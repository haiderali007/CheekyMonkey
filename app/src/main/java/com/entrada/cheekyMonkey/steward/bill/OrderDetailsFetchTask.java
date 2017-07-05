package com.entrada.cheekyMonkey.steward.bill;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by csat on 15/06/2015.
 */
public class OrderDetailsFetchTask extends AsyncTask<String, OrderTable, OrderTable> {

    private AdapterOrderTable adapterOrderTable;
    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;
    private String response = "";
    private String total_price = "";
    private String total_qty = "";
    private String guest = "";
    private String guest_code = "";
    private CustomTextview textViewTotalQuantity, textViewTotalPrice, textViewGuestName;


    public OrderDetailsFetchTask(Context context, AdapterOrderTable adapterOrderTable, String parameter,
                                 String serverIP, ProgressBar mDialog, CustomTextview textViewTotalQuantity,
                                 CustomTextview textViewTotalPrice, CustomTextview textViewGuestName) {

        this.context = context;
        this.adapterOrderTable = adapterOrderTable;
        this.mDialog = mDialog;
        this.parameter = parameter;
        this.serverIP = serverIP;
        this.textViewTotalQuantity = textViewTotalQuantity;
        this.textViewTotalPrice = textViewTotalPrice;
        this.textViewGuestName = textViewGuestName;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mDialog != null)
            mDialog.setVisibility(View.VISIBLE);
    }

    @Override
    protected OrderTable doInBackground(String... params) {

        response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_FATCH_ORDER_ITEM_BILL, parameter);

        try {

            Logger.d("ECABS_PullRunOrderResult", "::" + response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("ECABS_PullRunOrderResult"));
            String Order = "";

            OrderTable obj_order = null;
            float d = 0.0f;
            int q = 0;

            //for (int i = jsonArray.length()-1; i >= 0; i--) {
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);


                if (jsonObject.getString("Order").equals(Order)) {

                    obj_order = new OrderTable();
                    obj_order.order_no = "";
                    UserInfo.Orderno = obj_order.order_no = jsonObject.getString("Order");
                    obj_order.item_name = jsonObject.getString("Name");
                    obj_order.item_qty = jsonObject.getString("Qty");
                    int q1 = Integer.parseInt(obj_order.item_qty);
                    obj_order.item_price = jsonObject.getString("Itemprice");
                    guest = jsonObject.getString("guest");
                    guest_code = jsonObject.getString("GstCode");


                    float d1 = Float.parseFloat(obj_order.item_price);

                    d = d + d1;
                    q = q + q1;

                    publishProgress(obj_order);

                } else {

                    obj_order = new OrderTable();

                    UserInfo.Orderno = obj_order.order_no = jsonObject.getString("Order");
                    obj_order.item_name = jsonObject.getString("Name");
                    obj_order.item_qty = jsonObject.getString("Qty");
                    int q1 = Integer.parseInt(obj_order.item_qty);
                    obj_order.item_price = jsonObject.getString("Itemprice");
                    guest = jsonObject.getString("guest");

                    float d1 = Float.parseFloat(obj_order.item_price);

                    d = d + d1;
                    q = q + q1;

                    publishProgress(obj_order);
                }
                total_price = "" + d;
                total_qty = "" + q;


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
    protected void onProgressUpdate(OrderTable... values) {
        super.onProgressUpdate(values);
        adapterOrderTable.addDataSetItem(values[0]);

    }

    @Override
    protected void onPostExecute(OrderTable result) {
        super.onPostExecute(result);
        if (mDialog != null)
            mDialog.setVisibility(View.GONE);

        textViewTotalQuantity.setText(total_qty);
        textViewTotalPrice.setText(total_price);
        textViewGuestName.setText(guest);

    }

}
