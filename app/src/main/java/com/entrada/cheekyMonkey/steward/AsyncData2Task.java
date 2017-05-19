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
public class AsyncData2Task extends AsyncTask<String, Object, Object> implements
        DBConstants {

    private Context context;
    private String parameter;
    private String serverIP = "";
    private String response = "";


    public AsyncData2Task(Context context, String serverIP, String parameter) {
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

		/*
         * Logger.i("serverIP", "::" + serverIP); Logger.i("parameter", "::" +
		 * parameter);
		 */
        response = BaseNetwork.obj().postMethodWay(serverIP, "",
                BaseNetwork.DATA_SYNC2, parameter);

        if (!TextUtils.isEmpty(response)) {

            try {

                JSONObject jsonHeader = new JSONObject(response);
                JSONObject jsonItem = new JSONObject(
                        jsonHeader
                                .getString(StaticConstants.JSON_TAG_DATA_SYNC2_RESULT));

                JSONArray jsonAddonArray = new JSONArray(
                        jsonItem.getString(StaticConstants.JSON_TAG_ADD_ONS));
                int addonLength = jsonAddonArray.length();
                Logger.i("addonLength", "::" + addonLength);

                JSONArray jsonModArray = new JSONArray(
                        jsonItem.getString(StaticConstants.JSON_TAG_MODIFIER));
                int modLength = jsonModArray.length();
                Logger.i("modLength", "::" + modLength);

                JSONArray jsonComboArray = new JSONArray(
                        jsonItem.getString(StaticConstants.JSON_TAG_DS2_COMBO));
                int comboLength = jsonComboArray.length();
                Logger.i("comboLength", "::" + comboLength);

                JSONArray jsonCurrencyArray = new JSONArray(
                        jsonItem.getString(StaticConstants.JSON_TAG_CURRENCY_TAG));
                int currencyLength = jsonCurrencyArray.length();
                Logger.i("currencyLength", "::" + currencyLength);

                SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                        .getWritableDatabase();
                mdb.beginTransaction();
                try {

                    mdb.delete(KEY_ADDONS_TABLE,null,null);
                    mdb.delete(KEY_COMBO_TABLE,null,null);
                    mdb.delete(KEY_MODIFIER_TABLE,null,null);

                    for (int g = 0; g < addonLength; g++) {

                        JSONObject jsonObject = jsonAddonArray.getJSONObject(g);

                        ContentValues cv = new ContentValues();
                        // cv.put(KEY_ADDON_POS_CODE,jsonObject.getString(StaticConstants.JSON_TAG_POS));
                        cv.put(KEY_ADDONS_CODE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_ADD_ONS_CODE));
                        cv.put(KEY_ADDONS_COLOR,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_ADD_ONS_COLOR));
                        cv.put(KEY_ADDONS_ITEM_CODE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_ADD_ONS_ITEM_CODE));
                        cv.put(KEY_ADDONS_NAME,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_ADD_ONS_NAME));
                        cv.put(KEY_ADDONS_PRICE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_ADD_ONS_PRICE));
                        mdb.insert(KEY_ADDONS_TABLE, null, cv);

                    }
                    mdb.setTransactionSuccessful();

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    mdb.endTransaction();
                }
                mdb.beginTransaction();
                try {
                    for (int g = 0; g < modLength; g++) {

                        JSONObject jsonObject = jsonModArray.getJSONObject(g);
                        ContentValues cv = new ContentValues();
                        cv.put(KEY_MODIFIER_CODE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_MODIFIER_CODE));
                        cv.put(KEY_MODIFIER_COLOR,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_MODIFIER_COLOR));
                        cv.put(KEY_MODIFIER_ITEM_CODE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_MODIFIER_ITEM_CODE));
                        cv.put(KEY_MODIFIER_NAME,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_MODIFIER_NAME));
                        mdb.insert(KEY_MODIFIER_TABLE, null, cv);
                    }
                    mdb.setTransactionSuccessful();

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {

                    mdb.endTransaction();
                }

                mdb.beginTransaction();
                try {

                    for (int g = 0; g < comboLength; g++) {


                        JSONObject jsonObject = jsonComboArray.getJSONObject(g);

                        ContentValues cv = new ContentValues();
                        cv.put(KEY_COMBO_CAT_CODE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS2_COMBO_CAT_CODE));
                        cv.put(KEY_COMBO_CAT_NAME,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS2_COMBO_CAT_NAME));
                        cv.put(KEY_COMBO_CODE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS2_COMBO_COMBO_CODE));
                        cv.put(KEY_COMBO_FLAG,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS2_COMBO_COMBO_FLAG));
                        cv.put(KEY_COMBO_ITEM_CODE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS2_COMBO_ITEM_CODE));
                        cv.put(KEY_COMBO_NAME,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS2_COMBO_ITEM_NAME));
                        cv.put(KEY_COMBO_MAX_QTY,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS2_COMBO_MAX_QTY));
                        cv.put(KEY_COMBO_QTY,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_DS2_COMBO_QTY));
                        mdb.insert(KEY_COMBO_TABLE, null, cv);
                    }
                    mdb.setTransactionSuccessful();

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {

                    mdb.endTransaction();
                }

                mdb.beginTransaction();
                try {

                    for (int g = 0; g < currencyLength; g++) {


                        JSONObject jsonObject = jsonCurrencyArray.getJSONObject(g);

                        ContentValues cv = new ContentValues();
                        cv.put(KEY_CURRENCY_SR,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_CURRENCY_SR));
                        cv.put(KEY_CURRENCY_LABEL,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_CURRENCY_LABEL));
                        cv.put(KEY_CURRENCY_VALUE,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_CURRENCY_VALUE));
                        cv.put(KEY_CURRENCY_FLAG,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_CURRENCY_FLAG));
                        cv.put(KEY_CURRENCY_PIC,
                                jsonObject
                                        .getString(StaticConstants.JSON_TAG_CURRENCY_PIC));
                        mdb.insert(KEY_CURRENCY_TABLE, null, cv);
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

}
