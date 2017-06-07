package com.entrada.cheekyMonkey.dynamic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.addons.Add_Mixer_Fragment;
import com.entrada.cheekyMonkey.appInterface.IAsyncTaskRunner;
import com.entrada.cheekyMonkey.appInterface.OnBackPressInterface;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.dynamic.gpsTracker.GPSTracker;
import com.entrada.cheekyMonkey.entity.CategoryItem;
import com.entrada.cheekyMonkey.entity.GroupItems;
import com.entrada.cheekyMonkey.entity.Items;
import com.entrada.cheekyMonkey.entity.MenuItem;
import com.entrada.cheekyMonkey.entity.POSItem;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.staticData.PrefHelper;
import com.entrada.cheekyMonkey.staticData.ResultMessage;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.steward.discount.AutoDiscount;
import com.entrada.cheekyMonkey.steward.discount.DiscountLayout;
import com.entrada.cheekyMonkey.steward.discount.ICallDiscList;
import com.entrada.cheekyMonkey.steward.order.ICallBackSendOrderResponse;
import com.entrada.cheekyMonkey.steward.order.SendOrderTask;
import com.entrada.cheekyMonkey.takeorder.ICallTable;
import com.entrada.cheekyMonkey.takeorder.ItemsAdapter;
import com.entrada.cheekyMonkey.takeorder.OnMenuItemClick;
import com.entrada.cheekyMonkey.takeorder.TableNumberLayout;
import com.entrada.cheekyMonkey.takeorder.adapter.TakeOrderAdapter;
import com.entrada.cheekyMonkey.takeorder.entity.OrderDetail;
import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;
import com.entrada.cheekyMonkey.takeorder.entity.TableItem;
import com.entrada.cheekyMonkey.takeorder.popup.ICallOrder;
import com.entrada.cheekyMonkey.task.GuestCommonTask;
import com.entrada.cheekyMonkey.ui.CustomAsynctaskLoader;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.ui.MySlidingPaneLayout;
import com.entrada.cheekyMonkey.ui.PagerSlidingTabStrip;
import com.entrada.cheekyMonkey.util.AsyncTaskTools;
import com.entrada.cheekyMonkey.util.Logger;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rahul on 17/05/2016.
 */
