package com.entrada.cheekyMonkey.entity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.dynamic.BaseFragmentActivity;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.steward.notificationUI.AdminActivity;
import com.entrada.cheekyMonkey.steward.notificationUI.KitchenActivity;
import com.entrada.cheekyMonkey.ui.CustomTextview;

/**
 * Created by Administrator on 29/12/2014.
 */
public class UserInfo {


    public static String USER_ID = "";
    public static String editText = "";
    public static String guest_id = "";
    public static String guest_name = "";
    public static String FORM_TYPE = "";
    public static String Format_no = "";
    public static String Order_no = "";
    public static String table = "";
    public static String tableCode = "";
    public static String pos_type = "";
    public static String kitchen_name = "";
    public static String Bill_no = "";
    public static String Orderno = "";
    public static String item_code = "";
    public static String multicheck = "";
    public static String Comp_Code = "";
    public static String RunDate = "";
    public String USER_NAME = "";
    public String User_Password = "";
    public String POS = "";
    public String PERMISSION = "";
    //public static String ServerIP = "192.168.43.93/tb";
    //public static String ServerIP = "entradasoft.ga";
    //public static String ServerIP = "192.168.2.104/tb";
    //public static String ServerIP = "iamrohit-001-site1.ftempurl.com";
    public static String ServerIP ="entradasoft-001-site1.htempurl.com";

    public String Theme_Background_Color = "#ff010101";
    public String Theme_Font_Color = "#FFFFFF";
    public String Text_Size = "20";
    public String Theme_Navigation_Color = "#1E1E1E";
    public String Theme_Navigation_Content_Color = "#1E1E1E";
    public String Theme_Navigation_Font_Color = "#FFFFFF";
    public String LANG = StaticConstants.LANG_BY_DEFAULT;
    public POSItem posItem = new POSItem();
    public static String deviceInfo = "";
    public static String order_status = "";
    public static boolean lock;
    public static String PREV_NET_Status = "";
    public static boolean alertIsBeingShown = false;
    public static String error_message = "";
    public static boolean appIsRunning = false;

    // Get both these google sign in detail from start integrating google sign in with android app
    public static String SenderID = "555927586239";
    public static String apikey = "AIzaSyDBPGQLo7MQ5S_ab_WH93pUMV2b55XxMyI";
    public static String deviceid = "";

    public static String getRunningDate() {

        String[] f_Date;
        String date = "";

        if (RunDate.contains("/")) {

            f_Date = RunDate.split("/");
            date = f_Date[2] + "/" + f_Date[1] + "/" + f_Date[0];
        } else if (RunDate.contains("-")) {

            f_Date = RunDate.split("-");
            date = f_Date[2] + "-" + f_Date[1] + "-" + f_Date[0];
        }

        return date;
    }

    public static String getCurrentDate() {

        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return df.format(c.getTime());
    }


