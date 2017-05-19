package com.entrada.cheekyMonkey.steward.home_del;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.OnBackPressInterface;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

import java.util.ArrayList;


/**
 * Created by csat on 15/01/2016.
 */
public class DelLogLayout implements View.OnClickListener,
        OnBackPressInterface, ICallDelDetail {

    ListView lv_logDetail;
    DelLogAdapter delLogAdapter;
    Context context;
    CustomTextview tv_date, tv_Pos, tv_OdrTime,
            tv_strTime, tv_retTime, tv_amt, tv_guest, tv_address,
            tv_city, tv_phone, tv_cancel, tv_save;
    EditText tv_del_time, tvchangAmt, tv_retAmt;
    Spinner sp_billNo, sp_delBoy;

    ArrayList<String> crewCode;
    ArrayList<String> crewName;
    ArrayAdapter<String> adapterCrewName, adapterBillName;
    ArrayList<HomeItem> homeList;
    String flag = "";
    LayoutInflater layoutInflater;
    String parameter, serverIP;

    public DelLogLayout(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    private static int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public View getDelLogPopup(String parameter, String serverIP) {

        View view = layoutInflater.inflate(R.layout.del_log_layout, null);

        tv_date = (CustomTextview) view.findViewById(R.id.tv_home_date);
        tv_Pos = (CustomTextview) view.findViewById(R.id.tv_pos);
        sp_billNo = (Spinner) view.findViewById(R.id.tv_home_bill);
        sp_delBoy = (Spinner) view.findViewById(R.id.tv_del_boy);
        tv_OdrTime = (CustomTextview) view.findViewById(R.id.tv_home_odr_time);
        tv_strTime = (CustomTextview) view.findViewById(R.id.tv_str_time);
        tv_del_time = (EditText) view.findViewById(R.id.tv_home_delTime);
        tv_retTime = (CustomTextview) view.findViewById(R.id.tv_ret_time);
        tv_amt = (CustomTextview) view.findViewById(R.id.tv_amt);
        tvchangAmt = (EditText) view.findViewById(R.id.tv_chng_amt);
        tv_retAmt = (EditText) view.findViewById(R.id.tv_ret_amt);
        tv_guest = (CustomTextview) view.findViewById(R.id.tv_guestName);
        tv_address = (CustomTextview) view.findViewById(R.id.tv_guest_address);
        tv_city = (CustomTextview) view.findViewById(R.id.tv_guest_city);
        tv_phone = (CustomTextview) view.findViewById(R.id.tv_guest_phone);
        tv_cancel = (CustomTextview) view.findViewById(R.id.tv_cancel_log);
        tv_save = (CustomTextview) view.findViewById(R.id.tv_save_log);
        tv_cancel.setOnClickListener(this);
        tv_save.setOnClickListener(this);

        this.parameter = parameter;
        this.serverIP = serverIP;

        String pos = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posCode;
        tv_date.setText(UserInfo.RunDate);
        tv_Pos.setText(pos);
        sp_billNo.requestFocus();

        adapterBillName = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        sp_billNo.setAdapter(adapterBillName);

        crewCode = new ArrayList<>();
        crewName = new ArrayList<>();
        adapterCrewName = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, crewName);
        sp_delBoy.setAdapter(adapterCrewName);
        init();

        lv_logDetail = (ListView) view.findViewById(R.id.lv_log_detail);
        delLogAdapter = new DelLogAdapter(context);
        lv_logDetail.setAdapter(delLogAdapter);
        lv_logDetail.setOnItemClickListener(onListViewItemClick());
        lv_logDetail.setOnTouchListener(onTouchListener());

        new DeliveryDetailTask(context, delLogAdapter, parameter, serverIP, DelLogLayout.this).execute();
        return view;
    }

    public void init() {

        crewCode.add("");
        crewName.add("");

        SQLiteDatabase mdb = POSDatabase.getMenuItem(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        try {
            Cursor cursor = getAllRecords_STEWARD_TYPE(mdb);
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    crewCode.add(cursor.getString(2));
                    crewName.add(cursor.getString(1));  // used internally(optional)

                } while (cursor.moveToNext());

                adapterCrewName.notifyDataSetChanged();

            }
            mdb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }
    }

    public AdapterView.OnItemClickListener onListViewItemClick() {

        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                DelLogAdapter.Holder holder = (DelLogAdapter.Holder) view.getTag();

                tv_Pos.setText(holder.tv_Pos.getText().toString());
                sp_billNo.setSelection(getIndex(sp_billNo, holder.tv_billNo.getText().toString()));
                tv_amt.setText(holder.tv_amt.getText().toString());
                tv_guest.setText(holder.tv_guest.getText().toString());
                tv_OdrTime.setText(holder.tv_OdrTime.getText().toString());
                sp_delBoy.setSelection(crewName.indexOf(holder.tv_delBoy.getText().toString()));
                tv_strTime.setText(holder.tv_strTime.getText().toString());
                tv_del_time.setText(holder.tv_del_time.getText().toString());
                tv_retTime.setText(holder.tv_retTime.getText().toString());
                tvchangAmt.setText(holder.tvchangAmt.getText().toString());
                tv_retAmt.setText(holder.tv_retAmt.getText().toString());

                HomeItem homeItem = homeList.get(position);
                tv_address.setText(homeItem.address);
                tv_city.setText(homeItem.city);
                tv_phone.setText(homeItem.phone);

                if (sp_delBoy.getSelectedItemPosition() == 0)
                    flag = "A";
                else if (tv_del_time.getText().toString().isEmpty())
                    flag = "D";
                else if (tv_retTime.getText().toString().isEmpty())
                    flag = "R";
            }

        };
    }

    public View.OnTouchListener onTouchListener() {

        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        };
    }

    public Cursor getAllRecords_STEWARD_TYPE(SQLiteDatabase mdb) {

        return mdb.query(DBConstants.KEY_EMP_DETAIL, new String[]{
                        DBConstants.KEY_EMP_CODE, DBConstants.KEY_EMP_NAME, DBConstants.KEY_EMP_USER_ID},
                DBConstants.KEY_EMP_TYPE + "=" + "'DEL. BOY'", null, null, null, null);
    }

    @Override
    public void getDelDetail(String response) {

        adapterBillName.add("");
        homeList = delLogAdapter.getDataSet();

        for (HomeItem home : homeList)
            adapterBillName.add(home.billNo);
    }

    @Override
    public void getSaveResponse(String response) {

        delLogAdapter.clearDataSet();
        new DeliveryDetailTask(context, delLogAdapter, parameter, serverIP, DelLogLayout.this).execute();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.tv_save_log:

                if (sp_billNo.getSelectedItemPosition() == 0)
                    Toast.makeText(context, "Select Bill No", Toast.LENGTH_SHORT).show();

                else if (sp_delBoy.getSelectedItemPosition() == 0)
                    Toast.makeText(context, "Select Delivery Boy", Toast.LENGTH_SHORT).show();

                else {

                    String parameter = UtilToCreateJSON.createHomeDelLogJSON(
                            context, sp_billNo.getSelectedItem().toString(),
                            crewCode.get(sp_delBoy.getSelectedItemPosition()),
                            tvchangAmt.getText().toString(),
                            tv_retAmt.getText().toString(), flag);

                    String serverIP = POSApplication.getSingleton().getmDataModel()
                            .getUserInfo().getServerIP();

                    DelLogTask delLogTask = new DelLogTask(context, parameter, serverIP, DelLogLayout.this);
                    delLogTask.execute();
                }

                break;

            case R.id.tv_cancel_log:

                sp_billNo.setSelection(0);
                tv_amt.setText("");
                tv_guest.setText("");
                tv_OdrTime.setText("");
                sp_delBoy.setSelection(0);
                tv_strTime.setText("");
                tv_del_time.setText("");
                tv_retTime.setText("");
                tvchangAmt.setText("");
                tv_retAmt.setText("");

                break;
        }
    }

    @Override
    public boolean onBackPress() {
        return false;
    }
}
