package com.entrada.cheekyMonkey.steward.discount;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.task.ICallBackGuestProfileResponse;
import com.entrada.cheekyMonkey.util.Logger;


/**
 * Created by csat on 10/10/2015.
 */
public class GuestSearchTask extends AsyncTask<String, String, String> {

    ICallBackGuestProfileResponse iCallBack;
    ProgressDialog progressDialog;
    private Context context;
    private String parameter, serverIP;
    private String response = "";

    public GuestSearchTask(Context context, String parameter, String serverIP, ICallBackGuestProfileResponse iCallBack) {
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
        response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_GUEST_SEARCH, parameter);

        return response;
    }


    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        iCallBack.responseGuestSearch(response);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}