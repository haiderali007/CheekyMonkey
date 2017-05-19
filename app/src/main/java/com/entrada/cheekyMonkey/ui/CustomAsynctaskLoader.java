package com.entrada.cheekyMonkey.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.entrada.cheekyMonkey.R;

public class CustomAsynctaskLoader {

    Dialog mDialog;

    public CustomAsynctaskLoader(Context context) {
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        mDialog.setContentView(R.layout.custom_progress_dialog_small);
        mDialog.setCancelable(false);
        // mDialog.show();
    }

    public void ShowDialog() {
        if (mDialog != null && !mDialog.isShowing())
            mDialog.show();
    }

    public void SetMessage(String message) {
        ((TextView) mDialog.findViewById(R.id.txt_message)).setText(message);
    }

    public void DismissDialog() {
        mDialog.dismiss();
    }

    public boolean isDialogShowing() {
        return mDialog != null && mDialog.isShowing();
    }
}
