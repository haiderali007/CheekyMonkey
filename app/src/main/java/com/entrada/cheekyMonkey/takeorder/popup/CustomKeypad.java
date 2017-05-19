package com.entrada.cheekyMonkey.takeorder.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.steward.bill.ICallPaidAmount;
import com.entrada.cheekyMonkey.takeorder.adapter.TakeOrderAdapter;
import com.entrada.cheekyMonkey.ui.CustomTextview;


/**
 * Created by Rahul on 03/10/2015.
 */
public class CustomKeypad extends Dialog {

    CustomTextview txtQty;
    GridView gridView;
    ArrayAdapter<String> arrayAdapter;
    String[] strings = {"1", "2", "3", "Clear", "4", "5", "6", "Del", "7", "8", "9", "Close",
            ".", "0", "00", "OK", ".125", ".250", ".500", ".750"};
    Context context;
    View v;
    TakeOrderAdapter takeOrderAdapter;
    String qty = "";
    int index;
    ICallPaidAmount iCallPaidAmount;
    String payMode = "";

    public CustomKeypad(Context context, View view, int index, TakeOrderAdapter takeOrderAdapter) {
        super(context);
        this.context = context;
        v = view;
        this.takeOrderAdapter = takeOrderAdapter;
        this.index = index;
    }


    public CustomKeypad(Context context, ICallPaidAmount iCallPaidAmount, String total, String payMode) {
        super(context);
        this.context = context;
        this.iCallPaidAmount = iCallPaidAmount;
        this.qty = total;
        this.payMode = payMode;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.custom_keypad_popup);
        gridView = (GridView) findViewById(R.id.gridViewShowDigit);
        txtQty = (CustomTextview) findViewById(R.id.textViewQty);
        txtQty.setText(qty);

        arrayAdapter = new ArrayAdapter<>(context, R.layout.calculator_item, strings);
        gridView.setAdapter(arrayAdapter);
        gridView.setOnItemClickListener(OnItemClickListener());

    }


    private AdapterView.OnItemClickListener OnItemClickListener() {

        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String item = arrayAdapter.getItem(position);

                if (position != 19 && position % 4 == 3) {
                    if (position == 3)
                        qty = "";

                    else if (position == 7 && txtQty.length() != 0)
                        qty = qty.substring(0, qty.length() - 1);

                    else if (position == 11) {
                        qty = "";
                        dismiss();

                    } else if (position == 15 && qty.length() > 0 && !qty.equals(".")) {

                        if (v != null)
                            takeOrderAdapter.updateQty(index, Float.parseFloat(qty));

                        dismiss();
                    }

                } else if (item.contains(".")) {

                    if (!qty.contains("."))
                        qty = qty + item;

                } else if ((item.contains("0") && qty.length() != 0) || position < 11)
                    qty = qty + item;

                txtQty.setText(qty);
            }
        };
    }
}