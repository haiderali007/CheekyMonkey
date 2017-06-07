package com.entrada.cheekyMonkey.dynamic;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.entity.CategoryItem;
import com.entrada.cheekyMonkey.entity.GroupItems;
import com.entrada.cheekyMonkey.entity.Items;
import com.entrada.cheekyMonkey.entity.MenuItem;
import com.entrada.cheekyMonkey.network.BaseNetwork;

/**
 * Created by Rahul on 05/06/2015.
 */
public class FetchStoredMenuItemsTask extends AsyncTask<String, ArrayList<Items>, ArrayList<Items>>
        implements DBConstants {

    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;
    ICalItemsResponse iCallResponse ;

    public FetchStoredMenuItemsTask(Context context, String parameter, String serverIP,
                                    ProgressBar mDialog, ICalItemsResponse iCallResponse) {

        this.context = context;
        this.mDialog = mDialog;
        this.parameter = parameter;
        this.serverIP = serverIP;
        this.iCallResponse = iCallResponse;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (! (mDialog == null || ((Activity) context).isFinishing())) {
            mDialog.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ArrayList<Items> doInBackground(String... params) {

        ArrayList<Items> TITLES = new ArrayList<>();
        String previous_cat = "";

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        String queryPermission = "Select * from " + DBConstants.ITEMS_DETAIL_TABLE;
        Cursor c = mdb.rawQuery(queryPermission, null);

        try {
            if (c.moveToFirst()) {

                do {

                    if (! previous_cat.equals(c.getString(c.getColumnIndex("cat_code")))) {

                        CategoryItem categoryItem = new CategoryItem();
                        categoryItem.setCategory_Code(c.getString(c.getColumnIndex("cat_code")));
                        categoryItem.setCategory_Name(c.getString(c.getColumnIndex("cat_desc")));
                        categoryItem.setCategory_Image_Url(BaseNetwork.defaultUrlMethod(serverIP,
                                "/Image/" + categoryItem.getCategory_Code() + ".png"));


                        ArrayList<MenuItem> menuItemList = new ArrayList<>();
                        MenuItem menuItem = new MenuItem();
                        menuItem.setMenu_code(c.getString(c.getColumnIndex("item_code")));
                        menuItem.setMenu_name(c.getString(c.getColumnIndex("item_desc")));
                        menuItem.setMenu_categ_code(c.getString(c.getColumnIndex("cat_code")));
                        menuItem.setInc_Rate(Float.parseFloat(c.getString(c.getColumnIndex("inc_rate"))));
                        menuItem.setMenu_price(Float.parseFloat(c.getString(c.getColumnIndex("cur_rate"))));
                        menuItem.setMax_Price(Float.parseFloat(c.getString(c.getColumnIndex("max_price"))));
                        menuItem.setMin_Price(Float.parseFloat(c.getString(c.getColumnIndex("min_price"))));
                        menuItemList.add(menuItem);

                        TITLES.add(new Items(new GroupItems(), categoryItem, menuItemList));
                        previous_cat = c.getString(c.getColumnIndex("cat_code"));

                    }else {

                        ArrayList<MenuItem> menuItemList = TITLES.get(TITLES.size()-1).getMenuItemList();
                        MenuItem menuItem = new MenuItem();
                        menuItem.setMenu_code(c.getString(c.getColumnIndex("item_code")));
                        menuItem.setMenu_name(c.getString(c.getColumnIndex("item_desc")));
                        menuItem.setMenu_categ_code(c.getString(c.getColumnIndex("cat_code")));
                        menuItem.setInc_Rate(c.getFloat(c.getColumnIndex("inc_rate")));
                        menuItem.setMenu_price(c.getFloat(c.getColumnIndex("cur_rate")));
                        menuItem.setMax_Price(c.getFloat(c.getColumnIndex("max_price")));
                        menuItem.setMin_Price(c.getFloat(c.getColumnIndex("min_price")));
                        menuItemList.add(menuItem);
                    }

                }while (c.moveToNext());

            }
            c.close();
            mdb.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mdb.endTransaction();
        }

        return TITLES;
    }


    @Override
    protected void onPostExecute(ArrayList<Items> result) {
        super.onPostExecute(result);
        if (mDialog != null)
            mDialog.setVisibility(View.GONE);

            iCallResponse.getItemsResponse(result);
    }
}