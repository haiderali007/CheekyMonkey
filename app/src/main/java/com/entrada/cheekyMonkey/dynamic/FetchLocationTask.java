package com.entrada.cheekyMonkey.dynamic;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.entrada.cheekyMonkey.dynamic.start.ICallLocation;
import com.entrada.cheekyMonkey.network.BaseNetwork;

/**
 * Created by Rahul on 05/06/2015.
 */
public class FetchLocationTask extends AsyncTask<String, String, String> {

    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;
    ICallLocation iCallLocation;
    double latitude = 0;
    double longitude = 0;


    public FetchLocationTask(Context context, String parameter, String serverIP,
                             ICallLocation iCallLocation,ProgressBar mDialog) {

        this.context = context;
        this.mDialog = mDialog;
        this.parameter = parameter;
        this.serverIP = serverIP;
        this.iCallLocation = iCallLocation;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mDialog != null)
            mDialog.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {

        String response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.FETCH_OUTLET_LATI_LONGI, parameter);

        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("ECABS_Latitude_LongitudeResult");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);

            if (!(jsonObject1.getString("Latitude").isEmpty() ||jsonObject1.getString("Longitude").isEmpty())){

                latitude = jsonObject1.getDouble("Latitude");
                latitude = jsonObject1.getDouble("Longitude");

            }else {

                latitude = 28.4603805;  // lati & longi of Manhattan Bar Exchange, Gurgaon
                longitude = 77.0949395;

                /*latitude = 28.6020370;  //lati & longi of Csat Systems, Noida
                longitude = 77.3545650;*/
            }

        }catch (JSONException e){e.printStackTrace();}

        return null;

    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (mDialog != null)
            mDialog.setVisibility(View.GONE);

        iCallLocation.getLatiLongi(latitude, longitude);
    }
}