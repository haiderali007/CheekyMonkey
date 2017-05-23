package com.entrada.cheekyMonkey.steward.notificationUI;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.adapter.POSListAdapter;
import com.entrada.cheekyMonkey.entity.GuestOrderItem;
import com.entrada.cheekyMonkey.entity.GusetOrderDetail;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.ui.CustomTextview;

import java.util.Locale;


public class GstOrDetailAdp<T> extends POSListAdapter<T> {

    public GuestOrderItem item = new GuestOrderItem();

    public GstOrDetailAdp(Context context) {
        super(context);

    }

    public GuestOrderItem getItem() {
        return item;
    }

    public void setItem(GuestOrderItem item) {
        this.item = item;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        GstHeaderVHolder holder;
        if (v == null) {
            holder = new GstHeaderVHolder();
            v = inflater.inflate(R.layout.guest_or_detail_row, parent, false);
            holder.llDRowSelector = (LinearLayout) v
                    .findViewById(R.id.llDRowSelector);
            holder.txtName = (CustomTextview) v.findViewById(R.id.txtName);
            holder.txtQty = (CustomTextview) v.findViewById(R.id.txtQty);
            holder.txtprice = (CustomTextview) v.findViewById(R.id.txtprice);
            v.setTag(holder);
        } else
            holder = (GstHeaderVHolder) v.getTag();

        Object object = getItem(position);
        if (object instanceof GusetOrderDetail) {
            GusetOrderDetail item = (GusetOrderDetail) object;
            holder.txtName.setText(item.ItemName.isEmpty() ? UserInfo.getMixerName(item.ItemCode) : item.ItemName);
            holder.txtQty.setText(String.format(Locale.US, "%.1f", Float.parseFloat(item.ItemQty)));

            float amt = Float.parseFloat(item.ItemQty) * Float.parseFloat(item.ItemPrice);
            holder.txtprice.setText(String.format(Locale.US, "%.2f", amt));

            if (position == getIsSelectedPosition())
                holder.llDRowSelector.setSelected(true);
            else
                holder.llDRowSelector.setSelected(false);
        }

        return v;
    }

    public class GstHeaderVHolder {

        public LinearLayout llDRowSelector;
        public CustomTextview txtName, txtQty, txtprice;
    }

}