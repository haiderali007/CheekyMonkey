package com.entrada.cheekyMonkey.takeorder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.adapter.POSListAdapter;
import com.entrada.cheekyMonkey.takeorder.entity.OrderDetail;
import com.entrada.cheekyMonkey.takeorder.entity.TableItem;
import com.entrada.cheekyMonkey.ui.CustomTextview;

/**
 * Created by ${Tanuj.Sareen} on 07/03/2015.
 */
public class TableAdapter extends POSListAdapter<TableItem> {


    private OrderDetail orderDetail = new OrderDetail();

    public TableAdapter(Context context) {
        super(context);
    }

    public String getCode(String name ){

        for (int i = 0; i< dataSet.size(); i++){

            TableItem tableItem = dataSet.get(i);
            if (name.equals(tableItem.getName()))
                return tableItem.getCode();
        }
        return  "";
    }

    public String getStatus(String name){

        for (int i = 0; i< dataSet.size(); i++){

            TableItem tableItem = dataSet.get(i);
            if (name.equals(tableItem.getName()))
                return tableItem.getStatus();
        }
        return  "";
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TableViewHolder holder;
        if (convertView == null) {
            holder = new TableViewHolder();
            convertView = inflater.inflate(R.layout.table_row_layout, null);
            holder.textViewTableName = (CustomTextview) convertView.findViewById(R.id.textViewTableName);

            convertView.setTag(holder);
        } else
            holder = (TableViewHolder) convertView.getTag();

/*        UserInfo userInfo = POSApplication.getSingleton().getmDataModel().getUserInfo();

        if (!TextUtils.isEmpty(userInfo.getTheme_Font_Color()))
            holder.textViewTableName .setTextColor(Color.parseColor(userInfo.getTheme_Font_Color()));*/


        TableItem tableItem = dataSet.get(position);

        holder.textViewTableName.setText(tableItem.getName());

        if (tableItem.getStatus().equals("Vacant")) {
            holder.textViewTableName.setBackgroundResource(R.color.LightCyan);
        } else if (tableItem.getStatus().equals("Fill")) {
            holder.textViewTableName.setBackgroundResource(R.color.Brown);
            holder.textViewTableName.setSelected(true);
        } else if (tableItem.getStatus().equals("Lock")) {
            holder.textViewTableName.setBackgroundResource(R.color.Cyan);
        }

        holder.textViewTableName.setTag(holder);
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
        detail.setTableItem(new TableItem());
        this.setOrderDetail(detail);
    }

    @Override
    public void clearDataSetALL() {
        super.clearDataSetALL();
        this.setOrderDetail(new OrderDetail());
    }

    public class TableViewHolder {

        public CustomTextview textViewTableName;
    }
}
