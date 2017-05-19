package com.entrada.cheekyMonkey.steward.home_del;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;


/**
 * Created by CSAT on 26/10/2015.
 */


public class CrewMemberTask extends AsyncTask<String, String, String> {

    ICallHome iCallHome;
    ProgressDialog progressDialog;
    private String parameter, serverIP;

    public CrewMemberTask(Context context, String parameter, String serverIP, ICallHome iCallHome) {

        this.parameter = parameter;
        this.serverIP = serverIP;
        this.iCallHome = iCallHome;
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
        return BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_DEL_BOY_IN, parameter);

    }


    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        iCallHome.homeDetail(null, response);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}

