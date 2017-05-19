package com.entrada.cheekyMonkey.steward.billEdit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.steward.home_del.HomeItem;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;


public class DirectSettlementLayout implements OnItemClickListener {

    public GridView gridViewPendingBills;
    public PendingBillsAdapter billsAdapter;
    View tableView = null;
    LayoutInflater layoutInflater;
    Context context;

    ICallBillDetail iCallBillDetail;

    public DirectSettlementLayout(Context context, ICallBillDetail iCallBillDetail) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.iCallBillDetail = iCallBillDetail;
    }

    public View getPendingBillsPopupWindow() {

        tableView = layoutInflater.inflate(R.layout.direct_settle_layout, null);
        gridViewPendingBills = (GridView) tableView.findViewById(R.id.gv_pendingBills);

        billsAdapter = new PendingBillsAdapter(context);
        gridViewPendingBills.setAdapter(billsAdapter);
        gridViewPendingBills.setOnItemClickListener(this);

        String pos = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posCode;
        String parameter = UtilToCreateJSON.createPosJSON(pos);
        String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
        new DirectSettlementTask(context, billsAdapter, parameter, serverIP).execute();
        return tableView;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        iCallBillDetail.onClickBillNoToSettle((HomeItem) billsAdapter.getItem(position));

    }

    public void notifyDataSetChanged() {
        billsAdapter.notifyDataSetChanged();
    }

}
