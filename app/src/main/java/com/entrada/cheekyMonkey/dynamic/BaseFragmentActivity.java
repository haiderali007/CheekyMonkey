package com.entrada.cheekyMonkey.dynamic;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.entrada.cheekyMonkey.Help_Layout;
import com.entrada.cheekyMonkey.IntroductionScreen;
import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.ICallBackFinish;
import com.entrada.cheekyMonkey.appInterface.OnBackPressInterface;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.dynamic.Network_Info.ActivateUserIDTask;
import com.entrada.cheekyMonkey.dynamic.Network_Info.NetworkChangeReceiver;
import com.entrada.cheekyMonkey.dynamic.Network_Info.NetworkUtil;
import com.entrada.cheekyMonkey.dynamic.about.AboutDeveloperFragment;
import com.entrada.cheekyMonkey.dynamic.about.AboutUsFragment;
import com.entrada.cheekyMonkey.dynamic.about.DocsFragment;
import com.entrada.cheekyMonkey.dynamic.about.GridTitleAdapter;
import com.entrada.cheekyMonkey.dynamic.about.GridTitleAdapterSteward;
import com.entrada.cheekyMonkey.dynamic.about.NewsFragment;
import com.entrada.cheekyMonkey.dynamic.start.MoreOptionsLayout;
import com.entrada.cheekyMonkey.dynamic.syncData.FetchAndStoreMenuItemsTask;
import com.entrada.cheekyMonkey.dynamic.syncData.ICallResponse;
import com.entrada.cheekyMonkey.entity.TitleHeader;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.staticData.PrefHelper;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.steward.ExpandableListAdapter;
import com.entrada.cheekyMonkey.steward.StewardOrderFragment;
import com.entrada.cheekyMonkey.steward.bill.BillGenerateFragment;
import com.entrada.cheekyMonkey.steward.notificationUI.NotificationFragment;
import com.entrada.cheekyMonkey.steward.ordersplit.OrderSplitFragment;
import com.entrada.cheekyMonkey.steward.other.CustomLoginPopup;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.ui.SlidingUpPanelLayout;
import com.entrada.cheekyMonkey.uiDialog.ExitDialog;
import com.entrada.cheekyMonkey.util.Logger;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.navdrawer.SimpleSideDrawer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rahul on 16/05/2016.
 */
public class BaseFragmentActivity extends FragmentActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, ICallBackFinish, ICallResponse, ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {


    Context context;
    private SimpleSideDrawer slide_me;
    ListView listview_left, listview_right;
    ImageView home_icon_button, right_button, img_user_pic, img_camera, img_lock, img_unlock, img_refresh;
    ImageButton image_back, img_srch;
    SearchView edit_search;
    RelativeLayout layout_noti;
    LinearLayout layout_gst_ord_detail;
    ListView lv_gstorders;
    TextView tv_ord_status, tv_ord_no, tv_table_no, tv_odr_date, tv_ttl_amt;

