package com.entrada.cheekyMonkey.steward.ordersplit;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.adapter.POSListAdapter;
import com.entrada.cheekyMonkey.entity.UserInfo;


/**
 * Created by kamal on 05/06/2015.
 */
public class TableStatusAdapter extends POSListAdapter<TableStatus> {


    public TableStatusAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TableViewHolder holder;
        if (convertView == null) {
            holder = new TableViewHolder();

            convertView = inflater.inflate(R.layout.table_status_list, null);

            holder.table_l = (TextView) convertView
                    .findViewById(R.id.t_v_table_for_t_s_l);
            holder.order_l = (TextView) convertView
                    .findViewById(R.id.t_v_order_for_t_s_l);
            holder.quanity_l = (TextView) convertView
                    .findViewById(R.id.t_v_qnty_for_t_s_l);
            holder.item_name_l = (TextView) convertView
                    .findViewById(R.id.t_v_item_name_for_t_s_l);
            holder.cover_l = (TextView) convertView
                    .findViewById(R.id.t_v_cover_for_t_s_l);
            holder.time_l = (TextView) convertView
                    .findViewById(R.id.t_v_time_for_t_s_l);
            holder.stwrd_l = (TextView) convertView
                    .findViewById(R.id.t_v_stewrd_for_t_s_l);
            convertView.setTag(holder);
        } else
            holder = (TableViewHolder) convertView.getTag();

        UserInfo userInfo = POSApplication.getSingleton().getmDataModel().getUserInfo();

        if (!TextUtils.isEmpty(userInfo.getTheme_Font_Color())) {

            holder.table_l.setTextColor(Color.parseColor(userInfo.getTheme_Font_Color()));
            holder.order_l.setTextColor(Color.parseColor(userInfo.getTheme_Font_Color()));
            holder.quanity_l.setTextColor(Color.parseColor(userInfo.getTheme_Font_Color()));
            holder.item_name_l.setTextColor(Color.parseColor(userInfo.getTheme_Font_Color()));
            holder.cover_l.setTextColor(Color.parseColor(userInfo.getTheme_Font_Color()));
            holder.time_l.setTextColor(Color.parseColor(userInfo.getTheme_Font_Color()));
            holder.stwrd_l.setTextColor(Color.parseColor(userInfo.getTheme_Font_Color()));
        }

        if (!TextUtils.isEmpty(userInfo.getText_Size())) {

            float size = Float.parseFloat(userInfo.getText_Size());
            holder.table_l.setTextSize(size);
            holder.order_l.setTextSize(size);
            holder.quanity_l.setTextSize(size);
            holder.item_name_l.setTextSize(size);
            holder.cover_l.setTextSize(size);
            holder.time_l.setTextSize(size);
            holder.stwrd_l.setTextSize(size);
        }

        TableStatus tableCurrent = dataSet.get(position);
        String quantity[] = tableCurrent.getQnty_l().split(".0");

        if (position == 0) {

            holder.table_l.setText(tableCurrent.getTable_l());
            holder.order_l.setText(tableCurrent.getOrder_l());
            holder.quanity_l.setText(quantity[0]);
            holder.item_name_l.setText(tableCurrent.getItem_Name_l());
            holder.cover_l.setText(tableCurrent.getCover_l());
            holder.time_l.setText(tableCurrent.getTime_l());
            holder.stwrd_l.setText(tableCurrent.getStwrd_l());
        } else {

            TableStatus tablePrevious = dataSet.get(position - 1);
            String previousTable = tablePrevious.getTable_l();
            String selectedTable = tableCurrent.getTable_l();

            String previousOrder = tablePrevious.getOrder_l();
            String selectedOrder = tableCurrent.getOrder_l();

            if (selectedTable.equalsIgnoreCase(previousTable)) {

                holder.table_l.setText("");
                holder.cover_l.setText("");
                if (selectedOrder.equalsIgnoreCase(previousOrder)) {
                    holder.order_l.setText("");
                } else holder.order_l.setText(tableCurrent.getOrder_l());

            } else {

                holder.table_l.setText(tableCurrent.getTable_l());
                holder.cover_l.setText(tableCurrent.getCover_l());
                holder.order_l.setText(tableCurrent.getOrder_l());
            }


            holder.item_name_l.setText(tableCurrent.getItem_Name_l());
            holder.quanity_l.setText(quantity[0]);
            holder.stwrd_l.setText(tableCurrent.getStwrd_l());
            holder.time_l.setText(tableCurrent.getTime_l());
        }


        holder.table_l.setTag(tableCurrent);
        holder.order_l.setTag(tableCurrent);
        holder.quanity_l.setTag(tableCurrent);
        holder.item_name_l.setTag(tableCurrent);
        holder.stwrd_l.setTag(tableCurrent);


        return convertView;
    }

    public class TableViewHolder {

        TextView table_l, order_l, quanity_l, item_name_l, cover_l, time_l,
                stwrd_l;
    }
}