package com.entrada.cheekyMonkey.steward;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.addons.Add_Mixer_Fragment;
import com.entrada.cheekyMonkey.appInterface.OnBackPressInterface;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.DBStatements;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.dynamic.BaseFragmentActivity;
import com.entrada.cheekyMonkey.dynamic.GuestOrderStatusTask;
import com.entrada.cheekyMonkey.dynamic.ICallSendNotification;
import com.entrada.cheekyMonkey.dynamic.ItemsFragment;
import com.entrada.cheekyMonkey.entity.CategoryItem;
import com.entrada.cheekyMonkey.entity.GroupItems;
import com.entrada.cheekyMonkey.entity.Items;
import com.entrada.cheekyMonkey.entity.MenuItem;
import com.entrada.cheekyMonkey.entity.POSItem;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.staticData.PrefHelper;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.steward.bill.CreditDetailPopup;
import com.entrada.cheekyMonkey.steward.bill.ICallPaidAmount;
import com.entrada.cheekyMonkey.steward.billEdit.BillCancelTask;
import com.entrada.cheekyMonkey.steward.billEdit.BillEditLayout;
import com.entrada.cheekyMonkey.steward.billEdit.BillModifyTask;
import com.entrada.cheekyMonkey.steward.billEdit.BillSettleTask;
import com.entrada.cheekyMonkey.steward.billEdit.BillTransferLayout;
import com.entrada.cheekyMonkey.steward.billEdit.DirectSettlementLayout;
import com.entrada.cheekyMonkey.steward.billEdit.ICallBillDetail;
import com.entrada.cheekyMonkey.steward.billEdit.ICallBillModiResponse;
import com.entrada.cheekyMonkey.steward.billEdit.ICallSettleResponse;
import com.entrada.cheekyMonkey.steward.billEdit.IcallBackOrderCancel;
import com.entrada.cheekyMonkey.steward.billEdit.UndoBillSettleTask;
import com.entrada.cheekyMonkey.steward.billSplit.BillSplitLayout;
import com.entrada.cheekyMonkey.steward.companyDiscount.CompanyDiscountLayout;
import com.entrada.cheekyMonkey.steward.companyDiscount.ICallCompanyDiscount;
import com.entrada.cheekyMonkey.steward.discount.AutoDiscount;
import com.entrada.cheekyMonkey.steward.discount.DiscountLayout;
import com.entrada.cheekyMonkey.steward.discount.GuestDiscountLayout;
import com.entrada.cheekyMonkey.steward.discount.ICALL_ModificationRemark;
import com.entrada.cheekyMonkey.steward.discount.ICallDiscList;
import com.entrada.cheekyMonkey.steward.holdOrders.HoldOrdersLayout;
import com.entrada.cheekyMonkey.steward.home_del.CrewMemberLayout;
import com.entrada.cheekyMonkey.steward.home_del.DelBoyLayout;
import com.entrada.cheekyMonkey.steward.home_del.DelLogLayout;
import com.entrada.cheekyMonkey.steward.home_del.GuestListLayout;
import com.entrada.cheekyMonkey.steward.home_del.HomeDelOptions;
import com.entrada.cheekyMonkey.steward.home_del.HomeItem;
import com.entrada.cheekyMonkey.steward.home_del.ICallBackSendOrderHomeDResponse;
import com.entrada.cheekyMonkey.steward.home_del.ICallHome;
import com.entrada.cheekyMonkey.steward.home_del.SendOrderHomeTask;
import com.entrada.cheekyMonkey.steward.notificationUI.NotificationFragment;
import com.entrada.cheekyMonkey.steward.order.FetchOrderTask;
import com.entrada.cheekyMonkey.steward.order.ICallBackOrderNumber;
import com.entrada.cheekyMonkey.steward.order.ICallBackSendOrderResponse;
import com.entrada.cheekyMonkey.steward.order.ICallBackTestGenerateResponse;
import com.entrada.cheekyMonkey.steward.order.IcallBackOrderModication;
import com.entrada.cheekyMonkey.steward.order.OrderNumberLayout;
import com.entrada.cheekyMonkey.steward.order.SendOrderCancelTask;
import com.entrada.cheekyMonkey.steward.order.SendOrderModificationTask;
import com.entrada.cheekyMonkey.steward.order.SendOrderTask;
import com.entrada.cheekyMonkey.steward.order.SendTableCancelTask;
import com.entrada.cheekyMonkey.steward.other.CustomLoginPopup;
import com.entrada.cheekyMonkey.steward.rate.ICallRateResponse;
import com.entrada.cheekyMonkey.steward.roomService.ICallBackRoom;
import com.entrada.cheekyMonkey.steward.roomService.RoomItem;
import com.entrada.cheekyMonkey.steward.roomService.RoomNumberLayout;
import com.entrada.cheekyMonkey.takeorder.ICallTable;
import com.entrada.cheekyMonkey.takeorder.ItemsAdapter;
import com.entrada.cheekyMonkey.takeorder.TableNumberLayout;
import com.entrada.cheekyMonkey.takeorder.adapter.TakeOrderAdapter;
import com.entrada.cheekyMonkey.takeorder.entity.OrderDetail;
import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;
import com.entrada.cheekyMonkey.takeorder.entity.TableItem;
import com.entrada.cheekyMonkey.takeorder.popup.ICallOrder;
import com.entrada.cheekyMonkey.ui.CustomButton;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.ui.PagerSlidingTabStrip;
import com.entrada.cheekyMonkey.ui.ProgressBarCircularIndeterminate;
import com.entrada.cheekyMonkey.ui.SlidingUpPanelLayout;
import com.entrada.cheekyMonkey.util.Logger;
import com.entrada.cheekyMonkey.util.Util;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class StewardOrderFragment extends Fragment implements
        View.OnClickListener, OnBackPressInterface, ICallTable,
        ICallBackSendOrderResponse, ICallBackOrderNumber,
        ICALL_ModificationRemark, IcallBackOrderModication,
        IcallBackOrderCancel, OnMenuItemClick.ICallMenuPopup, ICallOrder, ICallBackSendOrderHomeDResponse,
        ICallBackTestGenerateResponse, ICallDiscList, ICallCompanyDiscount, ICallBackRoom,
        ICallHome, ICallBillDetail, ICallPaidAmount, ICallSettleResponse, ICallBillModiResponse,
        ICallBackPOS,ICallRateResponse, ICallSendNotification {

    private static Handler mHandler = null;
    private static String TAG = "PlainTextActivity";
    public Context context;
    ArrayList<Items> menu_search_list;
    public ItemsAdapter adapter_menu_search;
    public LinearLayout layout_flipMode,layout_menu, layout_order, layoutGroup, ll_bottomHeader,
            ll_bottomOrder, ll_settle_option;
    private FrameLayout frameLayout_container, frameLayout_discount, mainContainer, frameLayout_Cancel;

    public CustomTextview textviewForBIllno, textviewForDiscount, textviewForSubtotal, textviewForTax,
            textviewForTotal, textViewTAmt, textViewChange, textViewStlMode, textviewTop,
            textViewGuestName, tv_OrderNo, tv_Discount, tv_Subtotal, tv_Tax, tv_Total;

    public CustomButton txtOrderClear, txtOrderSubmit, textHoldOrder,txtOrderCancel, txtorderOk, txtCash, txtCredit,
            selectTable, selectRoom, selectGuest, selectDelBoy, newOrder,tv_order_cancel, tv_change_to_comp,
            tv_generate, tv_discount, tv_cover, tv_steward, tv_guest, tv_add_item, tv_show_bill, tv_odr_cancel_type,
            textView_cash, textView_credit;



    public ArrayList<Items> TITLES = new ArrayList<>();
    public TakeOrderAdapter takeOrderAdapter;
    public TableNumberLayout tableNumberLayout;
    public DiscountLayout discountLayout;
    public ListView listViewOrderItem, listViewSearchItem, selectGuestCompanyList;
    public boolean coverPopup = false, stewardPopup = false;
    public LinearLayout posLayoutContainer, addonLayoutContainer, layout_bill_operation, layout_Settle,
            layout_bill_settle, layout_order_cancel, linerLayoutTableView, linerLayoutHomeDelivery,
            layout_order_options, layoutGuestName;
    public String orderRemark = "", guest_code = "", guest_name = "", steward = "", userID = "",
            order_type = "", cover = "", companyCode = "";
    RadioGroup radioGroupVegNonveg;

    public ArrayList<String> codeList = new ArrayList<>();
    protected SlidingUpPanelLayout slidingUpPanelLayout;
    TableItem tableItem;
    RoomItem roomItem;
    HomeItem homeItem;
    AutoDiscount autoDiscount;
    boolean generateBill = false;


    OrderNumberLayout orderNumberLayout;
    LinearLayout layout_List_Item, layoutShowBillDetail, layoutShowHoldOrderDetail;
    RelativeLayout relativeLayoutItemList;
    GridView gridViewCurrency;
    ArrayList<String> currencyList = new ArrayList<>();
    ArrayAdapter<String> currencyAdapter;
    String[] currencyArray = {"? 1", "? 2", "? 5", "? 10", "? 20", "? 50", "? 100", "? 500", "? 1000", "? 2000"};

    ScheduledExecutorService statusScheduler;
    ArrayList<String> orderList ;

    POSItem posItem;
    UserInfo userInfo;
    BillEditLayout billEditLayout;
    HoldOrdersLayout holdOrdersLayout;
    RadioGroup rg_OdrCancel;
    float cashPaid = 0, creditPaid = 0;
    String orderNumber = "", flag = "";
    boolean gst_mandate = false, form_8 = false;
    private View view;
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private ProgressBarCircularIndeterminate progressRecallLastOrder;
    private RoomNumberLayout roomNumberLayout;
    private GuestListLayout guestListLayout;
    private DelBoyLayout delBoyLayout;
    private HomeDelOptions delOptionsLayout;
    private CrewMemberLayout crewMemberLayout;
    private CompanyDiscountLayout companyDiscountLayout;
    private GuestDiscountLayout guestDiscountLayout;
    private String formattedDate = "";
    private String time = "";
    int holdOrderIndex = 0;

    boolean odr_modi_rsn_mandat = false, print_order = false, odr_rem_mandat = false, rsn_bill_modi = false,
            gen_stw_bill = false, rem_as_gst = false, grp_wise_billing = false, hide_bill_remark = false,
            del_bill_edit = false, mod_bill_edit = false, disc_reason = false ;


    public static StewardOrderFragment newInstance(int position) {

        StewardOrderFragment fragment = new StewardOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null){
            view = inflater.inflate(R.layout.take_order_fargment2, container, false);
            userInfo = POSApplication.getSingleton().getmDataModel().getUserInfo();
            init(view);

            SelectPOS(new POSItem());
            int position = getArguments().getInt("position");
            onLeftListItemClick(position);
            getMenuItems();
        }

/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                extractLogToFile();
                //SendSmtpMail.sendMessageWithAttachment("rgrahulgupta001@gmail.com", "logcat.txt");
            }
        }, 5000);
