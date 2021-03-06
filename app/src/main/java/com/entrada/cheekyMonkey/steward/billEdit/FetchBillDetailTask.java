package com.entrada.cheekyMonkey.steward.billEdit;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.entrada.cheekyMonkey.steward.home_del.HomeItem;
import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by CSAT on 26/10/2015.
 */


public class FetchBillDetailTask extends AsyncTask<String, HomeItem, HomeItem> {

    ProgressDialog progressDialog;
    PendingBillsAdapter adapter;
    private String parameter, serverIP;

    public FetchBillDetailTask(Context context, PendingBillsAdapter adapter, String parameter, String serverIP) {

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

        String response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_FETCH_BILLS_DETAILS, parameter);
        Logger.d("DirectSettlement", "::" + response);

        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONObject jsonObject1 = jsonObject.getJSONObject("BillsDetailsResult");
            JSONArray jsonArray1 = jsonObject1.getJSONArray("Bill");
            JSONArray jsonArray2 = jsonObject1.getJSONArray("Orditm");
            boolean b = jsonArray1.length() == jsonArray2.length();
            int prvsPosition = 0;

            for (int i = 0; i < jsonArray1.length(); i++) {

                JSONObject jsonObject3 = jsonArray1.getJSONObject(i);

                HomeItem homeItem = new HomeItem();
                homeItem.BILL_NO = jsonObject3.getString("BILL_NO");
                homeItem.DISCOUNT = jsonObject3.getString("DISCOUNT");
                homeItem.SUBTOTAL = jsonObject3.getString("SUBTOTAL");
                homeItem.TAXES = jsonObject3.getString("TAXES");
                homeItem.TOTAL = jsonObject3.getString("TOTAL");
                homeItem.TABLE = jsonObject3.getString("Tbl");
                homeItem.guestId = jsonObject3.getString("Gstcode");
                homeItem.guestName = jsonObject3.getString("Gstname").equals("null") ? "" :
                                    jsonObject3.getString("Gstname");

                int j;
                for (j = prvsPosition; j < jsonArray2.length(); j++) {

                    JSONObject jsonObject4 = jsonArray2.getJSONObject(j);
                    if (jsonObject4.getString("Billno").equals(homeItem.BILL_NO)) {

                        OrderItem orderItem = new OrderItem();
                        orderItem.o_code = jsonObject4.getString("Name");
                        orderItem.o_name = jsonObject4.getString("Itmname");
                        orderItem.o_quantity = (float) jsonObject4.getDouble("Qty");
                        orderItem.o_amount = (float) jsonObject4.getDouble("Itemprice");
                        orderItem.o_price = orderItem.o_amount / orderItem.o_quantity;
                        orderItem.o_cover_item = jsonObject4.getString("COVER");
                        orderItem.o_grp_code = jsonObject4.getString("Grp");
                        orderItem.o_disc = jsonObject4.getString("Disc");
                        orderItem.orderNo = jsonObject4.getString("Order");

                        homeItem.itemList.add(orderItem);

                    } else
                        break;
                }

                prvsPosition = j;
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

