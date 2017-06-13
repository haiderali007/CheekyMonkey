package com.entrada.cheekyMonkey.dynamic.start;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.entrada.cheekyMonkey.IntroductionScreen;
import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.OnBackPressInterface;
import com.entrada.cheekyMonkey.fabric.digits.DigitsMainActivityNew;
import com.entrada.cheekyMonkey.staticData.PrefHelper;
import com.entrada.cheekyMonkey.task.GuestProfileTask;
import com.entrada.cheekyMonkey.task.ICallBackGuestProfileResponse;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

/**
 * Created by Rahul on 15/05/2016.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener,
        ICallBackGuestProfileResponse, OnBackPressInterface {


    Context context;
    EditText et_fname, et_lname, et_email, et_password ;
    EditText et_phone;
    ImageView img_validate_phone;
    TextView tv_continue, tv_submit;
    String guest_name = "", phone = "", email = "";
    FormData formData;
    RadioGroup rgAgeConfirmation;
    LinearLayout layout_continue, layout_signup;
    ProgressBar progress_signup;

    boolean MOBILE_VERIFICATION_DONE = true;
    int REQUEST_CODE = 48;
    int SUCCESS = 50;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(context).inflate(R.layout.signup_form,container,false);

        ImageButton img_back = (ImageButton) view.findViewById(R.id.image_back);
        layout_continue = (LinearLayout) view.findViewById(R.id.layout_signup1);
        layout_signup = (LinearLayout) view.findViewById(R.id.layout_signup2);
        et_fname = (EditText) view.findViewById(R.id.et_fname);
        et_lname = (EditText) view.findViewById(R.id.et_lname);
        et_phone = (EditText) view.findViewById(R.id.et_mobile);
        img_validate_phone = (ImageView) view.findViewById(R.id.img_validate_phone);
        et_email = (EditText) view.findViewById(R.id.et_email_no);
        et_password = (EditText) view.findViewById(R.id.et_pass);
        rgAgeConfirmation = (RadioGroup) view.findViewById(R.id.rg_age_confirm);
        tv_continue = (TextView) view.findViewById(R.id.tv_continue);
        tv_submit = (TextView) view.findViewById(R.id.tv_submit);

        progress_signup = (ProgressBar) view.findViewById(R.id.progress_signup);

        //et_phone = (EditText) view.findViewById(R.id.et_mob);
        //et_address1 = (EditText) view.findViewById(R.id.et_address1);
        //et_address2= (EditText) view.findViewById(R.id.et_address2);

        img_back.setOnClickListener(this);
        img_validate_phone.setOnClickListener(this);
        tv_continue.setOnClickListener(this);
        tv_submit.setOnClickListener(this);

        formData = new FormData();
        formData.addField(et_fname);
        formData.addField(et_lname);
        formData.addField(et_phone,"M");
        formData.addField(et_email,"E");
        formData.addField(et_password);
        return view;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.context = activity;
    }


    private void hideInputMethod() {
        View view = ((IntroductionScreen)context).getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.image_back:
                ((MainScreenFragment)getParentFragment()).onBackPress();
                hideInputMethod();
                break;

            case R.id.img_validate_phone:

                phone = et_phone.getText().toString();

                if (Patterns.PHONE.matcher(phone).matches()){
                    Intent intent = new Intent(context, DigitsMainActivityNew.class);
                    intent.putExtra("phone", phone);
                    startActivityForResult(intent, REQUEST_CODE);

                }else
                    et_phone.setError("Please enter valid Mobile Number");

                break;

            case R.id.tv_continue:

                if (formData.isValid()){

                    if (MOBILE_VERIFICATION_DONE){
                        layout_continue.setVisibility(View.GONE);
                        layout_signup.setVisibility(View.VISIBLE);
                        ((MainScreenFragment)getParentFragment()).currentBackListener = this;
                    }else
                        et_phone.setError("Mobile no. validation pending");
                }
                break;

            case R.id.tv_submit:

                try {

                    if (formData.isValid()) {

                        if (rgAgeConfirmation.getCheckedRadioButtonId() == R.id.rb_yes) {

                            guest_name = et_fname.getText().toString() + " " + et_lname.getText().toString();
                            phone = et_phone.getText().toString();
                            email = et_email.getText().toString();

                            String parameter = UtilToCreateJSON.createGuestParamter(
                                    guest_name, phone, email, "",
                                    "", "M", "", "", et_password.getText().toString(),
                                    "", "", false);

                            String serverIP = POSApplication.getSingleton()
                                    .getmDataModel().getUserInfo().getServerIP();

                            GuestProfileTask guestProfileTask = new GuestProfileTask(context, parameter, serverIP,progress_signup,this);
                            guestProfileTask.execute();

                        }else
                            Toast.makeText(context,"You are below 21 years. Please visit our location.",Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
        }
    }

    @Override
    public void responseGuestSave(String response) {
        ((MainScreenFragment)getParentFragment()).responseGuestSave(response);
        PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.GUEST_ID, email);
        PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.GUEST_NAME, guest_name);
        PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.GUEST_PHONE, phone);
    }

    @Override
    public void responseGuestSearch(String response) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == SUCCESS){
            img_validate_phone.setImageResource(R.drawable.remember_icon);
            et_phone.setError(null);
            MOBILE_VERIFICATION_DONE = true;
        }
    }

    @Override
    public boolean onBackPress() {

        if (layout_signup.getVisibility() == View.VISIBLE) {
            layout_signup.setVisibility(View.GONE);
            layout_continue.setVisibility(View.VISIBLE);
            hideInputMethod();
            return true;
        }

        return false;
    }
}
