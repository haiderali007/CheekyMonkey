package com.entrada.cheekyMonkey.steward.other;

import android.content.Context;

import com.entrada.cheekyMonkey.Validator.Validation;


abstract class BaseValidation implements Validation {

    protected Context mContext;

    protected BaseValidation(Context context) {
        mContext = context;
    }

}