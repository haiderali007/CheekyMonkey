package com.entrada.cheekyMonkey.dynamic.Network_Info;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.entrada.cheekyMonkey.network.BaseNetwork;

/**
 * Created by Rahul on 05/06/2015.
 */
public class ActivateUserIDTask extends AsyncTask<String, String, String> {

    private Context context;
    private String parameter;
    private String serverIP;

    public ActivateUserIDTask(Context context, String parameter, String serverIP) {

        this.context = context;
        this.parameter = parameter;
        this.serverIP = serverIP;

    }

    @Override
    protected String doInBackground(String... params) {

        String response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.PUSH_USER_STATUS, parameter);

        try {

            JSONObject jsonObject = new JSONObject(response);
            response = jsonObject.getString("ECABS_Update_User_StatusResult");

        }catch (JSONException e){
            e.printStackTrace();
        }

        return response;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        //Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
}