package com.entrada.cheekyMonkey.takeorder.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.adapter.POSListAdapter;
import com.entrada.cheekyMonkey.dynamic.TakeOrderFragment;
import com.entrada.cheekyMonkey.entity.MenuItem;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.steward.discount.DiscountLayout;
import com.entrada.cheekyMonkey.takeorder.entity.OrderDetail;
import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;
import com.entrada.cheekyMonkey.takeorder.entity.TableItem;
import com.entrada.cheekyMonkey.takeorder.popup.CustomKeypad;
import com.entrada.cheekyMonkey.takeorder.popup.ICallOrder;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.Util;

import java.util.Locale;

/**
 * Created by ${kamal} on 07/03/2015.
 */
public class TakeOrderAdapter extends POSListAdapter<OrderItem> {

    ICallOrder iCallOrder;
    DiscountLayout discountLayout;
    private OrderDetail orderDetail = new OrderDetail();


    public TakeOrderAdapter(Context context, ICallOrder iCallOrder, DiscountLayout discountLayout) {
        super(context);
        this.iCallOrder = iCallOrder;
        this.discountLayout = discountLayout;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final OrderViewHolder holder;
        if (convertView == null) {
            holder = new OrderViewHolder();
            convertView = inflater.inflate(R.layout.order_item_row_layout, null);
            holder.itemNameTxt = (CustomTextview) convertView
                    .findViewById(R.id.itemNameTxt);
            holder.itemQuantityTxt = (CustomTextview) convertView
                    .findViewById(R.id.itemQuantityTxt);
            holder.itemAmountTxt = (CustomTextview) convertView
                    .findViewById(R.id.itemAmountTxt);
            holder.minus_order = (ImageView) convertView
                    .findViewById(R.id.minus_order);
            holder.plus_order = (ImageView) convertView
                    .findViewById(R.id.plus_order);
            holder.remove_order = (ImageView) convertView
                    .findViewById(R.id.remove_order);

            holder.layoutItemQtyBottom1 = (LinearLayout) convertView
                    .findViewById(R.id.layout_qty_bottom1);
            holder.layoutItemQtyBottom2 = (LinearLayout) convertView
                    .findViewById(R.id.layout_qty_bottom2);
            holder.tv_ItemRemark = (TextView) convertView
                    .findViewById(R.id.tv_ItemRemark);
            holder.t_v_i_meal_for_o_s_l = (TextView) convertView
                    .findViewById(R.id.t_v_i_meal_for_o_s_l);
            holder.t_v_i_cvr_for_o_s_l = (TextView) convertView
                    .findViewById(R.id.t_v_i_cvr_for_o_s_l);


/*			UserInfo userInfo = POSApplication.getSingleton().getmDataModel().getUserInfo();
            int font_color = userInfo.getFontColor();

			holder.itemNameTxt .setTextColor(font_color);
			holder.itemQuantityTxt.setTextColor(font_color);
			holder.itemAmountTxt.setTextColor(font_color);*/

            convertView.setTag(holder);

        } else
            holder = (OrderViewHolder) convertView.getTag();

        holder.orderItem = dataSet.get(position);

        holder.tv_ItemRemark.setVisibility(View.GONE);
        holder.t_v_i_meal_for_o_s_l.setVisibility(View.GONE);


        holder.minus_order.setOnClickListener(onclick_minus(position));
        holder.plus_order.setOnClickListener(onclick_plus(position));
        holder.remove_order.setOnClickListener(onclick_remove(position));
        holder.itemQuantityTxt.setOnLongClickListener(onLongClickListnener(position));

        if (!holder.orderItem.getO_code().equals("")) {
            //holder.minus_order.setVisibility(View.VISIBLE);
            //holder.plus_order.setVisibility(View.VISIBLE);
            holder.remove_order.setVisibility(View.VISIBLE);


            holder.itemNameTxt.setText(holder.orderItem.getO_name());

            if (holder.orderItem.getO_name().isEmpty()){
                holder.itemNameTxt.setText(UserInfo.getMixerName(holder.orderItem.o_code));
            }

            //holder.itemAmountTxt.setText(String.format("%.2f", holder.orderItem.getO_amount()));

            //holder.itemAmountTxt.setText("" + holder.orderItem.getO_amount());


            String qty = "" + holder.orderItem.getO_quantity();
            if (qty.contains(".0") || holder.orderItem.getO_price() == 0)
                holder.itemQuantityTxt.setText(String.format(Locale.US, "%.0f",holder.orderItem.getO_quantity()));
            else
                holder.itemQuantityTxt.setText(String.format(Locale.US, "%.2f",holder.orderItem.getO_quantity()));

            showQtyByImage(holder, (int) holder.orderItem.getO_quantity());


            // holder.itemAmountTxt.setText("" + holder.orderItem.getO_amount());
            // holder.itemAmountTxt.setText(String.format("%.2f", holder.orderItem.getO_amount()));
         /*	NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
            DecimalFormat decFormat = (DecimalFormat) nf;
			decFormat.applyPattern("0.00");
			Logger.i("tag", "toatal price  " + holder.orderItem.getO_amount());*/
            String amount = context.getString(R.string.rupees, "\u20B9",
                    Util.numberfornmat(holder.orderItem.getO_amount()));
            holder.itemAmountTxt.setText(amount);


            if (holder.orderItem.getO_itm_rmrk().length() > 0) {
                holder.tv_ItemRemark.setText(holder.orderItem.getO_itm_rmrk());
                holder.tv_ItemRemark.setVisibility(View.VISIBLE);
            }

            if (holder.orderItem.getO_meal_cors_item()
                    .length() > 0) {
                holder.t_v_i_meal_for_o_s_l.setText(" M-" + holder.orderItem.getO_meal_name());
                holder.t_v_i_meal_for_o_s_l.setVisibility(View.VISIBLE);
            }

            /*if (holder.orderItem.getO_cover_item().length() > 0) {
                holder.t_v_i_cvr_for_o_s_l.setText(" C-" + holder.orderItem.getO_cover_item());
                holder.t_v_i_cvr_for_o_s_l.setVisibility(View.VISIBLE);
            }*/

        } else {
            holder.itemNameTxt.setText("**********************");
            holder.itemNameTxt.setClickable(false);
            holder.itemQuantityTxt.setText("********");
            holder.minus_order.setVisibility(View.GONE);
            holder.plus_order.setVisibility(View.GONE);
            holder.itemAmountTxt.setText("*********");
            holder.tv_ItemRemark.setText("");
            holder.tv_ItemRemark.setVisibility(View.GONE);

        }

        holder.itemNameTxt.setTag(holder.orderItem);
        holder.itemQuantityTxt.setTag(holder.orderItem);
        holder.itemAmountTxt.setTag(holder.orderItem);
        holder.minus_order.setTag(holder.orderItem);
        holder.plus_order.setTag(holder.orderItem);
        holder.remove_order.setTag(holder.orderItem);

        return convertView;
    }

