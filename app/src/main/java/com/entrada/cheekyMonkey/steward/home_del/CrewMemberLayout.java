package com.entrada.cheekyMonkey.steward.home_del;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.steward.StewardOrderFragment;
import com.entrada.cheekyMonkey.util.Logger;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by csat on 20/10/2015.
 */
public class CrewMemberLayout implements View.OnClickListener, ICallHome {

    View view = null;
    LayoutInflater layoutInflater;

    Context context;
    ICallHome iCallHome;
    ListView lv_crew;
    TextView et_crew_code, et_crew_name, tv_arvl, tv_dept;
    Button btn_in, btn_close;
    ArrayList<String> crewList;
    ArrayList<String> crewName;
    ArrayAdapter<String> arrayAdapter;
    Bundle bundle;

    public CrewMemberLayout(Context context, ICallHome iCallHome) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.iCallHome = iCallHome;
    }


    public View getCrewPopupLayout() {

        view = layoutInflater.inflate(R.layout.crew_member_layout, null);

        et_crew_code = (TextView) view.findViewById(R.id.et_del_code);
        et_crew_name = (TextView) view.findViewById(R.id.et_del_name);
        tv_arvl = (TextView) view.findViewById(R.id.tv_arvl);
        tv_dept = (TextView) view.findViewById(R.id.tv_dept);
        btn_in = (Button) view.findViewById(R.id.btn_in);
        btn_close = (Button) view.findViewById(R.id.btn_close);

        btn_in.setOnClickListener(this);
        btn_close.setOnClickListener(this);

        crewList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, crewList);
        lv_crew = (ListView) view.findViewById(R.id.lv_for_crew_member);
        lv_crew.setAdapter(arrayAdapter);

        bundle = new Bundle();
        init();

        lv_crew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                et_crew_code.setText(crewList.get(position));
                et_crew_name.setText(crewName.get(position));
                tv_arvl.setText(bundle.getString(String.valueOf(position)));

                setButtonStatus(bundle.getString(et_crew_code.getText().toString()));

            }
        });

        getAllCrewsStatus();
        return view;
    }

    public void setButtonStatus(String status) {

        if (status == null)
            return;

        if (status.equals("I")) {

            btn_in.setText("OUT");
            btn_in.setBackgroundColor(Color.RED);
            tv_dept.setText(UserInfo.getCurrentTime());

        } else {
            btn_in.setText("IN");
            btn_in.setBackgroundColor(Color.GREEN);
            tv_arvl.setText(UserInfo.getCurrentTime());
            tv_dept.setText("");
        }
    }

    public void getAllCrewsStatus() {

        String parameter = UtilToCreateJSON.createCrewStatusJSON(et_crew_code.getText().toString());
        String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();

        CrewStatusTask crewStatusTask = new CrewStatusTask(context, parameter, serverIP, CrewMemberLayout.this);
        crewStatusTask.execute();
    }

    public void init() {

        crewName = new ArrayList<>();

        SQLiteDatabase mdb = POSDatabase.getMenuItem(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        try {
            Cursor cursor = getAllRecords_STEWARD_TYPE(mdb);
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    crewList.add(cursor.getString(2));
                    crewName.add(cursor.getString(1));

                } while (cursor.moveToNext());

                arrayAdapter.notifyDataSetChanged();

            }
            mdb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }
    }

    public Cursor getAllRecords_STEWARD_TYPE(SQLiteDatabase mdb) {

        return mdb.query(DBConstants.KEY_EMP_DETAIL, new String[]{
                        DBConstants.KEY_EMP_CODE, DBConstants.KEY_EMP_NAME, DBConstants.KEY_EMP_USER_ID},
                DBConstants.KEY_EMP_TYPE + "=" + "'DEL. BOY'", null, null, null, null);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_in:

                String parameter = UtilToCreateJSON.createCrewInJSON(et_crew_code.getText().toString(),
                        UserInfo.getRunningDate());
                String serverIP = POSApplication.getSingleton()
                        .getmDataModel().getUserInfo().getServerIP();

                CrewMemberTask memberTask = new CrewMemberTask(context, parameter, serverIP,
                        CrewMemberLayout.this);
                memberTask.execute();
                break;


            case R.id.btn_close:

                if (iCallHome instanceof StewardOrderFragment) {

                    StewardOrderFragment takeOrderFragment = (StewardOrderFragment) iCallHome;
                    takeOrderFragment.showDefault();
                }

                break;
        }
    }

    @Override
    public void homeDetail(HomeItem homeItem, String response) {

        Logger.i("CrewStatus" + "::", response);

        try {

            JSONObject jsonObject = new JSONObject(response);

            if (response.contains("crewstatusResult")) {
                JSONArray jsonArray = jsonObject.getJSONArray("crewstatusResult");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    bundle.putString(jsonObject1.getString("User_Id"), jsonObject1.getString("Status"));
                    bundle.putString(String.valueOf(i), jsonObject1.getString("Arvl"));
                }
            } else if (response.contains("delinResult")) {

                String status = jsonObject.getString("delinResult");
                bundle.putString(et_crew_code.getText().toString(), status);
                setButtonStatus(status);
                getAllCrewsStatus();
            } else
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}