package com.entrada.cheekyMonkey.steward.billSplit;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;


/**
 * Created by csat on 02/09/2015.
 */
public class SendBillSplitTask extends AsyncTask<String, String, String> {

    ICallSplitResponse iCallBack;
    ProgressDialog progressDialog;
    private Context context;
    private String parameter, serverIP;
    private String response = "";

    public SendBillSplitTask(Context context, String parameter, String serverIP, ICallSplitResponse iCallBack) {
        this.context = context;
        this.parameter = parameter;
        this.serverIP = serverIP;
        this.iCallBack = iCallBack;
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
    protected String doInBackground(String... params) {

        Logger.i(serverIP, "::" + serverIP);
        Logger.i(parameter, "::" + parameter);
        response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_ECABS_BILL_SPLIT, parameter);

        return response;
    }


    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        iCallBack.getSplitResponse(response);

        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
