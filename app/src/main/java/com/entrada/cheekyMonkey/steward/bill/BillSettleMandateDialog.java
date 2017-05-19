package com.entrada.cheekyMonkey.steward.bill;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.ICallBackFinish;


public class BillSettleMandateDialog extends Dialog {

    Context context;
    ICallBackFinish callBackFinish;

    public BillSettleMandateDialog(Context context) {
        super(context);
        this.context = context;

    }


    public BillSettleMandateDialog(Context context, ICallBackFinish callBackFinish) {
        super(context);
        this.callBackFinish = callBackFinish;
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bill_stl_mandate_popup);

        Button ok = (Button) findViewById(R.id.ok_btn);

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
