package com.entrada.cheekyMonkey.fabric.digits;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.digits.sdk.android.SessionListener;
import com.entrada.cheekyMonkey.R;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;


public class DigitsMainActivityNew extends Activity {

  // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
  private static final String TWITTER_KEY = "7QkBwUZHqUtYsRqIgHfLlJXBY";
  private static final String TWITTER_SECRET = "qmLTE0S0CzmY2jiDmHBIFuQkdLP5hFxDIUQCVQA3bhjdxpFIsE";
  AuthCallback authCallback;
  SessionListener sessionListener;

  int SUCCESS = 50;
  int FAILURE = 51;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);

    Digits.Builder digitsBuilder = new Digits.Builder().withTheme(R.style.CustomDigitsTheme);
    Fabric.with(this, new TwitterCore(authConfig), digitsBuilder.build());
    setContentView(R.layout.digits_button_layout);


    authCallback = new AuthCallback() {
      @Override
      public void success(DigitsSession session, String phoneNumber) {
        // TODO: associate the session userID with your user model
        Toast.makeText(getApplicationContext(), "Authentication successful for "
                + phoneNumber, Toast.LENGTH_LONG).show();
        Intent intent = getIntent();
        intent.putExtra("success", true);
        setResult(SUCCESS, intent);
        finish();
      }

      @Override
      public void failure(DigitsException exception) {
        Log.d("Digits", "Sign in with Digits failure", exception);

        Intent intent = getIntent();
        intent.putExtra("success", false);
        setResult(FAILURE, intent);
        finish();
      }
    };

    String defaultNumber = "+91" + getIntent().getExtras().getString("phone");
    DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
    digitsButton.setCallback(authCallback);
    setDefaultPhoneNumber(defaultNumber);
    //registerSessionListener();
  }



  public AuthCallback getCallBack(){

    return authCallback;
  }

  public void setDefaultPhoneNumber(String defaultPhoneNumber){

    AuthConfig.Builder authConfigBuilder = new AuthConfig.Builder()
            .withAuthCallBack(getCallBack())
            .withPhoneNumber(defaultPhoneNumber)
            //.withEmailCollection();
            .withEmailCollection(false);

    Digits.authenticate(authConfigBuilder.build());
  }

  public void registerSessionListener(){

    sessionListener = new SessionListener() {
      @Override
      public void changed(DigitsSession newSession) {
        Toast.makeText(DigitsMainActivityNew.this, "Session phone was changed: " + newSession
                .getPhoneNumber(), Toast.LENGTH_SHORT).show();
      }
    };
    Digits.addSessionListener(sessionListener);
  }

/*  @Override
  protected void onStop() {
    Digits.removeSessionListener(sessionListener);
    super.onStop();
  }*/
}