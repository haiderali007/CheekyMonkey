package com.entrada.cheekyMonkey.steward;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.ICallBackFinish;
import com.entrada.cheekyMonkey.ui.CustomButton;


/**
 * Created by csat on 03/07/2015.
 */
public class CoverPopup extends Dialog implements View.OnClickListener, ICallBackFinish {

    public String redirect = "";
    public StewardOrderFragment takeOrderFragment;
    Context context;
    EditText txtCover;
    CustomButton txtCancel, txtOk;


    public CoverPopup(Context context, StewardOrderFragment takeOrderFragment) {
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

        setContentView(R.layout.cover_layout);
        txtCover = (EditText) findViewById(R.id.textView_Cover);
        txtCancel = (CustomButton) findViewById(R.id.txtCancelCover);
        txtOk = (CustomButton) findViewById(R.id.txtOkCover);
        txtCancel.setOnClickListener(this);
        txtOk.setOnClickListener(this);
        txtCover.setText(takeOrderFragment.cover);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txtOkCover:

                if (!txtCover.getText().toString().isEmpty()) {

                    dismiss();
                    if (takeOrderFragment != null){
                        show_cover_popup(redirect);
                    }
                }

                break;

            case R.id.txtCancelCover:

                takeOrderFragment.cover = "";
                dismiss();
                //takeOrderFragment.popup_mandatory(redirect);
                break;
        }
    }

    public void show_cover_popup(String redirect) {

        String cvr = txtCover.getText().toString();
        takeOrderFragment.cover = cvr;

        if (redirect.equals("yn"))
            takeOrderFragment.submitOrder();

        else if (redirect.equals("yy") && cvr.length() > 0) {
            SelectStewardPopup selectStewardPopup = new SelectStewardPopup(context, takeOrderFragment);
            selectStewardPopup.redirect = redirect;
            selectStewardPopup.takeOrderFragment = takeOrderFragment;
            selectStewardPopup.show();
        }
    }

    @Override
    public void onFinishDialog() {

    }


}
