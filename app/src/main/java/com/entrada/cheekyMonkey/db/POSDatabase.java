package com.entrada.cheekyMonkey.db;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.entrada.cheekyMonkey.util.Logger;

public class POSDatabase extends SQLiteOpenHelper {

    private static POSDatabase posDatabase;
    private static String DB_NAME = "CSAT_POS_DATABASE";
    private SQLiteDatabase mdb;

    public POSDatabase(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    public static final POSDatabase getInstanceLogin(Context context) {
        if (posDatabase == null) {
            try {
                PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), 0);
                posDatabase = new POSDatabase(context, pInfo.versionCode);
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            posDatabase.getWritableDatabase();
        }
        return posDatabase;
    }

    public static final POSDatabase getDataSYNC(Context context) {
        if (posDatabase == null) {
            try {
                PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), 0);
                posDatabase = new POSDatabase(context, pInfo.versionCode);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            posDatabase.getWritableDatabase();
        }
        return posDatabase;
    }

    public static final POSDatabase getPosSync(Context context) {
        if (posDatabase == null) {
            try {
                PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), 0);
                posDatabase = new POSDatabase(context, pInfo.versionCode);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            posDatabase.getWritableDatabase();
        }
        return posDatabase;
    }


    public static final POSDatabase getMenuItem(Context context) {
        if (posDatabase == null) {
            try {
                PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), 0);
                posDatabase = new POSDatabase(context, pInfo.versionCode);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            posDatabase.getWritableDatabase();
        }
        return posDatabase;
    }

    public static final POSDatabase getInstanceFloor(Context context) {
        if (posDatabase == null) {
            try {
                PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), 0);
                posDatabase = new POSDatabase(context, pInfo.versionCode);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            posDatabase.getWritableDatabase();
        }
        return posDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Logger.i("Tag", "oncreate tables");

        // create table
        String[] createStatements = getCreateTableStatement();
        int total = createStatements.length;
        for (int i = 0; i < total; i++) {
            Logger.i("Tag", "executing create query " + createStatements[i]);
            db.execSQL(createStatements[i]);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {

        return mdb != null ? mdb : (mdb = super.getWritableDatabase());
    }

    private String[] getCreateTableStatement() {

        String[] createDBStatements = new String[48];
        createDBStatements[0] = DBStatements.CREATE_USR_TABLE;
        createDBStatements[1] = DBStatements.CREATE_OUTLET_POS_TABLE;
        createDBStatements[2] = DBStatements.CREATE_OUTLET_TABLE;
        createDBStatements[3] = DBStatements.CREATE_GROUP_TABLE;
        createDBStatements[4] = DBStatements.CREATE_CATEGORY_TABLE;
        createDBStatements[5] = DBStatements.CREATE_MENU_ITEM_TABLE;
        createDBStatements[6] = DBStatements.CREATE_ADDON_ITEM_TABLE;
        createDBStatements[7] = DBStatements.CREATE_MODIFIER_TABLE;
        createDBStatements[8] = DBStatements.CREATE_COMBO_TABLE;
        createDBStatements[9] = DBStatements.CREATE_ITEM_REMARK_TABLE;
        createDBStatements[10] = DBStatements.CREATE_ORDER_REMARK_TABLE;
        createDBStatements[11] = DBStatements.CREATE_KITCHEN_MASTER_TABLE;
        createDBStatements[12] = DBStatements.CREATE_KITCHEN_REMARK_TABLE;
        createDBStatements[13] = DBStatements.CREATE_GUEST_TYPE_TABLE;
        createDBStatements[14] = DBStatements.CREATE_STEWARD_TYPE_TABLE;
        createDBStatements[15] = DBStatements.CREATE_DISC_MST_TABLE;
        createDBStatements[16] = DBStatements.CREATE_DISC_DOW_TABLE;
        createDBStatements[17] = DBStatements.CREATE_DISC_ITEM_TABLE;
        createDBStatements[18] = DBStatements.CREATE_DISC_SUB_ITEM_TABLE;
        createDBStatements[19] = DBStatements.CREATE_MEAL_TABLE;
        createDBStatements[20] = DBStatements.CREATE_ORDER_TYPE_TABLE;
        createDBStatements[21] = DBStatements.CREATE_GUEST_DISCOUNT_TABLE;
        createDBStatements[22] = DBStatements.CREATE_KEY_HSSURCHR_DOW;
        createDBStatements[23] = DBStatements.CREATE_KEY_HSSURCHR_MST;
        createDBStatements[24] = DBStatements.CREATE_KEY_HSSURCHG_ITEM;
        createDBStatements[25] = DBStatements.CREATE_KEY_GUEST_TITLE;
        createDBStatements[26] = DBStatements.CREATE_KEY_STEWARD_DETAIL;
        createDBStatements[27] = DBStatements.CREATE_KEY_ABT_ITEM_IMAGE;
        createDBStatements[28] = DBStatements.CREATE_KEY_GUEST_PERMISION;
        createDBStatements[29] = DBStatements.CREATE_KEY_MANDATORY;
        createDBStatements[30] = DBStatements.CREATE_KEY_PERMISSION;
        createDBStatements[31] = DBStatements.CREATE_KEY_COMMENT_CARD;
        createDBStatements[32] = DBStatements.CREATE_GUEST_SEARCH_TABLE;
        createDBStatements[33] = DBStatements.CREATE_GUEST_FEEDBACK_TABLE;
        createDBStatements[34] = DBStatements.CREATE_MENU_FTS;
        createDBStatements[35] = DBStatements.CREATE_GUEST_SEARCH_FTS;
        createDBStatements[36] = DBStatements.CREATE_RMS_COMPANY_TABLE;
        createDBStatements[37] = DBStatements.CREATE_FLOOR_IMAGE_TABLE;
        createDBStatements[38] = DBStatements.CREATE_FLR_TBL_TABLE;
        createDBStatements[39] = DBStatements.CREATE_FLR_FLOOR_TABLE;
        createDBStatements[40] = DBStatements.KEY_CHECK_SETTING_TABLE;
        createDBStatements[41] = DBStatements.CREATE_COMPLIMENTARY_TABLE;
        createDBStatements[42] = DBStatements.CREATE_GUEST_ORDER_TABLE;
        createDBStatements[43] = DBStatements.CREATE_GUEST_ORDERS_TABLE;
        createDBStatements[44] = DBStatements.CREATE_TABLE_IMAGE;
        createDBStatements[45] = DBStatements.CREATE_ITEMS_TABLE;
        createDBStatements[46] = DBStatements.CREATE_CURRENCY_TABLE;
        createDBStatements[47] = DBStatements.CREATE_RMS_OUTLET_TABLE;

        return createDBStatements;

    }

}