*/

        return view;
    }

    private void init(View v) {

        //layoutGroup = (LinearLayout) v.findViewById(R.id.linear_layout_show_group);
        layout_flipMode = (LinearLayout) v.findViewById(R.id.layout_flip_mode);
        layout_menu= (LinearLayout) v.findViewById(R.id.layout_create_order);
        layout_order = (LinearLayout) v.findViewById(R.id.layout_order);

        setCurrencyView(v);
        flipForPortrait();

        ll_bottomOrder = (LinearLayout) v.findViewById(R.id.ll_bottomOrder);
        listViewOrderItem = (ListView) v.findViewById(R.id.listViewOrderItem);
        txtorderOk = (CustomButton) v.findViewById(R.id.txtOrderOk);
        txtOrderSubmit = (CustomButton) v.findViewById(R.id.txtOrderSubmit);
        txtOrderClear = (CustomButton) v.findViewById(R.id.txtOrderClear);
        textHoldOrder = (CustomButton) v.findViewById(R.id.tvHoldOrder);
        txtOrderCancel = (CustomButton) v.findViewById(R.id.cancel_operation);
        textviewTop = (CustomTextview) v.findViewById(R.id.textViewBill);
        txtCash = (CustomButton) v.findViewById(R.id.tv_cash);
        txtCredit = (CustomButton) v.findViewById(R.id.tv_credit);
        selectRoom = (CustomButton) v.findViewById(R.id.selectRoom);
        rg_OdrCancel = (RadioGroup) v.findViewById(R.id.rg_odr_cancel);

        radioGroupVegNonveg = (RadioGroup) v.findViewById(R.id.rg_veg_nonveg);
        radioGroupVegNonveg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_all_item)
                    showFilteredMenuItems("A");
                else if (checkedId == R.id.rb_veg_item)
                    showFilteredMenuItems("V");
                else
                    showFilteredMenuItems("N");
            }
        });

        txtorderOk.setOnClickListener(this);
        txtOrderSubmit.setOnClickListener(this);
        txtOrderClear.setOnClickListener(this);
        textHoldOrder.setOnClickListener(this);
        txtOrderCancel.setOnClickListener(this);
        txtCash.setOnClickListener(this);
        txtCredit.setOnClickListener(this);
        selectRoom.setOnClickListener(this);

        relativeLayoutItemList = (RelativeLayout) v.findViewById(R.id.rl_item_list);
        layout_bill_operation = (LinearLayout) v.findViewById(R.id.layout_bill_operation);
        layout_order_options = (LinearLayout) v.findViewById(R.id.ll_order_options);
        layout_order_cancel = (LinearLayout) v.findViewById(R.id.layout_order_cancel);

        tv_generate = (CustomButton) v.findViewById(R.id.tv_generate_bill);
        tv_discount = (CustomButton) v.findViewById(R.id.tv_item_discount);
        tv_cover  = (CustomButton) v.findViewById(R.id.tv_item_cover);
        tv_steward  = (CustomButton) v.findViewById(R.id.tv_item_steward);
        tv_guest  = (CustomButton) v.findViewById(R.id.tv_item_guest);

        tv_add_item = (CustomButton) v.findViewById(R.id.tv_add_item);
        tv_show_bill = (CustomButton) v.findViewById(R.id.tv_show_bill);
        tv_odr_cancel_type = (CustomButton) v.findViewById(R.id.tv_odr_cancel_type);
        tv_order_cancel = (CustomButton) v.findViewById(R.id.tv_order_cancel);
        tv_change_to_comp = (CustomButton) v.findViewById(R.id.tv_chngeToComp);
        tv_generate.setOnClickListener(this);
        tv_discount.setOnClickListener(this);
        tv_cover.setOnClickListener(this);
        tv_steward.setOnClickListener(this);
        tv_guest.setOnClickListener(this);
        tv_add_item.setOnClickListener(this);
        tv_show_bill.setOnClickListener(this);
        tv_order_cancel.setOnClickListener(this);
        tv_change_to_comp.setOnClickListener(this);

        userID =  PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.USER_ID);
        steward = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.USER_ID);

        discountLayout = new DiscountLayout(context, this);
        takeOrderAdapter = new TakeOrderAdapter(context, this, discountLayout);
        listViewOrderItem.setAdapter(takeOrderAdapter);
        autoDiscount = new AutoDiscount(context, discountLayout, takeOrderAdapter);

        // Search Item ListView
        listViewSearchItem = (ListView) v.findViewById(R.id.listview_search_Item);
        menu_search_list = new ArrayList<>();
        adapter_menu_search = new ItemsAdapter(context, R.layout.item_layout_row, menu_search_list);
        listViewSearchItem.setAdapter(adapter_menu_search);
        listViewSearchItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //listViewOrderItem.setVisibility(View.VISIBLE);
                listViewSearchItem.setVisibility(View.GONE);
                radioGroupVegNonveg.setVisibility(View.GONE);
                ((BaseFragmentActivity) context).collapseToolbar();

                Items items = (Items) parent.getItemAtPosition(position);
                //onclickEvent(items.getMenuItem());
                showQtyPopup(items.getMenuItem());

            }
        });


        linerLayoutTableView = (LinearLayout) v.findViewById(R.id.linerLayoutTableView);
        layoutGuestName = (LinearLayout) v.findViewById(R.id.layout_guest);
        textViewGuestName = (CustomTextview) v.findViewById(R.id.tv_guest_name);

        layoutShowBillDetail = (LinearLayout) v.findViewById(R.id.layoutShowBillDetail);
        textviewForBIllno = (CustomTextview) v.findViewById(R.id.textviewForBIllno);
        textviewForDiscount = (CustomTextview) v.findViewById(R.id.textviewForDiscount);
        textviewForSubtotal = (CustomTextview) v.findViewById(R.id.textviewForSubtotal);
        textviewForTax = (CustomTextview) v.findViewById(R.id.textviewForTax);
        textviewForTotal = (CustomTextview) v.findViewById(R.id.textviewForTotal);
        textViewTAmt = (CustomTextview) v.findViewById(R.id.tv_TenderedAmt);
        textViewChange = (CustomTextview) v.findViewById(R.id.tv_ChangeAmt);
        textViewStlMode = (CustomTextview) v.findViewById(R.id.tv_stlmnt_mode);


        layoutShowHoldOrderDetail = (LinearLayout) v.findViewById(R.id.layout_holdOrderDetail);
        tv_OrderNo = (CustomTextview) v.findViewById(R.id.tvOrderNo);
        tv_Discount = (CustomTextview) v.findViewById(R.id.tv_ForSubtotal);
        tv_Subtotal = (CustomTextview) v.findViewById(R.id.tv_ForDiscount);
        tv_Tax = (CustomTextview) v.findViewById(R.id.tv_ForTax);
        tv_Total = (CustomTextview) v.findViewById(R.id.tv_ForTotal);


        ll_settle_option = (LinearLayout) v.findViewById(R.id.layout_stl_options);
        textView_cash =  (CustomButton) v.findViewById(R.id.textview_cash);
        textView_credit =  (CustomButton) v.findViewById(R.id.textview_credit);
        textView_cash.setOnClickListener(this);
        textView_credit.setOnClickListener(this);

        // POS Layout
        posLayoutContainer = (LinearLayout) v.findViewById(R.id.posLayoutContainer);
        //  posLayout = new POSLayout(context, StewardOrderFragment.this);

        selectGuestCompanyList = (ListView) v.findViewById(R.id.selectGuestCompanyList);


        /*searchViewGuest.setQueryHint("Type Guest Phone no here...");
        searchViewGuest.setIconifiedByDefault(false);
        searchViewGuest.setOnQueryTextListener(this);
        searchViewGuest.setOnCloseListener(this);*/

        layout_List_Item = (LinearLayout) view.findViewById(R.id.ll_list_item);
        addonLayoutContainer = (LinearLayout) v.findViewById(R.id.addonLayoutContainer);

       /* TITLES.add(new Items(new GroupItems("", "Home", ""),
                new CategoryItem(), new MenuItem()));*/
        tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        pager = (ViewPager) v.findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getChildFragmentManager());

        pager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                        .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setViewPager(pager);
        tabs.setVisibility(View.VISIBLE);

        adapter.notifyDataSetChanged();

        selectTable = (CustomButton) v.findViewById(R.id.selectTable);
        selectTable.setOnClickListener(this);
        newOrder = (CustomButton) v.findViewById(R.id.newOrder);
        newOrder.setOnClickListener(this);

        selectGuest = (CustomButton) v.findViewById(R.id.selectGuest);
        selectGuest.setOnClickListener(this);

        selectDelBoy = (CustomButton) v.findViewById(R.id.selectDelBoy);
        selectDelBoy.setOnClickListener(this);

        frameLayout_container = (FrameLayout) v.findViewById(R.id.frameLayout_container);
        mainContainer = (FrameLayout) v.findViewById(R.id.tof_container);
        frameLayout_discount = (FrameLayout) v.findViewById(R.id.frameLayout_discount);
        frameLayout_Cancel = (FrameLayout) v.findViewById(R.id.frameLayout_container_new);

        roomNumberLayout = new RoomNumberLayout(context, frameLayout_container.getWidth(), this);
        guestListLayout = new GuestListLayout(context, this);
        delBoyLayout = new DelBoyLayout(context, this);
        delOptionsLayout = new HomeDelOptions(context, this);
        crewMemberLayout = new CrewMemberLayout(context, this);

        tableNumberLayout = new TableNumberLayout(context, frameLayout_container.getWidth(), this);
        orderNumberLayout = new OrderNumberLayout(context, frameLayout_container.getWidth(), this);
        companyDiscountLayout = new CompanyDiscountLayout(context, frameLayout_container.getWidth(), this);
        guestDiscountLayout = new GuestDiscountLayout(context, frameLayout_container.getWidth(), this);

        slidingUpPanelLayout = (SlidingUpPanelLayout) v.findViewById(R.id.slidingUpPanelLayout);
        ll_bottomHeader = (LinearLayout) v.findViewById(R.id.ll_bottomHeader);
        progressRecallLastOrder = (ProgressBarCircularIndeterminate) v.findViewById(R.id.progressRecallLastOrder);
        linerLayoutHomeDelivery = (LinearLayout) v.findViewById(R.id.linerLayoutHomeDelevry);
        ImageView imgGuestSearch1 = (ImageView) v.findViewById(R.id.imgGuestSearch1);
        ImageView imgGuestSearch2 = (ImageView) v.findViewById(R.id.imgGuestSearch2);
        imgGuestSearch1.setOnClickListener(this);
        imgGuestSearch2.setOnClickListener(this);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",Locale.US);
        formattedDate = df.format(c.getTime());
        time = sdf.format(c.getTime());

        if (ll_bottomHeader != null)
            ll_bottomHeader.setOnClickListener(this);

        if (slidingUpPanelLayout != null)
            slidingUpPanelLayout.setPanelSlideListener(PageSliderLister());

        //tabs.setBackgroundColor(userInfo.getNaviBackground());
        //pager.setBackgroundColor(userInfo.getBackground());

        //linerLayoutTableView.setBackgroundColor(userInfo.getNaviBackground());
        //linerLayoutHomeDelivery.setBackgroundColor(userInfo.getNaviBackground());
        //layout_List_Item.setBackgroundColor(userInfo.getBackground());
        //ll_bottomOrder.setBackgroundColor(userInfo.getNaviBackground());

        int color = userInfo.getNaviFontColor();
        selectTable.setTextColor(color);
        newOrder.setTextColor(color);
        txtOrderSubmit.setTextColor(color);
        txtOrderClear.setTextColor(color);

        getAttributes();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                posLayoutContainer.setVisibility(View.GONE);
                //frameLayout_container.setVisibility(View.GONE);
                posItem = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem();

                if (posItem.posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_R)) {
                    // show restaurants things
                    OrderDetail detail = takeOrderAdapter.getOrderDetail();
                    detail.setPosItem(posItem);
                    takeOrderAdapter.setOrderDetail(detail);
                    selectTable.setVisibility(View.VISIBLE);

                } else if (posItem.posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_H)
                        || posItem.posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_T)) {

                    linerLayoutTableView.setVisibility(View.GONE);
                    linerLayoutHomeDelivery.setVisibility(View.VISIBLE);
                    frameLayout_container.setVisibility(View.GONE);
                    ll_bottomOrder.setVisibility(View.VISIBLE);
                    OrderDetail detail1 = takeOrderAdapter.getOrderDetail();
                    detail1.setPosItem(posItem);
                    takeOrderAdapter.setOrderDetail(detail1);

                    if (posItem.posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_H)) {

                        selectDelBoy.setVisibility(View.VISIBLE);
                        checkGuestMandate();
                        if (form_8) textHoldOrder.setVisibility(View.VISIBLE);
                    }

                } else if (posItem.posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_RS)) {

                    selectRoom.setVisibility(View.VISIBLE);
                    selectTable.setVisibility(View.GONE);
                    OrderDetail detail = takeOrderAdapter.getOrderDetail();
                    detail.setPosItem(posItem);
                    takeOrderAdapter.setOrderDetail(detail);
                }

            }
        }, 100);
    }


