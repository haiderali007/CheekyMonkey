package com.entrada.cheekyMonkey.dynamic;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.entrada.cheekyMonkey.network.BaseNetwork;

/**
 * Created by Rahul on 05/06/2015.
 */
public class FetchCategoryWisePriceTask extends AsyncTask<String, DynamicItem, DynamicItem> {

    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;
    private String response = "";



    public FetchCategoryWisePriceTask(Context context, String parameter, String serverIP, ProgressBar mDialog) {

        this.context = context;
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
    protected DynamicItem doInBackground(String... params) {

        response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.FETCH_DYNAMIC_ITEM_RATE, parameter);

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("Dynamic_Itm_RateResult");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);

            DynamicItem dynamicItem = new DynamicItem();
            dynamicItem.Current_Rate  = jsonObject1.getString("Current_Rate");
            dynamicItem.Inc_Qty  = jsonObject1.getString("Inc_Qty");
            dynamicItem.Inc_Rate  = jsonObject1.getString("Inc_Rate");
            dynamicItem.Item_Desc = jsonObject1.getString("Item_Name");
            dynamicItem.Max_Price  = jsonObject1.getString("Max_Price");
            dynamicItem.Min_Price  = jsonObject1.getString("Min_Price");
            dynamicItem.Previous_Rate  = jsonObject1.getString("Previous_Rate");
            dynamicItem.Sold_Qty  = jsonObject1.getString("Sold_Qty");
            dynamicItem.Today_Max  = jsonObject1.getString("Today_Max");

            return dynamicItem ;

        }catch (JSONException e){e.printStackTrace();}

        return null;

    }


    @Override
    protected void onPostExecute(DynamicItem result) {
        super.onPostExecute(result);
        if (mDialog != null)
            mDialog.setVisibility(View.GONE);

    }
}