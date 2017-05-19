package com.entrada.cheekyMonkey.steward.companyDiscount;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;


/**
 * Created by csat on 16/10/2015.
 */
public class SaveCompanyInfoTask extends AsyncTask<String, String, String> {

    ICallBackCompnayDiscResponse iCallBack;
    ProgressDialog progressDialog;
    private Context context;
    private String parameter, serverIP;
    private String response = "";

    public SaveCompanyInfoTask(Context context, String parameter, String serverIP, ICallBackCompnayDiscResponse iCallBack) {
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
        response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_SAVE_COMPNAY_DETAILS, parameter);

        return response;
    }


    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        iCallBack.responseCompSave(response);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
