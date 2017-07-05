package com.entrada.cheekyMonkey.db;

public interface DBConstants {

    /**
     * *********************** Master [DBCONSTANTS] ***********************
     */
    String CREATE_TABLE_BASE = "CREATE TABLE IF NOT EXISTS ";
    String CREATE_INDEX_BASE = "CREATE INDEX IF NOT EXISTS ";
    String ON = " ON ";
    String PRIMARY_KEY = " PRIMARY KEY";
    String INTEGER = " Integer";
    String TEXT = " TEXT";
    String BLOB = " BLOB";
    String AUTO_ICNREMENT = " AUTOINCREMENT";
    String START_COLUMN = " ( ";
    String FINISH_COLUMN = " ) ";
    String COMMA = ",";
    String _ID = "_id";

    /**
     * StewardDetail Table [DBCONSTANTS] ********************
     */
    String KEY_EMP_DETAIL = "EmpDetail";
    String KEY_EMP_CODE = "emp_code";
    String KEY_EMP_NAME = "emp_name";
    String KEY_EMP_USER_ID = "emp_user_id";
    String KEY_EMP_TYPE = "emp_type";
    String KEY_EMP_POS = "emp_pos";
    /**
     * USR_TABLE [CONSTANTS] ****************************
     */
    String KEY_USR_TABLE = "USR_Table";
    String KEY_USR_USERNAME = "USR_UserName";
    String KEY_USR_PASSWORD = "USR_Password";
    String KEY_USR_POS_CODE = "USR_Pos_Code";
    String KEY_USR_TOKEN = "USR_Token";
    String KEY_USR_SERVER_ID = "USR_ServerIP";
    String KEY_USR_LANGUAGE = "USR_Language";
    String KEY_USR_THEME_BCKGRND_COLOR = "USR_Theme_Background_Color";
    String KEY_USR_THEME_FONT_COLOR = "USR_Theme_Font_Color";
    String KEY_COMP_CODE = "USR_Comp_Code";
    String KEY_RUNNING_DATE = "Running_Date";

    /**
     * OUTLET_TABLE [CONSTANTS] *************************
     */
    String KEY_OUTLET_POS_TABLE = "Outlet_POS_Table";
    String KEY_OUTLET_POS_CODE = "Pos_Code";
    String KEY_OUTLET_POS_NAME = "Pos_Name";
    String KEY_OUTLET_POS_TYPE = "Pos_Type";
    String KEY_OUTLET_POS_HEADER = "Pos_Header";
    String KEY_OUTLET_POS_FOOTER = "Pos_Footer";


    /**
     * OUTLET DETAILS TABLE(FILL / VACANT / RESERVE )**** [CONSTANT] **
     */
    String KEY_OUTLET_TABLE_NAME = "RMS_OUTLETS";
    String KEY_OUTLET_TABLE_OUTLET_CODE = "Outlet_Code";
    String KEY_OUTLET_TABLE_OUTLET_NAME = "Outlet_Name";
    String KEY_OUTLET_TABLE_LOCATION = "Outlet_Address";
    String KEY_OUTLET_TABLE_CITY = "Outlet_City";


    /**
     * ADMIN TABLE (CREATE USER TYPE / PERMISSION ETC)**** [CONSTANT] **
     */
    String KEY_ADMIN_TABLE_NAME = "RMS_ADMIN";
    String KEY_ADMIN_TABLE_OUTLET_ID = "Admin_OutletID";
    String KEY_ADMIN_TABLE_ADMIN_NAME = "Admin_Name";
    String KEY_ADMIN_TABLE_USER_ID = "Admin_UserID";
    String KEY_ADMIN_TABLE_USER_PASSWORD = "Admin_Password";
    String KEY_ADMIN_TABLE_USER_TYPE = "Admin_User_Type";
    String KEY_ADMIN_TABLE_ACTIVE = "Admin_Active_Deactive";
    String KEY_ADMIN_TABLE_EXPIRE_DATE = "Admin_Expire_Date";
    String KEY_ADMIN_TABLE_PERMISSION = "Admin_Permission";

