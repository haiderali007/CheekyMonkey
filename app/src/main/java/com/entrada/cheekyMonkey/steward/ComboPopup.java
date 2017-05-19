package com.entrada.cheekyMonkey.steward;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.steward.discount.DiscountLayout;
import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;
import com.entrada.cheekyMonkey.ui.CustomTextview;

import java.util.ArrayList;
import java.util.List;


public class ComboPopup implements View.OnClickListener, AdapterView.OnItemClickListener {


    public Context context;
    public LayoutInflater inflater;
    public IcallBackCombo icallBackCombo;
    public List<Category> categories;
    public ExpandableAdapter exPandCombo;
    ExpandableListView exListView;
    CustomTextview ctvSave, ctvCancel;
    UserInfo userInfo;
    String code = "", name = "", grp_code = "";
    DiscountLayout discountLayout;
    public ComboPopup(Context context, String code, String name,
                      final String grp_code, IcallBackCombo icallBackCombo,
                      DiscountLayout discountLayout) {
        this.context = context;
        this.code = code;
        this.name = name;
        this.grp_code = grp_code;
        this.icallBackCombo = icallBackCombo;
        this.discountLayout = discountLayout;
        inflater = LayoutInflater.from(context);
    }

    private List<ItemDetail> createItems(String code, String cat_code, int num,
                                         SQLiteDatabase mdb) {
        List<ItemDetail> result = new ArrayList<ItemDetail>();

        Cursor c2 = getAllRecords_COMBO_particular_menuitem(mdb, code, cat_code);

        if (c2.moveToFirst()) {

            do {
                ItemDetail item = new ItemDetail(c2.getPosition(),
                        c2.getString(2), c2.getString(3), c2.getFloat(4), false);
                result.add(item);
            } while (c2.moveToNext());

        }

        return result;
    }

    public View addView() {
        initData(code);
        View v = inflater.inflate(R.layout.combo_layout, null);
        exListView = (ExpandableListView) v.findViewById(R.id.exPandCombo);
        exListView.setIndicatorBounds(10, 20);
        exPandCombo = new ExpandableAdapter(categories, context);
        exListView.setAdapter(exPandCombo);

        ctvSave = (CustomTextview) v.findViewById(R.id.txtOkCombo);
        ctvSave.setOnClickListener(this);
        ctvCancel = (CustomTextview) v.findViewById(R.id.txtCancelCombo);
        ctvCancel.setOnClickListener(this);
        userInfo = POSApplication.getSingleton().getmDataModel().getUserInfo();
        if (!TextUtils.isEmpty(userInfo.getTheme_Font_Color())) {
            ctvSave.setTextColor(Color.parseColor(userInfo
                    .getTheme_Font_Color()));
            ctvCancel.setTextColor(Color.parseColor(userInfo
                    .getTheme_Font_Color()));
        }

        return v;
    }

