package com.entrada.cheekyMonkey.dynamic;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.steward.notificationUI.NotificationFragment;
import com.entrada.cheekyMonkey.ui.CustomTextview;

/**
 * Created by Tanuj.Sareen on 1/30/2015.
 */
public class GuestOrdersAdapter extends ArrayAdapter<GuestOrders> {

    private Context context;
    private int resourceId;
    private ArrayList<GuestOrders> ordersArrayList ;


    public GuestOrdersAdapter(Context context, int resourceId, ArrayList<GuestOrders> arrayList) {
        super(context, resourceId, arrayList);

        this.context = context;
        this.resourceId = resourceId;
        this.ordersArrayList = arrayList;
    }


    public void clear(){
        ordersArrayList.clear();
    }

    public void addDataSet(ArrayList<GuestOrders> items){
        ordersArrayList.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(GuestOrders item) {
        ordersArrayList.add(item);
        notifyDataSetChanged();
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

            viewHolder.layoutStatus = (LinearLayout) convertView.findViewById(R.id.layout_odr_status);
            viewHolder.img_odrstatus = (ImageView) convertView.findViewById(R.id.img_odrStatus);
            viewHolder.tv_order_no = (CustomTextview) convertView.findViewById(R.id.tv_orderNo);
            viewHolder.tv_items = (CustomTextview) convertView.findViewById(R.id.tv_items);
            viewHolder.tv_amount = (CustomTextview) convertView.findViewById(R.id.tv_amt);
            viewHolder.tv_orderStatus = (CustomTextview) convertView.findViewById(R.id.tv_orderStatus);
            viewHolder.tv_tbl = (CustomTextview) convertView.findViewById(R.id.tv_order_tbl);
           // viewHolder.tv_date = (CustomTextview) convertView.findViewById(R.id.textView6);

            convertView.setTag(viewHolder);

        } else
            viewHolder = (ItemsViewHolder) convertView.getTag();

        GuestOrders orders = ordersArrayList.get(position);

        viewHolder.tv_order_no.setText(orders.getOrderNo());
        viewHolder.tv_items.setText(orders.getItem() + "....");

        String amount = context.getResources().getString(R.string.rupees, "₹", orders.getAmount());
        viewHolder.tv_amount.setText(amount);
        viewHolder.tv_tbl.setText(orders.getTableNo());
//        viewHolder.tv_date.setText(orders.getOrder_date());
        if (Build.VERSION.SDK_INT >= 16) {
            viewHolder.img_odrstatus.setBackground(null);
        }

        String odrStatus = orders.getOrder_status();

        if (odrStatus.equals(NotificationFragment.ORDER_ACCEPTED) ||
                odrStatus.equals(NotificationFragment.ORDER_PREPARATION_STARTED) ||
                odrStatus.equals(NotificationFragment.ORDER_READY_TO_SERVE)) {

            viewHolder.layoutStatus.setBackgroundResource(R.drawable.approved_border);
            viewHolder.tv_orderStatus.setText(R.string.approved);
            viewHolder.tv_orderStatus.setTextColor(Color.parseColor("#00D938"));
            viewHolder.img_odrstatus.setImageResource(R.drawable.approve);

        } else  if (odrStatus.equals(NotificationFragment.ORDER_REJECTED_BY_GUEST) ||
                odrStatus.equals(NotificationFragment.ORDER_REJECTED_BY_STEWARD) ||
                odrStatus.equals(NotificationFragment.ORDER_REJECTED_BY_ADMIN)){

            viewHolder.layoutStatus.setBackgroundResource(R.drawable.rejected_border);
            viewHolder.tv_orderStatus.setText(R.string.rejected);
            viewHolder.tv_orderStatus.setTextColor(Color.parseColor("#FF3000"));
            viewHolder.img_odrstatus.setImageResource(R.drawable.rejected);

        }else if (odrStatus.equals(NotificationFragment.ORDER_DELIVERED)){

            viewHolder.layoutStatus.setBackgroundResource(R.drawable.approved_border);
            viewHolder.tv_orderStatus.setText(R.string.approved);
            viewHolder.tv_orderStatus.setTextColor(Color.parseColor("#00D938"));
            viewHolder.img_odrstatus.setImageResource(R.drawable.approve);

        }else if (odrStatus.equals(NotificationFragment.ORDER_UNDER_PROCESS)){

            viewHolder.layoutStatus.setBackgroundResource(R.drawable.process_border);
            viewHolder.tv_orderStatus.setText(R.string.process_string);
            viewHolder.tv_orderStatus.setTextColor(Color.parseColor("#FFB400"));
            viewHolder.img_odrstatus.setImageResource(R.drawable.process);

        }


        return convertView;
    }

    public class ItemsViewHolder {

        public LinearLayout layoutStatus;
        public CustomTextview tv_order_no, tv_items, tv_amount, tv_orderStatus, tv_tbl,tv_date;
        public ImageView img_odrstatus;
    }

}
