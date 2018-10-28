package com.example.xandi.amicer;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefManager {

    private static final String SHARED_PREF_NAME = "fcmsharedpref";
    private static final String KEY_ACCESS_TOKEN = "token";
    private static Context mCtx;
    private static SharePrefManager mInstance;

    private SharePrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharePrefManager getmInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharePrefManager(context);
        }
        return mInstance;
    }

    public boolean storeToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, token);
        return true;
    }

    public String getToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }
}
