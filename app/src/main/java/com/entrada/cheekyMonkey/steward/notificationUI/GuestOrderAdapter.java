package com.entrada.cheekyMonkey.steward.notificationUI;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.adapter.POSListAdapter;
import com.entrada.cheekyMonkey.entity.GuestOrderItem;
import com.entrada.cheekyMonkey.ui.CustomTextview;


public class GuestOrderAdapter<T> extends POSListAdapter<T> {

    public GuestOrderAdapter(Context context) {
        super(context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        GstHeaderVHolder holder;
        if (v == null) {
            holder = new GstHeaderVHolder();
            v = inflater.inflate(R.layout.guest_order_layout, parent, false);
            holder.llRowSelector = (LinearLayout) v
                    .findViewById(R.id.llRowSelector);
            holder.txtTableNo = (CustomTextview) v
                    .findViewById(R.id.txtTableNoNo);
            holder.txtOrderNo = (CustomTextview) v
                    .findViewById(R.id.txtOrderNo);
            holder.txtTotalItem = (CustomTextview)
                    v.findViewById(R.id.txtItem);
            holder.txtTotalQty = (CustomTextview) v
                    .findViewById(R.id.txtQty);
            v.setTag(holder);
        } else
            holder = (GstHeaderVHolder) v.getTag();

        Object object = getItem(position);

        if (object instanceof GuestOrderItem) {
            GuestOrderItem item = (GuestOrderItem) object;
            holder.txtTableNo.setText(item.TABLE);
            holder.txtOrderNo.setText(item.ORDER_NUM);
            holder.txtTotalItem.setText(item.ITEM);
            holder.txtTotalQty.setText(item.QTY.substring(0, item.QTY.indexOf('.')));

            if (position == getIsSelectedPosition())
                holder.llRowSelector.setSelected(true);
            else
                holder.llRowSelector.setSelected(false);
        }

        return v;
    }

    public class GstHeaderVHolder {

        public LinearLayout llRowSelector;
        public CustomTextview txtTableNo, txtOrderNo, txtTotalItem, txtTotalQty;
    }

}