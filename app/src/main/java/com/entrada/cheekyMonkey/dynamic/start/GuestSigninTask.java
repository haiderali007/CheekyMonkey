package com.entrada.cheekyMonkey.dynamic.start;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.entrada.cheekyMonkey.task.ICallBackGuestProfileResponse;
import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;

/**
 * Created by csat on 10/10/2015.
 */
public class GuestSigninTask extends AsyncTask<String, String, String> {

    ICallBackGuestProfileResponse iCallBack;
    ProgressBar progressBar;
    private Context context;
    private String parameter, serverIP;
    private String response = "";

    public GuestSigninTask(Context context, String parameter, String serverIP,
                           ProgressBar progressBar,ICallBackGuestProfileResponse iCallBack) {
        this.context = context;
        this.parameter = parameter;
        this.serverIP = serverIP;
        this.iCallBack = iCallBack;
        this.progressBar = progressBar;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected String doInBackground(String... params) {

        Logger.i(serverIP, "::" + serverIP);
        Logger.i(parameter, "::" + parameter);
        response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.ECABS_GUEST_SIGN_IN, parameter);

        return response;
    }


    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        iCallBack.responseGuestSearch(response);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }
}