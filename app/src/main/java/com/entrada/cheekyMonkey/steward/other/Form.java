package com.entrada.cheekyMonkey.steward.other;

import android.content.Context;
import android.widget.EditText;

import com.entrada.cheekyMonkey.Validator.Field;
import com.entrada.cheekyMonkey.Validator.FieldValidationException;

import java.util.ArrayList;
import java.util.List;

public class Form {

    private List<Field> mFields = new ArrayList<Field>();
    private Context mActivity;

    public Form(Context activity) {
        this.mActivity = activity;
    }

    public void addField(Field field) {
        mFields.add(field);
    }

    public boolean isValid() {
        boolean result = true;
        try {
            for (Field field : mFields) {
                result &= field.isValid();
            }
        } catch (FieldValidationException e) {
            result = false;

            EditText textView = e.getTextView();
            textView.requestFocus();
            textView.selectAll();

            //  FormUtils.showKeyboard(mActivity, textView);
            textView.setError(e.getMessage());
            showErrorMessage(e.getMessage());
        }
        return result;
    }

    protected void showErrorMessage(String message) {
        // Crouton.makeText(mActivity, message, Style.ALERT).show();
        //Util.showErrorNetworkDialog(mActivity,message);

    }
}