package com.entrada.cheekyMonkey.dynamic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import java.util.ArrayList;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.entity.CategoryItem;
import com.entrada.cheekyMonkey.entity.GroupItems;
import com.entrada.cheekyMonkey.entity.Items;
import com.entrada.cheekyMonkey.util.Logger;

/**
 * Created by ACER on 29/05/2016.
 */
public class SearchItemListener implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    Context context;
    TakeOrderFragment takeOrderFragment;
    private SearchView edit_search;
    ImageView img_srch;


    public SearchItemListener(Context context,TakeOrderFragment takeOrderFragment,
                              SearchView edit_search,ImageView img_srch){

        this.context = context;
        this.takeOrderFragment = takeOrderFragment;
        this.edit_search = edit_search;
        this.img_srch = img_srch;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        showResults(newText + "*");
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String mquery) {
        showResults(mquery + "*");
        return false;
    }

    @Override
    public boolean onClose() {

        edit_search.setVisibility(View.GONE);
        img_srch.setVisibility(View.VISIBLE);
        takeOrderFragment.adapter_menu_search.clear();
        takeOrderFragment.listViewSearchItem.setVisibility(View.GONE);
        return true;
    }

    private void showResults(String query) {

        Logger.i("Key Pressed", "::" + query);
        SQLiteDatabase mdb = POSDatabase.getMenuItem(context)
                .getWritableDatabase();
        mdb.beginTransaction();
        try {
            //Cursor cursor = mdb.searchMenuCursor((query != null ? query.toString() : "@@@@"));

            String pos = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posCode;

            String mquery = "SELECT *" + " from Menu_Item_Table_fts where " + DBConstants.KEY_MENU_NAME + " MATCH '"
                    + query + "' " +  ";";

            String query_code = "SELECT * from Menu_Item_Table_fts where "
                    + DBConstants.KEY_MENU_CODE + " MATCH '" + query + "'" + ";";


            Logger.i("Search Query ", "::" + mquery);
            takeOrderFragment.adapter_menu_search.clear();

            Cursor cursor = mdb.rawQuery(mquery, null);
            Cursor cursor1 = mdb.rawQuery(query_code, null);

            if (cursor1.getCount() > 0)
                cursor = cursor1;

            if (cursor != null && cursor.getCount() > 0) {

                ArrayList<Items> menu_search_list = new ArrayList<>();

                if (cursor.moveToFirst()) {

                    do {

                        Log.i("result cursor", cursor.getString(2));

                        com.entrada.cheekyMonkey.entity.MenuItem menuItem = new com.entrada.cheekyMonkey.entity.MenuItem();

                        menuItem.setMenu_code(cursor.getString(cursor
                                .getColumnIndex(DBConstants.KEY_MENU_CODE)));
                        menuItem.setMenu_name(cursor.getString(cursor
                                .getColumnIndex(DBConstants.KEY_MENU_NAME)));
                        menuItem.setMenu_price(cursor.getFloat(cursor
                                .getColumnIndex(DBConstants.KEY_MENU_PRICE)));
                        menuItem.setMenuAmount(cursor.getFloat(cursor
                                .getColumnIndex(DBConstants.KEY_MENU_PRICE)));

                        menu_search_list.add(new Items(new GroupItems(), new CategoryItem(), menuItem));


                    } while (cursor.moveToNext());

                }

                Logger.i("ArrayList size", "::" + menu_search_list.size());
                takeOrderFragment.adapter_menu_search.addAll(menu_search_list);
                takeOrderFragment.listViewSearchItem.setVisibility(View.VISIBLE);
            }

            if (cursor != null){
                cursor.close();
                cursor1.close();
            }

            mdb.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            mdb.endTransaction();
        }
    }

}
