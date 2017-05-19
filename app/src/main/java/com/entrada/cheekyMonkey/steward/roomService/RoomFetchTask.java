package com.entrada.cheekyMonkey.steward.roomService;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by CSATSPL on 23/12/2015.
 */
public class RoomFetchTask extends AsyncTask<String, RoomItem, RoomItem> {

    private RoomServiceAdapter roomadapter;
    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;
    private String response = "";


    public RoomFetchTask(Context context, RoomServiceAdapter roomadapter, String parameter, String serverIP, ProgressBar mDialog) {

        this.context = context;
        this.roomadapter = roomadapter;
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
    protected RoomItem doInBackground(String... params) {

        response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_FETCH_ROOM, parameter);

        try {

            Logger.d("Room_Status", "::" + response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("Room_statusResult"));
            RoomItem obj_list;
            String prvs_room_code = "";

            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObject = jsonArray.getJSONObject(i);

                if (prvs_room_code.equals(jsonObject.getString("Room_Code")))
                    continue;

                if (jsonObject.getString("Status").equals("R")) {
                    obj_list = new RoomItem();
                    obj_list.Room_code = jsonObject.getString("Room_Code");
                    obj_list.Status = "R";
                    prvs_room_code = obj_list.Room_code;

                    publishProgress(obj_list);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }


    @Override
    protected void onProgressUpdate(RoomItem... values) {
        super.onProgressUpdate(values);
        roomadapter.addDataSetItem(values[0]);

    }

    @Override
    protected void onPostExecute(RoomItem result) {
        super.onPostExecute(result);
        if (mDialog != null)
            mDialog.setVisibility(View.GONE);
        roomadapter.notifyDataSetChanged();


    }

}
