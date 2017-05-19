package com.entrada.cheekyMonkey.steward;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.ICallBackFinish;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.ui.CustomTextview;

import java.util.ArrayList;



/**
 * Created by csat on 03/07/2015.
 */
public class OrderRemarkPopup extends Dialog implements View.OnClickListener, ICallBackFinish {

    Context context;
    RadioGroup radioGroup;
    EditText editText_for_item_remark_obj;
    Spinner sp;
    RadioButton r1, r2;
    ArrayList<String> order_remark_list = new ArrayList<String>();
    CustomTextview txtSaveOrderRemark, txtCancelOrderRemark;
    ArrayAdapter<String> order_remark_adapter;
    StewardOrderFragment takeOrderFragment;

    public OrderRemarkPopup(Context context, StewardOrderFragment takeOrderFragment) {
        super(context);
        this.context = context;
        this.takeOrderFragment = takeOrderFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order_remark_layout_popup);
        init();
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        r1 = (RadioButton) findViewById(R.id.radioButton1);
        r2 = (RadioButton) findViewById(R.id.radioButton2);

        sp = (Spinner) findViewById(R.id.spinner_for_item_remark_list);
        editText_for_item_remark_obj = (EditText) findViewById(R.id.edit_text_for_item_remark);
        txtSaveOrderRemark = (CustomTextview) findViewById(R.id.txtSaveOrderRemark);
        txtCancelOrderRemark = (CustomTextview) findViewById(R.id.txtCancelOrderRemark);
        txtSaveOrderRemark.setOnClickListener(this);
        txtCancelOrderRemark.setOnClickListener(this);

        if (!order_remark_adapter.isEmpty()) {
            order_remark_adapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            sp.setAdapter(order_remark_adapter);
        } else {

            sp.setVisibility(View.GONE);
            r1.setVisibility(View.GONE);
        }


    }


    public void init() {
        SQLiteDatabase mdb = POSDatabase.getMenuItem(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        try {
            Cursor order_remark = getAllRecords_ORDER_REMARK(mdb);
            if (order_remark != null && order_remark.moveToFirst()) {
                do {
                    order_remark_list.add(order_remark.getString(1));

                } while (order_remark.moveToNext());

            }
            order_remark_adapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_dropdown_item,
                    order_remark_list);

            order_remark_list = null;
            mdb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

    }

    public Cursor getAllRecords_ORDER_REMARK(SQLiteDatabase mdb) {

        return mdb.query(DBConstants.KEY_ORDER_REMARK_TABLE, new String[]{
                        DBConstants.KEY_ORDER_REMARK_CODE, DBConstants.KEY_ORDER_REMARK_DESC}, null, null,
                null, null, null);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txtSaveOrderRemark:
                if (r1.isChecked()) {

                    takeOrderFragment.orderRemark = sp.getSelectedItem().toString();
                    Toast.makeText(context,"order remark saved", Toast.LENGTH_SHORT).show();
                    dismiss();

                } else if (r2.isChecked()
                        && editText_for_item_remark_obj.getText().toString()
                        .length() != 0) {
                    takeOrderFragment.orderRemark = editText_for_item_remark_obj.getText()
                            .toString();
                    Toast.makeText(context,"order remark saved", Toast.LENGTH_SHORT).show();
                    dismiss();
                }

                break;

            case R.id.txtCancelOrderRemark:

                dismiss();

                break;
        }

    }

    @Override
    public void onFinishDialog() {

    }
}
