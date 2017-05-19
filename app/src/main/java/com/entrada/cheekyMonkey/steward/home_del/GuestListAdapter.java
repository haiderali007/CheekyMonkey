package com.entrada.cheekyMonkey.steward.home_del;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.adapter.POSListAdapter;
import com.entrada.cheekyMonkey.takeorder.entity.OrderDetail;
import com.entrada.cheekyMonkey.ui.CustomTextview;


/**
 * Created by CSATSPL on 22/12/2015.
 */
public class GuestListAdapter extends POSListAdapter<HomeItem> {


    private OrderDetail orderDetail = new OrderDetail();

    public GuestListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HomeViewHolder holder;
        if (convertView == null) {
            holder = new HomeViewHolder();
            convertView = inflater.inflate(R.layout.guest_name_row_layout, null);
            holder.textViewGuestName = (CustomTextview) convertView.findViewById(R.id.tv_GuestName);

            convertView.setTag(holder);
        } else
            holder = (HomeViewHolder) convertView.getTag();


        HomeItem homeItem = dataSet.get(position);
        holder.textViewGuestName.setText(homeItem.getGuest());
        holder.textViewGuestName.setTag(holder);

        return convertView;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    @Override
    public void clearDataSet() {
        super.clearDataSet();

        OrderDetail detail = this.getOrderDetail();
        detail.setHomeItem(new HomeItem());
        this.setOrderDetail(detail);
    }

    @Override
    public void clearDataSetALL() {
        super.clearDataSetALL();
        this.setOrderDetail(new OrderDetail());
    }

    public class HomeViewHolder {

        public CustomTextview textViewGuestName;
    }

}