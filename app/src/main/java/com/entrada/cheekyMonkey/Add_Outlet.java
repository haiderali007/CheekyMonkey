package com.entrada.cheekyMonkey;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.takeorder.entity.TableItem;

import java.util.ArrayList;

/**
 * Created by Administrator on 6/20/2017.
 */

public class Add_Outlet extends Activity {
    TextView textViewEditOutlet;
    ListView listViewEditOutlet;
    Button buttonEditOutlet;
    ImageView imageViewAddOutlet;
    ArrayList<String> arrayList = new ArrayList<>();
    Adapter_outlet adapter_outlet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adit_outlet);


        textViewEditOutlet = (TextView) findViewById(R.id.tv_edit_outlet);
        listViewEditOutlet = (ListView) findViewById(R.id.listView_edit_outlet);
        buttonEditOutlet = (Button) findViewById(R.id.btn_submit_edit_outlet);
        imageViewAddOutlet = (ImageView) findViewById(R.id.add_out_image);

//        arrayList.add("Cheeky Monkey, Panchkula");
//        arrayList.add("Cheeky Monkey, Ambala");
//        arrayList.add("Cheeky Monkey, Gurgaon");
        adapter_outlet = new Adapter_outlet(this, arrayList);
        listViewEditOutlet.setAdapter(adapter_outlet);


        imageViewAddOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_Outlet.this, Edit_Outlet_Details.class);
                startActivity(intent);
            }
        });

        fetchOutlets();
    }

    public void fetchOutlets()
    {

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(this).getWritableDatabase();
        mdb.beginTransaction();

        String queryPermission = "Select * from " + DBConstants.KEY_OUTLET_TABLE_NAME;
        Cursor c = mdb.rawQuery(queryPermission, null);
        OutletDetails outletDetails;

        try {
            if (c.moveToFirst()) {

                do {
                    outletDetails = new OutletDetails();
                    outletDetails.name= c.getString(c.getColumnIndex(DBConstants.KEY_OUTLET_TABLE_OUTLET_NAME));
//                    outletDetails.address= c.getString(c.getColumnIndex(DBConstants.KEY_OUTLET_TABLE_ADDRESS));
//                    outletDetails.city= c.getString(c.getColumnIndex(DBConstants.KEY_OUTLET_TABLE_CITY));
//                    outletDetails.pin= c.getString(c.getColumnIndex(DBConstants.KEY_OUTLET_TABLE_PIN));
                    arrayList.add(outletDetails.name);
                } while (c.moveToNext());
            }
            c.close();

            mdb.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

        adapter_outlet.notifyDataSetChanged();
    }



}
