package com.entrada.cheekyMonkey.steward.discount;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.steward.bill.AdapterManualDiscount;
import com.entrada.cheekyMonkey.steward.bill.BillGenerateFragment;
import com.entrada.cheekyMonkey.steward.bill.DiscountItem;
import com.entrada.cheekyMonkey.util.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Dev on 28/08/2015.
 */

public class DiscountLayout implements ICallDiscount {

    public Context context;
    public ListView listViewShowDiscount;
    public String poscode = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posCode,
            disc_group = "", disc_code = "", disc_per = "0";
    public boolean showList = true;
    /***********************
     * List For Item Details
     *******************************/

    public ArrayList<String> disc_array = new ArrayList<>();   // To Hold Discount(%) of each ordered item.
    public ArrayList<String> itemGroupList = new ArrayList<>();    // To Hold each ordered item Group.
    public ArrayList<String> amountList = new ArrayList<>();       // To Calculate disc Amount, Amt. of Each item requires.
    /************************
     * List For Discount Details
     ***************************/

    public ArrayList<String> disc_group_list = new ArrayList<>();     // Only Requires for Group wise -> fixed discount case.
    public ArrayList<String> disc_per_list = new ArrayList<>();      // To Get Disc (%), corresponding to these Groups.
    ICallDiscList iCallDiscList;
    View discView = null;
    LayoutInflater layoutInflater;
    AdapterManualDiscount adapterManualDiscount;
    String date = "";

    /*****************************************************************************/


    public DiscountLayout(Context context, ICallDiscList iCallDiscList) {
        this.context = context;
        this.iCallDiscList = iCallDiscList;
        layoutInflater = LayoutInflater.from(context);
    }

    /***************
     * Methods used to implement Discount Logic broadly
     ********/


    @Override
    public void createDiscList(String disc_per, String group_code, float amount) {

        disc_array.add(disc_per);
        itemGroupList.add(group_code);
        amountList.add("" + amount);
    }

    @Override
    public void updateDiscAmount(int position, float amount) {

        if (amountList.size() > position)
            amountList.set(position, "" + amount);
    }

    @Override
    public void removeItemDiscList(int position) {

        if (disc_array.size() > position) {

            disc_array.remove(position);
            itemGroupList.remove(position);
            amountList.remove(position);
        }
    }

    public void clearDiscList() {

        disc_array.clear();
        itemGroupList.clear();
        amountList.clear();
        disc_group_list.clear();
        disc_per_list.clear();
    }

    public String getDiscPer(int index) {

        return disc_array.size() > index ? disc_array.get(index) : "0";
    }

    public View addDiscLayout() {

        discView = layoutInflater.inflate(R.layout.discount_layout, null);
        listViewShowDiscount = (ListView) discView.findViewById(R.id.listViewDiscount);

        ArrayList<DiscountItem> disc_list = new ArrayList<>();
        adapterManualDiscount = new AdapterManualDiscount(context, R.layout.discount_show_list_layout, disc_list);
        listViewShowDiscount.setAdapter(adapterManualDiscount);

        listViewShowDiscount.setOnItemClickListener(on_item_discountListener());
        discount_list_show_background(disc_list);

        return discView;
    }


