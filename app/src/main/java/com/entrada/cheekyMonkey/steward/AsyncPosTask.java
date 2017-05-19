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

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by ${Tanuj.Sareen} on 14/02/2015.
 */
public class AsyncPosTask extends AsyncTask<String, Integer, Boolean> {

    private Context context;
    private String parameter;
    private String response = "";
    private String serverIP = "";

    public AsyncPosTask(Context context, String serverIP, String parameter) {
        this.context = context;
        this.parameter = parameter;
        this.serverIP = serverIP;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Boolean doInBackground(String... params) {

        boolean isSuccess = false;
        response = BaseNetwork.obj().postMethodWay(serverIP, "",
                BaseNetwork.ECABS_PullOutletPos, parameter);

        if (!TextUtils.isEmpty(response)) {

            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = new JSONArray(
                        jsonObject
                                .getString(StaticConstants.JSON_TAG_POS_RESULT));

                int length = jsonArray.length();
                if (length > 0) {

                    SQLiteDatabase mdb = POSDatabase.getInstanceLogin(
                            context).getWritableDatabase();
                    mdb.beginTransaction();
                    try {

                        for (int i = 0; i < length; i++) {
                            JSONObject jsonValue = jsonArray.getJSONObject(i);

                            ContentValues cv = new ContentValues();
                            cv.put(DBConstants.KEY_OUTLET_POS_CODE, jsonValue.getString(StaticConstants.JSON_TAG_POS_CODE));
                            cv.put(DBConstants.KEY_OUTLET_POS_NAME, jsonValue.getString(StaticConstants.JSON_TAG_POS_NAME));
                            cv.put(DBConstants.KEY_OUTLET_POS_TYPE, jsonValue.getString(StaticConstants.JSON_TAG_POS_TYPE));
                            cv.put(DBConstants.KEY_OUTLET_POS_HEADER, jsonValue.getString(StaticConstants.JSON_TAG_POS_HEADER));
                            cv.put(DBConstants.KEY_OUTLET_POS_FOOTER, jsonValue.getString(StaticConstants.JSON_TAG_POS_FOOTER));


                            mdb.insert(DBConstants.KEY_OUTLET_POS_TABLE, null, cv);
                            publishProgress(i + 1);

                        }
                        mdb.setTransactionSuccessful();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        mdb.endTransaction();
                    }
                    isSuccess = true;
                }

            } catch (Exception ex) {
                ex.printStackTrace();

            }

        }

        return isSuccess;

    }

}