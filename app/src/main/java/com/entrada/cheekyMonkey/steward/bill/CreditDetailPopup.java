package com.entrada.cheekyMonkey.steward.bill;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.ICallBackFinish;
import com.entrada.cheekyMonkey.steward.home_del.HomeItem;
import com.entrada.cheekyMonkey.ui.CustomTextview;


/**
 * Created by csat on 03/07/2015.
 */
public class CreditDetailPopup extends Dialog implements AdapterView.OnItemClickListener,
        View.OnClickListener, ICallBackFinish {

    Context context;
    GridView gridViewCrdetail;
    CreditDetailsAdapter adapter;
    CustomTextview txtOk;
    ICallPaidAmount iCallPaidAmount;

    public CreditDetailPopup(Context context, ICallPaidAmount iCallPaidAmount) {
        super(context);
        this.context = context;
        this.iCallPaidAmount = iCallPaidAmount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.credit_detail_layout);

        txtOk = (CustomTextview) findViewById(R.id.txtview_ok);
        txtOk.setOnClickListener(this);
        gridViewCrdetail = (GridView) findViewById(R.id.gv_crdetail);

        adapter = new CreditDetailsAdapter(context);
        gridViewCrdetail.setAdapter(adapter);
        gridViewCrdetail.setOnItemClickListener(this);

        String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
        new CreditDetailTask(context, adapter, "", serverIP).execute();

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        iCallPaidAmount.getCreditAmount((HomeItem) adapter.getItem(position));
        dismiss();
    }

    @Override
    public void onClick(View v) {

        dismiss();
    }


    @Override
    public void onFinishDialog() {

    }
}
