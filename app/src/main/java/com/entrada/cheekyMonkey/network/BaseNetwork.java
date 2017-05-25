package com.entrada.cheekyMonkey.network;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;

import com.entrada.cheekyMonkey.util.Logger;

public class BaseNetwork {

    public static final String HOST_URL = "http://localhost/TabletService/Service.svc/";
    public static final String KEY_USER_LOGIN = "/login";
    public static final String ECABS_PullOutletPos = "/ECABS_PullOutletPos";
    public static final String SRC_ECABS_CHECK_FORM_TYPE = "/ECABS_CheckFromType";
    public static final String KEY_ECABS_MULTI_CHECK = "/ECABS_CheckQuantity";
    public static final String KEY_ECABS_CHECK_GST_MANDATE = "/gst_mandat";
    public static final String DATA_SYNC = "/sync";
    public static final String DATA_SYNC2 = "/sync2";
    public static final String DATA_SYNC3 = "/sync3";
    public static final String DATA_SYNC4 = "/sync4";
    public static final String DATA_SYNC5 = "/sync5";
    public static final String DATA_FLOOR_SYNC6 = "/Ecabs_Floor";
    public static final String DATA_SYNC_IMAGE = "/DataSyncImage";
    public static final String FETCH_TABLE = "/ECABS_TableStatus";
    public static final String KEY_FETCH_ROOM = "/Room_Status";
    public static final String KEY_SEND_ORDER = "/ECABS_PushOrderAndroid_separator";
    public static final String KEY_SEND_ORDER_Home_D = "/ECABS_bill_generate";
    public static final String KEY_SEND_HOLD_ORDER = "/Getholdorders";
    public static final String KEY_TEST_GENERATE = "/Ecabs_TestGenerate";
    public static final String KEY_PASS_EXP_DATE = "/ECABS_GetPassExpDate";
    public static final String KEY_HEADER_FOOTER = "/ECABS_GetHeaderFooter";
    public static final String KEY_RECALL_LAST_ORDER = "/ECABS_Recall_LastOrder";
    public static final String KEY_CREATE_TAB = "/Ecabs_Tab_Table";
    public static final String KEY_PRINT_LAST_ORDER = "/ECABS_ReprintLastOrder";
    public static final String FATCH_TOUCH_EXPLORER = "/Touch_Explorer";
    public static final String FATCH_POUPLAR_ITEM = "/ECABS_FetchPopularItem";
    public static final String KEY_SAVE_COMPNAY_DETAILS = "/ECABS_SavedCompnay";
    public static final String KEY_CREW_STATUS = "/crewstatus";
    public static final String KEY_DEL_BOY_IN = "/delin";
    public static final String KEY_HOME_DELIVERY_LOG = "/deliverylog";
    public static final String KEY_HOME_DELIVERY_DETAIL = "/deliverydetail";
    public static final String KEY_PENDING_BILLS = "/SendBillDetail";
    public static final String KEY_CREDIT_DETAILS = "/settlement";
    public static final String KEY_BILL_SETTLE = "/billSettle";
    public static final String KEY_FETCH_BILLS_DETAILS = "/BillsDetails";
    public static final String KEY_BILL_MODI = "/billmodi";
    public static final String KEY_ECABS_OrderDetail = "/ECABS_Order";
    public static final String KEY_ECABS_ORDER_DETAIL = "/ECABS_PullOrderForModify";
    public static final String KEY_ECABS_OrderModiAndroid_new = "/ECABS_OrderModiAndroid_new";
    public static final String KEY_ECABS_OrderCancel = "/ECABS_PushOrdCancel";
    public static final String KEY_ECABS_TableCancel = "/ECABS_Order_Cancel";
    public static final String KEY_ECABS_IAMGE = "/FileUpload";
    public static final String KEY_ECABS_name = "/UploadPicture";
    public static final String KEY_ECABS_DisplayGuest = "/ECABS_DisplayGuest";
    public static final String KEY_ECABS_GuestOrder = "/ECABS_GuestOrder";
    public static final String KEY_ECABS_DispalyGuestOrder = "/ECABS_DispalyGuestOrder";
    public static final String KEY_ECABS_DISPLAY_GUEST_ORDER_DETAIL = "/ECABS_DispalyGuestOrderDetail";
    public static final String KEY_ECABS_PushOrderGuest = "/ECABS_PushOrderGuest";
    public static final String KEY_ECABS_CancelGuestOrder = "/ECABS_CancelGuestOrder";
    public static final String KEY_ECABS_PushOrderGuestStatus = "/ECABS_PushOrderGuestStatus";
    public static final String KEY_SEND_KITCHEN_MESSAGE = "/ECABS_KitchenMSGSend";
    public static final String KEY_SEND_ORDER_SPLIT = "/ECABS_OrderSplit";
    public static final String KEY_GUEST_PROFILE = "/ECABS_SaveGuest";
    public static final String KEY_GUEST_SEARCH = "/ECABS_GuestSearch";
    public static final String KEY_COMPANY_SEARCH = "/ECABS_Company";
    public static final String KEY_BILL_PRINT = "/printBill";
    public static final String KEY_BILL_SAVE1 = "/CABS_REST_BILL_GENERATE_SAVE1";
    public static final String KEY_BILL_GENERATE = "/CABS_REST_BILL_GENERATE1";
    public static final String KEY_FATCH_TABLE_BILL = "/ECABS_Table";
    public static final String KEY_FATCH_ORDER_ITEM_BILL = "/ECABS_PullRunOrder";
    public static final String KEY_FATCH_DYNAMIC_PRICE = "/Dynamic_Itm_Rate";
    public static final String KEY_FATCH_DYNAMIC_CATEGORY_ALL = "/Dynamic_Itm_Rate_All";
    public static final String KEY_FATCH_RUNNING_ORDER = "/ECABS_PullRunOrder";
    public static final String KEY_FATCH_ORDER_TRANSFER = "/ECABS_Fetch_OrderForTransfer";
    public static final String KEY_SEND_TABLE_AWAY = "/ECABS_TableAwayPrint";
    public static final String KEY_SEND_TABLE_JOIN = "/ECABS_TableJoin";
    public static final String KEY_SEND_TABLE_TRANSFER = "/ECABS_TableTransfer";
    public static final String KEY_SEND_GUEST_PROFILE = "/Save_Guest_Feedback_new";
    public static final String KEY_SEND_GUES_COMMENT = "/Save_Comment";
    public static final String KEY_GET_GUEST_COMMENTS = "/ECABS_GuestComments";
    public static final String KEY_SEND_GUES_STAR_RATING = "/guest_rating";
    public static final String KEY_SEND_GUES_NUMBER_RATING = "/guest_rating";
    public static final String KEY_SEND_GUES_SELECTION_RATING = "/guest_rating";
    public static final String KEY_FATCH_SAVE_BILL_NO = "/Save_Comment_Billno";
    public static final String KEY_ECABS_BillCancel = "/BillCancel";
    public static final String KEY_ECABS_BILL_TRNSFR = "/Transfer_Bill";
    public static final String KEY_ECABS_UNDO_BILL_SETTLE = "/UndoSett";
    public static final String KEY_ECABS_BILL_SPLIT = "/BillSplit";
    public static final String KEY_GET_TABLERESERVATION = "/tablereserve";
    public static final String KEY_GET_TABLE_RES_REF_NO = "/getresrefno";
    public static final String KEY_GET_TABLERESERVATION_DETAIL = "/getrestable";
    public static final String KEY_FATCH_TABLE_TRANSFER = "/ECABS_FetchTable_Transfer";
    public static final String KEY_ECABS_Order = "/ECABS_Order";
    public static final String FATCH_TABLE_STATUS = "/ECABS_ExplorerNEW";
    public static final String KEY_GUEST_ORDER_NOTIFY = "GuestNotification";
    public static final String KEY_SEND_UPCOMING_EVENT = "/upcomingEvent";

