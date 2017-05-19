package com.entrada.cheekyMonkey.steward.home_del;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.adapter.POSListAdapter;
import com.entrada.cheekyMonkey.ui.CustomTextview;


/**
 * Created by CSATSPL on 22/12/2015.
 */
public class DelLogAdapter extends POSListAdapter<HomeItem> {


    public DelLogAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if (convertView == null) {

            holder = new Holder();
            convertView = inflater.inflate(R.layout.del_log_adapter_layout, null);
            holder.tv_Pos = (CustomTextview) convertView.findViewById(R.id.tv_log_pos);
            holder.tv_billNo = (CustomTextview) convertView.findViewById(R.id.tv_log_bill);
            holder.tv_amt = (CustomTextview) convertView.findViewById(R.id.tv_log_amt);
            holder.tv_guest = (CustomTextview) convertView.findViewById(R.id.tv_log_guest);
            holder.tv_OdrTime = (CustomTextview) convertView.findViewById(R.id.tv_odr_time);
            holder.tv_delBoy = (CustomTextview) convertView.findViewById(R.id.tv_log_delBoy);
            holder.tv_strTime = (CustomTextview) convertView.findViewById(R.id.tv_log_startTime);
            holder.tv_del_time = (CustomTextview) convertView.findViewById(R.id.tv_log_del_time);
            holder.tv_retTime = (CustomTextview) convertView.findViewById(R.id.tv_log_retTime);
            holder.tvchangAmt = (CustomTextview) convertView.findViewById(R.id.tv_log_changeAmt);
            holder.tv_retAmt = (CustomTextview) convertView.findViewById(R.id.tv_log_retAmt);


            convertView.setTag(holder);
        } else
            holder = (Holder) convertView.getTag();


        HomeItem homeItem = dataSet.get(position);


        holder.tv_Pos.setText(homeItem.getPos());
        holder.tv_billNo.setText(homeItem.getBillNo());
        holder.tv_delBoy.setText(homeItem.getDelBoyName());
        holder.tv_OdrTime.setText(homeItem.getOdrTime());
        holder.tv_strTime.setText(homeItem.getStrTime());
        holder.tv_del_time.setText(homeItem.getDel_time());
        holder.tv_retTime.setText(homeItem.getRetTime());
        holder.tv_amt.setText(homeItem.getAmt());
        holder.tvchangAmt.setText(homeItem.getChangAmt());
        holder.tv_retAmt.setText(homeItem.getRetAmt());
        holder.tv_guest.setText(homeItem.getGuestName());


        setUp(holder);

        return convertView;
    }

    public void setUp(Holder holder) {


        holder.tv_Pos.setTag(holder);
        holder.tv_billNo.setTag(holder);
        holder.tv_delBoy.setTag(holder);
        holder.tv_OdrTime.setTag(holder);
        holder.tv_strTime.setTag(holder);
        holder.tv_del_time.setTag(holder);
        holder.tv_retTime.setTag(holder);
        holder.tv_amt.setTag(holder);
        holder.tvchangAmt.setTag(holder);
        holder.tv_retAmt.setTag(holder);
        holder.tv_guest.setTag(holder);

    }

    public class Holder {

        public CustomTextview tv_Pos, tv_billNo, tv_delBoy,
                tv_OdrTime, tv_strTime, tv_del_time, tv_retTime, tv_amt,
                tvchangAmt, tv_retAmt, tv_guest;
    }

}