public class TakeOrderFragment extends Fragment implements
        IAsyncTaskRunner, View.OnClickListener,
        OnMenuItemClick.ICallMenuPopup, ICallOrder, ICallSendNotification,
        ICallTable, ICallDiscList, ICallBackSendOrderResponse, OnBackPressInterface {

    public Context context;
    public ArrayList<Items> TITLES = new ArrayList<>();
    public TakeOrderAdapter takeOrderAdapter;
    public TableNumberLayout tableNumberLayout;
    public ListView listViewOrderItem, listViewSearchItem;
    public FrameLayout frameLayout_container, frameLayout_discount;
    public LinearLayout layoutBanner;
    public ArrayList<String> codeList = new ArrayList<>();
    TableItem tableItem;
    public DiscountLayout discountLayout;
    AutoDiscount autoDiscount;

    private PagerSlidingTabStrip tabs;
    public ViewPager pager;
    public MyPagerAdapter adapter;
    MySlidingPaneLayout sliding_pane_layout;

    public String orderRemark = "", steward = "", order_type = "", cover = "";
    CustomTextview selectTable, txtOrderSubmit, tv_no_order, tv_total_amt, tv_home, tv_myOrders;
    LinearLayout layoutHeader;

    ArrayList<String> orderList;
    ScheduledExecutorService statusScheduler;

    public ItemsAdapter adapter_menu_search;
    double latitude = 0, longitude = 0, min_latitude = 0, max_latitude = 0, min_longitude = 0, max_longitude = 0;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.dyn_take_order_fragment, container, false);
            init(view);

            SelectPOS(new POSItem());
            getMenuItems();
        }

        return view;
    }

    private void init(View v) {

        ProgressBar progress_loading = (ProgressBar) v.findViewById(R.id.progress_order);
        progress_loading.getIndeterminateDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);

        layoutBanner = (LinearLayout) v.findViewById(R.id.layout_progress);
        sliding_pane_layout = (MySlidingPaneLayout) v.findViewById(R.id.sliding_pane_layout);
        sliding_pane_layout.setParallaxDistance(200);
        sliding_pane_layout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelOpened(View panel) {


            }

            @Override
            public void onPanelClosed(View panel) {

                showHome();
            }
        });

        layoutHeader = (LinearLayout) v.findViewById(R.id.tv_header);
        tv_no_order = (CustomTextview) v.findViewById(R.id.tv_no_order);
        listViewOrderItem = (ListView) v.findViewById(R.id.listViewOrderItem);
        selectTable = (CustomTextview) v.findViewById(R.id.selectTable);
        txtOrderSubmit = (CustomTextview) v.findViewById(R.id.txtOrderSubmit);
        tv_total_amt = (CustomTextview) v.findViewById(R.id.ttl_amt);
        tv_home = (CustomTextview) v.findViewById(R.id.tv_home);
        tv_myOrders = (CustomTextview) v.findViewById(R.id.tv_myOrders);

        selectTable.setOnClickListener(this);
        txtOrderSubmit.setOnClickListener(this);
        tv_home.setOnClickListener(this);
        tv_myOrders.setOnClickListener(this);

        discountLayout = new DiscountLayout(context, this);
        takeOrderAdapter = new TakeOrderAdapter(context, this, discountLayout);
        listViewOrderItem.setAdapter(takeOrderAdapter);
        autoDiscount = new AutoDiscount(context, discountLayout, takeOrderAdapter);

        tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        pager = (ViewPager) v.findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getChildFragmentManager());

        final int pageMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        pager.setPageMargin(pageMargin);


        frameLayout_discount = (FrameLayout) v.findViewById(R.id.frameLayout_discount);
        frameLayout_container = (FrameLayout) v.findViewById(R.id.frameLayout_container);
        tableNumberLayout = new TableNumberLayout(context, frameLayout_container.getWidth(), this);

        min_latitude = PrefHelper.getStoredDouble(context, PrefHelper.PREF_FILE_NAME, PrefHelper.MIN_LATITUDE);
        max_latitude = PrefHelper.getStoredDouble(context, PrefHelper.PREF_FILE_NAME, PrefHelper.MAX_LATITUDE);
        min_longitude = PrefHelper.getStoredDouble(context, PrefHelper.PREF_FILE_NAME, PrefHelper.MIN_LONGITUDE);
        max_longitude = PrefHelper.getStoredDouble(context, PrefHelper.PREF_FILE_NAME, PrefHelper.MAX_LONGITUDE);

        ImageButton img_back = (ImageButton) v.findViewById(R.id.image_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (takeOrderAdapter.getCount() > 0)
                    showMixer();
                else
                    showHomeScreen();

            }
        });

        listViewSearchItem = (ListView) v.findViewById(R.id.lv_search_Item);
        ArrayList<Items> menu_search_list = new ArrayList<>();
        adapter_menu_search = new ItemsAdapter(context, R.layout.item_layout_row, menu_search_list);
        listViewSearchItem.setAdapter(adapter_menu_search);
        listViewSearchItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listViewOrderItem.setVisibility(View.VISIBLE);
                listViewSearchItem.setVisibility(View.GONE);

                Items items = (Items) parent.getItemAtPosition(position);
                //onclickEvent(items.getMenuItem());
                showQtyPopup(items.getMenuItem());
                ((BaseFragmentActivity) context).showHomeItemList();

            }
        });


        UserInfo.lock = PrefHelper.getStoredBoolean(context, PrefHelper.PREF_FILE_NAME, PrefHelper.DRAWER_LOCK_MODE);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                frameLayout_container.setVisibility(View.GONE);
                POSItem posItem = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem();

                // show restaurants things
                OrderDetail detail = takeOrderAdapter.getOrderDetail();
                detail.setPosItem(posItem);
                takeOrderAdapter.setOrderDetail(detail);

            }
        }, 100);

    }

    public void showQtyPopup(final MenuItem menuItem) {

        String[] qty = new String[15];

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_DARK);
        //AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_TRADITIONAL);
        //AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_quantity, null);

        ListView listview_qty = (ListView) view.findViewById(R.id.lv_qty);
        ArrayAdapter<String> adapter_qty = new ArrayAdapter<String>(context, R.layout.list_qty_layout, qty);

        for (int i = 0; i < 15; i++)
            qty[i] = i + 1 + "";

        listview_qty.setAdapter(adapter_qty);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
/*        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //do whatever you want the back key to do
                dialog.dismiss();
            }
        });*/

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


