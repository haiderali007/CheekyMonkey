package com.entrada.cheekyMonkey.steward.notificationUI;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.IAsyncTaskRunner;
import com.entrada.cheekyMonkey.appInterface.OnBackPressInterface;
import com.entrada.cheekyMonkey.dynamic.BaseFragmentActivity;
import com.entrada.cheekyMonkey.entity.GuestOrderItem;
import com.entrada.cheekyMonkey.entity.GusetOrderDetail;
import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.staticData.ResultMessage;
import com.entrada.cheekyMonkey.task.GuestCommonTask;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.ui.ProgressBarCircularIndeterminate;
import com.entrada.cheekyMonkey.util.AsyncTaskTools;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

import java.util.Locale;


@SuppressWarnings("rawtypes")
public class NotificationFragment extends Fragment implements
        OnBackPressInterface, IAsyncTaskRunner, OnClickListener {


    GuestOrderAdapter<GuestOrderItem> adapter;
    ListView listViewGuestOrdr;
    ProgressBarCircularIndeterminate pb;
    GstOrDetailAdp<GusetOrderDetail> detailAdp;
    ListView lstVDetail;
    ProgressBarCircularIndeterminate pbDetail;
    LinearLayout llNotifyBottom;
    CustomTextview txtTableNo, txtOrderNumber, txtAcceptOrder, txtRejectOrder,
            tv_guest_name, tv_order_amt, tv_order_sts;
    String selectedTable = "";
    private Context context;
    LinearLayout layout_flipMode;
    FrameLayout upper_layout;
    RelativeLayout lower_layout;


    // Please note : This Notification fragment is used to show order status to Steward and Admin
    // only while Guest order status is maintained By BaseFragmentActivity's RightListView and
    // Kitchen order status by Kitchen bar Fragment.
    // Possible order status for a Guest: Flags displayed here denotes corresponding order status.
    // Also Note, A Guest can have any order status(except B1,B2) and he can cancel his order
    // only if his order is under process.


    public static String EMPLOYEE_TYPE1 = "Admin";              // Permission level for the Employee
    public static String EMPLOYEE_TYPE2 = "Steward";
    static String ARGS_EMPLOYEE_TYPE = "EMPLOYEE_TYPE";
    static String ARGS_GUEST_ORDER_STATUS = "GUEST_ORDER_STATUS";
    String employeeType = "", orderStatus = "";


    public static String TYPE_UNDER_PROCESS = "under_process";  // Display Control, decides type of orders to show
    public static String TYPE_ACCEPTED = "accepted";
    public static String TYPE_REJECTED = "rejected";


    public static String ORDER_UNDER_PROCESS = "A";             // Action Control, decides what operation to perform
    public static String ORDER_ACCEPTED = "B";
    public static String ORDER_REJECTED_BY_GUEST = "C";
    public static String ORDER_REJECTED_BY_STEWARD = "D";
    public static String ORDER_REJECTED_BY_ADMIN = "E";
    public static String ORDER_DELIVERED = "F";
    public static String ORDER_PREPARATION_STARTED = "P";
    public static String ORDER_READY_TO_SERVE = "R";



    public static NotificationFragment newInstance(String empType, String status) {

        Bundle args = new Bundle();
        args.putString(ARGS_EMPLOYEE_TYPE, empType);
        args.putString(ARGS_GUEST_ORDER_STATUS, status);
        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_layout, container, false);

        employeeType = getArguments().getString(ARGS_EMPLOYEE_TYPE);
        orderStatus = getArguments().getString(ARGS_GUEST_ORDER_STATUS);

        MappingWidgets(view);
        return view;
    }

    private void MappingWidgets(View view) {

        layout_flipMode = (LinearLayout) view.findViewById(R.id.ll_flip_mode);
        upper_layout = (FrameLayout) view.findViewById(R.id.nfUpperBaseFrame);
        lower_layout= (RelativeLayout) view.findViewById(R.id.nfDetailFrame);
        flipIfPortrait();

        txtAcceptOrder = (CustomTextview) view.findViewById(R.id.txtAcceptOrder);
        txtAcceptOrder.setOnClickListener(this);
        txtRejectOrder = (CustomTextview) view.findViewById(R.id.txtRejectOrder);
        txtRejectOrder.setOnClickListener(this);

        if (orderStatus.equals(TYPE_UNDER_PROCESS)){
            txtAcceptOrder.setVisibility(View.VISIBLE);
            txtRejectOrder.setVisibility(View.VISIBLE);

        }else  if (orderStatus.equals(TYPE_ACCEPTED)){
            txtAcceptOrder.setVisibility(View.VISIBLE);
            txtAcceptOrder.setText(R.string.delivery_string);
            txtRejectOrder.setVisibility(employeeType.equals(EMPLOYEE_TYPE1)? View.VISIBLE : View.GONE);

        }else  if (orderStatus.equals(TYPE_REJECTED)){
            txtAcceptOrder.setVisibility(View.GONE);
            txtRejectOrder.setVisibility(View.GONE);
        }
        
        txtOrderNumber = (CustomTextview) view
                .findViewById(R.id.txtOrderNumber);
        txtTableNo = (CustomTextview) view
                .findViewById(R.id.txtTableNumber);
        tv_guest_name = (CustomTextview) view
                .findViewById(R.id.tv_guest_name);
        tv_order_amt = (CustomTextview) view
                .findViewById(R.id.tv_order_amt);
        tv_order_sts  = (CustomTextview) view
                .findViewById(R.id.tv_order_sts);

        listViewGuestOrdr = (ListView) view
                .findViewById(R.id.listViewGuestOrdr);
        pb = (ProgressBarCircularIndeterminate) view
                .findViewById(R.id.pbCustomNotification);

        lstVDetail = (ListView) view.findViewById(R.id.lstVDetail);
        pbDetail = (ProgressBarCircularIndeterminate) view
                .findViewById(R.id.pbDetail);

        llNotifyBottom = (LinearLayout) view.findViewById(R.id.llNotifyBottom);
        llNotifyBottom.setVisibility(View.VISIBLE);

        adapter = new GuestOrderAdapter<>(context);
        listViewGuestOrdr.setAdapter(adapter);
        listViewGuestOrdr.setOnItemClickListener(onItemClickListener());

        detailAdp = new GstOrDetailAdp<>(context);
        lstVDetail.setAdapter(detailAdp);
        FetchGuestOrder();
    }

    
    
    public void flipIfPortrait(){

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            layout_flipMode.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);

            p.setMargins(2, 1, 1, 1);
            upper_layout.setLayoutParams(p);

            LinearLayout.LayoutParams p_order = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);

            p_order.setMargins(2, 1, 1, 1);
            lower_layout.setLayoutParams(p_order);


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
                    ViewGroup.LayoutParams.MATCH_PARENT, 1);
            p.setMargins(1, 1, 1, 1);
            upper_layout.setLayoutParams(p);

            LinearLayout.LayoutParams p_order = new LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1);
            p_order.setMargins(0, 1, 1, 1);
            lower_layout.setLayoutParams(p_order);


        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            layout_flipMode.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            p.setMargins(2, 1, 1, 1);
            upper_layout.setLayoutParams(p);

            LinearLayout.LayoutParams p_order = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            p_order.setMargins(2, 1, 1, 1);
            lower_layout.setLayoutParams(p_order);

        }
    }


    
    private OnItemClickListener onItemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                adapter.setIsSelectedPosition(position);
                GuestOrderItem item = (GuestOrderItem) adapter.getItem(position);
                GuestOrderDetail(item);
            }
        };
    }

    @Override
    public boolean onBackPress() {
        return false;
    }

    /**
     * Fetch Guest Order
     */
    @SuppressWarnings("unchecked")
    public void FetchGuestOrder() {

        try {

            adapter.clearDataSetALL();
            adapter.notifyDataSetChanged();

            detailAdp.clearDataSetALL();
            detailAdp.notifyDataSetChanged();

            String paramter = UtilToCreateJSON.createParamToFetchOrder(context, orderStatus);
            GuestCommonTask<String, ResultMessage> commomTask = new GuestCommonTask<>(
                    context, this, BaseNetwork.KEY_ECABS_DispalyGuestOrder, paramter, pb);
            AsyncTaskTools.execute(commomTask);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    public void GuestOrderDetail(GuestOrderItem item) {

        detailAdp.clearDataSetALL();
        detailAdp.notifyDataSetChanged();
        detailAdp.setItem(item);

        txtTableNo.setText(getString(R.string.selected_table_string, selectedTable= item.TABLE));
        txtTableNo.setTag(item.TABLE);

        txtOrderNumber.setText(getString(R.string.selected_odr_string, item.ORDER_NUM));
        txtOrderNumber.setTag(item.ORDER_NUM);

        tv_guest_name.setText(item.Guest_Name);
        tv_guest_name.setTag(item.Guest_Name);


        String paramter = UtilToCreateJSON.createParamToFetchOrderDetail(
                context, item);
        GuestCommonTask<String, ResultMessage> commomTask = new GuestCommonTask<>(
                context, this, BaseNetwork.KEY_ECABS_DISPLAY_GUEST_ORDER_DETAIL, paramter, pbDetail);
        AsyncTaskTools.execute(commomTask);
    }

    @Override
    public void taskStarting() {

    }

    @Override
    public void taskCompleted(Object result) {

        if (result instanceof ResultMessage) {

            ResultMessage message = (ResultMessage) result;
            if (message.TYPE
                    .equalsIgnoreCase(BaseNetwork.KEY_ECABS_DISPLAY_GUEST_ORDER_DETAIL)) {

                llNotifyBottom.setVisibility(View.VISIBLE);
            } else if (message.TYPE
                    .equalsIgnoreCase(BaseNetwork.KEY_ECABS_PushOrderGuest)) {

                FetchGuestOrder();
            } else if (message.TYPE
                    .equalsIgnoreCase(BaseNetwork.KEY_ECABS_CancelGuestOrder)) {
                FetchGuestOrder();
            }

            if (context instanceof BaseFragmentActivity)
            ((BaseFragmentActivity)context).refreshPendingOrders(adapter.getCount());
        }

    }

    @Override
    public void taskProgress(Object progress) {

        try {

            if (progress instanceof GuestOrderItem) {
                adapter.addDataSetItem((GuestOrderItem) progress);

            } else if (progress instanceof GusetOrderDetail) {
                GusetOrderDetail  orderDetail = (GusetOrderDetail) progress;
                detailAdp.addDataSetItem(orderDetail);

                float amt = Float.parseFloat(orderDetail.TotalAmt);
                tv_order_amt.setText(String.format(Locale.US, "%.2f",amt));
                tv_order_amt.setTag(String.valueOf(amt));

                tv_order_sts.setText(getStatus(orderDetail.Order_sts));
                tv_order_sts.setTag(getStatus(orderDetail.Order_sts));

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void taskErrorMessage(Object result) {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void onClick(View id) {

        switch (id.getId()) {
            case R.id.txtAcceptOrder:
                onAcceptMethod();
                break;
            case R.id.txtRejectOrder:
                onRejectMethod();
                break;

            default:
                break;
        }

    }

    @SuppressWarnings("unchecked")
    public void onAcceptMethod() {

        String parameter = UtilToCreateJSON.createTOAcceptOrder(context, detailAdp,
                orderStatus.equals(TYPE_UNDER_PROCESS) ? ORDER_ACCEPTED : ORDER_DELIVERED);
        GuestCommonTask<String, ResultMessage> commonTask = new GuestCommonTask<String, ResultMessage>(
                context, NotificationFragment.this,
                orderStatus.equals(TYPE_UNDER_PROCESS) ? BaseNetwork.KEY_ECABS_PushOrderGuest :
                        BaseNetwork.KEY_ECABS_PushOrderGuestStatus, parameter, pbDetail);
        AsyncTaskTools.execute(commonTask);

        clearData();
    }

    @SuppressWarnings("unchecked")
    public void onRejectMethod() {

        if (txtOrderNumber.getTag() != null){

            String parameter = UtilToCreateJSON.onReject(context, txtOrderNumber
                    .getTag().toString(), selectedTable,
                    employeeType.equals(EMPLOYEE_TYPE1) ? ORDER_REJECTED_BY_ADMIN : ORDER_REJECTED_BY_STEWARD);
            GuestCommonTask<String, ResultMessage> commonTask = new GuestCommonTask<>(
                    context, NotificationFragment.this,
                    BaseNetwork.KEY_ECABS_CancelGuestOrder, parameter, pbDetail);
            AsyncTaskTools.execute(commonTask);

            clearData();
        }
    }


    public String getStatus(String flag){

        String status = "";

        switch (flag){

            case "A" :
                status = "waiting for approval";
                break;
            case "B" :
                status = "accepted";
                break;
            case "B1" :
                status = "under preparation";
                break;
            case "B2" :
                status = "ready to serve";
                break;
            case "C" :
                status = "order cancelled (By Guest)";
                break;
            case "D" :
                status = "order cancelled (By Waiter)";
                break;
            case "E" :
                status = "order cancelled (By Admin)";
                break;
            case "F" :
                status = "delivered";
                break;
        }

        return status;
    }

    public void clearData(){

        txtTableNo.setText(R.string.table_string);
        txtOrderNumber.setText(R.string.select_order_string);
        tv_guest_name.setText("");
        tv_order_amt.setText("");
        tv_order_sts.setText("");
    }
}