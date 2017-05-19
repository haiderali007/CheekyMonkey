package com.entrada.cheekyMonkey.steward.other;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.Validator.Field;
import com.entrada.cheekyMonkey.appInterface.ICallBackFinish;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.dynamic.BaseFragmentActivity;
import com.entrada.cheekyMonkey.steward.StewardOrderFragment;
import com.entrada.cheekyMonkey.steward.loginFragment.AsyncLoginTask;
import com.entrada.cheekyMonkey.steward.loginFragment.ICallBackLoginResponse;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.staticData.PrefHelper;
import com.entrada.cheekyMonkey.staticData.ResultMessage;
import com.entrada.cheekyMonkey.ui.CustomEditView;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.uiDialog.ExitDialog;
import com.entrada.cheekyMonkey.util.Logger;
import com.entrada.cheekyMonkey.util.Util;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;


/**
 * Created by csat on 03/07/2015.
 */
public class CustomLoginPopup extends Dialog implements View.OnClickListener,
        ICallBackFinish, ICallBackLoginResponse {

    public StewardOrderFragment takeOrderFragment;
    Context context;
    CustomTextview txtCancel, txtSave;
    private CustomEditView editTextUserId, editTextPassword;
    private Form formUserId, formPassword;
    int previousIndex = 1;
    String userID = "";


    public CustomLoginPopup(Context context, StewardOrderFragment takeOrderFragment) {
        super(context);
        this.context = context;
        this.takeOrderFragment = takeOrderFragment;
    }

    public CustomLoginPopup(Context context, int position, String userID) {
        super(context);
        this.context = context;
        this.previousIndex = position ;
        this.userID= userID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.custom_login_layout);

        editTextUserId = (CustomEditView) findViewById(R.id.et_UserId);
        editTextPassword = (CustomEditView) findViewById(R.id.et_Password);

        formUserId = new Form(context);
        formUserId.addField(Field.using(editTextUserId).validate(NotEmpty.build(context)));

        formPassword = new Form(context);
        formPassword.addField(Field.using(editTextPassword).validate(NotEmpty.build(context)));

        txtCancel = (CustomTextview) findViewById(R.id.txtCancelLogin);
        txtSave = (CustomTextview) findViewById(R.id.txtSaveLogin);
        txtCancel.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        setCancelable(false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txtSaveLogin:

                if (formUserId.isValid() && formPassword.isValid()) {

                    String server_ip = getServerIP();

                    String parameter = UtilToCreateJSON.createLoginJSON(
                            editTextUserId.getText().toString(),
                            editTextPassword.getText().toString(),
                            Util.getWifiMACAddress(context), server_ip);

                    new AsyncLoginTask(context, server_ip, parameter, this).execute();
                }

                break;

            case R.id.txtCancelLogin:

                ExitDialog exitDialog = new ExitDialog(context, (BaseFragmentActivity) context);
                exitDialog.show();

                break;
        }
    }


    @Override
    public void getLoginResponse(String parameter, String response) {

        Logger.i(Logger.LOGGER_TAG, Logger.LOGGER_OP + response);

        if (response.contains("{")) {

            ResultMessage message = UtilToCreateJSON.parseLoginResponseNew(context, parameter, response);
            if (message.isSuccess) {

                if (takeOrderFragment != null)
                    takeOrderFragment.steward = editTextUserId.getText().toString();

                else{

                    PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.TEMP_USER_ID,
                            editTextUserId.getText().toString());
                    validateAuthorizedID();
                }

                dismiss();
            }

        } else
            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
    }


    public void validateAuthorizedID(){

        BaseFragmentActivity baseFragmentActivity = (BaseFragmentActivity)context;
        /*NavigationDrawerFragment navigationDrawerFragment = baseFragmentActivity.mNavigationDrawerFragment;
        int selectedIndex = navigationDrawerFragment.mDrawerListView.getSelectedItemPosition();

        if (selectedIndex == -1 && previousIndex != 1)
            baseFragmentActivity.onNavigationDrawerItemSelected(previousIndex);

        else*/ if (userID.equals("BG")){

            if (!baseFragmentActivity.UserPermission("BG"))
                ((BaseFragmentActivity) context).onCreateBillGenerate(UserInfo.table,UserInfo.tableCode);

        }else if (userID.equals("DI")){

            if (baseFragmentActivity.currentBackListener instanceof StewardOrderFragment){
               /* StewardOrderFragment takeOrderFragment = (StewardOrderFragment)baseFragmentActivity.currentBackListener ;
                takeOrderFragment.showDiscount();*/
            }
        }
    }

    public String getServerIP() {

        String server_ip = "";

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();
        try {

            Cursor cursor = mdb.query(DBConstants.KEY_USR_TABLE, new String[]{
                            DBConstants.KEY_USR_SERVER_ID}, null, null,
                    null, null, null);

            if (cursor.moveToFirst())
                server_ip = cursor.getString(0);

            cursor.close();
            mdb.setTransactionSuccessful();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

        return server_ip;
    }


    @Override
    public void onFinishDialog() {

    }

}
