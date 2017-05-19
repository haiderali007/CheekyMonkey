package com.entrada.cheekyMonkey.staticData;

/**
 * Created by abc on 1/25/2015.
 */
public class StaticConstants {

    /**
     * Async Default Values
     */
    public static final int ASYN_NETWORK_FAIL = 0;
    public static final int ASYN_RESULT_OK = 1;
    public static final int ASYN_RESULT_CANCEL = 3;
    public static final int ASYN_RESULT_PARSE_FAILED = 5;
    // Fragment Tags
    public static final String LOGIN_FRAGMENT_TAG = "Login_Fragment";
    public static final String INTRODUCTION_FRAGMENT_TAG = "IntroductionFragment";
    public static final String NOTIFICATION_TAG = "NotificationFragment";

    public static final String LANG_BY_DEFAULT = "English";
    public static final String LANG_HINDI = "Hindi";
    public static final String LANG_CHINESE = "Chinese";

    public static final String KEY_PARCELABLE = "ItemParcelable";


    public static final String KEY_TABLE_STATUS_ORDER = "odr";
    public static final String KEY_TAG_ORDER_TYPE = "A";

    /**
     * pos TYPE VALUE
     */
    public static final String KEY_TAG_POS_TYPE_R = "R";
    public static final String KEY_TAG_POS_TYPE_H = "H";
    public static final String KEY_TAG_POS_TYPE_T = "T";
    public static final String KEY_TAG_POS_TYPE_RS = "S";
    public static final String KEY_TAF_TABLE_FLOOR_FLAG = "FT";

    public static final String JSON_TAG_DATA_SYNC_POS = "pos";
    public static final String JSON_TAG_DATA_SYNC_POS_CODE = "poscode";
    public static final String JSON_TAG_DATA_SYNC_TOKEN = "token";
    public static final String JSON_TAG_DATA_TOKEN = "gettoken";
    public static final String JSON_TAG_POS = "Pos";
    public static final String JSON_TAG_DATA_SYNC_RESULT = "syncResult";
    public static final String JSON_TAG_DATA_SYNC2_RESULT = "sync2Result";
    public static final String JSON_TAG_DATA_SYNC3_RESULT = "sync3Result";
    public static final String JSON_TAG_DATA_SYNC4_RESULT = "sync4Result";
    public static final String JSON_TAG_DATA_SYNC5_RESULT = "sync5Result";
    public static final String JSON_TAG_DATA_FLOOR_RESULT = "Ecabs_FloorResult";


    public static final String JSON_TAG_EVENT_DATE = "date";
    public static final String JSON_TAG_MESSAGE_TYPE = "msgType";
    public static final String JSON_TAG_EVENT_MESSAGE = "message";

    // Fetch Order Number
    public static final String JSON_TAG_ORDER_NO_POS = "pos";
    public static final String JSON_TAG_ORDER_NO_TABLE = "tbl";
    public static final String JSON_TAG_ORDER_NO_TOKEN = "token";


    // fetch OrderDetail
    public static final String JSON_TAG_ORDER_DETAIL_POS = "pos";
    public static final String JSON_TAG_ORDER_DETAIL_NO = "OdrNo";
    public static final String JSON_TAG_ORDER_DETAIL_TABLE = "tbl";
    public static final String JSON_TAG_ORDER_DETAIL_TOKEN = "token";

    public static final String MODIFIER_POPUP_TAG = "ModifierPOP";
    public static final String COMBO_POPUP_TAG = "ComboPOP";
    public static final String ADDON_POPUP_TAG = "AddonPOP";
    public static final String ORDER_REMARK_TAG = "OrderPOP";



    	/*
     * public static final String JSON_TAG_DS_MENU_="Cat_Name"; public static
	 * final String JSON_TAG_DS_MENU_SUB_NAME="Cat_Name";
	 */

	/* CREATE BY KAMAL */
    /**
     * ******* ADAON ***************************************************
     */
    public static final String JSON_TAG_ADD_ONS = "GetAddon";
    public static final String JSON_TAG_ADD_ONS_ITEM_CODE = "Addon_Item_Code";
    public static final String JSON_TAG_ADD_ONS_CODE = "Addon_Code";
    public static final String JSON_TAG_ADD_ONS_NAME = "Addon_Name";
    public static final String JSON_TAG_ADD_ONS_COLOR = "Addon_Color";
    public static final String JSON_TAG_ADD_ONS_PRICE = "Addon_Rate";

