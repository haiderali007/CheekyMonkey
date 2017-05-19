package com.entrada.cheekyMonkey.steward.ordersplit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.OnBackPressInterface;
import com.entrada.cheekyMonkey.steward.bill.TableBillGenTask;
import com.entrada.cheekyMonkey.steward.billSplit.ICallSplitResponse;
import com.entrada.cheekyMonkey.steward.billSplit.SendBillSplitTask;
import com.entrada.cheekyMonkey.entity.POSItem;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.staticData.PrefHelper;
import com.entrada.cheekyMonkey.takeorder.adapter.TableAdapter;
import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;
import com.entrada.cheekyMonkey.takeorder.entity.TableItem;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.Logger;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by csat on 28/08/2015.
 */
public class OrderSplitFragment extends Fragment implements OnBackPressInterface,
        View.OnClickListener, ICallBackSendOrderSplitResponse, ICallSplitResponse {

    LinearLayout linearMidOrder_split;
    TextView selectedPos;
    ArrayAdapter<String> table_adapter_spinner;
    Spinner spinnerOldTable, spinnerOrderNo, spinnerNewTable;
    CustomTextview tv_clear_split, tv_send_split;
    String selectedTable = "", selectedOrderNo = "";
    ArrayAdapter<String> adapterTableList;
    ListView list_view_for_o_s_1ListView, list_view_for_o_s_2ListView;
    ArrayAdapter<String> table_order_no;
    ArrayList<String> Order_list = new ArrayList<>();
    ArrayList<OrderItem> arrayList_order_split = new ArrayList<>();
    OrderSplitAdapter orderSplitAdapter;
    ArrayList<OrderItem> arrayList_order_split_1;
    OrderSplitAdapter1 orderSplitAdapter1;
    String steward = "";
    private View view;
    private Context context;
    private TableAdapter tableAdapter_order_split;
    private ProgressBar progressBarTableBillGen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.order_split_layout, container, false);

        initData(view);
        return view;
    }

    public void initData(View view) {

        linearMidOrder_split = (LinearLayout) view.findViewById(R.id.linearMidOrder_split);
        selectedPos = (TextView) view.findViewById(R.id.selectedPosCounterSplit);

        spinnerOldTable = (Spinner) view.findViewById(R.id.spinner_table_for_order_split);
        spinnerOrderNo = (Spinner) view.findViewById(R.id.spinner_order_num_for_order_split);
        spinnerNewTable = (Spinner) view.findViewById(R.id.spinner_tableList);
        tv_clear_split = (CustomTextview) view.findViewById(R.id.tv_clr_split);
        tv_send_split = (CustomTextview) view.findViewById(R.id.send_for_order_split);

        list_view_for_o_s_1ListView = (ListView) view.findViewById(R.id.list_view_for_o_s_1);
        list_view_for_o_s_2ListView = (ListView) view.findViewById(R.id.list_view_for_o_s_2);


        UserInfo userInfo = POSApplication.getSingleton().getmDataModel().getUserInfo();
        list_view_for_o_s_1ListView.setBackgroundColor(userInfo.getBackground());
        list_view_for_o_s_2ListView.setBackgroundColor(userInfo.getBackground());

        tv_clear_split.setOnClickListener(this);
        tv_send_split.setOnClickListener(this);
        tableAdapter_order_split = new TableAdapter(context);

        POSItem posItem = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem();
        selectedPos.setText(posItem.posName);

        getRunningTables();
        show_Split_order_no(Order_list);

        arrayList_order_split = new ArrayList<>();
        orderSplitAdapter = new OrderSplitAdapter(context, R.layout.order_split_row_layout, arrayList_order_split, this);
        list_view_for_o_s_1ListView.setAdapter(orderSplitAdapter);

        arrayList_order_split_1 = new ArrayList<>();
        orderSplitAdapter1 = new OrderSplitAdapter1(context, R.layout.order_split_row_layout1, arrayList_order_split_1, this);
        list_view_for_o_s_2ListView.setAdapter(orderSplitAdapter1);
        steward = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.USER_ID);

        setAdapterForTable();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    public void getRunningTables() {

        if (!TextUtils.isEmpty(POSApplication.getSingleton()
                .getmDataModel().getUserInfo().getPosItem().posCode)) {
            String parameter = UtilToCreateJSON
                    .createTableJSON(context);
            String serverIP = POSApplication.getSingleton()
                    .getmDataModel().getUserInfo().getServerIP();
            new TableBillGenTask(context, tableAdapter_order_split, parameter, serverIP, progressBarTableBillGen).execute();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (tableAdapter_order_split != null) {
                    show_table_Order_split();
                }
            }
        }, 500);
    }

    public void show_table_Order_split() {

        ArrayList<String> tableListOrder = new ArrayList<>();
        tableListOrder.add("");
        table_adapter_spinner = new ArrayAdapter<>(context, R.layout.text_view_for_spinner, tableListOrder);

        table_adapter_spinner
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerOldTable.setAdapter(table_adapter_spinner);

        for (int i = 0; i < tableAdapter_order_split.getCount(); i++) {

            String table = ((TableItem) tableAdapter_order_split.getItem(i)).getCode();
            tableListOrder.add(table);
        }

        table_adapter_spinner.notifyDataSetChanged();
        spinnerOldTable.setOnItemSelectedListener(table_selected_listner());

    }

    public void show_Split_order_no(ArrayList<String> Order_list) {

        table_order_no = new ArrayAdapter<>(context, R.layout.text_view_bill_spinner, Order_list);
        table_order_no.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderNo.setAdapter(table_order_no);
        spinnerOrderNo.setOnItemSelectedListener(order_no_selected_listner());

    }

    private AdapterView.OnItemSelectedListener table_selected_listner() {
        // TODO Auto-generated method stub
        return new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                selectedTable = spinnerOldTable.getSelectedItem().toString();
                updateItemList();

                if (! POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posCode.isEmpty()) {

                    Order_list.clear();
                    String parameter = UtilToCreateJSON.createOrderSplitJson(context, selectedTable);
                    String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
                    new FetchOrderNoTask(context, spinnerOrderNo, table_order_no, parameter, serverIP,
                            progressBarTableBillGen, Order_list).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        };
    }


    private AdapterView.OnItemSelectedListener order_no_selected_listner() {

        return new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedOrderNo = spinnerOrderNo.getSelectedItem().toString();
                updateItemList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }


    public void updateItemList() {

        orderSplitAdapter.clear();
        orderSplitAdapter1.clear();

        String pos = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posCode;

        if (!(TextUtils.isEmpty(selectedTable) || TextUtils.isEmpty(pos))) {
            String parameter = UtilToCreateJSON
                    .createGetOrderItemSplitJson(context, selectedTable, selectedOrderNo);
            String serverIP = POSApplication.getSingleton()
                    .getmDataModel().getUserInfo().getServerIP();
            new TableItemTask(context, orderSplitAdapter, parameter, serverIP,
                    progressBarTableBillGen, arrayList_order_split).execute();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.send_for_order_split:

                try {
                    if (validationSuccess()) {

                        String newTable = spinnerNewTable.getSelectedItem().toString();

                        String parameter = UtilToCreateJSON.createSendOrderSplitJsonNew(
                                context, orderSplitAdapter, orderSplitAdapter1, selectedTable,
                                newTable, steward);
                        String serverIP = POSApplication.getSingleton()
                                .getmDataModel().getUserInfo().getServerIP();


                        if (!(TextUtils.isEmpty(parameter) && TextUtils.isEmpty(serverIP))) {
                            SendBillSplitTask sendBillSplitTask = new SendBillSplitTask(context, parameter,
                                    serverIP, this);
                            sendBillSplitTask.execute();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.tv_clr_split:

                spinnerOldTable.setSelection(0);
                spinnerNewTable.setSelection(0);
                break;

        }
    }


    public boolean validationSuccess() {

        if (orderSplitAdapter.getCount() == 0) {
            Toast.makeText(context, "old Table must have At least one Item", Toast.LENGTH_SHORT).show();
            return false;
        } else if (orderSplitAdapter1.getCount() == 0) {
            Toast.makeText(context, "New Table must have At least one Item ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (spinnerNewTable.getSelectedItemPosition() == 0) {
            Toast.makeText(context, "Select New Table", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }


    public void setAdapterForTable() {

        if (!TextUtils.isEmpty(POSApplication.getSingleton()
                .getmDataModel().getUserInfo().getPosItem().posCode)) {
            String parameter = UtilToCreateJSON
                    .createTableStatusJson(context);
            String serverIP = POSApplication.getSingleton()
                    .getmDataModel().getUserInfo().getServerIP();

            ArrayList<String> listTables = new ArrayList<>();
            listTables.add("");
            adapterTableList = new ArrayAdapter<>(context, R.layout.text_view_for_spinner, listTables);
            adapterTableList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerNewTable.setAdapter(adapterTableList);

            new TableNumberTask(context, adapterTableList, parameter, serverIP, progressBarTableBillGen).execute();
        }
    }

    @Override
    public void responseFromServerOrderSplit(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("ECABS_OrderSplitResult").equals("true")) {
                Toast.makeText(context, "Order Split Successfully", Toast.LENGTH_LONG).show();

                arrayList_order_split.clear();
                orderSplitAdapter.notifyDataSetChanged();
                arrayList_order_split_1.clear();
                orderSplitAdapter1.notifyDataSetChanged();
                tableAdapter_order_split.clearDataSet();
                getRunningTables();

            } else
                Toast.makeText(context, response, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getSplitResponse(String response) {

        Logger.i("OrderSplitResponse", response);
        try {

            JSONObject jsonObject = new JSONObject(response);
            String result = jsonObject.getString("BillSplitResult");

            if (result.equals("Success")) {

                orderSplitAdapter.clear();
                orderSplitAdapter1.clear();
                tableAdapter_order_split.clearDataSet();
                getRunningTables();

                Toast.makeText(context, "Order Split done", Toast.LENGTH_SHORT).show();
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
        int position = orderSplitAdapter.getPosition(itemToRemove);

        if (orderSplitAdapter.getPosition(itemToRemove) != -1) {
            if (!(itemToRemove.getO_name().contains("##") || itemToRemove.getO_name().contains("FREE"))) {

                if (itemToRemove.getO_quantity() >= 1) {

                    Log.i("position", String.valueOf(position));

                    int index = -1;
                    for (int i = 0; i < arrayList_order_split_1.size(); i++) {

                        OrderItem orderItem = arrayList_order_split_1.get(i);
                        if (orderItem.getO_code().equals(itemToRemove.getO_code())) {
                            index = i;
                            break;
                        }
                    }

                    if (index != -1) {

                        itemToRemove2 = arrayList_order_split_1.get(index);

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
                        arrayList_order_split_1.add(itemToRemove2);
                    }
                    orderSplitAdapter1.notifyDataSetChanged();

                    if (itemToRemove.getO_quantity() == 1) {

                        arrayList_order_split.remove(itemToRemove);

                    } else {

                        itemToRemove.setO_quantity(itemToRemove.getO_quantity() - 1);
                    }

                    orderSplitAdapter.notifyDataSetChanged();

                    if (!arrayList_order_split.isEmpty()) {
                        while (arrayList_order_split.listIterator(
                                position).hasNext()) {

                            itemToRemove = arrayList_order_split
                                    .listIterator(position).next();

                            if (itemToRemove.getO_name().contains("##")
                                    || itemToRemove.getO_name()
                                    .contains("FREE")) {

                                arrayList_order_split_1.add(itemToRemove);
                                orderSplitAdapter1.notifyDataSetChanged();
                                arrayList_order_split.remove(orderSplitAdapter.getPosition(itemToRemove));
                                orderSplitAdapter.notifyDataSetChanged();

                            } else {
                                break;
                            }
                        }
                    }

                } else {
                    Log.i("position", String.valueOf(position));

                    arrayList_order_split_1.add(itemToRemove);
                    orderSplitAdapter1.notifyDataSetChanged();
                    arrayList_order_split.remove(position);
                    orderSplitAdapter.notifyDataSetChanged();

                    if (!arrayList_order_split.isEmpty()) {

                        while (arrayList_order_split.listIterator(
                                position).hasNext()) {

                            itemToRemove = arrayList_order_split.listIterator(position).next();

                            if (itemToRemove.getO_name().contains("##")
                                    || itemToRemove.getO_name()
                                    .contains("FREE")) {


                                arrayList_order_split_1.add(itemToRemove);
                                orderSplitAdapter1.notifyDataSetChanged();
                                arrayList_order_split.remove(orderSplitAdapter.getPosition(itemToRemove));
                                orderSplitAdapter.notifyDataSetChanged();
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
        int position = orderSplitAdapter1.getPosition(itemToRemove);

        if (orderSplitAdapter1.getPosition(itemToRemove) != -1) {

            if (!(itemToRemove.getO_name().contains("##") || itemToRemove
                    .getO_name().contains("FREE"))) {

                if (itemToRemove.getO_quantity() >= 1) {

                    Log.i("position", String.valueOf(position));

                    int index = -1;
                    for (int i = 0; i < arrayList_order_split.size(); i++) {

                        OrderItem orderItem = arrayList_order_split.get(i);
                        if (orderItem.getO_code().equals(itemToRemove.getO_code())) {
                            index = i;
                            break;
                        }
                    }

                    if (index != -1) {

                        itemToRemove2 = arrayList_order_split.get(index);

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
                        arrayList_order_split.add(itemToRemove2);
                    }
                    orderSplitAdapter.notifyDataSetChanged();

                    if (itemToRemove.getO_quantity() == 1) {

                        arrayList_order_split_1.remove(itemToRemove);

                    } else {
                        itemToRemove
                                .setO_quantity(itemToRemove.getO_quantity() - 1);
                    }

                    orderSplitAdapter1.notifyDataSetChanged();

                    if (!arrayList_order_split_1.isEmpty()) {

                        while (arrayList_order_split_1.listIterator(
                                position).hasNext()) {

                            itemToRemove = arrayList_order_split_1
                                    .listIterator(position).next();

                            if (itemToRemove.getO_name().contains("##")
                                    || itemToRemove.getO_name()
                                    .contains("FREE")) {

                                arrayList_order_split.add(itemToRemove);
                                orderSplitAdapter.notifyDataSetChanged();
                                arrayList_order_split_1.remove(orderSplitAdapter1.getPosition(itemToRemove));
                                orderSplitAdapter1.notifyDataSetChanged();

                            } else {
                                break;
                            }
                        }
                    }

                } else {

                    Log.i("position", String.valueOf(position));

                    arrayList_order_split.add(itemToRemove);
                    orderSplitAdapter.notifyDataSetChanged();
                    arrayList_order_split_1.remove(position);
                    orderSplitAdapter1.notifyDataSetChanged();

                    if (!arrayList_order_split_1.isEmpty()) {

                        while (arrayList_order_split_1.listIterator(
                                position).hasNext()) {

                            itemToRemove = arrayList_order_split_1
                                    .listIterator(position).next();

                            if (itemToRemove.getO_name().contains("##")
                                    || itemToRemove.getO_name()
                                    .contains("FREE")) {

                                arrayList_order_split.add(itemToRemove);
                                orderSplitAdapter.notifyDataSetChanged();
                                arrayList_order_split_1.remove(orderSplitAdapter1.getPosition(itemToRemove));
                                orderSplitAdapter1.notifyDataSetChanged();

                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}