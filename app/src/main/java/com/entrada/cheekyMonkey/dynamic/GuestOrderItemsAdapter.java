package com.entrada.cheekyMonkey.dynamic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.ui.CustomTextview;

/**
 * Created by Tanuj.Sareen on 1/30/2015.
 */
public class GuestOrderItemsAdapter extends ArrayAdapter<GuestOrders> {

    private Context context;
    private int resourceId;
    private ArrayList<GuestOrders> ordersArrayList ;


    public GuestOrderItemsAdapter(Context context, int resourceId, ArrayList<GuestOrders> arrayList) {
        super(context, resourceId, arrayList);

        this.context = context;
        this.resourceId = resourceId;
        this.ordersArrayList = arrayList;
    }


    public void clear(){
        ordersArrayList.clear();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getCount() {
        return ordersArrayList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemsViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ItemsViewHolder();
            convertView = LayoutInflater.from(context).inflate(resourceId, null);

            viewHolder.tv_items = (CustomTextview) convertView.findViewById(R.id.textview_item);
            viewHolder.tv_qty = (CustomTextview) convertView.findViewById(R.id.textview_qty);
            viewHolder.tv_price = (CustomTextview) convertView.findViewById(R.id.textview_price);

            convertView.setTag(viewHolder);

        } else
            viewHolder = (ItemsViewHolder) convertView.getTag();

        GuestOrders orders = ordersArrayList.get(position);

        viewHolder.tv_items.setText(orders.getItem());
        viewHolder.tv_price.setText(orders.getPrice());
        viewHolder.tv_qty.setText(orders.getQty());

        return convertView;
    }


    public class ItemsViewHolder {

        public LinearLayout layoutStatus;
        public CustomTextview tv_order_no, tv_items, tv_price, tv_orderStatus, tv_qty;
        public ImageView img_odrstatus;
    }

}