    public static final String FETCH_TABLE_DYNAMIC = "/ECABS_TableStatus_Dynamic";
    public static final String KEY_ECABS_Fetch_GuestOrder = "/ECABS_FetchGuestOrderDetail";
    public static final String KEY_ECABS_SYNC_DATA = "/sync6";
    public static final String ECABS_GUEST_SIGN_UP = "/ECABS_GuestSignUp";
    public static final String ECABS_GUEST_SIGN_IN = "/ECABS_GuestSignIn";
    public static final String FETCH_DYNAMIC_ITEM_RATE = "/Dynamic_Itm_Rate";
    public static final String FETCH_DYNAMIC_ITEM = "/Dynamic_Itm_Rate_All";
    public static final String SYNC_DYNAMIC_ITEM = "/sync_dynamic";
    public static final String FETCH_OUTLET_LATI_LONGI = "/ECABS_Latitude_Longitude";
    public static final String PUSH_USER_STATUS = "/ECABS_Update_User_Status";
    public static final String FETCH_GUEST_ORDER_STATUS = "/ECABS_Guest_Order_Status";


    private static final String REQUEST_METHOD_POST = "POST";
    private static final String REQUEST_PROPERTY_FIELD = "Content-Type";
    private static final String REQUEST_PROPERTY_VALUE = "application/json;charset=utf-8";
    private static final int CONNECTION_TIMEOUT_MILLIS = 30000;
    private static final int READ_TIMEOUT_MILLIS = 30000;
    private static BaseNetwork obj;

