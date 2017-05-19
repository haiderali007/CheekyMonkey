package com.entrada.cheekyMonkey.steward.bill;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.adapter.POSListAdapter;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.ui.CustomTextview;


/**
 * Created by kamal on 15/06/2015.
 */
public class AdapterOrderTable extends POSListAdapter<OrderTable> {


    public AdapterOrderTable(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderViewHolder holder;
        if (convertView == null) {
            holder = new OrderViewHolder();
            convertView = inflater.inflate(R.layout.order_table_layout, null);
            holder.textOrderNo = (CustomTextview) convertView.findViewById(R.id.textOrderNo);
            holder.textViewItemName = (CustomTextview) convertView.findViewById(R.id.textViewItemName);
            holder.textViewQuantity = (CustomTextview) convertView.findViewById(R.id.textViewQuantity);
            holder.textViewPrice = (CustomTextview) convertView.findViewById(R.id.textViewPrice);

            convertView.setTag(holder);
        } else
            holder = (OrderViewHolder) convertView.getTag();

        UserInfo userInfo = POSApplication.getSingleton().getmDataModel().getUserInfo();

        int font_color = userInfo.getFontColor();
        holder.textOrderNo.setTextColor(font_color);
        holder.textViewItemName.setTextColor(font_color);
        holder.textViewQuantity.setTextColor(font_color);
        holder.textViewPrice.setTextColor(font_color);


/*
        float size = userInfo.getTextSize();
        size = size - 2;
        holder.textOrderNo.setTextSize(size);
        holder.textViewItemName.setTextSize(size);
        holder.textViewQuantity.setTextSize(size);
        holder.textViewPrice.setTextSize(size);
*/


        OrderTable tableItem = dataSet.get(position);
        holder.textOrderNo.setText(tableItem.getOrder_no());
        holder.textViewItemName.setText(tableItem.getName());
        holder.textViewQuantity.setText(tableItem.getqty());
        holder.textViewPrice.setText(tableItem.getItem_price());


        holder.textOrderNo.setTag(holder);
        holder.textViewItemName.setTag(holder);
        holder.textViewQuantity.setTag(holder);
        holder.textViewPrice.setTag(holder);
        return convertView;
    }


    public class OrderViewHolder {

        public CustomTextview textOrderNo, textViewItemName, textViewQuantity, textViewPrice;

    }
}


