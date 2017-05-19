package com.entrada.cheekyMonkey.steward.home_del;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;


/**
 * Created by CSAT on 26/10/2015.
 */


public class DelLogTask extends AsyncTask<String, String, String> {

    ICallDelDetail iCallDelDetail;
    ProgressDialog progressDialog;
    private Context context;
    private String parameter, serverIP;

    public DelLogTask(Context context, String parameter, String serverIP, ICallDelDetail iCallDelDetail) {
        this.context = context;
        this.parameter = parameter;
        this.serverIP = serverIP;
        this.iCallDelDetail = iCallDelDetail;
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
        return BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_HOME_DELIVERY_LOG, parameter);
    }


    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        iCallDelDetail.getSaveResponse(response);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}

