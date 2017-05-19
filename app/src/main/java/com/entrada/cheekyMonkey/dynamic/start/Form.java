package com.entrada.cheekyMonkey.dynamic.start;

import android.content.Context;
import android.widget.EditText;

import java.util.LinkedList;
import java.util.List;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.Validator.FieldValidationException;


public class Form {

    private List<EditText> mEditText = new LinkedList<>();
    private Context mActivity;

    public Form(Context activity) {
        this.mActivity = activity;
    }

    public void addField(EditText editText){
        mEditText.add(editText);
    }


    public boolean isValid() {
        boolean result = true;
        try
        {
            for (EditText editText : mEditText)
                result &= isValid(editText);

        } catch (FieldValidationException e) {
            result = false;

            EditText textView = e.getTextView();
            textView.requestFocus();
            textView.selectAll();

            textView.setError(e.getMessage());
            showErrorMessage(e.getMessage());
        }
        return result;
    }

    protected void showErrorMessage(String message) {
       // Crouton.makeText(mActivity, message, Style.ALERT).show();
    	//Util.showErrorNetworkDialog(mActivity,message);

    }

    public boolean isValid(EditText mTextView)throws FieldValidationException  {

        if (mTextView.getText().toString().isEmpty()) {
            String errorMessage = getErrorMessage();
            throw new FieldValidationException(errorMessage, mTextView);
        }

        return true;
    }

    public String getErrorMessage() {
        return mActivity.getString(R.string.field_could_not_be_empty);
    }
}