package com.entrada.cheekyMonkey.staticData;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefHelper {

    public static final String LANGUAGE_UNITS_KEY = "language";
    public static final String FIRST_TIME_LUNCHAPP = "firsttimelunchapp";
    public static final String FIRST_TIME_LUNCHAPP_DONE = "Done";
    public static final String LOGGED_OUT = "loggedOut";
    public static final String FIRST_TIME_REGISTER = "firsttimeregister";
    public static final String Language = "languages";
    public static final String APP_STATUS = "App_Status";
    public static final String FIRST_TIME_SETUP_COMPLE_State_ = "firsttimesetupcomplestate";
    public static final String FIRST_TIME_SETUP_COMPLE_LANGUAGE = "firsttimesetupcomplelanguage";
    public static final String IPAddress = "posipAddress";
    public static final String GCM_REG_KEY = "gmcRegKey";
    /*public static final String IPPort = "posPort";*/
    public static final String TIME_TICK = "TimeTick";
    public static final String TOKEN = "Token";
    public static final String POINT_OF_SALE = "POS";
    public static final String POINT_OF_SALE_TYPE = "POS_Type";
    public static final String SERVER_IP = "ServerIp";
    public static final String BACKGROUND_COLOR = "Background_Color";
    public static final String FONT_COLOR = "Font_Color";
    public static final String Navigation_Color = "Navi_color";
    public static final String Navigation_Content_Color = "Navi_content_color";
    public static final String Navigation_Font_Color = "Navi_font_color";
    public static final String ACTIVATE_FLOOR_TABLE = "floor_table";
    public static final String FLOOR_INDEX = "index";
    public static final String STEWARD_IP = "StewardIP";
    public static final String DATA_SYNC = "isDataSync";
    public static final String TEXT_SIZE = "textSize";
    public static final String USER_ID = "user_id";
    public static final String MAX_ORDER = "max_order";
    public static String PREF_FILE_NAME = "CSAT_POS_PREF_FILE";
    public static String PREF_FILE_NAME_ONLY = "CSAT_POS_PREF_FILE_ONLY";
    public static String USERNAMEPOS = "UserNamePOS";
    public static String USERPASSWORDPOS = "PasswordPOS";
    public static String TEMP_USER_ID = "PasswordPOS";
    public static final String POS_INDEX = "PosIndex";
    public static final String LIST_INDEX = "ListIndex";
    public static final String GUEST_ID = "GUEST_CODE";
    public static final String GUEST_NAME = "GUEST_NAME";
    public static final String GUEST_PHONE = "GUEST_PHONE";
    public static final String GUEST_PICTURE = "GUEST_PICTURE";
    public static final String MIN_LATITUDE = "min_lati";
    public static final String MAX_LATITUDE = "max_lati";
    public static final String MIN_LONGITUDE = "min_longi";
    public static final String MAX_LONGITUDE = "max_longi";
    public static final String DRAWER_LOCK_MODE = "drawer_lock";
    public static final String DRAWER_LOCK_INDEX = "drawer_lock_index";
    public static final String EVENT_MESSAGE = "message";
    public static final String OUTLET_ID = "OutletID";

    public static final String TEMP_URL = "url";
    public  static final String GCM_TOKEN = "gcmtoken";
    public static final String REMEMBER_ME = "remember";
    public static final String LOGIN_EMAIL_ID = "emailId";
    public static final String LOGIN_PASSWORD = "password";
    public  static final String STEWARD_LOGIN = "stewardLogin";
    public  static final String ADMIN_LOGIN = "adminLogin";
    public  static final String ADMIN_LOGIN_DONE = "adminLoginDone";
    public static final String KITCHEN_LOGIN = "kitchenLogin";
    public static final String KITCHEN_LOGIN_DONE = "kitchenLoginDone";


    static public String getStoredString(Context aContext, String FileName, String aKey) {
        SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName, 0);
        return sharedPrefs.getString(aKey, "");
    }

    static public float getStoredFloat(Context aContext, String FileName, String aKey) {
        SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName, 0);
        return sharedPrefs.getFloat(aKey, 0.0f);
    }

    static public boolean getStoredBoolean(Context aContext, String FileName, String aKey) {
        SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName, 0);
        return sharedPrefs.getBoolean(aKey, false);
    }

    static public boolean getStoredBoolean(Context aContext, String FileName, String aKey, boolean value) {
        SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName, 0);
        return sharedPrefs.getBoolean(aKey, true);
    }

    static public int getStoredInt(Context aContext, String FileName, String aKey) {
        SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName, 0);
        return sharedPrefs.getInt(aKey, 0);
    }

    static public void storeString(Context aContext, String FileName, String aKey, String aValue) {
        SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName, 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(aKey, aValue);
        editor.apply();
    }

    static public void storeInt(Context aContext, String FileName, String aKey, int aValue) {
        SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName, 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(aKey, aValue);
        editor.apply();
    }


    static public void storeFloat(Context aContext, String FileName, String aKey, float aValue) {
        SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName, 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putFloat(aKey, aValue);
        editor.apply();
    }


    static public void storeDouble(Context aContext, String FileName, String aKey, double value) {
        SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName, 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor .putLong(aKey, Double.doubleToRawLongBits(value));
        editor.apply();

    }

    static public double getStoredDouble(Context aContext, String FileName, String aKey) {
        SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName, 0);
        return Double.longBitsToDouble(sharedPrefs.getLong(aKey, 0));
    }

    static public void storeBoolean(Context aContext, String FileName, String aKey, boolean aValue) {
        SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName, 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(aKey, aValue);
        editor.apply();
    }
}