	/* CREATE BY KAMAL */
    /**
     * ******* MODIFIER ***************************************************
     */
    public static final String JSON_TAG_MODIFIER = "GetModif";
    public static final String JSON_TAG_MODIFIER_ITEM_CODE = "Modifier_Item_Code";
    public static final String JSON_TAG_MODIFIER_CODE = "Modifier_Code";
    public static final String JSON_TAG_MODIFIER_NAME = "Modifier_Name";
    public static final String JSON_TAG_MODIFIER_COLOR = "Modifier_Color";

    /********* COmbo Items **************************************************/
    /**
     * {"Combo_Cat_Code":"","Combo_Cat_Name":"","Combo_Code":"003","Combo_Flag":
     * "F", "Combo_Item_Code":"059","Combo_Item_Name":"Mix Vegitable",
     * "Combo_Max_Qty":"","Qty":"1"}
     */
    public static final String JSON_TAG_DS2_COMBO = "Listcombo";
    public static final String JSON_TAG_DS2_COMBO_CAT_CODE = "Combo_Cat_Code";
    public static final String JSON_TAG_DS2_COMBO_CAT_NAME = "Combo_Cat_Name";
    public static final String JSON_TAG_DS2_COMBO_COMBO_CODE = "Combo_Code";
    public static final String JSON_TAG_DS2_COMBO_COMBO_FLAG = "Combo_Flag";
    public static final String JSON_TAG_DS2_COMBO_ITEM_CODE = "Combo_Item_Code";
    public static final String JSON_TAG_DS2_COMBO_ITEM_NAME = "Combo_Item_Name";
    public static final String JSON_TAG_DS2_COMBO_MAX_QTY = "Combo_Max_Qty";
    public static final String JSON_TAG_DS2_COMBO_QTY = "Qty";

    /**
     * ******* Sub Items **************************************************
    /**
     * string uid, string password, string vendar, string WIFI, string ip
     */
    public static final String JSON_TAG_LOGIN_USER_ID = "uid";
    public static final String JSON_TAG_LOGIN_PASSWORD = "password";
    public static final String JSON_TAG_VENDOR = "vendar";
    public static final String JSON_TAG_VENDOR_VALUE = "ESOFT";
    public static final String JSON_TAG_WIFI = "WIFI";
    public static final String JSON_TAG_LOGIN_SERVER_IP = "ip";
    public static final String JSON_TAG_LOGIN_RESULT = "loginResult";

    public static final String JSON_TAG_TOKEN = "Token";
    public static final String JSON_TAG_POS_TOKEN = "token";
    public static final String JSON_TAG_POS_MAC = "MAC";
    public static final String JSON_TAG_TEST_ERROR = "TEST_ERROR";
    public static final String JSON_TAG_TEST_PASS = "PassExpDate";
    public static final String JSON_TAG_COMP_CODE = "Compcode";
    public static final String JSON_TAG_RUNNING_DATE = "RunDate";

    // TABLE
    public static final String JSON_TAG_TABLE_TAG = "tbl";

    // {"ECABS_PullOutletPosResult":[{"Code":"REST","Name":"RESTAURANT","Pos_type":"R"},{"Code":"HOME","Name":"HOME DELIVERY","Pos_type":"H"},{"Code":"TAKE","Name":"TAKE AWAY","Pos_type":"T"}]}
    public static final String JSON_TAG_POS_RESULT = "ECABS_PullOutletPosResult";
    public static final String JSON_TAG_POS_CODE = "Code";
    public static final String JSON_TAG_POS_NAME = "Name";
    public static final String JSON_TAG_POS_TYPE = "Pos_type";
    public static final String JSON_TAG_POS_HEADER = "Header";
    public static final String JSON_TAG_POS_FOOTER = "Footer";


    /**
     * Guest Login via-PhoneNumber
     */
    public static final String JSON_TAG_GUEST_PHONE = "GstCode";
    public static final String JSON_TAG_GUEST_RESPONSE = "ECABS_DisplayGuestResult";

