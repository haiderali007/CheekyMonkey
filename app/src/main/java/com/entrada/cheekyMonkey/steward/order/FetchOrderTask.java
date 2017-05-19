package com.entrada.cheekyMonkey.steward.order;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.entrada.cheekyMonkey.steward.discount.DiscountLayout;
import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.takeorder.adapter.TakeOrderAdapter;
import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FetchOrderTask extends AsyncTask<String, OrderItem, OrderItem> {

    private TakeOrderAdapter adapter;
    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;
    private String response = "";
    private DiscountLayout discountLayout;
    private ArrayList<String> codeList;


    public FetchOrderTask(Context context, TakeOrderAdapter adapter, DiscountLayout discountLayout,
                          ArrayList<String> codeList, String parameter, String serverIP) {

        this.context = context;
        this.adapter = adapter;
        this.discountLayout = discountLayout;
        this.codeList = codeList;
        this.parameter = parameter;
        this.serverIP = serverIP;
        mDialog = new ProgressBar(context);
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
            JSONObject jsonParam ;
            JSONArray jsonArray ;

            Logger.d("OrderNumberResponse", "::" + response);

            JSONObject obj = new JSONObject(response);
            jsonArray = new JSONArray(
                    obj.getString("ECABS_PullOrderForModifyResult"));

            OrderItem obj_order ;
            for (int i = 0; i < jsonArray.length(); i++) {

                jsonParam = jsonArray.getJSONObject(i);

                obj_order = new OrderItem();

               /* steward = jsonParam.getString("stw");
                ORemark = jsonParam.getString("Orem");
                cvr = jsonParam.getString("cover");
                GstName = jsonParam.getString("guest");
                odtype = jsonParam.getString("rkot_Typ");*/

                obj_order.o_code = jsonParam.getString("Code");
                obj_order.o_grp_code = jsonParam.getString("Grp");

                if (jsonParam.getString("zcomboItem").equals("Y") || jsonParam.getString("zcomboItem").equals(""))
                    obj_order.o_name = jsonParam.getString("Name");
                else
                    obj_order.o_name = "##" + jsonParam.getString("Name");


                obj_order.o_itm_rmrk = jsonParam.getString("remark");
                obj_order.o_addon_code = jsonParam.getString("addon");
                obj_order.o_mod = jsonParam.getString("modifier");
                obj_order.o_combo_code = jsonParam.getString("zcomboItem");
                obj_order.o_happy_hour = jsonParam.getString("Name").equals("Free") ? "Y" : "";
                obj_order.o_subunit = "";
                obj_order.o_quantity = Float.parseFloat(jsonParam.getString("qty"));
                obj_order.o_price = Float.parseFloat(jsonParam.getString("dat"));
                obj_order.o_amount = obj_order.o_quantity * obj_order.o_price;
                obj_order.o_disc = jsonParam.getString("GUEST_DISC");

                discountLayout.createDiscList("",obj_order.o_disc,obj_order.o_amount);

                if (!codeList.contains(obj_order.o_code))
                    codeList.add(obj_order.o_code);

                publishProgress(obj_order);

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(OrderItem... values) {
        super.onProgressUpdate(values);
        adapter.addDataSetItem(values[0]);
    }

    @Override
    protected void onPostExecute(OrderItem result) {
        super.onPostExecute(result);
        if (mDialog != null)
            mDialog.setVisibility(View.GONE);

    }

}
