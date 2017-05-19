package com.entrada.cheekyMonkey.steward;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.db.MenuItemDB;
import com.entrada.cheekyMonkey.entity.MenuItem;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.steward.discount.AutoDiscount;
import com.entrada.cheekyMonkey.steward.discount.DiscountLayout;
import com.entrada.cheekyMonkey.takeorder.adapter.TakeOrderAdapter;
import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;
import com.entrada.cheekyMonkey.util.Logger;

import java.util.ArrayList;


public class OnMenuItemClick {

    public ICallMenuPopup callMenuPopup;
    ModifierPopup modifierPopup;
    AddonPopup addonPopup;
    ComboPopup comboPopup;
    DiscountLayout discountLayout;
    AutoDiscount autoDiscount;
    private Context context;
    private MenuItem menuItem;
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

        if (menuItem.getMenu_combo().equals("F")) {

            obj_order.o_code = menuItem.getMenu_code();
            obj_order.o_name = menuItem.getMenu_name();
            obj_order.o_amount = menuItem.getMenu_price();
            obj_order.o_combo_code = "Y";
            obj_order.o_price = menuItem.getMenu_price();
            obj_order.o_subunit = menuItem.getMenu_sub_unit();
            obj_order.o_grp_code = menuItem.getMenu_group_code();
            Logger.i("Combo Code", "::" + menuItem.getMenu_code());
            takeOrderAdapter.addDataSetItem(obj_order);

            ArrayList<OrderItem> list = MenuItemDB.obj().getComboItem(context,
                    menuItem.getMenu_code(), discountLayout);
            if (list != null)
                takeOrderAdapter.addDataSetList(list);

        } else if (menuItem.getMenu_combo().equals("O")) {

            obj_order.o_code = menuItem.getMenu_code();
            obj_order.o_name = menuItem.getMenu_name();
            obj_order.o_amount = menuItem.getMenu_price();
            obj_order.o_combo_code = "Y";
            obj_order.o_price = menuItem.getMenu_price();
            obj_order.o_subunit = menuItem.getMenu_sub_unit();
            obj_order.o_grp_code = menuItem.getMenu_group_code();

            Logger.i("Combo Code", "::" + menuItem.getMenu_code());
            takeOrderAdapter.addDataSetItem(obj_order);
            showComboItem(menuItem.getMenu_code(), menuItem.getMenu_name(),
                    menuItem.getMenu_group_code());

        } else {

            obj_order.o_code = menuItem.getMenu_code();
            obj_order.o_name = menuItem.getMenu_name();
            obj_order.o_quantity = menuItem.getQuantity();
            obj_order.o_price = menuItem.getMenu_price();
            obj_order.o_amount = menuItem.getQuantity() * menuItem.getMenu_price();
            obj_order.o_subunit = menuItem.getMenu_sub_unit();
            obj_order.o_grp_code = menuItem.getMenu_group_code();

            takeOrderAdapter.addDataSetItem(obj_order);


            if (menuItem.getMenu_mod().equals("Y"))
                showModifierItem(menuItem.getMenu_code(), menuItem.getMenu_name());



            if (menuItem.getMenu_open_item_flag() == 1) {
                 open_item_popup(obj_order.o_code, obj_order.o_name,
                         String.valueOf(obj_order.o_quantity),
                         String.valueOf(obj_order.o_amount),
                         String.valueOf(obj_order.o_price));
            }


            if (menuItem.getMenu_sub_unit() != null && menuItem.getMenu_sub_unit().equals("Y")) {

                SubQtyPopup subQtyPopup = new SubQtyPopup(context,takeOrderAdapter);
                subQtyPopup.show();
            }

            /*
			 *
			 * if (surchrg.equals("y")) {
			 *
			 * surcharg(item_list.obj.getCode(), obj_order);
			 *
			 * }
			 *
			 * Happy_hour_new(item_list.obj.getCode());
			 *
			 * if (item_list.obj.getOpen_item_flag() == 1) {
			 * open_item_popup(obj_order.o_code, obj_order.o_name,
			 * String.valueOf(obj_order.o_quantity),
			 * String.valueOf(obj_order.o_amount),
			 * String.valueOf(obj_order.o_price)); }
			 *
			 * if (item_list.obj.getSUB_ITEM().equals("Y")) {
			 * show_Sub_item_popup(item_list.obj.getCode(),
			 * item_list.obj.getName());
			 *
			 * }
			 */

        }


