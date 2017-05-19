package com.entrada.cheekyMonkey.steward.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.steward.ordersplit.OrderNumberAdapter;


public class OrderNumberLayout implements OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    public GridView grdVwOrderNumber;
    View tableView = null;
    LayoutInflater layoutInflater;
    ProgressBar progressBarTable;
    Context context;
    int width, heightv;
    ICallBackOrderNumber iCallBackOrderNumber;
    String flag = "";
    private OrderNumberAdapter adapter;

    public OrderNumberLayout(Context context, int width,
                             ICallBackOrderNumber iCallBackOrderNumber) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.width = width;
        this.iCallBackOrderNumber = iCallBackOrderNumber;
    }

    public View getOrderNumberPopupWindow(String parameter, String serverIP, String flag) {

        tableView = layoutInflater.inflate(R.layout.order_number_popup_layout, null);

        this.flag = flag;
        grdVwOrderNumber = (GridView) tableView.findViewById(R.id.gridViewOrderNO);
        progressBarTable = (ProgressBar) tableView.findViewById(R.id.progressBarTable);

        adapter = new OrderNumberAdapter(context);
        grdVwOrderNumber.setAdapter(adapter);
        grdVwOrderNumber.setOnItemClickListener(this);
        grdVwOrderNumber.setOnItemLongClickListener(this);
        new OrderNumberTask(context, adapter, parameter, serverIP, progressBarTable).execute();
        return tableView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        // TableGridAdapter.ViewHandler holder = (ViewHandler) view.getTag();
        if (flag.isEmpty() || flag.equals("M"))
            iCallBackOrderNumber.modifyOrder(adapter.getItem(position).toString());

        else if (flag.equals("C"))
            iCallBackOrderNumber.cancelOrder(adapter.getItem(position).toString());

    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {

        iCallBackOrderNumber.cancelOrder(adapter.getItem(position).toString());

        return false;
    }
}