    public void discount_list_show_background(ArrayList<DiscountItem> disc_list) {

        //date = UserInfo.getRunningDate();
        date = "2014/12/12";

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
        mdb.beginTransaction();

        try {
            String queryDiscount = "Select * from "
                    + DBConstants.KEY_DISC_MST_TABLE + " WHERE "
                    + DBConstants.KEY_DISC_MST_POS_CODE + "='" + poscode + "' " + "and "
                    + DBConstants.KEY_DISC_MST_DISC_FLAG + "= 'M' " + " and "
                    + "'" + date + "'" + " between " + DBConstants.KEY_DISC_MST_FROM_DATE
                    + " and " + DBConstants.KEY_DISC_MST_TO_DATE;

            Cursor cursor = mdb.rawQuery(queryDiscount, null);

            if (cursor != null && cursor.moveToFirst()) {

                do {
                    if (!checkDow(cursor.getString(cursor.getColumnIndex
                            (DBConstants.KEY_DISC_MST_DISC_CODE))))
                        continue;

                    DiscountItem obj_disc_list = new DiscountItem();
                    obj_disc_list.setName(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_DISC_MST_DISC_DESC)));
                    obj_disc_list.setCode(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_DISC_MST_DISC_CODE)));
                    obj_disc_list.setOPN(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_DISC_MST_DISC_OPN)));
                    obj_disc_list.setFlat_flag(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_DISC_MST_DISC_FLAT_FLAG)));
                    obj_disc_list.setOGI(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_DISC_MST_DISC_OGI)));
                    obj_disc_list.setFL(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_DISC_MST_DISC_FL)));

                    disc_list.add(obj_disc_list);

                } while (cursor.moveToNext());
                cursor.close();
            }
            if (!disc_list.isEmpty()) {
                iCallDiscList.hideOrderVisibility();
                listViewShowDiscount.setVisibility(View.VISIBLE);
                adapterManualDiscount.notifyDataSetChanged();
                showList = true;
            } else {
                Toast.makeText(context, "No Discount List", Toast.LENGTH_SHORT).show();
                showList = false;
            }

            mdb.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            mdb.endTransaction();
        }
    }


    private AdapterView.OnItemClickListener on_item_discountListener() {
        // TODO Auto-generated method stub
        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub


                final AdapterManualDiscount.Discount_show_List_holder item_list_dis =
                        (AdapterManualDiscount.Discount_show_List_holder) arg1.getTag();

                disc_code = item_list_dis.list_obj.getCode();

                if (!disc_array.isEmpty() || iCallDiscList instanceof BillGenerateFragment) {
                    final String disc_OPN = item_list_dis.list_obj.getOPN();
                    final String Flat_flag = item_list_dis.list_obj.getFlat_flag();

                    final String disc_OGI = item_list_dis.list_obj.getOGI();
                    final String disc_FL = item_list_dis.list_obj.getFL();

                    disc_per = getFixedDisc_per(disc_code, disc_OGI, disc_FL);

                    if (disc_OPN.equalsIgnoreCase("F"))     // F- Fixed
                    {
                        if (!(iCallDiscList instanceof BillGenerateFragment)) {
                            setDiscArray(disc_per, disc_OGI, Flat_flag);
                        }
                        Toast.makeText(context, "Applied", Toast.LENGTH_SHORT).show();

                        iCallDiscList.showOrderVisibility();
                        listViewShowDiscount.setVisibility(View.GONE);
                    } else {
                        String title = "ENTER " + item_list_dis.list_obj.getName();

                        if (disc_OGI.equals("F"))
                            title = title + " (IN RUPEES) -";
                        else
                            title = title + " (IN PERCENTAGE) -";

                        AlertDialog.Builder objBuilder = new AlertDialog.Builder(
                                context);

                        objBuilder.setTitle(title);

                        final EditText edit_t_disc = new EditText(
                                context);

                        objBuilder.setView(edit_t_disc);
                        edit_t_disc.setInputType(InputType.TYPE_CLASS_NUMBER
                                | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                        objBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        String dis_per = edit_t_disc.getText().toString();

                                        if (!TextUtils.isEmpty(dis_per) &&
                                                (disc_OGI.equals("F") || Float.parseFloat(dis_per) <= 100)) {
                                            disc_per = dis_per;
                                            if (!(iCallDiscList instanceof BillGenerateFragment))
                                                setDiscArray(disc_per, disc_OGI, Flat_flag);

                                            iCallDiscList.showOrderVisibility();
                                            listViewShowDiscount.setVisibility(View.GONE);
                                            Toast.makeText(context, "Applied", Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(context, "Discount(%) must be < = 100.",
                                                    Toast.LENGTH_LONG).show();
                                    }
                                }
                        );

                        objBuilder.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub

                                    }
                                });

                        final AlertDialog dialog = objBuilder.create();
                        dialog.show();
                    }
                } else {
                    Toast.makeText(context, "Select Items First", Toast.LENGTH_SHORT).show();
                    iCallDiscList.showOrderVisibility();
                    listViewShowDiscount.setVisibility(View.GONE);
                }
            }
        };
    }


    public String getFixedDisc_per(String disc_code, String disc_OGI, String disc_FL) {
        disc_group = "";
        disc_group_list.clear();
        disc_per = "0";
        disc_per_list.clear();

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();

        mdb.beginTransaction();
        try {
            String queryDiscPer = "Select Disc_Item_Item_Code, Disc_Item_Disc_Per from "
                    + DBConstants.KEY_DISC_ITEM_TABLE + " WHERE "
                    + DBConstants.KEY_DISC_ITEM_POS_CODE + "= '" + poscode + "' and "
                    + DBConstants.KEY_DISC_ITEM_DISC_CODE + "= '" + disc_code + "' ";

            Cursor cursor = mdb.rawQuery(queryDiscPer, null);

            if (disc_OGI.equalsIgnoreCase("G")) {
                if (cursor != null && cursor.moveToFirst()) {

                    do {
                        disc_group_list.add(cursor.getString(0));
                        disc_per_list.add(cursor.getString(1));

                    } while (cursor.moveToNext());
                }
            } else if (disc_OGI.equalsIgnoreCase("F")) {
                disc_per = disc_FL;

                if (cursor != null && cursor.moveToFirst()) {

                    do {
                        disc_group_list.add(cursor.getString(0));

                    } while (cursor.moveToNext());
                }
            } else {
                if (cursor != null && cursor.moveToFirst()) {
                    disc_group = cursor.getString(0);
                    disc_per = cursor.getString(1);
                }
            }

            mdb.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mdb.endTransaction();
        }
        return disc_per;
    }


    public void setDiscArray(String disc_per, String disc_OGI, String Flat_flag) {

        if (disc_OGI.equalsIgnoreCase("I"))      // I- Items (All)
        {
            for (int i = 0; i < disc_array.size(); i++) {
                disc_array.set(i, disc_per);
            }
        } else if (disc_OGI.equalsIgnoreCase("G")) {
            for (int i = 0; i < itemGroupList.size(); i++) {
                if (disc_group_list.contains(itemGroupList.get(i))) {
                    if (!disc_per.equals("0"))  //  if(Group -> Open)
                    {
                        disc_array.set(i, disc_per);    //  (Then, One Percentage for all Group)
                    } else if (!disc_per_list.isEmpty())  // if (Group -> Fixed)
                    {
                        int index = disc_group_list.indexOf(itemGroupList.get(i));  // Getting index of each disc group.
                        disc_array.set(i, disc_per_list.get(index));    //  (Then, Diff. Perc. for diff Group)
                    }
                }
            }
        } else if (disc_OGI.equalsIgnoreCase("F")) {
            String disc_per_new = "";
            float total_amt = 0.0f;

            if (Flat_flag.equalsIgnoreCase("I")) {
                for (int i = 0; i < amountList.size(); i++) {
                    total_amt = total_amt + Float.parseFloat(amountList.get(i));
                }
                disc_per_new = "" + Float.parseFloat(disc_per) / total_amt * 100;

                for (int j = 0; j < disc_array.size(); j++) {
                    disc_array.set(j, disc_per_new);
                }
            } else if (Flat_flag.equalsIgnoreCase("G")) {
                ArrayList<Integer> groupIndex = new ArrayList<Integer>();

                for (int i = 0; i < itemGroupList.size(); i++) {
                    if (disc_group_list.contains(itemGroupList.get(i))) {
                        total_amt = total_amt + Float.parseFloat(amountList.get(i));
                        groupIndex.add(i);
                    }
                }

                disc_per_new = "" + Float.parseFloat(disc_per) / total_amt * 100;

                for (int j = 0; j < groupIndex.size(); j++) {
                    disc_array.set(groupIndex.get(j), disc_per_new);
                }
            }
        } else if (disc_OGI.equalsIgnoreCase("S"))     // S- Item Specific
        {
            disc_array.set(disc_array.size() - 1, disc_per);
        }

        Logger.i("DiscountArraySize: ", "" + disc_array.size());
        Logger.i("", "************************");
        for (int i = 0; i < disc_array.size(); i++) {
            Logger.i("Discount[" + i + "]=", "" + disc_array.get(i));
            Logger.i("AmountList[" + i + "]=", "" + amountList.get(i));
        }
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

}