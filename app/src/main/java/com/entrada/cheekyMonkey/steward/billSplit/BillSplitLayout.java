package com.entrada.cheekyMonkey.steward.billSplit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.OnBackPressInterface;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.steward.home_del.HomeItem;
import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.Logger;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by csat on 15/01/2016.
 */

public class BillSplitLayout implements View.OnClickListener,
        OnBackPressInterface, ICallSplitResponse {

    Context context;
    CustomTextview tv_tbl, tv_amount, tv_clear, tv_transfer;
    Spinner sp_pos, sp_billNo;

    ArrayList<String> posCodeList;
    ArrayList<HomeItem> homeItemList;
    ArrayAdapter<String> pos_adapter;
    ArrayAdapter<String> billNoAdapter;

    ListView lv_split1, lv_split2;

    ArrayList<OrderItem> itemList1;
    BillSplitAdapter1 itemAdapter1;
    ArrayList<OrderItem> itemList2;
    BillSplitAdapter2 itemAdapter2;
    String steward = "";

    LayoutInflater layoutInflater;


    public BillSplitLayout(Context context, String steward) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.steward = steward;
    }

    public View getBillSplitPopup() {

        View view = layoutInflater.inflate(R.layout.bill_split_layout, null);
        sp_pos = (Spinner) view.findViewById(R.id.sp_split_pos);
        sp_billNo = (Spinner) view.findViewById(R.id.sp_split_billno);
        tv_tbl = (CustomTextview) view.findViewById(R.id.tv_split_billtbl);
        tv_amount = (CustomTextview) view.findViewById(R.id.tv_bill_amt);

        lv_split1 = (ListView) view.findViewById(R.id.lv_splt1);
        lv_split2 = (ListView) view.findViewById(R.id.lv_splt2);

        tv_clear = (CustomTextview) view.findViewById(R.id.clr_bill_split);
        tv_transfer = (CustomTextview) view.findViewById(R.id.save_bill_split);

        tv_clear.setOnClickListener(this);
        tv_transfer.setOnClickListener(this);


        homeItemList = new ArrayList<>();

        /********************* Setting PoS Spinner data *****************/

        initPosAdapter();
        sp_pos.setAdapter(pos_adapter);
        sp_pos.setOnItemSelectedListener(onPosSelectedListener());

        /********************* Setting Available Bill Nos. *********************/

        ArrayList<String> billList = new ArrayList<>();
        billList.add("");

        billNoAdapter = new ArrayAdapter<>(context, R.layout.text_view_for_spinner, billList);
        billNoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_billNo.setAdapter(billNoAdapter);
        sp_billNo.setOnItemSelectedListener(onBillNoSelectedListener());

        /********************* Setting both List View Data ********************/

        itemList1 = new ArrayList<>();
        itemAdapter1 = new BillSplitAdapter1(context, R.layout.bill_split_row_layout, itemList1, this);
        lv_split1.setAdapter(itemAdapter1);

        itemList2 = new ArrayList<>();
        itemAdapter2 = new BillSplitAdapter2(context, R.layout.bill_split_row_layout, itemList2, this);
        lv_split2.setAdapter(itemAdapter2);

        return view;
    }


    public void initPosAdapter() {

        pos_adapter = new ArrayAdapter<>(context, R.layout.text_view_for_spinner);
        pos_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pos_adapter.add("");

        posCodeList = new ArrayList<>();
        posCodeList.add("");

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();
        try {

            Cursor cursor = mdb.query(DBConstants.KEY_OUTLET_POS_TABLE, new String[]{
                            DBConstants.KEY_OUTLET_POS_CODE, DBConstants.KEY_OUTLET_POS_NAME,
                            DBConstants.KEY_OUTLET_POS_TYPE}, null, null,
                    null, null, null);

            if (cursor.moveToFirst()) {

                do {

                    posCodeList.add(cursor.getString(0));
                    pos_adapter.add(cursor.getString(1));

                } while (cursor.moveToNext());
            }

            cursor.close();
            mdb.setTransactionSuccessful();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }
    }


    public AdapterView.OnItemSelectedListener onPosSelectedListener() {

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

    public void showBillSeriesForPosAt(int position) {

        String posCode = posCodeList.get(position);
        billNoAdapter.clear();
        homeItemList.clear();

        if (!posCode.isEmpty()) {

            String pos = posCodeList.get(position);
            String parameter = UtilToCreateJSON.createPosJSON(pos, "", "A"); // billType - paid / Billed
            String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();

            new FetchBillNoDetailTask(context, billNoAdapter, homeItemList, parameter, serverIP).execute();
        }
    }

    public AdapterView.OnItemSelectedListener onBillNoSelectedListener() {

        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (sp_billNo.getSelectedItemPosition() == 0) {

                    tv_tbl.setText("");
                    tv_amount.setText("");
                    itemAdapter1.clear();
                    itemAdapter2.clear();
                } else {

                    tv_tbl.setText(homeItemList.get(position - 1).TABLE);
                    tv_amount.setText(homeItemList.get(position - 1).TOTAL);
                    itemList1.clear();
                    itemAdapter2.clear();
                    itemAdapter1.addAll(homeItemList.get(position - 1).itemList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.save_bill_split:

                try {
                    if (validationSuccess()) {

                        String parameter = UtilToCreateJSON.createBillSplitJSON(context,
                                posCodeList.get(sp_pos.getSelectedItemPosition()),
                                sp_billNo.getSelectedItem().toString(),
                                tv_tbl.getText().toString(),
                                itemAdapter1, itemAdapter2, steward);

                        String serverIP = POSApplication.getSingleton()
                                .getmDataModel().getUserInfo().getServerIP();

                        if (!(TextUtils.isEmpty(parameter) && TextUtils.isEmpty(serverIP))) {
                            SendBillSplitTask sendBillSplitTask = new SendBillSplitTask(context, parameter,
                                    serverIP, BillSplitLayout.this);
                            sendBillSplitTask.execute();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case R.id.clr_bill_split:

                sp_billNo.setSelection(0);
                tv_tbl.setText("");
                itemAdapter1.clear();
                itemAdapter2.clear();
                tv_amount.setText("");

                break;
        }
    }

    public boolean validationSuccess() {

        if (sp_pos.getSelectedItemPosition() == 0) {
            Toast.makeText(context, "Select POS first", Toast.LENGTH_SHORT).show();
            return false;

        } else if (sp_billNo.getSelectedItemPosition() == 0) {
            Toast.makeText(context, "Select Bill No", Toast.LENGTH_SHORT).show();
            return false;

        } else if (itemAdapter1.getCount() == 0) {
            Toast.makeText(context, "Old Bill must have At least one Item", Toast.LENGTH_SHORT).show();
            return false;

        } else if (itemAdapter2.getCount() == 0) {
            Toast.makeText(context, "New Bill must have At least one Item ", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    @Override
    public void getSplitResponse(String response) {

        Logger.i("BillSplitResponse", response);
        try {

            JSONObject jsonObject = new JSONObject(response);
            String result = jsonObject.getString("BillSplitResult");

            if (result.equals("Success")) {

                itemAdapter1.clear();
                itemAdapter2.clear();
                tv_amount.setText("");
                showBillSeriesForPosAt(sp_pos.getSelectedItemPosition());
                itemAdapter1.notifyDataSetChanged();

                Toast.makeText(context, "Bill Split done", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onBackPress() {
        return false;
    }


    public void onclick_remove_1(View v) {


        OrderItem itemToRemove = (OrderItem) v.getTag();
        OrderItem itemToRemove2;
        int position = itemAdapter1.getPosition(itemToRemove);

        if (itemAdapter1.getPosition(itemToRemove) != -1) {
            if (!(itemToRemove.getO_name().contains("##") || itemToRemove.getO_name().contains("FREE"))) {

                if (itemToRemove.getO_quantity() >= 1) {

                    Log.i("position", String.valueOf(position));

                    int index = -1;
                    for (int i = 0; i < itemList2.size(); i++) {

                        OrderItem orderItem = itemList2.get(i);
                        if (orderItem.getO_code().equals(itemToRemove.getO_code())) {
                            index = i;
                            break;
                        }
                    }

                    if (index != -1) {

                        itemToRemove2 = itemList2.get(index);
                        itemToRemove2.o_quantity = itemToRemove2.getO_quantity() + 1;
                    } else {
                        itemToRemove2 = new OrderItem();
                        itemToRemove2.orderNo = itemToRemove.orderNo;
                        itemToRemove2.o_code = itemToRemove.o_code;
                        itemToRemove2.o_name = itemToRemove.o_name;
                        itemToRemove2.o_itm_rmrk = itemToRemove.o_itm_rmrk;
                        itemToRemove2.o_addon_code = itemToRemove.o_addon_code;
                        itemToRemove2.o_mod = itemToRemove.o_mod;
                        itemToRemove2.o_combo_code = itemToRemove.o_combo_code;
                        itemToRemove2.o_happy_hour = itemToRemove.o_happy_hour;
                        itemToRemove2.o_subunit = "";
                        itemToRemove2.o_quantity = 1;
                        itemToRemove2.o_amount = itemToRemove.o_amount;
                        itemToRemove2.o_price = itemToRemove.o_price;
                        itemToRemove2.o_disc = itemToRemove.o_disc;

                        Log.i("else q1", String.valueOf(itemToRemove.getO_quantity()));
                        itemList2.add(itemToRemove2);
                    }
                    itemAdapter2.notifyDataSetChanged();

                    if (itemToRemove.getO_quantity() == 1) {

                        itemList1.remove(itemToRemove);

                    } else {

                        itemToRemove.setO_quantity(itemToRemove.getO_quantity() - 1);
                    }

                    itemAdapter1.notifyDataSetChanged();

                    if (!itemList1.isEmpty()) {
                        while (itemList1.listIterator(
                                position).hasNext()) {

                            itemToRemove = itemList1
                                    .listIterator(position).next();

                            if (itemToRemove.getO_name().contains("##")
                                    || itemToRemove.getO_name()
                                    .contains("FREE")) {

                                itemList2.add(itemToRemove);
                                itemAdapter2.notifyDataSetChanged();
                                itemList1.remove(itemAdapter1.getPosition(itemToRemove));
                                itemAdapter1.notifyDataSetChanged();

                            } else {
                                break;
                            }
                        }
                    }

                } else {
                    Log.i("position", String.valueOf(position));

                    itemList2.add(itemToRemove);
                    itemAdapter2.notifyDataSetChanged();
                    itemList1.remove(position);
                    itemAdapter1.notifyDataSetChanged();

                    if (!itemList1.isEmpty()) {

                        while (itemList1.listIterator(
                                position).hasNext()) {

                            itemToRemove = itemList1.listIterator(position).next();

                            if (itemToRemove.getO_name().contains("##")
                                    || itemToRemove.getO_name()
                                    .contains("FREE")) {


                                itemList2.add(itemToRemove);
                                itemAdapter2.notifyDataSetChanged();
                                itemList1.remove(itemAdapter1.getPosition(itemToRemove));
                                itemAdapter1.notifyDataSetChanged();
                                // }
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void onclick_remove_2(View v) {

        OrderItem itemToRemove = (OrderItem) v.getTag();
        OrderItem itemToRemove2;
        int position = itemAdapter2.getPosition(itemToRemove);

        if (itemAdapter2.getPosition(itemToRemove) != -1) {

            if (!(itemToRemove.getO_name().contains("##") || itemToRemove
                    .getO_name().contains("FREE"))) {

                if (itemToRemove.getO_quantity() >= 1) {

                    Log.i("position", String.valueOf(position));

                    int index = -1;
                    for (int i = 0; i < itemList1.size(); i++) {

                        OrderItem orderItem = itemList1.get(i);
                        if (orderItem.getO_code().equals(itemToRemove.getO_code())) {
                            index = i;
                            break;
                        }
                    }

                    if (index != -1) {

                        itemToRemove2 = itemList1.get(index);
                        itemToRemove2.o_quantity = itemToRemove2.getO_quantity() + 1;
                    } else {

                        itemToRemove2 = new OrderItem();
                        itemToRemove2.orderNo = itemToRemove.orderNo;
                        itemToRemove2.o_code = itemToRemove.o_code;
                        itemToRemove2.o_name = itemToRemove.o_name;
                        itemToRemove2.o_itm_rmrk = itemToRemove.o_itm_rmrk;
                        itemToRemove2.o_addon_code = itemToRemove.o_addon_code;
                        itemToRemove2.o_mod = itemToRemove.o_mod;
                        itemToRemove2.o_combo_code = itemToRemove.o_combo_code;
                        itemToRemove2.o_happy_hour = itemToRemove.o_happy_hour;
                        itemToRemove2.o_subunit = "";
                        itemToRemove2.o_quantity = 1;
                        itemToRemove2.o_amount = itemToRemove.o_amount;
                        itemToRemove2.o_price = itemToRemove.o_price;
                        itemToRemove2.o_disc = itemToRemove.o_disc;

                        Log.i("else q1",
                                String.valueOf(itemToRemove.getO_quantity()));
                        itemList1.add(itemToRemove2);
                    }
                    itemAdapter1.notifyDataSetChanged();

                    if (itemToRemove.getO_quantity() == 1)
                        itemList2.remove(itemToRemove);

                    else
                        itemToRemove.setO_quantity(itemToRemove.getO_quantity() - 1);

                    itemAdapter2.notifyDataSetChanged();

                    if (!itemList2.isEmpty()) {

                        while (itemList2.listIterator(position).hasNext()) {

                            itemToRemove = itemList2.listIterator(position).next();

                            if (itemToRemove.getO_name().contains("##") || itemToRemove.getO_name().contains("FREE")) {

                                itemList1.add(itemToRemove);
                                itemAdapter1.notifyDataSetChanged();
                                itemList2.remove(itemAdapter2.getPosition(itemToRemove));
                                itemAdapter2.notifyDataSetChanged();

                            } else
                                break;
                        }
                    }

                } else {

                    Log.i("position", String.valueOf(position));

                    itemList1.add(itemToRemove);
                    itemAdapter1.notifyDataSetChanged();
                    itemList2.remove(position);
                    itemAdapter2.notifyDataSetChanged();

                    if (!itemList2.isEmpty()) {

                        while (itemList2.listIterator(position).hasNext()) {

                            itemToRemove = itemList2.listIterator(position).next();

                            if (itemToRemove.getO_name().contains("##") || itemToRemove.getO_name().contains("FREE")) {

                                itemList1.add(itemToRemove);
                                itemAdapter1.notifyDataSetChanged();
                                itemList2.remove(itemAdapter2.getPosition(itemToRemove));
                                itemAdapter2.notifyDataSetChanged();

                            } else
                                break;
                        }
                    }
                }
            }
        }
    }

}
