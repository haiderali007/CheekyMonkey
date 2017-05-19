package com.entrada.cheekyMonkey.dynamic.start;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import com.entrada.cheekyMonkey.ui.CustomTextview;

/**
 * Created by CSAT on 09/04/2016.
 */
public class FormData {

    boolean flag = true;
    ArrayList<Object> mObjects = new ArrayList<>();
    ArrayList<String> mStrings = new ArrayList<>();


    public void addField(Object object){
        mObjects.add(object);
        mStrings.add("");
    }


    public void addField(EditText editText, String usedFor){
        mObjects.add(editText);
        mStrings.add(usedFor);
    }



    public boolean isValid(){

        flag = true ;
        boolean result = true;

        for (int i = 0; i< mObjects.size(); i++)
            result &= isValid(mObjects.get(i), i);

        return result;
    }



    public boolean isValid(Object object, int index){

        if (object instanceof Spinner){

            Spinner spinner = (Spinner)object ;
            TextView textView = (TextView)spinner.getSelectedView();
            if (textView.getText().toString().isEmpty()){
                textView.setError("anything here, just to add the icon");
                return false ;
            }

        } else if (object instanceof EditText){

            EditText editText = (EditText) object;

            if (editText.getText().toString().isEmpty()) {
                setErrMessage(editText, "Field can't be empty");
                return false;

            } else if (mStrings.get(index).equals("E")) {

                if (!editText.getText().toString().contains("@") ||
                        !editText.getText().toString().contains(".")) {
                    setErrMessage(editText, "Enter valid Email Id");
                    return false;
                }
            } else if (mStrings.get(index).equals("M")) {

                if (editText.getText().length() < 10) {
                    setErrMessage(editText, "Enter valid Mobile No");
                    return false;
                }
            }

        }else if (object instanceof CustomTextview){

            CustomTextview customTextview = (CustomTextview)object;
            if (customTextview.getText().toString().isEmpty()){
                customTextview.setError("Field can't be empty");
                return false ;
            }

        }else if (object instanceof TextView){

            TextView textView = (TextView)object;
            if (textView.getText().toString().isEmpty()){
                textView.setError("Field can't be empty");
                return false ;
            }

        }

        return true ;
    }


    public void setErrMessage(EditText editText, String message ){

        editText.setError(message);
        if (flag){

            editText.setSelection(editText.getText().length());
            editText.requestFocus();
            flag = false;
        }
    }
}