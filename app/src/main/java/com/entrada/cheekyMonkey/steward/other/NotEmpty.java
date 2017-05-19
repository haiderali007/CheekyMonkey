package com.entrada.cheekyMonkey.steward.other;

import android.content.Context;
import android.text.TextUtils;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.Validator.Validation;


public class NotEmpty extends BaseValidation {

    private NotEmpty(Context context) {
        super(context);
    }

    public static Validation build(Context context) {
        return new NotEmpty(context);
    }

    @Override
    public String getErrorMessage() {
        return mContext.getString(R.string.field_could_not_be_empty);
    }

    @Override
    public boolean isValid(String text) {
        return !TextUtils.isEmpty(text);
    }

    @Override
    public boolean isValid(String text, String text2) {
        // TODO Auto-generated method stub
        return false;
    }
}