    /**
     * ADMIN TABLE PERMISSION(PERMISSION ONLY)**** [CONSTANT] **
     */
    String KEY_ADMIN_TABLE_PERRMISSION = "RMS_ADMIN_PERMISSION";
    String KEY_ADMIN_TABLE_PERMISSION_OUTLET_ID = "Admin_OutletID";
    String KEY_ADMIN_TABLE_PERMISSION_USER_ID = "Admin_UserID";
    String KEY_ADMIN_TABLE_PERMISSION_TYPE = "Permission";



    /**
     * OUTLET TABLE(FILL / VACANT / RESERVE )**** [CONSTANT] **
     */
    String KEY_OUTLET_TABLE = "Outlet_Table";
    String KEY_OUTLET_TABLE_POS_CODE = "Outlet_Table_Pos_Code";
    String KEY_OUTLET_TABLE_CODE = "Table_Code";
    String KEY_OUTLET_TABLE_NUM = "Table_Num";

    /**
     * GROUP TABLE( FOOD, BEVERAGE etc)***** [CONSTANTS] **
     */
    String KEY_GROUP_TABLE = "Group_Table";
    String KEY_GROUP_CODE = "Group_Code";
    String KEY_GROUP_COLOR = "Group_Color";
    String KEY_GROUP_Name = "Group_Name";

    /**
     * CATEGORY TABLE ***** [CONSTANTS] *******************
     */
    String KEY_CATEGORY_TABLE = "Category_Table";
    String KEY_CATEGORY_POS_CODE = "Category_Pos_Code";
    String KEY_CAT_CODE = "Category_Code";
    String KEY_CAT_COLOR = "Category_Color";
    String KEY_CAT_NAME = "Category_Name";
    String KEY_CAT_GRP_CODE = "Category_Group_Code";

    /**
     * MENU ITEM TABLE ****[CONSTANTS] *******************
     */
    String KEY_MENU_ITEM_FTS_TABLE = "Menu_Item_Table_fts";
    String KEY_MENU_ITEM_TABLE = "Menu_Item_Table";
    String KEY_MENU_POS_CODE = "Menu_Pos_Code";
    String KEY_MENU_CODE = "Menu_Code";
    String KEY_MENU_NAME = "Menu_Name";
    String KEY_MENU_COLOR = "menu_color";
    String KEY_MENU_CAT_CODE = "Menu_Category_Code";
    String KEY_MENU_GRP_CODE = "Menu_Group_Code";
    String KEY_MENU_PRICE = "Menu_Price";
    String KEY_MENU_VAT = "Menu_Vat";
    String KEY_MENU_SUB_UNIT = "Menu_Sub_Unit";
    String KEY_MENU_COMBO_FLAG = "Menu_Combo_Flag";
    String KEY_MENU_ADDON_FLAG = "Menu_Addon_Flag";
    String KEY_MENU_MODIFIER = "Menu_Modifier_Flag";
    String KEY_MENU_OPEN_ITEM = "Menu_Open_Item_Flag";
    String KEY_MENU_ITEM_DYNAMIC = "Menu_Item_Dynamic";
    String KEY_MENU_SUB_ITEM = "Menu_Sub_Item_Flag";
    String KEY_MENU_VEG_NON = "Menu_Veg_Non";
    String KEY_MENU_CALORIES = "menu_calories";

    /**
     * ADDONS TABLE ***** [CONSTANTS] *******************
     */
    String KEY_ADDONS_TABLE = "Addons_Table";
    String KEY_ADDON_POS_CODE = "Addon_Pos_Code";
    String KEY_ADDONS_ITEM_CODE = "Addon_Item_Code";
    String KEY_ADDONS_CODE = "Addon_Code";
    String KEY_ADDONS_NAME = "Addon_Name";
    String KEY_ADDONS_COLOR = "Addon_Color";
    String KEY_ADDONS_PRICE = "Addon_Price";

    /**
     * MODIFIER TABLE **** [DBCONSTANTS] *******************
     */
    String KEY_MODIFIER_TABLE = "Modifier_Table";
    String KEY_MODIFIER_POS_CODE = "Addon_Pos_Code";
    String KEY_MODIFIER_ITEM_CODE = "Modifier_Item_Code";
    String KEY_MODIFIER_CODE = "Modifier_Code";
    String KEY_MODIFIER_NAME = "Modifier_Name";
    String KEY_MODIFIER_COLOR = "Modifier_Color";

