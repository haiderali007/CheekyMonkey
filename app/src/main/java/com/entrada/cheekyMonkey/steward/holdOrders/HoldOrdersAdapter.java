package com.entrada.cheekyMonkey.steward.holdOrders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.adapter.POSListAdapter;
import com.entrada.cheekyMonkey.steward.home_del.HomeItem;
import com.entrada.cheekyMonkey.ui.CustomTextview;


/**
 * Created by CSATSPL on 22/12/2015.
 */
public class HoldOrdersAdapter extends POSListAdapter<HomeItem> {


    public HoldOrdersAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HomeViewHolder holder;
        if (convertView == null) {

            holder = new HomeViewHolder();
            convertView = inflater.inflate(R.layout.hold_orders_row_layout, null);
            holder.tv_mobile = (CustomTextview) convertView.findViewById(R.id.tv_mobile_no);
            holder.tv_gst_name = (CustomTextview) convertView.findViewById(R.id.gst_name);
            holder.textViewOrderNo = (CustomTextview) convertView.findViewById(R.id.tv_order_num);

            convertView.setTag(holder);
        } else
            holder = (HomeViewHolder) convertView.getTag();


        HomeItem homeItem = dataSet.get(position);

        holder.tv_mobile.setText(homeItem.getGuestId());
        holder.tv_gst_name.setText(homeItem.getGuestName());
        holder.textViewOrderNo.setText(homeItem.getBILL_NO());

        holder.tv_mobile.setTag(holder);
        holder.tv_gst_name.setTag(holder);
        holder.textViewOrderNo.setTag(holder);

        return convertView;
    }

    public class HomeViewHolder {

        public CustomTextview tv_mobile, tv_gst_name, textViewOrderNo;
    }

}