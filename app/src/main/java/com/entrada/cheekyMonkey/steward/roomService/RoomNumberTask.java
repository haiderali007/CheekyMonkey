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
 * Created by CSATSPL on 22/12/2015.
 */
public class RoomNumberTask extends AsyncTask<String, RoomItem, RoomItem> {

    private RoomServiceAdapter roomServiceAdapter;
    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;


    public RoomNumberTask(Context context, RoomServiceAdapter roomServiceAdapter, String parameter, String serverIP,
                          ProgressBar mDialog) {

        this.context = context;
        this.roomServiceAdapter = roomServiceAdapter;
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

        String response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_FETCH_ROOM, parameter);

        try {
            Logger.d("Room_Status", "::" + response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("Room_statusResult"));
            RoomItem obj_list = new RoomItem();
            String prvs_room_code = "";

            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObject = jsonArray.getJSONObject(i);

                if (prvs_room_code.equals(jsonObject.getString("Room_Code")))
                    obj_list.Status = jsonObject.getString("Status");

                else {

                    obj_list = new RoomItem();
                    obj_list.GMember_Id = jsonObject.getString("GMemb_Id");
                    obj_list.Room_code = jsonObject.getString("Room_Code");
                    obj_list.Room_name = jsonObject.getString("Room_Code");
                    obj_list.GuestId = jsonObject.getString("GuestID");
                    obj_list.GuestName = jsonObject.getString("GuestName");
                    obj_list.RMSC_Row = jsonObject.getString("RMSC_Row");
                    obj_list.RMSC_Column = jsonObject.getString("RMSC_Column");
                    obj_list.Status = jsonObject.getString("Status");
                    prvs_room_code = obj_list.Room_code;

                    publishProgress(obj_list);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(RoomItem... values) {
        super.onProgressUpdate(values);
        roomServiceAdapter.addDataSetItem(values[0]);
    }

    @Override
    protected void onPostExecute(RoomItem result) {
        super.onPostExecute(result);
        Logger.i("result", "" + result);
        if (mDialog != null)
            mDialog.setVisibility(View.GONE);
    }
}