    /**
     * COMBO TABLE *****[DBCONSTANTS] *******************
     */
    String KEY_COMBO_TABLE = "Combo_Table";
    String KEY_COMBO_POS_CODE = "Combo_Pos_Code";
    String KEY_COMBO_CODE = "Combo_Code";
    String KEY_COMBO_ITEM_CODE = "Combo_Item_Code";
    String KEY_COMBO_NAME = "Combo_Item_Name";
    String KEY_COMBO_QTY = "Combo_Qty";
    String KEY_COMBO_MAX_QTY = "Combo_Max_Qty";
    String KEY_COMBO_FLAG = "Combo_Flag";
    String KEY_COMBO_CAT_CODE = "Combo_Cat_Code";
    String KEY_COMBO_CAT_NAME = "Combo_Cat_Name";

    /**
     * ITEM REMARK TABLE *** [DBCONSTANTS] *****************
     */
    String KEY_ITEM_REMARK_TABLE = "Item_Remark_Table";
    String KEY_ITEM_REMARK_CODE = "Item_Remark_Code";
    String KEY_ITEM_REMARK_DESC = "Item_Remark_Desc";

    /**
     * ORDER REMARK TABLE ****[DBCONSTANTS] ****************
     */
    String KEY_ORDER_REMARK_TABLE = "OrderRemark_Table";
    String KEY_ORDER_REMARK_CODE = "Order_Remark_Code";
    String KEY_ORDER_REMARK_DESC = "Order_Remark_Desc";

    /**
     * ORDER REMARK TABLE ****[DBCONSTANTS] ****************
     */
    String KEY_ORDER_TYPE_TABLE = "OrderType_Table";
    //  String KEY_ORDER_TYPE_CODE = "OrderType_Remark_Code";
    String KEY_ORDER_TYPE_DESC = "OrderType_Remark_Desc";

    /**
     * KITCHEN_MASTER TABLE *** [DBCONSTANTS] ****************
     */
    String KEY_KITCHEN_MASTER_TABLE = "Ktichen_Master_Table";
    String KEY_KITCHEN_MASTER_CODE = "Kitchen_Master_Code";
    String KEY_KITCHEN_MASTER_NAME = "Kitchen_Master_Name";

    /**
     * KITCHEN REMARK TABLE ** [DBCONSTANTS] ***************
     */
    String KEY_KITCHEN_REMARK_TABLE = "Kitchen_Remark_Table";
    String KEY_KITCHEN_REMARK_CODE = "Kitchen_Remark_Code";
    String KEY_KITCHEN_REMARK_NAME = "Kitchen_Remark_Name";

    /**
     * GUEST TYPE TABLE ***[DBCONSTANTS] *****************
     */
    String KEY_GUEST_TYPE_TABLE = "Guest_Type_Table";
    String KEY_GUEST_TYPE_CODE = "Guest_Type_Code";
    String KEY_GUEST_TYPE_NAME = "Guest_Type_Name";

    /**
     * COMP TYPE TABLE ***[DBCONSTANTS] *****************
     */
    String KEY_COMPLIMENTARY_TABLE = "Comp_Type_Table";
    String KEY_COMPLIMENTARY_CODE = "Comp_Code";
    String KEY_COMPLIMENTARY_NAME = "Comp_Name";


    /**
     * STEWARD TYPE TABLE ***[DBCONSTANTS] ****************
     */
    String KEY_STEWARD_TYPE_TABLE = "Steward_Type_Table";
    String KEY_STEWARD_TYPE_CODE = "Steward_Type_Code";
    String KEY_STEWARD_TYPE_NAME = "Steward_Type_Name";
    String KEY_STEWARD_TYPE_USER_ID = "Steward_Type_User_Id";