    /**
     * Guest Order Place Parameter *
     */
    public static final String JSON_TAG_GOP_POS = "POS";
    public static final String JSON_TAG_GOP_TABLE = "Tbl";
    public static final String JSON_TAG_GOP_STEWARD = "Stw";
    public static final String JSON_TAG_GOP_USERID = "Userid";
    public static final String JSON_TAG_GOP_GUEST_CODE = "GstCode";
    public static final String JSON_TAG_GOP_GUEST_NAME = "GstName";
    public static final String JSON_TAG_GOP_ITEM = "Item";
    public static final String JSON_TAG_GOP_QTY = "Qty";
    public static final String JSON_TAG_GOP_PRICE = "Rat";
    public static final String JSON_TAG_GOP_REMARK = "Remark";
    public static final String JSON_TAG_GOP_TOEKN = "token";
    public static final String JSON_TAG_GOP_COVER = "cvr";
    public static final String JSON_TAG_GOP_ORDER_REMARK = "odrem";
    public static final String JSON_TAG_GOP_ORDER_TYPE = "odtype";
    public static final String JSON_TAG_SERVER_API_KEY = "apikey";
    public static final String JSON_TAG_DEVICE_ID = "deviceid";

    /**
     * GuestOrderResponse---> {"ECABS_GuestOrderResult":"OB000001"}
     */
    public static final String JSON_TAG_GOP_RESPONSE = "ECABS_GuestOrderResult";

    /**
     * **************** Offline start Tags *******************
     */
    // Oflline Tags
    public static final String JSON_TAG_METHODNAME = "methodname";
    public static final String JSON_TAG_PARAMETER = "parameter";
    public static final String JSON_TAG_APP_VERSION = "AppVersion";

    /******************** End Tag *************************/

    /******************** Guest Order Display ***************************/
    /**
     * param *
     */
    public static final String JSON_TAG_GUEST_POS_CODE = "POS";

    public static final String JSON_TAG_GUEST_ORDER_RESULT = "ECABS_DispalyGuestOrderResult";
    public static final String JSON_TAG_GUEST_ORDER_NUM = "OrderNo";
    public static final String JSON_TAG_GUEST_ITEM = "Item";
    public static final String JSON_TAG_GUEST_QTY = "Qty";
    public static final String JSON_TAG_GUEST_GUEST = "Guest";
    public static final String JSON_TAG_GUEST_GUEST_CODE = "Guest_Code";
    public static final String JSON_TAG_GUEST_GUEST_NAME = "Guest_Name";
    public static final String JSON_TAG_GUEST_ITEM_RATE = "Rate";
    public static final String JSON_TAG_GUEST_ITEM_TOTAL = "Total";


    /**
     * Param for Order Detail *
     */
    public static final String JSON_TAG_GUEST_ORDER_ID = "Order";
    public static final String JSON_TAG_GUEST_ORDER_STATUS = "Status";

    /**
     * response *
     */
    public static final String JSON_TAG_GUEST_DETAIL_RESULT = "ECABS_DispalyGuestOrderDetailResult";
    public static final String JSON_TAG_GST_TABLE = "Tbl";
    public static final String JSON_TAG_GST_STW = "Stw";
    public static final String JSON_TAG_GST_ITEMCODE = "ItemCode";
    public static final String JSON_TAG_GST_QTY = "StrQty";
    public static final String JSON_TAG_GST_POS = "pos";
    public static final String JSON_TAG_GST_TOKEN = "token";
    public static final String JSON_TAG_GST_COVER = "cvr";
    public static final String JSON_TAG_GST_ODREM = "odrem";
    public static final String JSON_TAG_GST_ODTYPE = "odtype";
    public static final String JSON_TAG_GST_ITEM_REMARK = "ItemRemark";
    public static final String JSON_TAG_GST_GST_NAME = "GstName";
    public static final String JSON_TAG_GST_USER_ID = "Userid";
    public static final String JSON_TAG_GST_ORDERNUM = "OrderNo";



    public static final String JSON_TAG_Current_Rate = "Current_Rate";
    public static final String JSON_TAG_Inc_Qty = "Inc_Qty";
    public static final String JSON_TAG_Inc_Rate = "Inc_Rate";
    public static final String JSON_TAG_Item_Desc = "Item_Name";
    public static final String JSON_TAG_Max_Price = "Max_Price";
    public static final String JSON_TAG_Min_Price = "Min_Price";
    public static final String JSON_TAG_Previous_Rate = "Previous_Rate";
    public static final String JSON_TAG_Sold_Qty = "Sold_Qty";
    public static final String JSON_TAG_Today_Max = "Today_Max";
    public static final String JSON_TAG_Cat_Code = "RMNU_CAT";
    public static final String JSON_TAG_Cat_Desc = "CU_DESC";
    public static final String JSON_TAG_Item_Code = "Item_Code";


