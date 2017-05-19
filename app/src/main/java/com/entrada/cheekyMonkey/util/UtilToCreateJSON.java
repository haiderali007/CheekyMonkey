package com.entrada.cheekyMonkey.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.entrada.cheekyMonkey.Exception.SearchException;
import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.entity.GuestOrderItem;
import com.entrada.cheekyMonkey.entity.GusetOrderDetail;
import com.entrada.cheekyMonkey.entity.POSItem;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.staticData.PrefHelper;
import com.entrada.cheekyMonkey.staticData.ResultMessage;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.steward.billSplit.BillSplitAdapter1;
import com.entrada.cheekyMonkey.steward.billSplit.BillSplitAdapter2;
import com.entrada.cheekyMonkey.steward.discount.DiscountLayout;
import com.entrada.cheekyMonkey.steward.notificationUI.GstOrDetailAdp;
import com.entrada.cheekyMonkey.steward.ordersplit.OrderSplitAdapter;
import com.entrada.cheekyMonkey.steward.ordersplit.OrderSplitAdapter1;
import com.entrada.cheekyMonkey.steward.roomService.RoomItem;
import com.entrada.cheekyMonkey.takeorder.adapter.TakeOrderAdapter;
import com.entrada.cheekyMonkey.takeorder.entity.OrderDetail;
import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;
import com.entrada.cheekyMonkey.ui.CustomEditView;
import com.entrada.cheekyMonkey.ui.CustomTextview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abc on 1/25/2015.
 */
public class UtilToCreateJSON extends StaticConstants implements DBConstants {


    static String clientID = "";

