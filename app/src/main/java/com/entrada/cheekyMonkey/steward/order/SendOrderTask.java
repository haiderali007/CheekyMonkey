package com.entrada.cheekyMonkey.steward.order;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;


/**
 * Created by ${Tanuj.Sareen} on 07/03/2015.
 */
public class SendOrderTask extends AsyncTask<String, String, String> {

    ICallBackSendOrderResponse iCallBack;
    ProgressDialog progressDialog;
    private Context context;
    private String parameter, serverIP;
    private String response = "";

    public SendOrderTask(Context context, String parameter, String serverIP,
                         ICallBackSendOrderResponse iCallBack) {
        this.context = context;
        this.parameter = parameter;
        this.serverIP = serverIP;
        this.iCallBack = iCallBack;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Processing...");
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
        response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_SEND_ORDER, parameter);

        return response;
    }


    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        iCallBack.responseFromServer(response);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