    public static final String JSON_TAG_CURRENCY_TAG = "LstCurrency";
    public static final String JSON_TAG_CURRENCY_SR = "Currency_Sr";
    public static final String JSON_TAG_CURRENCY_LABEL = "Currency_Label";
    public static final String JSON_TAG_CURRENCY_VALUE = "Currency_Value";
    public static final String JSON_TAG_CURRENCY_FLAG = "Currency_Flag";
    public static final String JSON_TAG_CURRENCY_PIC = "Currency_Pic";



    public static final String JSON_TAG_DS3_DISC_DW = "DIscDW";
    public static final String JSON_TAG_DS3_DISC_CODE = "Disc_Dow_Disc_Code";
    public static final String JSON_TAG_DS3_DISC_DAY = "Disc_Dow_Day";
    public static final String JSON_TAG_DS3_DISC_POS = "Disc_Dow_Pos_Code";

    /****** DIscITM *************************************/
    /**
     * {"Disc_Item_disc_Code":"Disc.","Disc_Item_Free_Qty":"0"
     * ,"Disc_Item_Free_Item_Code":"008","Item_Flag":"I"
     * ,"PER":"0","Disc_Item_Pos_Code":"REST","BQTY":"0",
     * "Disc_Item_Item_Code":"008"}
     */
    public static final String JSON_TAG_DS3_DISC_ITEM = "DIscITM";
    public static final String JSON_TAG_DS3_DISC_ITEM_POS = "Disc_Item_Pos_Code";
    public static final String JSON_TAG_DS3_DISC_ITEM_CODE = "Disc_Item_disc_Code";
    public static final String JSON_TAG_DS3_DISC_ITEM_ITEMCODE = "Disc_Item_Item_Code";
    public static final String JSON_TAG_DS3_DISC_ITEM_PER = "PER";
    public static final String JSON_TAG_DS3_DISC_ITEM_BQTY = "BQTY";
    public static final String JSON_TAG_DS3_DISC_ITEM_FQTY = "Disc_Item_Free_Qty";
    public static final String JSON_TAG_DS3_DISC_ITEM_FREEITEM = "Disc_Item_Free_Item_Code";
    public static final String JSON_TAG_DS3_DISC_HPY_RATE = "HPY_Rate";
    public static final String JSON_TAG_DS3_DISC_ITEM_Item_Flag = "Item_Flag";


    public static final String JSON_TAG_DS3_DISC_MASTER = "Discmaster";
    public static final String JSON_TAG_DS3_DISC_MASTER_POS_CODE = "POS";
    public static final String JSON_TAG_DS3_DISC_MASTER_DISC_CODE = "Code";
    public static final String JSON_TAG_DS3_DISC_MASTER_FROM_TIME = "FTIME";
    public static final String JSON_TAG_DS3_DISC_MASTER_TO_TIME = "TTIME";
    public static final String JSON_TAG_DS3_DISC_MASTER_FROM_DATE = "FDATE";
    public static final String JSON_TAG_DS3_DISC_MASTER_TO_DATE = "TDATE";
    public static final String JSON_TAG_DS3_DISC_MASTER_DISC_ACTIVE = "ACT";
    public static final String JSON_TAG_DS3_DISC_MASTER_FLAG = "FLAG";
    public static final String JSON_TAG_DISC_MST_DISC_DESC = "DISC_DESC";
    public static final String JSON_TAG_DISC_MST_DISC_FL = "DISC_FL";
    public static final String JSON_TAG_DISC_MST_DISC_TL = "DISC_TL";
    public static final String JSON_TAG_DISC_MST_DISC_FLAG = "DISC_FLAG";
    public static final String JSON_TAG_DISC_MST_DISC_OPN = "DISC_OPN";
    public static final String JSON_TAG_DISC_MST_DISC_FLAT_FLAG = "FLAT_FLAG";
    public static final String JSON_TAG_DISC_MST_DISC_OGI = "Disc_OGI";

}
