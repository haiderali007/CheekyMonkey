package com.entrada.cheekyMonkey.steward.bill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.OnBackPressInterface;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.steward.home_del.HomeItem;
import com.entrada.cheekyMonkey.steward.billEdit.BillSettleTask;
import com.entrada.cheekyMonkey.steward.billEdit.ICallSettleResponse;
import com.entrada.cheekyMonkey.steward.discount.DiscountLayout;
import com.entrada.cheekyMonkey.steward.discount.ICallDiscList;
import com.entrada.cheekyMonkey.entity.POSItem;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.staticData.PrefHelper;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.takeorder.adapter.TableAdapter;
import com.entrada.cheekyMonkey.takeorder.entity.TableItem;
import com.entrada.cheekyMonkey.takeorder.popup.CustomKeypad;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.Logger;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by csat on 15/06/2015.
 */
public class BillGenerateFragment extends Fragment implements OnBackPressInterface, View.OnClickListener,
        ICallBackSavdBillResponse, ICallDiscList, ICallPaidAmount, ICallSettleResponse,
        ICallBillGenerate {

    public static FrameLayout frameLayout_discount;
    private static android.os.Handler mHandler = null;
    private static String TAG = "PlainTextActivity";
    public CustomTextview textviewForBIllno, textviewForDiscount, textviewForSubtotal, textviewForTax,
            textviewForTotal, textViewTAmt, textViewChange, textViewStlMode, txtCash, txtCredit;
    public DiscountLayout discountLayout;
    public String subtotal = "", discount = "", taxes = "", total = "", bill_no = "", bill_rem = "";
    TableAdapter adapter_bill_gen;
    LinkedList<byte[]> printlist = new LinkedList<>();
    ArrayList<String> bill_format = new ArrayList<>();
    String dis_code_bill = "", dis_per_bill = "";
    String posType = "";
    HomeItem homeItem;
    LinearLayout layoutBillDetail, layoutStlMode, layout_subtotal, layout_bill_No;
    RelativeLayout relativeLayoutTable;
    boolean settleMandatory = false, showSettleMode = false, disc_reason_mandat = false ;
    float cashPaid = 0, creditPaid = 0;
    private Context context;

    private LinearLayout LinerLayoutBillDetails, layout_flipMode, layout_billing, layout_bill_settle;
    private GridView gridViewCurrency;
    ArrayList<String> currencyList = new ArrayList<>(); ;
    ArrayAdapter<String> currencyAdapter;

    private CustomTextview textViewBillNo, textViewTax, textViewDiscount, textViewSubTotal, textViewTotal, textViewTotalQuantity,
            textViewTotalPrice, tableNoDetails, txtOrderDiscount, txtOrderGenerate, txtBillSave, selectTableBillGene,
            textViewTotalCount, textViewBillRemark, textViewGuest, textViewGuestName;
    private RelativeLayout layout_table;
    private GridView gridViewShowTableBillGene;
    private ProgressBar progressBarTableBillGen, progressListItem;
    private AdapterOrderTable adapterOrderTable;
    private String formattedDate = "";
    private String time = "";
    public String table = "", tableCode = "" ;


    public StringBuilder taxWithTaxName = new StringBuilder();
    boolean isGuestCopy = true;
    boolean isOfficeCopy = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bill_genrate_layout, container, false);
        initData(view);
        setTable();
        return view;
    }

    public void initData(View view) {

        layout_flipMode = (LinearLayout) view.findViewById(R.id.layout_flipMode);
        layout_billing = (LinearLayout) view.findViewById(R.id.layout_bill_detail);
        layout_table = (RelativeLayout) view.findViewById(R.id.layout_table);
        layoutBillDetail = (LinearLayout) view.findViewById(R.id.layoutBillDetail);

        getAttributes();
        setCurrencyView(view);
        flipIfPortrait();

        LinerLayoutBillDetails = (LinearLayout) view.findViewById(R.id.LinerLayoutBillDetails);
        textViewBillNo = (CustomTextview) view.findViewById(R.id.textViewBillNo);
        textViewTax = (CustomTextview) view.findViewById(R.id.textViewTax);
        textViewDiscount = (CustomTextview) view.findViewById(R.id.textViewDiscount);
        textViewSubTotal = (CustomTextview) view.findViewById(R.id.textViewSubTotal);
        textViewTotalCount = (CustomTextview) view.findViewById(R.id.textViewTotalCount);
        textViewTotal = (CustomTextview) view.findViewById(R.id.textViewTotal);
        textViewTotalQuantity = (CustomTextview) view.findViewById(R.id.textViewTotalQuantity);
        textViewTotalPrice = (CustomTextview) view.findViewById(R.id.textViewTotalPrice);
        txtOrderDiscount = (CustomTextview) view.findViewById(R.id.txtOrderDiscount);
        txtOrderGenerate = (CustomTextview) view.findViewById(R.id.txtbillGenerate);
        tableNoDetails = (CustomTextview) view.findViewById(R.id.tableNoDetails);
        txtBillSave = (CustomTextview) view.findViewById(R.id.txtBillSave);
        ListView listViewBillItem = (ListView) view.findViewById(R.id.listViewBillItem);
        gridViewShowTableBillGene = (GridView) view.findViewById(R.id.gridViewShowTableBillGene);
        progressBarTableBillGen = (ProgressBar) view.findViewById(R.id.progressBarTableBillGen);
        progressListItem = (ProgressBar) view.findViewById(R.id.progressListItem);
        textViewBillRemark = (CustomTextview) view.findViewById(R.id.textViewBillRemark);
        textViewGuest = (CustomTextview) view.findViewById(R.id.textViewGuest);
        textViewGuestName = (CustomTextview) view.findViewById(R.id.textViewGuestName);
        progressListItem.setVisibility(View.GONE);
        LinearLayout ll_list_itemBill = (LinearLayout) view.findViewById(R.id.ll_list_item);

        relativeLayoutTable = (RelativeLayout) view.findViewById(R.id.relative_HeaderTable);

        layoutStlMode = (LinearLayout) view.findViewById(R.id.layout_settle_mode);
        layout_subtotal = (LinearLayout) view.findViewById(R.id.layout_subtotal);
        layout_bill_No = (LinearLayout) view.findViewById(R.id.layout_bill_No);

        discountLayout = new DiscountLayout(context, this);
        frameLayout_discount = (FrameLayout) view.findViewById(R.id.frameLayout_discount);
        selectTableBillGene = (CustomTextview) view.findViewById(R.id.selectTableBillGene);

        textviewForBIllno = (CustomTextview) view.findViewById(R.id.textviewForBIllno);
        textviewForDiscount = (CustomTextview) view.findViewById(R.id.textviewForDiscount);
        textviewForSubtotal = (CustomTextview) view.findViewById(R.id.textviewForSubtotal);
        textviewForTax = (CustomTextview) view.findViewById(R.id.textviewForTax);
        textviewForTotal = (CustomTextview) view.findViewById(R.id.textviewForTotal);
        textViewTAmt = (CustomTextview) view.findViewById(R.id.tv_TenderedAmt);
        textViewChange = (CustomTextview) view.findViewById(R.id.tv_ChangeAmt);
        textViewStlMode = (CustomTextview) view.findViewById(R.id.tv_stlmnt_mode);
        txtCash = (CustomTextview) view.findViewById(R.id.tv_cash);
        txtCredit = (CustomTextview) view.findViewById(R.id.tv_credit);

        txtCash.setOnClickListener(this);
        txtCredit.setOnClickListener(this);

        UserInfo userInfo = POSApplication.getSingleton().getmDataModel().getUserInfo();

        listViewBillItem.setBackgroundColor(userInfo.getBackground());
        ll_list_itemBill.setBackgroundColor(userInfo.getBackground());
        //layoutTblHeader.setBackgroundColor(userInfo.getNaviBackground());
        //LinerLayoutBillDetails.setBackgroundColor(userInfo.getNaviBackground());

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
        formattedDate = df.format(c.getTime());
        time = sdf.format(c.getTime());


        adapterOrderTable = new AdapterOrderTable(context);
        listViewBillItem.setAdapter(adapterOrderTable);
        adapter_bill_gen = new TableAdapter(context);

        txtOrderDiscount.setOnClickListener(this);
        txtOrderGenerate.setOnClickListener(this);
        textViewBillRemark.setOnClickListener(this);
        txtBillSave.setOnClickListener(this);
        selectTableBillGene.setOnClickListener(this);


        POSItem posItem = POSApplication.getSingleton().getmDataModel()
                .getUserInfo().getPosItem();
        posType = posItem.posType;
        String poscode = posItem.posCode;

        if (posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_R)) {

            gridViewShowTableBillGene.setAdapter(adapter_bill_gen);
            gridViewShowTableBillGene.setOnItemClickListener(OnTableClickListener());

            selectTableBillGene.setVisibility(View.VISIBLE);
            if (!poscode.isEmpty()) {
                String parameter = UtilToCreateJSON.createTableJSON(context);
                String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
                new TableBillGenTask(context, adapter_bill_gen, parameter, serverIP, progressBarTableBillGen).execute();
            }
        }
    }



    public void setCurrencyView(View v){

        if (settleMandatory && currencyHasCreated()){

            layout_bill_settle = (LinearLayout) v.findViewById(R.id.layout_settle_up);
            gridViewCurrency = (GridView) v.findViewById(R.id.gridview_show_currency);

            currencyAdapter = new ArrayAdapter<>(context, R.layout.currency_value, currencyList);
            gridViewCurrency.setAdapter(currencyAdapter);
            gridViewCurrency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String amount = ((TextView) view).getText().toString().substring(2);
                    getCashAmount(amount, "CURR");
                }
            });
        }
    }

    public boolean currencyHasCreated() {

        if (currencyList.isEmpty()){

            SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
            mdb.beginTransaction();

            try {

                Cursor cursor = mdb.query(DBConstants.KEY_CURRENCY_TABLE, new String[]{
                                DBConstants.KEY_CURRENCY_SR,DBConstants.KEY_CURRENCY_LABEL,
                                DBConstants.KEY_CURRENCY_VALUE,DBConstants.KEY_CURRENCY_FLAG,
                                DBConstants.KEY_CURRENCY_PIC}, null, null,
                        null, null, null);

                if (cursor.moveToFirst()) {

                    do {

                        currencyList.add(("₹ " + cursor.getString(2)));

                    }while (cursor.moveToNext());

                }

                cursor.close();
                mdb.setTransactionSuccessful();

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mdb.endTransaction();
            }
        }

        return ! currencyList.isEmpty();
    }

    public void showCurrencyInPortraitView(boolean flag){

        if (settleMandatory && currencyHasCreated()){

            if (flag){

                // Setting Currency Orientation for Portrait View
                layoutBillDetail.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams sett = new LinearLayout.LayoutParams(
                        0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                LinearLayout.LayoutParams curr = new LinearLayout.LayoutParams(
                        0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

                layout_bill_settle.setLayoutParams(sett);
                gridViewCurrency.setLayoutParams(curr);
                gridViewCurrency.setNumColumns(2);

            }else {

                // Setting Currency Orientation  for LandScape View
                layoutBillDetail.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams sett = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
                LinearLayout.LayoutParams curr = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 60);

                layout_bill_settle.setLayoutParams(sett);
                gridViewCurrency.setLayoutParams(curr);
                gridViewCurrency.setNumColumns(5);
            }
        }
    }


    public void flipIfPortrait(){

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            layout_flipMode.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);

            p.setMargins(2, 1, 1, 1);
            layout_billing.setLayoutParams(p);

            LinearLayout.LayoutParams p_order = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);

            p_order.setMargins(2, 1, 1, 1);
            layout_table.setLayoutParams(p_order);

            showCurrencyInPortraitView(true);

        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            layout_flipMode.setOrientation(LinearLayout.HORIZONTAL);

    }

    // on configuration changed save the instance and run flip mode
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            layout_flipMode.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.MATCH_PARENT, 2);
            p.setMargins(1, 1, 1, 1);
            layout_billing.setLayoutParams(p);

            LinearLayout.LayoutParams p_order = new LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1);
            p_order.setMargins(0, 1, 1, 1);
            layout_table.setLayoutParams(p_order);

            showCurrencyInPortraitView(false);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            layout_flipMode.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            p.setMargins(2, 1, 1, 1);
            layout_billing.setLayoutParams(p);

            LinearLayout.LayoutParams p_order = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            p_order.setMargins(2, 1, 1, 1);
            layout_table.setLayoutParams(p_order);

            showCurrencyInPortraitView(true);
        }
    }


    @Override
    public void getCashAmount(String cash, String payMode) {


        float total = Float.parseFloat(textViewTotal.getText().toString());
        float tendered = Float.parseFloat(textViewTAmt.getText().toString()) + Float.parseFloat(cash);
        float change = tendered - total;

        /*if (settleMandatory){
            relativeLayoutTable.setVisibility(View.GONE);
            layoutBillDetail.setVisibility(View.VISIBLE);
        }*/

        textviewForBIllno.setText(textViewBillNo.getText().toString());
        textviewForSubtotal.setText(textViewSubTotal.getText().toString());
        textviewForDiscount.setText(textViewDiscount.getText().toString());
        textviewForTax.setText(textViewTax.getText().toString());
        textviewForTotal.setText(textViewTotal.getText().toString());
        textViewTAmt.setText(String.valueOf(tendered));
        textViewChange.setText(String.valueOf(change));

        if ( ! cash.equals("0.0")){

            if (payMode.equals("CURR")){
                cashPaid = cashPaid + Float.parseFloat(cash);
                textViewStlMode.setText("CASH : " + cashPaid + " ");

            } else if (payMode.equals("CS")) {
                textViewStlMode.append("CASH" + " - " + cash + "\n");
                cashPaid = cashPaid + Float.parseFloat(cash);

            } else {
                textViewStlMode.append(homeItem.getCreditDesc() + " - " + cash + "\n");
                creditPaid = creditPaid + Float.parseFloat(cash);
            }
        }
    }

    @Override
    public void getCreditAmount(HomeItem homeItem) {

        this.homeItem = homeItem;
        if (layoutBillDetail.getVisibility() == View.GONE || textViewChange.getText().toString().contains("-"))
            showKeypad("CR");
    }

    @Override
    public void hideOrderVisibility() {

        gridViewShowTableBillGene.setVisibility(View.GONE);
    }

    @Override
    public void showOrderVisibility() {

        gridViewShowTableBillGene.setVisibility(View.VISIBLE);
    }


    public void showDiscountList() {
        if (gridViewShowTableBillGene.getVisibility() == View.VISIBLE) {

            gridViewShowTableBillGene.setVisibility(View.GONE);
            frameLayout_discount.addView(discountLayout.addDiscLayout());
            frameLayout_discount.setVisibility(View.VISIBLE);

        } else {
            frameLayout_discount.removeAllViews();
            frameLayout_discount.setVisibility(View.GONE);
            gridViewShowTableBillGene.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onBackPress() {

        if (layoutBillDetail.getVisibility() == View.VISIBLE) {

            layoutBillDetail.setVisibility(View.GONE);
            layoutStlMode.setVisibility(View.GONE);
            relativeLayoutTable.setVisibility(View.VISIBLE);
            return true;

        } else if (!TextUtils.isEmpty(tableNoDetails.getText())) {

            clearData();
            txtOrderDiscount.setClickable(true);
            textViewTotalCount.setText("");
            textViewTotalQuantity.setText("");
            textViewTotalPrice.setText("");
            layoutStlMode.setVisibility(View.GONE);
            return true;
        }

        return false;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    private AdapterView.OnItemClickListener OnTableClickListener() {

        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TableItem item = (TableItem) adapter_bill_gen.getItem(i);
                table = item.getName().isEmpty() ? item.getCode() : item.getName();
                tableCode = item.getCode();
                setTable();
            }
        };
    }



    public void setTable() {

        if (!table.isEmpty()) {

            textViewTotalCount.setText(getResources().getString(R.string.total_string));
            clearData();

            tableNoDetails.setText(table);
            String parameter = UtilToCreateJSON.createTableOrderJson(context, tableCode);
            String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();

            if (!(TextUtils.isEmpty(parameter) && TextUtils.isEmpty(serverIP))) {
                new OrderDetailsFetchTask(context, adapterOrderTable, parameter, serverIP,
                        progressListItem, textViewTotalQuantity, textViewTotalPrice, textViewGuestName).execute();
            }
        }/*else {

            if (posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_R))
                getTables();
        }*/
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_cash:

                showKeypad("CS");
                break;

            case R.id.tv_credit:

                CreditDetailPopup creditDetailPopup = new CreditDetailPopup(context, this);
                creditDetailPopup.show();
                break;


            case R.id.txtOrderDiscount:

                showDiscountList();
                break;

            case R.id.txtbillGenerate:

                if (adapterOrderTable.getCount() > 0) {
                    //  BluetoothPrint("Hello Kamal");
                    // BluetoothBillPrintConnect();
                    dis_code_bill = discountLayout.disc_code;
                    dis_per_bill = discountLayout.disc_per;

                    String parameter2 = UtilToCreateJSON.createBillGenerateJson(
                            context, tableCode, dis_code_bill, dis_per_bill);
                    String serverIP2 = POSApplication.getSingleton()
                            .getmDataModel().getUserInfo().getServerIP();
                    if (!(TextUtils.isEmpty(parameter2) && TextUtils
                            .isEmpty(serverIP2))) {
                        new BillGenerateTask(context, parameter2, serverIP2,
                                progressListItem, textViewBillNo, textViewTax, textViewDiscount,
                                textViewSubTotal, textViewTotal, LinerLayoutBillDetails,
                                textViewBillRemark, this).execute();
                    }

                    textViewGuest.setVisibility(View.GONE);
                    textViewGuestName.setVisibility(View.GONE);

                    txtBillSave.setVisibility(View.VISIBLE);
                    txtOrderDiscount.setClickable(false);
                    txtOrderGenerate.setVisibility(View.GONE);

                    textViewTotalCount.setText("");
                    textViewTotalQuantity.setText("");
                    textViewTotalPrice.setText("");

                } else
                    Toast.makeText(context, "Select Table First !!", Toast.LENGTH_SHORT).show();

                break;

            case R.id.txtBillSave:

                if (textViewBillNo.getText().toString().isEmpty())
                    break;

                if (disc_reason_mandat && ! textViewDiscount.getText().toString().equals("0.00") && bill_rem.isEmpty()){
                    show_bill_remark();
                    break;
                }

                if (settleMandatory) {
                    if (textViewChange.getText().toString().contains("-") || textViewStlMode.getText().toString().isEmpty()) {
                        BillSettleMandateDialog dialog = new BillSettleMandateDialog(context);
                        dialog.show();
                        break;
                    }
                }

                try {

                    String parameter = UtilToCreateJSON.createBillSaveJson(context, tableCode, bill_rem, "");
                    String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();

                    if (!(TextUtils.isEmpty(parameter) && TextUtils.isEmpty(serverIP))) {

                        SaveBillTask saveBillTask = new SaveBillTask(context, parameter, serverIP, this);
                        saveBillTask.execute();

                        layoutStlMode.setVisibility(View.GONE);
                        txtOrderDiscount.setClickable(true);
                        adapter_bill_gen.clearDataSet();
                        adapter_bill_gen.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.selectTableBillGene:
                selectTableBillGene.setVisibility(View.VISIBLE);
                getTables();
                break;


            case R.id.textViewBillRemark:
                show_bill_remark();
                break;
        }
    }

    @Override
    public void getBillGenerateResponse(String response) {

        if (response.equals("null"))
            Toast.makeText(context, "Define Bill Series first", Toast.LENGTH_SHORT).show();

        else if (showSettleMode)
            layoutStlMode.setVisibility(View.VISIBLE);

        if (settleMandatory){
            relativeLayoutTable.setVisibility(View.GONE);
            layoutBillDetail.setVisibility(View.VISIBLE);
            textviewForBIllno.setText(textViewBillNo.getText().toString());
            textviewForSubtotal.setText(textViewSubTotal.getText().toString());
            textviewForDiscount.setText(textViewDiscount.getText().toString());
            textviewForTax.setText(textViewTax.getText().toString());
            textviewForTotal.setText(textViewTotal.getText().toString());
            textViewTAmt.setText("0");
            cashPaid = 0; creditPaid = 0;
        }

        layout_bill_No.setVisibility(View.VISIBLE);
        layout_subtotal.setVisibility(View.INVISIBLE);
    }

    public void showKeypad(String payMode) {

        float total = Float.parseFloat(textViewTotal.getText().toString());
        float tendered = Float.parseFloat(textViewTAmt.getText().toString());
        float balance = total < tendered ? 0 : total - tendered;

        CustomKeypad keypad = new CustomKeypad(context, this, String.valueOf(balance), payMode);
        keypad.show();
    }

    public void show_bill_remark() {

        AlertDialog.Builder objBuilder = new AlertDialog.Builder(
                context);

        objBuilder.setTitle("Enter Bill Remark ");

        final EditText edit_t_disc = new EditText(context);

        objBuilder.setView(edit_t_disc);
        edit_t_disc.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        objBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        if (!edit_t_disc.getText().toString().isEmpty())
                            bill_rem = edit_t_disc.getText().toString();
                    }
                });

        objBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        // TODO Auto-generated method stub

                    }
                });

        final AlertDialog dialog = objBuilder.create();
        dialog.show();

    }


    public void getTables() {

        gridViewShowTableBillGene.setVisibility(View.VISIBLE);
        frameLayout_discount.setVisibility(View.GONE);
        adapter_bill_gen.clearDataSet();
        adapter_bill_gen.notifyDataSetChanged();

        if (!TextUtils.isEmpty(POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posCode)) {
            String parameter = UtilToCreateJSON.createTableJSON(context);
            String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
            new TableBillGenTask(context, adapter_bill_gen, parameter, serverIP, progressBarTableBillGen).execute();
        }
    }

    public void doBillSettle() {

        try {

            String user_id = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.USER_ID);
            String tamt = textViewTAmt.getText().toString();
            String change = textViewChange.getText().toString();

            if (!change.contains("-")) {

                String credit_code = homeItem == null ? "" : homeItem.getCreditCode();
                String credit_type = homeItem == null ? "" : homeItem.getCreditType();

                String parameter = UtilToCreateJSON.CreateSavePaidAmtJson(
                        context, textviewForBIllno.getText().toString(),
                        String.valueOf(cashPaid), credit_code, credit_type,
                        String.valueOf(creditPaid), "0", user_id);
                String serverIP = POSApplication.getSingleton()
                        .getmDataModel().getUserInfo().getServerIP();


                if (!(TextUtils.isEmpty(parameter) && TextUtils.isEmpty(serverIP))) {

                    BillSettleTask billSettleTask = new BillSettleTask(context, parameter, serverIP, this);
                    billSettleTask.execute();
                }

            } else if (!tamt.equals("0"))

                Toast.makeText(context, "Settlement Amount can't be less than Bill Amount",
                        Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getBillSettleResponse(String response) {

        Logger.i("SettlementResult", response);
        try {
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("BillSettleResult").equals("Bill Setteled"))
                Toast.makeText(context, "Bill Settled", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void responseFromServer(String response, StringBuilder taxWithTaxName) {

        boolean isSuccess = UtilToCreateJSON.BillSaveTaskResponse(context, response);

      /*  if (bill_format.equals("70")) {
            getPrintBill();
        } else
            getPrintBillWithImageLogo();*/

        if (isSuccess) {

            clearData();
            if (posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_R))
                getTables();

            if (layoutBillDetail.getVisibility() == View.VISIBLE) {

                layoutBillDetail.setVisibility(View.GONE);
                relativeLayoutTable.setVisibility(View.VISIBLE);
                doBillSettle();
            }

        } else
            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
        Logger.d("TaxDetail :: >>>", response);
    }

    public void clearData() {

        textViewBillNo.setText(bill_no);
        textViewTax.setText(taxes);
        textViewDiscount.setText(discount);
        textViewSubTotal.setText(subtotal);
        textViewTotal.setText(total);
        tableNoDetails.setText("");
        textViewGuestName.setText("");
        adapterOrderTable.clearDataSet();
        adapterOrderTable.notifyDataSetChanged();
        discountLayout.disc_code = "";
        discountLayout.disc_per = "";
        textViewChange.setText("");
        textViewStlMode.setText("");

        isGuestCopy = true ;
        isOfficeCopy = true;

        textViewGuest.setVisibility(View.VISIBLE);
        textViewGuestName.setVisibility(View.VISIBLE);
        textViewBillRemark.setVisibility(View.GONE);

        txtBillSave.setVisibility(View.GONE);
        txtOrderGenerate.setVisibility(View.VISIBLE);

        layout_bill_No.setVisibility(View.GONE);
        layout_subtotal.setVisibility(View.VISIBLE);
    }



    public void getAttributes() {

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();
        try {

            Cursor cursor = mdb.query(DBConstants.KEY_ATTRIBUTE_TABLE, new String[]{
                            DBConstants.KEY_CHECK_BILL_SETTLE,
                            DBConstants.KEY_CHECK_BILL_SETTLE_MANDATE,
                            DBConstants.KEY_DISCOUNT_REASON_MANDATORY}, null, null,
                    null, null, null);

            if (cursor.moveToFirst()) {

                showSettleMode = cursor.getString(0).equals("y");
                settleMandatory = cursor.getString(1).equals("y");
                disc_reason_mandat = cursor.getString(2).equals("y");
            }

            cursor.close();
            mdb.setTransactionSuccessful();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }
    }

}