/*
    public void onLeftListItemClick(int position){

        switch (position){

            case 0:
                ((BaseFragmentActivity)context).showHomeItemList();
                break;
            case 2:
                if (!UserPermission("OC"))
                    showRunningTables("C");
                break;
            case 3:
                if (!UserPermission("OM"))
                    showRunningTables("M");
                break;
            case 4:
                break;
            case 5:
                if (!UserPermission("OT"))
                    ((BaseFragmentActivity) context).showOrderSplit();
                break;
            case 7:
                if (!UserPermission("BC"))
                    getBillEditPopup("C");
                break;
            case 8:
                if (!UserPermission("BM"))
                    getBillEditPopup("M");
                break;
            case 9:
                if (!UserPermission("TT"))
                    getBillTransferPopup();
                break;
            case 10:
                if (!UserPermission("BT"))
                    getBillSplitPopup();
                break;
            case 11:
                if (!UserPermission("BS"))
                    getBillSettlementPopup();
                break;
            case 12:
                if (!UserPermission("US"))
                    getBillEditPopup("U");
                break;
            case 13:
                UserInfo.showLogoutDialog(context);
                break;
            default:
                break;

        }
    }
*/


    public void onLeftListItemClick(int position){


        switch (position){

            case 3:
                getAllTables();
                break;
            case 4:
                if (!UserPermission("BS"))
                    getBillSettlementPopup();
                break;
            case 5:
                if (!UserPermission("US"))
                    getBillEditPopup("U");
                break;
            case 6:
                if (!UserPermission("BG"))
                    ((BaseFragmentActivity) context).onCreateBillGenerate("","");
                break;
            case 7:
                showDiscount();
                break;
            case 8:
                ((BaseFragmentActivity) context).showNotificationFor(NotificationFragment.TYPE_UNDER_PROCESS);

                break;
            case 9:
                ((BaseFragmentActivity) context).showNotificationFor(NotificationFragment.TYPE_ACCEPTED);
                break;
            case 10:
                ((BaseFragmentActivity) context).showNotificationFor(NotificationFragment.TYPE_REJECTED);
                break;
            case 11:
                CoverPopup coverPopup = new CoverPopup(context, this);
                coverPopup.show();
                break;


            case 15:
                if (!UserPermission("OC"))
                showRunningTables("C");
                break;

            case 16:
                if (!UserPermission("OM"))
                showRunningTables("M");
                break;

            case 17:
                if (!UserPermission("OT"))
                ((BaseFragmentActivity) context).showOrderSplit();
                break;

            case 18:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!UserPermission("BC"))
                            getBillEditPopup("C");
                    }
                },500);

                break;

            case 19:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!UserPermission("BM"))
                            getBillEditPopup("M");
                    }
                },500);
                break;

            case 20:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!UserPermission("BT"))
                            getBillSplitPopup();
                    }
                },500);
                break;

            case 21:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!UserPermission("TT"))
                            getBillTransferPopup();
                    }
                },500);

                break;
        }
    }


    public void showQtyPopup(final MenuItem menuItem){

        String[] qty = new String[99];

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_DARK);
        //AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_TRADITIONAL);
        //AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_quantity, null);

        ListView listview_qty = (ListView) view.findViewById(R.id.lv_qty);
        ArrayAdapter<String> adapter_qty = new ArrayAdapter<String>(context, R.layout.list_qty_layout, qty);

        for (int i = 0; i < 99; i++)
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

                setQuantity(menuItem, position+1);
                dialog.dismiss();
                addMixer();

               /* //setQuantity(menuItem, position);
                dialog.dismiss();
                addMixer();*/
            }
        });
    }

    public void addMixer(){

        frameLayout_container.setVisibility(View.VISIBLE);
        Add_Mixer_Fragment addMixerFragment = new Add_Mixer_Fragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout_container, addMixerFragment);
        transaction.commit();
    }

   /* public void setQuantity(MenuItem menuItem, int quantity) {
        menuItem.setQuantity(quantity+1);
        menuItem.setMenuAmount(menuItem.getMenu_price() * (quantity + 1));
        onclickEvent(menuItem);    // on MenuItem click
    }*/

    public void setQuantity(MenuItem menuItem, int quantity) {
        menuItem.setQuantity(quantity);
        menuItem.setMenuAmount(menuItem.getMenu_price() * (quantity));
        //onclickEvent(menuItem);    // on MenuItem click
        showMultiQty(menuItem);

    }

    @Override
    public void onPause() {
        super.onPause();

        if (statusScheduler != null && ! statusScheduler.isShutdown())
            statusScheduler.shutdownNow();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (orderList.size() > 0){
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

                    if (! previous_cat.equals(c.getString(c.getColumnIndex("cat_code")))) {

                        CategoryItem categoryItem = new CategoryItem();
                        categoryItem.setCategory_Code(c.getString(c.getColumnIndex("cat_code")));
                        categoryItem.setCategory_Name(c.getString(c.getColumnIndex("cat_desc")));
                        categoryItem.setCategory_Image_Url(BaseNetwork.defaultUrlMethod(serverIP,
                                "/Image/" + categoryItem.getCategory_Code() + ".png"));


                        ArrayList<MenuItem> menuItemList = new ArrayList<>();
                        MenuItem menuItem = new MenuItem();
                        menuItem.setMenu_code(c.getString(c.getColumnIndex("item_code")));
                        menuItem.setMenu_name(c.getString(c.getColumnIndex("item_desc")));
                        menuItem.setInc_Rate(Float.parseFloat(c.getString(c.getColumnIndex("inc_rate"))));
                        menuItem.setMenu_price(Float.parseFloat(c.getString(c.getColumnIndex("cur_rate"))));
                        menuItem.setMax_Price(Float.parseFloat(c.getString(c.getColumnIndex("max_price"))));
                        menuItem.setMin_Price(Float.parseFloat(c.getString(c.getColumnIndex("min_price"))));
                        menuItemList.add(menuItem);

                        mTITLES.add(new Items(new GroupItems(), categoryItem, menuItemList));
                        previous_cat = c.getString(c.getColumnIndex("cat_code"));

                    }else {

                        ArrayList<MenuItem> menuItemList = mTITLES.get(mTITLES.size()-1).getMenuItemList();
                        MenuItem menuItem = new MenuItem();
                        menuItem.setMenu_code(c.getString(c.getColumnIndex("item_code")));
                        menuItem.setMenu_name(c.getString(c.getColumnIndex("item_desc")));
                        menuItem.setInc_Rate(c.getFloat(c.getColumnIndex("inc_rate")));
                        menuItem.setMenu_price(c.getFloat(c.getColumnIndex("cur_rate")));
                        menuItem.setMax_Price(c.getFloat(c.getColumnIndex("max_price")));
                        menuItem.setMin_Price(c.getFloat(c.getColumnIndex("min_price")));
                        menuItemList.add(menuItem);
                    }

                }while (c.moveToNext());

            }
            c.close();
            mdb.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mdb.endTransaction();
        }

        if (! mTITLES.isEmpty()) {

            TITLES.clear();
            TITLES.addAll(mTITLES);

            pager.removeAllViews();
            pager.setAdapter(adapter);
            tabs.setViewPager(pager);
        }

        updateStatusPendingOrders();
    }

    public void updateStatusPendingOrders(){

        orderList = new ArrayList<>();

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
        mdb.beginTransaction();

        try {
            Cursor cursor = mdb.rawQuery("Select * from " + DBConstants.KEY_GUEST_ORDERS_TABLE +
                    " where " + DBConstants.KEY_GUEST_ORDER_STATUS  +  "= 'K' ", null);

            if (cursor.moveToFirst() ){

                do {
                    orderList.add(cursor.getString(1));
                }while (cursor.moveToNext());

            }

            cursor.close();
            mdb.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {mdb.endTransaction();
        }

        if (!orderList.isEmpty())
            scheduleExecutors(orderList.get(0));

    }

    /*************** Complete flow to control Active Notification **********************/

    public void scheduleExecutors(final String orderNum){

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
                        StewardOrderFragment.this, null);
                statusTask.execute();


            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    public void sendNotification(String orderNum, String status) {

        if (status.equals("failure")){
            statusScheduler.shutdownNow();
            Toast.makeText(context,"Connection failed",Toast.LENGTH_SHORT).show();
            return;
        }

        if (! orderList.contains(orderNum) || status.isEmpty())
            return;

        if (getActivity() != null && isAdded()){

            if (status.equals("B") || status.equals("C")){

                if (status.equals("B"))
                    ((BaseFragmentActivity)context).sendNotification(getString(R.string.accepted_noti, UserInfo.guest_name, orderNum));
                else
                    ((BaseFragmentActivity)context).sendNotification(getString(R.string.rejected_noti, UserInfo.guest_name, orderNum));

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

    public void saveOrderStatus(String orderNum, String status){

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
        mdb.beginTransaction();
        try {

            ArrayList<OrderItem> orderItems =  takeOrderAdapter.getDataSet();
            for (int i =0; i<takeOrderAdapter.getCount();i++){

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

        } catch (Exception ex) {ex.printStackTrace();}
        finally {mdb.endTransaction();}

        //clearData();

    }

    public void updateOrderStatus(String orderNum, String status){

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
        mdb.beginTransaction();
        try {

            ContentValues cv = new ContentValues();
            cv.put(DBConstants.KEY_GUEST_ORDER_NUMBER, orderNum);
            cv.put(DBConstants.KEY_GUEST_ORDER_STATUS, status);

            mdb.update(DBConstants.KEY_GUEST_ORDERS_TABLE, cv, DBConstants.KEY_GUEST_ORDER_NUMBER + "= '" + orderNum + "' ", null);
            mdb.setTransactionSuccessful();

        } catch (Exception ex) {ex.printStackTrace();}
        finally {mdb.endTransaction();}
    }

    /* ******************************************************************* */

    private String extractLogToFile() {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo (context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
        }
        String model = Build.MODEL;
        if (!model.startsWith(Build.MANUFACTURER))
            model = Build.MANUFACTURER + " " + model;

        // Make file name - file must be saved to external storage or it wont be readable by
        // the email app.

        File appDirectory = new File( Environment.getExternalStorageDirectory() + "/StewardPad" );
        File logDirectory = new File( appDirectory + "/log" );
        File logFile = new File( logDirectory, "logcat.txt" );

        // create app folder
        if ( !appDirectory.exists() ) {
            appDirectory.mkdir();
        }

        // create log folder
        if ( !logDirectory.exists() ) {
            logDirectory.mkdir();
        }
        /*String path = Environment.getExternalStorageDirectory() + "/" + "MyApp/";
        String fullName = path + "log" + System.currentTimeMillis() + ".txt";

        // Extract to file.
        File file = new File (fullName);
        */

        InputStreamReader reader = null;
        FileWriter writer = null;

        try
        {
            // For Android 4.0 and earlier, you will get all app's log output, so filter it to
            // mostly limit it to your app's output.  In later versions, the filtering isn't needed.
            String cmd = (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) ?
                    "logcat -d -v time MyApp:v dalvikvm:v System.err:v *:s" :
                    "logcat -d -v time";

            // get input stream
            Process process = Runtime.getRuntime().exec(cmd);
            reader = new InputStreamReader (process.getInputStream());

            // write output stream
            writer = new FileWriter(logFile);
            writer.write ("Android version: " +  Build.VERSION.SDK_INT + "\n");
            writer.write ("Device: " + model + "\n");
            writer.write ("App version: " + (info == null ? "(null)" : info.versionCode) + "\n");

            char[] buffer = new char[10000];
            do
            {
                int n = reader.read (buffer, 0, buffer.length);
                if (n == -1)
                    break;
                writer.write (buffer, 0, n);
            } while (true);

            reader.close();
            writer.close();
        }
        catch (IOException e)
        {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e1) {
                }
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e1) {
                }

            // You might want to write a failure message to the log here.
            return null;
        }

        return "";
    }

    // Setting Currency View
    public void setCurrencyView(View v){

        if (currencyHasCreated()) {

            layout_Settle= (LinearLayout) v.findViewById(R.id.layout_settle);
            layout_bill_settle = (LinearLayout) v.findViewById(R.id.layout_settle_up);
            gridViewCurrency = (GridView) v.findViewById(R.id.gridview_show_currency);

            currencyAdapter = new ArrayAdapter<>(context, R.layout.currency_value, currencyList);
            gridViewCurrency.setAdapter(currencyAdapter);
            gridViewCurrency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String amount = ((TextView) view).getText().toString().substring(2);
                    getCashAmount(amount, "CURR");
                }
            });
        }
    }

    public boolean currencyHasCreated() {

        if (currencyList.isEmpty()){

            SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
            mdb.beginTransaction();

            try {

                Cursor cursor = mdb.query(DBConstants.KEY_CURRENCY_TABLE, new String[]{
                                DBConstants.KEY_CURRENCY_SR,DBConstants.KEY_CURRENCY_LABEL,
                                DBConstants.KEY_CURRENCY_VALUE,DBConstants.KEY_CURRENCY_FLAG,
                                DBConstants.KEY_CURRENCY_PIC}, null, null,
                        null, null, null);

                if (cursor.moveToFirst()) {

                    do {

                        currencyList.add(("? " + cursor.getString(2)));

                    }while (cursor.moveToNext());

                }

                cursor.close();
                mdb.setTransactionSuccessful();

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mdb.endTransaction();
            }
        }

        return ! currencyList.isEmpty();
    }

    public void showCurrencyInPortraitView(boolean flag){

        if (! currencyList.isEmpty()){

            if (flag){

                // Setting Currency Orientation for Portrait View
                layout_Settle.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams sett = new LinearLayout.LayoutParams(
                        0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                LinearLayout.LayoutParams curr = new LinearLayout.LayoutParams(
                        0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

                layout_bill_settle.setLayoutParams(sett);
                gridViewCurrency.setLayoutParams(curr);
                gridViewCurrency.setNumColumns(2);

            }else {

                // Setting Currency Orientation  for LandScape View
                layout_Settle.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams sett = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
                LinearLayout.LayoutParams curr = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                layout_bill_settle.setLayoutParams(sett);
                gridViewCurrency.setLayoutParams(curr);
                gridViewCurrency.setNumColumns(5);
            }
        }
    }


    // Handle layout configuration for Vertical view in device.
    public void flipForPortrait(){

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            layout_flipMode.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.2f);

            p.setMargins(2, 1, 1, 1);
            layout_menu.setLayoutParams(p);

            LinearLayout.LayoutParams p_order = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);

            p_order.setMargins(2, 1, 1, 1);
            layout_order.setLayoutParams(p_order);

            showCurrencyInPortraitView(true);

        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            layout_flipMode.setOrientation(LinearLayout.HORIZONTAL);

    }

    // on configuration changed save the instance and run flip mode
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            layout_flipMode.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1.2f);
            p.setMargins(1, 1, 1, 1);
            layout_menu.setLayoutParams(p);

            LinearLayout.LayoutParams p_order = new LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1);
            p_order.setMargins(0, 1, 1, 1);
            layout_order.setLayoutParams(p_order);

            showCurrencyInPortraitView(false);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            layout_flipMode.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.2f);
            p.setMargins(2, 1, 1, 1);
            layout_menu.setLayoutParams(p);

            LinearLayout.LayoutParams p_order = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            p_order.setMargins(2, 1, 1, 1);
            layout_order.setLayoutParams(p_order);

            showCurrencyInPortraitView(true);
        }
    }


    public void showItemByFilter(){

        if (listViewSearchItem.getVisibility() == View.GONE){
            listViewSearchItem.setVisibility(View.VISIBLE);
            radioGroupVegNonveg.setVisibility(View.VISIBLE);
            showFilteredMenuItems("A");
        }
    }

    public void showFilteredMenuItems(String itemType) {

        menu_search_list.clear();

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        try {

            String queryMenuItem = itemType.equals("A") ?
                    "Select * from " + DBConstants.KEY_MENU_ITEM_TABLE + " WHERE "
                            + DBConstants.KEY_MENU_POS_CODE + "='" + posItem.posCode + "'" :
                    "Select * from "
                            + DBConstants.KEY_MENU_ITEM_TABLE + " WHERE "
                            + DBConstants.KEY_MENU_VEG_NON + "='" + itemType + "' AND "
                            + DBConstants.KEY_MENU_POS_CODE + "='" + posItem.posCode + "'";

            Cursor cursor = mdb.rawQuery(queryMenuItem, null);
            if (cursor.moveToFirst()) {
                do {

                    MenuItem menuItem = new MenuItem();
                    menuItem.setMenu_code(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_MENU_CODE)));
                    menuItem.setMenu_name(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_MENU_NAME)));
                    menuItem.setMenu_color(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_MENU_COLOR)));
                    menuItem.setMenu_sub_item(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_MENU_SUB_ITEM)));
                    menuItem.setMenu_combo(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_MENU_COMBO_FLAG)));
                    menuItem.setMenu_addon(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_MENU_ADDON_FLAG)));
                    menuItem.setMenu_mod(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_MENU_MODIFIER)));
                    menuItem.setMenu_group_code(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_MENU_GRP_CODE)));
                    menuItem.setMenu_item_url(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_MENU_CODE)));
                    menuItem.setMenu_sub_unit(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_MENU_SUB_UNIT)));
                    menuItem.setMenu_open_item_flag(Float.valueOf(Util.
                            checkStringIntIsEmpty(cursor.getString(cursor
                                    .getColumnIndex(DBConstants.KEY_MENU_OPEN_ITEM)))));
                    menuItem.setMenu_veg(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_MENU_VEG_NON)));
                    menuItem.setMenu_calories(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_MENU_CALORIES)));

                    menuItem.setMenu_price(Float.valueOf(Util
                            .checkStringFloatIsEmpty(cursor.getString(cursor
                                    .getColumnIndex(DBConstants.KEY_MENU_PRICE)))));
                    menu_search_list.add(new Items(new GroupItems(),
                            new CategoryItem(), menuItem));

                } while (cursor.moveToNext());

                cursor.close();
                mdb.setTransactionSuccessful();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

        adapter_menu_search.notifyDataSetChanged();
    }


    private SlidingUpPanelLayout.PanelSlideListener PageSliderLister() {

        return new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelCollapsed(View panel) {

                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
                ll_bottomHeader.startAnimation(animation);
                ll_bottomHeader.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPanelExpanded(View panel) {

                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
                ll_bottomHeader.startAnimation(animation);
                ll_bottomHeader.setVisibility(View.GONE);
            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {

            }
        };
    }

    @Override
    public void onAttach(Context activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void hideOrderVisibility() {

        listViewOrderItem.setVisibility(View.GONE);
    }

    @Override
    public void showOrderVisibility() {

        listViewOrderItem.setVisibility(View.VISIBLE);
    }


    public boolean showSlidingPanel(){

        if (slidingUpPanelLayout != null){

            if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED)
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            else
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }

        return slidingUpPanelLayout != null ;
    }

    public void showDiscountList() {
        frameLayout_discount.addView(discountLayout.addDiscLayout());
        if (discountLayout.showList) {
            listViewOrderItem.setVisibility(View.GONE);
            frameLayout_discount.setVisibility(View.VISIBLE);
        }
    }

    public void addSeparator() {

        if (!takeOrderAdapter.isEmpty()) {

            OrderItem orderItem = new OrderItem();
            orderItem.o_code = "";
            orderItem.o_name = "";

            takeOrderAdapter.addDataSetItem(orderItem);
            discountLayout.createDiscList("0", "", 0);
            codeList.add("");
            updateScrolling();
        }
    }


    @Override
    public boolean getPosVisibility() {

        return (addonLayoutContainer.getTag() == null && posLayoutContainer.getTag() == null);
    }

    @Override
    public void onClick(View v) {

        OrderDetail detail = takeOrderAdapter.getOrderDetail();
        detail.setTableItem(tableItem);
        takeOrderAdapter.setOrderDetail(detail);

        switch (v.getId()) {

            case R.id.ll_bottomHeader:
                showSlidingPanel();
                break;

            case R.id.txtOrderOk:
                layoutShowBillDetail.setVisibility(View.GONE);
                layoutShowHoldOrderDetail.setVisibility(View.GONE);
                txtorderOk.setVisibility(View.GONE);
                showDefault();
                break;

            case R.id.tv_cash:
                showKeypad("CS");
                break;

            case R.id.tv_credit:
                CreditDetailPopup creditDetailPopup = new CreditDetailPopup(context, this);
                creditDetailPopup.show();
                break;

            case R.id.textview_cash:
                showKeypad("CS");
                break;

            case R.id.textview_credit:
                CreditDetailPopup creditDetailPopup1 = new CreditDetailPopup(context, this);
                creditDetailPopup1.show();
                break;


            case R.id.selectTable:      // called when Table View is clicked, displays list of all tables via GridView.
                if (layoutShowBillDetail.getVisibility() == View.GONE)
                    getAllTables();
                break;

            case R.id.selectRoom:      // called when Room View is clicked, displays a list of all rooms via GridView.
                getAllRooms();
                break;

            case R.id.selectGuest:
                getAllGuest();
                break;

            case R.id.selectDelBoy:
                getAllDelBoy();
                break;

            case R.id.newOrder:     // called when Order View is clicked, displays all orders under that table in GridView.

                try {

                    if (layoutShowBillDetail.getVisibility() == View.GONE) {

                        if (frameLayout_container.getVisibility() == View.VISIBLE)
                            showDefault();

                        else if (!selectTable.getText().equals("Table"))
                            showRunningOrders();

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
                break;

            case R.id.txtOrderSubmit:

              /*  if (UserInfo.Format_no != null) {
                    if (UserInfo.Format_no.equals("64")) {
                        BluetoothPrintConnect64();
                    } else {
                        if (UserInfo.Format_no.equals("65")) {
                            BluetoothPrintConnect65();
                        }
                    }
                }*/

                String posType = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posType;

                if (layoutShowBillDetail.getVisibility() == View.VISIBLE || ll_settle_option.getVisibility() == View.VISIBLE)
                    doBillSettle();

                else if (txtOrderSubmit.getText().toString().equals(getResources().getString(R.string.save_string))) {

                    if (flag.equals("C")) {

                        if (permission("BC", "Select Bill No First"))
                            Util.show_Bill_edit_popup(context, StewardOrderFragment.this, false, billEditLayout.cancelFlag);

                    } else if (flag.equals("U")) {

                        if (permission("US", "Select Bill No First"))
                            Util.show_undoSettle_reason_popup(context, StewardOrderFragment.this);
                    } else
                        submitHomeOrder("G");   // G- Generate Bill of Hold Order

                } else if (txtOrderSubmit.getText().toString().equals(getResources().getString(R.string.update_bill))) {

                    if (permission("BM", "Select Bill No First")){

/*                        if (rsn_bill_modi)
                            Util.show_Bill_edit_popup(context, this, true, "");
                        else
                            getBillEditReason("", true);*/

                        Util.show_Bill_edit_popup(context, StewardOrderFragment.this, true, "");
                    }

                } else if (txtOrderSubmit.getText().toString().equals(getResources().getString(R.string.create_guest))) {

                    frameLayout_container.removeAllViews();
                    frameLayout_container.addView(guestDiscountLayout.getGuestDiscountLayout());
                    ll_bottomOrder.setVisibility(View.GONE);

                }/*else if(txtOrderSubmit.getText().toString().equals(getResources().getString(R.string.create_emp))){

                    frameLayout_container.removeAllViews();
                    frameLayout_container.addView(guestDiscountLayout.getGuestDiscountLayout());
                    ll_bottomOrder.setVisibility(View.GONE);

                }*/
                else if ((posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_H) ||
                        posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_T))) {

                    submitHomeOrder("Y");   // Y- Yes, Generate Bill Immediately

                } else if (txtOrderSubmit.getText().toString().equals(getResources().getString(R.string.submit_string))) {

                    if (selectTable.getVisibility() == View.VISIBLE && selectTable.getText().equals("Table"))
                        getAllTables();

                    else if (selectRoom.getVisibility() == View.VISIBLE && selectRoom.getText().equals("Room"))
                        getAllRooms();

                    else if (!takeOrderAdapter.isEmpty()) {

                        if (orderRemark.isEmpty() && odr_rem_mandat){

                            OrderRemarkPopup orderRemarkPopup = new OrderRemarkPopup(context,this);
                            orderRemarkPopup.show();

                        }else if (mandatoryPopup())
                            submitOrder();
                    }

                } else if (txtOrderSubmit.getText().toString().equals(getResources().getString(R.string.update_string))) {

                    if (permission("OM", "Select Order First")){

                        /*if (odr_modi_rsn_mandat)
                            Util.show_new_order_remark_popup(context, this, true);
                        else
                            getOrderEditReason("", true);*/

                        Util.show_new_order_remark_popup(context, StewardOrderFragment.this, true);

                    }

                } else if (txtOrderSubmit.getText().toString().equals(getResources().getString(R.string.cancel_string))
                        || txtOrderSubmit.getText().toString().equals(getResources().getString(R.string.cancel_order))
                        || txtOrderSubmit.getText().toString().equals(getResources().getString(R.string.change_to_comp))) {

                    if (permission("OC", "Select Order First"))
                        Util.show_new_order_remark_popup(context, StewardOrderFragment.this, false);
                }

                break;


            case R.id.txtOrderClear:

                /*try {

                    new ReadGmailTask(context).execute();
                    //new GmailReadTask().execute();

                }catch (Exception e){
                    e.printStackTrace();
                }*/

                if (discountLayout.listViewShowDiscount != null &&
                        discountLayout.listViewShowDiscount.getVisibility() == View.VISIBLE) {
                    discountLayout.listViewShowDiscount.setVisibility(View.GONE);
                    listViewOrderItem.setVisibility(View.VISIBLE);

                } else if (frameLayout_container.getVisibility() == View.VISIBLE)
                    showDefault();

                else if (listViewSearchItem.getVisibility() == View.VISIBLE) {

                    listViewSearchItem.setVisibility(View.GONE);
                    radioGroupVegNonveg.setVisibility(View.GONE);
                    listViewOrderItem.setVisibility(View.VISIBLE);

                } else if (layout_bill_operation.getVisibility() == View.VISIBLE) {

                    if (billEditLayout != null)         billEditLayout.clearDetail();
                    else if (holdOrdersLayout != null)  holdOrdersLayout.clearDetail();

                    cashPaid = 0;
                    creditPaid = 0;
                    codeList.clear();
                    discountLayout.clearDiscList();
                    takeOrderAdapter.clearDataSet();
                    takeOrderAdapter.notifyDataSetChanged();
                    layoutShowBillDetail.setVisibility(View.GONE);
                    selectTable.setText(getResources().getString(R.string.table_string));
                    selectGuest.setText(getResources().getString(R.string.guest_string));
                    selectDelBoy.setText(getResources().getString(R.string.del_boy));

                } else if (layoutShowBillDetail.getVisibility() == View.VISIBLE ||
                        layoutShowHoldOrderDetail.getVisibility() == View.VISIBLE) {

                    showDefault();
                    layoutShowBillDetail.setVisibility(View.GONE);
                    layoutShowHoldOrderDetail.setVisibility(View.GONE);
                    ll_settle_option.setVisibility(View.GONE);
                    selectTable.setText(getResources().getString(R.string.table_string));
                    selectGuest.setText(getResources().getString(R.string.guest_string));
                    selectDelBoy.setText(getResources().getString(R.string.del_boy));

                } else if (takeOrderAdapter.isEmpty()){

                    String activeView = txtOrderSubmit.getText().toString();

                    if (activeView.equals(getResources().getString(R.string.submit_string)) ||
                            activeView.equals(getResources().getString(R.string.hold_order))) {

                        selectTable.setText(getResources().getString(R.string.table_string));
                        selectGuest.setText(getResources().getString(R.string.guest_string));
                        selectDelBoy.setText(getResources().getString(R.string.del_boy));
                    }

                    if (activeView.equals(getResources().getString(R.string.update_string)) ||
                            activeView.equals(getResources().getString(R.string.Cancel_string))){

                        selectTable.setText(getResources().getString(R.string.table_string));
                        selectGuest.setText(getResources().getString(R.string.guest_string));
                        txtOrderSubmit.setText(getResources().getString(R.string.submit_string));
                    }


                    if (layoutGuestName.getVisibility() == View.VISIBLE){
                        layoutGuestName.setVisibility(View.GONE);
                        textViewGuestName.setText("");
                    }

                } else if (addonLayoutContainer.getTag() == null && posLayoutContainer.getTag() == null) {

                    codeList.clear();
                    discountLayout.clearDiscList();
                    takeOrderAdapter.clearDataSet();
                    takeOrderAdapter.notifyDataSetChanged();
                    layoutShowBillDetail.setVisibility(View.GONE);
                }

                //SendSmtpMail.sendMessage("rgrahulgupta001@gmail.com", "Hello");
                break;

            case R.id.tvHoldOrder:
                if (selectGuest.getText().toString().equals("Guest"))
                    showAllGuest();
                else
                    submitHomeOrder("N");   // N- No, Don't Generate Bill for Hold Order
                break;


            case R.id.imgGuestSearch1:
                showOptionsList();
                break;

            case R.id.imgGuestSearch2:
                showOptionsList();
                break;

            case R.id.tv_add_item:
                layout_bill_operation.setVisibility(View.GONE);
                relativeLayoutItemList.setVisibility(View.VISIBLE);
                tv_add_item.setVisibility(View.GONE);
                tv_show_bill.setVisibility(View.VISIBLE);
                break;


            case R.id.tv_show_bill:
                layout_bill_operation.setVisibility(View.VISIBLE);
                relativeLayoutItemList.setVisibility(View.GONE);
                tv_show_bill.setVisibility(View.GONE);
                tv_add_item.setVisibility(View.VISIBLE);
                break;

            case R.id.tv_generate_bill:
                if (!UserPermission("BG"))
                    ((BaseFragmentActivity) context).onCreateBillGenerate("","");
                break;

            case R.id.tv_item_discount:
                showDiscount();
                break;

            case R.id.tv_item_cover:
                CoverPopup coverPopup = new CoverPopup(context, this);
                coverPopup.show();
                break;

            case R.id.tv_item_steward:
                SelectStewardPopup selectStewardPopup = new SelectStewardPopup(context, this);
                selectStewardPopup.show();
                break;

            case R.id.tv_item_guest:
                if (showSlidingPanel())
                    showAllGuest();
                else
                    getAllGuest();

                break;

            case R.id.tv_order_cancel:
                tv_odr_cancel_type.setText(tv_order_cancel.getText().toString());
                break;

            case R.id.tv_chngeToComp:
                tv_odr_cancel_type.setText(tv_change_to_comp.getText().toString());
                break;

            case R.id.cancel_operation:
                layout_order_cancel.setVisibility(View.GONE);
                showDefault();
                break;
        }
    }

    public void showDiscount(){

        if (!UserPermission("DI")){

            if (showSlidingPanel())
                showDiscountList();

            else if (discountLayout.listViewShowDiscount != null &&
                    discountLayout.listViewShowDiscount.getVisibility() == View.VISIBLE) {
                discountLayout.listViewShowDiscount.setVisibility(View.GONE);
                listViewOrderItem.setVisibility(View.VISIBLE);

            } else
                showDiscountList();
        }

    }

    public void showRunningTables(String flag) {

        this.flag = flag;
        listViewOrderItem.setVisibility(View.GONE);
        ll_bottomOrder.setVisibility(View.GONE);

        String parameter = UtilToCreateJSON.createTableJSON(context);
        String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();

        if (flag.equals("M")) {

            frameLayout_container.setVisibility(View.VISIBLE);
            frameLayout_container.removeAllViews();
            frameLayout_container.addView(tableNumberLayout.getRunningTablePopupWindow(parameter, serverIP));

        } else {

            frameLayout_container.setVisibility(View.GONE);
            frameLayout_container.removeAllViews();
            frameLayout_Cancel.setVisibility(View.VISIBLE);
            frameLayout_Cancel.removeAllViews();
            frameLayout_Cancel.addView(tableNumberLayout.getRunningTablePopupWindow(parameter, serverIP));
            layout_order_cancel.setVisibility(View.VISIBLE);
        }
    }

    public void showRunningOrders() {

        String table = takeOrderAdapter.getOrderDetail().getTableItem().getCode();

        String parameter = UtilToCreateJSON.createOrderNoParameter(context, table);
        String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();

        frameLayout_container.setVisibility(View.VISIBLE);
        frameLayout_container.addView(orderNumberLayout.getOrderNumberPopupWindow(parameter, serverIP, flag));
        ll_bottomOrder.setVisibility(View.GONE);
        this.flag = "";
    }



    @Override
    public void getCashAmount(String cash, String payMode) {

        float total = Float.parseFloat(textviewForTotal.getText().toString());
        float tendered = Float.parseFloat(textViewTAmt.getText().toString()) + Float.parseFloat(cash);
        float change = tendered - total;

        textViewTAmt.setText(String.format("%.2f", tendered));
        textViewChange.setText(String.format("%.2f", change));

        if ( ! cash.equals("0.0")){

            if (payMode.equals("CURR")){
                cashPaid = cashPaid + Float.parseFloat(cash);
                textViewStlMode.setText("CASH : " + cashPaid + " ");

            } else if (payMode.equals("CS")) {
                textViewStlMode.append("CASH" + " : " + cash + "\n");
                cashPaid = cashPaid + Float.parseFloat(cash);

            } else {
                textViewStlMode.append(homeItem.getCreditDesc() + " : " + cash + "\n");
                creditPaid = creditPaid + Float.parseFloat(cash);
            }
        }
    }

    @Override
    public void getCreditAmount(HomeItem homeItem) {

        this.homeItem = homeItem;

        if (textViewChange.getText().toString().contains("-"))
            showKeypad("CR");
    }


    public void showKeypad(String payMode) {

        float total = Float.parseFloat(textviewForTotal.getText().toString());
        float tendered = Float.parseFloat(textViewTAmt.getText().toString());
        float balance = total < tendered ? 0 : total - tendered;

        AmountAcceptor keypad = new AmountAcceptor(context, this, String.valueOf(balance), payMode);
        keypad.show();
    }


    public void showGuestCompProfile(int position) {

        frameLayout_container.removeAllViews();
        frameLayout_container.setVisibility(View.GONE);
        listViewOrderItem.setVisibility(View.GONE);
        ll_bottomOrder.setVisibility(View.VISIBLE);

        switch (position) {

            case 0:

                if (frameLayout_container.getVisibility() == View.VISIBLE) {
                    frameLayout_container.removeAllViews();
                    frameLayout_container.setVisibility(View.GONE);
                    ll_bottomOrder.setVisibility(View.VISIBLE);

                } else {

                    frameLayout_container.setVisibility(View.VISIBLE);
                    frameLayout_container.addView(guestDiscountLayout.getGuestDiscountLayout());
                    ll_bottomOrder.setVisibility(View.GONE);
                }

                break;


            case 1:

                if (frameLayout_container.getVisibility() == View.VISIBLE) {
                    frameLayout_container.removeAllViews();
                    frameLayout_container.setVisibility(View.GONE);
                    ll_bottomOrder.setVisibility(View.VISIBLE);

                } else {

                    frameLayout_container.setVisibility(View.VISIBLE);
                    frameLayout_container.addView(companyDiscountLayout.getCompanyDiscountPopupWindow());
                    ll_bottomOrder.setVisibility(View.GONE);
                }

                break;
        }
    }

    public void submitOrder() {

        try {

            steward = steward.isEmpty() ? userID : steward;
            String parameter = UtilToCreateJSON.createSendSyncJSON(
                    context, takeOrderAdapter, order_type, orderRemark, guest_code,
                    steward, cover, discountLayout, companyCode);
            String serverIP = POSApplication.getSingleton()
                    .getmDataModel().getUserInfo().getServerIP();
            if (!(TextUtils.isEmpty(parameter) && TextUtils.isEmpty(serverIP))) {

                SendOrderTask sendOrderTask = new SendOrderTask(context, parameter,
                        serverIP, StewardOrderFragment.this);
                sendOrderTask.execute();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void submitHomeOrder(String flag) {

        this.flag = flag ;

        if (!takeOrderAdapter.isEmpty()) {

            try {

                if (! flag.equals("G") && selectGuest.getText().toString().equals("Guest") && gst_mandate)
                    getAllGuest();

                else {

                    String orderNO = layout_bill_operation.getVisibility() == View.VISIBLE ?
                            holdOrdersLayout.textviewForOrderno.getText().toString() : "";

                    String parameter = UtilToCreateJSON.createSendHomeDelevryJSON(
                            context, takeOrderAdapter, orderNO, discountLayout, companyCode, order_type,
                            orderRemark, guest_code, steward, cover, flag);
                    String serverIP = POSApplication.getSingleton()
                            .getmDataModel().getUserInfo().getServerIP();

                    if (!(TextUtils.isEmpty(parameter) && TextUtils.isEmpty(serverIP))) {

                        SendOrderHomeTask sendOrderHomeTask = new SendOrderHomeTask(context, parameter,
                                serverIP, this);
                        sendOrderHomeTask.execute();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void checkGuestMandate() {

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
        mdb.beginTransaction();

        try {

            Cursor cursor = mdb.query(DBConstants.KEY_ATTRIBUTE_TABLE, new String[]{
                            DBConstants.KEY_CHECK_HOME_ORDER_GUEST_MANDATE,
                            DBConstants.KEY_CHECK_HOME_HOLD_ORDER}, null, null,
                    null, null, null);

            if (cursor.moveToFirst()) {

                gst_mandate = cursor.getString(0).equals("1");
                form_8 = cursor.getString(1).equals("8");
            }

            cursor.close();
            mdb.setTransactionSuccessful();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }
    }

    public void getAttributes(){

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
        mdb.beginTransaction();

        try {

            Cursor cursor = mdb.query(DBConstants.KEY_ATTRIBUTE_TABLE, new String[]{
                            DBConstants.KEY_NO_REASON_ORDER_MODIFICATION,
                            DBConstants.KEY_PRINT_CANCEL_MODIFY, DBConstants.KEY_ORDER_REMARK_MANDATORY,
                            DBConstants.KEY_NO_REASON_BILL_MODIFICATION, DBConstants.KEY_NO_GENERATE_STW_BILL,
                            DBConstants.KEY_ACTIVATE_REMARK_AS_GUEST, DBConstants.KEY_GROUP_WISE_BILLING,
                            DBConstants.KEY_HIDE_BILL_REMARK, DBConstants.KEY_NO_DELETE_BTN_BILL_EDIT,
                            DBConstants.KEY_NO_MODIFY_BTN_BILL_EDIT,DBConstants.KEY_DISCOUNT_REASON_MANDATORY},
                    null, null, null, null, null);

            if (cursor.moveToFirst()) {

                odr_modi_rsn_mandat = cursor.getString(0).equals("y");
                print_order = cursor.getString(1).equals("y");
                odr_rem_mandat = cursor.getString(2).equals("y");
                rsn_bill_modi= cursor.getString(3).equals("y");
                gen_stw_bill= cursor.getString(4).equals("y");
                rem_as_gst= cursor.getString(5).equals("y");
                grp_wise_billing = cursor.getString(6).equals("y");
                hide_bill_remark= cursor.getString(7).equals("y");
                del_bill_edit = cursor.getString(8).equals("y");
                mod_bill_edit = cursor.getString(9).equals("y");
                disc_reason = cursor.getString(10).equals("y");
            }

            cursor.close();
            mdb.setTransactionSuccessful();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onBackPress() {

        if (mainContainer.getVisibility() == View.VISIBLE) {
            mainContainer.removeAllViews();
            mainContainer.setVisibility(View.GONE);
            return true;

        } else if (slidingUpPanelLayout != null
                && slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {

            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            String activeView = txtOrderSubmit.getText().toString();
            showDefault();
            txtOrderSubmit.setText(activeView);
            return true;

        } else if (layout_bill_operation.getVisibility() == View.VISIBLE) {

            layout_bill_operation.removeAllViews();
            layout_bill_operation.setVisibility(View.GONE);
            layoutShowBillDetail.setVisibility(View.GONE);
            ll_settle_option.setVisibility(View.GONE);
            relativeLayoutItemList.setVisibility(View.VISIBLE);
            layout_order_options.setVisibility(View.VISIBLE);
            tv_add_item.setVisibility(View.GONE);
            tv_show_bill.setVisibility(View.GONE);

            selectGuest.setText(getResources().getString(R.string.guest_string));
            selectDelBoy.setText(getResources().getString(R.string.del_boy));
            selectTable.setText(getResources().getString(R.string.table_string));

            codeList.clear();
            discountLayout.clearDiscList();
            takeOrderAdapter.clearDataSet();
            takeOrderAdapter.notifyDataSetChanged();
            showDefault();

            return true;

        } else if (addonLayoutContainer.getVisibility() == View.VISIBLE) {
            addonLayoutContainer.removeAllViews();
            addonLayoutContainer.setTag(null);
            addonLayoutContainer.setVisibility(View.GONE);
            return true;

        } else if (posLayoutContainer.getTag() != null &&
                (posLayoutContainer.getTag().toString().equals(StaticConstants.MODIFIER_POPUP_TAG) ||
                        posLayoutContainer.getTag().toString().equals(StaticConstants.COMBO_POPUP_TAG))) {

            posLayoutContainer.removeAllViews();
            posLayoutContainer.setTag(null);
            posLayoutContainer.setVisibility(View.GONE);
            return true;

        } else if (frameLayout_container.getVisibility() == View.VISIBLE ||
                layout_order_cancel.getVisibility() == View.VISIBLE) {

            layout_order_cancel.setVisibility(View.GONE);
            frameLayout_Cancel.removeAllViews();
            showDefault();
            return true;

        } /*else if (tabs.getVisibility() == View.VISIBLE) {

            TITLES.set(0, new Items(new GroupItems("", "Home", ""), new CategoryItem(), new MenuItem()));

            adapter = new MyPagerAdapter(getChildFragmentManager());
            pager.setAdapter(adapter);
            tabs.setViewPager(pager);
            tabs.setVisibility(View.GONE);
            frameLayout_container.setVisibility(View.GONE);
            return true;

        }*/ else if (listViewSearchItem.getVisibility() == View.VISIBLE){
            listViewSearchItem.setVisibility(View.GONE);
            radioGroupVegNonveg.setVisibility(View.GONE);
            return true;

        } else if (! takeOrderAdapter.isEmpty() || tv_show_bill.getVisibility() == View.VISIBLE) {

            codeList.clear();
            discountLayout.clearDiscList();
            takeOrderAdapter.clearDataSet();
            takeOrderAdapter.notifyDataSetChanged();
            selectTable.setText(getResources().getString(R.string.table_string));
            layoutGuestName.setVisibility(View.GONE);
            textViewGuestName.setText("");
            tv_show_bill.setVisibility(View.GONE);
            showDefault();
            return true;

        } else
            return false;
    }


    @Override
    public void showMenuItem(MenuItem menuItem) {

        OnMenuItemClick itemClick = new OnMenuItemClick(context, menuItem, takeOrderAdapter,
                this, discountLayout, autoDiscount);
        itemClick.showSelection();
        updateScrolling();
    }

    public void onclickEvent(MenuItem menuItem) {

        if (listViewOrderItem.getVisibility() != View.VISIBLE)
            showDefault();

/*        if (menuItem.getMenu_open_item_flag() == 0){
            FetchThirdPartyRateTask rateTask = new FetchThirdPartyRateTask(menuItem, this);
            rateTask.execute();

        }else*/
        showMenuItem(menuItem);
    }

    public void showMultiQty(MenuItem menuItem) {

        /*if (! UserInfo.lock)
            showOrderItemList();*/

        String menuCode = menuItem.getMenu_code();

        if (listViewOrderItem.getVisibility() != View.VISIBLE)
            showDefault();

        if (codeList.contains(menuCode)) {
            //takeOrderAdapter.addQty(codeList.indexOf(menuCode));
            //takeOrderAdapter.addMultiQty(menuItem, codeList.indexOf(menuCode));

            ArrayList<OrderItem> itemArrayList = takeOrderAdapter.dataSet;
            for (int i=0; i< itemArrayList.size(); i++){
                OrderItem orderItem = itemArrayList.get(i);
                if (orderItem.getO_code().equals(menuItem.getMenu_code())){
                    takeOrderAdapter.updateQty(i, menuItem.getQuantity());
                    //showMixer();
                    //getAddOn(menuItem.getMenu_code(), menuItem.getMenu_name(), menuItem.getMenu_group_code());
                    //addView(addonPopup.addView(), StaticConstants.ADDON_POPUP_TAG);
                    break;
                }

            }

            takeOrderAdapter.updateQty(codeList.indexOf(menuCode), menuItem.getQuantity());

        } else {
            onclickEvent(menuItem);
            codeList.add(menuItem.getMenu_code());
        }

    }

    AddonPopup addonPopup;

    public AddonPopup getAddOn(String menu_code, String menu_name, String menu_group_code){

        if (addonPopup == null){

            return addonPopup = new AddonPopup(context, menu_code, menu_name,
                    menu_group_code, new AddonPopup.IcallBackAddon() {

                @Override
                public void removeView(String Tag) {
                    StewardOrderFragment.this.removeView(Tag);
                }

                @Override
                public void addItem(ArrayList<OrderItem> arrayList,
                                    String Tag) {

                    //takeOrderAdapter.addDataSetList(arrayList);
                    takeOrderAdapter.updateDataSetList(arrayList);
                    StewardOrderFragment.this.removeView(Tag);
                }
            }, takeOrderAdapter, discountLayout);
        }

        return addonPopup;
    }

    @Override
    public void updateCodeList(int position) {

        if (codeList.size() > position)
            codeList.remove(position);
    }

    @Override
    public void updateScrolling() {
        if (takeOrderAdapter != null && takeOrderAdapter.getCount() > 0)
            listViewOrderItem.smoothScrollToPosition(takeOrderAdapter.getCount() - 1);
    }


    @Override   // Method called when an order no. is long pressed
    public void cancelOrder(String orderNumber) {

        showDefault();
        this.orderNumber = orderNumber;
        takeOrderAdapter.clearDataSet();
        takeOrderAdapter.notifyDataSetChanged();

        txtOrderSubmit.setText(getResources().getString(R.string.cancel_string));

        String parameter = UtilToCreateJSON.createFetchOrderJSON(context,
                orderNumber, takeOrderAdapter);
        String serverIP = POSApplication.getSingleton().getmDataModel()
                .getUserInfo().getServerIP();
        new FetchOrderTask(context, takeOrderAdapter, discountLayout,codeList, parameter, serverIP)
                .execute();
    }


    @Override   // Method called when an order no is clicked, displays items under the order
    public void modifyOrder(String orderNumber) {

        showDefault();
        takeOrderAdapter.clearDataSet();
        takeOrderAdapter.notifyDataSetChanged();

        this.orderNumber = orderNumber;
        txtOrderSubmit.setText(getResources().getString(R.string.update_string));

        String parameter = UtilToCreateJSON.createFetchOrderJSON(context,
                orderNumber, takeOrderAdapter);
        String serverIP = POSApplication.getSingleton().getmDataModel()
                .getUserInfo().getServerIP();
        new FetchOrderTask(context, takeOrderAdapter, discountLayout,codeList, parameter, serverIP)
                .execute();
    }

    @Override
    public void getOrderEditReason(String reason, boolean isModify) {

        if (isModify) {
            try {

                String parameter = UtilToCreateJSON.createOrderModification(
                        context, takeOrderAdapter, orderNumber, discountLayout, reason, steward);
                String serverIP = POSApplication.getSingleton().getmDataModel()
                        .getUserInfo().getServerIP();
                if (!(TextUtils.isEmpty(parameter) && TextUtils
                        .isEmpty(serverIP))) {

                    SendOrderModificationTask sendOrderTask = new SendOrderModificationTask(
                            context, parameter, serverIP, StewardOrderFragment.this);
                    sendOrderTask.execute();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

            if (txtOrderSubmit.getText().toString().equals(getResources().getString(R.string.cancel_string))) {

                try {

                    String parameter = UtilToCreateJSON.createOrderCancelJson(
                            context, takeOrderAdapter, orderNumber, reason);
                    String serverIP = POSApplication.getSingleton().getmDataModel()
                            .getUserInfo().getServerIP();
                    if (!(TextUtils.isEmpty(parameter) && TextUtils
                            .isEmpty(serverIP))) {

                        SendOrderCancelTask sendOrderTask = new SendOrderCancelTask(
                                context, parameter, serverIP,
                                StewardOrderFragment.this);
                        sendOrderTask.execute();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else
                cancelTable(reason);
        }
    }

    public void cancelTable(String reason) {

        try {

            String cancelType = tv_odr_cancel_type.getText().toString();
            String flag = cancelType.equals(getResources().getString(R.string.order_cancel)) ? "C" : "CO";

            String parameter = UtilToCreateJSON.createTableCancelJson(context, takeOrderAdapter,
                    reason, steward, flag);
            String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();

            if (!(TextUtils.isEmpty(parameter) && TextUtils.isEmpty(serverIP))) {

                SendTableCancelTask cancelTask = new SendTableCancelTask(
                        context, parameter, serverIP, StewardOrderFragment.this);
                cancelTask.execute();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OrderCancelResponse(String response) {

        // {"ECABS_OrderModiAndroid_newResult":true}

        if (!TextUtils.isEmpty(response)) {

            boolean isSuccess = UtilToCreateJSON.parseOrderCancelResponse(
                    context, takeOrderAdapter.getOrderDetail().getTableItem()
                            .getName(), response);
            if (isSuccess) {

                takeOrderAdapter.clearDataSet();
                takeOrderAdapter.notifyDataSetChanged();
                txtOrderSubmit.setText(getResources().getString(
                        R.string.submit_string));
            }

        } else {

            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void TableCancelResponse(String response) {

        Logger.i("TableCancelResponse", response);
        try {

            JSONObject jsonObject = new JSONObject(response);
            String result = jsonObject.getString("ECABS_Order_CancelResult");

            if (result.equals("Successs!")) {

                takeOrderAdapter.clearDataSet();
                takeOrderAdapter.notifyDataSetChanged();
                txtOrderSubmit.setText(getResources().getString(R.string.submit_string));
                selectTable.setText(getResources().getString(R.string.table_string));
                codeList.clear();
                discountLayout.clearDiscList();
                Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OrderModificationResponse(String response) {

        // {"ECABS_OrderModiAndroid_newResult":true}

        if (!TextUtils.isEmpty(response)) {

            boolean isSuccess = UtilToCreateJSON.parseOrderModificationResponse(context,
                    tableItem.getName(), tableItem.getCode(), response, orderNumber, this);
            if (isSuccess) {

                discountLayout.clearDiscList();
                takeOrderAdapter.clearDataSet();
                takeOrderAdapter.notifyDataSetChanged();
                txtOrderSubmit.setText(getResources().getString(R.string.submit_string));
            }

        } else
            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void responseHomeDeliveryServer(String response) {

        if (!TextUtils.isEmpty(response)) {

            boolean isSuccess = UtilToCreateJSON.parseHomeDelevryOrderResponse(
                    context, response, textviewForBIllno, textviewForDiscount,
                    textviewForSubtotal, textviewForTax, textviewForTotal, textViewChange);

            if (isSuccess) {

				/*PrintPreviewPopup popup = new PrintPreviewPopup(context,
                        UtilPrint.getWholeListViewItemsToBitmap(
								listViewOrderItem, takeOrderAdapter));
				popup.show();*/

                discountLayout.clearDiscList();
                takeOrderAdapter.clearDataSet();
                takeOrderAdapter.notifyDataSetChanged();
                codeList.clear();
                cover = "";
                coverPopup = false;
                stewardPopup = false;
                txtOrderSubmit.setText(getResources().getText(R.string.save_string));
                txtOrderSubmit.setVisibility(View.VISIBLE);
                textHoldOrder.setVisibility(View.GONE);

                if (flag.equals("N"))
                {
                    layoutShowHoldOrderDetail.setVisibility(View.VISIBLE);
                    tv_OrderNo.setText(textviewForBIllno.getText().toString());
                    tv_Discount.setText(textviewForDiscount.getText().toString());
                    tv_Subtotal.setText(textviewForSubtotal.getText().toString());
                    tv_Tax.setText(textviewForTax.getText().toString());
                    tv_Total.setText(textviewForTotal.getText().toString());
                    txtOrderSubmit.setVisibility(View.GONE);

                } else{

                    layoutShowBillDetail.setVisibility(View.VISIBLE);
                    textviewTop.setText("Bill No :");
                    textViewTAmt.setText("0");
                    textViewStlMode.setText("");

                    if(flag.equals("G"))
                        holdOrdersLayout.clearDetail();
                }

                if (layout_bill_operation.getVisibility() == View.VISIBLE)
                    holdOrdersLayout.ordersAdapter.removeDataSetItem(holdOrderIndex);
            }

        } else
            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void responseFromTestGenerate(String response) {

        Logger.i("Testgenerate", response);

        if (!TextUtils.isEmpty(response)) {
            boolean isSuccess = UtilToCreateJSON.parseTestGenerateResponse(
                    response, textviewForBIllno, textviewForDiscount,
                    textviewForSubtotal, textviewForTax, textviewForTotal);

            if (isSuccess) {

                textviewTop.setText("Order No:");
                txtorderOk.setVisibility(View.VISIBLE);
                txtOrderSubmit.setVisibility(View.GONE);
                textHoldOrder.setVisibility(View.GONE);
                txtOrderClear.setVisibility(View.GONE);
                layoutShowBillDetail.setVisibility(View.VISIBLE);
                listViewOrderItem.setVisibility(View.GONE);

            } else
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void responseCompanyDiscount(String response) {

        Toast.makeText(context, response, Toast.LENGTH_LONG).show();

        if (frameLayout_container.getVisibility() == View.VISIBLE) {
            frameLayout_container.removeAllViews();
            frameLayout_container.setVisibility(View.GONE);
            ll_bottomOrder.setVisibility(View.VISIBLE);
        }
        txtOrderSubmit.setText(getResources().getString(R.string.submit_string));
    }

    @Override
    public void responseGuestDiscount(String phone, String name) {

        guest_code = phone;
        selectGuest.setText("Guest" + " : " + name);
    }

    @Override
    public void responseFromServer(String response) {

        if (!response.isEmpty()) {

            String posType = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posType;
            boolean isSuccess = false;

            if (posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_RS))

                isSuccess = UtilToCreateJSON.parseTakeOrderResponse1(
                        context, roomItem.getRoom_name(), roomItem.getRoom_code(), response, this);

            else if (posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_R))
                isSuccess = UtilToCreateJSON.parseTakeOrderResponse(
                        context, tableItem.getName(),tableItem.getCode(), response, this);


            if (isSuccess) {

				/*PrintPreviewPopup popup = new PrintPreviewPopup(context,
                        UtilPrint.getWholeListViewItemsToBitmap(
								listViewOrderItem, takeOrderAdapter));
				popup.show();*/

                selectTable.setText(getResources().getString(R.string.table_string));
                discountLayout.clearDiscList();
                takeOrderAdapter.clearDataSet();
                takeOrderAdapter.notifyDataSetChanged();
                codeList.clear();
                cover = "";
                order_type = "";
                guest_code = "";
                guest_name = "";
                coverPopup = false;
                stewardPopup = false;

                if (layoutGuestName.getVisibility() == View.VISIBLE){
                    layoutGuestName.setVisibility(View.GONE);
                    textViewGuestName.setText("");
                }

                if (PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, PrefHelper.MAX_ORDER).equals("y")) {
                    //((BaseFragmentActivity)context).logOutMethod();
                    CustomLoginPopup customLoginPopup = new CustomLoginPopup(context, this);
                    customLoginPopup.show();
                }
            }

        } else
            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

    }


    public void showDefault() {

        frameLayout_container.removeAllViews();
        frameLayout_container.setVisibility(View.GONE);
        selectGuestCompanyList.setVisibility(View.GONE);
        listViewOrderItem.setVisibility(View.VISIBLE);
        ll_bottomOrder.setVisibility(View.VISIBLE);
        txtOrderClear.setText(getResources().getString(R.string.clear_string));
        txtOrderSubmit.setText(getResources().getString(R.string.submit_string));
        txtOrderSubmit.setVisibility(View.VISIBLE);
        txtOrderClear.setVisibility(View.VISIBLE);
        if (form_8) textHoldOrder.setVisibility(View.VISIBLE);
        flag = "";
    }

    public void getItem(String grpCode){

        // tabs.removeAllViews();
        TITLES.clear();
        // adapter.destroyItem(pager,0,adapter.getItem(0));
       /*  adapter.destroyAllItem();
        TITLES.clear();
        adapter.notifyDataSetChanged();*/

        //pager.removeAllViewsInLayout();

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();
        try {
            String queryCategory = "Select * from "
                    + DBConstants.KEY_CATEGORY_TABLE + " where "
                    + DBConstants.KEY_CAT_GRP_CODE + "='"
                    + grpCode + "' AND "
                    + DBConstants.KEY_CATEGORY_POS_CODE + "='"
                    + takeOrderAdapter.getOrderDetail().getPosItem().posCode + "' ";
            Cursor cursor = mdb.rawQuery(queryCategory, null);
            if (cursor != null && cursor.moveToFirst()) {
                TITLES = new ArrayList<>();
                do {

                    CategoryItem categoryItem = new CategoryItem();
                    categoryItem.setCategory_Code(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_CAT_CODE)));
                    categoryItem.setCategory_Name(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_CAT_NAME)));
                    categoryItem.setCategory_Group_Code(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_CAT_GRP_CODE)));
                    categoryItem.setCategory_Color(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_CAT_COLOR)));
                    TITLES.add(new Items(new GroupItems(), categoryItem, new MenuItem()));

                    Logger.i(
                            Logger.LOGGER_TAG,
                            Logger.LOGGER_OP
                                    + cursor.getString(cursor
                                    .getColumnIndex(DBConstants.KEY_CAT_NAME)));

                } while (cursor.moveToNext());
                cursor.close();
            }

            mdb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {

            mdb.endTransaction();
        }

        // pager.removeAllViews();
        if (!TITLES.isEmpty()) {
            adapter = new MyPagerAdapter(getChildFragmentManager());
            pager.setAdapter(adapter);
            tabs.setViewPager(pager);
            tabs.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    public void getItem(Items items) {

        getItem(items.getGroupItems().group_Code);
    }

    public void setFloorTable(Bundle bundle){

        if (bundle != null){

            tableItem = new TableItem();
            tableItem.setName(bundle.getString("name"));
            tableItem.setCode(bundle.getString("code"));

            OrderDetail detail = takeOrderAdapter.getOrderDetail();
            detail.setTableItem(tableItem);
            takeOrderAdapter.setOrderDetail(detail);
            selectTable.setText(getResources().getString(R.string.table_string) + " :" + tableItem.getName());
        }
    }
    @Override
    public void TableItem(TableItem tableItem) {

        this.tableItem = tableItem;
        cover =  tableItem.cvr;
        steward = tableItem.stwrd;
        OrderDetail detail = takeOrderAdapter.getOrderDetail();
        detail.setTableItem(tableItem);
        takeOrderAdapter.setOrderDetail(detail);
        selectTable.setText(getResources().getString(R.string.table_string) + " :" + tableItem.getName());

        String cancelType = tv_odr_cancel_type.getText().toString();

        if (tableItem.getStatus().equals("Lock"))
            getBillEditPopup("L");

        else if (flag.isEmpty())
            showDefault();

        else if (rg_OdrCancel.getCheckedRadioButtonId() == R.id.rb_tbl)
            showTableItems(cancelType);

        else if (cancelType.isEmpty() || cancelType.equals(getResources().getString(R.string.order_cancel)))
            showRunningOrders();
    }

    public void showTableItems(String cancelType) {

        showDefault();
        if (cancelType.equals(getResources().getString(R.string.order_cancel)))
            txtOrderSubmit.setText(getResources().getString(R.string.cancel_order));
        else
            txtOrderSubmit.setText(getResources().getString(R.string.change_to_comp));

        String table = takeOrderAdapter.getOrderDetail().getTableItem().getCode();
        String parameter = UtilToCreateJSON.createTableItemsJson(context, table);
        String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();

        if (!(TextUtils.isEmpty(parameter) && TextUtils.isEmpty(serverIP)))
            new FetchTableItemsTask(context, takeOrderAdapter, parameter, serverIP, null).execute();

    }

    @Override
    public void RoomItem(RoomItem roomItem) {

        this.roomItem = roomItem;
        OrderDetail detail = takeOrderAdapter.getOrderDetail();
        detail.setRoomItem(roomItem);
        takeOrderAdapter.setOrderDetail(detail);

        if (!roomItem.getStatus().equalsIgnoreCase("V")) {
            selectRoom.setText(getResources().getString(R.string.select_room_string1, roomItem.getRoom_code()));
            //String sss = getResources().getString(R.string.welcome_messages, "Test", 0);

            showDefault();
        }

        txtOrderSubmit.setText(getResources().getString(R.string.submit_string));
    }

    @Override
    public void homeDetail(HomeItem homeItem, String keyword) {

        switch (keyword) {

            case "G":
                if (!homeItem.getGuestName().isEmpty()) {

                    selectGuest.setText(getResources().getString(R.string.guest_string) + " : " + homeItem.getGuestName());
                    guest_code = homeItem.getGuestId();
                    showDefault();

                    if (posItem.posType.equals(StaticConstants.KEY_TAG_POS_TYPE_R)){
                        layoutGuestName.setVisibility(View.VISIBLE);
                        textViewGuestName.setText(homeItem.getGuestName());
                    }
                }
                break;

            case "D":

                if (!homeItem.getDelBoyName().isEmpty()) {

                    selectDelBoy.setText(getResources().getString(R.string.del_boy) + " : " + homeItem.getDelBoyName());
                    showDefault();
                }
                break;

            case "R1":
                showAllGuest();
                //showGuestCompProfile(0);
                break;

            case "R2":
                showGuestCompProfile(1);
                break;

            case "R3":
                if (!UserPermission("OC"))
                    showRunningTables("C");
                break;

            case "R4":
                if (!UserPermission("OT"))
                    ((BaseFragmentActivity) context).showOrderSplit();
                break;

            case "R5":
                if (!UserPermission("OM"))
                    showRunningTables("M");
                break;

            case "R6":
                if (!UserPermission("BS"))
                    getBillSettlementPopup();
                break;

            case "R7":
                if (!UserPermission("BC"))
                    getBillEditPopup("C");
                break;

            case "R8":
                if (!UserPermission("BT"))
                    getBillSplitPopup();
                break;

            case "R9":
                if (!UserPermission("BM"))
                    getBillEditPopup("M");
                break;

            case "R10":
                if (!UserPermission("TT"))
                    getBillTransferPopup();
                break;

            case "R11":
                if (!UserPermission("US"))
                    getBillEditPopup("U");
                break;

            case "H1":
                getCrewMembers();
                break;

            case "H2":
                getDelLogPopup();
                break;

            case "H3":
                getHoldOrdersPopup();
                break;

            case "H4":
                if (!UserPermission("BS"))
                    getBillSettlementPopup();
                break;

            case "H5":
                if (!UserPermission("BC"))
                    getBillEditPopup("C");
                break;

            case "H6":
                if (!UserPermission("BT"))
                    getBillSplitPopup();
                break;

            case "H7":
                if (!UserPermission("BM"))
                    getBillEditPopup("M");
                break;

            case "H8":
                if (!UserPermission("TT"))
                    getBillTransferPopup();
                break;

            case "H9":
                if (!UserPermission("US"))
                    getBillEditPopup("U");
                break;

        }
    }

    @Override
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

            posLayoutContainer.removeAllViewsInLayout();
            posLayoutContainer.setVisibility(View.GONE);
        }


    }

    @Override
    public void addView(View view, String Tag) {

        if (Tag.equalsIgnoreCase(StaticConstants.ADDON_POPUP_TAG)) {
            addonLayoutContainer.addView(view);
            addonLayoutContainer.setTag(Tag);
            addonLayoutContainer.setVisibility(View.VISIBLE);
        } else {
            posLayoutContainer.addView(view);
            posLayoutContainer.setTag(Tag);
            posLayoutContainer.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void removeView(String Tag) {
        if (Tag.equalsIgnoreCase(StaticConstants.ADDON_POPUP_TAG)) {

            if (addonLayoutContainer.getTag().toString().equalsIgnoreCase(Tag))
                addonLayoutContainer.removeAllViews();
            addonLayoutContainer.setTag(null);
            addonLayoutContainer.setVisibility(View.GONE);
        } else {

            if (posLayoutContainer.getTag().toString().equalsIgnoreCase(Tag)) {
                posLayoutContainer.removeAllViews();
                posLayoutContainer.setTag(null);
                posLayoutContainer.setVisibility(View.GONE);
            }
        }
    }

    public void getAllTables() {

        if (frameLayout_container.getVisibility() == View.VISIBLE)
            showDefault();

        else if (takeOrderAdapter != null && ! takeOrderAdapter.getOrderDetail().getPosItem().posCode.isEmpty()) {

            String parameter = UtilToCreateJSON.createTableJSON(context);
            String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
            frameLayout_container.setVisibility(View.VISIBLE);
            frameLayout_container.addView(tableNumberLayout.getAllTablePopupWindow(parameter, serverIP));
            listViewOrderItem.setVisibility(View.GONE);
            ll_bottomOrder.setVisibility(View.GONE);
        }
    }

    public void getAllRooms() {

        if (layoutShowBillDetail.getVisibility() == View.GONE) {

            if (frameLayout_container.getVisibility() == View.VISIBLE) {
                showDefault();

            } else {

                String parameter = UtilToCreateJSON.createRoomServiceJSON(context);
                String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
                frameLayout_container.setVisibility(View.VISIBLE);
                frameLayout_container.addView(roomNumberLayout.getRoomNumberPopupWindow(parameter, serverIP));
                listViewOrderItem.setVisibility(View.GONE);
                ll_bottomOrder.setVisibility(View.GONE);
            }
        }
    }


    public void getAllGuest() {

        if (layoutShowBillDetail.getVisibility() == View.GONE &&
                layout_bill_operation.getVisibility() == View.GONE) {

            if (frameLayout_container.getVisibility() == View.VISIBLE){

                showDefault();
                showGuestHeader();
            }
            else
                showAllGuest();
        }
    }

    public void showAllGuest() {

        frameLayout_container.removeAllViews();
        frameLayout_container.addView(guestListLayout.getGuestPopupWindow());
        frameLayout_container.setVisibility(View.VISIBLE);
        ll_bottomOrder.setVisibility(View.VISIBLE);
        listViewOrderItem.setVisibility(View.GONE);
        selectGuestCompanyList.setVisibility(View.GONE);
        txtOrderClear.setText(getResources().getString(R.string.cancel_string));
        txtOrderSubmit.setText(getResources().getString(R.string.create_guest));
        showGuestHeader();
    }

    public void showGuestHeader(){

        if (layoutGuestName.getVisibility() == View.VISIBLE)
            layoutGuestName.setVisibility(View.GONE);
        else if (! textViewGuestName.getText().toString().isEmpty())
            layoutGuestName.setVisibility(View.VISIBLE);
    }

    public void getAllDelBoy() {

        if (layoutShowBillDetail.getVisibility() == View.GONE) {

            if (frameLayout_container.getVisibility() == View.VISIBLE) {
                showDefault();

            } else {

                frameLayout_container.setVisibility(View.VISIBLE);
                frameLayout_container.addView(delBoyLayout.getDelBoyPopupWindow());
                ll_bottomOrder.setVisibility(View.VISIBLE);
                listViewOrderItem.setVisibility(View.GONE);
                selectGuestCompanyList.setVisibility(View.GONE);
                txtOrderClear.setText(getResources().getString(R.string.cancel_string));
                txtOrderSubmit.setText(getResources().getString(R.string.create_emp));
            }
        }
    }

    public void showOptionsList() {

        if (layoutShowBillDetail.getVisibility() == View.GONE) {

            if (frameLayout_container.getVisibility() == View.VISIBLE) {
                showDefault();

            } else {

                frameLayout_container.setVisibility(View.VISIBLE);
                frameLayout_container.addView(delOptionsLayout.getHomeOptionsPopupWindow());
                ll_bottomOrder.setVisibility(View.VISIBLE);
                listViewOrderItem.setVisibility(View.GONE);
                selectGuestCompanyList.setVisibility(View.GONE);
            }
        }
    }

    public void getCrewMembers() {

        if (layoutShowBillDetail.getVisibility() == View.GONE) {

            frameLayout_container.removeAllViews();
            frameLayout_container.setVisibility(View.VISIBLE);
            frameLayout_container.addView(crewMemberLayout.getCrewPopupLayout());
            ll_bottomOrder.setVisibility(View.GONE);
            listViewOrderItem.setVisibility(View.GONE);
            selectGuestCompanyList.setVisibility(View.GONE);

        }
    }

    public void getDelLogPopup() {

        String parameter = UtilToCreateJSON.createDelDetailJSON();
        String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();

        DelLogLayout delLogLayout = new DelLogLayout(context);
        mainContainer.removeAllViews();
        mainContainer.addView(delLogLayout.getDelLogPopup(parameter, serverIP));
        mainContainer.setVisibility(View.VISIBLE);

    }

    public void getHoldOrdersPopup() {

        showDefault();
        showSlidingPanel();
        txtOrderSubmit.setText(getResources().getString(R.string.save_string));
        textHoldOrder.setVisibility(View.GONE);
        takeOrderAdapter.clearDataSet();
        holdOrdersLayout = new HoldOrdersLayout(context, this);
        layout_bill_operation.removeAllViews();
        layout_bill_operation.addView(holdOrdersLayout.getHoldOrdersPopupWindow());
        layout_bill_operation.setVisibility(View.VISIBLE);
        relativeLayoutItemList.setVisibility(View.GONE);
        flag = "HO";
    }

    public void getBillSettlementPopup() {

        showDefault();
        showSlidingPanel();
        txtOrderSubmit.setText(getResources().getString(R.string.save_string));
        textHoldOrder.setVisibility(View.GONE);
        DirectSettlementLayout settlementLayout = new DirectSettlementLayout(context, this);
        layout_bill_operation.removeAllViews();
        layout_bill_operation.addView(settlementLayout.getPendingBillsPopupWindow());
        layout_bill_operation.setVisibility(View.VISIBLE);
        relativeLayoutItemList.setVisibility(View.GONE);
        flag = "DS";
    }

    public void getBillSplitPopup() {

        BillSplitLayout billBillBillSplitLayout = new BillSplitLayout(context, steward);
        mainContainer.removeAllViews();
        mainContainer.addView(billBillBillSplitLayout.getBillSplitPopup());
        mainContainer.setVisibility(View.VISIBLE);
    }

    public void getBillEditPopup(String flag) {

        showDefault();
        showSlidingPanel();
        textHoldOrder.setVisibility(View.GONE);

        if (flag.equals("M"))
            txtOrderSubmit.setText(getResources().getString(R.string.update_bill));
        else
            txtOrderSubmit.setText(getResources().getString(R.string.save_string));

        String table = tableItem == null ? "" : tableItem.code;
        takeOrderAdapter.clearDataSet();
        billEditLayout = new BillEditLayout(context, this);
        layout_bill_operation.removeAllViews();
        layout_bill_operation.addView(billEditLayout.getPendingBillsPopupWindow(flag, table));
        layout_bill_operation.setVisibility(View.VISIBLE);
        relativeLayoutItemList.setVisibility(View.GONE);
        this.flag = flag;

        if (flag.equals("M")) {

            layout_order_options.setVisibility(View.VISIBLE);
            tv_add_item.setVisibility(View.VISIBLE);

        } else if (flag.equals("C"))
            layout_order_options.setVisibility(View.GONE);

    }

    public void getBillTransferPopup() {

        BillTransferLayout billTransferLayout = new BillTransferLayout(context);
        mainContainer.removeAllViews();
        mainContainer.addView(billTransferLayout.getBillTransferPopup());
        mainContainer.setVisibility(View.VISIBLE);
    }

    public void doBillSettle() {

        try {

            String tamt = textViewTAmt.getText().toString();
            String change = textViewChange.getText().toString();

            if (!change.contains("-")) {

                String credit_code = homeItem == null ? "" : homeItem.getCreditCode();
                String credit_type = homeItem == null ? "" : homeItem.getCreditType();

                String parameter = UtilToCreateJSON.CreateSavePaidAmtJson(
                        context, textviewForBIllno.getText().toString(),
                        String.valueOf(cashPaid), credit_code, credit_type,
                        String.valueOf(creditPaid), "0", steward);
                String serverIP = POSApplication.getSingleton()
                        .getmDataModel().getUserInfo().getServerIP();

                if (!(TextUtils.isEmpty(parameter) && TextUtils.isEmpty(serverIP))) {

                    BillSettleTask billSettleTask = new BillSettleTask(context, parameter, serverIP, this);
                    billSettleTask.execute();
                }

            } else if (!tamt.equals("0"))

                Toast.makeText(context, "Settlement Amount can't be less than Bill Amount",
                        Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getBillEditReason(String reason, boolean isModify) {

        if (isModify) {

            try {

                String parameter = UtilToCreateJSON.createBillModifyJSON(
                        context, takeOrderAdapter, discountLayout, homeItem.BILL_NO,
                        homeItem.TABLE, reason, steward);
                String serverIP = POSApplication.getSingleton()
                        .getmDataModel().getUserInfo().getServerIP();
                if (!(TextUtils.isEmpty(parameter) && TextUtils.isEmpty(serverIP))) {

                    BillModifyTask billModifyTask = new BillModifyTask(context, parameter,
                            serverIP, StewardOrderFragment.this);
                    billModifyTask.execute();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            try {

                String parameter = UtilToCreateJSON.createBillCancelJson(
                        homeItem.BILL_NO, reason, billEditLayout.cancelFlag);
                String serverIP = POSApplication.getSingleton().getmDataModel()
                        .getUserInfo().getServerIP();
                if (!(TextUtils.isEmpty(parameter) && TextUtils.isEmpty(serverIP))) {

                    BillCancelTask billCancelTask = new BillCancelTask(
                            context, parameter, serverIP, StewardOrderFragment.this);
                    billCancelTask.execute();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void getUndoSettleReason(String reason) {

        try {

            String parameter = UtilToCreateJSON.createBillCancelJson(
                    homeItem.BILL_NO, reason, "");
            String serverIP = POSApplication.getSingleton().getmDataModel()
                    .getUserInfo().getServerIP();
            if (!(TextUtils.isEmpty(parameter) && TextUtils.isEmpty(serverIP))) {

                UndoBillSettleTask undoBillSettleTask = new UndoBillSettleTask(
                        context, parameter, serverIP, StewardOrderFragment.this);
                undoBillSettleTask.execute();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickBillNoToSettle(HomeItem homeItem) {

        showSlidingPanel();
        selectGuest.setText(getResources().getString(R.string.guest_string) +
                (homeItem.getGuestName().isEmpty() ? "" : " : " + homeItem.getGuestName()));
        selectDelBoy.setText(getResources().getString(R.string.del_boy) +
                (homeItem.getDelBoyName().isEmpty() ? "" : " : " + homeItem.getDelBoyName()));
        selectTable.setText(getResources().getString(R.string.table_string) +
                (homeItem.TABLE.isEmpty() ? "" : " : " + homeItem.TABLE));

        layoutShowBillDetail.setVisibility(View.VISIBLE);
        textviewTop.setText("Bill No :");
        textviewForBIllno.setText(homeItem.getBILL_NO());
        textviewForDiscount.setText(homeItem.getDISCOUNT());
        textviewForSubtotal.setText(homeItem.getSUBTOTAL());
        textviewForTax.setText(homeItem.getTAXES());
        textviewForTotal.setText(homeItem.getTOTAL());

        textViewTAmt.setText("0");
        textViewChange.setText("-" + homeItem.getTOTAL());
        textViewStlMode.setText("");
    }

    @Override
    public void onClickBillNoToCancel(HomeItem homeItem) {

        showSlidingPanel();
        this.homeItem = homeItem;
        tableItem = new TableItem();
        tableItem.setCode(homeItem.TABLE);

        takeOrderAdapter.clearDataSet();
        takeOrderAdapter.addDataSetList(homeItem.itemList);
        selectTable.setText(getResources().getString(R.string.table_string) + " :" + homeItem.TABLE);
    }

    @Override
    public void onClickBillNoToModify(HomeItem homeItem) {

        showSlidingPanel();
        this.homeItem = homeItem;
        tableItem = new TableItem();
        tableItem.setCode(homeItem.TABLE);

        ArrayList<OrderItem> itemList = homeItem.itemList;
        takeOrderAdapter.clearDataSet();
        takeOrderAdapter.addDataSetList(itemList);
        selectTable.setText(getResources().getString(R.string.table_string) + " :" + homeItem.TABLE);

        for (int i = 0; i < itemList.size(); i++) {

            OrderItem orderItem = itemList.get(i);
            discountLayout.createDiscList(orderItem.getO_disc(), orderItem.getO_grp_code(), orderItem.o_amount);

            if (!codeList.contains(orderItem.o_code))
                codeList.add(orderItem.o_code);
        }
    }

    @Override
    public void onClickBillNoToUnSettle(HomeItem homeItem) {

        showSlidingPanel();
        this.homeItem = homeItem;
        tableItem = new TableItem();
        tableItem.setCode(homeItem.TABLE);

        takeOrderAdapter.clearDataSet();
        takeOrderAdapter.addDataSetList(homeItem.itemList);
        selectTable.setText(getResources().getString(R.string.table_string) + " :" + homeItem.TABLE);
    }

    @Override
    public void onClickHoldOrderNo(HomeItem homeItem, int index) {

        if (layoutShowBillDetail.getVisibility() == View.GONE){

            this.homeItem = homeItem;
            holdOrderIndex = index ;
            tableItem = new TableItem();
            tableItem.setCode(homeItem.TABLE);

            holdOrdersLayout.setDetail(homeItem);
            takeOrderAdapter.clearDataSet();
            takeOrderAdapter.addDataSetList(homeItem.itemList);

            selectGuest.setText(getResources().getString(R.string.guest_string) +
                    (homeItem.getGuestName().isEmpty() ? "" : " : " + homeItem.getGuestName()));
            selectDelBoy.setText(getResources().getString(R.string.del_boy) +
                    (homeItem.getDelBoyName().isEmpty() ? "" : " : " + homeItem.getDelBoyName()));
        }
    }

    @Override
    public void getBillSettleResponse(String response) {

        Logger.i("SettlementResult", response);

        try {

            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("BillSettleResult").equals("Bill Setteled")) {

                if (flag.equals("DS"))
                    getBillSettlementPopup();

                else if (flag.equals("L"))
                    billEditLayout.refreshDetail();

                else if(flag.equals("Y"))
                    showDefault();

                cashPaid = 0;
                creditPaid = 0;
                layoutShowBillDetail.setVisibility(View.GONE);
                selectTable.setText(getResources().getString(R.string.table_string));
                selectGuest.setText(getResources().getString(R.string.guest_string));
                selectDelBoy.setText(getResources().getString(R.string.del_boy));
                Toast.makeText(context, "Bill settled", Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void BillCancelResponse(String response) {

        try {

            JSONObject jsonObject = new JSONObject(response);
            String result = jsonObject.getString("BillCancelResult");
            if (result.equals("Success")) {

                String cancelType =  billEditLayout.cancelFlag.equals("C") ?
                        "Bill cancelled" :
                        billEditLayout.cancelFlag.equals("CO") ?
                                "Bill changed to complimentary" : "Full discount applied to Bill" ;

                takeOrderAdapter.clearDataSet();
                getBillEditPopup("C");
                Toast.makeText(context, cancelType, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getBillModiResponse(String response) {

        Logger.i("BillModifyResponse", response);
        try {

            JSONObject jsonObject = new JSONObject(response);
            String result = jsonObject.getString("billmodiResult");

            if (result.equals("Success")) {

                takeOrderAdapter.clearDataSet();
                takeOrderAdapter.notifyDataSetChanged();
                txtOrderSubmit.setText(getResources().getString(R.string.submit_string));
                selectTable.setText(getResources().getString(R.string.table_string));
                codeList.clear();
                discountLayout.clearDiscList();
                getBillEditPopup("M");
                Toast.makeText(context, "Bill modified", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void undoBillSettleResponse(String response) {

        try {

            JSONObject jsonObject = new JSONObject(response);
            String result = jsonObject.getString("UndoSettResult");
            if (result.equals("Success")) {

                takeOrderAdapter.clearDataSet();
                selectTable.setText(getResources().getString(R.string.table_string));
                getBillEditPopup("U");
                Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public boolean permission(String flag, String message) {

        if (!UserPermission(flag)) {

            if (takeOrderAdapter.isEmpty()) {

                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                return false;

            } else
                return true;
        }

        return false;
    }


    public boolean UserPermission(String menu_id) {

        return ((BaseFragmentActivity)context).UserPermission(menu_id);
    }



    public String show_Group_items_horizontally() {


        String frst_grp_code = "";

        ViewGroup.MarginLayoutParams l = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        l.setMargins(4, 2, 1, 2);

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        try {

            String queryGroup = "Select * from " + DBConstants.KEY_GROUP_TABLE;
            Cursor cursor = mdb.rawQuery(queryGroup, null);

            if (cursor.moveToFirst()) {

                frst_grp_code = cursor.getString(1);

                do {

                    Button show_group_without_imButton = new Button(context);
                    show_group_without_imButton.setText(cursor.getString(3));
                    show_group_without_imButton.setTextSize(
                            TypedValue.COMPLEX_UNIT_PX, getResources()
                                    .getDimension(R.dimen.group_font_size));
                    show_group_without_imButton.setTextColor(Color.BLACK);
                    show_group_without_imButton.setPadding(5,0,5,0);

                    show_group_without_imButton
                            .setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT));
                    show_group_without_imButton.setLines(1);
                    show_group_without_imButton.setMinWidth((int) getResources()
                            .getDimension(R.dimen.group_min_width));
                    show_group_without_imButton
                            .setBackgroundResource(R.drawable.btn_default_holo_dark_blue);
                    show_group_without_imButton.
                            setTag(cursor.getString(1));
                    show_group_without_imButton.
                            setBackgroundColor(Color.parseColor(cursor.getString(2)));

                    show_group_without_imButton
                            .setOnClickListener(Group_listener_wihout_im());

                    layoutGroup.addView(show_group_without_imButton, l);

                } while (cursor.moveToNext());

            }

            cursor.close();
            mdb.setTransactionSuccessful();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mdb.endTransaction();
        }
        return frst_grp_code;
    }

    private View.OnClickListener Group_listener_wihout_im() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                getItem(v.getTag().toString());

            }
        };

    }


    public boolean mandatoryPopup() {

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();
        mdb.execSQL(DBStatements.CREATE_KEY_MANDATORY);
        String queryPermission = "Select * from " + DBConstants.KEY_MANDATORY_TABLE;
        Cursor c = mdb.rawQuery(queryPermission, null);

        if (c.moveToFirst()) {

            if (c.getString(1).equals("y")
                    && c.getString(2).equals("y")
                    && c.getString(3).equals("y")
                    && (c.getString(4).equals("y"))) {

/*                if (cover.isEmpty() || steward.isEmpty())
                    popup_mandatory("yy");*/
                if (cover.isEmpty() && steward.isEmpty())
                    popup_mandatory("yy");
                else if (cover.isEmpty())
                    popup_mandatory("yn");
                else if (steward.isEmpty())
                    popup_mandatory("ny");
                else{
                    coverPopup = true;
                    stewardPopup = true;
                }
            } else if (c.getString(2).equals("n")
                    && c.getString(3).equals("y")
                    && c.getString(4).equals("y")) {

                if (steward.equals(""))
                    popup_mandatory("ny");


            } else if (c.getString(1).equals("y")
                    && c.getString(2).equals("y")
                    && c.getString(4).equals("n")) {

                if (cover.equals(""))
                    popup_mandatory("yn");
                else {
                    coverPopup = true;
                    stewardPopup = true;
                }

            }else  if(c.getString(1).equals("n") && c.getString(2).equals("y")){
                Toast.makeText(context,"Activate Cover on Order", Toast.LENGTH_SHORT).show();

            } else  if(c.getString(3).equals("n") && c.getString(4).equals("y")){
                Toast.makeText(context,"Activate Steward on Order", Toast.LENGTH_SHORT).show();
            }
            else {
                coverPopup = true;
                stewardPopup = true;
            }

        } else {
            coverPopup = true;
            stewardPopup = true;
        }
        mdb.endTransaction();
        c.close();

        return coverPopup && stewardPopup ;
    }

    public void popup_mandatory(final String value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Mandatory : ");
        if (value.equals("yy")) {

            builder.setMessage("Cover and Steward both are mandatory !! ");

        } else if (value.equals("yn")) {

            builder.setMessage("Cover is mandatory !! ");

        } else if (value.equals("ny")) {

            builder.setMessage("Steward is mandatory !! ");
        }

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                SelectStewardPopup selectStewardPopup = new SelectStewardPopup(context, StewardOrderFragment.this);
                selectStewardPopup.redirect = value;
                selectStewardPopup.takeOrderFragment = StewardOrderFragment.this;

                CoverPopup coverPopup = new CoverPopup(context, StewardOrderFragment.this);
                coverPopup.redirect = value;
                coverPopup.takeOrderFragment = StewardOrderFragment.this;

                if (value.equals("yy") || value.equals("yn"))
                    coverPopup.show();
                else if (value.equals("ny"))
                    selectStewardPopup.show();

            }
        });

        builder.create().show();
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

		/*
         * private String[] TITLES = {"Categories", "Food", "Beverage",
		 * "Indian", "Chinese", "Cuisin", "Liquor", "Top New Free", "Trending"};
		 */

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {

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

            return ItemsFragment.newInstance(position, TITLES.get(position),
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
                FragmentManager manager = ((Fragment) object)
                        .getFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove((Fragment) object);
                trans.commit();
            }
        }

    }

}