    private View.OnLongClickListener onLongClickListnener(final int position) {

        return new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                CustomKeypad keypad = new CustomKeypad(context, v, position, TakeOrderAdapter.this);
                keypad.show();
                return false;
            }
        };
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

    private View.OnClickListener onclick_minus(final int position) {

        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!dataSet.get(position).getO_name().contains("##") &&
                        !dataSet.get(position).getO_name().contains("(Free)")) { // To avoid decreasing combo items particularly

                    OrderItem orderItem = dataSet.get(position);
                    float quantity = dataSet.get(position).getO_quantity();
                    float price = dataSet.get(position).getO_price();

                    if (quantity > 1) {
                        quantity = quantity - 1;
                        price = quantity * price;

                        orderItem.setO_quantity(quantity);
                        orderItem.setO_amount(price);

                        int count = position + 1;
                        //if(orderItem.getO_combo_code().equals("Y") ) // Check to see if it contains combo items
                        if (dataSet.size() > count) {
                            do {

                                if (dataSet.get(count).getO_name().contains("##") ||        // combo items contains ##. (Immediate below of main item)
                                        dataSet.get(count).getO_name().contains("(Free)")) {
                                    float qty = dataSet.get(count).getO_quantity() * quantity / (quantity + 1);
                                    dataSet.get(count).setO_quantity(qty); // Decrease qty. of combo items
                                    count++;

                                } else
                                    break;

                            } while (count < getCount());
                        }
                    }

                    discountLayout.updateDiscAmount(position, price);
                    dataSet.set(position, orderItem);
                    notifyDataSetChanged();

                    if (iCallOrder instanceof TakeOrderFragment) {
                        TakeOrderFragment takeOrderFragment = (TakeOrderFragment) iCallOrder;
                        takeOrderFragment.showTotalAmount();
                    }
                }
            }
        };
    }

    private View.OnClickListener onclick_plus(final int position) {

        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                addQty(position);
            }
        };
    }

    public void addQty(int position) {

        if (!dataSet.get(position).getO_name().contains("##") &&
                !dataSet.get(position).getO_name().contains("(Free)")) { // To avoid incrementing combo items.

            OrderItem orderItem = dataSet.get(position);
            float quantity = dataSet.get(position).getO_quantity();
            float price = dataSet.get(position).getO_price();

            quantity = quantity + 1;
            price = quantity * price;

            orderItem.setO_quantity(quantity);
            orderItem.setO_amount(price);

            int count = position + 1;

            //if(orderItem.getO_combo_code().equals("Y") ) // Check to see if it contains combo items
            if (dataSet.size() > count) {
                do {

                    if (dataSet.get(count).getO_name().contains("##") ||        // ##- for filtering combo items
                            dataSet.get(count).getO_name().contains("(Free)")) {
                        float qty = dataSet.get(count).getO_quantity() * quantity / (quantity - 1);
                        dataSet.get(count).setO_quantity(qty); //Update qty of combo items
                        count++;

                    } else
                        break;

                } while (count < getCount());
            }

            discountLayout.updateDiscAmount(position, price);
            dataSet.set(position, orderItem);
            notifyDataSetChanged();

            if (iCallOrder instanceof TakeOrderFragment) {
                TakeOrderFragment takeOrderFragment = (TakeOrderFragment) iCallOrder;
                takeOrderFragment.showTotalAmount();
            }
        }
    }

    public void addMultiQty(MenuItem menuItem, int position) {

        if (!dataSet.get(position).getO_name().contains("##") &&
                !dataSet.get(position).getO_name().contains("(Free)")) { // To avoid incrementing combo items.

            OrderItem orderItem = dataSet.get(position);
            float quantity = menuItem.getQuantity();
            float price = menuItem.getMenuAmount();

            orderItem.setO_quantity(quantity);
            orderItem.setO_amount(price);

            int count = position + 1;

            //if(orderItem.getO_combo_code().equals("Y") ) // Check to see if it contains combo items
            if (dataSet.size() > count) {
                do {

                    if (dataSet.get(count).getO_name().contains("##") ||        // ##- for filtering combo items
                            dataSet.get(count).getO_name().contains("(Free)")) {
                        float qty = dataSet.get(count).getO_quantity() * quantity / (quantity - 1);
                        dataSet.get(count).setO_quantity(qty); //Update qty of combo items
                        count++;

                    } else
                        break;

                } while (count < getCount());
            }

            dataSet.set(position, orderItem);
            notifyDataSetChanged();

            if (iCallOrder instanceof TakeOrderFragment) {
                TakeOrderFragment takeOrderFragment = (TakeOrderFragment) iCallOrder;
                takeOrderFragment.showTotalAmount();
            }
        }
    }


    public void updateQty(int position, float quantity) {

        if (!dataSet.get(position).getO_name().contains("##") &&
                !dataSet.get(position).getO_name().contains("(Free)")) { // To avoid incrementing combo items.

            OrderItem orderItem = dataSet.get(position);
            float price = dataSet.get(position).getO_price();

            price = quantity * price;

            orderItem.setO_quantity(quantity);
            orderItem.setO_amount(price);

            int count = position + 1;

            //if(orderItem.getO_combo_code().equals("Y") ) // Check to see if it contains combo items
            if (dataSet.size() > count) {
                do {

                    if (dataSet.get(count).getO_name().contains("##")) {    // ##- for filtering combo items)
                        dataSet.get(count).setO_quantity(quantity); // Update qty of combo items
                        count++;

                    } else if (dataSet.get(count).getO_name().contains("(Free)")) {

                        float qty = dataSet.get(count).getPerItemFreeQty() * quantity;
                        dataSet.get(count).setO_quantity(qty); // Update qty of combo items
                        count++;
                    } else
                        break;

                } while (count < getCount());
            }

            discountLayout.updateDiscAmount(position, price);
            dataSet.set(position, orderItem);
            notifyDataSetChanged();

            if (iCallOrder instanceof TakeOrderFragment) {
                TakeOrderFragment takeOrderFragment = (TakeOrderFragment) iCallOrder;
                takeOrderFragment.showTotalAmount();
            }
        }
    }

    private View.OnClickListener onclick_remove(final int position) {

        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Removes Any Ordered item(including it's name, price, qty) from List view and it's add on/combo items. (if contains)

                if (iCallOrder.getPosVisibility() && !dataSet.get(position).getO_name().contains("##") &&
                        !dataSet.get(position).getO_name().contains("(Free)")) {
                    OrderItem orderItem = dataSet.get(position);
                    dataSet.remove(position);  // Removes Selected ordered item
                    discountLayout.removeItemDiscList(position);
                    iCallOrder.updateCodeList(position);

                    if (getCount() > position) {

                        if (orderItem.getO_addon_code_new().equalsIgnoreCase("Y")) { // Check to see if deleted item is of add-on type

                            do {
                                if (!dataSet.get(position).getO_addon_code().isEmpty()) //Remove it's add on items
                                {
                                    dataSet.remove(position);
                                    discountLayout.removeItemDiscList(position);
                                } else
                                    break;

                            } while (getCount() > position);
                        } else if (orderItem.getO_combo_code().equals("Y")) // Check to see if deleted item is of combo type
                        {
                            do {
                                if (dataSet.get(position).getO_name().contains("##")) //Remove it's combo items
                                {
                                    dataSet.remove(position);
                                    discountLayout.removeItemDiscList(position);
                                } else break;

                            } while (getCount() > position);
                        } else if (dataSet.get(position).getO_name().contains("(Free)")) {

                            do {
                                if (dataSet.get(position).getO_name().contains("(Free)")) //Remove it's combo items
                                {
                                    dataSet.remove(position);
                                    discountLayout.removeItemDiscList(position);
                                    iCallOrder.updateCodeList(position);
                                } else break;

                            } while (getCount() > position);

                        }
                    }

                    notifyDataSetChanged();
                    if (iCallOrder instanceof TakeOrderFragment) {
                        TakeOrderFragment takeOrderFragment = (TakeOrderFragment) iCallOrder;
                        takeOrderFragment.showTotalAmount();
                    }
                }
            }
        };
    }

    private void showQtyByImage(OrderViewHolder holder, int qty) {

        holder.layoutItemQtyBottom1.removeAllViews();
        holder.layoutItemQtyBottom2.removeAllViews();
        ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(40, 40);

        String mDrawableName = "img" + holder.orderItem.o_categ_code;
        int resID = context.getResources().getIdentifier(mDrawableName,
                "drawable", context.getPackageName());

        for (int i = 0; i < qty; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(resID);
            imageView.setLayoutParams(param);

            if (i < 8)
                holder.layoutItemQtyBottom1.addView(imageView);
            else
                holder.layoutItemQtyBottom2.addView(imageView);
        }
    }

    public class OrderViewHolder {

        private CustomTextview itemNameTxt, itemQuantityTxt, itemAmountTxt;
        private ImageView minus_order, plus_order, remove_order;
        private TextView tv_ItemRemark, t_v_i_meal_for_o_s_l, t_v_i_cvr_for_o_s_l;
        LinearLayout layoutItemQtyBottom1, layoutItemQtyBottom2;
        OrderItem orderItem;
    }
}