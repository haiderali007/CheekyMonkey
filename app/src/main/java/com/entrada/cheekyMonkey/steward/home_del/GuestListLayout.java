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
public class GuestListLayout implements AdapterView.OnItemClickListener {

    public ArrayList<HomeItem> guest_list;
    View view ;
    LayoutInflater layoutInflater;
    ProgressBar progressBarGuest;
    GridView gridViewGuestName;
    Context context;
    ICallHome ICallHome;
    private GuestListAdapter guestListAdapter;

    public GuestListLayout(Context context, ICallHome ICallHome) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.ICallHome = ICallHome;
    }

    public View getGuestPopupWindow() {

        view = layoutInflater.inflate(R.layout.existing_guest_list_layout, null);

        gridViewGuestName = (GridView) view.findViewById(R.id.grdViewGuestList);
        progressBarGuest = (ProgressBar) view.findViewById(R.id.progressBarGuest);

        guestListAdapter = new GuestListAdapter(context);
        gridViewGuestName.setAdapter(guestListAdapter);
        gridViewGuestName.setOnItemClickListener(this);

        init();
        return view;
    }


    public void init() {

        guest_list = new ArrayList<>();

        SQLiteDatabase mdb = POSDatabase.getMenuItem(context).getWritableDatabase();
        mdb.beginTransaction();

        try {
            Cursor cursor = getAllRecords_GUEST_TYPE(mdb);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    HomeItem homeItem = new HomeItem();
                    homeItem.setGuestId(cursor.getString(0));
                    homeItem.setGuestName(cursor.getString(1));

                    guest_list.add(homeItem);

                } while (cursor.moveToNext());

                guestListAdapter.addDataSetList(guest_list);
                progressBarGuest.setVisibility(View.GONE);
            }
            mdb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

    }

    public Cursor getAllRecords_GUEST_TYPE(SQLiteDatabase mdb) {

        return mdb.query(DBConstants.KEY_GUEST_TYPE_TABLE, new String[]{
                        DBConstants.KEY_GUEST_TYPE_CODE, DBConstants.KEY_GUEST_TYPE_NAME}, null, null,

                null, null, null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ICallHome.homeDetail((HomeItem) guestListAdapter.getItem(position), "G");
    }
}