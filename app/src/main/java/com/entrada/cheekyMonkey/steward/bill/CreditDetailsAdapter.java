package com.entrada.cheekyMonkey.steward.bill;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.steward.home_del.HomeItem;
import com.entrada.cheekyMonkey.steward.other.POSAdapter;
import com.entrada.cheekyMonkey.ui.CustomTextview;


/**
 * Created by csat on 21/01/2016.
 */
public class CreditDetailsAdapter extends POSAdapter<HomeItem> {

    public CreditDetailsAdapter(Context context) {

        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.guest_name_row_layout, null);
            holder = new Holder();
            holder.textviewCr = (CustomTextview) convertView.findViewById(R.id.tv_GuestName);
            convertView.setTag(holder);
        } else
            holder = (Holder) convertView.getTag();

        HomeItem homeItem = dataSet.get(position);
        holder.textviewCr.setText(homeItem.getCreditDesc());
        holder.textviewCr.setTag(holder);

        return convertView;
    }


    public static class Holder {

        CustomTextview textviewCr;
    }
}