    CustomTextview txtGuestNotify, tv_no_order;
    public OnBackPressInterface currentBackListener;
    public TakeOrderFragment takeOrderFragment;
    public StewardOrderFragment stewardOrderFragment;
    NetworkChangeReceiver receiver;
    public RelativeLayout layout_retry;
    String errorMsg = "";
    public int position = -1;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    protected SlidingUpPanelLayout slidingUpPanelLayout;
    ScheduledExecutorService statusScheduler;


    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public double currentLatitude;
    public double currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_fragment);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        context = this;

        ProgressBar progress_loading = (ProgressBar) findViewById(R.id.progressBar);
        progress_loading.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.home_color), PorterDuff.Mode.MULTIPLY);

        setupSimpleSlidingPane();
        controlNetStatus();
        showHome();

        initLocationAPI();
        //scheduleExecutors();
    }


    public boolean UserPermission(String menu_id) {

        String userID = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.TEMP_USER_ID);
        userID = userID.isEmpty() ? UserInfo.USER_ID : userID;
        PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.TEMP_USER_ID, "");

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
        mdb.beginTransaction();

        String queryPermission = "Select menu_value from "
                + DBConstants.KEY_PERMISSION + " WHERE "
                + DBConstants.KEY_PRM_USER_ID + "='" + userID + "' AND "
                + DBConstants.KEY_PRM_menu_id + "='" + menu_id + "'";

        Cursor cursor = mdb.rawQuery(queryPermission, null);

        if (cursor != null && cursor.moveToFirst()) {

            if (cursor.getString(0).equals("n")) {

                mdb.endTransaction();
                cursor.close();
                ShowPopupAccessDenied(menu_id);
                return true;

            } else {

                mdb.endTransaction();
                cursor.close();
                return false;
            }

        } else {

            mdb.endTransaction();
            return false;

        }
    }

    public void ShowPopupAccessDenied(final String userID) {

        AlertDialog.Builder objbuilder = new AlertDialog.Builder(context);

        AlertDialog.Builder builder = objbuilder.setTitle("Access Denied - ");
        builder.setMessage("Not Authorized User ID. Want to login as Authorized User ID ?");

        objbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });

        objbuilder.setNegativeButton("No",

                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();

        Button btn_yes = dialog.getButton(Dialog.BUTTON_POSITIVE);
        Button btn_no = dialog.getButton(Dialog.BUTTON_NEGATIVE);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                CustomLoginPopup customLoginPopup = new CustomLoginPopup(context, position, userID);
                customLoginPopup.show();
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
    }


    public void showOrderSplit() {
        FragmentManager fmOther = getSupportFragmentManager();
        FragmentTransaction transaction = fmOther.beginTransaction();

        OrderSplitFragment orderSplitFragment = new OrderSplitFragment();
        transaction.replace(R.id.container, orderSplitFragment);
        transaction.commit();
        currentBackListener = orderSplitFragment;

    }

    public void showHome() {

        if (NetworkUtil.getConnectivityStatus(this) != 0 && errorMsg.isEmpty()) {

            showGuestHome();
            showPendingNotificationOnStart();   // Set unread notifications so far.
            setupActionBar();
            updateUserStatus("A");  // To send Price change notification only to Active Users, User status is maintained.

        }
    }

    public void showStewardHome(int position) {

        if (position == 0)
            showGuestHome();

        else {

            slide_me.closeLeftSide();
            FragmentManager fmOther = getSupportFragmentManager();
            FragmentTransaction transaction = fmOther.beginTransaction();
            stewardOrderFragment = StewardOrderFragment.newInstance(position);
            transaction.replace(R.id.container, stewardOrderFragment);
            transaction.commit();
            currentBackListener = stewardOrderFragment;
            layout_retry.setVisibility(View.GONE);
        }
    }

    public void showGuestHome() {

        slide_me.closeLeftSide();
        img_srch.setVisibility(View.VISIBLE);
        layout_noti.setVisibility(View.VISIBLE);

        FragmentManager fmOther = getSupportFragmentManager();
        FragmentTransaction transaction = fmOther.beginTransaction();
        takeOrderFragment = new TakeOrderFragment();
        transaction.replace(R.id.container, takeOrderFragment);
        transaction.commit();
        currentBackListener = takeOrderFragment;
        layout_retry.setVisibility(View.GONE);
    }

    private void onExpandableListItemClick(int position) {

        if (!slide_me.isClosed()) {

            if (position == 7){
                slide_me.closeLeftSide();
                takeOrderFragment.showDiscountList();

            } else if (position == 12)
                UserInfo.showLogoutDialog(context);

            else
                showStewardHome(position);
        }
    }


    /* *************************** Expandable list Setup ******************************/

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    private void initExpandableListData() {

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        expListView.setVisibility(View.VISIBLE);
        listview_left.setVisibility(View.GONE);

        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                img_srch.setVisibility(groupPosition == 0 ? View.VISIBLE : View.GONE);
                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            int lastExpandedPosition = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();*/

                if (groupPosition == 1 || groupPosition == 2) {

                    if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition)
                        expListView.collapseGroup(lastExpandedPosition);
                    lastExpandedPosition = groupPosition;

                } else
                    onExpandableListItemClick(groupPosition);
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();*/

                if (groupPosition != 1 && groupPosition != 2)
                    onExpandableListItemClick(groupPosition);
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                // TODO Auto-generated method stub
                /*Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT).show();*/

                expListView.collapseGroup(groupPosition);
                int position = groupPosition == 1 ? 15 + childPosition : 18 + childPosition;
                onExpandableListItemClick(position);

                return false;
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        String[] listHeader = getResources().getStringArray(R.array.leftList_steward);
        listDataHeader = (Arrays.asList(listHeader));

        // Adding child data
        List<String> order = new ArrayList<>();
        order.add("Cancel");
        order.add("Modify");
        order.add("Split");
        //order.add("Transfer");

        List<String> bill = new ArrayList<>();
        bill.add("Cancel");
        bill.add("Modify");

        bill.add("Split");
        bill.add("Transfer");

        listDataChild.put(listDataHeader.get(1), order); // Header, Child data
        listDataChild.put(listDataHeader.get(2), bill);
    }

    /* ******************************************************************************/

    /**
     * NotificationFragment *
     */
    public void showNotificationFor(final String status) {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                FragmentManager manager = getSupportFragmentManager();
                NotificationFragment fragment = NotificationFragment.newInstance(
                        NotificationFragment.EMPLOYEE_TYPE2, status);
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container, fragment, StaticConstants.NOTIFICATION_TAG);
                transaction.commit();
                currentBackListener = fragment;

                img_srch.setVisibility(View.GONE);
            }
        }, 400);

    }

    public void showUnreadNotifications() {

        txtGuestNotify.setText("0");
        txtGuestNotify.setVisibility(View.GONE);
        PrefHelper.storeInt(context, PrefHelper.PREF_FILE_NAME, "pendingOrders", 0);

        myOrders();

/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                ActiveNotificationsFragment fragment = new ActiveNotificationsFragment();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                currentBackListener = fragment;
            }
        }, 200);*/
    }

    public void setupSimpleSlidingPane() {

        slide_me = new SimpleSideDrawer(this);
        slide_me.setAnimationDurationLeft(100);
        slide_me.setAnimationDurationRight(100);
        slide_me.setLeftBehindContentView(R.layout.left_menu);
        slide_me.setRightBehindContentView(R.layout.right_menu);
        listview_left = (ListView) findViewById(R.id.listView_left);

        if (PrefHelper.getStoredBoolean(context, PrefHelper.PREF_FILE_NAME, PrefHelper.STEWARD_LOGIN)) {
            //new LeftListOperator(context, listview_left);
            /*listview_left.setAdapter(getLeftListAdapterSteward());
            listview_left.setOnItemClickListener(this);*/
            initExpandableListData();
        } else {
            listview_left.setAdapter(getLeftListAdapter());
            listview_left.setOnItemClickListener(onListItemClickListener());

        }

        listview_right = (ListView) findViewById(R.id.lv_order_history);
        setRightListItem();

        UserInfo.guest_name = PrefHelper.getStoredString(getApplicationContext(), PrefHelper.PREF_FILE_NAME, PrefHelper.GUEST_NAME);
        UserInfo.guest_id = PrefHelper.getStoredString(getApplicationContext(), PrefHelper.PREF_FILE_NAME, PrefHelper.GUEST_ID);

        TextView tvGuest = (TextView) findViewById(R.id.tv_guest);
        tvGuest.setOnClickListener(this);
        if (!UserInfo.guest_name.isEmpty())
            tvGuest.setText(UserInfo.guest_name);


        home_icon_button = (ImageView) findViewById(R.id.left_button);
        right_button = (ImageView) findViewById(R.id.right_button);
        home_icon_button.setOnClickListener(this);
        right_button.setOnClickListener(this);

/*      img_lock = (ImageView) findViewById(R.id.img_lock);
        img_lock.setOnClickListener(this);
        img_unlock =  (ImageView) findViewById(R.id.img_unlock);
        img_unlock.setOnClickListener(this);*/

        layout_noti = (RelativeLayout) findViewById(R.id.layout_noti);
        layout_noti.setOnClickListener(this);
        txtGuestNotify = (CustomTextview) findViewById(R.id.tv_guest_notify);

    }


    public void setupActionBar() {

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingUpPanelLayout);
        image_back = (ImageButton) findViewById(R.id.image_back);
        image_back.setOnClickListener(this);

        img_srch = (ImageButton) findViewById(R.id.img_srch);
        img_srch.setOnClickListener(this);

        edit_search = (SearchView) findViewById(R.id.edit_search);
        edit_search.setQueryHint("Search");
        edit_search.setIconifiedByDefault(false);

        /* ***************** Logic to hide search hint icon ****************************/
        int imageId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView magImage = (ImageView) edit_search.findViewById(imageId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        /* *****************************************************************************/

        SearchItemListener searchListener = new SearchItemListener(context, takeOrderFragment, edit_search, img_srch);
        edit_search.setOnCloseListener(searchListener);
        edit_search.setOnQueryTextListener(searchListener);
        edit_search.clearFocus();
    }

    public void setRightListItem() {

        tv_no_order = (CustomTextview) findViewById(R.id.tv_no_order);
        ImageButton img_back = (ImageButton) findViewById(R.id.image_back_right);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slide_me.closeRightSide();
                edit_search.clearFocus();
            }
        });

        layout_gst_ord_detail = (LinearLayout) findViewById(R.id.layout_gst_ord_detail);
        lv_gstorders = (ListView) findViewById(R.id.lv_gstorders);
        tv_ord_status = (TextView) findViewById(R.id.tv_odr_staus);
        tv_ord_no = (TextView) findViewById(R.id.tv_ord_no);
        tv_table_no = (TextView) findViewById(R.id.tv_table_no);
        tv_odr_date = (TextView) findViewById(R.id.tv_odr_date);
        tv_ttl_amt = (TextView) findViewById(R.id.tv_ttl_amt);
    }

    public void setLeftListFooter() {

        MoreOptionsLayout moreOptionsLayout = new MoreOptionsLayout(context);
        LinearLayout ll_footer = (LinearLayout) findViewById(R.id.ll_footer);
        ll_footer.addView(moreOptionsLayout.addViewlayout());
    }


    public GridTitleAdapter getLeftListAdapter() {

        String[] stringOfArrayHeader = getResources().getStringArray(R.array.leftList_guest);
        ArrayList<TitleHeader> arrayListHeaders = new ArrayList<>(stringOfArrayHeader.length);

        for (String title : stringOfArrayHeader)
            arrayListHeaders.add(new TitleHeader(false, title));

        return new GridTitleAdapter(this, R.layout.group_row_layout, arrayListHeaders);
    }


    public GridTitleAdapterSteward getLeftListAdapterSteward() {

        String[] stringOfArrayHeader = context.getResources().getStringArray(R.array.leftList_steward);
        ArrayList<TitleHeader> arrayListHeaders = new ArrayList<>(stringOfArrayHeader.length);

        for (String title : stringOfArrayHeader)
            arrayListHeaders.add(new TitleHeader(false, title));

        return new GridTitleAdapterSteward(context, R.layout.group_row_layout, arrayListHeaders);
    }

    public GuestOrdersAdapter getRightListAdapter() {

        String prvsOdr = "";
        ArrayList<GuestOrders> ordersList = new ArrayList<>();

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
        mdb.beginTransaction();
        try {

            Cursor cursor = mdb.rawQuery("Select * from " + DBConstants.KEY_GUEST_ORDERS_TABLE + " order by order_no desc", null);

            if (cursor.moveToFirst()) {

                do {
                    if (!prvsOdr.equals(cursor.getString(1))) {
                        GuestOrders guestOrders = new GuestOrders();
                        guestOrders.setOrderNo(cursor.getString(1));
                        guestOrders.setTableNo(cursor.getString(2));
                        guestOrders.setItem(cursor.getString(4));
                        guestOrders.setPrice(cursor.getString(5));
                        guestOrders.setQty(cursor.getString(6));
                        guestOrders.setAmount(cursor.getString(8));
                        guestOrders.setOrder_status(cursor.getString(9));
                        guestOrders.setOrder_date(cursor.getString(10));
                        ordersList.add(guestOrders);

                    } else {
                        GuestOrders guestOrders = ordersList.get(ordersList.size() - 1);
                        float amount = Float.parseFloat(guestOrders.getAmount()) + cursor.getFloat(8);
                        guestOrders.setAmount(String.valueOf(amount));
                    }

                    prvsOdr = cursor.getString(1);


                } while (cursor.moveToNext());

                cursor.close();
            }
            cursor.close();
            mdb.setTransactionSuccessful();
            return new GuestOrdersAdapter(context, R.layout.guest_orders_list_layout, ordersList);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

        return null;
    }

    public GuestOrderItemsAdapter getRightListItemsAdapter(String odrNo) {

        ArrayList<GuestOrders> ordersList = new ArrayList<>();

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
        mdb.beginTransaction();
        try {

            float amount = 0;
            Cursor cursor = mdb.rawQuery("Select * from " + DBConstants.KEY_GUEST_ORDERS_TABLE +
                    " where " + DBConstants.KEY_ORDER_NUMBER + " = '" + odrNo + "'", null);

            if (cursor.moveToFirst()) {

                tv_ord_no.setText(getString(R.string.odr_string, cursor.getString(1)));
                tv_table_no.setText(getString(R.string.tbl_string, cursor.getString(2)));

                if (cursor.getString(10).length() >= 10)
                    tv_odr_date.setText(getString(R.string.date_string, cursor.getString(10).substring(0, 10)));

                tv_ttl_amt.setText(cursor.getString(8));
                String odrStatus = cursor.getString(9);

                if (odrStatus.equals(NotificationFragment.ORDER_REJECTED_BY_GUEST) ||
                        odrStatus.equals(NotificationFragment.ORDER_REJECTED_BY_STEWARD) ||
                        odrStatus.equals(NotificationFragment.ORDER_REJECTED_BY_ADMIN)) {
                    tv_ord_status.setText(getString(R.string.sts_string, "REJECTED"));
                    tv_ord_status.setBackgroundColor(0xFFFF3000);

                } else if (odrStatus.equals(NotificationFragment.ORDER_ACCEPTED) ||
                        odrStatus.equals(NotificationFragment.ORDER_PREPARATION_STARTED) ||
                        odrStatus.equals(NotificationFragment.ORDER_READY_TO_SERVE)) {
                    tv_ord_status.setText(getString(R.string.sts_string, "APPROVED"));
                    tv_ord_status.setBackgroundColor(0xFF00D938);

                } else if (odrStatus.equals(NotificationFragment.ORDER_UNDER_PROCESS)) {
                    tv_ord_status.setText(getString(R.string.sts_string, "WAITING FOR APPROVAL"));
                    tv_ord_status.setBackgroundColor(0xFFFFB400);

                } else if (odrStatus.equals(NotificationFragment.ORDER_DELIVERED)) {
                    tv_ord_status.setText(getString(R.string.sts_string, "DELIVERED"));
                    tv_ord_status.setBackgroundColor(0xFF00FFFF);
                }

                do {
                    GuestOrders guestOrders = new GuestOrders();
                    guestOrders.setOrderNo(cursor.getString(1));
                    guestOrders.setTableNo(cursor.getString(2));
                    guestOrders.setItem(cursor.getString(4));
                    guestOrders.setPrice(cursor.getString(5));
                    guestOrders.setQty(cursor.getString(6));
                    amount += cursor.getFloat(8);
                    guestOrders.setAmount(cursor.getString(8));
                    guestOrders.setOrder_status(cursor.getString(9));
                    guestOrders.setOrder_date(cursor.getString(10));

                    ordersList.add(guestOrders);

                } while (cursor.moveToNext());

                cursor.close();
            }
            tv_ttl_amt.setText(String.format(Locale.US, "₹ %.2f", amount) + "\n*Taxes Applicable");
            tv_ttl_amt.setTextSize(14);
            tv_ttl_amt.setGravity(Gravity.RIGHT);
//**//

            cursor.close();
            mdb.setTransactionSuccessful();
            return new GuestOrderItemsAdapter(context, R.layout.items_row_layout, ordersList);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

        return null;
    }

    public AdapterView.OnItemClickListener onListItemClickListener() {

        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!slide_me.isClosed()) {

                    if (position != 1 && position != 11)
                        slide_me.closeLeftSide();

                    //for hiding search view
                    img_srch.setVisibility(position == 3 || position == 5 || position == 8
                            || position == 9 || position == 10 ? View.GONE : View.VISIBLE);
                    layout_noti.setVisibility(position == 3 || position == 5 || position == 8
                            || position == 9 || position == 10 ? View.GONE : View.VISIBLE);

                    switch (position) {

                        case 0:
                            takeOrderFragment.showHome();
                            break;

                        case 1:
                            break;

                        case 2:

                            try {

/*                            // Pin Pointing address on Map
                            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 28.66, 77.228);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            context.startActivity(intent);

                            // Direction for Source location to Destination
                            Intent intent2 = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
                            startActivity(intent2);

                            // Direction for Current location to Destination
                            Intent intent3 = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?daddr=20.5666,45.345"));
                            startActivity(intent3);*/

                                String address = "SCO 395 Sector 8, Panchkula, India";
                                Uri uri = Uri.parse("https://www.google.com/maps/place/" + address);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);

                           /* // Showing Map for Address
                            Intent intent4 = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("google.navigation:q=an+address+city"));
                            startActivity(intent4);


                            String name = "Csat";
                            Intent intent5 = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("geo:0,0?q=37.423156,-122.084917 (" + name + ")"));
                            startActivity(intent5);*/

                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }

                            break;

                        case 3:
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    NewsFragment newsFragment = new NewsFragment();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.replace(R.id.container, newsFragment).commit();
                                    currentBackListener = newsFragment;


                                }
                            }, 200);
                            break;

                        case 4:
                            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                            // To count with Play market backstack, After pressing back button,
                            // to taken back to our application, we need to add following flags to intent.
                            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            try {
                                startActivity(goToMarket);
                            } catch (ActivityNotFoundException e) {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                            }
                            break;


                        case 5:
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    AboutUsFragment aboutUsFragment = new AboutUsFragment();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.replace(R.id.container, aboutUsFragment).commit();
                                    currentBackListener = aboutUsFragment;

                                }
                            }, 200);
                            break;


                        case 6:
                            Uri uri2 = Uri.parse("https://www.facebook.com/cheekymonkeypkl/");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri2);
                            startActivity(intent);
                            break;


                        case 7:
                            Uri uri1 = Uri.parse("https://www.instagram.com/cheekymonkeychd/");
                            Intent intent1 = new Intent(Intent.ACTION_VIEW, uri1);
                            startActivity(intent1);
                            break;

                        case 8:

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    DocsFragment docsFragment = new DocsFragment();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.replace(R.id.container, docsFragment).commit();
                                    currentBackListener = docsFragment;


                                }
                            }, 200);
                            break;


                        case 9:

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    Help_Layout help_layout = new Help_Layout();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.replace(R.id.container, help_layout).commit();
                                    currentBackListener = help_layout;


                                }
                            }, 200);
                            break;


                        case 10:
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    AboutDeveloperFragment devFragment = new AboutDeveloperFragment();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.replace(R.id.container, devFragment).commit();
                                    currentBackListener = devFragment;


                                }
                            }, 200);
                            break;


                        case 11:
                            UserInfo.showLogoutDialog(context);
                            break;
                    }
                }
            }
        };
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.lv_order_history) {
            layout_gst_ord_detail.setVisibility(View.VISIBLE);
            GuestOrders guestOrders = (GuestOrders) listview_right.getItemAtPosition(position);
            GuestOrderItemsAdapter itemsAdapter = getRightListItemsAdapter(guestOrders.getOrderNo());
            lv_gstorders.setAdapter(itemsAdapter);

        } else
            onExpandableListItemClick(position);
    }

    public boolean collapseToolbar() {

       /* if (!searchView.isIconified()){
            searchView.onActionViewCollapsed();
            layoutTopLeft.setVisibility(View.VISIBLE);
            return true;
        }*/

        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.left_button:
                slide_me.toggleLeftDrawer();
                break;


            case R.id.tv_guest:
                slide_me.closeLeftSide();
                break;


            case R.id.img_srch:
                showInputMethod();
                break;

            case R.id.image_back:
                showHomeItemList();
                break;

 /*           case R.id.img_unlock:
                img_unlock.setVisibility(View.GONE);
                img_lock.setVisibility(View.VISIBLE);
                UserInfo.lock = true;
                break;


            case R.id.img_lock:
                img_lock.setVisibility(View.GONE);
                img_unlock.setVisibility(View.VISIBLE);
                UserInfo.lock = false;
                break;


            case R.id.img_refresh:
                Animation rotation = AnimationUtils.loadAnimation(context, R.anim.clockwise_refresh);
                rotation.setRepeatCount(Animation.INFINITE);
                img_refresh.startAnimation(rotation);

                ItemsFragment fragment = takeOrderFragment.adapter.fragment;
                fragment.doRefreshItems();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        img_refresh.clearAnimation();
                    }
                },2000);

                break;*/


            case R.id.layout_noti:

                if (PrefHelper.getStoredBoolean(context, PrefHelper.PREF_FILE_NAME, PrefHelper.STEWARD_LOGIN)) {
                    showNotificationFor(NotificationFragment.TYPE_UNDER_PROCESS);
                } else {
                    showHomeItemList();
                    showUnreadNotifications();
                }
                break;


            case R.id.right_button:
                myOrders();
                break;
        }
    }

    public void myOrders() {

        if (PrefHelper.getStoredBoolean(context, PrefHelper.PREF_FILE_NAME, PrefHelper.STEWARD_LOGIN))
            showNotificationFor(NotificationFragment.TYPE_UNDER_PROCESS);

        else {
            listview_right.setAdapter(getRightListAdapter());
            listview_right.setOnItemClickListener(this);
            slide_me.toggleRightDrawer();
            tv_no_order.setVisibility(getRightListAdapter().getCount() == 0 ? View.VISIBLE : View.GONE);
        }
    }


    public void showHomeItemList() {

        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        slidingUpPanelLayout.clearFocus();

//        takeOrderFragment.adapter_menu_search.clear();
//        takeOrderFragment.listViewSearchItem.setVisibility(View.GONE);
        image_back.setVisibility(View.GONE);
        edit_search.setQuery("", false);
        edit_search.clearFocus();
        hideSoftKeyboard(this);
    }

    private void showInputMethod() {

        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        image_back.setVisibility(View.VISIBLE);
        edit_search.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void onCreateBillGenerate(final String table, final String tableCode) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                FragmentManager fmOther = getSupportFragmentManager();
                FragmentTransaction transaction = fmOther.beginTransaction();
                BillGenerateFragment billGenerateFragment = new BillGenerateFragment();
                billGenerateFragment.table = table;
                billGenerateFragment.tableCode = tableCode;
                transaction.replace(R.id.container, billGenerateFragment);
                transaction.commit();
                currentBackListener = billGenerateFragment;

            }
        }, 200);


    }

    /* ********************************** Google Location API ********************************* */


    public void initLocationAPI() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(20 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    public void updateUserStatus(String status) {

        if (!UserInfo.guest_id.isEmpty()) {

            UserInfo.appIsRunning = true;
            String parameter = UtilToCreateJSON.createGuestStatus(UserInfo.guest_id, status);   // status A means User is Active
            String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
            ActivateUserIDTask activateUserIDTask = new ActivateUserIDTask(context, parameter, serverIP);
            activateUserIDTask.execute();
        } else
            UserInfo.appIsRunning = false;
    }


    /**
     * If connected get lat and long
     */
    @Override
    public void onConnected(Bundle bundle) {


        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context, new String[]{com.entrada.cheekyMonkey.Manifest.permission.
                    ACCESS_FINE_LOCATION, com.entrada.cheekyMonkey.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_LOCATION);

        } else {

            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            } else {
                //If everything went fine lets get latitude and longitude
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();

                if (takeOrderFragment != null) {
                    takeOrderFragment.latitude = currentLatitude;
                    takeOrderFragment.longitude = currentLongitude;
                }

                String loc = "Location detail : " + currentLatitude + " :: " + currentLongitude;
                //Toast.makeText(this, loc, Toast.LENGTH_LONG).show();
                //Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        if (takeOrderFragment != null) {
            takeOrderFragment.latitude = currentLatitude;
            takeOrderFragment.longitude = currentLongitude;
        }

        String loc = "Location detail : " + currentLatitude + "  ::  " + currentLongitude;
        //Toast.makeText(this, loc, Toast.LENGTH_LONG).show();
        //Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }


    /* *************************************************************************************** */

    @Override
    public void onBackPressed() {

        if (layout_gst_ord_detail.getVisibility() == View.VISIBLE)
            layout_gst_ord_detail.setVisibility(View.GONE);

        else if (image_back.getVisibility() == View.VISIBLE)
            showHomeItemList();

        else if (!slide_me.isClosed()) {

            LinearLayout layoutLeft = (LinearLayout) slide_me.getLeftBehindView().getParent();
            if (layoutLeft.getVisibility() == View.VISIBLE)
                slide_me.closeLeftSide();
            else {
                slide_me.closeRightSide();
                edit_search.clearFocus();
            }
        } else if (!(currentBackListener instanceof TakeOrderFragment
                || currentBackListener instanceof StewardOrderFragment))

            showGuestHome();

        else if (!currentBackListener.onBackPress()) {
            ExitDialog exitDialog = new ExitDialog(this, this);
            exitDialog.show();
        }
    }

    @Override
    public void onFinishDialog() {

        if (Build.VERSION.SDK_INT >= 16)
            this.finishAffinity();
        else
            finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onDestroy() {

        try {

            if (statusScheduler != null && !statusScheduler.isShutdown())
                statusScheduler.shutdownNow();

            if (receiver != null) {
                unregisterReceiver(receiver);
                receiver = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }


    public void controlNetStatus() {

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");

        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

        layout_retry = (RelativeLayout) findViewById(R.id.layout_retry);
        TextView tv_retry = (TextView) findViewById(R.id.tv_retry);
        tv_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHome();
            }
        });

    }

    public void showOutletDirection() {

        // To start from source location to destination location

        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)", 28.66f, 76.2867, "Home Sweet Home", 12f, 2f, "Where the party is at");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);


        // To start from current location to destination
        String uri1 = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", 12f, 2f, "Where the party is at");
        Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(uri1));
        intent1.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }


    public final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    public final int REQUEST_ACCESS_LOCATION = 101;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {

            case REQUEST_WRITE_EXTERNAL_STORAGE:

                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "failure", Toast.LENGTH_SHORT).show();
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                break;

            case REQUEST_ACCESS_LOCATION:
                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "failure", Toast.LENGTH_SHORT).show();
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                break;
        }
    }


    public void logOut(boolean closeDrawer) {

        if (closeDrawer)
            slide_me.toggleLeftDrawer();
        if (slidingUpPanelLayout != null)
            slidingUpPanelLayout.setVisibility(View.GONE);
/*
        FragmentManager fmOther = getSupportFragmentManager();
        FragmentTransaction transaction = fmOther.beginTransaction();
        MainScreenFragment fragment = new MainScreenFragment();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
*/

        PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.GUEST_NAME, "");
        PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.GUEST_ID, "");

        PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                PrefHelper.FIRST_TIME_LUNCHAPP, PrefHelper.LOGGED_OUT);
        PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                PrefHelper.ADMIN_LOGIN, PrefHelper.LOGGED_OUT);
        PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                PrefHelper.KITCHEN_LOGIN, PrefHelper.LOGGED_OUT);
        PrefHelper.storeBoolean(context, PrefHelper.PREF_FILE_NAME,
                PrefHelper.STEWARD_LOGIN, false);


        Intent intent = new Intent(BaseFragmentActivity.this, IntroductionScreen.class);
        startActivity(intent);
        finish();
    }

    public void refreshPendingOrders(int orders) {

        if (orders > 0) {
            txtGuestNotify.setText(String.valueOf(orders));
            txtGuestNotify.setVisibility(View.VISIBLE);

        } else {
            txtGuestNotify.setText(String.valueOf(orders));
            txtGuestNotify.setVisibility(View.GONE);
        }
    }

    public void showPendingNotificationOnStart() {

        int orders = PrefHelper.getStoredInt(context, PrefHelper.PREF_FILE_NAME, "pendingOrders");
        if (orders > 0) {
            txtGuestNotify.setText(String.valueOf(orders));
            txtGuestNotify.setVisibility(View.VISIBLE);
        }
    }

    public void sendNotification(String text) {

        setPendingNotification();
        NotificationManager mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // In this sample, we'll use the same text for the ticker and the expanded notification
        //CharSequence text = getText(R.string.STATUS_STRING);

        // The PendingIntent to launch our activity if the user selects this notification
        //PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
        //        new Intent(this, LocalServiceActivities.Controller.class), 0);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, BaseFragmentActivity.class), PendingIntent.FLAG_ONE_SHOT);

        Uri soundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= 16) {

            // Set the info for the views that show in the notification panel.
            Notification notification = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.app_icon)  // the status icon
                    .setTicker(text)  // the status text
                    .setWhen(System.currentTimeMillis())  // the time stamp
                    .setContentTitle(getText(R.string.app_name))  // the label of the entry
                    .setContentText(text)  // the contents of the entry
                    .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                    .setSound(soundURI)
                    //.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    //.setSound(Uri.parse("uri://sadfasdfasdf.mp3"))
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setLights(Color.RED, 3000, 3000)
                    .build();

            // Send the notification.
            // We use a string id because it is a unique number.  We use it later to cancel.
            mNM.notify(R.string.remote_service_started, notification);

        } else {
            Notification notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.app_icon)  // the status icon
                    .setTicker(text)  // the status text
                    .setWhen(System.currentTimeMillis())  // the time stamp
                    .setContentTitle(getText(R.string.app_name))  // the label of the entry
                    .setContentText(text)  // the contents of the entry
                    .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                    .setSound(soundURI)
                    //.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    //.setSound(Uri.parse("uri://sadfasdfasdf.mp3"))
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setLights(Color.RED, 3000, 3000)
                    .build();

            mNM.notify(0, notification);
        }

    }

    public void setPendingNotification() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                String noty = txtGuestNotify.getText().toString();
                if (noty.length() > 0) {
                    int n = Integer.parseInt(noty) + 1;
                    txtGuestNotify.setText(String.valueOf(n));
                    txtGuestNotify.setVisibility(View.VISIBLE);
                    PrefHelper.storeInt(context, PrefHelper.PREF_FILE_NAME, "pendingOrders", n);
                } else {
                    txtGuestNotify.setText("1");
                    txtGuestNotify.setVisibility(View.VISIBLE);
                    PrefHelper.storeInt(context, PrefHelper.PREF_FILE_NAME, "pendingOrders", 1);
                }
                // .setBackgroundColor(BaseFragmentActivity.this.getResources().getColor(R.color.transparent_dar_red));

            }
        });

    }



    /* ************** Complete flow to Get Items Current Price **********************/

    public void scheduleExecutors() {

        statusScheduler = Executors.newSingleThreadScheduledExecutor();
        statusScheduler.scheduleWithFixedDelay(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                // Hit WebService

                Logger.i("FetchCurrentPrice >>>>>>>>>>>", " Running");
                refreshItemPrice();

            }
        }, 30, 20, TimeUnit.SECONDS);
    }


    public void refreshItemPrice() {

        String parameter = UtilToCreateJSON.createToken();
        String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
        FetchAndStoreMenuItemsTask menuItemsTask = new FetchAndStoreMenuItemsTask(
                context, parameter, serverIP, null, this);
        menuItemsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void getResponseFromServer(String response) {

        layout_retry.setVisibility(response.equals("success1") ? View.GONE : View.VISIBLE);
        errorMsg = response.equals("success1") ? "" : response;
    }

    /* ************************************************************************** */

}