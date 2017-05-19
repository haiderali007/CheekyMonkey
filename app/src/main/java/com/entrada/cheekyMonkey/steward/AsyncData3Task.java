package com.entrada.cheekyMonkey.steward;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by Tanuj.Sareen on 1/26/2015.
 */
public class AsyncData3Task extends AsyncTask<String, Object, Object> implements
        DBConstants {

    private Context context;
    private String parameter;
    private String serverIP = "";
    private String response = "";

    public AsyncData3Task(Context context, String serverIP, String parameter) {
        this.context = context;
        this.parameter = parameter;
        this.serverIP = serverIP;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(String... params) {

	/*	Logger.i("serverIP", "::" + serverIP);
        Logger.i("parameter", "::" + parameter);*/
        response = BaseNetwork.obj().postMethodWay(serverIP, "",
                BaseNetwork.DATA_SYNC3, parameter);

        if (!TextUtils.isEmpty(response)) {

            try {
                Logger.i("sync3Result", "::" + response);

                JSONObject jsonHeader = new JSONObject(response);
                JSONObject jsonItem = new JSONObject(
                        jsonHeader.getString(StaticConstants.JSON_TAG_DATA_SYNC3_RESULT));

                JSONArray jsonDiscDwArray = new JSONArray(
                        jsonItem.getString(StaticConstants.JSON_TAG_DS3_DISC_DW));
                int discDWLength = jsonDiscDwArray.length();
                Logger.i("discDWLength", "::" + discDWLength);

                JSONArray jsonDiscITEMArray = new JSONArray(
                        jsonItem.getString(StaticConstants.JSON_TAG_DS3_DISC_ITEM));
                int discITEMLength = jsonDiscITEMArray.length();
                Logger.i("discITEMLength", "::" + discITEMLength);

                JSONArray jsonDiscMSTArray = new JSONArray(
                        jsonItem.getString(StaticConstants.JSON_TAG_DS3_DISC_MASTER));
                int discMSTLength = jsonDiscMSTArray.length();
                Logger.i("discMSTLength", "::" + discMSTLength);


                SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                        .getWritableDatabase();
                mdb.beginTransaction();
                try {

                    mdb.delete(KEY_DISC_DOW_TABLE,null, null);
                    mdb.delete(KEY_DISC_ITEM_TABLE,null, null);
                    mdb.delete(KEY_DISC_MST_TABLE ,null, null);

                    for (int g = 0; g < discDWLength; g++) {

                        JSONObject jsonObject = jsonDiscDwArray
                                .getJSONObject(g);

                        ContentValues cv = new ContentValues();
                        cv.put(KEY_DISC_DOW_DISC_CODE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_CODE));
                        cv.put(KEY_DISC_DOW_DAY,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_DAY));
                        cv.put(KEY_DISC_DOW_POS_CODE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_POS));
                        mdb.insert(KEY_DISC_DOW_TABLE, null, cv);

                    }
                    mdb.setTransactionSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    mdb.endTransaction();
                }
                mdb.beginTransaction();
                try {

                    for (int g = 0; g < discITEMLength; g++) {

                        JSONObject jsonObject = jsonDiscITEMArray
                                .getJSONObject(g);
                        ContentValues cv = new ContentValues();
                        cv.put(KEY_DISC_ITEM_DISC_CODE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_ITEM_CODE));
                        cv.put(KEY_DISC_ITEM_POS_CODE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_ITEM_POS));
                        cv.put(KEY_DISC_ITEM_ITEM_CODE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_ITEM_ITEMCODE));
                        cv.put(KEY_DISC_ITEM_DISC_PER,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_ITEM_PER));
                        cv.put(KEY_DISC_ITEM_BILLED_QTY,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_ITEM_BQTY));
                        cv.put(KEY_DISC_ITEM_FREE_QTY,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_ITEM_FQTY));
                        cv.put(KEY_DISC_FREE_ITEM_CODE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_ITEM_FREEITEM));
                        cv.put(KEY_DISC_HAPPY_RATE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_HPY_RATE));

                        mdb.insert(KEY_DISC_ITEM_TABLE, null, cv);
                    }
                    mdb.setTransactionSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    mdb.endTransaction();
                }

                mdb.beginTransaction();
                try {

                    for (int g = 0; g < discMSTLength; g++) {

                        JSONObject jsonObject = jsonDiscMSTArray
                                .getJSONObject(g);
                        ContentValues cv = new ContentValues();
                        cv.put(KEY_DISC_MST_POS_CODE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_MASTER_POS_CODE));
                        cv.put(KEY_DISC_MST_DISC_CODE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_MASTER_DISC_CODE));
                        cv.put(KEY_DISC_MST_FROM_TIME,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_MASTER_FROM_TIME));
                        cv.put(KEY_DISC_MST_TO_TIME,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_MASTER_TO_TIME));
                        cv.put(KEY_DISC_MST_FROM_DATE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_MASTER_FROM_DATE));
                        cv.put(KEY_DISC_MST_TO_DATE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_MASTER_TO_DATE));
                        cv.put(KEY_DISC_MST_DISC_ACTIVE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_MASTER_DISC_ACTIVE));
                        cv.put(KEY_DISC_MST_DISC_FLAG,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS3_DISC_MASTER_FLAG));
                        cv.put(KEY_DISC_MST_DISC_DESC,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DISC_MST_DISC_DESC));
                        cv.put(KEY_DISC_MST_DISC_FL,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DISC_MST_DISC_FL));
                        cv.put(KEY_DISC_MST_DISC_TL,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DISC_MST_DISC_TL));
                        cv.put(KEY_DISC_MST_DISC_FLAG,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DISC_MST_DISC_FLAG));
                        cv.put(KEY_DISC_MST_DISC_OPN,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DISC_MST_DISC_OPN));
                        cv.put(KEY_DISC_MST_DISC_FLAT_FLAG,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DISC_MST_DISC_FLAT_FLAG));
                        cv.put(KEY_DISC_MST_DISC_OGI,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DISC_MST_DISC_OGI));


                        mdb.insert(KEY_DISC_MST_TABLE, null, cv);
                    }
                    mdb.setTransactionSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    mdb.endTransaction();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }

        return "";

    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Logger.i(Logger.LOGGER_TAG, Logger.LOGGER_OP + o.toString());
        //iCallBackPOS.completeSync(BaseNetwork.DATA_SYNC3, true);
    }
}