    public static String createLoginJSON(String userID, String password,
                                         String wifi, String serverIP) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(JSON_TAG_LOGIN_USER_ID, userID);
            jsonObject.put(JSON_TAG_LOGIN_PASSWORD, password);
            jsonObject.put(JSON_TAG_WIFI, wifi);
            jsonObject.put(JSON_TAG_VENDOR, JSON_TAG_VENDOR_VALUE);
            jsonObject.put(JSON_TAG_LOGIN_SERVER_IP, serverIP);

        } catch (JSONException e) {

            e.printStackTrace();
        }

        return jsonObject.toString();

    }

    public static String createToken() {

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("clientID", clientID);
            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public static String createPOSJSON(Context context) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(JSON_TAG_POS_TOKEN, PrefHelper.getStoredString(
                    context, PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN));
            jsonObject.put(JSON_TAG_POS_MAC, Util.getWifiMACAddress(context));
            return jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static ResultMessage parseLoginResponseNew(Context context, String parameter, String response) {

        ResultMessage message = new ResultMessage();
        try {

            JSONObject jsonHeader = new JSONObject(response);
            JSONObject jsonObject = new JSONObject(jsonHeader.getString(JSON_TAG_LOGIN_RESULT));
            String token = jsonObject.getString(JSON_TAG_TOKEN);
            String test_error = jsonObject.getString(JSON_TAG_TEST_ERROR);

            if (test_error.equalsIgnoreCase("invalid device"))
                Toast.makeText(context, "Invalid Device, Do Tablet Setup first !!", Toast.LENGTH_SHORT).show();

            else if (test_error.equalsIgnoreCase("Server Not Found"))
                Toast.makeText(context, test_error, Toast.LENGTH_SHORT).show();

            else if (test_error.contains("Cannot open database"))
                Toast.makeText(context, "No such database", Toast.LENGTH_SHORT).show();

            else if (test_error.equalsIgnoreCase("inactive"))
                Toast.makeText(context, "In-Active User ID", Toast.LENGTH_SHORT).show();

            else if (test_error.equalsIgnoreCase("invalid UserID"))
                Toast.makeText(context, "Enter valid User id", Toast.LENGTH_SHORT).show();

            else if (test_error.equalsIgnoreCase("wrng_pass"))
                Toast.makeText(context, "Enter correct password", Toast.LENGTH_SHORT).show();

            else if (!token.isEmpty())
                message.isSuccess = true;

        } catch (Exception ex) {ex.printStackTrace();}

        return message;
    }


    public static String createGuestParamter(String name, String phone, String email_id,
                                             String dob, String m_day, String sex, String address,
                                             String address1, String spl_remark, String validdate,
                                             String discount, boolean updateGuest) {

        String parameter = "";
        JSONObject jsonParam = new JSONObject();
        try {

            jsonParam.put("Name", name);
            jsonParam.put("Phone", phone);
            jsonParam.put("Email", email_id);
            jsonParam.put("dob", dob);
            jsonParam.put("m_day", m_day);
            jsonParam.put("Sex", sex);
            jsonParam.put("Address", address);
            jsonParam.put("Address1", address1);
            jsonParam.put("Spl_remark", spl_remark);
            jsonParam.put("validdate", validdate);
            jsonParam.put("discount", discount);
            jsonParam.put("apikey", UserInfo.apikey);
            jsonParam.put("deviceid", UserInfo.deviceid);
            jsonParam.put("updateGuest", updateGuest);
            jsonParam.put("clientID", clientID);

            Logger.i("input", jsonParam.toString());
            parameter = jsonParam.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";
        }
        return parameter;
    }


    public static String createGstSigninParameter(String email, String password) {
        String parameter ;

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("clientID", clientID);
            jsonParam.put("email", email);
            jsonParam.put("pass", password);
            jsonParam.put("deviceid", UserInfo.deviceid);

            Logger.i("input", jsonParam.toString());
            parameter = jsonParam.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";
        }
        return parameter;

    }


    public static String createGuestOrderStatus(Context context, String odrNo) {
        String parameter = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("clientID", clientID);
            jsonObject.put("Ord_No", odrNo);

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        }
        return parameter;
    }

    public static String createGuestStatus(String email, String status) {
        String parameter = "";
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("clientID", clientID);
            jsonObject.put("Email", email);
            jsonObject.put("Status", status);

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        }
        return parameter;
    }

    public static String createTablesJSON(Context context) {

        JSONObject jsonObject = new JSONObject();
        try {

            String token = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);

            jsonObject.put("clientID", clientID);
            jsonObject.put(JSON_TAG_TABLE_TAG, "");
            jsonObject.put(JSON_TAG_DATA_SYNC_TOKEN, token);
            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }


    public static void fetchLoginResponse(Context context) {


        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();

        mdb.beginTransaction();
        try {
            String queryUsrTable = "SELECT * FROM " + KEY_USR_TABLE;
            Cursor cursor = mdb.rawQuery(queryUsrTable, null);

            if (cursor.moveToFirst()) {
                UserInfo userInfo = new UserInfo();

                userInfo.setUSER_ID(cursor.getString(cursor
                        .getColumnIndex(KEY_USR_USERNAME)));
                userInfo.setUser_Password(cursor.getString(cursor
                        .getColumnIndex(KEY_USR_PASSWORD)));
                userInfo.setServerIP(cursor.getString(cursor
                        .getColumnIndex(KEY_USR_SERVER_ID)));
                userInfo.setComp_Code(cursor.getString(cursor
                        .getColumnIndex(KEY_COMP_CODE)));
                userInfo.setRunDate(cursor.getString(cursor
                        .getColumnIndex(KEY_RUNNING_DATE)));

                setThemeDetail(context, userInfo);
                POSApplication.getSingleton().getmDataModel().setUserInfo(userInfo);
            }
            cursor.close();
            mdb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

    }


    public static String createGuestOrder(Context context, String guestID, String guestName,
                                          String cover, String steward, TakeOrderAdapter takeOrderAdapter,
                                          String ordertype, String orderRemark) {

        UserInfo userInfo = POSApplication.getSingleton().getmDataModel()
                .getUserInfo();
        OrderDetail detail = takeOrderAdapter.getOrderDetail();
        String token = PrefHelper.getStoredString(context,
                PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);

        JSONObject jsonParam = null;
        JSONArray jarray_code, jarray_StrQty, jarray_ItemRemark, jarray_strAddon, jarray_strModi,
                jarray_strCombo, jarray_strHappy, jarry_disc, jarray_sub_item, jarray_meal, jarray_cover, jarray_price, jarray_surcharge ;

        try {
            jsonParam = new JSONObject();

            jarray_code = new JSONArray();
            jarray_StrQty = new JSONArray();
            jarray_ItemRemark = new JSONArray();
            jarray_strAddon = new JSONArray();
            jarray_strModi = new JSONArray();
            jarray_strCombo = new JSONArray();
            jarray_strHappy = new JSONArray();
            jarry_disc = new JSONArray();
            jarray_sub_item = new JSONArray();
            jarray_cover = new JSONArray();
            jarray_meal = new JSONArray();
            jarray_surcharge = new JSONArray();
            jarray_price = new JSONArray();

            OrderItem list_obj;
            for (int i = 0; i < takeOrderAdapter.getCount(); i++) {

                list_obj = (OrderItem) takeOrderAdapter.getItem(i);

                if (!list_obj.getO_code().equals("")) {
                    jarray_code.put(list_obj.getO_code());
                    jarray_StrQty.put(list_obj.getO_quantity());
                    jarray_ItemRemark.put(list_obj.getO_itm_rmrk());
                    jarray_strAddon.put(list_obj.getO_addon_code());
                    jarray_strHappy.put(list_obj.getO_happy_hour());
                    //jarry_disc.put(discountLayout.getDiscPer(i));
                    jarray_cover.put(list_obj.getO_cover_item());
                    jarray_meal.put(list_obj.getO_meal_cors_item());

                    jarray_surcharge.put(list_obj.getO_surcharge());
                    jarray_price.put(list_obj.getO_price());

                    if (!list_obj.getO_sub_item().equals("")) {

                        jarray_strModi.put("");
                        jarray_sub_item.put(list_obj.getO_sub_item());

                    } else {

                        if (list_obj.getO_name().contains("-")
                                || !(list_obj.getO_mod().isEmpty())) {

                            jarray_strModi.put(list_obj.getO_name());
                        } else {
                            jarray_strModi.put("");
                        }

                        jarray_sub_item.put(list_obj.getO_sub_item());
                    }
                    jarray_strCombo.put(list_obj.getO_combo_code());
                } else {

                    jarray_code.put("");
                    jarray_StrQty.put("");
                    jarray_ItemRemark.put("");
                    jarray_strAddon.put("");
                    jarray_strHappy.put("");
                    jarray_strModi.put(String.valueOf((char) 1));
                    jarray_strCombo.put("");
                    jarry_disc.put("");
                    jarray_sub_item.put("");
                    jarray_cover.put("");
                    jarray_meal.put("");
                    jarray_surcharge.put("");
                    jarray_price.put("");

                }

            }

            jsonParam.put("clientID", clientID);
            jsonParam.put(StaticConstants.JSON_TAG_GOP_POS, detail.getPosItem().posCode);
            jsonParam.put(JSON_TAG_GOP_TABLE, detail.getTableItem().getCode());
            jsonParam.put(JSON_TAG_GOP_STEWARD, steward);
            jsonParam.put(JSON_TAG_GOP_USERID, userInfo.getUSER_ID());
            jsonParam.put(JSON_TAG_GOP_GUEST_CODE, guestID);
            jsonParam.put(JSON_TAG_GOP_GUEST_NAME, guestName);
            jsonParam.put(JSON_TAG_GOP_ITEM, jarray_code);
            jsonParam.put(JSON_TAG_GOP_QTY, jarray_StrQty);
            jsonParam.put(JSON_TAG_GOP_PRICE, jarray_price);
            jsonParam.put(JSON_TAG_GOP_REMARK, jarray_ItemRemark);
            jsonParam.put(JSON_TAG_GOP_TOEKN, token);
            jsonParam.put(JSON_TAG_GOP_COVER, cover);
            jsonParam.put(JSON_TAG_GOP_ORDER_REMARK, orderRemark);
            jsonParam.put(JSON_TAG_GOP_ORDER_TYPE, ordertype);
            jsonParam.put(JSON_TAG_SERVER_API_KEY, UserInfo.apikey);
            jsonParam.put(JSON_TAG_DEVICE_ID, UserInfo.deviceid);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /***********
         * string POS, string Tbl, string Stw, string Userid, string GstCode,
         * string GstName, List<string> Item, List<string> Qty, List<string>
         * Remark, string token, string cvr, string odrem, string odtype
         *******************/

        return jsonParam.toString();
    }


    public void getGuestDetailResponse(Context context, String jsonString,
                                       ResultMessage message) {

        // {"ECABS_DisplayGuestResult":[]}

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.has(JSON_TAG_GUEST_RESPONSE)) {

                JSONArray jsonArray = jsonObject
                        .getJSONArray(JSON_TAG_GUEST_RESPONSE);
                int lenght = jsonArray.length();

                if (lenght > 0) {
                    message.RESPONSE = jsonArray.toString();
                    message.STATUS = ASYN_RESULT_OK;
                } else {
                    message.STATUS = SearchException.NO_RESULTS;
                    message.RESPONSE = jsonArray.toString();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void getGuestOrderResponse(Context context, String jsonString,
                                      ResultMessage message) {

        // {"ECABS_GuestOrderResult":"OB000001"}
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.has(JSON_TAG_GOP_RESPONSE)) {
                message.RESPONSE = jsonObject.getString(JSON_TAG_GOP_RESPONSE);
                message.STATUS = ASYN_RESULT_OK;

            } else {
                message.STATUS = SearchException.NO_RESULTS;
                message.RESPONSE = "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public static String createBillTransferJSON(Context context, String newPos,
                                                String posType, String billNo, String reason) {

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("pos", newPos);
            jsonObject.put("postype", posType);
            jsonObject.put("billno", billNo);
            jsonObject.put("reason", reason);

            jsonObject.put(JSON_TAG_POS_TOKEN, PrefHelper.getStoredString(
                    context, PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN));
            jsonObject.put(JSON_TAG_WIFI, Util.getWifiMACAddress(context));

            return jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String createBillModifyJSON(Context context, TakeOrderAdapter takeOrderAdapter,
                                              DiscountLayout discountLayout, String billno,
                                              String tbl, String reason, String steward) {

        String posCode = POSApplication.getSingleton().getmDataModel().getUserInfo().posItem.posCode;
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String orderNo = "";

        try {

            for (int i = 0; i < takeOrderAdapter.getCount(); i++) {

                OrderItem list_obj = (OrderItem) takeOrderAdapter.getItem(i);

                if (i == 0 || !list_obj.getOrderNo().isEmpty())
                    orderNo = list_obj.getOrderNo();

                jsonArray.put(
                        "itemcode" + ":" + list_obj.getO_code() + ";" +
                                "qty" + ":" + list_obj.getO_quantity() + ";" +
                                "price" + ":" + list_obj.getO_price() + ";" +
                                "ordno" + ":" + orderNo + ";" +
                                "ItemRemark" + ":" + list_obj.getO_itm_rmrk() + ";" +
                                "strAddon" + ":" + list_obj.getO_addon_code() + ";" +
                                "strModi" + ":" + list_obj.getO_mod() + ";" +
                                "strCombo" + ":" + list_obj.getO_combo_code() + ";" +
                                "strdisc" + ":" + discountLayout.getDiscPer(i) + ";");

            }

            jsonObject.put("pos", posCode);
            jsonObject.put("Stw", "#" + steward);
            jsonObject.put("cvr", "0");
            jsonObject.put("GstName", "");
            jsonObject.put("odtype", "");
            jsonObject.put("billno", billno);
            jsonObject.put("tbl", tbl);
            jsonObject.put("mod", jsonArray);
            jsonObject.put("remark", reason);

            jsonObject.put(JSON_TAG_POS_TOKEN, PrefHelper.getStoredString(
                    context, PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN));
            jsonObject.put(JSON_TAG_WIFI, Util.getWifiMACAddress(context));

            return jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }


    public static String createBillSplitJSON(Context context, String posCode, String billno,
                                             String table, BillSplitAdapter1 billSplitAdapter1,
                                             BillSplitAdapter2 billSplitAdapter2, String steward) {

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();

        try {

            for (int i = 0; i < billSplitAdapter1.getCount(); i++) {

                OrderItem list_obj = billSplitAdapter1.getItem(i);

                jsonArray1.put(
                        "itemcode" + ":" + list_obj.getO_code() + ";" +
                                "qty" + ":" + list_obj.getO_quantity() + ";" +
                                "price" + ":" + list_obj.getO_price() + ";" +
                                "ordno" + ":" + list_obj.getOrderNo() + ";" +
                                "ItemRemark" + ":" + list_obj.getO_itm_rmrk() + ";" +
                                "strAddon" + ":" + list_obj.getO_addon_code() + ";" +
                                "strModi" + ":" + list_obj.getO_mod() + ";" +
                                "strCombo" + ":" + list_obj.getO_combo_code() + ";" +
                                "strdisc" + ":" + list_obj.o_disc + ";");

            }

            for (int i = 0; i < billSplitAdapter2.getCount(); i++) {

                OrderItem list_obj = billSplitAdapter2.getItem(i);

                jsonArray2.put(
                        "itemcode" + ":" + list_obj.getO_code() + ";" +
                                "qty" + ":" + list_obj.getO_quantity() + ";" +
                                "price" + ":" + list_obj.getO_price() + ";" +
                                "ordno" + ":" + "" + ";" +
                                "ItemRemark" + ":" + list_obj.getO_itm_rmrk() + ";" +
                                "strAddon" + ":" + list_obj.getO_addon_code() + ";" +
                                "strModi" + ":" + list_obj.getO_mod() + ";" +
                                "strCombo" + ":" + list_obj.getO_combo_code() + ";" +
                                "strdisc" + ":" + list_obj.o_disc + ";");
            }

            jsonObject.put("pos", posCode);
            jsonObject.put("oldtbl", table);
            jsonObject.put("newtbl", "");
            jsonObject.put("Stw", "#" + steward);
            jsonObject.put("cvr", "0");
            jsonObject.put("GstName", "");
            jsonObject.put("odtype", "");
            jsonObject.put("billno", billno);
            jsonObject.put("mod", jsonArray1);
            jsonObject.put("transfer", jsonArray2);

            jsonObject.put(JSON_TAG_POS_TOKEN, PrefHelper.getStoredString(
                    context, PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN));
            jsonObject.put(JSON_TAG_WIFI, Util.getWifiMACAddress(context));

            return jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }


    public static String createPosJSON(String pos) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(JSON_TAG_DATA_SYNC_POS, pos);
            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }


    public static String createPosJSON(String pos, String table, String flag) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(JSON_TAG_DATA_SYNC_POS_CODE, pos);
            jsonObject.put("flag", flag);
            jsonObject.put("tbl",table);
            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String createOrderNoParameter(Context context, String table) {

        JSONObject jsonObject = new JSONObject();
        try {

            String token = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);

            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);

            jsonObject.put(JSON_TAG_ORDER_NO_POS, pos);
            jsonObject.put(JSON_TAG_ORDER_NO_TABLE, table);
            jsonObject.put(JSON_TAG_ORDER_NO_TOKEN, token);
            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }


    public static String createCommentBillNoJson(Context context, String selectedTable) {
        String parameter = "";
        JSONObject jsonObject = new JSONObject();
        try {

            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);

            jsonObject.put("pos", pos);
            jsonObject.put("dat", UserInfo.getRunningDate() );
            jsonObject.put("tbl", selectedTable);


            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        }
        return parameter;
    }

    public static boolean parseTakeOrderResponse(Context context, String table, String tableCode,
                                                 String response, Object obj) {
        JSONObject jsonObject ;
        try {
            jsonObject = new JSONObject(response);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("ECABS_PushOrderAndroid_separatorResult"));
            Logger.i("parseTakeOrderResponse", jsonObject.toString());

            if (jsonObject.getString("ECABS_PushOrderAndroid_separatorResult").equals("Please try Again!!")) {
                return false;

            } else {

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);

                    String result = jsonObject.getString("OrderNo");
                    UserInfo.Format_no = jsonObject.getString("OrdFormat");
                    UserInfo.Order_no = result;

                    if (table.isEmpty())
                        table = tableCode;

                    UserInfo.table = table;
                    UserInfo.tableCode = tableCode;
                    Util.show_popup_success(context, table, tableCode, result, true, obj);
                }

                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }

        return false;
    }



    public static boolean parseTakeOrderResponse1(Context context, String room, String roomCode,
                                                  String response, Object obj) {

        JSONObject jsonObject ;


        try {
            jsonObject = new JSONObject(response);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("ECABS_PushOrderAndroid_separatorResult"));
            Logger.i("parseTakeOrderResponse", jsonObject.toString());

            if (jsonObject.getString("ECABS_PushOrderAndroid_separatorResult").equals("Please try Again!!")) {
                jsonObject.getString("ECABS_PushOrderAndroid_separatorResult");
                return false;

            } else {

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    jsonObject = jsonArray.getJSONObject(i);
                    String result = jsonObject.getString("OrderNo");
                    UserInfo.Format_no = jsonObject.getString("OrdFormat");
                    UserInfo.Order_no = result;

                    if (room.isEmpty())
                        room = roomCode;
                    Util.show_popup_success1(context, room, roomCode, result, true, obj);
                }


                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }

        return false;
    }


    public static boolean parseTakeOrderResponseNew(Context context, String table, String tableCode,
                                                    String response, Object obj) {
        JSONObject jsonObject ;
        try {
            jsonObject = new JSONObject(response);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("ECABS_PushOrderAndroid_separatorResult"));
            Logger.i("parseTakeOrderResponse", jsonObject.toString());

            if (jsonObject.getString("ECABS_PushOrderAndroid_separatorResult").equals("Please try Again!!")) {
                return false;

            } else {

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);

                    String result = jsonObject.getString("OrderNo");
                    UserInfo.Format_no = jsonObject.getString("OrdFormat");
                    UserInfo.Order_no = result;

                    if (table.isEmpty())
                        table = tableCode;

                    UserInfo.table = table;
                    UserInfo.tableCode = tableCode;
                    Util.show_popup_successNew(context, table, tableCode, result, true, obj);
                }

                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }

        return false;
    }

    public static String createDelDetailJSON() {

        String pos = POSApplication.getSingleton().getmDataModel().getUserInfo().posItem.posCode;
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(JSON_TAG_DATA_SYNC_POS, pos);
            jsonObject.put("date", UserInfo.getRunningDate());

        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
        return jsonObject.toString();
    }



    public static boolean parseHomeDelevryOrderResponse(Context context, String response, CustomTextview textviewForBIllno,
                                                        CustomTextview textviewForDiscount, CustomTextview textviewForSubtotal,
                                                        CustomTextview textviewForTax, CustomTextview textviewForTotal,
                                                        CustomTextview textviewChange) {

        JSONObject jsonObject ;

        try {
            jsonObject = new JSONObject(response);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("ECABS_bill_generateResult"));
            Logger.i("ECABS_bill_generate", jsonObject.toString());

            if (jsonObject.getString("ECABS_bill_generateResult").equals("Please try Again!!")) {
                jsonObject.getString("ECABS_bill_generateResult");
                return false;

            } else {

                String bill_no = jsonArray.getJSONObject(0).getString("BILL_NO");
                String discount = jsonArray.getJSONObject(0).getString("DISCOUNT");
                String subtotal = jsonArray.getJSONObject(0).getString("SUBTOTAL");
                String taxes = jsonArray.getJSONObject(0).getString("TAXES");
                String total = jsonArray.getJSONObject(0).getString("TOTAL");
                  /*  message = "BillNo-" + biil_no + ", Discount- "
                            + discont + ",Subtotal-" + subtotal + ",Taxes-"
                            + taxes + ",Total- " + total;
*/
                textviewForBIllno.setText(bill_no);
                textviewForDiscount.setText(discount);
                textviewForSubtotal.setText(subtotal);
                textviewForTax.setText(taxes);
                textviewForTotal.setText(total);
                textviewChange.setText("-" + total);

                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return false;
    }

    public static boolean parseTestGenerateResponse(String response, CustomTextview textviewForBIllno, CustomTextview textviewForDiscount, CustomTextview textviewForSubtotal, CustomTextview textviewForTax, CustomTextview textviewForTotal) {

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("Ecabs_TestGenerateResult"));
            Logger.i("Ecabs_TestGenerate", jsonObject.toString());

            if (jsonObject.getString("Ecabs_TestGenerateResult").equals("Please try Again!!")) {
                jsonObject.getString("Ecabs_TestGenerateResult");
                return false;

            } else {

                textviewForBIllno.setText(jsonArray.getJSONObject(0).getString("BILL_NO"));
                textviewForDiscount.setText(jsonArray.getJSONObject(0).getString("DISCOUNT"));
                textviewForSubtotal.setText(jsonArray.getJSONObject(0).getString("SUBTOTAL"));
                textviewForTax.setText(jsonArray.getJSONObject(0).getString("TAXES"));
                textviewForTotal.setText(jsonArray.getJSONObject(0).getString("TOTAL"));

                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return false;
    }

    public static boolean parseOrderModificationResponse(Context context, String table, String tableCode,
                                                         String response,
                                                         String result, Object obj) {

        JSONObject jsonObject;
        // {"ECABS_OrderModiAndroid_newResult":true}
        try {

            jsonObject = new JSONObject(response);
            Logger.i("parseTakeOrderResponse", jsonObject.toString());

            if (jsonObject.getString("ECABS_OrderModiAndroid_newResult").equals("Please try Again!!")) {

                jsonObject.getString("ECABS_OrderModiAndroid_newResult");
                return false;

            } else {
                /*String result = jsonObject
                        .getString("ECABS_OrderModiAndroid_newResult");*/
                if (! table.isEmpty())
                    Util.show_popup_success(context, table,tableCode, result, false, obj);
                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }

        return false;
    }


    public static boolean ParseGuestReg(Context context, String response) {

        Logger.i("Save_Guest_Feedback_new", response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            String result = jsonObject.getString("Save_Guest_Feedback_newResult");

            if (result.equals("Guest Saved")) {
                return true;

            } else {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                return false;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }

        return false;
    }

    public static boolean ParseCommentSave(Context context, String response) {

        Logger.i("guest_rating", response);

        try {

            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("guest_ratingResult").equals("Data Saved")) {

                Toast.makeText(context,"detail saved",Toast.LENGTH_SHORT).show();
                return true;
            }
            else {
                Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }


    public static boolean parseFormTypeResponse(Context context, String response) {

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(response);
            Logger.i("ECABS_CheckFromType", jsonObject.toString());
            if (jsonObject.getString("ECABS_CheckFromTypeResult")
                    .equals("Please try Again!!")) {
                jsonObject.getString("ECABS_CheckFromTypeResult");
                return false;
            } else {

                UserInfo.FORM_TYPE = jsonObject.getString("ECABS_CheckFromTypeResult");

                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {

        }

        return false;
    }

    public static boolean parseMultiCheckResponse(Context context, String response) {

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(response);
            Logger.i("ECABS_CheckQuantity", jsonObject.toString());
            if (jsonObject.getString("ECABS_CheckQuantityResult")
                    .equals("Please try Again!!")) {
                jsonObject.getString("ECABS_CheckQuantityResult");
                return false;
            } else {

                UserInfo.multicheck = jsonObject.getString("ECABS_CheckQuantityResult");

                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {

        }

        return false;
    }

    public static String createFetchOrderJSON(Context context, String orderNumber,
                                              TakeOrderAdapter takeOrderAdapter) {

        JSONObject jsonObject = null;

        try {
            String token = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);
            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);
            String table = takeOrderAdapter.getOrderDetail().getTableItem().getCode();

            jsonObject = new JSONObject();
            jsonObject.put(StaticConstants.JSON_TAG_ORDER_DETAIL_POS, pos);
            jsonObject.put(StaticConstants.JSON_TAG_ORDER_DETAIL_TOKEN, token);
            jsonObject.put(StaticConstants.JSON_TAG_ORDER_DETAIL_TABLE, table);
            jsonObject.put(StaticConstants.JSON_TAG_ORDER_DETAIL_NO, orderNumber);
            Logger.i("input", jsonObject.toString());

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String createOrderModification(Context context,
                                                 TakeOrderAdapter takeOrderAdapter, String orderNumber,
                                                 DiscountLayout discountLayout, String orderRemarkReason,
                                                 String steward) throws JSONException {

        OrderDetail detail = takeOrderAdapter.getOrderDetail();

        String token = PrefHelper.getStoredString(context,
                PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);

        JSONObject jsonParam = null;
        JSONArray jarray_code = null, jarray_StrQty = null, jarray_ItemRemark = null, jarray_strAddon = null, jarray_strModi = null, jarray_strCombo = null, jarray_strHappy = null, jarry_disc = null;

        jarray_code = new JSONArray();
        jarray_StrQty = new JSONArray();
        jarray_ItemRemark = new JSONArray();
        jarray_strAddon = new JSONArray();
        jarray_strModi = new JSONArray();
        jarray_strCombo = new JSONArray();
        jarray_strHappy = new JSONArray();
        jarry_disc = new JSONArray();

        for (int i = 0; i < takeOrderAdapter.getCount(); i++) {

            OrderItem list_obj = (OrderItem) takeOrderAdapter.getItem(i);

            jarray_code.put(list_obj.getO_code());
            jarray_StrQty.put(list_obj.getO_quantity());
            jarray_ItemRemark.put(list_obj.getO_itm_rmrk());
            jarray_strAddon.put(list_obj.getO_addon_code());
            jarray_strHappy.put(list_obj.getO_happy_hour());
            jarry_disc.put(discountLayout.getDiscPer(i));

            if (list_obj.getO_name().contains("-")
                    || !(list_obj.getO_mod().isEmpty())) {

                jarray_strModi.put(list_obj.getO_name());
            } else {
                jarray_strModi.put("");
            }
            jarray_strCombo.put(list_obj.getO_combo_code());

        }

        jsonParam = new JSONObject();

        jsonParam.put("pos", detail.getPosItem().posCode);
        jsonParam.put("Order", orderNumber);
        jsonParam.put("Tbl", detail.getTableItem().getCode());
        jsonParam.put("Stw", "#" + steward);
        jsonParam.put("ItemCode", jarray_code);
        jsonParam.put("StrQty", jarray_StrQty);
        jsonParam.put("token", token);
        jsonParam.put("ORemark", orderRemarkReason);
        jsonParam.put("cvr", "0");
        jsonParam.put("ItemRemark", jarray_ItemRemark);
        jsonParam.put("strAddon", jarray_strAddon);
        jsonParam.put("GstName", "");
        jsonParam.put("strModi", jarray_strModi);
        jsonParam.put("odtype", "");
        jsonParam.put("strCombo", jarray_strCombo);
        jsonParam.put("strdisc", jarry_disc);
        jsonParam.put("WIFI", Util.getWifiMACAddress(context));

        return jsonParam.toString();
    }


    public static String createOrderCancelJson(Context context,
                                               TakeOrderAdapter takeOrderAdapter, String orderNumber,
                                               String orderRemarkReason) throws JSONException {

        OrderDetail detail = takeOrderAdapter.getOrderDetail();

        String parameter = "";
        JSONObject jsonObject = new JSONObject();
        try {

            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);
            String token = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);


            jsonObject = new JSONObject();
            jsonObject.put("POS", detail.getPosItem().posCode);
            jsonObject.put("token", token);
            jsonObject.put("remark", orderRemarkReason);
            jsonObject.put("userid", UserInfo.guest_id);
            jsonObject.put("order", orderNumber);

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        }
        return parameter;
    }


    public static String createTableCancelJson(Context context, TakeOrderAdapter takeOrderAdapter,
                                               String reason, String steward, String flag) throws JSONException {

        OrderDetail detail = takeOrderAdapter.getOrderDetail();

        String parameter = "";
        JSONObject jsonObject;
        try {

            String token = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);


            jsonObject = new JSONObject();
            jsonObject.put("pos", detail.getPosItem().posCode);
            jsonObject.put("token", token);
            jsonObject.put("WIFI", Util.getWifiMACAddress(context));
            jsonObject.put("remark", reason);
            jsonObject.put("userid", steward);
            jsonObject.put("tbl", detail.getTableItem().getCode());
            jsonObject.put("flag", flag);


            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return parameter;
    }

    public static String createBillCancelJson(String billNumber, String reason,
                                              String flag) throws JSONException {

        String parameter = "";
        JSONObject jsonObject;
        try {


            jsonObject = new JSONObject();
            jsonObject.put("remark", reason);
            jsonObject.put("billno", billNumber);

            if (!flag.isEmpty())
                jsonObject.put("flag", flag);

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        }
        return parameter;
    }

    public static boolean parseOrderCancelResponse(Context context, String Table, String response) {

        JSONObject jsonObject = null;
        // {"ECABS_OrderModiAndroid_newResult":true}
        try {
            jsonObject = new JSONObject(response);
            Logger.i("parseTakeOrderResponse", jsonObject.toString());
            if (jsonObject.getString("ECABS_PushOrdCancelResult").equals(
                    "Please try Again!!")) {
                jsonObject.getString("ECABS_PushOrdCancelResult");
                return false;
            } else {
                String result = jsonObject.getString("ECABS_PushOrdCancelResult");

                if (result.equals("true"))
                    Toast.makeText(context, "successfully cancelled ", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                // Util.show_popup_success(context, Table, result, false);
                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {

        }

        return false;
    }


    public static void setThemeDetail(Context context, UserInfo userInfo) {

        String background = PrefHelper.getStoredString(
                context, PrefHelper.PREF_FILE_NAME, PrefHelper.BACKGROUND_COLOR);
        String font_color = PrefHelper.getStoredString(
                context, PrefHelper.PREF_FILE_NAME, PrefHelper.FONT_COLOR);
        String font_size = PrefHelper.getStoredString(
                context, PrefHelper.PREF_FILE_NAME, PrefHelper.TEXT_SIZE);
        String navi_color = PrefHelper.getStoredString(
                context, PrefHelper.PREF_FILE_NAME, PrefHelper.Navigation_Color);
        String navi_content_color = PrefHelper.getStoredString(
                context, PrefHelper.PREF_FILE_NAME, PrefHelper.Navigation_Content_Color);
        String navi_font_color = PrefHelper.getStoredString(
                context, PrefHelper.PREF_FILE_NAME, PrefHelper.Navigation_Font_Color);

        if (!background.isEmpty())
            userInfo.setTheme_Background_Color(background);
        if (!font_color.isEmpty())
            userInfo.setTheme_Font_Color(font_color);
        if (!font_size.isEmpty())
            userInfo.setText_Size(font_size);
        if (!navi_color.isEmpty())
            userInfo.setTheme_Navigation_Color(navi_color);
        if (!navi_content_color.isEmpty())
            userInfo.setTheme_Navigation_Color(navi_content_color);
        if (!navi_font_color.isEmpty())
            userInfo.setTheme_Navigation_Font_Color(navi_font_color);
    }

    public static String createGuestLogin(String guestPhone) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(JSON_TAG_GUEST_PHONE, guestPhone);
            return jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String createGuestOrder(Context context, String guestID, String guestName,
                                          String cover, String steward, TakeOrderAdapter takeOrderAdapter,
                                          DiscountLayout discountLayout, String ordertype, String orderRemark) {

        UserInfo userInfo = POSApplication.getSingleton().getmDataModel()
                .getUserInfo();
        OrderDetail detail = takeOrderAdapter.getOrderDetail();
        String token = PrefHelper.getStoredString(context,
                PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);

        JSONObject jsonParam = null;
        JSONArray jarray_code , jarray_StrQty, jarray_ItemRemark, jarray_strAddon, jarray_strModi, jarray_strCombo, jarray_strHappy, jarry_disc, jarray_sub_item, jarray_meal, jarray_cover, jarray_price, jarray_surcharge;

        try {
            jsonParam = new JSONObject();

            jarray_code = new JSONArray();
            jarray_StrQty = new JSONArray();
            jarray_ItemRemark = new JSONArray();
            jarray_strAddon = new JSONArray();
            jarray_strModi = new JSONArray();
            jarray_strCombo = new JSONArray();
            jarray_strHappy = new JSONArray();
            jarry_disc = new JSONArray();
            jarray_sub_item = new JSONArray();
            jarray_cover = new JSONArray();
            jarray_meal = new JSONArray();
            jarray_surcharge = new JSONArray();
            jarray_price = new JSONArray();

            OrderItem list_obj;
            for (int i = 0; i < takeOrderAdapter.getCount(); i++) {

                list_obj = (OrderItem) takeOrderAdapter.getItem(i);

                if (!list_obj.getO_code().equals("")) {
                    jarray_code.put(list_obj.getO_code());
                    jarray_StrQty.put(list_obj.getO_quantity());
                    jarray_ItemRemark.put(list_obj.getO_itm_rmrk());
                    jarray_strAddon.put(list_obj.getO_addon_code());
                    jarray_strHappy.put(list_obj.getO_happy_hour());
                    jarry_disc.put(discountLayout.getDiscPer(i));
                    jarray_cover.put(list_obj.getO_cover_item());
                    jarray_meal.put(list_obj.getO_meal_cors_item());

                    jarray_surcharge.put(list_obj.getO_surcharge());
                    jarray_price.put(list_obj.getO_price());

                    if (!list_obj.getO_sub_item().equals("")) {

                        jarray_strModi.put("");
                        jarray_sub_item.put(list_obj.getO_sub_item());

                    } else {

                        if (list_obj.getO_name().contains("-")
                                || !(list_obj.getO_mod().isEmpty())) {

                            jarray_strModi.put(list_obj.getO_name());
                        } else {
                            jarray_strModi.put("");
                        }

                        jarray_sub_item.put(list_obj.getO_sub_item());
                    }
                    jarray_strCombo.put(list_obj.getO_combo_code());
                } else {

                    jarray_code.put("");
                    jarray_StrQty.put("");
                    jarray_ItemRemark.put("");
                    jarray_strAddon.put("");
                    jarray_strHappy.put("");
                    jarray_strModi.put(String.valueOf((char) 1));
                    jarray_strCombo.put("");
                    jarry_disc.put("");
                    jarray_sub_item.put("");
                    jarray_cover.put("");
                    jarray_meal.put("");
                    jarray_surcharge.put("");
                    jarray_price.put("");

                }

            }

            jsonParam.put(StaticConstants.JSON_TAG_GOP_POS, detail.getPosItem().posCode);
            jsonParam.put(JSON_TAG_GOP_TABLE, detail.getTableItem().getCode());
            jsonParam.put(JSON_TAG_GOP_STEWARD, steward);
            jsonParam.put(JSON_TAG_GOP_USERID, userInfo.getUSER_ID());
            jsonParam.put(JSON_TAG_GOP_GUEST_CODE, guestID);
            jsonParam.put(JSON_TAG_GOP_GUEST_NAME, guestName);
            jsonParam.put(JSON_TAG_GOP_ITEM, jarray_code);
            jsonParam.put(JSON_TAG_GOP_QTY, jarray_StrQty);
            jsonParam.put(JSON_TAG_GOP_PRICE, jarray_price);
            jsonParam.put(JSON_TAG_GOP_REMARK, jarray_ItemRemark);
            jsonParam.put(JSON_TAG_GOP_TOEKN, token);
            jsonParam.put(JSON_TAG_GOP_COVER, cover);
            jsonParam.put(JSON_TAG_GOP_ORDER_REMARK, orderRemark);
            jsonParam.put(JSON_TAG_GOP_ORDER_TYPE, ordertype);
            jsonParam.put(JSON_TAG_SERVER_API_KEY, "");
            jsonParam.put(JSON_TAG_DEVICE_ID, "");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /***********
         * string POS, string Tbl, string Stw, string Userid, string GstCode,
         * string GstName, List<string> Item, List<string> Qty, List<string>
         * Remark, string token, string cvr, string odrem, string odtype
         *******************/

        return jsonParam.toString();
    }



    public static String createGuestOrderNew(Context context, String guestID, String guestName,
                                             String cover, String steward, ArrayList<OrderItem> orderItemsList,
                                             String table, String orderRemark) {

        UserInfo userInfo = POSApplication.getSingleton().getmDataModel().getUserInfo();
        String pos = userInfo.getPosItem().posCode;
        String token = PrefHelper.getStoredString(context,
                PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);

        JSONObject jsonParam = null;
        JSONArray jarray_code , jarray_StrQty, jarray_ItemRemark, jarray_strAddon,
                jarray_strModi, jarray_strCombo, jarray_strHappy, jarry_disc, jarray_sub_item,
                jarray_meal, jarray_cover, jarray_price, jarray_surcharge;

        try {
            jsonParam = new JSONObject();

            jarray_code = new JSONArray();
            jarray_StrQty = new JSONArray();
            jarray_ItemRemark = new JSONArray();
            jarray_strAddon = new JSONArray();
            jarray_strModi = new JSONArray();
            jarray_strCombo = new JSONArray();
            jarray_strHappy = new JSONArray();
            jarry_disc = new JSONArray();
            jarray_sub_item = new JSONArray();
            jarray_cover = new JSONArray();
            jarray_meal = new JSONArray();
            jarray_surcharge = new JSONArray();
            jarray_price = new JSONArray();

            OrderItem list_obj;
            for (int i = 0; i < orderItemsList.size(); i++) {

                list_obj = orderItemsList.get(i);

                if (!list_obj.getO_code().equals("")) {
                    jarray_code.put(list_obj.getO_code());
                    jarray_StrQty.put(list_obj.getO_quantity());
                    jarray_ItemRemark.put(list_obj.getO_itm_rmrk());
                    jarray_strAddon.put(list_obj.getO_addon_code());
                    jarray_strHappy.put(list_obj.getO_happy_hour());
                    jarray_cover.put(list_obj.getO_cover_item());
                    jarray_meal.put(list_obj.getO_meal_cors_item());

                    jarray_surcharge.put(list_obj.getO_surcharge());
                    jarray_price.put(list_obj.getO_price());

                    if (!list_obj.getO_sub_item().equals("")) {

                        jarray_strModi.put("");
                        jarray_sub_item.put(list_obj.getO_sub_item());

                    } else {

                        if (list_obj.getO_name().contains("-")
                                || !(list_obj.getO_mod().isEmpty())) {

                            jarray_strModi.put(list_obj.getO_name());
                        } else {
                            jarray_strModi.put("");
                        }

                        jarray_sub_item.put(list_obj.getO_sub_item());
                    }
                    jarray_strCombo.put(list_obj.getO_combo_code());
                } else {

                    jarray_code.put("");
                    jarray_StrQty.put("");
                    jarray_ItemRemark.put("");
                    jarray_strAddon.put("");
                    jarray_strHappy.put("");
                    jarray_strModi.put(String.valueOf((char) 1));
                    jarray_strCombo.put("");
                    jarry_disc.put("");
                    jarray_sub_item.put("");
                    jarray_cover.put("");
                    jarray_meal.put("");
                    jarray_surcharge.put("");
                    jarray_price.put("");

                }

            }

            jsonParam.put(StaticConstants.JSON_TAG_GOP_POS, pos);
            jsonParam.put(JSON_TAG_GOP_TABLE, table);
            jsonParam.put(JSON_TAG_GOP_STEWARD, steward);
            jsonParam.put(JSON_TAG_GOP_USERID, userInfo.getUSER_ID());
            jsonParam.put(JSON_TAG_GOP_GUEST_CODE, guestID);
            jsonParam.put(JSON_TAG_GOP_GUEST_NAME, guestName);
            jsonParam.put(JSON_TAG_GOP_ITEM, jarray_code);
            jsonParam.put(JSON_TAG_GOP_QTY, jarray_StrQty);
            jsonParam.put(JSON_TAG_GOP_PRICE, jarray_price);
            jsonParam.put(JSON_TAG_GOP_REMARK, jarray_ItemRemark);
            jsonParam.put(JSON_TAG_GOP_TOEKN, token);
            jsonParam.put(JSON_TAG_GOP_COVER, cover);
            jsonParam.put(JSON_TAG_GOP_ORDER_REMARK, orderRemark);
            jsonParam.put(JSON_TAG_GOP_ORDER_TYPE, "");
            jsonParam.put(JSON_TAG_SERVER_API_KEY, "");
            jsonParam.put(JSON_TAG_DEVICE_ID, "");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /***********
         * string POS, string Tbl, string Stw, string Userid, string GstCode,
         * string GstName, List<string> Item, List<string> Qty, List<string>
         * Remark, string token, string cvr, string odrem, string odtype
         *******************/

        return jsonParam.toString();
    }


    public static String createParamToFetchOrder(Context context, String status) {

        String pos = PrefHelper.getStoredString(context,
                PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);

        if (!TextUtils.isEmpty(pos)) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(JSON_TAG_GUEST_POS_CODE, pos);
                jsonObject.put(JSON_TAG_GUEST_ORDER_STATUS, status);

                return jsonObject.toString();

            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    public static String createParamToFetchOrderDetail(Context context,
                                                       GuestOrderItem item) {

        JSONObject jsonObject = new JSONObject();
        try {
            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);

            jsonObject.put(JSON_TAG_GUEST_POS_CODE, pos);
            jsonObject.put(JSON_TAG_GUEST_ORDER_ID, item.ORDER_NUM);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String createTOAcceptOrder(Context context,
                                             GstOrDetailAdp<GusetOrderDetail> detailAdp,
                                             String status) {
        // string Tbl, string Stw, List<string> ItemCode, List<string> StrQty,
        // string pos, string token, string cvr, string odrem, string odtype,
        // List<string> ItemRemark, string GstName, string Userid, string
        // OrderNo

        UserInfo userInfo = POSApplication.getSingleton().getmDataModel()
                .getUserInfo();
        GuestOrderItem detail = detailAdp.getItem();
        String token = PrefHelper.getStoredString(context,
                PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);
        String steward = PrefHelper.getStoredString(context,
                PrefHelper.PREF_FILE_NAME, PrefHelper.USER_ID);

        JSONObject jsonParam = null;
        JSONArray jarray_code, jarray_StrQty, jarray_ItemRemark, jarray_strAddon, jarray_strModi,
                jarray_strCombo, jarray_strHappy, jarry_disc, jarray_sub_item, jarray_meal,
                jarray_cover, jarray_price, jarray_surcharge;

        try {
            jsonParam = new JSONObject();

            jarray_code = new JSONArray();
            jarray_StrQty = new JSONArray();
            jarray_ItemRemark = new JSONArray();
            jarray_strAddon = new JSONArray();
            jarray_strModi = new JSONArray();
            jarray_strCombo = new JSONArray();
            jarray_strHappy = new JSONArray();
            jarry_disc = new JSONArray();
            jarray_sub_item = new JSONArray();
            jarray_cover = new JSONArray();
            jarray_meal = new JSONArray();
            jarray_surcharge = new JSONArray();
            jarray_price = new JSONArray();

            GusetOrderDetail list_obj;
            for (int i = 0; i < detailAdp.getCount(); i++) {

                list_obj = (GusetOrderDetail) detailAdp.getItem(i);

                if (!list_obj.ItemCode.equals("")) {
                    jarray_code.put(list_obj.ItemCode);
                    jarray_StrQty.put(list_obj.ItemQty);
                    jarray_ItemRemark.put("");
                    jarray_strAddon.put("");
                    jarray_strHappy.put("");
                    jarry_disc.put("");
                    jarray_cover.put("");
                    jarray_meal.put("");

                    jarray_surcharge.put("");
                    jarray_price.put("");

					/*
					 * if (!list_obj.getO_sub_item().equals("")) {
					 *
					 * jarray_strModi.put("");
					 * jarray_sub_item.put(list_obj.getO_sub_item());
					 *
					 * } else {
					 *
					 * if (list_obj.getO_name().contains("-") ||
					 * !(list_obj.getO_mod().isEmpty())) {
					 *
					 * jarray_strModi.put(list_obj.getO_name()); } else {
					 * jarray_strModi.put(""); }
					 *
					 * jarray_sub_item.put(list_obj.getO_sub_item()); }
					 * jarray_strCombo.put(list_obj.getO_combo_code());
					 */
                } else {

                    jarray_code.put("");
                    jarray_StrQty.put("");
                    jarray_ItemRemark.put("");
                    jarray_strAddon.put("");
                    jarray_strHappy.put("");
                    jarray_strModi.put(String.valueOf((char) 1));
                    jarray_strCombo.put("");
                    jarry_disc.put("");
                    jarray_sub_item.put("");
                    jarray_cover.put("");
                    jarray_meal.put("");
                    jarray_surcharge.put("");
                    jarray_price.put("");

                }

            }
            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);

            jsonParam.put(StaticConstants.JSON_TAG_GST_POS, pos);
            jsonParam.put(JSON_TAG_GST_TABLE, detail.TABLE);
            jsonParam.put(JSON_TAG_GST_STW, steward);
            jsonParam.put(JSON_TAG_GST_USER_ID, userInfo.getUSER_ID());
            jsonParam.put(JSON_TAG_GST_GST_NAME, "");
            jsonParam.put(JSON_TAG_GST_ITEMCODE, jarray_code);
            jsonParam.put(JSON_TAG_GST_QTY, jarray_StrQty);
            jsonParam.put(JSON_TAG_GST_ITEM_REMARK, jarray_ItemRemark);
            jsonParam.put(JSON_TAG_GST_TOKEN, token);
            jsonParam.put(JSON_TAG_GST_COVER, "");
            jsonParam.put(JSON_TAG_GST_ODREM, "");
            jsonParam.put(JSON_TAG_GST_ODTYPE, "");
            jsonParam.put(JSON_TAG_GST_ORDERNUM, detail.ORDER_NUM);
            jsonParam.put(JSON_TAG_GUEST_ORDER_STATUS, status);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return jsonParam.toString();

    }

    public static String onReject(Context context, String ORDER_NUM, String tbl, String status) {

        JSONObject jsonObject = new JSONObject();
        try {
            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);

            jsonObject.put(JSON_TAG_GUEST_POS_CODE, pos);
            jsonObject.put(JSON_TAG_GST_TABLE, tbl);
            jsonObject.put(JSON_TAG_GUEST_ORDER_ID, ORDER_NUM);
            jsonObject.put(JSON_TAG_GUEST_ORDER_STATUS, status);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String createTableOrderJson(Context context, String table) {
        String parameter = "";
        JSONObject jsonObject = new JSONObject();
        try {

            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);

            jsonObject.put("pos", pos);
            jsonObject.put("tbl", table);
            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        } finally {

        }
        return parameter;
    }

    public static String createTableItemsJson(Context context,
                                              String table) {
        String parameter = "";
        JSONObject jsonObject = new JSONObject();
        try {

            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);

            jsonObject.put("pos", pos);
            jsonObject.put("tbl", table);
            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";
        }

        return parameter;
    }

    public static String createBillGenerateJson(Context context, String table, String dis_code_bill,
                                                String dis_per_bill) {
        String parameter = "";
        JSONObject jsonObject = new JSONObject();
        try {

            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);
            String token = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);

            jsonObject.put("pos", pos);
            jsonObject.put("Tbl", table);
            jsonObject.put("token", token);
            jsonObject.put("Disc_code", Util.checkStringIsEmpty(dis_code_bill));
            jsonObject.put("Disc_per", Util.checkStringIsEmpty(dis_per_bill));
            jsonObject.put("OrderNo1", Util.checkStringIsEmpty(UserInfo.Orderno));

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        } finally {

        }
        return parameter;
    }

    public static String createBillPrintReciptJson(Context context) {
        String parameter = "";
        JSONObject jsonObject = new JSONObject();
        try {

            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);
            String token = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);

            jsonObject.put("pos", pos);
            jsonObject.put("billno", UserInfo.Bill_no);

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        }
        return parameter;
    }

    public static String createBillSaveJson(Context context, String table,
                                            String bill_rem, String billno) {
        String parameter = "";
        JSONObject jsonObject = new JSONObject();

        try {

            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);
            String token = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);

            jsonObject.put("pos", pos);
            jsonObject.put("Tbl", table);
            jsonObject.put("token", token);
            jsonObject.put("ip", Util.getWifiMACAddress(context));
            jsonObject.put("OrderNo", Util.checkStringIsEmpty(UserInfo.Order_no));
            jsonObject.put("rem", Util.checkStringIsEmpty(bill_rem));
            jsonObject.put("billno", billno);

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        } finally {

        }
        return parameter;
    }

    public static boolean BillSaveTaskResponse(Context context, String response) {

        Logger.i("parseTakeOrderResponse", response);

        try {

            if (response.equals("Please try Again!!"))
                return false;

            else {

                // Util.show_popup_success(context, response,result, true);
                UserInfo.Bill_no = response;
                Toast.makeText(context, UserInfo.Bill_no, Toast.LENGTH_LONG).show();
                return true;
            }

        } catch (Exception ex) {ex.printStackTrace();}

        return false;
    }


    public static String createSendTableAwayJson(Context context, String table) throws JSONException {

        String pos = POSApplication.getSingleton().getmDataModel().getUserInfo().posItem.posCode;
        String parameter = "";
        JSONObject jsonParam ;

        try {

            jsonParam = new JSONObject();
            jsonParam.put("tbl", table);
            jsonParam.put("POS", pos);
            parameter = jsonParam.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        }

        return parameter;
    }


    public static String createRunningOrderJson(Context context, String table) throws JSONException {


        String parameter = "";
        JSONObject jsonObject = new JSONObject();
        try {

            String pos = POSApplication.getSingleton()
                    .getmDataModel().getUserInfo().getPosItem().posCode;

            jsonObject.put("pos", pos);
            jsonObject.put("tbl", table);
            parameter = jsonObject.toString();


        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";
        }
        return parameter;
    }

    public static boolean parseTableAwayRespnse(Context context, String response) {

        JSONObject jsonObject ;

        try {
            jsonObject = new JSONObject(response);
            Logger.i("parseTakeOrderResponse", jsonObject.toString());

            if (jsonObject.getString("ECABS_TableAwayPrintResult").equals("Please try Again!!")) {
                return false;
            }

            if (jsonObject.getString("ECABS_TableAwayPrintResult").equals("true"))
                Toast.makeText(context, "successfully done", Toast.LENGTH_SHORT).show();
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();

        }

        return false;
    }

    public static String createTableStatusJson(Context context) {

        JSONObject jsonObject = new JSONObject();
        try {

            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);

            jsonObject.put(JSON_TAG_DATA_SYNC_POS, pos);
            jsonObject.put(KEY_TABLE_STATUS_ORDER, KEY_TAG_ORDER_TYPE);
            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }


    }

    public static boolean parseTabalStatusresonse(Context context, String response) {

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(response);
            Logger.i("ECABS_ExplorerNEWResult", jsonObject.toString());
            if (jsonObject.getString("ECABS_ExplorerNEWResult")
                    .equals("Please try Again!!")) {
                jsonObject.getString("ECABS_ExplorerNEWResult");
                return false;
            } else {
                String result = jsonObject
                        .getString("ECABS_ExplorerNEWResult");

                Toast.makeText(context, result, Toast.LENGTH_LONG).show();

                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {

        }

        return false;
    }

    public static String createCrewInJSON(String del_id, String date) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("code", del_id);
            jsonObject.put("dates", date);

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String createCrewStatusJSON(String del_id) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("del)", del_id);
            jsonObject.put("dates", UserInfo.getRunningDate());

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String createHomeDelLogJSON(Context context, String bill, String del_id,
                                              String chng_amt, String ret_amt, String flag) {

        String pos = PrefHelper.getStoredString(context,
                PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);

        String date = UserInfo.getRunningDate();

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("pos", pos);
            jsonObject.put("billno", bill);
            jsonObject.put("del", del_id);
            jsonObject.put("camt", chng_amt);
            jsonObject.put("ramt", ret_amt);
            jsonObject.put("flag", flag);
            jsonObject.put("date", date);

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String CreateSavePaidAmtJson(Context context, String billno, String cash, String credit_code,
                                               String credit_type, String credit,
                                               String tip, String user_id) throws JSONException {


        String parameter = "";
        JSONObject jsonObject = new JSONObject();
        try {

            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);

            JSONArray jsonArray = new JSONArray();
            if (!cash.equals("0"))
                jsonArray.put("-" + "C" + "-" + cash + "-" + "n");
            if (!credit.equals("0"))
                jsonArray.put(credit_code + "-" + credit_type + "-" + credit + "-" + "n");
            if (!tip.equals("0"))
                jsonArray.put("-" + "-" + tip + "-" + "y");

            jsonObject.put("pos", pos);
            jsonObject.put("billno", billno);
            jsonObject.put("uid", user_id);
            jsonObject.put("sett", jsonArray);

            Log.i("input", jsonObject.toString());

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        } finally {

        }
        return parameter;

    }

    public static String createTableJoinJson(Context context, CustomTextview textViewMain, CustomTextview textViewToJoin) {
        String parameter = "";
        JSONObject jsonObject = new JSONObject();
        try {

            String pos = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posCode;

            jsonObject.put("pos", pos);
            jsonObject.put("MainTbl", textViewMain.getText().toString());
            jsonObject.put("ToTbl", textViewToJoin.getText().toString());
            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        }
        return parameter;
    }

    public static String createGuestProfile(Context context, EditText editTextName, EditText editTextMobileNo,
                                            EditText editTextEmailComment, CustomTextview textViewDateofBrith,
                                            String Sex_String, RadioButton r_b_male, RadioButton r_female,
                                            CustomTextview textViewAnniversary) {
        String parameter = "";


        JSONObject jsonObject = new JSONObject();
        try {

            if (r_b_male.isChecked()) {

                Log.i("male", "yo");
                Sex_String = "M";
            }
            if (r_female.isChecked()) {
                Sex_String = "F";

            }

            jsonObject.put("guest_id", UserInfo.guest_id);
            jsonObject.put("Name", editTextName.getText().toString());
            jsonObject.put("Phone", editTextMobileNo.getText().toString());
            jsonObject.put("Email", editTextEmailComment.getText().toString());
            jsonObject.put("DOB", textViewDateofBrith.getText().toString());
            jsonObject.put("Sex", Sex_String);
            jsonObject.put("Ansv", textViewAnniversary.getText().toString());

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        }
        return parameter;
    }

    public static String createGuestCommentJson(Context context, EditText editTextComment) {
        String parameter = "";


        JSONObject jsonObject = new JSONObject();
        try {

            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);


            jsonObject.put("gst", UserInfo.guest_id);
            jsonObject.put("Comm", editTextComment.getText().toString());
            jsonObject.put("pos", pos);
            jsonObject.put("Billno", UserInfo.editText);
            jsonObject.put("date", UserInfo.getRunningDate());

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        } finally {

        }
        return parameter;
    }

    public static boolean parseTabalJoinResonse(Context context, String response) {

        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(response);
            Logger.i("ECABS_TableJoinResult", jsonObject.toString());

            if (jsonObject.getString("ECABS_TableJoinResult").equals("Please try Again!!")) {
                jsonObject.getString("ECABS_TableJoinResult");
                return false;
            } else {
                String result = jsonObject.getString("ECABS_TableJoinResult");

                if (result != null) {
                    if (result.equals("true"))
                        Toast.makeText(context, "Table Joined Successfully !!", Toast.LENGTH_SHORT).show();
                    else if (result.equals("false"))
                        Toast.makeText(context, "Null value from web server !!", Toast.LENGTH_LONG).show();
                }
                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public static boolean parseTableTransferJson(Context context, String response) {

        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(response);
            Logger.i("ECABS_TableTransfer", jsonObject.toString());

            if (jsonObject.getString("ECABS_TableTransferResult").equals("Please try Again!!")) {
                jsonObject.getString("ECABS_TableTransferResult");
                return false;
            } else {
                String result = jsonObject.getString("ECABS_TableTransferResult");
                if (result != null) {

                    if (result.equals("true"))
                        Toast.makeText(context, "Table Transfered Successfully", Toast.LENGTH_SHORT).show();

                    else if (result.equals("false")) {
                        Toast.makeText(context, "Null value from web server !!", Toast.LENGTH_LONG).show();
                    }
                }

                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }

        return false;
    }

    public static String createTableOrderFatchJson(Context context, TextView textViewMainTable) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("tbl", textViewMainTable.getText().toString());

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return "";
    }

    public static String createTableTransferJSON(Context context, TextView textViewMainTable, TextView textViewTransferTable, CustomTextview textViewOrderNoShow) {

        JSONObject jsonObject = new JSONObject();
        try {

            String token = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);

            String pos = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posCode;

            if (textViewOrderNoShow.getText().length() != 0) {
                jsonObject.put("odr", textViewOrderNoShow.getText());
            } else {
                jsonObject.put("odr", "");
            }
            jsonObject.put("pos", pos);
            jsonObject.put("oldTbl", textViewMainTable.getText());
            jsonObject.put("Newtbl", textViewTransferTable.getText());


            return jsonObject.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }


    public static String createUpcomingEventJson(Context context, String date, String msgType, String message){

        try {
            JSONObject jsonObject = new JSONObject();
            date = UserInfo.changeDateToServerFormat(date);
            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);

            jsonObject.put(JSON_TAG_DATA_SYNC_POS, pos);
            jsonObject.put(JSON_TAG_EVENT_DATE,date);
            jsonObject.put(JSON_TAG_MESSAGE_TYPE,msgType);
            jsonObject.put(JSON_TAG_EVENT_MESSAGE,message);
            return jsonObject.toString();

        }catch (JSONException e){
            e.printStackTrace();
            return "";
        }
    }

    public static String createTableJSON(Context context) {

        JSONObject jsonObject = new JSONObject();
        try {

            String token = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);

            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);

            jsonObject.put(JSON_TAG_TABLE_TAG, "");
            jsonObject.put(JSON_TAG_DATA_SYNC_POS, pos);
            jsonObject.put(JSON_TAG_DATA_SYNC_TOKEN, token);
            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public static String createOrderSplitJson(Context context, String selectedTable) {
        String parameter = "";
        JSONObject jsonObject = new JSONObject();
        try {

            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);
            String token = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);

            jsonObject.put("pos", pos);
            jsonObject.put("tbl", selectedTable);
            jsonObject.put("token", token);

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        } finally {

        }
        return parameter;
    }

    public static String createGetOrderItemSplitJson(Context context, String selectedTable, String SelectOrderNo) {
        String parameter = "";
        JSONObject jsonObject = new JSONObject();
        try {

            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);
            String token = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);


            jsonObject.put("OdrNo", SelectOrderNo);
            jsonObject.put("tbl", selectedTable);
            jsonObject.put("pos", pos);
            jsonObject.put("token", token);

            Log.i("input", jsonObject.toString());

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        } finally {

        }
        return parameter;
    }

    public static String createSendOrderSplitJsonNew(Context context,
                                                     OrderSplitAdapter orderSplitAdapter,
                                                     OrderSplitAdapter1 orderSplitAdapter1,
                                                     String oldTable, String newTable,
                                                     String steward) {

        String pos = PrefHelper.getStoredString(context,
                PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();

        try {

            for (int i = 0; i < orderSplitAdapter.getCount(); i++) {

                OrderItem list_obj = orderSplitAdapter.getItem(i);

                jsonArray1.put(
                        "itemcode" + ":" + list_obj.getO_code() + ";" +
                                "qty" + ":" + list_obj.getO_quantity() + ";" +
                                "price" + ":" + list_obj.getO_price() + ";" +
                                "ordno" + ":" + list_obj.getOrderNo() + ";" +
                                "ItemRemark" + ":" + list_obj.getO_itm_rmrk() + ";" +
                                "strAddon" + ":" + list_obj.getO_addon_code() + ";" +
                                "strModi" + ":" + list_obj.getO_mod() + ";" +
                                "strCombo" + ":" + list_obj.getO_combo_code() + ";" +
                                "strdisc" + ":" + list_obj.o_disc + ";");

            }

            for (int i = 0; i < orderSplitAdapter1.getCount(); i++) {

                OrderItem list_obj = orderSplitAdapter1.getItem(i);

                jsonArray2.put(
                        "itemcode" + ":" + list_obj.getO_code() + ";" +
                                "qty" + ":" + list_obj.getO_quantity() + ";" +
                                "price" + ":" + list_obj.getO_price() + ";" +
                                "ordno" + ":" + "" + ";" +
                                "ItemRemark" + ":" + list_obj.getO_itm_rmrk() + ";" +
                                "strAddon" + ":" + list_obj.getO_addon_code() + ";" +
                                "strModi" + ":" + list_obj.getO_mod() + ";" +
                                "strCombo" + ":" + list_obj.getO_combo_code() + ";" +
                                "strdisc" + ":" + list_obj.o_disc + ";");
            }

            jsonObject.put("pos", pos);
            jsonObject.put("billno", "");
            jsonObject.put("oldtbl", oldTable);
            jsonObject.put("newtbl", newTable);
            jsonObject.put("Stw", "#" + steward);
            jsonObject.put("GstName", "");
            jsonObject.put("cvr", "");
            jsonObject.put("odrem", "");
            jsonObject.put("odtype", "");
            jsonObject.put("cvr", "0");
            jsonObject.put("mod", jsonArray1);
            jsonObject.put("transfer", jsonArray2);

            jsonObject.put(JSON_TAG_POS_TOKEN, PrefHelper.getStoredString(
                    context, PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN));
            jsonObject.put(JSON_TAG_WIFI, Util.getWifiMACAddress(context));

            return jsonObject.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String createGstSearchParameter(String mobile) {
        String parameter = "";

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("Phone", mobile);

            Logger.i("input", jsonParam.toString());
            parameter = jsonParam.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";
        }
        return parameter;

    }

    public static String createCompSearchParameter(String mobile) {
        String parameter = "";

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("Code", mobile);

            Logger.i("input", jsonParam.toString());
            parameter = jsonParam.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";
        }
        return parameter;

    }

    public static String CreateCompanyDisountJson(Context context, CustomEditView editTextCompCode,
                                                  CustomEditView editTextCompName, CustomEditView editTextAddress1,
                                                  CustomEditView editTextAddress2, CustomEditView editTextCompCity,
                                                  CustomEditView editTextCompPin, CustomEditView edittTextPhone,
                                                  TextView textViewCompanyInfoDate, CustomTextview testViewLoyaltyEffDate,
                                                  Spinner spinnerStatusComp, String validdate, String discount) throws JSONException {


        String parameter = "";
        JSONObject jsonObject = new JSONObject();
        try {

            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);
            String token = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);


            jsonObject.put("code", editTextCompCode.getText().toString());
            jsonObject.put("name", editTextCompName.getText().toString());
            jsonObject.put("address1", editTextAddress1.getText().toString());
            jsonObject.put("address2", editTextAddress2.getText().toString());
            jsonObject.put("city", editTextCompCity.getText().toString());
            jsonObject.put("pin", editTextCompPin.getText().toString());
            jsonObject.put("phone", edittTextPhone.getText().toString());
            jsonObject.put("companyInfodate", textViewCompanyInfoDate.getText().toString());
            jsonObject.put("loyaltydate", testViewLoyaltyEffDate.getText().toString());
            jsonObject.put("status", spinnerStatusComp.getSelectedItem().toString());
            jsonObject.put("validdate", validdate);
            jsonObject.put("discount", discount);
            jsonObject.put("token", token);

            Log.i("input", jsonObject.toString());

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        } finally {

        }
        return parameter;

    }

    public static String createSendSyncJSON(Context context, TakeOrderAdapter takeOrderAdapter,
                                            String ordertype, String orderRemark, String guest, String steward,
                                            String cover, DiscountLayout discountLayout, String companyCode) throws JSONException {

        OrderDetail detail = takeOrderAdapter.getOrderDetail();
        String parameter = "";

        String token = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);

        JSONObject jsonParam;
        JSONArray jarray_code, jarray_StrQty, jarray_ItemRemark, jarray_strAddon, jarray_strModi, jarray_strCombo,
                jarray_strHappy, jarry_disc, jarray_sub_item, jarray_meal, jarray_cover, jarray_price, jarray_surcharge;

        try {
            jarray_code = new JSONArray();
            jarray_StrQty = new JSONArray();
            jarray_ItemRemark = new JSONArray();
            jarray_strAddon = new JSONArray();
            jarray_strModi = new JSONArray();
            jarray_strCombo = new JSONArray();
            jarray_strHappy = new JSONArray();
            jarry_disc = new JSONArray();
            jarray_sub_item = new JSONArray();
            jarray_cover = new JSONArray();
            jarray_meal = new JSONArray();
            jarray_surcharge = new JSONArray();
            jarray_price = new JSONArray();

            OrderItem list_obj;
            for (int i = 0; i < takeOrderAdapter.getCount(); i++) {

                list_obj = (OrderItem) takeOrderAdapter.getItem(i);

                if (!list_obj.getO_code().isEmpty()) {

                    jarray_code.put(list_obj.getO_code());
                    jarray_StrQty.put(list_obj.getO_happy_hour().equals("Y") ?
                            (int) list_obj.getO_quantity() : list_obj.getO_quantity());
                    jarray_ItemRemark.put(list_obj.getO_itm_rmrk());
                    jarray_strAddon.put(list_obj.getO_addon_code());
                    jarray_strHappy.put(list_obj.getO_happy_hour());
                    jarry_disc.put(discountLayout.getDiscPer(i));
                    jarray_cover.put(list_obj.getO_cover_item());
                    jarray_meal.put(list_obj.getO_meal_cors_item());
                    jarray_surcharge.put(list_obj.getO_surcharge());
                    jarray_price.put(list_obj.getO_price());

                    if (!list_obj.getO_sub_item().equals("")) {

                        jarray_strModi.put("");
                        jarray_sub_item.put(list_obj.getO_sub_item());

                    } else {

                        if (list_obj.getO_name().contains("-")
                                || !(list_obj.getO_mod().isEmpty())) {

                            jarray_strModi.put(list_obj.getO_name());
                        } else {
                            jarray_strModi.put("");
                        }

                        jarray_sub_item.put(list_obj.getO_sub_item());
                    }
                    jarray_strCombo.put(list_obj.getO_combo_code());
                } else {

                    jarray_code.put("");
                    jarray_StrQty.put("");
                    jarray_ItemRemark.put("");
                    jarray_strAddon.put("");
                    jarray_strHappy.put("");

                    jarray_strModi.put(String.valueOf((char) 1));
                    jarray_strCombo.put("");
                    jarry_disc.put("0");
                    jarray_sub_item.put("");

                    jarray_cover.put("");
                    jarray_meal.put("");
                    jarray_surcharge.put("");
                    jarray_price.put("");

                }

            }

            jsonParam = new JSONObject();
            POSItem posItem = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem();

            if (posItem.posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_RS)) {

                RoomItem roomItem = new RoomItem();
                jsonParam.put("Tbl", detail.getRoomItem().getRoom_code());
                jsonParam.put("Stw", "#" + steward);
                jsonParam.put("ItemCode", jarray_code);
                jsonParam.put("StrQty", jarray_StrQty);
                jsonParam.put("pos", detail.getPosItem().posCode);
                jsonParam.put("token", token);
                jsonParam.put("cvr", cover);
                jsonParam.put("odrem", orderRemark);
                jsonParam.put("odtype", ordertype);
                jsonParam.put("ItemRemark", jarray_ItemRemark);
                jsonParam.put("strAddon", jarray_strAddon);
                jsonParam.put("GstName", detail.getRoomItem().getGuestName());
                jsonParam.put("strModi", jarray_strModi);
                jsonParam.put("strCombo", jarray_strCombo);
                jsonParam.put("strHappy", jarray_strHappy);
                jsonParam.put("strdisc", jarry_disc);
                jsonParam.put("sub_item", jarray_sub_item);
                jsonParam.put("cover_item", jarray_cover);
                jsonParam.put("meal_item", jarray_meal);
                jsonParam.put("surchr", jarray_surcharge);
                jsonParam.put("price", jarray_price);
                jsonParam.put("Comp_D", companyCode);

            } else if ((posItem.posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_R))){

                jsonParam.put("Tbl", detail.getTableItem().getCode());
                jsonParam.put("Stw", "#" + steward);
                jsonParam.put("ItemCode", jarray_code);
                jsonParam.put("StrQty", jarray_StrQty);
                jsonParam.put("pos", detail.getPosItem().posCode);
                jsonParam.put("token", token);
                jsonParam.put("cvr", cover);
                jsonParam.put("odrem", orderRemark);
                jsonParam.put("odtype", ordertype);
                jsonParam.put("ItemRemark", jarray_ItemRemark);
                jsonParam.put("strAddon", jarray_strAddon);
                jsonParam.put("GstName", guest);
                jsonParam.put("strModi", jarray_strModi);
                jsonParam.put("strCombo", jarray_strCombo);
                jsonParam.put("strHappy", jarray_strHappy);
                jsonParam.put("strdisc", jarry_disc);
                jsonParam.put("sub_item", jarray_sub_item);
                jsonParam.put("cover_item", jarray_cover);
                jsonParam.put("meal_item", jarray_meal);
                jsonParam.put("surchr", jarray_surcharge);
                jsonParam.put("price", jarray_price);
                jsonParam.put("Comp_D", companyCode);
            }

            parameter = jsonParam.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return parameter;
    }

    public static String createSendSyncJSONNew(Context context, ArrayList<OrderItem> orderItemsList,
                                               String table, String ordertype, String orderRemark,
                                               String guest, String steward, String cover) throws JSONException {

        UserInfo userInfo = POSApplication.getSingleton().getmDataModel().getUserInfo();
        String pos = userInfo.getPosItem().posCode;
        String parameter = "";

        String token = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);

        JSONObject jsonParam;
        JSONArray jarray_code, jarray_StrQty, jarray_ItemRemark, jarray_strAddon, jarray_strModi, jarray_strCombo,
                jarray_strHappy, jarry_disc, jarray_sub_item, jarray_meal, jarray_cover, jarray_price, jarray_surcharge;

        try {
            jarray_code = new JSONArray();
            jarray_StrQty = new JSONArray();
            jarray_ItemRemark = new JSONArray();
            jarray_strAddon = new JSONArray();
            jarray_strModi = new JSONArray();
            jarray_strCombo = new JSONArray();
            jarray_strHappy = new JSONArray();
            jarry_disc = new JSONArray();
            jarray_sub_item = new JSONArray();
            jarray_cover = new JSONArray();
            jarray_meal = new JSONArray();
            jarray_surcharge = new JSONArray();
            jarray_price = new JSONArray();

            OrderItem list_obj;
            for (int i = 0; i < orderItemsList.size(); i++) {

                list_obj = orderItemsList.get(i);

                if (!list_obj.getO_code().isEmpty()) {

                    jarray_code.put(list_obj.getO_code());
                    jarray_StrQty.put(list_obj.getO_happy_hour().equals("Y") ?
                            (int) list_obj.getO_quantity() : list_obj.getO_quantity());
                    jarray_ItemRemark.put(list_obj.getO_itm_rmrk());
                    jarray_strAddon.put(list_obj.getO_addon_code());
                    jarray_strHappy.put(list_obj.getO_happy_hour());
                    jarry_disc.put(0);
                    jarray_cover.put(list_obj.getO_cover_item());
                    jarray_meal.put(list_obj.getO_meal_cors_item());
                    jarray_surcharge.put(list_obj.getO_surcharge());
                    jarray_price.put(list_obj.getO_price());

                    if (!list_obj.getO_sub_item().equals("")) {

                        jarray_strModi.put("");
                        jarray_sub_item.put(list_obj.getO_sub_item());

                    } else {

                        if (list_obj.getO_name().contains("-")
                                || !(list_obj.getO_mod().isEmpty())) {

                            jarray_strModi.put(list_obj.getO_name());
                        } else {
                            jarray_strModi.put("");
                        }

                        jarray_sub_item.put(list_obj.getO_sub_item());
                    }
                    jarray_strCombo.put(list_obj.getO_combo_code());
                } else {

                    jarray_code.put("");
                    jarray_StrQty.put("");
                    jarray_ItemRemark.put("");
                    jarray_strAddon.put("");
                    jarray_strHappy.put("");

                    jarray_strModi.put(String.valueOf((char) 1));
                    jarray_strCombo.put("");
                    jarry_disc.put("0");
                    jarray_sub_item.put("");

                    jarray_cover.put("");
                    jarray_meal.put("");
                    jarray_surcharge.put("");
                    jarray_price.put("");

                }

            }

            jsonParam = new JSONObject();
            POSItem posItem = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem();

            if (posItem.posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_RS)) {

                RoomItem roomItem = new RoomItem();
                jsonParam.put("Tbl", table);
                jsonParam.put("Stw", "#" + steward);
                jsonParam.put("ItemCode", jarray_code);
                jsonParam.put("StrQty", jarray_StrQty);
                jsonParam.put("pos", pos);
                jsonParam.put("token", token);
                jsonParam.put("cvr", cover);
                jsonParam.put("odrem", orderRemark);
                jsonParam.put("odtype", ordertype);
                jsonParam.put("ItemRemark", jarray_ItemRemark);
                jsonParam.put("strAddon", jarray_strAddon);
                jsonParam.put("GstName", guest);
                jsonParam.put("strModi", jarray_strModi);
                jsonParam.put("strCombo", jarray_strCombo);
                jsonParam.put("strHappy", jarray_strHappy);
                jsonParam.put("strdisc", jarry_disc);
                jsonParam.put("sub_item", jarray_sub_item);
                jsonParam.put("cover_item", jarray_cover);
                jsonParam.put("meal_item", jarray_meal);
                jsonParam.put("surchr", jarray_surcharge);
                jsonParam.put("price", jarray_price);
                jsonParam.put("Comp_D", "");

            } else if ((posItem.posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_R))){

                jsonParam.put("Tbl",table);
                jsonParam.put("Stw", "#" + steward);
                jsonParam.put("ItemCode", jarray_code);
                jsonParam.put("StrQty", jarray_StrQty);
                jsonParam.put("pos", pos);
                jsonParam.put("token", token);
                jsonParam.put("cvr", cover);
                jsonParam.put("odrem", orderRemark);
                jsonParam.put("odtype", ordertype);
                jsonParam.put("ItemRemark", jarray_ItemRemark);
                jsonParam.put("strAddon", jarray_strAddon);
                jsonParam.put("GstName", guest);
                jsonParam.put("strModi", jarray_strModi);
                jsonParam.put("strCombo", jarray_strCombo);
                jsonParam.put("strHappy", jarray_strHappy);
                jsonParam.put("strdisc", jarry_disc);
                jsonParam.put("sub_item", jarray_sub_item);
                jsonParam.put("cover_item", jarray_cover);
                jsonParam.put("meal_item", jarray_meal);
                jsonParam.put("surchr", jarray_surcharge);
                jsonParam.put("price", jarray_price);
                jsonParam.put("Comp_D", "");
            }

            parameter = jsonParam.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";
        }
        return parameter;
    }


    public static String createHoldOrderJson(Context context, String pos) {
        String parameter = "";
        JSONObject jsonObject = new JSONObject();
        try {

            String token = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);

            jsonObject.put("pos", pos);
            jsonObject.put("token", token);

            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        }
        return parameter;
    }


    public static String createSendHomeDelevryJSON(Context context, TakeOrderAdapter takeOrderAdapter,
                                                   String table, DiscountLayout discountLayout, String companyCode,
                                                   String orderType, String orderRemark, String guest,
                                                   String steward, String cover, String flag) throws JSONException {

        OrderDetail detail = takeOrderAdapter.getOrderDetail();

        String parameter = "";


        String token = PrefHelper.getStoredString(context,
                PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);

        JSONObject jsonParam;
        JSONArray jarray_code, jarray_StrQty, jarray_ItemRemark, jarray_strAddon, jarray_strModi,
                jarray_strCombo, jarray_strHappy, jarry_disc, jarray_sub_item, jarray_meal, jarray_cover;

        try {
            jarray_code = new JSONArray();
            jarray_StrQty = new JSONArray();
            jarray_ItemRemark = new JSONArray();
            jarray_strAddon = new JSONArray();
            jarray_strModi = new JSONArray();
            jarray_strCombo = new JSONArray();
            jarray_strHappy = new JSONArray();
            jarry_disc = new JSONArray();
            jarray_sub_item = new JSONArray();
            jarray_cover = new JSONArray();
            jarray_meal = new JSONArray();
            //  jarray_surcharge = new JSONArray();
            // jarray_price = new JSONArray();

            OrderItem list_obj;
            for (int i = 0; i < takeOrderAdapter.getCount(); i++) {

                list_obj = (OrderItem) takeOrderAdapter.getItem(i);

                if (!list_obj.getO_code().equals("")) {
                    jarray_code.put(list_obj.getO_code());
                    jarray_StrQty.put(list_obj.getO_quantity());
                    jarray_ItemRemark.put(list_obj.getO_itm_rmrk());
                    jarray_strAddon.put(list_obj.getO_addon_code());
                    jarray_strHappy.put(list_obj.getO_happy_hour());
                    jarry_disc.put(discountLayout.getDiscPer(i));
                    jarray_cover.put(list_obj.getO_cover_item());
                    jarray_meal.put(list_obj.getO_meal_cors_item());


                    if (!list_obj.getO_sub_item().equals("")) {

                        jarray_strModi.put("");
                        jarray_sub_item.put(list_obj.getO_sub_item());
                        Log.i("String ", "Tsring 3");
                    } else {

                        if (list_obj.getO_name().contains("-")
                                || !(list_obj.getO_mod().isEmpty())) {

                            jarray_strModi.put(list_obj.getO_name());
                        } else {
                            jarray_strModi.put("");
                        }

                        jarray_sub_item.put(list_obj.getO_sub_item());
                    }
                    jarray_strCombo.put(list_obj.getO_combo_code());

                } else {
                    jarray_code.put("");
                    jarray_StrQty.put("");
                    jarray_ItemRemark.put("");
                    jarray_strAddon.put("");
                    jarray_strHappy.put("");

                    jarray_strModi.put(String.valueOf((char) 1));
                    jarray_strCombo.put("");
                    jarry_disc.put("");
                    jarray_sub_item.put("");

                }
                Log.i("Strinf ", "Tsring 5");
                Log.i("disc", list_obj.getO_disc());

            }


            jsonParam = new JSONObject();

            jsonParam.put("Tbl", table);
            jsonParam.put("Stw", "#" + steward);
            jsonParam.put("ItemCode", jarray_code);
            jsonParam.put("StrQty", jarray_StrQty);
            jsonParam.put("pos", detail.getPosItem().posCode);
            jsonParam.put("token", token);
            jsonParam.put("cvr", cover);
            jsonParam.put("odrem", orderRemark);
            jsonParam.put("odtype", orderType);
            jsonParam.put("ItemRemark", jarray_ItemRemark);
            jsonParam.put("strAddon", jarray_strAddon);
            jsonParam.put("GstName", guest);
            jsonParam.put("strModi", jarray_strModi);
            jsonParam.put("strCombo", jarray_strCombo);
            jsonParam.put("strHappy", jarray_strHappy);
            jsonParam.put("strdisc", jarry_disc);
            jsonParam.put("sub_item", jarray_sub_item);
            jsonParam.put("cover_item", jarray_cover);
            jsonParam.put("meal_item", jarray_meal);
            jsonParam.put("Comp_D", companyCode);
            jsonParam.put("flag", flag);
            //jsonParam.put("Disc_code", Util.checkStringIsEmpty(dis_code));
            // jsonParam.put("Disc_per", Util.checkStringIsEmpty(dis_per));

            parameter = jsonParam.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            parameter = "";

        } finally {

        }
        return parameter;
    }

    public static String createRoomServiceJSON(Context context) {

        JSONObject jsonObject = new JSONObject();
        try {

            String token = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.TOKEN);

            String pos = PrefHelper.getStoredString(context,
                    PrefHelper.PREF_FILE_NAME, PrefHelper.POINT_OF_SALE);

            //    jsonObject.put(JSON_TAG_TABLE_TAG, "");
            jsonObject.put(JSON_TAG_DATA_SYNC_POS, pos);
            jsonObject.put(JSON_TAG_DATA_TOKEN, token);
            return jsonObject.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

}