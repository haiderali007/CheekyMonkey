package com.entrada.cheekyMonkey.steward.home_del;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;

import java.util.ArrayList;


/**
 * Created by CSATSPL on 22/12/2015.
 */

public class DelBoyLayout implements AdapterView.OnItemClickListener {

    public ArrayList<HomeItem> delBoy_list;
    View view = null;
    LayoutInflater layoutInflater;
    ProgressBar progressBarDelBoy;
    GridView gridViewDelBoy;
    Context context;
    ICallHome iCallHome;
    private DelBoyListAdapter delBoyListAdapter;

    public DelBoyLayout(Context context, ICallHome iCallHome) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.iCallHome = iCallHome;
    }

    public View getDelBoyPopupWindow() {

        view = layoutInflater.inflate(R.layout.del_boy_layout, null);
        gridViewDelBoy = (GridView) view.findViewById(R.id.gridViewDelBoy);
        progressBarDelBoy = (ProgressBar) view.findViewById(R.id.progressBarDelBoy);

        delBoyListAdapter = new DelBoyListAdapter(context);
        gridViewDelBoy.setAdapter(delBoyListAdapter);
        gridViewDelBoy.setOnItemClickListener(this);

        init();
        return view;
    }


    public void init() {

        delBoy_list = new ArrayList<>();

        SQLiteDatabase mdb = POSDatabase.getMenuItem(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        try {
            Cursor cursor = getAllRecords_STEWARD_TYPE(mdb);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    HomeItem homeItem = new HomeItem();
                    homeItem.setDelBoyId(cursor.getString(0));
                    homeItem.setDelBoyName(cursor.getString(1));

                    delBoy_list.add(homeItem);

                } while (cursor.moveToNext());

                delBoyListAdapter.addDataSetList(delBoy_list);
                progressBarDelBoy.setVisibility(View.GONE);
            }
            mdb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }
    }

    public Cursor getAllRecords_STEWARD_TYPE(SQLiteDatabase mdb) {

        return mdb.query(DBConstants.KEY_EMP_DETAIL, new String[]{
                        DBConstants.KEY_EMP_CODE, DBConstants.KEY_EMP_NAME, DBConstants.KEY_EMP_USER_ID},
                DBConstants.KEY_EMP_TYPE + "=" + "'DEL. BOY'", null, null, null, null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        iCallHome.homeDetail((HomeItem) delBoyListAdapter.getItem(position), "D");
    }
}