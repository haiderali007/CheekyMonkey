package com.entrada.cheekyMonkey.steward.discount;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.takeorder.adapter.TakeOrderAdapter;
import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by csat on 11/01/2016.
 */
public class AutoDiscount {

    public String poscode = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posCode;
    Context context;
    DiscountLayout discountLayout;
    TakeOrderAdapter takeOrderAdapter;
    String disc_code = "";
    String date = "";
    boolean flag = false;

    public AutoDiscount(Context context, DiscountLayout discountLayout, TakeOrderAdapter takeOrderAdapter) {

        this.context = context;
        this.discountLayout = discountLayout;
        this.takeOrderAdapter = takeOrderAdapter;
        disc_code = getHappyDiscCode();
        flag = !disc_code.isEmpty() && checkDow(disc_code);
    }


    public boolean isAppliedOn(OrderItem orderItem, ArrayList<String> codeList) {

        String disc_per;
        String item_code = orderItem.o_code;

        if (flag) {

            SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
            mdb.beginTransaction();

            try {
                String queryDiscPer = "Select * from "
                        + DBConstants.KEY_DISC_ITEM_TABLE + " WHERE "
                        + DBConstants.KEY_DISC_ITEM_POS_CODE + "= '" + poscode + "' and "
                        + DBConstants.KEY_DISC_ITEM_DISC_CODE + "= '" + disc_code + "' and "
                        + DBConstants.KEY_DISC_ITEM_ITEM_CODE + "= '" + item_code + "' ";

                Cursor cursor = mdb.rawQuery(queryDiscPer, null);

                if (cursor != null && cursor.moveToFirst()) {

                    disc_per = cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_DISC_ITEM_DISC_PER));
                    float hpy_rate = cursor.getFloat(cursor
                            .getColumnIndex(DBConstants.KEY_DISC_HAPPY_RATE));
                    orderItem.o_price = orderItem.o_amount = hpy_rate;  // Update Main Item Rate by Happy Rate
                    orderItem.o_happy_hour = "M";

                    discountLayout.createDiscList(disc_per, orderItem.o_grp_code, orderItem.o_price);  // Add Main Item Discount (%)

                    OrderItem obj_order = new OrderItem();
                    obj_order.o_code = cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_DISC_FREE_ITEM_CODE));
                    float billed_qty = cursor.getFloat(cursor
                            .getColumnIndex(DBConstants.KEY_DISC_ITEM_BILLED_QTY));
                    float free_qty = cursor.getFloat(cursor
                            .getColumnIndex(DBConstants.KEY_DISC_ITEM_FREE_QTY));

                    if (billed_qty > 0 && free_qty > 0) {

                        if (obj_order.o_code.isEmpty()) obj_order.o_code = item_code;

                        obj_order.o_name = getItemName(obj_order.o_code) + "(Free)";
                        obj_order.o_quantity = free_qty / billed_qty;
                        obj_order.perItemFreeQty = obj_order.o_quantity;
                        obj_order.o_happy_hour = "Y";

                        takeOrderAdapter.addDataSetItem(obj_order); // Add happy hour free item
                        discountLayout.createDiscList("0", "", 0);
                        if (codeList != null)
                            codeList.add("");
                    }

                    cursor.close();
                    return true;
                }

                mdb.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mdb.endTransaction();
            }

        }

        return false;
    }

    public String getHappyDiscCode() {

        String disc_code = "";

        date = UserInfo.getRunningDate();
        String time = UserInfo.getCurrentTime();

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();
        try {
            String queryDiscount = "Select " + DBConstants.KEY_DISC_MST_DISC_CODE + " from "
                    + DBConstants.KEY_DISC_MST_TABLE + " WHERE "
                    + DBConstants.KEY_DISC_MST_POS_CODE + "='" + poscode + "' " + " and "
                    + DBConstants.KEY_DISC_MST_DISC_FLAG + "= 'A' " + " and "
                    + "'" + date + "'" + " between " + DBConstants.KEY_DISC_MST_FROM_DATE
                    + " and " + DBConstants.KEY_DISC_MST_TO_DATE + " and "
                    + "'" + time + "'" + " between " + DBConstants.KEY_DISC_MST_FROM_TIME
                    + " and " + DBConstants.KEY_DISC_MST_TO_TIME;


            Cursor cursor = mdb.rawQuery(queryDiscount, null);

            if (cursor != null && cursor.moveToFirst()) {

                disc_code = cursor.getString(0);
                cursor.close();
            }

            mdb.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

        return disc_code;
    }

    public boolean checkDow(String disc_code) {

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(date));
        int dow = c.get(Calendar.DAY_OF_WEEK);

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();
        try {
            String queryDiscount = "Select * from "
                    + DBConstants.KEY_DISC_DOW_TABLE + " WHERE "
                    + DBConstants.KEY_DISC_DOW_POS_CODE + "='" + poscode + "' " + " and "
                    + DBConstants.KEY_DISC_DOW_DISC_CODE + "= '" + disc_code + "' " + " and "
                    + DBConstants.KEY_DISC_DOW_DAY + "= '" + dow + "'";


            Cursor cursor = mdb.rawQuery(queryDiscount, null);

            if (cursor != null && cursor.moveToFirst()) {

                cursor.close();
                return true;
            }

            mdb.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

        return false;
    }

    public String getItemName(String item_code) {

        String item_name = "";
        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();
        try {
            String queryDiscount = "Select " + DBConstants.KEY_MENU_NAME + " from "
                    + DBConstants.KEY_MENU_ITEM_TABLE + " WHERE "
                    + DBConstants.KEY_MENU_POS_CODE + "='" + poscode + "' " + " and "
                    + DBConstants.KEY_MENU_CODE + "= '" + item_code + "' ";

            Cursor cursor = mdb.rawQuery(queryDiscount, null);

            if (cursor != null && cursor.moveToFirst()) {

                item_name = cursor.getString(0);
                cursor.close();
            }

            mdb.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

        return item_name;
    }

}