/*        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = 400;
        lp.height = 530;
        dialog.getWindow().setAttributes(lp);*/


        listview_qty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                setQuantity(menuItem, position + 1);
                dialog.dismiss();
                //addMixer();
            }
        });
    }

    public void addMixer() {

        frameLayout_container.setVisibility(View.VISIBLE);
        Add_Mixer_Fragment addMixerFragment = new Add_Mixer_Fragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout_container, addMixerFragment);
        transaction.commit();
        sliding_pane_layout.openPane();
    }

    public void setQuantity(MenuItem menuItem, int quantity) {
        menuItem.setQuantity(quantity);
        menuItem.setMenuAmount(menuItem.getMenu_price() * (quantity));
        //onclickEvent(menuItem);    // on MenuItem click
        showMultiQty(menuItem);

    }

    public void updateStatusPendingOrders() {

        orderList = new ArrayList<>();

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
        mdb.beginTransaction();

        try {
            Cursor cursor = mdb.rawQuery("Select * from " + DBConstants.KEY_GUEST_ORDERS_TABLE +
                    " where " + DBConstants.KEY_GUEST_ORDER_STATUS + "= 'K' ", null);

            if (cursor.moveToFirst()) {

                do {
                    orderList.add(cursor.getString(1));
                } while (cursor.moveToNext());

            }

            cursor.close();
            mdb.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

        if (!orderList.isEmpty())
            scheduleExecutors(orderList.get(0));

    }

    public void onclickEvent(MenuItem menuItem) {

        if (listViewOrderItem.getVisibility() != View.VISIBLE)
            showDefault();

        OnMenuItemClick itemClick = new OnMenuItemClick(context, menuItem, takeOrderAdapter,
                this, discountLayout, autoDiscount);

        itemClick.showSelection();
        updateScrolling();
        showTotalAmount();

        if (!UserInfo.lock)
            showOrderItemList();
    }

    public void showMultiQty(MenuItem menuItem) {

        if (!UserInfo.lock)
            showOrderItemList();

        String menuCode = menuItem.getMenu_code();

        if (listViewOrderItem.getVisibility() != View.VISIBLE)
            showDefault();

        if (codeList.contains(menuCode)) {
            //takeOrderAdapter.addQty(codeList.indexOf(menuCode));
            //takeOrderAdapter.addMultiQty(menuItem, codeList.indexOf(menuCode));

            ArrayList<OrderItem> itemArrayList = takeOrderAdapter.dataSet;
            for (int i = 0; i < itemArrayList.size(); i++) {
                OrderItem orderItem = itemArrayList.get(i);
                if (orderItem.getO_code().equals(menuItem.getMenu_code())) {
                    takeOrderAdapter.updateQty(i, menuItem.getQuantity());
                    showMixer();
                    break;
                }

            }

            takeOrderAdapter.updateQty(codeList.indexOf(menuCode), menuItem.getQuantity());

        } else {
            onclickEvent(menuItem);
            codeList.add(menuItem.getMenu_code());
        }

    }

    public void updateScrolling() {
        if (takeOrderAdapter != null && takeOrderAdapter.getCount() > 0)
            listViewOrderItem.smoothScrollToPosition(takeOrderAdapter.getCount() - 1);
    }


    public void showTotalAmount() {

        float totalAmt = 0;
        for (OrderItem orderItem : takeOrderAdapter.getDataSet())
            totalAmt = totalAmt + orderItem.getO_amount();

        String amount = getString(R.string.rupees, "₹", String.valueOf(totalAmt));
        tv_total_amt.setText(amount);
        boolean result1 = takeOrderAdapter.getCount() > 0;

        if (result1) {
            selectTable.setEnabled(true);
        } else {
            selectTable.setEnabled(false);
            txtOrderSubmit.setBackgroundResource(R.drawable.button_state);
            txtOrderSubmit.setTextColor(Color.WHITE);
        }
        boolean result = !selectTable.getText().equals(getString(R.string.slct_tbl_string)) && takeOrderAdapter.getCount() > 0;

        txtOrderSubmit.setEnabled(result);

        if (result) {
            txtOrderSubmit.setBackgroundResource(R.drawable.button_state1);
            txtOrderSubmit.setTextColor(Color.BLUE);
        } else {
            txtOrderSubmit.setBackgroundResource(R.drawable.button_state);
            txtOrderSubmit.setTextColor(Color.WHITE);
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onClick(View v) {

        OrderDetail detail = takeOrderAdapter.getOrderDetail();
        detail.setTableItem(tableItem);
        takeOrderAdapter.setOrderDetail(detail);

        switch (v.getId()) {


            case R.id.selectTable:      // called when Table View is clicked, displays a list of all tables via GridView.
                getAllTables();
                break;


            case R.id.txtOrderSubmit:

                if (PrefHelper.getStoredBoolean(context, PrefHelper.PREF_FILE_NAME, PrefHelper.STEWARD_LOGIN)) {

                    if (UserInfo.guest_id.isEmpty() || UserInfo.guest_name.isEmpty()) {
                        showHome();
                        UserInfo.showLoginDialog(context);
                    } else if (selectTable.getText().equals(getString(R.string.slct_tbl_string))) {
                        getAllTables();
                    }
                    //**//
                    else if (!takeOrderAdapter.isEmpty())
                        submitStewardOrder();
                    else
                        Toast.makeText(context, "Add any item", Toast.LENGTH_SHORT).show();

                } else {
                    //UserInfo.showAccessDeniedDialog(context, getString(R.string.order_restriction));
                    //submitOrder();
                    submitOrderWithoutValidation();
                }

                break;


            case R.id.tv_home:
                //showHome();
                showOrderReview();
                break;


            case R.id.tv_myOrders:
                //showOrderItemList();
                ((BaseFragmentActivity) context).myOrders();
                break;
        }
    }


    public void showHomeScreen() {

        sliding_pane_layout.closePane();
        layoutHeader.setVisibility(View.VISIBLE);
        frameLayout_container.setVisibility(View.GONE);
    }

    public void showOrderReview() {

        sliding_pane_layout.openPane();
        layoutHeader.setVisibility(View.VISIBLE);
        frameLayout_container.setVisibility(View.GONE);
        frameLayout_discount.setVisibility(View.GONE);
    }

    public void showMixer() {

        sliding_pane_layout.openPane();
        layoutHeader.setVisibility(View.GONE);
        frameLayout_container.setVisibility(View.VISIBLE);
        frameLayout_discount.setVisibility(View.GONE);
    }

    public void submitOrder() {

        if (UserInfo.guest_id.isEmpty() || UserInfo.guest_name.isEmpty()) {
            showHome();
            UserInfo.showLoginDialog(context);

        } else if (latitude == 0 || longitude == 0)
            getLocationOfCurrentAddress();

        else if (selectTable.getText().equals(getString(R.string.slct_tbl_string)))
            getAllTables();

        else if ((28.45 <= latitude && latitude <= 28.46) &&
                (77.06 <= longitude && longitude <= 77.07)) {

            if (!takeOrderAdapter.isEmpty())

                showConfirmOrderDialog();
            //  placeGuestOrderMethod(UserInfo.guest_id, UserInfo.guest_name);

        } else
            UserInfo.showAccessDeniedDialog(context, getString(R.string.not_in_location));
    }

    public void submitOrderWithoutValidation() {

        if (UserInfo.guest_id.isEmpty() || UserInfo.guest_name.isEmpty()) {
            showHome();
            UserInfo.showLoginDialog(context);

        } else if (selectTable.getText().equals(getString(R.string.slct_tbl_string)))
            getAllTables();

        else if (!takeOrderAdapter.isEmpty())
            showConfirmOrderDialog();
        //  placeGuestOrderMethod(UserInfo.guest_id, UserInfo.guest_name);
    }

    public void showConfirmOrderDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.exit_layout, null);
        TextView ctv_title = (TextView) view.findViewById(R.id.ctv_title);
        TextView ctv_message = (TextView) view.findViewById(R.id.tv_msg);
        CustomTextview yes = (CustomTextview) view.findViewById(R.id.tv_yes);
        CustomTextview no = (CustomTextview) view.findViewById(R.id.tv_cancel);

        ctv_title.setText(context.getText(R.string.confirm_order_title));
        ctv_message.setText(context.getString(R.string.confirm_order_msg));

        builder.setView(view);
        final AlertDialog dialog = builder.create();


        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                placeGuestOrderMethod(UserInfo.guest_id, UserInfo.guest_name);
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


    public void submitStewardOrder() {

        try {

            String parameter = UtilToCreateJSON.createSendSyncJSON(
                    context, takeOrderAdapter, order_type, orderRemark, UserInfo.guest_id,
                    "a", cover, discountLayout, "");
            String serverIP = UserInfo.ServerIP;
            if (!(TextUtils.isEmpty(parameter) && TextUtils.isEmpty(serverIP))) {

                SendOrderTask sendOrderTask = new SendOrderTask(context, parameter, serverIP, this);
                sendOrderTask.execute();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void responseFromServer(String response) {

        if (!response.isEmpty()) {

            String posType = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posType;
            boolean isSuccess = false;

            if (posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_R))
                isSuccess = UtilToCreateJSON.parseTakeOrderResponse(
                        context, tableItem.getName(), tableItem.getCode(), response, this);

            if (isSuccess) {

                //selectTable.setText(getResources().getString(R.string.table_string));
                discountLayout.clearDiscList();
                takeOrderAdapter.clearDataSet();
                takeOrderAdapter.notifyDataSetChanged();
                codeList.clear();
                cover = "";
                order_type = "";
            }

        } else
            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onBackPress() {

        if (sliding_pane_layout.isOpen()) {
            sliding_pane_layout.closePane();
            return true;
        }
        return false;
    }

    public void SelectPOS(POSItem posItem) {

        if (posItem != null) {

            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                    PrefHelper.POINT_OF_SALE, posItem.posCode);
            UserInfo info = POSApplication.getSingleton().getmDataModel()
                    .getUserInfo();
            info.setPOS(posItem.posCode);

            OrderDetail detail = takeOrderAdapter.getOrderDetail();
            detail.setPosItem(posItem);
            takeOrderAdapter.setOrderDetail(detail);
        }


    }

    @Override
    public void addView(View view, String Tag) {

        if (takeOrderAdapter.getCount() > 1)
            showMixer();

        else if (Tag.equalsIgnoreCase(StaticConstants.ADDON_POPUP_TAG)) {
            frameLayout_container.addView(view);
            frameLayout_container.setTag(Tag);
            frameLayout_container.setVisibility(View.VISIBLE);
            layoutHeader.setVisibility(View.GONE);
        }
    }

    @Override
    public void removeView(String Tag) {
        if (Tag.equalsIgnoreCase(StaticConstants.ADDON_POPUP_TAG)) {

           /* if (frameLayout_container.getTag().toString().equalsIgnoreCase(Tag))
                frameLayout_container.removeAllViews();*/

            frameLayout_container.setTag(null);
            frameLayout_container.setVisibility(View.GONE);
            layoutHeader.setVisibility(View.VISIBLE);
        }
    }


   /* public void getAllTables() {

        if (frameLayout_container.getVisibility() == View.VISIBLE)
            showDefault();

        else if (takeOrderAdapter != null) {

            String parameter = UtilToCreateJSON.createTablesJSON(context);
            String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
            frameLayout_container.setVisibility(View.VISIBLE);
            frameLayout_container.addView(tableNumberLayout.getAllTablePopupWindow(parameter, serverIP));
            listViewOrderItem.setVisibility(View.GONE);
        }
    }*/

    public void showDiscount() {

        if (!UserPermission("DI")) {

            if (discountLayout.listViewShowDiscount != null &&
                    discountLayout.listViewShowDiscount.getVisibility() == View.VISIBLE) {

                discountLayout.listViewShowDiscount.setVisibility(View.GONE);

                showOrderReview();

            } else
                showDiscountList();
        }

    }

    public void showDiscountList() {

        frameLayout_discount.addView(discountLayout.addDiscLayout());

        if (discountLayout.showList) {
            frameLayout_container.setVisibility(View.GONE);
            frameLayout_discount.setVisibility(View.VISIBLE);
        }
    }


    public void getAllTables() {

        final ArrayList<TableItem> tableItemList = getTableItemList();
        final ArrayList<String> tableList = new ArrayList<>();

        for (TableItem tableItem : tableItemList)
            tableList.add(tableItem.getCode());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_quantity, null);


        TextView tv_title = (TextView) view.findViewById(R.id.tv_selectTitle);
        tv_title.setText(getString(R.string.select_table_string));
        ListView listViewTable = (ListView) view.findViewById(R.id.lv_qty);
        ArrayAdapter<String> adapter_qty = new ArrayAdapter<>(context, R.layout.list_qty_layout, tableList);
        listViewTable.setAdapter(adapter_qty);
        builder.setView(view);

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

        listViewTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                tableItem = tableItemList.get(position);
                selectTable.setText(getString(R.string.selected_table_string, tableItem.getCode()));

                txtOrderSubmit.setEnabled(takeOrderAdapter.getCount() > 0);
                txtOrderSubmit.setBackgroundResource(R.drawable.button_state1);
                txtOrderSubmit.setTextColor(Color.BLUE);
                //**//
                dialog.dismiss();
            }
        });
    }

    public ArrayList<TableItem> getTableItemList() {

        ArrayList<TableItem> tableList = new ArrayList<>();
        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
        mdb.beginTransaction();

        String queryPermission = "Select * from " + DBConstants.KEY_OUTLET_TABLE;
        Cursor c = mdb.rawQuery(queryPermission, null);
        TableItem obj_list;

        try {
            if (c.moveToFirst()) {

                do {
                    obj_list = new TableItem();
                    obj_list.code = c.getString(c.getColumnIndex(DBConstants.KEY_OUTLET_TABLE_CODE));
                    obj_list.name = c.getString(c.getColumnIndex(DBConstants.KEY_OUTLET_TABLE_NUM));
                    tableList.add(obj_list);
                } while (c.moveToNext());
            }
            c.close();

            mdb.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mdb.endTransaction();
        }
        return tableList;
    }

    public boolean UserPermission(String menu_id) {

        return ((BaseFragmentActivity) context).UserPermission(menu_id);
    }


    @Override
    public boolean getPosVisibility() {

        return true;
    }

    @Override
    public void updateCodeList(int position) {

        if (codeList.size() > position)
            codeList.remove(position);
    }

    public void TableItem(TableItem tableItem) {

        this.tableItem = tableItem;
        OrderDetail detail = takeOrderAdapter.getOrderDetail();
        detail.setTableItem(tableItem);
        takeOrderAdapter.setOrderDetail(detail);
        selectTable.setText(getString(R.string.selected_table_string, tableItem.getName()));
        showDefault();
    }

    @Override
    public void responseGuestDiscount(String Phone, String Name) {

    }


    public void showHome() {

        tv_home.setBackgroundResource(R.color.home_color);
        tv_myOrders.setBackgroundResource(R.color.grey_light);
        sliding_pane_layout.closePane();
    }

    public void showOrderItemList() {

        if (!sliding_pane_layout.isOpen()) {

            tv_home.setBackgroundResource(R.color.grey_light);
            tv_myOrders.setBackgroundResource(R.color.home_color);
            sliding_pane_layout.openPane();
            tv_no_order.setVisibility(takeOrderAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    public void clearData() {

        //selectTable.setText(getResources().getString(R.string.select_table_string));
        takeOrderAdapter.clearDataSet();
        takeOrderAdapter.notifyDataSetChanged();
        codeList.clear();

        String amount = getString(R.string.rupees, "0", "\u20B9");
        tv_total_amt.setText(amount);
    }

    public void showDefault() {

        frameLayout_container.removeAllViews();
        frameLayout_container.setVisibility(View.GONE);
        listViewOrderItem.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideOrderVisibility() {

    }

    @Override
    public void showOrderVisibility() {

        showOrderReview();
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

		/*
         * private String[] TITLES = {"Categories", "Food", "Beverage",
		 * "Indian", "Chinese", "Cuisin", "Liquor", "Top New Free", "Trending"};
		 */

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public ItemsFragment fragment;

        @Override
        public CharSequence getPageTitle(int position) {

            if (getCount() == position + 1)
                layoutBanner.setVisibility(View.GONE);

            CategoryItem categoryItem = TITLES.get(position).getCategoryItem();
            if (!TextUtils.isEmpty(categoryItem.getCategory_Name())) {

                // tabs.setBackgroundColor(Color.parseColor(TITLES.get(position).getCategoryItem().getCategory_Color()));

                CategoryItem cat = TITLES.get(position).getCategoryItem();
                return cat.getCategory_Name()
                        + "<>" + cat.getCategory_Color()
                        + "<>" + cat.getCategory_Image_Url()
                        + "<>" + categoryItem.getCategory_Code();

            } else
                return TITLES.get(position).getGroupItems().getGroup_name();

        }

        @Override
        public int getCount() {
            return TITLES.size();
        }

        @Override
        public Fragment getItem(int position) {

            return fragment = ItemsFragment.newInstance(position, TITLES.get(position),
                    takeOrderAdapter.getOrderDetail().getPosItem().posCode);
        }

        public void destroyAllItem() {
            int mPosition = pager.getCurrentItem();
            int mPositionMax = pager.getCurrentItem() + 1;
            if (TITLES.size() > 0 && mPosition < TITLES.size()) {
                if (mPosition > 0) {
                    mPosition--;
                }

                for (int i = mPosition; i < mPositionMax; i++) {
                    try {
                        Object objectobject = this.instantiateItem(pager, i);
                        if (objectobject != null)
                            destroyItem(pager, i, objectobject);
                    } catch (Exception e) {
                        Logger.i(Logger.LOGGER_TAG,
                                "no more Fragment in FragmentPagerAdapter");
                    }
                }
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

            if (position <= getCount()) {
                FragmentManager manager = ((Fragment) object).getFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove((Fragment) object);
                trans.commit();
            }
        }

    }


    @Override
    public void onPause() {
        super.onPause();

        if (statusScheduler != null && !statusScheduler.isShutdown())
            statusScheduler.shutdownNow();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (orderList.size() > 0) {
            if (statusScheduler != null && statusScheduler.isShutdown())
                scheduleExecutors(orderList.get(0));
        }
    }


    public void getMenuItems() {

        String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();

        ArrayList<Items> mTITLES = new ArrayList<>();
        String previous_cat = "";

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        String queryPermission = "Select * from " + DBConstants.ITEMS_DETAIL_TABLE;
        Cursor c = mdb.rawQuery(queryPermission, null);

        try {
            if (c.moveToFirst()) {

                do {

                    if (!previous_cat.equals(c.getString(c.getColumnIndex("cat_code")))) {

                        CategoryItem categoryItem = new CategoryItem();
                        categoryItem.setCategory_Code(c.getString(c.getColumnIndex("cat_code")));
                        categoryItem.setCategory_Name(c.getString(c.getColumnIndex("cat_desc")));
                        categoryItem.setCategory_Image_Url(BaseNetwork.defaultUrlMethod(serverIP,
                                "/Image/" + categoryItem.getCategory_Code() + ".png"));


                        ArrayList<com.entrada.cheekyMonkey.entity.MenuItem> menuItemList = new ArrayList<>();
                        com.entrada.cheekyMonkey.entity.MenuItem menuItem = new com.entrada.cheekyMonkey.entity.MenuItem();
                        menuItem.setMenu_code(c.getString(c.getColumnIndex("item_code")));
                        menuItem.setMenu_name(c.getString(c.getColumnIndex("item_desc")));
                        menuItem.setMenu_categ_code(c.getString(c.getColumnIndex("cat_code")));
                        menuItem.setInc_Rate(Float.parseFloat(c.getString(c.getColumnIndex("inc_rate"))));
                        menuItem.setMenu_price(Float.parseFloat(c.getString(c.getColumnIndex("cur_rate"))));
                        menuItem.setMax_Price(Float.parseFloat(c.getString(c.getColumnIndex("max_price"))));
                        menuItem.setMin_Price(Float.parseFloat(c.getString(c.getColumnIndex("min_price"))));
                        menuItemList.add(menuItem);

                        mTITLES.add(new Items(new GroupItems(), categoryItem, menuItemList));
                        previous_cat = c.getString(c.getColumnIndex("cat_code"));

                    } else {

                        ArrayList<com.entrada.cheekyMonkey.entity.MenuItem> menuItemList = mTITLES.get(mTITLES.size() - 1).getMenuItemList();
                        com.entrada.cheekyMonkey.entity.MenuItem menuItem = new com.entrada.cheekyMonkey.entity.MenuItem();
                        menuItem.setMenu_code(c.getString(c.getColumnIndex("item_code")));
                        menuItem.setMenu_name(c.getString(c.getColumnIndex("item_desc")));
                        menuItem.setMenu_categ_code(c.getString(c.getColumnIndex("cat_code")));
                        menuItem.setInc_Rate(c.getFloat(c.getColumnIndex("inc_rate")));
                        menuItem.setMenu_price(c.getFloat(c.getColumnIndex("cur_rate")));
                        menuItem.setMax_Price(c.getFloat(c.getColumnIndex("max_price")));
                        menuItem.setMin_Price(c.getFloat(c.getColumnIndex("min_price")));
                        menuItemList.add(menuItem);
                    }

                } while (c.moveToNext());

            }
            c.close();
            mdb.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

        if (!mTITLES.isEmpty()) {

            TITLES.clear();
            TITLES.addAll(mTITLES);

            pager.removeAllViews();
            pager.setAdapter(adapter);
            tabs.setViewPager(pager);
        }

        updateStatusPendingOrders();
    }



    /* ******************* Complete flow to Place Guest Order ****************** */

    @SuppressWarnings("unchecked")
    public void placeGuestOrderMethod(String guestID, String guestName) {

        CustomAsynctaskLoader loader = new CustomAsynctaskLoader(context);
        String paramter = UtilToCreateJSON.createGuestOrder(context, guestID,
                guestName, cover, steward, takeOrderAdapter, order_type, orderRemark);
        GuestCommonTask<String, ResultMessage> commomTask = new GuestCommonTask<>(
                context, this, BaseNetwork.KEY_ECABS_GuestOrder,
                paramter, loader);
        AsyncTaskTools.execute(commomTask);
    }


    @Override
    public void taskStarting() {

    }

    @Override
    public void taskProgress(Object result) {
        if (result instanceof ResultMessage) {
            ResultMessage message = (ResultMessage) result;
            if (message.TYPE
                    .equalsIgnoreCase(BaseNetwork.KEY_ECABS_DisplayGuest)) {
            }
        }
    }

    @Override
    public void taskCompleted(Object result) {

        if (result instanceof ResultMessage) {
            ResultMessage message = (ResultMessage) result;

            if (message.TYPE.equalsIgnoreCase(BaseNetwork.KEY_ECABS_GuestOrder)) {

                String orderNum = message.RESPONSE;
                showHome();

                saveOrderStatus(orderNum, "A");
                ((BaseFragmentActivity) context).sendNotification(getString(R.string.process_noti, UserInfo.guest_name, orderNum));

                ((BaseFragmentActivity) context).myOrders();
                showOrderConfirmedMsg(orderNum);
                //Toast.makeText(context, orderNum, Toast.LENGTH_LONG).show();
                //clearData();

                if (orderList.isEmpty()) {       // indicating no service is running.
                    orderList.add(orderNum);
                    scheduleExecutors(orderNum);
                } else
                    orderList.add(orderNum);  //queue this order number, which will get handled by ScheduledExecutor at last.
            }
        }
    }

    @Override
    public void taskErrorMessage(Object result) {

    }

    @Override
    public Context getContext() {
        return null;
    }


    public void showOrderConfirmedMsg(String orderNo) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_confirm_popup, null);
        TextView textviewOk = (TextView) view.findViewById(R.id.tv_ok);
        TextView tv_orderNo = (TextView) view.findViewById(R.id.tv_ord_no);
        tv_orderNo.setText(orderNo);

        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        textviewOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

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


    /*************** Complete flow to control Active Notification **********************/
    public void scheduleExecutors(final String orderNum) {

        statusScheduler = Executors.newSingleThreadScheduledExecutor();
        statusScheduler.scheduleWithFixedDelay(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                // Hit WebService

                Logger.i("Order Status Service >>>>>>>>>>>", " Running");

                String parameter = UtilToCreateJSON.createGuestOrderStatus(context, orderNum);
                String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
                GuestOrderStatusTask statusTask = new GuestOrderStatusTask(context, orderNum, parameter, serverIP,
                        TakeOrderFragment.this, null);
                statusTask.execute();


            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    public void sendNotification(String orderNum, String status) {

        if (status.equals("failure")) {
            statusScheduler.shutdownNow();
            Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!orderList.contains(orderNum) || status.isEmpty())
            return;

        if (getActivity() != null && isAdded()) {

            if (status.equals("B") || status.equals("C") || status.equals("D") || status.equals("E")) {

                if (status.equals("B"))
                    ((BaseFragmentActivity) context).sendNotification(getString(R.string.accepted_noti, UserInfo.guest_name, orderNum));
                else
                    ((BaseFragmentActivity) context).sendNotification(getString(R.string.rejected_noti, UserInfo.guest_name, orderNum));

                orderList.remove(orderList.indexOf(orderNum));
                updateOrderStatus(orderNum, status);
                statusScheduler.shutdownNow();

                if (orderList.size() > 0) {
                    if (statusScheduler.isShutdown())
                        scheduleExecutors(orderList.get(0));
                }
            }
        }
    }

    public void saveOrderStatus(String orderNum, String status) {

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
        mdb.beginTransaction();
        try {

            ArrayList<OrderItem> orderItems = takeOrderAdapter.getDataSet();
            for (int i = 0; i < takeOrderAdapter.getCount(); i++) {

                ContentValues cv = new ContentValues();
                cv.put(DBConstants.KEY_ORDER_NUMBER, orderNum);
                cv.put(DBConstants.KEY_TABLE_NUMBER, tableItem.getName());
                cv.put(DBConstants.KEY_ITEM_CODE, orderItems.get(i).getO_code());
                cv.put(DBConstants.KEY_ITEM_NAME, orderItems.get(i).getO_name());
                cv.put(DBConstants.KEY_ITEM_PRICE, orderItems.get(i).getO_price());
                cv.put(DBConstants.KEY_ITEM_QTY, orderItems.get(i).getO_quantity());
                cv.put(DBConstants.KEY_ORDER_AMOUNT, orderItems.get(i).getO_amount());
                cv.put(DBConstants.KEY_ORDER_STATUS, status);
                cv.put(DBConstants.KEY_ORDER_DATE, UserInfo.getCurrentDate());

                mdb.insert(DBConstants.KEY_GUEST_ORDERS_TABLE, null, cv);
            }

            mdb.setTransactionSuccessful();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

        clearData();

    }

    public void updateOrderStatus(String orderNum, String status) {

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
        mdb.beginTransaction();
        try {

            ContentValues cv = new ContentValues();
            cv.put(DBConstants.KEY_GUEST_ORDER_NUMBER, orderNum);
            cv.put(DBConstants.KEY_GUEST_ORDER_STATUS, status);

            mdb.update(DBConstants.KEY_GUEST_ORDERS_TABLE, cv, DBConstants.KEY_GUEST_ORDER_NUMBER + "= '" + orderNum + "' ", null);
            mdb.setTransactionSuccessful();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }
    }

    /* ******************************************************************* */


    /* *** Handling Guest Location in terms of latitude & longitude ****** */
    class GeoPoint {

        int lattitue = 0;
        int longitutde = 0;

        GeoPoint(int lati, int longi) {

            lattitue = lati;
            longitutde = longi;
        }
    }

    public GeoPoint getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        GeoPoint p1;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
                    (int) (location.getLongitude() * 1E6));

            return p1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void getLocationOfCurrentAddress() {

        // check if GPS enabled
        GPSTracker gpsTracker = new GPSTracker(context);

        if (gpsTracker.getIsGPSTrackingEnabled()) {

/*          latitude = gpsTracker.latitude;
            longitude= gpsTracker.longitude;
            String country = gpsTracker.getCountryName(context);
            String city = gpsTracker.getLocality(context);
            String postalCode = gpsTracker.getPostalCode(context);
            String addressLine = gpsTracker.getAddressLine(context);*/

        } else
            gpsTracker.showSettingsAlert();

        //isLocationEnabled(context);
        //isLocationServiceEnabled();
    }

    public boolean isLocationServiceEnabled() {
        LocationManager lm = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        String provider = lm.getBestProvider(new Criteria(), true);
        return (!provider.isEmpty() &&
                !LocationManager.PASSIVE_PROVIDER.equals(provider));
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    public void ShowPopupAccessDenied() {

        AlertDialog.Builder objbuilder = new AlertDialog.Builder(context);

        AlertDialog.Builder builder = objbuilder.setTitle("Message");
        builder.setMessage("Your order can't be placed as you are not in the company premise.");

        objbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
    }


    public void scheduleTimer() {

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //process();
                adapter.notifyDataSetChanged();
                Logger.w("Timer scheduled >>>>>>>>>>>", "Yeppy !...");
            }
        };

        Timer mTimer = new Timer();
        mTimer.schedule(timerTask, 20, 20 * 1000);
    }
}