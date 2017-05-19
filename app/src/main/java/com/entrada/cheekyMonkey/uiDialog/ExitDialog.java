package com.entrada.cheekyMonkey.uiDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.ICallBackFinish;
import com.entrada.cheekyMonkey.dynamic.Network_Info.ActivateUserIDTask;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

public class ExitDialog extends Dialog {

    Context context;
    ICallBackFinish callBackFinish;

    public ExitDialog(Context context, ICallBackFinish callBackFinish) {
        super(context);
        this.callBackFinish = callBackFinish;
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exit_layout);

        CustomTextview yes = (CustomTextview) findViewById(R.id.tv_yes);
        CustomTextview no = (CustomTextview) findViewById(R.id.tv_cancel);

        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                callBackFinish.onFinishDialog();
                updateUserStatus("I");
            }

        });

        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
    }

    public void updateUserStatus(String status){

        if (! UserInfo.guest_id.isEmpty()){

            String parameter = UtilToCreateJSON.createGuestStatus(UserInfo.guest_id, status);   // status A means User is Active
            String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
            ActivateUserIDTask activateUserIDTask = new ActivateUserIDTask(context,parameter,serverIP);
            activateUserIDTask.execute();
        }
            UserInfo.appIsRunning = false;
    }

}
