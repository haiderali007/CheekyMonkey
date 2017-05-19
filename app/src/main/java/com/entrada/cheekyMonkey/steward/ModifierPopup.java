package com.entrada.cheekyMonkey.steward;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.steward.other.POSAdapter;
import com.entrada.cheekyMonkey.ui.CustomTextview;

import java.util.ArrayList;

/**
 * Created by Tanuj.Sareen on 09/05/2015.
 */
public class ModifierPopup implements View.OnClickListener, OnItemClickListener {

    ICallBackMod backMod;
    ArrayList<ModifierItem> modifierItemList = new ArrayList<ModifierItem>();
    CustomTextview txtAddModi, txtCancelModi;
    /**
     * Modifier Adapter
     */
    ModifierAdapter<ModifierItem> arrayAdapter;
    ListView listViewModifer;
    UserInfo userInfo;
    private CustomTextview txtViewModifierer;
    private Context context;
    private LayoutInflater inflater;
    private String code = "", name = "";
    public ModifierPopup(Context context, final String code, String name,
                         ICallBackMod backMod) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.code = code;
        this.name = name;
        this.backMod = backMod;
    }

    void init() {
        SQLiteDatabase mdb = POSDatabase.getMenuItem(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        try {
            Cursor cMod = getAllRecords_MODIFIER_particular(code, mdb);
            if (cMod != null && cMod.moveToFirst()) {
                do {
                    ModifierItem item = new ModifierItem(
                            cMod.getString(cMod
                                    .getColumnIndex(DBConstants.KEY_MODIFIER_CODE)),
                            cMod.getString(cMod
                                    .getColumnIndex(DBConstants.KEY_MODIFIER_NAME)),
                            cMod.getString(cMod
                                    .getColumnIndex(DBConstants.KEY_MODIFIER_ITEM_CODE)));
                    modifierItemList.add(item);

                } while (cMod.moveToNext());
            }
            mdb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

    }

    private Cursor getAllRecords_MODIFIER_particular(String modifier_item_code,
                                                     SQLiteDatabase mdb) {

        String queryModifer = "SELECT * FROM " + DBConstants.KEY_MODIFIER_TABLE
                + " WHERE " + DBConstants.KEY_MODIFIER_ITEM_CODE + "='"
                + modifier_item_code + "'";

        return mdb.rawQuery(queryModifer, null);
    }

    @SuppressLint("InflateParams")
    public View addView() {
        init();
        View v = inflater.inflate(R.layout.modifier_layout, null);
        txtViewModifierer = (CustomTextview) v
                .findViewById(R.id.txtViewModifierer);

        if (!TextUtils.isEmpty(name))
            txtViewModifierer.setText(context.getResources().getString(
                    R.string.modifier_popup_string) + " " + name);

        listViewModifer = (ListView) v.findViewById(R.id.listViewModifer);
        arrayAdapter = new ModifierAdapter<>(context);
        arrayAdapter.addDataSetList(modifierItemList);
        listViewModifer.setAdapter(arrayAdapter);
        listViewModifer.setOnItemClickListener(this);

        txtAddModi = (CustomTextview) v.findViewById(R.id.txt_proceed);
        txtAddModi.setOnClickListener(this);
        txtCancelModi = (CustomTextview) v.findViewById(R.id.txt_skip);
        txtCancelModi.setOnClickListener(this);

        if (!TextUtils.isEmpty(userInfo.getTheme_Font_Color())) {
            txtAddModi.setTextColor(Color.parseColor(userInfo
                    .getTheme_Font_Color()));
            txtCancelModi.setTextColor(Color.parseColor(userInfo
                    .getTheme_Font_Color()));
        }

        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txt_proceed:

                String modiName = "";
                int count = arrayAdapter.getCount();
                for (int i = 0; i < count; i++) {

                    ModifierItem item = (ModifierItem) arrayAdapter.getItem(i);
                    if (item.isSelected) {

                        if (!TextUtils.isEmpty(modiName))
                            modiName += item.modiName;
                        else
                            modiName = item.modiName;
                    }
                }

                if (!TextUtils.isEmpty(modiName))
                    backMod.addItem(modiName, StaticConstants.MODIFIER_POPUP_TAG);
                else
                    Toast.makeText(context,
                            "Please Select Atleast One Modifier !!",
                            Toast.LENGTH_SHORT).show();

                break;

            case R.id.txt_skip:
                backMod.removeView(StaticConstants.MODIFIER_POPUP_TAG);
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {

        ModifierItem item = (ModifierItem) arrayAdapter.getItem(position);
        item.isSelected = !item.isSelected;

        arrayAdapter.setDataSetItem(position, item);
        arrayAdapter.notifyDataSetChanged();

    }

    public interface ICallBackMod {

        /**
         * Add DataSet to TakeOrderAdapter
         *
         */
        void addItem(String name, String Tag);

        /**
         * Remove View from Layout
         *
         * @param Tag
         */
        void removeView(String Tag);
    }

    public class ModifierAdapter<T> extends POSAdapter<T> {

        public ModifierAdapter(Context context) {
            super(context);
        }

        @SuppressLint("InflateParams")
        @SuppressWarnings("unchecked")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ModifierViewHolder holder;
            if (convertView == null) {
                holder = new ModifierViewHolder();
                convertView = inflater.inflate(R.layout.modifier_row_layout,
                        null);
                holder.txtModi = (CustomTextview) convertView
                        .findViewById(R.id.txtModiView);
                holder.imgSelector = (ImageView) convertView
                        .findViewById(R.id.imgModiSelector);
                convertView.setTag(holder);
            } else
                holder = (ModifierViewHolder) convertView.getTag();

            ModifierItem item = (ModifierItem) dataSet.get(position);
            holder.txtModi.setText(item.modiName);
            holder.imgSelector.setSelected(item.isSelected);

            return convertView;
        }

        private class ModifierViewHolder {

            CustomTextview txtModi;
            ImageView imgSelector;
        }

    }

}
