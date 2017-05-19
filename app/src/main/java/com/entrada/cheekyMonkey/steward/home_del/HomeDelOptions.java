package com.entrada.cheekyMonkey.steward.home_del;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.staticData.StaticConstants;

import java.util.ArrayList;


/**
 * Created by CSATSPL on 22/12/2015.
 */
public class HomeDelOptions implements AdapterView.OnItemClickListener {

    LayoutInflater layoutInflater;
    ListView listView_Options;
    Context context;
    HomeOptionsAdapter optionsAdapter;
    ArrayList<HomeItem> optionsList;
    ICallHome iCallHome;

    String posType = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posType;
    String flag = posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_R) ? "R" : "H";

    public HomeDelOptions(Context context, ICallHome iCallHome) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.iCallHome = iCallHome;
    }

    public View getHomeOptionsPopupWindow() {

        View view = layoutInflater.inflate(R.layout.layout_home_options, null);

        optionsList = new ArrayList<>();
        init();
        optionsAdapter = new HomeOptionsAdapter(context, R.layout.home_options_adapter, optionsList);
        listView_Options = (ListView) view.findViewById(R.id.lv_homeOptions);
        listView_Options.setAdapter(optionsAdapter);
        listView_Options.setOnItemClickListener(this);

        return view;
    }

    public void init() {

        if (posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_R)) {

            String s[] = {"Select Guest", "Select Company", "Order Cancel",
                    "Order Split", "Order Modify", "Direct Settlement", "Bill Cancel",
                    "Bill Split", "Bill Modify", "Bill Transfer", "Undo Bill Settlement"};

            for (String data : s) {

                HomeItem homeItem = new HomeItem();
                homeItem.setName(data);
                optionsList.add(homeItem);
            }
        } else if (posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_H) ||
                posType.equalsIgnoreCase(StaticConstants.KEY_TAG_POS_TYPE_T)) {

            String s[] = {"Crew Availability", "Delivery Log", "Orders", "Direct Settlement",
                    "Bill Cancel", "Bill Split", "Bill Modify", "Bill Transfer", "Undo Bill Settlement"};

            for (String data : s) {

                HomeItem homeItem = new HomeItem();
                homeItem.setName(data);
                optionsList.add(homeItem);
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        iCallHome.homeDetail(optionsAdapter.getItem(position), flag + (position + 1));
    }
}