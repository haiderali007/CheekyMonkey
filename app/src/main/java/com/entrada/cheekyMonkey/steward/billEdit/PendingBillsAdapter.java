package com.entrada.cheekyMonkey.steward.billEdit;

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
public class PendingBillsAdapter extends POSListAdapter<HomeItem> {


    public PendingBillsAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HomeViewHolder holder;
        if (convertView == null) {
            holder = new HomeViewHolder();
            convertView = inflater.inflate(R.layout.guest_name_row_layout, null);
            holder.textViewBillNo = (CustomTextview) convertView.findViewById(R.id.tv_GuestName);

            convertView.setTag(holder);
        } else
            holder = (HomeViewHolder) convertView.getTag();


        HomeItem homeItem = dataSet.get(position);
        holder.textViewBillNo.setText(homeItem.getBillNoWithGuest());
        holder.textViewBillNo.setTag(holder);

        return convertView;
    }

    public class HomeViewHolder {

        public CustomTextview textViewBillNo;
    }

}