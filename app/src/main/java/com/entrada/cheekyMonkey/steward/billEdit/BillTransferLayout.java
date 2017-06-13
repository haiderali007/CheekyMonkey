package com.entrada.cheekyMonkey.steward.billEdit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.OnBackPressInterface;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.Logger;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by csat on 15/01/2016.
 */
public class BillTransferLayout implements View.OnClickListener,
        OnBackPressInterface, ICallBillTransferResponse {

    Context context;
    CustomTextview tv_amount, tv_clear, tv_transfer;
    EditText et_reason;
    Spinner sp_from_pos, sp_to_pos, sp_billNo;
    ArrayList<String> posCodeList;
    ArrayList<String> posTypeList;
    ArrayList<String> billAmtList;
    ArrayAdapter<String> pos_adapter;
    ArrayAdapter<String> billsAdapter;


    LayoutInflater layoutInflater;

    public BillTransferLayout(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public View getBillTransferPopup() {

        View view = layoutInflater.inflate(R.layout.bill_transfer_layout, null);

        sp_from_pos = (Spinner) view.findViewById(R.id.sp_from_pos);
        sp_to_pos = (Spinner) view.findViewById(R.id.sp_to_pos);
        sp_billNo = (Spinner) view.findViewById(R.id.sp_billno);

        et_reason = (EditText) view.findViewById(R.id.et_bill_trns_rsn);
        tv_amount = (CustomTextview) view.findViewById(R.id.tv_bill_amount);
        tv_clear = (CustomTextview) view.findViewById(R.id.clr_trns_detail);
        tv_transfer = (CustomTextview) view.findViewById(R.id.send_bill_trnsfr);

        tv_clear.setOnClickListener(this);
        tv_transfer.setOnClickListener(this);

        billAmtList = new ArrayList<>();
        billAmtList.add("");

        /********************* Setting PoS Spinner data *****************/

        initPosAdapter();
        sp_from_pos.setAdapter(pos_adapter);
        sp_to_pos.setAdapter(pos_adapter);
        sp_from_pos.setOnItemSelectedListener(onPosSelectedListener());

        /********************* Setting Available Bill No. *********************/

        ArrayList<String> billList = new ArrayList<>();
        billList.add("");

        billsAdapter = new ArrayAdapter<>(context, R.layout.text_view_for_spinner, billList);
        billsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_billNo.setAdapter(billsAdapter);
        sp_billNo.setOnItemSelectedListener(onBillNoSelectedListener());

        return view;
    }


    private void initPosAdapter() {

        pos_adapter = new ArrayAdapter<>(context, R.layout.text_view_for_spinner);
        pos_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pos_adapter.add("");

        posCodeList = new ArrayList<>();
        posTypeList = new ArrayList<>();
        posCodeList.add("");
        posTypeList.add("");

        posCodeList.add("REST");
        pos_adapter.add("RESTAURANT");
        posTypeList.add("R");


        /*SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();
        try {

            Cursor cursor = mdb.query(DBConstants.KEY_OUTLET_POS_TABLE, new String[]{
                            DBConstants.KEY_OUTLET_POS_CODE, DBConstants.KEY_OUTLET_POS_NAME,
                            DBConstants.KEY_OUTLET_POS_TYPE}, null, null, null, null, null);

            if (cursor.moveToFirst()) {

                do {

                    posCodeList.add(cursor.getString(0));
                    pos_adapter.add(cursor.getString(1));
                    posTypeList.add(cursor.getString(2));

                } while (cursor.moveToNext());
            }

            cursor.close();
            mdb.setTransactionSuccessful();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }*/
    }

    private AdapterView.OnItemSelectedListener onPosSelectedListener() {

        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                showBillSeriesForPosAt(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private AdapterView.OnItemSelectedListener onBillNoSelectedListener() {

        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (sp_billNo.getSelectedItemPosition() == 0)
                    tv_amount.setText("");
                else
                    tv_amount.setText(billAmtList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private void showBillSeriesForPosAt(int position) {

        String posCode = posCodeList.get(position);

        if (!posCode.isEmpty()) {

            billsAdapter.clear();
            billsAdapter.add("");
            billAmtList.clear();
            billAmtList.add("");

            String parameter = UtilToCreateJSON.createPosJSON(posCode, "", "A");
            String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();

            new FetchBillNoTask(context, billsAdapter, billAmtList, parameter, serverIP).execute();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.send_bill_trnsfr:

                if (validationSuccess()) {

                    int index = sp_to_pos.getSelectedItemPosition();

                    String parameter = UtilToCreateJSON.createBillTransferJSON(context,
                            posCodeList.get(index), posTypeList.get(index),
                            sp_billNo.getSelectedItem().toString(),
                            et_reason.getText().toString());

                    String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
                    new BillTransferTask(context, parameter, serverIP, this).execute();

                }

                break;


            case R.id.clr_trns_detail:

                sp_billNo.setSelection(0);
                sp_from_pos.setSelection(0);
                sp_to_pos.setSelection(0);
                tv_amount.setText("");
                et_reason.setText("");

                break;
        }
    }

    private boolean validationSuccess() {

        if (sp_from_pos.getSelectedItemPosition() == 0) {

            Toast.makeText(context, "Select old POS", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sp_billNo.getSelectedItemPosition() == 0) {

            Toast.makeText(context, "Select Bill No.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sp_to_pos.getSelectedItemPosition() == 0) {

            Toast.makeText(context, "Select new POS", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (et_reason.getText().toString().isEmpty()) {

            Toast.makeText(context, "Enter reason", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sp_from_pos.getSelectedItemPosition() == sp_to_pos.getSelectedItemPosition()){
            Toast.makeText(context, "Can not transfer to same POS", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void getBillTransferResponse(String response) {

        Logger.i("BillTransferResponse", response);
        try {

            JSONObject jsonObject = new JSONObject(response);
            String result = jsonObject.getString("Transfer_BillResult");

            if (result.equals("Success")) {

                sp_billNo.setSelection(0);
                tv_amount.setText("");
                et_reason.setText("");
                showBillSeriesForPosAt(sp_to_pos.getSelectedItemPosition());

                Toast.makeText(context, "Bill transferred", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onBackPress() {
        return false;
    }
}
