package com.entrada.cheekyMonkey.fabric.crashlytics;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import io.fabric.sdk.android.Fabric;
import com.crashlytics.android.Crashlytics;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.staticData.PrefHelper;

public class AppCrashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        // TODO: Move this to where you establish a user session
        logUser();

        setContentView(R.layout.app_crash_layout);
    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }

    private void logUser() {

        String phone = PrefHelper.getStoredString(this,PrefHelper.PREF_FILE_NAME, PrefHelper.GUEST_PHONE);
        String email = PrefHelper.getStoredString(this,PrefHelper.PREF_FILE_NAME, PrefHelper.LOGIN_EMAIL_ID);
        String name = PrefHelper.getStoredString(this,PrefHelper.PREF_FILE_NAME, PrefHelper.GUEST_NAME);

        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier(phone);   // "12345"
        Crashlytics.setUserEmail(email);        // "user@fabric.io"
        Crashlytics.setUserName(name);          // "Test User"
    }

}