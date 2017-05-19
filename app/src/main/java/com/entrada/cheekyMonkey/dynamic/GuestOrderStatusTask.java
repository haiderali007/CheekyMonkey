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
public class GuestOrderStatusTask extends AsyncTask<String, String, String> {

    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;
    private String response = "";
    private ICallSendNotification iCallSendNotification;
    private String orderNum = "";


    public GuestOrderStatusTask(Context context,String orderNum, String parameter, String serverIP,
                                ICallSendNotification iCallSendNotification, ProgressBar mDialog) {

        this.context = context;
        this.mDialog = mDialog;
        this.orderNum = orderNum;
        this.parameter = parameter;
        this.serverIP = serverIP;
        this.iCallSendNotification = iCallSendNotification;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mDialog != null)
            mDialog.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {

        String status = "";
        response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.FETCH_GUEST_ORDER_STATUS, parameter);

        if (! response.contains("ECABS_Guest_Order_StatusResult"))
            return "failure";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("ECABS_Guest_Order_StatusResult");
            if (jsonArray.length() > 0){

                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                status = jsonObject1.getString("Status");
            }

            return status;

        }catch (JSONException e){e.printStackTrace();}

        return status;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (mDialog != null)
            mDialog.setVisibility(View.GONE);

        iCallSendNotification.sendNotification(orderNum,result);
    }
}