    private void initData(String combo_code) {
        categories = new ArrayList<Category>();

        SQLiteDatabase mdb = POSDatabase.getMenuItem(context)
                .getWritableDatabase();
        mdb.beginTransaction();
        try {
            Cursor cursor = mdb.rawQuery("Select " + "distinct (" + ""
                    + DBConstants.KEY_COMBO_CAT_CODE + ") from "
                    + DBConstants.KEY_COMBO_TABLE + " where "
                    + DBConstants.KEY_COMBO_CODE + "= '" + combo_code
                    + "' and " + DBConstants.KEY_COMBO_FLAG + " = 'O'", null);

            if (cursor.moveToFirst()) {
                do {

                    Cursor c2 = mdb
                            .rawQuery("Select " + "" + DBConstants.KEY_COMBO_CAT_CODE + " , "
                                    + DBConstants.KEY_COMBO_CAT_NAME + "," + DBConstants.KEY_COMBO_CODE + "," + DBConstants.KEY_COMBO_NAME
                                    + ", MAX(" + DBConstants.KEY_COMBO_MAX_QTY + ") from " + DBConstants.KEY_COMBO_TABLE
                                    + " where " + DBConstants.KEY_COMBO_CODE + "= '" + combo_code + "' and "
                                    + DBConstants.KEY_COMBO_FLAG + " = 'O' and " + DBConstants.KEY_COMBO_CAT_CODE + "='"
                                    + cursor.getString(cursor.getColumnIndex(DBConstants.KEY_COMBO_CAT_CODE)) + "'", null);

                    if (c2.moveToFirst()) {
                        Log.i("cat", cursor.getString(0));

                        Category cat1 = new Category(c2.getPosition() + 1,
                                c2.getString(0), c2.getString(1),
                                c2.getFloat(4));

                        cat1.setItemList(createItems(combo_code,
                                cursor.getString(0), c2.getCount(), mdb));

                        categories.add(cat1);
                    }

                } while (cursor.moveToNext());
            }

            mdb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txtOkCombo:

                ArrayList<OrderItem> orderItemsList = new ArrayList<>();

                ItemDetail obj_order1;
                Category cat;
                boolean set = false;

                if (!exPandCombo.isEmpty()) {

                    for (int g = 0; g < exPandCombo.getGroupCount(); g++) {
                        int IntC = 0;
                        cat = (Category) exPandCombo.getGroup(g);

                        for (int child = 0; child < exPandCombo.getChildrenCount(g); child++) {

                            obj_order1 = (ItemDetail) exPandCombo.getChild(g, child);

                            if (obj_order1.ischecked) {

                                IntC = IntC + 1;

                                OrderItem obj_order = new OrderItem();
                                obj_order.o_code = obj_order1.code;
                                obj_order.o_name = "##" + obj_order1.name;
                                obj_order.o_quantity = obj_order1.quantity;
                                obj_order.o_grp_code = grp_code;

                                orderItemsList.add(obj_order);

                                if (IntC > cat.max) {
                                    Toast.makeText(context, "You have selected more than "
                                            + (int) cat.max + " Items for Category "
                                            + cat.name + ".", Toast.LENGTH_SHORT).show();
                                    set = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (!set) {
                        if (orderItemsList.isEmpty()) {
                            Toast.makeText(context, "Select Open Combo First !!", Toast.LENGTH_SHORT).show();
                            break;
                        } else
                            icallBackCombo.addItem(orderItemsList, StaticConstants.COMBO_POPUP_TAG);

                        for (int i = 0; i < orderItemsList.size(); i++)
                            discountLayout.createDiscList("0", "", 0f);
                    }
                }

                break;


            case R.id.txtCancelCombo:

                icallBackCombo.removeView(StaticConstants.COMBO_POPUP_TAG);
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public Cursor getAllRecords_COMBO_particular_menuitem(SQLiteDatabase mdb,
                                                          String combo_code, String combo_cat_code) {

        return mdb.rawQuery("Select " + DBConstants.KEY_COMBO_CAT_CODE +
                " , " + DBConstants.KEY_COMBO_CAT_NAME + "," + DBConstants.KEY_COMBO_CODE + ","
                + DBConstants.KEY_COMBO_NAME + "," + DBConstants.KEY_COMBO_QTY + " from "
                + DBConstants.KEY_COMBO_TABLE + " where " + DBConstants.KEY_COMBO_CODE
                + "= '" + combo_code + "' and " + DBConstants.KEY_COMBO_FLAG
                + " = 'O' and " + DBConstants.KEY_COMBO_CAT_CODE + "='" + combo_cat_code
                + "'", null);

    }

    public interface IcallBackCombo {

        void addItem(ArrayList<OrderItem> orderItemsList, String Tag);

        /**
         * Remove View from layout
         */
        void removeView(String Tag);

    }

    public class Category {

        private long id;
        private String code;
        private String name;
        private float max;
        private List<ItemDetail> itemList = new ArrayList<ItemDetail>();

        public Category(long id, String c, String n, float m) {
            this.id = id;
            this.code = c;
            this.name = n;
            this.max = m;
        }

        public List<ItemDetail> getItemList() {
            return itemList;
        }

        public void setItemList(List<ItemDetail> itemList) {
            this.itemList = itemList;
        }

    }

    public class ItemDetail {

        float quantity;
        boolean ischecked;
        private long id;
        private String code, name;

        public ItemDetail(long id, String c, String n, float q, boolean check) {
            this.id = id;
            this.code = c;
            this.name = n;
            this.quantity = q;
            this.ischecked = check;
        }
    }

    public class ExpandableAdapter extends BaseExpandableListAdapter {

        private List<Category> catList;
        private int itemLayoutId;
        private int groupLayoutId;
        private Context ctx;

        public ExpandableAdapter(List<Category> catList, Context ctx) {

            this.itemLayoutId = R.layout.item_layout;
            this.groupLayoutId = R.layout.group_layout;
            this.catList = catList;
            this.ctx = ctx;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return catList.get(groupPosition).getItemList().get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return catList.get(groupPosition).getItemList().get(childPosition).hashCode();
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            // View v = convertView;
            Item_detail_holder holder;

            if (convertView == null) {
                holder = new Item_detail_holder();
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_layout, parent, false);

                holder.t_q = (TextView) convertView.findViewById(R.id.t_v_q_for_i_l);
                holder.ch = (CheckBox) convertView.findViewById(R.id.c_b_for_i_l);
                convertView.setTag(holder);

            } else
                holder = (Item_detail_holder) convertView.getTag();

            holder.item_detail_holder = catList.get(groupPosition).getItemList().get(childPosition);
            ItemDetail det = catList.get(groupPosition).getItemList().get(childPosition);
            holder.t_q.setText(String.valueOf(holder.item_detail_holder.quantity));
            holder.ch.setChecked(holder.item_detail_holder.ischecked);
            holder.ch.setOnCheckedChangeListener(oncheck(holder));
            holder.ch.setOnCheckedChangeListener(oncheck(holder));
            holder.ch.setText(holder.item_detail_holder.name);
            holder.ch.setTag(holder.item_detail_holder);
            holder.t_q.setTag(holder.item_detail_holder);

            return convertView;

        }

        private OnCheckedChangeListener oncheck(final Item_detail_holder holder) {
            return new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        holder.item_detail_holder.ischecked = isChecked;
                        holder.ch.setChecked(isChecked);

                    } else {
                        holder.item_detail_holder.ischecked = isChecked;
                        holder.ch.setChecked(isChecked);

                    }
                    exPandCombo.notifyDataSetChanged();
                }
            };
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            int size = catList.get(groupPosition).getItemList().size();
            System.out.println("Child for group [" + groupPosition + "] is [" + size + "]");
            return size;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return catList.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return catList.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return catList.get(groupPosition).hashCode();
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {

            // View v = convertView;
            Categry_holder holder_cat;

            if (convertView == null) {

                holder_cat = new Categry_holder();
                LayoutInflater inflater = (LayoutInflater) ctx
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.group_layout, parent, false);
                holder_cat.t_cat = (TextView) convertView.findViewById(R.id.groupName);
                holder_cat.t_max = (TextView) convertView.findViewById(R.id.t_v_max_q);
                convertView.setTag(holder_cat);

            } else
                holder_cat = (Categry_holder) convertView.getTag();


            // TextView groupDescr = (TextView) v.findViewById(R.id.groupDescr);

            holder_cat.cat_detail_holder = catList.get(groupPosition);
            Category cat = catList.get(groupPosition);

            holder_cat.t_cat.setText(holder_cat.cat_detail_holder.name);
            holder_cat.t_max.setText("Max. Items(" + (int) (holder_cat.cat_detail_holder.max) + ")");
            holder_cat.t_cat.setTag(holder_cat.cat_detail_holder);
            holder_cat.t_max.setTag(holder_cat.cat_detail_holder);
            return convertView;

        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public class Categry_holder {

            Category cat_detail_holder;

            TextView t_cat;
            TextView t_max;
        }

        public class Item_detail_holder {

            ItemDetail item_detail_holder;

            TextView t_q;
            CheckBox ch;

        }

    }

}
