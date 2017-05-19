package com.entrada.cheekyMonkey.steward.ordersplit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;
import com.entrada.cheekyMonkey.ui.CustomTextview;

import java.util.ArrayList;


/**
 * Created by csat on 28/08/2015.
 */
public class OrderSplitAdapter extends ArrayAdapter<OrderItem> {

    OrderSplitFragment orderSplitFragment;
    private Context c;
    private int layout_id;
    private ArrayList<OrderItem> order_split_list;

    public OrderSplitAdapter(Context context, int textViewResourceId,
                             ArrayList<OrderItem> objects, OrderSplitFragment orderSplitFragment) {
        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub

        this.c = context;
        this.layout_id = textViewResourceId;
        this.order_split_list = objects;
        this.orderSplitFragment = orderSplitFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        // return super.getView(position, convertView, parent);

        OrderSplitViewHolder holder;
        if (convertView == null) {

            holder = new OrderSplitViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(c);
            convertView = mInflater.inflate(layout_id, null);

            holder.im_b = (ImageButton) convertView
                    .findViewById(R.id.imViewItemAdd);
            holder.t_odr = (CustomTextview) convertView
                    .findViewById(R.id.tv_ordr_no);
            holder.t_code = (CustomTextview) convertView
                    .findViewById(R.id.textViewForOrderNo);
            holder.t_name = (CustomTextview) convertView
                    .findViewById(R.id.textViewOrdersplitItemName);
            holder.t_qntity = (CustomTextview) convertView
                    .findViewById(R.id.textViewForQty);

            UserInfo info = POSApplication.getSingleton().getmDataModel().getUserInfo();
            holder.im_b.setBackgroundColor(info.getBackground());

            holder.im_b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    orderSplitFragment.onclick_remove_1(v);
                }
            });
            convertView.setTag(holder);

        } else {

            holder = (OrderSplitViewHolder) convertView.getTag();
        }

        holder.obj_class = order_split_list.get(position);

        holder.t_odr.setText(holder.obj_class.getOrderNo());
        holder.t_code.setText(holder.obj_class.getO_code());
        holder.t_name.setText(holder.obj_class.getO_name());
        holder.t_qntity.setText(String.valueOf(holder.obj_class.getO_quantity()));

        holder.im_b.setTag(holder.obj_class);
        holder.t_odr.setTag(holder.obj_class);
        holder.t_code.setTag(holder.obj_class);
        holder.t_name.setTag(holder.obj_class);
        holder.t_qntity.setTag(holder.obj_class);
        return convertView;
    }

    public static class OrderSplitViewHolder {

        OrderItem obj_class;

        ImageButton im_b;
        CustomTextview t_odr, t_code, t_name, t_qntity;

    }
}