    /**
     * DISC_MST TABLE ***[DBCONSTANTS] *********************
     */
    String KEY_DISC_MST_TABLE = "Disc_Mst_Table";
    String KEY_DISC_MST_POS_CODE = "Disc_Mst_Pos_Code";
    String KEY_DISC_MST_DISC_CODE = "Disc_Mst_Disc_Code";
    String KEY_DISC_MST_FROM_DATE = "Disc_Mst_From_Date";
    String KEY_DISC_MST_TO_DATE = "Disc_Mst_To_Date";
    String KEY_DISC_MST_FROM_TIME = "Disc_Mst_From_Time";
    String KEY_DISC_MST_TO_TIME = "Disc_Mst_To_Time";
    String KEY_DISC_MST_DISC_ACTIVE = "Disc_Mst_Disc_Active";
    String KEY_DISC_MST_DISC_DESC = "disc_desc";
    String KEY_DISC_MST_DISC_FL = "Disc_FL";
    String KEY_DISC_MST_DISC_TL = "Disc_TL";
    String KEY_DISC_MST_DISC_FLAG = "Disc_Flag";
    String KEY_DISC_MST_DISC_OPN = "Disc_Opn";
    String KEY_DISC_MST_DISC_FLAT_FLAG = "FLAT_Flag";
    String KEY_DISC_MST_DISC_OGI = "DISC_OGI";


    /**
     * DISC_DOW TABLE ***[DBCONSTANTS] ********************
     */
    String KEY_DISC_DOW_TABLE = "Disc_Dow_Table";
    String KEY_DISC_DOW_POS_CODE = "Disc_Dow_Pos_Code";
    String KEY_DISC_DOW_DISC_CODE = "Disc_Dow_Disc_Code";
    String KEY_DISC_DOW_DAY = "Disc_Dow_Day";

    /**
     * DISC_ITEM TABLE (POS_Code DISC_Code Item_Code Disc_Per Billed_Qty
     * Free_Qty Free_Item_Code Item_Flag Disc_rate) **[DBCONSTANTS]
     * ***********************
     */
    String KEY_DISC_ITEM_TABLE = "Dics_Item_Table";
    String KEY_DISC_ITEM_POS_CODE = "Disc_Item_Pos_Code";
    String KEY_DISC_ITEM_DISC_CODE = "Disc_Item_disc_Code";
    String KEY_DISC_ITEM_ITEM_CODE = "Disc_Item_Item_Code";
    String KEY_DISC_ITEM_DISC_PER = "Disc_Item_Disc_Per";
    String KEY_DISC_ITEM_BILLED_QTY = "Disc_Item_Billed_Qty";
    String KEY_DISC_ITEM_FREE_QTY = "Disc_Item_Free_Qty";
    String KEY_DISC_FREE_ITEM_CODE = "Disc_Item_Free_Item_Code";
    String KEY_DISC_HAPPY_RATE = "Disc_Item_HPY_Rate";


    /**
     * SUB ITEM TABLE *** [DBCONSTANTS] ******************
     */
    String KEY_SUB_ITEM_TABLE = "Sub_Item_Table";
    String KEY_SUB_ITEM_MENU_CODE = "Sub_Item_Menu_Code";
    String KEY_SUB_ITEM_SUB_CODE = "Sub_Item_Sub_Code";
    String KEY_SUB_ITEM_SUB_NAME = "Sub_Item_Sub_Name";
    String KEY_SUB_ITEM_POS = "Sub_Item_Pos";
    String KEY_SUB_ITEM_SUB_RATE = "Sub_Item_Sub_Rate";

    /**
     * Guest Discount Table [DBCONSTANTS] ********************
     */
    String KEY_GUEST_DISCOUNT_TABLE = "GuestDiscountTable";
    String KEY_GUEST_DIS_GUEST_CODE = "GD_GuestCode";
    String KEY_GUEST_DIS_GUEST_GRP_CODE = "GD_GuestGroupCode";
    String KEY_GUEST_DIS_GUEST_DISCOUNT = "GD_GuestDiscount";

    /**
     * SurchargeDOW Table [DBCONSTANTS] ********************
     */
    String KEY_HSSURCHR_DOW = "SurchargeDOW";
    String KEY_SURCHARGE_POS_CODE = "pos_code";
    String KEY_SURCHARGE_DISC_CODE = "disc_code";
    String KEY_SURCHARGE = "disc_dow";

