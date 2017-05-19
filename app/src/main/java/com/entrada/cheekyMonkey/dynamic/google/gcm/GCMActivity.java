package com.entrada.cheekyMonkey.dynamic.google.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import com.entrada.cheekyMonkey.R;

public class GCMActivity extends AppCompatActivity {

    private final Context mContext = this;
    //private final String SENDER_ID = "680524347527"; // Project Number at https://console.developers.google.com/project/...
    private final String SENDER_ID = "990611609510"; // Project Number at https://console.developers.google.com/project/...
    private final String SHARD_PREF = "com.example.gcmclient_preferences";
    private final String GCM_TOKEN = "gcmtoken";    
    public static TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gcm_layout);

        SharedPreferences appPrefs = mContext.getSharedPreferences(SHARD_PREF, Context.MODE_PRIVATE);
        String token = appPrefs.getString(GCM_TOKEN, "");
        if (token.isEmpty()) {
            try {
                getGCMToken();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setText(token);
    }



    private void getGCMToken() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InstanceID instanceID = InstanceID.getInstance(mContext);
                    String token = instanceID.getToken(SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    if (token != null && !token.isEmpty()) {
                        SharedPreferences appPrefs = mContext.getSharedPreferences(SHARD_PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = appPrefs.edit();
                        prefsEditor.putString(GCM_TOKEN, token);
                        prefsEditor.apply();
                    }
                    Log.i("GCM", token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}