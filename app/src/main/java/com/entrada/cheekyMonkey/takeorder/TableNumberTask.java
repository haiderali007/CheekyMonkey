package com.entrada.cheekyMonkey.takeorder;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.takeorder.adapter.TableAdapter;
import com.entrada.cheekyMonkey.takeorder.entity.TableItem;
import com.entrada.cheekyMonkey.util.Logger;


public class TableNumberTask extends AsyncTask<String, TableItem, TableItem> {

    private TableAdapter adapter;
    private ProgressBar mDialog;
    private String parameter;
    private String serverIP;


    public TableNumberTask(Context context, TableAdapter adapter, String parameter, String serverIP,
                           ProgressBar mDialog) {

        this.adapter = adapter;
        this.mDialog = mDialog;
        this.parameter = parameter;
        this.serverIP = serverIP;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mDialog != null)
            mDialog.setVisibility(View.VISIBLE);
    }

    @Override
    protected TableItem doInBackground(String... params) {

        String response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.FETCH_TABLE_DYNAMIC, parameter);

        try {

            Logger.d("TableStatusItem", "::" + response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("ECABS_TableStatus_DynamicResult"));
            TableItem obj_list;

            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObject = jsonArray.getJSONObject(i);
                obj_list = new TableItem();
                obj_list.code = jsonObject.getString("Code");
                obj_list.name = jsonObject.getString("Desc");
                obj_list.status = jsonObject.getString("Status");
                obj_list.cvr = jsonObject.getString("CVR");
                obj_list.stwrd = jsonObject.getString("STWD");
                obj_list.gst_name = jsonObject.getString("GUEST_name");
                obj_list.gst_code = jsonObject.getString("GUEST_code");
               /* obj_list.tbl_font = jsonObject.getDouble("TBL_FNT");
                obj_list.tbl_h = jsonObject.getDouble("TBL_H");
                obj_list.tbl_w = jsonObject.getDouble("TBL_W");*/
                obj_list.odr_type = jsonObject.getString("odtype");
                obj_list.no_pax = jsonObject.getString("NO_PAX").equals("") ? 0
                        : Integer.parseInt(jsonObject.getString("NO_PAX"));
                // obj_list.no_pax=Integer.parseInt(jsonParam.getString("NO_PAX"));

               /* if (obj_list.tbl_h > 90 || obj_list.tbl_w > 90) {

                    column = obj_list.tbl_h > 90 ? (int) obj_list.tbl_h
                            : (int) obj_list.tbl_w;

                }*/
                publishProgress(obj_list);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(TableItem... values) {
        super.onProgressUpdate(values);
        adapter.addDataSetItem(values[0]);
    }

    @Override
    protected void onPostExecute(TableItem result) {
        super.onPostExecute(result);
        if (mDialog != null)
            mDialog.setVisibility(View.GONE);
    }
}