    /**
     * SurchargeMST Table [DBCONSTANTS] ********************
     */
    String KEY_HSSURCHR_MST = "SurchargeMST";
    String KEY_SURCHARGE_MST_POS_CODE = "pos_code";
    String KEY_SURCHARGE_MST_DISC_CODE = "disc_code";
    String KEY_SURCHARGE_MST_FROM_DATE = "from_date";
    String KEY_SURCHARGE_MST_TO_DATE = "to_date";
    String KEY_SURCHARGE_MST_FROM_TIME = "from_time";
    String KEY_SURCHARGE_MST_TO_TIME = "to_time";
    String KEY_SURCHARGE_MST_DISC_ACT = "disc_act";
    String KEY_SURCHARGE_MST_DISC_OG1 = "disc_ogi";
    String KEY_SURCHARGE_MST_DISC_FL = "disc_fl";
    String KEY_SURCHARGE_MST_DISC_TL = "disc_tl";
    String KEY_SURCHARGE_MST_DISC_PER = "disc_per";

    /**
     * SurchargeItem Table [DBCONSTANTS] ********************
     */
    String KEY_HSSURCHG_ITEM = "SurchargeItem";
    String KEY_HSSURCHG_ITM_POS_CODE = "pos_code";
    String KEY_HSSURCHG_ITM_DISC_CODE = "disc_code";
    String KEY_HSSURCHG_ITM_ITEM_CODE = "item_code";
    String KEY_HSSURCHG_ITM_DISC_PER = "disc_per";

    /**
     * MeunItemDetail Table [DBCONSTANTS] ********************
     */
    String KEY_GUEST_TITLE = "ItmTitle";
    String KEY_GUEST_TITL_CODE = "code";
    String KEY_GUEST_TITL_ABOUT = "about";
    String KEY_GUEST_TITL_DESC = "desc";


    /**
     * MeunItemDetail Table [DBCONSTANTS] ********************
     */
    String KEY_ABT_ITEM_IMAGE = "AbtItemImage";
    String KEY_ABT_CODE = "code";
    String KEY_ABT_ABOUT = "about";

    /**
     * Cmment_Card Table [DBCONSTANTS] ********************
     */
    String KEY_COMMENT_CARD = "Cmment_Card";
    String KEY_CC_PAR = "cc_para";
    String KEY_CC_RATING = "cc_rating";
    String KEY_CC_FLAG = "cc_flag";

    /**
     * Gst_prmission Table [DBCONSTANTS] ********************
     */
    String KEY_GUEST_PERMISION = "Gst_prmission";
    String KEY_GUEST_GUEST_MAC = "guest_mac";
    String KEY_GUEST_GUEST_PERMISISON_CODE = "guest_permission_code";

    /**
     * MEAL TABLE ****** [DBCONSTANTS] ********************
     */
    String KEY_MEAL_TABLE = "Meal_Table";
    String KEY_MEAL_CODE = "Meal_Code";
    String KEY_MEAL_DESC = "Meal_Desc";
    String KEY_MEAL_PRN_ODR = "Meal_Prn_Odr";

    /**
     * MEAL TABLE ****** [DBCONSTANTS] ********************
     */
    String KEY_MANDATORY_TABLE = "Mandatory";
    String KEY_MD_COVER_ACTIVE = "cover_active";
    String KEY_MD_COVER_MANDATORY = "Cover_Man";
    String KEY_MD_STEWARD_ACTIVE = "steward_active";
    String KEY_MD_STEWARD_MANDATORY = "steward_man";
    String KEY_MD_MENU_CODE = "menu_code";
    String KEY_MD_COVER_NO = "cover_no";
    String KEY_MD_MEAL_COURSE = "meal_cors";
    String KEY_MD_MEAL_SURCHARGE_ACT = "surcharge_act";

    /**
     * Prmission TABLE ****** [DBCONSTANTS] ********************
     */
    String KEY_PERMISSION = "Prmission";
    String KEY_PRM_USER_ID = "user_id";
    String KEY_PRM_MENU_VALUE = "menu_value";
    String KEY_PRM_menu_id = "menu_id";

