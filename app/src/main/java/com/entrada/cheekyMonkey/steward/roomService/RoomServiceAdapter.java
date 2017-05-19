package com.entrada.cheekyMonkey.steward.roomService;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.adapter.POSListAdapter;
import com.entrada.cheekyMonkey.takeorder.entity.OrderDetail;
import com.entrada.cheekyMonkey.ui.CustomTextview;


/**
 * Created by CSATSPL on 22/12/2015.
 */
public class RoomServiceAdapter extends POSListAdapter<RoomItem> {


    private OrderDetail orderDetail = new OrderDetail();

    public RoomServiceAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RoomViewHolder holder;
        if (convertView == null) {
            holder = new RoomViewHolder();
            convertView = inflater.inflate(R.layout.room_row_layout, null);
            holder.textViewRommName = (CustomTextview) convertView.findViewById(R.id.textViewRommName);

            convertView.setTag(holder);
        } else
            holder = (RoomViewHolder) convertView.getTag();


        RoomItem roomItem = dataSet.get(position);

        holder.textViewRommName.setText(roomItem.getRoom_code());

        if (roomItem.getStatus().equals("V")) {
            holder.textViewRommName.setBackgroundResource(R.color.Cyan);

        } else if (roomItem.getStatus().equals("F")) {
            holder.textViewRommName.setBackgroundResource(R.color.Green);
            holder.textViewRommName.setSelected(true);
        } else if (roomItem.getStatus().equals("R")) {
            holder.textViewRommName.setBackgroundResource(R.color.Brown);
        }
        holder.textViewRommName.setTag(holder);
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
        detail.setRoomItem(new RoomItem());
        this.setOrderDetail(detail);
    }

    @Override
    public void clearDataSetALL() {
        super.clearDataSetALL();
        this.setOrderDetail(new OrderDetail());
    }

    public class RoomViewHolder {

        public CustomTextview textViewRommName;
    }

}