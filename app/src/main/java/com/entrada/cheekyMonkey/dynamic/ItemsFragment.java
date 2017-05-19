package com.entrada.cheekyMonkey.dynamic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.dynamic.syncData.FetchAndStoreMenuItemsTask;
import com.entrada.cheekyMonkey.dynamic.syncData.ICallResponse;
import com.entrada.cheekyMonkey.entity.Items;
import com.entrada.cheekyMonkey.entity.MenuItem;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.steward.StewardOrderFragment;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

/**
 * Created by Rahul on 1/30/2015.
 */
public class ItemsFragment extends Fragment implements
        GridView.OnItemClickListener, ICalItemsResponse, ICallResponse,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_POSITION = "position";
    private static final String ARG_POS_CODE = "PosCode";
    protected DynamicItemsAdapter dynamicItemsAdapter;
    private int position;
    private String posCode = "";
    private Items items;
    private Context context;
    private View view;
    private ListView itemsListView;
    private HashMap<Integer,Integer> hashMap = new HashMap<>();

    private SwipeRefreshLayout swipeRefreshLayout;
    ScheduledExecutorService scheduler ;

    public static ItemsFragment newInstance(int position, Items items, String posCode) {
        ItemsFragment f = new ItemsFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putParcelable(StaticConstants.KEY_PARCELABLE, items);
        b.putString(ARG_POS_CODE, posCode);
        //b.putString(ClassesAccess,ClassesAccess);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
        items = getArguments().getParcelable(StaticConstants.KEY_PARCELABLE);
        posCode = getArguments().getString(ARG_POS_CODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.items_fragment, null, false);

        itemsListView = (ListView) view.findViewById(R.id.list_cat_items);  //  Displays Items
        dynamicItemsAdapter = new DynamicItemsAdapter(context);
        itemsListView.setAdapter(dynamicItemsAdapter);
        itemsListView.setOnItemClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        if (getParentFragment() instanceof TakeOrderFragment){
            ArrayList<Items> itemsList = ((TakeOrderFragment)getParentFragment()).TITLES;
            getItemsResponse(itemsList);
        }else if (getParentFragment() instanceof StewardOrderFragment) {
            ArrayList<Items> itemsList = ((StewardOrderFragment)getParentFragment()).TITLES;
            getItemsResponse(itemsList);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (position == 0)
                    itemsListView.smoothScrollToPositionFromTop(position, 2);

                else {
                    int index = hashMap.get(position - 1);
                    //itemsListView.smoothScrollToPositionFromTop(position + index, 2);
                    itemsListView.setSelection(position+index);
                }

            }
        }, 500);

        return view;
    }

    @Override
    public void onRefresh() {

        //doRefreshItems();
        refreshItemPrice();
    }

    public void refreshItemPrice(){

        String parameter = UtilToCreateJSON.createToken();
        String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
        FetchAndStoreMenuItemsTask menuItemsTask = new FetchAndStoreMenuItemsTask(
                context, parameter, serverIP, null, this);
        menuItemsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public void getResponseFromServer(String response) {

        BaseFragmentActivity activity = (BaseFragmentActivity) context;
        activity.getResponseFromServer(response);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (! scheduler.isShutdown())
            scheduler.shutdownNow();
    }

    @Override
    public void onResume() {
        super.onResume();
        scheduleThreadPoolExecutor();
    }


    /**** Scheduling Executor(that starts a new thread) to update items rate at fixed interval ****/
    public void scheduleThreadPoolExecutor(){

        scheduler = new ScheduledThreadPoolExecutor(1);
        scheduler.scheduleWithFixedDelay(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                // Hit WebService

                doRefreshItems();

            }
        }, 0, 5, TimeUnit.SECONDS);
    }


    public void doRefreshItems(){

        String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
        FetchStoredMenuItemsTask storedMenuItemsTask = new FetchStoredMenuItemsTask(context,"",serverIP, null, this );
        storedMenuItemsTask.execute();
    }

    @Override
    public void getItemsResponse(ArrayList<Items> itemsList) {

        if (itemsList == null || itemsList.isEmpty())
            scheduler.shutdown();

        else {

            dynamicItemsAdapter.clear();

            int index = 0;
            for (int i = 0; i < itemsList.size(); i++) {

                MenuItem menuItem = new MenuItem();
                menuItem.Cat_Desc = itemsList.get(i).getCategoryItem().Category_Name;
                int total_items =  itemsList.get(i).getMenuItemList().size();
                dynamicItemsAdapter.addSectionHeaderItem(menuItem, total_items);
                dynamicItemsAdapter.addDataSet(itemsList.get(i).getMenuItemList());

                index += itemsList.get(i).getMenuItemList().size();
                hashMap.put(i,index);
            }

            dynamicItemsAdapter.notifyDataSetChanged();

            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);
        }
    }





    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    boolean dialogIsHidden = true;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (dialogIsHidden) {
            dialogIsHidden = false;
            showQtyPopup(position);
        }
    }


    public void showQtyPopup(final int itemPosition){

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
                    dialogIsHidden = true;
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

                setQuantity(itemPosition, position);
                dialogIsHidden = true;
                dialog.dismiss();
            }
        });
    }

    public void setQuantity(int position, int quantity) {

        if (getParentFragment() instanceof TakeOrderFragment){

            TakeOrderFragment takeOrderFragment = (TakeOrderFragment) getParentFragment();
            MenuItem menuItem = (MenuItem)dynamicItemsAdapter.getItem(position);
            menuItem.setQuantity(quantity+1);
            menuItem.setMenuAmount(menuItem.getMenu_price() * (quantity + 1));
            takeOrderFragment.showMultiQty(menuItem);    // on MenuItem click

           /* if (UserInfo.multicheck.equals("0"))
                takeOrderFragment.onclickEvent(menuItem);    // on MenuItem click
            else
                takeOrderFragment.showMultiQty(menuItem);   // on MenuItem click*/

        } else if (getParentFragment() instanceof StewardOrderFragment) {

            StewardOrderFragment stewardOrderFragment = (StewardOrderFragment) getParentFragment();
            MenuItem menuItem = (MenuItem)dynamicItemsAdapter.getItem(position);
            menuItem.setQuantity(quantity+1);
            menuItem.setMenuAmount(menuItem.getMenu_price() * (quantity + 1));
            stewardOrderFragment.showMultiQty(menuItem);    // on MenuItem click

           /* if (UserInfo.multicheck.equals("0"))
                stewardOrderFragment.onclickEvent(menuItem);    // on MenuItem click
            else
                stewardOrderFragment.showMultiQty(menuItem);   // on MenuItem click*/

        }

    }

    public void scrollListToLastPosition(){

        // save index and top position
        int index = itemsListView.getFirstVisiblePosition();
        View v = itemsListView.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - itemsListView.getPaddingTop());

        // restore index and position
        itemsListView.setSelectionFromTop(index, top);

    }
}