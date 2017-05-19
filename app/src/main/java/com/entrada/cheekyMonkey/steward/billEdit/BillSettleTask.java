package com.entrada.cheekyMonkey.steward.billEdit;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.entrada.cheekyMonkey.network.BaseNetwork;


/**
 * Created by CSAT on 26/10/2015.
 */


public class BillSettleTask extends AsyncTask<String, String, String> {

    ProgressDialog progressDialog;
    ICallSettleResponse iCallSettleResponse;
    private String parameter, serverIP;

    public BillSettleTask(Context context, String parameter, String serverIP, ICallSettleResponse iCallSettleResponse) {

        this.parameter = parameter;
        this.serverIP = serverIP;
        progressDialog = new ProgressDialog(context);
        this.iCallSettleResponse = iCallSettleResponse;
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

        return BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_BILL_SETTLE, parameter);
    }


    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        iCallSettleResponse.getBillSettleResponse(response);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}

