package com.entrada.cheekyMonkey.dynamic.syncData;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.util.Logger;

/**
 * Created by Rahul on 05/06/2015.
 */
public class FetchAndStoreMenuItemsTask extends AsyncTask<Void,  Void,  String>
        implements DBConstants {

    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;
    ICallResponse iCallResponse ;

    public FetchAndStoreMenuItemsTask(Context context, String parameter, String serverIP,
                                      ProgressBar mDialog, ICallResponse iCallResponse) {

        this.context = context;
        this.mDialog = mDialog;
        this.parameter = parameter;
        this.serverIP = serverIP;
        this.iCallResponse = iCallResponse;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (! (mDialog == null || ((Activity) context).isFinishing()))
            mDialog.setVisibility(View.VISIBLE);

    }


    @Override
    protected String doInBackground(Void... avoid) {

        String response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.FETCH_DYNAMIC_ITEM, parameter);
        Logger.i("Dynamic_Itm_Rate_All_List", response);

        if (response.equals("{\"Dynamic_Itm_Rate_AllResult\":[]}"))
            return response;

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();

        try {
            mdb.beginTransaction();

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("Dynamic_Itm_Rate_AllResult");

            if (jsonArray.length() == 0)
                return "";

            mdb.delete(ITEMS_DETAIL_TABLE,null,null);
            mdb.delete(KEY_MENU_ITEM_FTS_TABLE,null,null);

            for (int i=0; i<jsonArray.length(); i++){

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                ContentValues cv = new ContentValues();
                cv.put(Current_Rate, jsonObject1.getString(StaticConstants.JSON_TAG_Current_Rate));
                cv.put(Inc_Qty,  jsonObject1.getString(StaticConstants.JSON_TAG_Inc_Qty));
                cv.put(Inc_Rate, jsonObject1.getString(StaticConstants.JSON_TAG_Inc_Rate));
                cv.put(Item_Desc, jsonObject1.getString(StaticConstants.JSON_TAG_Item_Desc));
                cv.put(Max_Price, jsonObject1.getString(StaticConstants.JSON_TAG_Max_Price));
                cv.put(Min_Price, jsonObject1.getString(StaticConstants.JSON_TAG_Min_Price));
                cv.put(Previous_Rate, jsonObject1.getString(StaticConstants.JSON_TAG_Previous_Rate));
                cv.put(Sold_Qty, jsonObject1.getString(StaticConstants.JSON_TAG_Sold_Qty));
                cv.put(Today_Max,jsonObject1.getString(StaticConstants.JSON_TAG_Today_Max));
                cv.put(Cat_Code, jsonObject1.getString(StaticConstants.JSON_TAG_Cat_Code));
                cv.put(Cat_Desc, jsonObject1.getString(StaticConstants.JSON_TAG_Cat_Desc));
                cv.put(Item_Code, jsonObject1.getString(StaticConstants.JSON_TAG_Item_Code));

                mdb.insert(ITEMS_DETAIL_TABLE, null, cv);


                ContentValues cv1 = new ContentValues();
                cv1.put(DBConstants.KEY_MENU_CODE,jsonObject1.getString(StaticConstants.JSON_TAG_Item_Code));
                cv1.put(DBConstants.KEY_MENU_NAME,jsonObject1.getString(StaticConstants.JSON_TAG_Item_Desc));
                cv1.put(DBConstants.KEY_MENU_PRICE, jsonObject1.getString(StaticConstants.JSON_TAG_Current_Rate));

                mdb.insert(DBConstants.KEY_MENU_ITEM_FTS_TABLE, null, cv1);
            }

            mdb.setTransactionSuccessful();
            response = "success1";

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            mdb.endTransaction();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (iCallResponse != null)
            iCallResponse.getResponseFromServer(result);

        if (! result.equals("success1")){
            UserInfo.showNetFailureDialog(context);
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }
    }
}