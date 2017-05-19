package com.entrada.cheekyMonkey.steward.holdOrders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.steward.billEdit.ICallBillDetail;
import com.entrada.cheekyMonkey.steward.home_del.HomeItem;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;


public class HoldOrdersLayout implements OnItemClickListener {

    public ListView listViewOrders;
    public HoldOrdersAdapter ordersAdapter;
    public CustomTextview textviewForOrderno, textviewForDiscount, textviewForSubtotal,
            textviewForTax, textviewForTotal;
    View tableView;
    Context context;
    ICallBillDetail iCallBillDetail;
    LayoutInflater layoutInflater;


    public HoldOrdersLayout(Context context, ICallBillDetail iCallBillDetail) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.iCallBillDetail = iCallBillDetail;
    }

    public View getHoldOrdersPopupWindow() {

        tableView = layoutInflater.inflate(R.layout.hold_orders_layout, null);

        listViewOrders = (ListView) tableView.findViewById(R.id.lv_hold_orders);
        ordersAdapter = new HoldOrdersAdapter(context);
        listViewOrders.setAdapter(ordersAdapter);
        listViewOrders.setOnItemClickListener(this);

        textviewForOrderno = (CustomTextview) tableView.findViewById(R.id.tv_Order_no);
        textviewForDiscount = (CustomTextview) tableView.findViewById(R.id.tv__Discount);
        textviewForSubtotal = (CustomTextview) tableView.findViewById(R.id.tv_Subtotal);
        textviewForTax = (CustomTextview) tableView.findViewById(R.id.tv_Tax);
        textviewForTotal = (CustomTextview) tableView.findViewById(R.id.tv_Total);

        init();
        return tableView;
    }

    public void init() {

        String pos = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posCode;
        String parameter = UtilToCreateJSON.createHoldOrderJson(context, pos);
        String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
        new FetchHoldOrdersTask(context, ordersAdapter, parameter, serverIP).execute();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        HomeItem homeItem = (HomeItem) ordersAdapter.getItem(position);
        iCallBillDetail.onClickHoldOrderNo(homeItem, position);
    }


    public void setDetail(HomeItem homeItem){

        textviewForOrderno.setText(homeItem.getBILL_NO());
        textviewForDiscount.setText(homeItem.getDISCOUNT().substring(0, homeItem.getDISCOUNT().indexOf('.') + 3));
        textviewForSubtotal.setText(homeItem.getSUBTOTAL().substring(0, homeItem.getSUBTOTAL().indexOf('.') + 3));
        textviewForTax.setText(homeItem.getTAXES().substring(0, homeItem.getTAXES().indexOf('.') + 3));
        textviewForTotal.setText(homeItem.getTOTAL().substring(0, homeItem.getTOTAL().indexOf('.') + 3));
    }

    public void clearDetail() {

        textviewForOrderno.setText("");
        textviewForDiscount.setText("");
        textviewForSubtotal.setText("");
        textviewForTax.setText("");
        textviewForTotal.setText("");
    }
}