    /**
     * ============= sync3 obj.DIscDW = ECABS_DiscDOW(); obj.DIscITM =
     * ECABS_DiscITEMS(); obj.Discmaster = ECABS_DiscMst(); obj.Itmremark =
     * ECABS_PullItmRemark(token); obj.Kitchen = ECABS_KitchenMaster();
     * obj.OderRemark = ECABS_OrderRemark(); obj.OderType =
     * ECABS_GetOrderType();
     *
     *
     * GetKitchen_Remark_Json ================== ======================= sync4
     * obj.Gst_Disc = ECABS_GUEST_DISC(); obj.Hssurchg_dow =
     * CABS_RMS_HSSURCHG_DOW(pos); obj.Hssurchg_itm =
     * CABS_RMS_HSSURCHG_ITEMS(pos); obj.Hssurchg_mst =
     * CABS_RMS_HSSURCHG_MST(pos); obj.ItmTitle = ECABS_GuestTitle();
     * obj.Steward = ECABS_PullSteward(token); ==========================
     *
     * sync5 obj.AbtItemImage = ECABS_AboutItemImage(); obj.Cmment_Card =
     * Guest_Feedback(token); obj.Gst_prmission = GET_GUESTPERMISSION(mac);
     * obj.Meal_Course = CABS_Meal_Course(); obj.Mndatory = Ecabs_Mandatory();
     * obj.Prmission = Ecabs_permission();
     **/


    /**
     * * GUEST SEARCH TABLE******[DBCONSTANTS]************************
     */
    String KEY_GUEST_SEARCH_FTS_TABLE = "guestsearch_fts";
    String KEY_GUEST_SEARCH_TABLE = "Guest_Search_Table";
    String KEY_GUEST_NAME = "Gst_Name";
    String KEY_GUEST_PHONE = "Gst_Phone";
    String KEY_GUEST_EMAIL = "Gst_Email";
    String KEY_GUEST_DOB = "Gst_DOB";
    String KEY_GUEST_ANNIVERSARY = "Gst_Anniversary";
    String KEY_GUEST_SEX = "Gst_Sex";
    String KEY_GUEST_ADDRESS = "Gst_Address";
    String KEY_GUEST_ADDRESS1 = "Gst_Address1";
    String KEY_GUEST_REMARK = "Gst_Remark";
    String KEY_GUEST_COMMENT = "Gst_Comment";


    /**
     * GUEST FEEDBACK TABLE****** [DBCONSTANTS] ********************
     */

    String KEY_GUEST_FEEDBACK_TABLE = "guest_feedback_table";
    String KEY_GUEST_FEEDBACK_CC_PARA = "guest_item_cc_para";
    String KEY_GUEST_FEEDBACK_CC_RATING = "guest_feedback_cc_rating";
    String KEY_GUEST_FEEDBACK_CC_FLAG = "guest_feedback_cc_flag";
    String KEY_GUEST_FEEDBACK_CC_GRP = "guest_cc_grp";
    String KEY_GUEST_FEEDBACK_CC_SEL = "guest_cc_sel";


    /**
     * GUEST ORDERS TABLE****** [DBCONSTANTS] ********************
     */

    String KEY_GUEST_ORDER_TABLE = "guest_odr_table";
    String KEY_GUEST_ORDER_NUMBER = "order_no";
    String KEY_GUEST_ORDER_ITEMS = "order_items";
    String KEY_GUEST_ORDER_AMT = "order_amt";
    String KEY_GUEST_ORDER_TABLE_NO = "order_tblno";
    String KEY_GUEST_ORDER_STATUS = "order_status";




    /*********  New Guest Orders Table  *******/

     String KEY_GUEST_ORDERS_TABLE = "guest_orders_table";
     String KEY_ORDER_NUMBER = "order_no";
     String KEY_TABLE_NUMBER = "table_no";
     String KEY_ITEM_CODE = "item_code";
     String KEY_ITEM_NAME = "item_name";
     String KEY_ITEM_PRICE = "item_price";
     String KEY_ITEM_QTY = "item_qty";
     String KEY_ORDER_TAX = "ttl_tax";
     String KEY_ORDER_AMOUNT = "order_amt";
     String KEY_ORDER_STATUS = "order_status";
     String KEY_ORDER_DATE = "order_date";
     String KEY_ORDER_TIME = "order_time";


    /******************* COMPANY DISCOUNT [DBCONSTANTS]*************************/

