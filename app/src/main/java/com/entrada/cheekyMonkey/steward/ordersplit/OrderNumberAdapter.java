package com.entrada.cheekyMonkey.steward.ordersplit;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.adapter.POSListAdapter;
import com.entrada.cheekyMonkey.ui.CustomTextview;


/**
 * Created by ${Tanuj.Sareen} on 07/03/2015.
 */
public class OrderNumberAdapter extends POSListAdapter<String> {


    public OrderNumberAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderNumberViewHolder holder;
        if (convertView == null) {
            holder = new OrderNumberViewHolder();
            convertView = inflater.inflate(R.layout.order_no_row_layout, null);
            holder.textViewOrderNumber = (CustomTextview) convertView.findViewById(R.id.textViewOrderNumber);

            convertView.setTag(holder);
        } else
            holder = (OrderNumberViewHolder) convertView.getTag();

        holder.textViewOrderNumber.setText(dataSet.get(position));
        holder.textViewOrderNumber.setTag(holder);

        return convertView;
    }

    public class OrderNumberViewHolder {

        public CustomTextview textViewOrderNumber;
    }
}
