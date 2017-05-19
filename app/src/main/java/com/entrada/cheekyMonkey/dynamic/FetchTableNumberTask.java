package com.entrada.cheekyMonkey.dynamic;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.dynamic.syncData.ICallResponse;
import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;


public class FetchTableNumberTask extends AsyncTask<String,String, String> implements
        DBConstants{

    Context context;
    ICallResponse iCallResponse;

    public FetchTableNumberTask(Context context, ICallResponse iCallResponse){

        this.context = context;
        this.iCallResponse = iCallResponse;
    }

    @Override
    protected String doInBackground(String... params) {

        String parameter = UtilToCreateJSON.createTablesJSON(context);
        String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
        String response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.FETCH_TABLE_DYNAMIC, parameter);

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();

        try {

            mdb.beginTransaction();

            Logger.d("TableStatusItem", "::" + response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("ECABS_TableStatus_DynamicResult"));


            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObject = jsonArray.getJSONObject(i);
                ContentValues cv = new ContentValues();
                cv.put(KEY_OUTLET_TABLE_CODE, jsonObject.getString("Code"));
                cv.put(KEY_OUTLET_TABLE_NUM,  jsonObject.getString("Desc"));
                mdb.insert(KEY_OUTLET_TABLE, null, cv);
            }

            mdb.setTransactionSuccessful();
            response = "success2";

        } catch (Exception e) {
            e.printStackTrace();
            return response = "";
        }finally {
            mdb.endTransaction();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        iCallResponse.getResponseFromServer(response);
    }
}