    String KEY_RMS_COMPANY_TABLE = "RMS_COMPANY_TABLE";
    String KEY_COMPANY_COMP_CODE = "COMP_CODE";
    String KEY_COMPANY_COMP_NAME = "COMP_NAME";
    String KEY_COMPANY_COMP_ADD1 = "COMP_ADD1";
    String KEY_COMPANY_COMP_ADD2 = "COMP_ADD2";
    String KEY_COMPANY_COMP_CITY = "COMP_CITY";
    String KEY_COMPANY_COMP_PIN = "COMP_PIN";
    String KEY_COMPANY_COMP_EMAIL = "COMP_EMAIL";
    String KEY_COMPANY_COMP_CL = "COMP_CL";
    String KEY_COMPANY_COMP_PAN_NO = "COMP_PAN_NO";
    String KEY_COMPANY_COMP_ID = "COMP_ID";
    String KEY_COMPANY_COMP_PHONE1 = "COMP_PHONE1";
    String KEY_COMPANY_COMP_VNO = "COMP_VNO";
    String KEY_COMPANY_COMP_COMMENT = "COMP_COMMENT";
    String KEY_COMPANY_COMP_PIC = "COMP_PIC";
    String KEY_COMPANY_COMP_CAT = "COMP_CAT";
    String KEY_COMPANY_COMP_ARR_DATE = "COMP_ARR_DATE";
    String KEY_COMPANY_COMP_DEP_DATE = "COMP_DEP_DATE";
    String KEY_COMPANY_COMP_STS = "COMP_STS";
    String KEY_COMPANY_COMP_INFO_DATE = "COMP_INFO_DATE";
    String KEY_COMPANY_COMP_LOYALTY_DATE = "COMP_LOYALTY_DATE";


    String KEY_FLOOR_TABLE = "Floor_Table";
    String KEY_FLOOR_POS = "POP";
    String KEY_FLOOR_SELECTION = "floor";
    String KEY_FLOOR_IMAGE_NAME = "Floor_Image_Name";
    String KEY_FLOOR_IMAGE_X = "Floor_Image_x";
    String KEY_FLOOR_IMAGE_Y = "Floor_Image_y";
    String KEY_FLOOR_IMAGE_SCALE_X = "Floor_Image_SCALE_X";
    String KEY_FLOOR_IMAGE_SCALE_Y = "Floor_Image_SCALE_Y";
    String KEY_FLOOR_IMAGE_SCALE_ANGLE = "Floor_Image_SCALE_ANGLE";
    String KEY_FLOOR_IMAGE_WIDTH = "Floor_Image_WIDTH";
    String KEY_FLOOR_IMAGE_HEIGHT = "Floor_Image_HEIGHT";
    String KEY_FLOOR_IMAGE_DWIDTH = "Floor_Image_DWIDTH";
    String KEY_FLOOR_IMAGE_DHEIGHT = "Floor_Image_DHEIGHT";
    String KEY_FLOOR_MMINX = "mMinX";
    String KEY_FLOOR_MMAXX = "mMaxX";
    String KEY_FLOOR_MMINY = "mMinY";
    String KEY_FLOOR_MMAXY = "mMaxY";
    String KEY_FLOOR_MISGRABAREASELECTED = "mIsGrabAreaSelected";
    String KEY_FLOOR_MISLATESTSELECTED = "mIsLatestSelected";
    String KEY_FLOOR_MGRABAREAX1 = "mGrabAreaX1";
    String KEY_FLOOR_MGRABAREAY1 = "mgrabareay1";
    String KEY_FLOOR_MGRABAREAX2 = "mGrabAreaX2";
    String KEY_FLOOR_MGRABAREAY2 = "mGrabAreaY2";
    String KEY_FLOOR_MSTARTMIDX = "mStartMidX";
    String KEY_FLOOR_MSTARTMIDY = "mStartMidY";
    String KEY_FLOOR_INTRINSICWIDTH = "IntrinsicWidth";
    String KEY_FLOOR_INTRINSICHEIGHT = "IntrinsicHeight";
    String KEY_FLOOR_IS_FIRST_LOAD = "FirstLoad";

    String KEY_FLOOR_UI_MODE_ROTATE = "UI_MODE_ROTATE";
    String KEY_FLOOR_UI_MODE_ANISOTROPIC_SCALE = "UI_MODE_ANISOTROPIC_SCALE";
    String KEY_FLOOR_UI_MUIMODE = "mUIMode";
    String KEY_FLOOR_IMAGE_TEXT = "ImageText";

