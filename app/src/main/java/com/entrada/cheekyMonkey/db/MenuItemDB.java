package com.entrada.cheekyMonkey.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import com.entrada.cheekyMonkey.steward.discount.DiscountLayout;
import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;

public class MenuItemDB implements DBConstants {

    private static MenuItemDB obj = null;

    public synchronized static MenuItemDB obj() {
        if (obj == null)
            obj = new MenuItemDB();
        return obj;
    }

    public ArrayList<OrderItem> getComboItem(Context context, String combo_code,
                                             DiscountLayout discountLayout) {

        ArrayList<OrderItem> arrayList = new ArrayList<>();
        SQLiteDatabase mdb = POSDatabase.getMenuItem(context)
                .getWritableDatabase();
        mdb.beginTransaction();
        try {

            Cursor cursor = mdb.query(KEY_COMBO_TABLE, new String[]{
                    KEY_COMBO_CODE, KEY_COMBO_ITEM_CODE, KEY_COMBO_NAME,
                    KEY_COMBO_QTY}, KEY_COMBO_CODE + "='"
                    + combo_code + "'", null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    OrderItem obj_order = new OrderItem();
                    obj_order.o_code = cursor.getString(0);
                    obj_order.o_name = "##" + cursor.getString(2);
                    obj_order.o_quantity = cursor.getFloat(3);
                    obj_order.o_amount = 0;
                    obj_order.o_itm_rmrk = "";
                    obj_order.o_addon_code = "";
                    obj_order.o_mod = "";
                    obj_order.o_combo_code = cursor.getString(1);
                    obj_order.o_happy_hour = "";
                    obj_order.o_price = 0;
                    obj_order.o_subunit = "";
                    obj_order.o_sub_item = "";
                    obj_order.o_disc = "";

                    discountLayout.createDiscList("0", obj_order.o_combo_code, obj_order.o_amount);
                    arrayList.add(obj_order);
                } while (cursor.moveToNext());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

        return arrayList.size() > 0 ? arrayList : null;
    }
}