        obj_order.o_addon_code_new = "Y";
        show_addon_popup_new(menuItem.getMenu_code(), menuItem.getMenu_name(),
                menuItem.getMenu_group_code());


        if (!autoDiscount.isAppliedOn(obj_order, null))
            discountLayout.createDiscList("0", menuItem.getMenu_group_code(), menuItem.getMenu_price());

    }



    public void open_item_popup(final String code, final String name,
                                final String qntity, final String amnt, final String price) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Open Item");

        LayoutInflater inflater = LayoutInflater.from(context);
        View v_for_open_item = inflater.inflate(R.layout.open_item_popup, null);

        final EditText ed_name, ed_qntity, ed_price;
        ed_name = (EditText) v_for_open_item.findViewById(R.id.e_t_n_for_o_i_p);
        ed_qntity = (EditText) v_for_open_item
                .findViewById(R.id.e_t_q_for_o_i_p);
        ed_price = (EditText) v_for_open_item
                .findViewById(R.id.e_t_p_for_o_i_p);
        ed_name.setHint(name);
        ed_qntity.setHint(qntity);
        ed_price.setHint(price);

        builder.setView(v_for_open_item);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        Button theButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                ArrayList<OrderItem> orderList = takeOrderAdapter.getDataSet();
                OrderItem obj_order = orderList.get(orderList.size()-1);

                if (ed_name.getText().toString().length() > 0
                        && ed_qntity.getText().toString().length() > 0
                        && ed_price.getText().toString().length() > 0) {

                    obj_order.o_name = ed_name.getText().toString();
                    obj_order.o_quantity = Float.parseFloat(ed_qntity.getText()
                            .toString());
                    obj_order.o_amount = Float.parseFloat(ed_qntity.getText()
                            .toString())
                            * Float.parseFloat(ed_price.getText().toString());
                    obj_order.o_addon_code = ed_price.getText().toString();
                    obj_order.o_mod = ed_name.getText().toString();
                    obj_order.o_price = Float.parseFloat(ed_price.getText()
                            .toString());

                } else if (ed_name.getText().toString().length() > 0
                        && ed_qntity.getText().toString().length() > 0
                        && !(ed_price.getText().toString().length() > 1)) {

                    obj_order.o_name = ed_name.getText().toString();
                    obj_order.o_quantity = Float.parseFloat(ed_qntity.getText()
                            .toString());
                    obj_order.o_amount = Float.parseFloat(ed_qntity.getText()
                            .toString()) * obj_order.getO_price();
                    obj_order.o_addon_code = String.valueOf(obj_order
                            .getO_price());
                    obj_order.o_mod = ed_name.getText().toString();

                } else if (!(ed_name.getText().toString().length() > 1)
                        && ed_qntity.getText().toString().length() > 0
                        && ed_price.getText().toString().length() > 0) {

                    obj_order.o_quantity = Float.parseFloat(ed_qntity.getText()
                            .toString());
                    obj_order.o_amount = Float.parseFloat(ed_qntity.getText()
                            .toString())
                            * Float.parseFloat(ed_price.getText().toString());
                    obj_order.o_addon_code = ed_price.getText().toString();
                    obj_order.o_price = Float.parseFloat(ed_price.getText()
                            .toString());

                } else if (ed_name.getText().toString().length() > 0
                        && !(ed_qntity.getText().toString().length() > 1)
                        && ed_price.getText().toString().length() > 0) {

                    obj_order.o_name = ed_name.getText().toString();
                    obj_order.o_quantity = obj_order.getO_quantity();
                    obj_order.o_amount = obj_order.getO_quantity()
                            * Float.parseFloat(ed_price.getText().toString());
                    obj_order.o_addon_code = ed_price.getText().toString();
                    obj_order.o_mod = ed_name.getText().toString();
                    obj_order.o_price = Float.parseFloat(ed_price.getText()
                            .toString());

                } else if (ed_name.getText().toString().length() > 0
                        && !(ed_price.getText().toString().length() > 1)
                        && !(ed_qntity.getText().toString().length() > 1)) {

                    obj_order.o_name = ed_name.getText().toString();
                    obj_order.o_addon_code = String.valueOf(obj_order
                            .getO_price());
                    obj_order.o_mod = ed_name.getText().toString();

                } else if (ed_name.getText().toString().length() > 0
                        && ed_price.getText().toString().length() > 0
                        && !(ed_qntity.getText().toString().length() > 1)) {

                    obj_order.o_name = ed_name.getText().toString();
                    obj_order.o_quantity = obj_order.getO_quantity();
                    obj_order.o_amount = obj_order.getO_quantity()
                            * Float.parseFloat(ed_price.getText().toString());
                    obj_order.o_addon_code = ed_price.getText().toString();
                    obj_order.o_mod = ed_name.getText().toString();
                    obj_order.o_price = Float.parseFloat(ed_price.getText()
                            .toString());

                } else if (!(ed_name.getText().toString().length() > 1)
                        && !(ed_price.getText().toString().length() > 1)
                        && ed_qntity.getText().toString().length() > 0) {

                    obj_order.o_quantity = Float.parseFloat(ed_qntity.getText()
                            .toString());
                    obj_order.o_amount = Float.parseFloat(ed_qntity.getText()
                            .toString()) * obj_order.getO_price();
                    obj_order.o_addon_code = String.valueOf(obj_order
                            .getO_price());

                } else if (!(ed_name.getText().toString().length() > 1)
                        && ed_price.getText().toString().length() > 0
                        && !(ed_qntity.getText().toString().length() > 1)) {

                    obj_order.o_amount = obj_order.getO_quantity()
                            * Float.parseFloat(ed_price.getText().toString());
                    obj_order.o_addon_code = ed_price.getText().toString();
                    obj_order.o_price = Float.parseFloat(ed_price.getText()
                            .toString());

                } else {

                    obj_order.o_addon_code = String.valueOf(obj_order
                            .getO_price());

                }

                takeOrderAdapter.notifyDataSetInvalidated();
                dialog.dismiss();

            }
        });
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

    public void showComboItem(final String code, String name,
                              final String grp_code) {

        comboPopup = new ComboPopup(context, code, name, grp_code, new ComboPopup.IcallBackCombo() {

            @Override
            public void removeView(String Tag) {
                callMenuPopup.removeView(Tag);
            }

            @Override
            public void addItem(ArrayList<OrderItem> arrayList, String Tag) {

                takeOrderAdapter.addDataSetList(arrayList);
                callMenuPopup.updateScrolling();
                callMenuPopup.removeView(Tag);
            }
        }, discountLayout);
        callMenuPopup.addView(comboPopup.addView(), StaticConstants.COMBO_POPUP_TAG);
    }

    public void showModifierItem(final String code, final String name) {

        modifierPopup = new ModifierPopup(context, code, name,
                new ModifierPopup.ICallBackMod() {

                    @Override
                    public void removeView(String Tag) {
                        callMenuPopup.removeView(Tag);
                    }

                    @Override
                    public void addItem(String modiName, String Tag) {

                        int count = takeOrderAdapter.getCount() - 1;

                        OrderItem item = (OrderItem) takeOrderAdapter
                                .getItem(count);

                        item.setO_name(item.getO_name() + "-" + modiName);
                        item.setO_mod("Y");
                        takeOrderAdapter.setDataSetItem(count, item);
                        callMenuPopup.removeView(Tag);
                    }
                });

        callMenuPopup.addView(modifierPopup.addView(), StaticConstants.MODIFIER_POPUP_TAG);

    }

    public void removeModifierView(String Tag) {
        callMenuPopup.removeView(Tag);
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
