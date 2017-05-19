package com.entrada.cheekyMonkey.dynamic.syncData;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.dynamic.BaseFragmentActivity;
import com.entrada.cheekyMonkey.dynamic.Network_Info.NetworkUtil;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.staticData.PrefHelper;
import com.entrada.cheekyMonkey.steward.AsyncData2Task;
import com.entrada.cheekyMonkey.steward.AsyncData3Task;
import com.entrada.cheekyMonkey.steward.AsyncPosTask;
import com.entrada.cheekyMonkey.util.AsyncTaskTools;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

/**
 * Created by ACER on 12/08/2016.
 */
public class SyncFragment extends Fragment implements ICallResponse{

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(context).inflate(R.layout.sync_fragment_layout,container,false);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_sync);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);

        //getDistanceOffset(28.4665433, 77.0641075, 0.20);      // passed 200 mtr radius in km. (Manhattan)
        //getDistanceOffset(28.6020370, 77.3545650, 0.10);      // passed 100 mtr radius in km. (Csat)

        getMenuItems();
        syncPOS();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void syncPOS(){

        String parameter = UtilToCreateJSON.createPOSJSON(context);
        String serverIP = POSApplication.getSingleton().getmDataModel()
                .getUserInfo().getServerIP();

        AsyncPosTask posTask = new AsyncPosTask(context, serverIP, parameter);
        AsyncTaskTools.execute(posTask);
        AsyncData2Task data2Task = new AsyncData2Task(context, serverIP, parameter);
        data2Task.execute();
        AsyncData3Task data3Task = new AsyncData3Task(context, serverIP, parameter);
        data3Task.execute();
    }

    public void getMenuItems(){

        if (NetworkUtil.getConnectivityStatus(context) != 0) {

            String parameter = UtilToCreateJSON.createToken();
            String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
            FetchItemsWithTableTask storedMenuItemsTask = new FetchItemsWithTableTask(
                    context,parameter,serverIP, null, this );
            storedMenuItemsTask.execute();

        } else
            UserInfo.showNetFailureDialog(context);

    }

    @Override
    public void getResponseFromServer(String response) {

        if (response.equals("success")){
            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.FIRST_TIME_LUNCHAPP,
                    PrefHelper.FIRST_TIME_LUNCHAPP_DONE);
            Intent intent = new Intent(context, BaseFragmentActivity.class);
            if (getActivity() != null)
            getActivity().finish();
            startActivity(intent);

        } else
            UserInfo.showNetFailureDialog(context);
    }


    public void getDistanceOffset(double latitude, double longitude, double radiusInKm){

        double kmInLongitudeDegree = 111.320 * Math.cos( latitude / 180.0 * Math.PI);

        //Combining this, it's easy to get deltas of latitude and longitude that will cover your circle:

        double deltaLat = radiusInKm / 111.1;
        double deltaLong = radiusInKm / kmInLongitudeDegree;

        double minLat = latitude - deltaLat;
        double maxLat = latitude + deltaLat;
        double minLong = longitude - deltaLong;
        double maxLong = longitude + deltaLong;

        PrefHelper.storeDouble(context, PrefHelper.PREF_FILE_NAME, PrefHelper.MIN_LATITUDE, minLat);
        PrefHelper.storeDouble(context, PrefHelper.PREF_FILE_NAME, PrefHelper.MAX_LATITUDE, maxLat);
        PrefHelper.storeDouble(context, PrefHelper.PREF_FILE_NAME, PrefHelper.MIN_LONGITUDE, minLong);
        PrefHelper.storeDouble(context, PrefHelper.PREF_FILE_NAME, PrefHelper.MAX_LONGITUDE, maxLong);

    }

}