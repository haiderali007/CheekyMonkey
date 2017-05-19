package com.entrada.cheekyMonkey.steward;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.takeorder.adapter.TakeOrderAdapter;
import com.entrada.cheekyMonkey.ui.CustomButton;


/**
 * Created by csat on 03/07/2015.
 */
public class SubQtyPopup extends Dialog implements View.OnClickListener {

    Context context;
    EditText txtSubQty;
    CustomButton txtCancel, txtOk;
    TakeOrderAdapter takeOrderAdapter;


    public SubQtyPopup(Context context, TakeOrderAdapter takeOrderAdapter) {
        super(context);
        this.context = context;
        this.takeOrderAdapter = takeOrderAdapter;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.sub_unit_layout);
        txtSubQty = (EditText) findViewById(R.id.textView_subQty);
        txtCancel = (CustomButton) findViewById(R.id.txtCancelSubUnit);
        txtOk = (CustomButton) findViewById(R.id.txtOkSubUnit);
        txtCancel.setOnClickListener(this);
        txtOk.setOnClickListener(this);
        setCancelable(false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txtOkSubUnit:

                String subQty = txtSubQty.getText().toString();

                if (!subQty.isEmpty()) {
                    int itemPosition = takeOrderAdapter.getCount() -1;
                    takeOrderAdapter.updateQty(itemPosition, Float.parseFloat(subQty));
                    takeOrderAdapter.notifyDataSetChanged();
                    dismiss();
                }

                break;

            case R.id.txtCancelSubUnit:
                dismiss();
        }
    }

}