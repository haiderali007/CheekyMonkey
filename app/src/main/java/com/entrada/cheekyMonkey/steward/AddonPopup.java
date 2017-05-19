package com.entrada.cheekyMonkey.steward;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.dynamic.BaseFragmentActivity;
import com.entrada.cheekyMonkey.dynamic.TakeOrderFragment;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.steward.discount.DiscountLayout;
import com.entrada.cheekyMonkey.steward.other.POSAdapter;
import com.entrada.cheekyMonkey.takeorder.adapter.TakeOrderAdapter;
import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.Util;

import java.util.ArrayList;


public class AddonPopup implements View.OnClickListener, OnItemClickListener {

    ArrayList<AddonItems> addonItemList = new ArrayList<>();
    CustomTextview tv_skip, tv_proceed;
    /**
     * Modifier Adapter
     */
    AddonAdapter<AddonItems> arrayAdapter;
    ListView listViewModifer;
    IcallBackAddon backAddon;
    TakeOrderAdapter takeOrderAdapter;
    DiscountLayout discountLayout;
    private CustomTextview tv_back, txtViewModifierer;
    private Context context;
    private LayoutInflater inflater;
    private String code = "", name = "", menu_group_code = "";

    public AddonPopup(Context context, final String code, String name,
                      String menu_group_code, IcallBackAddon backAddon,
                      TakeOrderAdapter takeOrderAdapter,
                      DiscountLayout discountLayout) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.code = code;
        this.name = name;
        this.menu_group_code = menu_group_code;
        this.backAddon = backAddon;
        this.takeOrderAdapter = takeOrderAdapter;
        this.discountLayout = discountLayout;
    }

    private void init() {

        SQLiteDatabase mdb = POSDatabase.getMenuItem(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        try {
            Cursor cAddon = getAllRecordsAddonParticular(code, mdb);
            if (cAddon != null && cAddon.moveToFirst()) {
                do {
                    AddonItems item = new AddonItems(
                            cAddon.getString(cAddon
                                    .getColumnIndex(DBConstants.KEY_ADDONS_CODE)),
                            cAddon.getString(cAddon
                                    .getColumnIndex(DBConstants.KEY_ADDONS_NAME)),
                            cAddon.getString(cAddon
                                    .getColumnIndex(DBConstants.KEY_ADDONS_ITEM_CODE)),
                            cAddon.getFloat(cAddon
                                    .getColumnIndex(DBConstants.KEY_ADDONS_PRICE)), false);
                    addonItemList.add(item);

                } while (cAddon.moveToNext());
            }
            mdb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

    }

    private Cursor getAllRecordsAddonParticular(String adonnItemCode, SQLiteDatabase mdb) {

        String queryAddon = "SELECT * FROM " + DBConstants.KEY_ADDONS_TABLE ;

        return mdb.rawQuery(queryAddon, null);
    }

    @SuppressLint("InflateParams")
    public View addView() {

        init();

        View v = inflater.inflate(R.layout.modifier_layout, null);
        txtViewModifierer = (CustomTextview) v.findViewById(R.id.txtViewModifierer);
        listViewModifer = (ListView) v.findViewById(R.id.listViewModifer);

        arrayAdapter = new AddonAdapter<>(context);
        arrayAdapter.addDataSetList(addonItemList);
        listViewModifer.setAdapter(arrayAdapter);
        listViewModifer.setOnItemClickListener(this);

        tv_back = (CustomTextview) v.findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        tv_proceed = (CustomTextview) v.findViewById(R.id.txt_proceed);
        tv_proceed.setOnClickListener(this);
        tv_skip = (CustomTextview) v.findViewById(R.id.txt_skip);
        tv_skip.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_back:

                if (context instanceof BaseFragmentActivity){
                    TakeOrderFragment takeOrderFragment = ((BaseFragmentActivity)context).takeOrderFragment;
                    if(takeOrderFragment != null)
                        takeOrderFragment.showHomeScreen();
                }
                break;


            case R.id.txt_proceed:

                ArrayList<OrderItem> orderItemsList = new ArrayList<>();
                boolean isSelected = false;
                int count = arrayAdapter.getCount();
                for (int i = 0; i < count; i++) {

                    AddonItems item = (AddonItems) arrayAdapter.getItem(i);
                    if (item.addonQty > 0) {
                        isSelected = true;
                        OrderItem obj_order = new OrderItem();
                        obj_order.o_code = item.addonID;
                        obj_order.o_name = item.addonName;
                        obj_order.o_quantity = item.addonQty;
                        obj_order.o_price = item.addonPrice;
                        obj_order.o_amount = item.addonPrice *item.addonQty;
                        obj_order.o_itm_rmrk = "";
                        obj_order.o_addon_code = code;
                        obj_order.o_mod = "";
                        obj_order.o_combo_code = "";
                        obj_order.o_happy_hour = "";
                        obj_order.o_subunit = "";
                        obj_order.o_grp_code = menu_group_code;

                        discountLayout.createDiscList("0", obj_order.o_grp_code, obj_order.o_amount);
                        orderItemsList.add(obj_order);
                    }
                }

                if (isSelected)
                    backAddon.addItem(orderItemsList, StaticConstants.ADDON_POPUP_TAG);
                else
                    backAddon.removeView(StaticConstants.ADDON_POPUP_TAG);

                break;

            case R.id.txt_skip:
                backAddon.removeView(StaticConstants.ADDON_POPUP_TAG);
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {

        AddonItems item = (AddonItems) arrayAdapter.getItem(position);
        item.isSelected = !item.isSelected;

        arrayAdapter.setDataSetItem(position, item);
        arrayAdapter.notifyDataSetChanged();

    }

    public interface IcallBackAddon {

        void addItem(ArrayList<OrderItem> orderItemsList, String Tag);

        /**
         * Remove View from layout
         */
        void removeView(String Tag);

    }

    public class AddonAdapter<T> extends POSAdapter<T> {

        boolean dialogIsHidden = true;

        private AddonAdapter(Context context) {
            super(context);
        }

        @SuppressLint("InflateParams")
        @SuppressWarnings("unchecked")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final AddonViewHolder holder;
            if (convertView == null) {
                holder = new AddonViewHolder();
                convertView = inflater.inflate(R.layout.addon_row_layout_new, null);
                holder.txtaddonName = (CustomTextview) convertView
                        .findViewById(R.id.txtaddonName);
                holder.txtAddonQtyView = (CustomTextview) convertView
                        .findViewById(R.id.txtAddonQtyView);
                holder.txtAddonPriceView = (CustomTextview) convertView
                        .findViewById(R.id.txtAddonPriceView);
                holder.imgSelector = (ImageView) convertView
                        .findViewById(R.id.imgAddonSelector);
                convertView.setTag(holder);
            } else
                holder = (AddonViewHolder) convertView.getTag();



            final AddonItems item = (AddonItems) dataSet.get(position);
            holder.txtaddonName.setText(item.addonName);
            holder.txtAddonPriceView.setText(String.valueOf(Util.numberFormat(item.addonPrice)));

            holder.imgSelector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    item.addonQty = 0;
                    holder.imgSelector.setVisibility(View.INVISIBLE);
                    holder.txtAddonQtyView.setVisibility(View.INVISIBLE);
                    holder.txtAddonQtyView.setText(context.getString(R.string.total_qty, "0"));

                    int i=0;
                    for (OrderItem orderItem : takeOrderAdapter.getDataSet()){

                        if (orderItem.o_code.equals(item.addonID)){
                            takeOrderAdapter.removeDataSetItem(i);
                            break;
                        }
                        i++;
                    }

                }
            });


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*holder.imgSelector.setVisibility(View.VISIBLE);
                    holder.txtAddonQtyView.setVisibility(View.VISIBLE);
                    String qty = holder.txtAddonQtyView.getText().toString().substring(2);
                    item.addonQty = Integer.valueOf(qty) + 1;
                    holder.txtAddonQtyView.setText(context.getString(R.string.total_qty, String.valueOf(item.addonQty)));*/

                    if (dialogIsHidden) {
                        dialogIsHidden = false;
                        showQtyPopup(holder,item);
                    }
                }
            });

            return convertView;
        }

        private void showQtyPopup(final AddonViewHolder holder, final AddonItems items){

            String[] qty = new String[15];

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            //AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_DARK);
            //AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_TRADITIONAL);
            //AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.layout_quantity, null);

            TextView textViewTitle = (TextView)view.findViewById(R.id.tv_selectTitle);
            textViewTitle.setText(R.string.select_quantity);

            ListView listview_qty = (ListView) view.findViewById(R.id.lv_qty);
            ArrayAdapter<String> adapter_qty = new ArrayAdapter<>(context, R.layout.list_qty_layout, qty);

            for (int i = 0; i < 15; i++)
                qty[i] = i + 1 + "";

            listview_qty.setAdapter(adapter_qty);
            builder.setView(view);

            final AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface arg0, int keyCode,
                                     KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialogIsHidden = true;
                        dialog.dismiss();
                    }
                    return true;
                }
            });

            dialog.show();

            listview_qty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    holder.imgSelector.setVisibility(View.VISIBLE);
                    holder.txtAddonQtyView.setVisibility(View.VISIBLE);
                    items.addonQty = position + 1;
                    holder.txtAddonQtyView.setText(context.getString(R.string.total_qty, String.valueOf(items.addonQty)));

                    dialogIsHidden = true;
                    dialog.dismiss();
                }
            });
        }


        private class AddonViewHolder {

            CustomTextview txtaddonName, txtAddonQtyView, txtAddonPriceView;
            ImageView imgSelector;
        }

    }

}