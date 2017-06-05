package com.entrada.cheekyMonkey.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.dynamic.BaseFragmentActivity;
import com.entrada.cheekyMonkey.dynamic.TakeOrderFragment;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.steward.StewardOrderFragment;
import com.entrada.cheekyMonkey.steward.discount.ICALL_ModificationRemark;
import com.entrada.cheekyMonkey.ui.CustomTextview;

/**
 * Created by ${Tanuj.Sareen} on 07/02/2015.
 */
public class Util {

    public static String getWifiMACAddress(Context context) {

        String mac_address = "";

        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);

        if (wifiManager.isWifiEnabled()) {

            WifiInfo info = wifiManager.getConnectionInfo();
            mac_address = info.getMacAddress();

        }
        //return "00:14:00:00:30:74";
        return mac_address;

    }

    public static void show_popup_success(final Context context, final String table, final String tableCode,
                                          String order_no, boolean isOrderPlace, final Object obj) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (isOrderPlace)
            builder.setTitle("Order Summary - ").setMessage(
                    "Order Saved Successfully !!");
        else
            builder.setTitle("Order Summary - ").setMessage(
                    "Order Updated Successfully !!");

        View v = LayoutInflater.from(context).inflate(R.layout.order_summary, null);

        final CustomTextview t_table, t_order, t_room, t_tableShow;
        t_table = (CustomTextview) v.findViewById(R.id.t_v_table_for_order_summry);
        t_order = (CustomTextview) v.findViewById(R.id.t_v_order_for_order_summry);
        t_room = (CustomTextview) v.findViewById(R.id.t_v_header_room_no_summary);
        t_tableShow = (CustomTextview) v.findViewById(R.id.t_v_header_table_for_o_summry);
        t_tableShow.setVisibility(View.VISIBLE);
        t_room.setVisibility(View.GONE);
        t_table.setText(table);
        t_order.setText(order_no);
        builder.setView(v);

        builder.setNegativeButton("Generate Bill",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });

        builder.setPositiveButton("Home",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });

        final AlertDialog dialog = builder.create();
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


        Button select_table = dialog.getButton(Dialog.BUTTON_POSITIVE);
        select_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                if (obj instanceof TakeOrderFragment)
                    //((TakeOrderFragment) obj).showHome();
                    ((BaseFragmentActivity) context).showHome();

               /* if (obj instanceof StewardOrderFragment)
                    ((StewardOrderFragment) obj).getAllTables();*/
            }
        });

        Button hide = dialog.getButton(Dialog.BUTTON_NEGATIVE);
        hide.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();

                if (!((BaseFragmentActivity)context).UserPermission("BG"))
                    ((BaseFragmentActivity) context).onCreateBillGenerate(table, tableCode);
                else
                    UserInfo.showAccessDeniedDialog(context,"Your ID is not authorized. Please Login with authorized User ID. ");
            }
        });

    }

    public static void show_popup_successNew(final Context context, final String table, final String tableCode,
                                             String order_no, boolean isOrderPlace,
                                             final Object obj) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (isOrderPlace)
            builder.setTitle("Order Summary - ").setMessage(
                    "Order Saved Successfully !!");
        else
            builder.setTitle("Order Summary - ").setMessage(
                    "Order Updated Successfully !!");

        View v = LayoutInflater.from(context).inflate(R.layout.order_summary, null);

        final CustomTextview t_table, t_order, t_room, t_tableShow;
        t_table = (CustomTextview) v.findViewById(R.id.t_v_table_for_order_summry);
        t_order = (CustomTextview) v.findViewById(R.id.t_v_order_for_order_summry);
        t_room = (CustomTextview) v.findViewById(R.id.t_v_header_room_no_summary);
        t_tableShow = (CustomTextview) v.findViewById(R.id.t_v_header_table_for_o_summry);
        t_tableShow.setVisibility(View.VISIBLE);
        t_room.setVisibility(View.GONE);
        t_table.setText(table);
        t_order.setText(order_no);
        builder.setView(v);

        builder.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });

       /* builder.setPositiveButton("Generate Bill",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        if ((obj instanceof StewardOrderFragment) && !((StewardOrderFragment) obj).UserPermission("BG"))
                            ((BaseFragmentActivity) context).onCreateBillGenerate(table, tableCode);
                    }
                });*/

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();

    }

    public static void show_popup_success1(final Context context, final String room, final String roomCode,
                                           String order_no, boolean isOrderPlace,
                                           final Object obj) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (isOrderPlace)
            builder.setTitle("Order Summary - ").setMessage(
                    "Order Saved Successfully !!");
        else
            builder.setTitle("Order Summary - ").setMessage(
                    "Order Updated Successfully !!");

        View v = LayoutInflater.from(context).inflate(R.layout.order_summary, null);

        final CustomTextview t_table, t_order, t_room, t_tableShow;
        t_table = (CustomTextview) v.findViewById(R.id.t_v_table_for_order_summry);
        t_order = (CustomTextview) v.findViewById(R.id.t_v_order_for_order_summry);
        t_room = (CustomTextview) v.findViewById(R.id.t_v_header_room_no_summary);
        t_tableShow = (CustomTextview) v.findViewById(R.id.t_v_header_table_for_o_summry);
        t_tableShow.setVisibility(View.GONE);
        t_room.setVisibility(View.VISIBLE);
        t_table.setText(room);
        t_order.setText(order_no);
        builder.setView(v);

        builder.setNegativeButton("Generate Bill",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });

        builder.setPositiveButton("Select Room",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();


        Button select_table = dialog.getButton(Dialog.BUTTON_POSITIVE);
        Button gnrt_bill = dialog.getButton(Dialog.BUTTON_NEGATIVE);
        select_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                if (obj instanceof StewardOrderFragment)
                    ((StewardOrderFragment) obj).getAllRooms();
            }
        });

        gnrt_bill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();

                if ((obj instanceof StewardOrderFragment) && !((StewardOrderFragment) obj).UserPermission("BG"))
                    ((BaseFragmentActivity) context).onCreateBillGenerate(room, roomCode);
            }
        });

    }



    public static void show_new_order_remark_popup(final Context context,
                                                   final ICALL_ModificationRemark icall_modificationRemark,
                                                   final boolean isModify) {

        AlertDialog.Builder objBuilder = new AlertDialog.Builder(context);
        if (isModify)
            objBuilder.setTitle("Enter Reason for Order Modification -");
        else
            objBuilder.setTitle("Enter Reason for Order Cancellation -");

        LayoutInflater inflator = LayoutInflater.from(context);

        View v_for_item_remark = inflator.inflate(
                R.layout.item_remark_dialog_box, null);

        final EditText editText_for_item_remark_obj = (EditText) v_for_item_remark
                .findViewById(R.id.edit_text_for_item_remark);

        objBuilder.setView(v_for_item_remark);

        objBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub

                    }
                });

        objBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });

        final AlertDialog dialog = objBuilder.create();
        dialog.show();

        Button theButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String reason = editText_for_item_remark_obj.getText().toString();
                if (!reason.isEmpty()) {

                    icall_modificationRemark.getOrderEditReason(reason, isModify);
                    dialog.dismiss();

                } else
                    Toast.makeText(context, "enter reason !!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public static void show_Bill_edit_popup(final Context context,
                                            final ICALL_ModificationRemark icall_modificationRemark,
                                            final boolean isModify, String cancelType) {

        AlertDialog.Builder objBuilder = new AlertDialog.Builder(context);
        if (isModify)
            objBuilder.setTitle("Enter Reason for Bill Modification -");
        else {

            if (cancelType.equals("C"))
                objBuilder.setTitle("Enter Reason for Bill Cancellation -");
            else if (cancelType.equals("CO"))
                objBuilder.setTitle("Enter Reason for Change Bill to Complimentary -");
            else
                objBuilder.setTitle("Enter Reason for Full Discount on Bill -");
        }

        LayoutInflater inflator = LayoutInflater.from(context);

        View v_for_item_remark = inflator.inflate(
                R.layout.item_remark_dialog_box, null);

        final EditText editText_for_item_remark_obj = (EditText) v_for_item_remark
                .findViewById(R.id.edit_text_for_item_remark);

        objBuilder.setView(v_for_item_remark);

        objBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub

                    }
                });

        objBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });

        final AlertDialog dialog = objBuilder.create();
        dialog.show();
        dialog.setCancelable(false);

        Button theButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String reason = editText_for_item_remark_obj.getText().toString();
                if (!reason.isEmpty()) {

                    icall_modificationRemark.getBillEditReason(reason, isModify);
                    dialog.dismiss();
                } else
                    Toast.makeText(context, "enter reason", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void show_undoSettle_reason_popup(final Context context,
                                                    final ICALL_ModificationRemark icall_modificationRemark) {

        AlertDialog.Builder objBuilder = new AlertDialog.Builder(context);
        objBuilder.setTitle("Enter reason for Undo Bill Settlement -");

        LayoutInflater inflator = LayoutInflater.from(context);

        View v_for_item_remark = inflator.inflate(
                R.layout.item_remark_dialog_box, null);

        final EditText editText_for_item_remark_obj = (EditText) v_for_item_remark
                .findViewById(R.id.edit_text_for_item_remark);

        objBuilder.setView(v_for_item_remark);

        objBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub

                    }
                });

        objBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });

        final AlertDialog dialog = objBuilder.create();
        dialog.show();

        Button theButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String reason = editText_for_item_remark_obj.getText().toString();
                if (!reason.isEmpty()) {

                    icall_modificationRemark.getUndoSettleReason(reason);
                    dialog.dismiss();
                } else
                    Toast.makeText(context, "enter reason", Toast.LENGTH_SHORT).show();
            }
        });

    }



    public static String checkStringIsEmpty(String string) {
        return TextUtils.isEmpty(string) ? "" : string;
    }

    public static String checkStringFloatIsEmpty(String string) {
        return TextUtils.isEmpty(string) ? "0.00" : numberFormat(Float
                .valueOf(string.trim()));
    }

    public static String checkStringIntIsEmpty(String string) {
        return TextUtils.isEmpty(string) ? "0" : string;
    }

    public static String numberFormat(float value) {

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("0.00");
        return df.format(value);
    }

    public static String numberFormatInt(float value) {

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("0.000");
        return df.format(value);
    }

    public static String numberFormat(Double value) {

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("0.00");
        return df.format(value);
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }



    public static String rgbToString(float r, float g, float b) {
        String rs = Integer.toHexString((int) (r * 256));
        String gs = Integer.toHexString((int) (g * 256));
        String bs = Integer.toHexString((int) (b * 256));
        return rs + gs + bs;
    }

    public static String checkStringisEmpty(String string) {
        return TextUtils.isEmpty(string) ? "" : string;
    }


    public static String numberfornmat(float value) {

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("0.00");
        return df.format(value);
    }

    public static String numberfornmatInt(float value) {

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("0.000");
        return df.format(value);
    }


    public static String saveToInternalSorage(Context context,
                                              Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        if (directory != null)
            directory.mkdir();
        // Create imageDir

        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to
            // the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mypath.getAbsolutePath();
    }

    public static String saveImage(Context context, Bitmap bitmapImage) {

        File backupDB;
        try {

            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                String backupDBPath = "/CSAT" + "/printdump" + ".jpeg";
                File direct = new File(Environment.getExternalStorageDirectory() + "/CSAT");

                if (!direct.exists()) {
                    if (direct.mkdir()) {

                    }
                }
                backupDB = new File(sd, backupDBPath);
                FileOutputStream fos = null;
                fos = new FileOutputStream(backupDB);
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                Logger.i("backupDB:", "::> " + backupDB.getAbsolutePath());
                return backupDB.getAbsolutePath();

            } else
                return "";

        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.i("imageDump:", "::> exception");
            return "";

        }

    }

    public static JSONArray readBytes(InputStream inputStream) throws IOException, JSONException {

        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        JSONArray array = new JSONArray();
        int len = 0, i = 0;
        while ((len = inputStream.read(buffer)) != -1) {

            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            byteBuffer.write(buffer, 0, len);
            byte[] b = byteBuffer.toByteArray();
            array.put(i, Base64.encodeToString(b, Base64.DEFAULT));
            i++;
        }
        return array;
    }

    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                resources.getDisplayMetrics());
        return (int) px;
    }



    public static String getVersionCode(Context context) {

        PackageInfo manager;
        try {

            manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return "" + manager.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String CheckJsonIsEmpty(JSONObject jsonObject,
                                          String jsonTagname) throws JSONException {
        if (jsonObject.has(jsonTagname))
            return TextUtils.isEmpty(jsonObject.getString(jsonTagname)) ? ""
                    : jsonObject.getString(jsonTagname);
        else
            return "";

    }



    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap convertToBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static byte[] getStoredImageAsByteArray(Context context){

        byte[] image = null ;
        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
        mdb.beginTransaction();
        Cursor cursor = mdb.rawQuery("Select * from " + DBConstants.IMAGE_TABLE,null);

        try {
            if (cursor != null && cursor.moveToFirst()){
                image = cursor.getBlob(1);
                cursor.close();
            }
            mdb.setTransactionSuccessful();

        }catch (SQLiteException e){e.printStackTrace();}
        finally {mdb.endTransaction();}

        return image;
    }

    public static void setProfilePic(Context context, ImageView imageView){

        byte[] image = Util.getStoredImageAsByteArray(context);

        if (image != null){
            imageView.setBackgroundResource(0);
            imageView.setImageBitmap(Util.convertToBitmap(image));
        }
    }

}
