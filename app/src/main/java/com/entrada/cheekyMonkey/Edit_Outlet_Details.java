package com.entrada.cheekyMonkey;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.twitter.sdk.android.core.models.TwitterCollection;

import org.json.JSONObject;

/**
 * Created by Administrator on 6/21/2017.
 */

public class Edit_Outlet_Details extends Activity {

    EditText editTextOutName, editTextAddress, editTextCity, editTextPin;
    ImageView imageViewEdit;
    Button buttonSave;
    String stringOutlateName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_outlet_details);

        editTextOutName = (EditText) findViewById(R.id.edt_outlet_name);
        editTextAddress = (EditText) findViewById(R.id.edt_outlet_address);
        editTextCity = (EditText) findViewById(R.id.edt_outlet_city);
        editTextPin = (EditText) findViewById(R.id.edt_outlet_pin);
        imageViewEdit = (ImageView) findViewById(R.id.img_edit_outlet);
        buttonSave = (Button) findViewById(R.id.btn_edit_save);

        editTextOutName.setEnabled(false);
        editTextAddress.setEnabled(false);
        editTextCity.setEnabled(false);
        editTextPin.setEnabled(false);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertOutletRecord();
                editTextOutName.setEnabled(true);
                editTextAddress.setEnabled(true);
                editTextCity.setEnabled(true);
                editTextPin.setEnabled(true);
                Intent intent = new Intent(Edit_Outlet_Details.this, Add_Outlet.class);
                startActivity(intent);

            }
        });

        imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextOutName.setEnabled(true);
                editTextAddress.setEnabled(true);
                editTextCity.setEnabled(true);
                editTextPin.setEnabled(true);
            }
        });
    }

    public void insertOutletRecord() {

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(
                this).getWritableDatabase();
        mdb.beginTransaction();
        try {

            ContentValues cv = new ContentValues();
            cv.put(DBConstants.KEY_OUTLET_TABLE_OUTLET_NAME, editTextOutName.getText().toString());
            cv.put(DBConstants.KEY_OUTLET_TABLE_ADDRESS, editTextAddress.getText().toString());
            cv.put(DBConstants.KEY_OUTLET_TABLE_CITY, editTextCity.getText().toString());
            cv.put(DBConstants.KEY_OUTLET_TABLE_PIN, editTextPin.getText().toString());


            mdb.insert(DBConstants.KEY_OUTLET_TABLE_NAME, null, cv);


            mdb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

    }


}