    public static BaseNetwork obj() {
        if (obj == null)
            obj = new BaseNetwork();

        return obj;
    }

    public static String defaultUrlMethod(String ip) {

        // "http://sutablet.csat.me/Service.svc";
        //return "http://" + ip + "/Service.svc";                 // To Connect device with Local Server
        return "http://" + ip + "/Service.svc";                   // To Connect device with Cloud Server
    }

    public static String defaultUrlMethod(String ip, String ImageURL) {

        // return "http://192.168.1.12/tb/Image/001.bmp"
        return "http://" + ip + ImageURL;
        // return "http://" + ip + "/tb/Service.svc";
    }

    public String postMethodWay(String serverIP, String hostName,
                                String keyword, String parameter) {

        URL urlString;
        HttpURLConnection urlConnect = null;
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        try {

            if (  TextUtils.isEmpty(hostName)) {
                urlString = new URL(defaultUrlMethod(serverIP) + keyword);
                Logger.i(Logger.LOGGER_TAG,
                        Logger.LOGGER_OP + urlString.toString());
            } else
                urlString = new URL(hostName);

            urlConnect = (HttpURLConnection) urlString.openConnection();
            urlConnect.setConnectTimeout(CONNECTION_TIMEOUT_MILLIS);
            urlConnect.setReadTimeout(READ_TIMEOUT_MILLIS);
            urlConnect.setRequestMethod(REQUEST_METHOD_POST);
            urlConnect.setRequestProperty(REQUEST_PROPERTY_FIELD,
                    REQUEST_PROPERTY_VALUE);

            if (!TextUtils.isEmpty(parameter))
                urlConnect.setDoInput(true);
            urlConnect.setDoOutput(true);

            if (!TextUtils.isEmpty(parameter)) {
                Logger.i("::", "::" + parameter);
                OutputStream outputStream = urlConnect.getOutputStream();
                DataOutputStream printout = new DataOutputStream(outputStream);
                printout.write(parameter.getBytes());
                printout.flush();
                printout.close();
            }
            urlConnect.connect();

            if (urlConnect.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream in = new BufferedInputStream(
                        urlConnect.getInputStream());
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in));

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    /*
                     * Logger.i(BaseNetwork.class.getName(),
					 * stringBuilder.toString());
					 */
                }
                in.close();
                reader.close();

                // Logger.i("stringBuilder", stringBuilder.toString());
                return stringBuilder.toString();

            } else {

                Logger.e(BaseNetwork.class.toString(),
                        urlConnect.getResponseMessage());
                return urlConnect.getResponseCode() + "::"
                        + urlConnect.getResponseMessage();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Invalid URL";

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return "Time Out";

        } catch (SocketException e) {
            e.printStackTrace();
            return "Connection Failed";
        } catch (Exception e) {
            e.printStackTrace();
            return "Connection Closed";

        } finally {
            if (urlConnect != null) {
                urlConnect.disconnect();
                Logger.i(BaseNetwork.class.getName(), "Connection Closed");
            }

        }

    }

    public void showErrorNetworkDialog(final Context c, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage(message).setNegativeButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void showErrorNetwork(final Context c, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage(message).setNegativeButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean checkConnOnline(Context _context) {
        ConnectivityManager conMgr = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr != null) {
            NetworkInfo i = conMgr.getActiveNetworkInfo();
            if (i == null) {
                return false;
            }
            if (!i.isConnected()) {
                return false;
            }
            return i.isAvailable();
        } else
            return false;
    }


}
