package com.entrada.cheekyMonkey.dynamic.start;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.staticData.PrefHelper;
import com.entrada.cheekyMonkey.task.ICallBackGuestProfileResponse;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.Logger;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rahul on 15/05/2016.
 */
public class SignInFragment extends Fragment implements ICallBackGuestProfileResponse {


    Context context;
    EditText et_email,et_pass ;
    TextView tv_submit;
    FormData form;
    ProgressBar progress_signin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(context).inflate(R.layout.signin_form,container,false);

        et_email = (EditText) view.findViewById(R.id.et_email_no);
        et_pass = (EditText) view.findViewById(R.id.et_pass);
        progress_signin = (ProgressBar) view.findViewById(R.id.progress_signin);

        ImageButton img_back = (ImageButton) view.findViewById(R.id.image_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainScreenFragment)getParentFragment()).onBackPress();
            }
        });

        tv_submit = (TextView) view.findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GuestSearch();
            }
        });

        form = new FormData();
        form.addField(et_email, "E");
        form.addField(et_pass);
        return view;
    }

    public void GuestSearch() {

        try {

            if (form.isValid()) {

                String parameter = UtilToCreateJSON.createGstSigninParameter(
                        et_email.getText().toString(), et_pass.getText().toString());
                String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();

                GuestSigninTask guestSigninTask = new GuestSigninTask(context, parameter, serverIP,progress_signin, this);
                guestSigninTask.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void responseGuestSearch(String response) {

        Logger.i("GuestSignInResponse", response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject jsonObject1 = jsonObject.getJSONObject("ECABS_GuestSignInResult");
            JSONArray jsonArray = jsonObject1.getJSONArray("ListGuestOrders");

            JSONObject jsonObject2 = jsonObject1.getJSONObject("GstSearch");
            JSONArray jsonArray2 = jsonObject2.getJSONArray("Gstmst");
            String error_msg = jsonObject2.getString("Error");


            if (error_msg.isEmpty()) {

                String guest_name = jsonArray2.getJSONObject(0).getString("Name");
                PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.SERVER_IP, UserInfo.ServerIP);
                PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.GUEST_ID, et_email.getText().toString());
                PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.GUEST_NAME, guest_name);

                ((MainScreenFragment)getParentFragment()).setUserType(et_email.getText().toString(),jsonArray);


            }else if (error_msg.equals("invalid_email"))
                showFailureDialog("Please enter valid Email ID");
            else if (error_msg.equals("invalid_pass"))
                showFailureDialog("Please enter valid Password");

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Connection Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void showFailureDialog(String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.layout_failure_msg, null);
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_msg.setText(message);
        CustomTextview btn_Ok = (CustomTextview) view.findViewById(R.id.btn_dismiss);
        builder.setView(view);

        final AlertDialog dialog = builder.create();

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });
        dialog.show();
    }


    @Override
    public void responseGuestSave(String response) {

    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.context = activity;
    }

}
