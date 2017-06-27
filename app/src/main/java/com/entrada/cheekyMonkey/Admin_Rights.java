package com.entrada.cheekyMonkey;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Administrator on 6/27/2017.
 */

public class Admin_Rights extends Activity implements View.OnClickListener {
    EditText editTextID, editTextName, editTextUID, editTextPassword;
    TextView textViewUserType, textViewExpireDate, textViewPermission;
    CheckBox checkBoxActive;
    final CharSequence[] stringType = {"Steward", "Kitchen", "Outlet"};
    public int mYear,mMonth,mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_rights);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        editTextID = (EditText) findViewById(R.id.et_adm_outletid);
        editTextName = (EditText) findViewById(R.id.edt_adm_outlet_name);
        editTextUID = (EditText) findViewById(R.id.edt_adm_outlet_uid);
        editTextPassword = (EditText) findViewById(R.id.et_adm_outlet_password);
        textViewUserType = (TextView) findViewById(R.id.tv_adm_outlet_usertype);
        textViewExpireDate = (TextView) findViewById(R.id.tv_adm_outlet_expireDate);
        textViewPermission = (TextView) findViewById(R.id.tv_adm_outlet_permission);
        checkBoxActive = (CheckBox) findViewById(R.id.ckb_adm_outlet_active);

        textViewUserType.setOnClickListener(this);
        textViewExpireDate.setOnClickListener(this);
        textViewPermission.setOnClickListener(this);



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_adm_outlet_usertype:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                dialog.setTitle("User Type").setItems(stringType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int postion) {
                        Toast.makeText(getApplicationContext(), " " + stringType[postion], Toast.LENGTH_LONG).show();
                        textViewUserType.setText(stringType[postion]);

                    }
                });
                dialog.show();
                break;
            case R.id.tv_adm_outlet_expireDate:
                final Calendar calendar=Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(java.util.Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textViewExpireDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                }, mDay, mMonth, mYear);
                datePickerDialog.show();

                break;
            case R.id.tv_adm_outlet_permission:
                Intent intent = new Intent(this, AdminPermissionList.class);
                startActivity(intent);
                break;


        }


    }
}
