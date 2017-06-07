package com.entrada.cheekyMonkey.takeorder;

import android.content.Context;
import android.view.View;

import com.entrada.cheekyMonkey.entity.MenuItem;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.steward.AddonPopup;
import com.entrada.cheekyMonkey.steward.discount.AutoDiscount;
import com.entrada.cheekyMonkey.steward.discount.DiscountLayout;
import com.entrada.cheekyMonkey.takeorder.adapter.TakeOrderAdapter;
import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;

import java.util.ArrayList;

public class OnMenuItemClick {

    private ICallMenuPopup callMenuPopup;
    private Context context;
    private MenuItem menuItem;
    private DiscountLayout discountLayout;
    private AutoDiscount autoDiscount;
    private AddonPopup addonPopup;
    private TakeOrderAdapter takeOrderAdapter;

    public OnMenuItemClick(Context context, MenuItem menuItem,
                           TakeOrderAdapter takeOrderAdapter, ICallMenuPopup callMenuPopup,
                           DiscountLayout discountLayout, AutoDiscount autoDiscount) {
        this.context = context;
        this.menuItem = menuItem;
        this.takeOrderAdapter = takeOrderAdapter;
        this.callMenuPopup = callMenuPopup;
        this.discountLayout = discountLayout;
        this.autoDiscount = autoDiscount;
    }

    public void showSelection() {

        OrderItem obj_order = new OrderItem();


        obj_order.o_code = menuItem.getMenu_code();
        obj_order.o_name = menuItem.getMenu_name();
        obj_order.o_amount = menuItem.getMenuAmount();
        obj_order.o_price = menuItem.getMenu_price();
        obj_order.o_subunit = menuItem.getMenu_sub_unit();
        obj_order.o_grp_code = menuItem.getMenu_group_code();
        obj_order.o_categ_code = menuItem.getMenu_categ_code();
        obj_order.o_quantity = menuItem.getQuantity();

        takeOrderAdapter.addDataSetItem(obj_order);

        obj_order.o_addon_code_new = "Y";
        show_addon_popup_new(menuItem.getMenu_code(), menuItem.getMenu_name(),
                menuItem.getMenu_group_code());

        if (!autoDiscount.isAppliedOn(obj_order, null))
            discountLayout.createDiscList("0", menuItem.getMenu_group_code(), menuItem.getMenu_price());

    }


    private void show_addon_popup_new(String menu_code, String menu_name,
                                      String menu_group_code) {

        addonPopup = new AddonPopup(context, menu_code, menu_name,
                menu_group_code, new AddonPopup.IcallBackAddon() {

            @Override
            public void removeView(String Tag) {
                callMenuPopup.removeView(Tag);
            }

            @Override
            public void addItem(ArrayList<OrderItem> arrayList,
                                String Tag) {

                //takeOrderAdapter.addDataSetList(arrayList);
                takeOrderAdapter.updateDataSetList(arrayList);
                callMenuPopup.removeView(Tag);
            }
        }, takeOrderAdapter, discountLayout);

        callMenuPopup.addView(addonPopup.addView(), StaticConstants.ADDON_POPUP_TAG);

    }

    public interface ICallMenuPopup {

        /**
         * Add View into the layout
         *
         * @param view
         */
        void addView(View view, String Tag);

        /**
         * Remove View from layout
         */
        void removeView(String Tag);

        void updateScrolling();
    }

}
