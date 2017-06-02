package com.entrada.cheekyMonkey.dynamic.start;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.OnBackPressInterface;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.dynamic.google.gmail.SendSmtpMail;
import com.entrada.cheekyMonkey.dynamic.syncData.SyncFragment;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.staticData.PrefHelper;
import com.entrada.cheekyMonkey.steward.notificationUI.AdminActivity;
import com.entrada.cheekyMonkey.steward.notificationUI.KitchenActivity;
import com.entrada.cheekyMonkey.task.GuestProfileTask;
import com.entrada.cheekyMonkey.task.ICallBackGuestProfileResponse;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.Logger;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Rahul on 14/05/2016.
 */
public class MainScreenFragment extends Fragment implements View.OnClickListener,
        OnBackPressInterface, ICallBackGuestProfileResponse,
        GoogleApiClient.OnConnectionFailedListener{

    Context context;
    FrameLayout frameLayout;
    LinearLayout layout_main;
    Button customFB;
    LoginButton loginButton;
    CallbackManager callbackManager;
    String guest_name = "", guest_id = "",  email = "", birthday = "", gender = "M";
    ProgressBar progress_ggl, progress_fb;



    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    OnBackPressInterface currentBackListener;
    EditText editTextEmail, editTextPassword;
    boolean forgotPassword = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(context).inflate(R.layout.main_screen,container,false);

        //Register both button and add click listener
        Button signInButton = (Button) view.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        //signInButton.setColorScheme(Button.COLOR_AUTO);


        progress_ggl = (ProgressBar) view.findViewById(R.id.progress_ggl);
        progress_fb = (ProgressBar) view.findViewById(R.id.progress_fb);

        frameLayout = (FrameLayout) view.findViewById(R.id.container);
        layout_main = (LinearLayout) view.findViewById(R.id.layout_main);


        customSignIn(view);

        /*LinearLayout layoutSignIn = (LinearLayout)view.findViewById(R.id.layout_signin);
        LinearLayout layoutSignUp = (LinearLayout)view.findViewById(R.id.layout_signup);
        layoutSignIn.setOnClickListener(this);
        layoutSignUp.setOnClickListener(this);*/
        onFBLogin(view);
        //onPostCreate();

        return view;
    }


    /* ************************************ Added Newly ******************************************/

    ProgressBar progress_signin;
    FormData form;
    ArrayList<String> arrayListEmail ;

    public void customSignIn(View view){

        boolean check = PrefHelper.getStoredBoolean(context,PrefHelper.PREF_FILE_NAME, PrefHelper.REMEMBER_ME,true);
        String email = PrefHelper.getStoredString(context,PrefHelper.PREF_FILE_NAME, PrefHelper.LOGIN_EMAIL_ID);
        String pass = PrefHelper.getStoredString(context,PrefHelper.PREF_FILE_NAME, PrefHelper.LOGIN_PASSWORD);

        editTextEmail = (EditText)view.findViewById(R.id.et_email_no);
        editTextPassword = (EditText)view.findViewById(R.id.editTextPass);
        progress_signin = (ProgressBar) view.findViewById(R.id.progress_signin);
        ImageView imgSignIn = (ImageView) view.findViewById(R.id.img_signin);
        TextView imgForgotPass = (TextView) view.findViewById(R.id.tv_forgot_pass);
        Button btnSignIn = (Button) view.findViewById(R.id.buttton_signin);
        Button btnSignUp = (Button)view.findViewById(R.id.button_signup);

        editTextEmail.setText(check ?  email : "" );
        editTextPassword.setText(check ?  pass : "" );
        imgForgotPass.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        imgSignIn.setOnClickListener(this);


        final CheckedTextView ctv_remeber = (CheckedTextView) view.findViewById(R.id.ctv_remember);
        ctv_remeber.setChecked(check);

        ctv_remeber.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ctv_remeber.toggle();

                PrefHelper.storeBoolean(context,PrefHelper.PREF_FILE_NAME, PrefHelper.REMEMBER_ME,
                        ctv_remeber.isChecked());
            }
        });

        arrayListEmail = new ArrayList<>();
        arrayListEmail.add("rk@gmail.com");
        arrayListEmail.add("js@gmail.com");
        arrayListEmail.add("ns@gmail.com");
        arrayListEmail.add("js@entradasoft.com");
        arrayListEmail.add("kit@gmail.com");
        arrayListEmail.add("st@gmail.com");

        form = new FormData();
        form.addField(editTextEmail);
        form.addField(editTextPassword);
    }

    public void getForgotPassword() {

        try {

            if (isValidNumberOrEmail()) {

                forgotPassword = true;
                String parameter = UtilToCreateJSON.createGstSigninParameter(
                        editTextEmail.getText().toString(), "");
                String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();

                GuestSigninTask guestSigninTask = new GuestSigninTask(context, parameter, serverIP,progress_signin, this);
                guestSigninTask.execute();
                PrefHelper.storeString(context,PrefHelper.PREF_FILE_NAME, PrefHelper.LOGIN_EMAIL_ID,
                        editTextEmail.getText().toString());
                PrefHelper.storeString(context,PrefHelper.PREF_FILE_NAME, PrefHelper.LOGIN_PASSWORD,
                        editTextPassword.getText().toString());
            }else
                editTextEmail.setError("Please enter vail email");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isValidNumberOrEmail(){

        String email = editTextEmail.getText().toString();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ||
                PhoneNumberUtils.isGlobalPhoneNumber(email);
    }

    public void GuestSearch() {

        try {

            if (form.isValid()) {

                String parameter = UtilToCreateJSON.createGstSigninParameter(
                        editTextEmail.getText().toString(), editTextPassword.getText().toString());
                String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();

                GuestSigninTask guestSigninTask = new GuestSigninTask(context, parameter, serverIP,progress_signin, this);
                guestSigninTask.execute();
                PrefHelper.storeString(context,PrefHelper.PREF_FILE_NAME, PrefHelper.LOGIN_EMAIL_ID,
                        editTextEmail.getText().toString());
                PrefHelper.storeString(context,PrefHelper.PREF_FILE_NAME, PrefHelper.LOGIN_PASSWORD,
                        editTextPassword.getText().toString());
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
                PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.GUEST_ID, editTextEmail.getText().toString());
                PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.GUEST_NAME, guest_name);
                setUserType(editTextEmail.getText().toString(),jsonArray);

            }else if (error_msg.contains("invalid_email"))
                showFailureDialog("Please enter valid Email ID/ Mobile #");
            else if (error_msg.equals("invalid_pass"))
                showFailureDialog("Please enter valid Password");
            else if (error_msg.contains("invalid_pass#")){
                if (forgotPassword){
                    String[] data = error_msg.split("#");
                    sendOrderViaMail(data[1], data[2]);
                    showFailureDialog("We have sent you an email. Please check your inbox.");
                    forgotPassword = false;
                }else
                    showFailureDialog("Please enter valid Password");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Connection Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendOrderViaMail(String GuestName, String password){

        String email = editTextEmail.getText().toString();
        String subject = "Cheeky Monkey";
        String message = "Hi "+GuestName+", "
                + "\nLooks like you forget your password. Nothing to worry."
                + "\nEmail : " + email
                + "\nPassword : " + password
                + "\nTeam: Cheeky Monkey ";

        SendSmtpMail.sendMessage(email, subject, message);

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


    /* *************************** Google & FB API initialization Section ***************************************/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();

        try {

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

/*            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestScopes(new Scope(Scopes.EMAIL))
                    .requestIdToken(getString(R.string.server_client_id))
                    .requestEmail()
                    .build();*/

           /* GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(this.getResources().getString(R.string.server_client_id))
                    .requestEmail().build();*/

            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onPostCreate(){

        float fbIconScale = 1.45F;
        Drawable drawable = getResources().getDrawable(
                com.facebook.R.drawable.com_facebook_button_icon);

        if (drawable != null){
            drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*fbIconScale),
                    (int)(drawable.getIntrinsicHeight()*fbIconScale));
            loginButton.setCompoundDrawables(drawable, null, null, null);
            loginButton.setCompoundDrawablePadding(getResources().
                    getDimensionPixelSize(R.dimen.fb_margin_override_textpadding));
/*            loginButton.setPadding(
                    getResources().getDimensionPixelSize(
                            R.dimen.fb_margin_override_lr),
                    getResources().getDimensionPixelSize(
                            R.dimen.fb_margin_override_top),
                    0,
                    getResources().getDimensionPixelSize(
                            R.dimen.fb_margin_override_bottom));*/
        }
    }

    // login using LoginManager
    private void onButtonFBLogin() {

        // Set permissions          // parameter : "publish_actions" can be used with publishPermission() only.
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends",
                "user_photos", "user_hometown", "user_location"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {

                                        if (response.getError() != null) {
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            signUpUsingFB(json);
                                        }
                                    }

                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday,hometown,location");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Log.d("CANCEL", "on cancel");
                        showCancelDialog(getResources().getString(R.string.fb_cancel_string));
                    }


                    @Override
                    public void onError(FacebookException error) {
                        Log.d("ERROR", error.toString());
                    }
                });
    }

    // login using FBLoginButton
    public void onFBLogin(View view){

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setText("");
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends",
                "user_photos", "user_hometown", "user_location"));

        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                signUpUsingFB(object);

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,hometown,location");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Log.v("LoginActivity", "cancel");
                showCancelDialog(getResources().getString(R.string.fb_cancel_string));
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v("LoginActivity", exception.getMessage());
                showCancelDialog(exception.getMessage());
            }
        });

       /* customFB = (Button) view.findViewById(R.id.fb);
        customFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginButton.performClick();
            }
        });*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }else
                callbackManager.onActivityResult(requestCode, resultCode, data);

        }catch (Exception e){
            e.printStackTrace();
            Logger.w("Exception", e.getClass().toString());
        }
    }

    public void signUpUsingFB(JSONObject jsonObject){

        Log.v("FBLoginResponse", jsonObject.toString());

        try {
            guest_name = jsonObject.getString("name");
            guest_id = jsonObject.getString("id") == null ? "": jsonObject.getString("id").substring(0, 12);
            //guest_id = jsonObject.getString("email");
            email = jsonObject.getString("email");
            gender = jsonObject.getString("gender") == null || jsonObject.getString("gender").equalsIgnoreCase("male") ? "M":"F";
            //birthday = jsonObject.getString("birthday");

        }catch (Exception e){
            e.printStackTrace();
        }

        try {

            String password = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0,7);
            String parameter = UtilToCreateJSON.createGuestParamter(
                    guest_name, guest_id , email, birthday,"", gender, "","", password, "", "", true);

            GuestProfileTask guestProfileTask = new GuestProfileTask(context, parameter, UserInfo.ServerIP,progress_fb, this);
            guestProfileTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCancelDialog(String message){

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
    public void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();
        //disconnectFromFacebook();
    }


    // To completely decouple Facebook app from FB user account
    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {

                        LoginManager.getInstance().logOut();

                    }
                }).executeAsync();
    }


    /* ***************************** Google Plus Login Section *******************************/

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {

        if (mGoogleApiClient.isConnected()){
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            //Toast.makeText(context,"logged out", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        progress_ggl.setVisibility(View.GONE);

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            signUpUsingGooglePlus(acct);

        } else
            //showCancelDialog(result.getStatus().toString());
            showCancelDialog(getResources().getString(R.string.ggl_cancel_string));
    }

    private void signUpUsingGooglePlus( GoogleSignInAccount acct){

        String name = acct.getDisplayName();
        String given = acct.getGivenName();
        String family = acct.getFamilyName();
        email = acct.getEmail();
        String photoUrl = acct.getPhotoUrl() == null ? "": acct.getPhotoUrl().toString();
        String id = acct.getId() == null ? "": acct.getId().substring(0,12);
        String token = acct.getIdToken();
        String authCode = acct.getServerAuthCode();

        try {
            guest_id = id;
            guest_name = name;
            String password = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0,7);
            String parameter = UtilToCreateJSON.createGuestParamter(name, id, email, "", "", "M", "", "", password, "", "", true);

            GuestProfileTask guestProfileTask = new GuestProfileTask(context, parameter, UserInfo.ServerIP,progress_ggl, this);
            guestProfileTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ***************************************************************************************/

    @Override
    public void responseGuestSave(String response) {

        Logger.i("SaveResponse", response);
        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONObject jsonObject1 = jsonObject.getJSONObject("ECABS_GuestSignUpResult");
            JSONArray jsonArray = jsonObject1.getJSONArray("ListSaveGuest");
            JSONArray jsonArray2 = jsonObject1.getJSONArray("ListGuestOrders");

            if (jsonArray.length() > 0){

                JSONObject jsonObject2= (JSONObject) jsonArray.get(0);
                String errorMsg = jsonObject2.getString("Code");

                if (errorMsg.contains("This email Id is already in use"))
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();

                else {
                    PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.SERVER_IP, UserInfo.ServerIP);
                    PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.GUEST_ID, email);
                    PrefHelper.storeString(context,PrefHelper.PREF_FILE_NAME,PrefHelper.GUEST_NAME, guest_name);

                    setUserType(email, jsonArray2);
                    signOut();
                }

            }
            else
                Toast.makeText(context, "Please try again", Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
        }
    }

    protected void saveGuestOrders(JSONArray jsonArray) {

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();

        try {

            mdb.beginTransaction();
            mdb.delete(DBConstants.KEY_GUEST_ORDERS_TABLE, null,null);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContentValues cv = new ContentValues();
                String itemName = jsonObject.getString("Item");
                itemName = itemName.isEmpty() ? UserInfo.getMixerName(jsonObject.getString("Code")) : itemName;

                cv.put(DBConstants.KEY_ORDER_NUMBER, jsonObject.getString("OrderNo"));
                cv.put(DBConstants.KEY_TABLE_NUMBER,  jsonObject.getString("Table"));
                cv.put(DBConstants.KEY_ITEM_CODE, jsonObject.getString("Code"));
                cv.put(DBConstants.KEY_ITEM_NAME,  itemName);
                cv.put(DBConstants.KEY_ITEM_PRICE, jsonObject.getString("Rate"));
                cv.put(DBConstants.KEY_ITEM_QTY,  jsonObject.getString("Qty"));
                cv.put(DBConstants.KEY_ORDER_TAX, jsonObject.getString("Tax"));
                cv.put(DBConstants.KEY_ORDER_AMOUNT,  jsonObject.getString("Amount"));
                cv.put(DBConstants.KEY_ORDER_STATUS, jsonObject.getString("Status"));
                cv.put(DBConstants.KEY_ORDER_DATE,  jsonObject.getString("Date"));
                cv.put(DBConstants.KEY_ORDER_TIME, jsonObject.getString("Time"));
                mdb.insert(DBConstants.KEY_GUEST_ORDERS_TABLE, null, cv);
            }

            mdb.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            mdb.endTransaction();
        }

    }


    public void setUserType(String email, JSONArray jsonArray){

        if (email.contains("admin@gmail.com")){ // Admin will also see accepted & rejected orders by tab at bottom.

           /* FragmentManager manager = getChildFragmentManager();
            NotificationFragment fragment = NotificationFragment.newInstance(
                    NotificationFragment.EMPLOYEE_TYPE1, NotificationFragment.TYPE_UNDER_PROCESS);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, fragment, StaticConstants.NOTIFICATION_TAG);
            transaction.commit();

            layout_main.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);*/

            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.ADMIN_LOGIN, PrefHelper.ADMIN_LOGIN_DONE);
            Intent intent = new Intent(context, AdminActivity.class);
            startActivity(intent);
            getActivity().finish();

        } else if (email.equals("kit@gmail.com")) { // Used KitchenBarFragment here bcz a new UI is required for this option.
           /* FragmentManager manager = getChildFragmentManager();
            KitchenBarFragment fragment = KitchenBarFragment.newInstance(NotificationFragment.TYPE_ACCEPTED);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, fragment, StaticConstants.NOTIFICATION_TAG);
            transaction.commit();
            layout_main.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);*/

            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.KITCHEN_LOGIN, PrefHelper.KITCHEN_LOGIN_DONE);
            Intent intent = new Intent(context, KitchenActivity.class);
            startActivity(intent);
            getActivity().finish();

        } else{

            if (arrayListEmail.contains(email))         // if Steward, else Guest
                PrefHelper.storeBoolean(context,PrefHelper.PREF_FILE_NAME,PrefHelper.STEWARD_LOGIN, true);
            else {
                PrefHelper.storeBoolean(context, PrefHelper.PREF_FILE_NAME, PrefHelper.STEWARD_LOGIN, false);
                saveGuestOrders(jsonArray);
            }

            requestLoginFromServer();
        }
    }

    public void requestLoginFromServer(){

        if (!getActivity().isFinishing()){
            layout_main.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            frameLayout.removeAllViews();
            FragmentManager fmOther1 = getChildFragmentManager();
            FragmentTransaction transaction = fmOther1.beginTransaction();
            SyncFragment syncFragment = new SyncFragment();
            transaction.replace(R.id.container, syncFragment).commitAllowingStateLoss();
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.sign_in_button:
                progress_ggl.setVisibility(View.VISIBLE);
                signIn();
                break;


/*
            case R.id.img_ggl_login:

                // Login using google signin

                Intent intent2 = new Intent(context, GoogleSigninActivity.class);
                getActivity().finish();
                startActivity(intent2);
                break;*//*


               // To send mail without user intervention using GMAIL API.

                Intent intent2 = new Intent(context, GmailActivity.class);
                getActivity().finish();
                startActivity(intent2);*//*


                // To send mail without user intervention using GMAIL SMTP.
                //SendSmtpMail.sendMessage("Hello Rahul,...");
                //SendSmtpMail.sendMail();
                break;
*/


            case R.id.img_signin:
                /*layout_main.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                FragmentManager fmOther = getChildFragmentManager();
                FragmentTransaction transaction = fmOther.beginTransaction();
                transaction.replace(R.id.container, new SignInFragment()).commit();*/
                GuestSearch();
                break;

            case R.id.buttton_signin:
                GuestSearch();
                break;

            case R.id.button_signup:
                layout_main.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                frameLayout.removeAllViews();
                FragmentManager fmOther1 = getChildFragmentManager();
                FragmentTransaction transaction1 = fmOther1.beginTransaction();
                SignUpFragment signUpFragment = new SignUpFragment();
                transaction1.replace(R.id.container, signUpFragment).commit();
                break;

            case R.id.tv_forgot_pass:
                getForgotPassword();
                break;

          /*  case R.id.img_never_mind:
                requestLoginFromServer();
                break;*/
        }
    }

    @Override
    public boolean onBackPress() {

        if (currentBackListener != null && currentBackListener.onBackPress()){
            currentBackListener = null;
            return true;
        }

        else if (frameLayout.getVisibility() == View.VISIBLE){
            frameLayout.setVisibility(View.GONE);
            layout_main.setVisibility(View.VISIBLE);
            return true;
        }

        return false;
    }

}


/*
  Please note: Every time you build a project on different PC, every time debug.keystore gets changed
  that means key hash for this project always get changed when you run on different PC. So, you'll
  have to manually update this new key hash to your facebook developer profile in order to login
  facebook properly. Here, are Steps How to get this new Key Hash :
  Run this project in debug mode and put debugging break point at LoginManager's
  finishLogin(newToken, originalRequest, exception, isCanceled, callback) method
  to check what this method's exception parameter returns actually. If exception is null, that means
  facebook login is done successfully otherwise, it will have the new key hash that you need to
  register on your facebook developer console.
*/