    public static String changeDateToServerFormat(String dateToFormat) {

        try {

            Date date = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(dateToFormat);
            return new SimpleDateFormat("yyyy/MM/dd", Locale.US).format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateToFormat;
    }


    public static String getCurrentTime() {

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int seconds = c.get(Calendar.SECOND);

        // if (hour == 0) hour = 12;
        String h = hour < 10 ? "0" + hour : String.valueOf(hour);
        String m = minute < 10 ? "0" + minute : String.valueOf(minute);
        String s = seconds < 10 ? "0" + seconds : String.valueOf(seconds);

        // return  h +":"+ m + ":" + s;
        return h + ":" + m;
    }

    public String getText_Size() {
        return Text_Size;
    }

    public void setText_Size(String text_Size) {
        Text_Size = text_Size;
    }

    public String getComp_Code() {
        return Comp_Code;
    }

    public void setComp_Code(String comp_Code) {
        Comp_Code = comp_Code;
    }

    public String getRunDate() {
        return RunDate;
    }

    public void setRunDate(String runDate) {
        RunDate = runDate;
    }

    public String getTheme_Background_Color() {
        return Theme_Background_Color;
    }

    public void setTheme_Background_Color(String theme_Background_Color) {
        Theme_Background_Color = theme_Background_Color;
    }

    public String getTheme_Font_Color() {
        return Theme_Font_Color;
    }

    public void setTheme_Font_Color(String theme_Font_Color) {
        Theme_Font_Color = theme_Font_Color;
    }

    public String getPERMISSION() {
        return PERMISSION;
    }

    public void setPERMISSION(String PERMISSION) {
        this.PERMISSION = PERMISSION;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String user_id) {
        USER_ID = user_id;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getPOS() {
        return POS;
    }

    public void setPOS(String POS) {
        this.POS = POS;
    }

    public String getLANG() {
        return LANG;
    }

    public void setLANG(String LANG) {
        this.LANG = LANG;
    }

    public String getServerIP() {
        return ServerIP;
    }

    public void setServerIP(String serverIP) {
        ServerIP = serverIP;
    }

    public String getUser_Password() {
        return User_Password;
    }

    public void setUser_Password(String user_Password) {
        User_Password = user_Password;
    }

    public POSItem getPosItem() {
        return posItem;
    }

    public void setPosItem(POSItem posItem) {
        this.posItem = posItem;
    }

    public String getTheme_Navigation_Color() {
        return Theme_Navigation_Color;
    }

    public void setTheme_Navigation_Color(String theme_Background_Navigation_Color) {
        Theme_Navigation_Color = theme_Background_Navigation_Color;
    }

    public String getTheme_Navigation_Content_Color() {
        return Theme_Navigation_Content_Color;
    }

    public void setTheme_Navigation_Content_Color(String theme_Navigation_Content_Color) {
        Theme_Navigation_Content_Color = theme_Navigation_Content_Color;
    }

    public String getTheme_Navigation_Font_Color() {
        return Theme_Navigation_Font_Color;
    }

    public void setTheme_Navigation_Font_Color(String theme_Navigation_Font_Color) {
        Theme_Navigation_Font_Color = theme_Navigation_Font_Color;
    }

    /**************************************************************************************/

    public int getBackground() {

        return Color.parseColor(Theme_Background_Color);
    }

    public float getTextSize() {

        return Float.parseFloat(Text_Size);
    }

    public int getFontColor() {

        return Color.parseColor(Theme_Font_Color);
    }

    public int getNaviBackground() {

        return Color.parseColor(Theme_Navigation_Color);
    }

    public int getNaviContentBackgrounnd() {

        return Color.parseColor(Theme_Navigation_Content_Color);
    }

    public int getNaviFontColor() {

        return Color.parseColor(Theme_Navigation_Font_Color);
    }

    public static View.OnTouchListener onTouchListener() {

        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        };
    }


    //  show dialog section


    public static void showAccessDeniedDialog(Context context, String message) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        //Setting Dialog Title
        alertDialog.setTitle(R.string.no_perm_string);

        //Setting Dialog Message
        alertDialog.setMessage(message);

        //On Pressing Setting button
        alertDialog.setPositiveButton(R.string.ok_string, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        if (!((Activity) context).isFinishing())
            alertDialog.show();
    }

    public static void showNetFailureDialog(Context context) {

        if (alertIsBeingShown)
            return;

        alertIsBeingShown = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.layout_failure_msg, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);

        tv_title.setText(context.getText(R.string.conn_failed));
        tv_msg.setText(context.getText(R.string.hit_later));

        CustomTextview btn_Ok = (CustomTextview) view.findViewById(R.id.btn_dismiss);
        builder.setView(view);

        final AlertDialog dialog = builder.create();

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                alertIsBeingShown = false;
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
                    alertIsBeingShown = false;
                }
                return true;
            }
        });

        if (!((Activity) context).isFinishing())
            dialog.show();
    }

    public static void showLogoutDialog(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.exit_layout, null);
        TextView ctv_title = (TextView) view.findViewById(R.id.ctv_title);
        TextView ctv_message = (TextView) view.findViewById(R.id.tv_msg);
        CustomTextview yes = (CustomTextview) view.findViewById(R.id.tv_yes);
        CustomTextview no = (CustomTextview) view.findViewById(R.id.tv_cancel);

        ctv_title.setText(context.getText(R.string.sign_out_string));
        ctv_message.setText(context.getString(R.string.signOutFromApp));

        builder.setView(view);
        final AlertDialog dialog = builder.create();


        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (context instanceof BaseFragmentActivity) {
                    BaseFragmentActivity baseFragmentActivity = (BaseFragmentActivity) context;
                    baseFragmentActivity.logOut(true);
                } else if (context instanceof AdminActivity) {
                    AdminActivity adminActivity = (AdminActivity) context;
                    adminActivity.logOut(true);
                } else if (context instanceof KitchenActivity) {
                    KitchenActivity kitchenActivity = (KitchenActivity) context;
                    kitchenActivity.logOut(true);
                }

            }

        });

        no.setOnClickListener(new View.OnClickListener() {

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

        if (!((Activity) context).isFinishing())
            dialog.show();
    }

    public static void showLoginDialog(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.exit_layout, null);
        TextView ctv_title = (TextView) view.findViewById(R.id.ctv_title);
        TextView ctv_message = (TextView) view.findViewById(R.id.tv_msg);
        CustomTextview yes = (CustomTextview) view.findViewById(R.id.tv_yes);
        CustomTextview no = (CustomTextview) view.findViewById(R.id.tv_cancel);
        ctv_title.setText(context.getText(R.string.alert_string));
        ctv_message.setText(context.getString(R.string.sign_in_msg));

        builder.setView(view);
        final AlertDialog dialog = builder.create();


        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (context instanceof BaseFragmentActivity) {
                    BaseFragmentActivity baseFragmentActivity = (BaseFragmentActivity) context;
                    baseFragmentActivity.logOut(true);
                } else if (context instanceof AdminActivity) {
                    AdminActivity adminActivity = (AdminActivity) context;
                    adminActivity.logOut(true);
                } else if (context instanceof KitchenActivity) {
                    KitchenActivity kitchenActivity = (KitchenActivity) context;
                    kitchenActivity.logOut(true);
                }
            }

        });

        no.setOnClickListener(new View.OnClickListener() {

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

        if (!((Activity) context).isFinishing())
            dialog.show();
    }

    public static void showMessageDialog(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.msg_layout, null);
        CustomTextview yes = (CustomTextview) view.findViewById(R.id.tv_yes);
        CustomTextview no = (CustomTextview) view.findViewById(R.id.tv_cancel);

        builder.setView(view);
        final AlertDialog dialog = builder.create();

        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        no.setOnClickListener(new View.OnClickListener() {

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

    public static String getMixerName(String addonCode) {

        String addon = "";

        switch (addonCode) {

            case "A01":
                addon = "SODA";
                break;

            case "A02":
                addon = "Packed Water";
                break;

            case "A03":
                addon = "Red Bull";
                break;

            case "A04":
                addon = "Aerated Drinks";
                break;

            case "A05":
                addon = "Diet Coke";
                break;

            case "A06":
                addon = "Refer to ice";
                break;

            case "A07":
                addon = "Tonic Water";
                break;

            case "A08":
                addon = "Coke Can";
                break;

            case "A09":
                addon = "Juice";
                break;

            case "A10":
                addon = "Hot Water";
                break;

            case "A11":
                addon = "Chicken Tikka";
                break;
        }

        return addon;
    }

}