    /**
     * Data Sync Floor Table
     */
    /*
     Start
      */
    String KEY_TBLTABLE = "tableFloor";
    String KEY_TBL_RMSC_POP = "RMSC_POP";
    String KEY_TBL_RMSC_COD = "RMSC_COD";
    String KEY_TBL_RMSC_STD = "rmsc_std";
    String KEY_TBL_RMSC_BCOLOR = "RMSC_BColor";
    String KEY_TBL_RMSC_FLOOR = "RMSC_Floor";
    String KEY_TBL_RMSC_ROW = "RMSC_Row";
    String KEY_TBL_RMSC_COLUMN = "RMSC_Column";
    String KEY_TBL_RMSC_SHAPE = "RMSC_Shape";


    String KEY_FLR_FLOOR = "floors";
    String KEY_FLR_FLOOR_CODE = "floor_code";
    String KEY_FLR_FLOOR_DESC = "Floor_Desc";
    String KEY_FLR_FLOOR_PIC = "Floor_Pic";
    String KEY_FLR_FLOOR_STATUS = "Floor_Status";
    String KEY_FLR_RMS_FLOOR_MST = "RMS_FLOOR_MST";
    /*
    End
     */


    /**
     * * GUEST SEARCH TABLE******[DB CONSTANTS]************************
     */

    String KEY_ATTRIBUTE_TABLE = "Attribute";
    String KEY_CHECK_HOME_ORDER_GUEST_MANDATE = "guest";
    String KEY_CHECK_HOME_HOLD_ORDER = "hold_order";
    String KEY_CHECK_MULTI_QTY = "multi_qty";
    String KEY_CHECK_FORM_TYPE = "form_type";
    String KEY_CHECK_BILL_SETTLE = "settle";
    String KEY_CHECK_BILL_SETTLE_MANDATE = "bill_settle";
    String KEY_CHECK_PER_ORDER_LOGIN = "max_order";
    String KEY_NO_REASON_ORDER_MODIFICATION = "rsn_odr_modi";
    String KEY_PRINT_CANCEL_MODIFY = "print_order";
    String KEY_ORDER_REMARK_MANDATORY = "odr_rem";
    String KEY_NO_REASON_BILL_MODIFICATION = "rsn_bill_modi";
    String KEY_NO_GENERATE_STW_BILL = "gen_stw_bill";
    String KEY_ACTIVATE_REMARK_AS_GUEST = "rem_as_gst";
    String KEY_GROUP_WISE_BILLING= "gw_billing";
    String KEY_HIDE_BILL_REMARK = "hide_bill_remark";
    String KEY_NO_DELETE_BTN_BILL_EDIT = "del_bill_edit";
    String KEY_NO_MODIFY_BTN_BILL_EDIT = "mod_bill_edit";
    String KEY_DISCOUNT_REASON_MANDATORY = "disc_reason";


    // Table to store User image

    String IMAGE_TABLE = "table_image";
    String IMAGE_NAME = "image_name";
    String IMAGE_DATA = "image_data";



    // Table to store items detail
    String ITEMS_DETAIL_TABLE = "ItemsTable";
    String Current_Rate = "cur_rate";
    String Inc_Qty = "inc_qty";
    String Inc_Rate = "inc_rate";
    String Item_Desc = "item_desc";
    String Max_Price = "max_price";
    String Min_Price = "min_price";
    String Previous_Rate = "prev_rate";
    String Sold_Qty = "sold_qty";
    String Today_Max = "today_max";
    String Cat_Code = "cat_code";
    String Cat_Desc = "cat_desc";
    String Item_Code = "item_code";

    /**
     * COMBO TABLE *****[DBCONSTANTS] *******************
     */
    String KEY_CURRENCY_TABLE = "Currency_Table";
    String KEY_CURRENCY_SR = "Currency_Sr";
    String KEY_CURRENCY_LABEL = "Currency_Label";
    String KEY_CURRENCY_VALUE = "Currency_Value";
    String KEY_CURRENCY_FLAG = "Currency_Item_Flag";
    String KEY_CURRENCY_PIC = "Currency_Pic";
}
