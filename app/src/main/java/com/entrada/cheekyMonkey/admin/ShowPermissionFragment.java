package com.entrada.cheekyMonkey.admin;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;

/**
 * Created by Administrator on 6/27/2017.
 */

public class ShowPermissionFragment extends Fragment {
Context context;

    String stringPermission[] = {
            "1. Order",
            "2. Order Cancel ",
            "3. Order Modify",
            "4. Order Split",
            "5. Bill",
            "6. Bill Cancel ",
            "7. Bill Modify",
            "8. Bill Split",
            "9. Table in Restaurant",
            "10. Settlement",
            "11. Undo Settlement",
            "12. Generate Bill",
            "13. Discount",
            "14. Order Under Process",
            "15. Order Accepted",
            "16. Order Rejected",
            "17. Cover",

    };

    String stringID[] = {
            "OG",
            "OC",
            "OM",
            "OS",
            "BI",
            "BC",
            "BM",
            "BS",
            "TR",
            "ST",
            "US",
            "GB",
            "DT",
            "OUP",
            "OA",
            "OR",
            "CR"
    };

    ListView listView;
    ShowPermissionAdapter showPermisssion_adapter;
    CheckBox checkBoxcheckall;
    TextView textViewSave;
    ImageView imageViewBack;
    String outletID = "", userID = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    public static ShowPermissionFragment newInstance(String outletID, String userID) {

        Bundle args = new Bundle();
        args.putString("outletID", outletID);
        args.putString("userID", userID);
        ShowPermissionFragment fragment = new ShowPermissionFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_permission_list, container, false);


        listView = (ListView) view.findViewById(R.id.lv_admin_custom);
        checkBoxcheckall = (CheckBox) view.findViewById(R.id.admin_chk_checkall);
        textViewSave = (TextView) view.findViewById(R.id.tv_outlet_save);
        imageViewBack = (ImageView) view.findViewById(R.id.image_back);


        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddEmployeeActivity activity = (AddEmployeeActivity) getActivity();
                activity.frameLayout.setVisibility(View.GONE);
            }
        });

        textViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                permissionDelete();
                boolean[] array = showPermisssion_adapter.getCheckedArray();

                for (int i = 0; i < array.length; i++) {

                    if (array[i]) {
                        String value = stringID[i];
                        permissionInsert(value);
                    }
                }

                AddEmployeeActivity activity = (AddEmployeeActivity) getActivity();
                activity.frameLayout.setVisibility(View.GONE);
            }

        });

        Bundle bundle = getArguments();
        outletID = bundle.getString("outletID");
        userID = bundle.getString("userID");
        showPermisssion_adapter = new ShowPermissionAdapter(context, stringPermission,bundle);
        listView.setAdapter(showPermisssion_adapter);

        checkBoxcheckall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                showPermisssion_adapter.setCheckAll(isChecked);
                showPermisssion_adapter.notifyDataSetChanged();

            }
        });

        return view;

    }

    private void permissionDelete() {


        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(
                context).getWritableDatabase();
        mdb.beginTransaction();

        try {

            String whereClause = DBConstants.KEY_ADMIN_TABLE_PERMISSION_OUTLET_ID + "= ? and " +
                    DBConstants.KEY_ADMIN_TABLE_PERMISSION_USER_ID + "= ? ";
            mdb.delete(DBConstants.KEY_ADMIN_TABLE_PERRMISSION, whereClause, new String[]{outletID, userID});
            mdb.setTransactionSuccessful();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }


    }


    private void permissionInsert(String value) {


        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(
                context).getWritableDatabase();
        mdb.beginTransaction();
        try {

            ContentValues cv = new ContentValues();
            cv.put(DBConstants.KEY_ADMIN_TABLE_PERMISSION_OUTLET_ID, outletID);
            cv.put(DBConstants.KEY_ADMIN_TABLE_PERMISSION_USER_ID, userID);
            cv.put(DBConstants.KEY_ADMIN_TABLE_PERMISSION_TYPE, value);

            mdb.insert(DBConstants.KEY_ADMIN_TABLE_PERRMISSION, null, cv);
//                mdb.update(DBConstants.KEY_OUTLET_TABLE_NAME,cv, DBConstants.KEY_OUTLET_TABLE_OUTLET_CODE + "= '"+outletID+"'", null);


            mdb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }


    }

}
