package com.entrada.cheekyMonkey.steward;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.ICallBackFinish;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.ui.CustomButton;

import java.util.ArrayList;


/**
 * Created by csat on 03/07/2015.
 */
public class SelectStewardPopup extends Dialog implements View.OnClickListener, ICallBackFinish {

    public String redirect = "";
    public StewardOrderFragment takeOrderFragment;
    Context context;
    ListView listview_steward;
    CustomButton txtCancelSelectSteward, txtSaveSelectSteward;
    ArrayList<String> steward;
    ArrayList<String> emp_code;
    ArrayAdapter<String> adapter_select_steward;


    public SelectStewardPopup(Context context, StewardOrderFragment takeOrderFragment) {
        super(context);
        this.context = context;
        this.takeOrderFragment = takeOrderFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_steward_layout_popup);


        listview_steward = (ListView) findViewById(R.id.lv_steward);
        txtCancelSelectSteward = (CustomButton) findViewById(R.id.txtCancelSelectSteward);
        txtSaveSelectSteward = (CustomButton) findViewById(R.id.txtSaveSelectSteward);
        txtSaveSelectSteward.setOnClickListener(this);
        txtCancelSelectSteward.setOnClickListener(this);

        setCancelable(false);
        setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                }
                return true;
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Set_Adapter_for_steward_type();

            }
        }, 200);
    }

    public void Set_Adapter_for_steward_type() {

        steward = new ArrayList<>();
        emp_code = new ArrayList<>();

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        try {
            Cursor cursor = getAllRecords_STEWARD_TYPE(mdb);
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    //if (cursor.getString(2).equals( UserInfo.USER_ID))
                    emp_code.add(cursor.getString(0));
                    steward.add(cursor.getString(2));

                } while (cursor.moveToNext());

            }
            adapter_select_steward = new ArrayAdapter<>(context,
                    android.R.layout.simple_list_item_single_choice, steward);

            listview_steward.setAdapter(adapter_select_steward);
            int position = emp_code.indexOf(takeOrderFragment.steward);
            listview_steward.setItemChecked(position, true);

            mdb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }
    }

    public Cursor getAllRecords_STEWARD_TYPE(SQLiteDatabase mdb) {

        String pos = POSApplication.getSingleton().getmDataModel().getUserInfo().posItem.posCode;

        String queryCategory = "Select * from "
                + DBConstants.KEY_EMP_DETAIL + " where "
                + DBConstants.KEY_EMP_TYPE + "='STEWARD' AND "
                + "("
                + DBConstants.KEY_EMP_POS + "= '" + pos + "' or " + DBConstants.KEY_EMP_POS + " IS NULL or "
                + DBConstants.KEY_EMP_POS + "='' )";

        Cursor cursor = mdb.rawQuery(queryCategory, null);
        return cursor;


/*        return mdb.query(DBConstants.KEY_EMP_DETAIL, new String[]{
                        DBConstants.KEY_EMP_CODE, DBConstants.KEY_EMP_NAME, DBConstants.KEY_EMP_USER_ID},
                DBConstants.KEY_EMP_TYPE + "=" + "'STEWARD'", null, null, null, null);*/
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txtSaveSelectSteward:

                int position = listview_steward.getCheckedItemPosition();
                if (position != -1) {
                    listview_steward.setItemChecked(position, true);
                    if (takeOrderFragment != null){
                        takeOrderFragment.steward = steward.get(position);
                        showStewardPopup();
                    }

                    dismiss();
                }

                break;

            case R.id.txtCancelSelectSteward:

                dismiss();
                if (takeOrderFragment != null){
                    takeOrderFragment.steward = "";
                }
                //StewardOrderFragment.steward = "";
                //takeOrderFragment.popup_mandatory(redirect);
                break;
        }

    }

    public void showStewardPopup() {
        if (redirect.equals("ny") || (redirect.equals("yy")))
            takeOrderFragment.submitOrder();
    }

    @Override
    public void onFinishDialog() {

    }
}
