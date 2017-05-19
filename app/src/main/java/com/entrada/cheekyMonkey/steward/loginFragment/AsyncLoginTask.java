package com.entrada.cheekyMonkey.steward.loginFragment;

import android.content.Context;
import android.os.AsyncTask;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.ui.CustomAsynctaskLoader;

/**
 * Created by Tanuj.Sareen on 17/01/2015.
 */
public class AsyncLoginTask extends AsyncTask<String, Void, String> {

    private Context context;
    private String parameter;
    private String serverIP = "";
    private ICallBackLoginResponse iCallBackLoginResponse;
    private CustomAsynctaskLoader progressDialog;

    public AsyncLoginTask(Context context, String serverIP, String parameter,
                          ICallBackLoginResponse iCallBackLoginResponse) {
        this.context = context;
        this.parameter = parameter;
        this.serverIP = serverIP;
        this.iCallBackLoginResponse = iCallBackLoginResponse;
        progressDialog = new CustomAsynctaskLoader(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.ShowDialog();
    }

    @Override
    protected String doInBackground(String... params) {

        String response =  BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_USER_LOGIN, parameter);
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        iCallBackLoginResponse.getLoginResponse(parameter, response);
        if (progressDialog != null)
            progressDialog.DismissDialog();
    }

}
