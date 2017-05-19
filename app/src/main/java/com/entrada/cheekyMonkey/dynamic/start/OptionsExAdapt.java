package com.entrada.cheekyMonkey.dynamic.start;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.entity.POSItem;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.pointOfSale.PosTitleEntity;
import com.entrada.cheekyMonkey.staticData.PrefHelper;

public class OptionsExAdapt extends BaseExpandableListAdapter {
    Context context;
    String backgrnd = "", txtfont = "", textSize = "";
    private ArrayList<PosTitleEntity> itemList;
    private LayoutInflater inflater;
    boolean onOff[] = {false, false};


    public OptionsExAdapt(Context context, ArrayList<PosTitleEntity> itemList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.itemList = itemList;
        onOff[0] = PrefHelper.getStoredBoolean(context, PrefHelper.PREF_FILE_NAME, PrefHelper.DRAWER_LOCK_MODE);
    }


    @Override
    public PosTitleEntity getGroup(int groupPosition) {
        return itemList.get(groupPosition);
    }


    @Override
    public long getGroupId(final int groupPosition) {
        return groupPosition;
    }


    @Override
    public int getGroupCount() {
        return itemList.size();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View theConvertView, ViewGroup parent) {
        View resultView = theConvertView;
        ViewParentHolder holder;

        if (resultView == null) {
            resultView = inflater.inflate(R.layout.options_layout, null);
            holder = new ViewParentHolder();
            holder.textLabel = (TextView) resultView
                    .findViewById(R.id.textViewInfoListTitile);
            holder.imgExpandCollapse = (ImageView) resultView
                    .findViewById(R.id.imgExpandCollapse);
            resultView.setTag(holder);
        } else
            holder = (ViewParentHolder) resultView.getTag();

        if (isExpanded) {
            holder.imgExpandCollapse.setSelected(true);
        } else {
            holder.imgExpandCollapse.setSelected(false);
        }

        PosTitleEntity item = getGroup(groupPosition);

        holder.textLabel.setText(item.Tittle);
        return resultView;
    }

    @Override
    public POSItem getChild(int groupPosition, int childPosition) {

        return itemList.get(groupPosition).arrayListPosItems.get(childPosition);
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return itemList.get(groupPosition).arrayListPosItems.size();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {
        View resultView = convertView;
        final ViewChildHolder holder;

        if (resultView == null) {

            resultView = inflater.inflate(R.layout.toggle_layout, null);
            holder = new ViewChildHolder();
            holder.txtsubpingconnection = (TextView) resultView.findViewById(R.id.tv_item_name);
            holder.togg = (ToggleButton) resultView.findViewById(R.id.toggleButton);

            resultView.setTag(holder);
        } else {
            holder = (ViewChildHolder) resultView.getTag();
        }

        POSItem item = getChild(groupPosition, childPosition);
        holder.txtsubpingconnection.setText("" + item.posName);

        UserInfo info = POSApplication.getSingleton().getmDataModel().getUserInfo();

        resultView.setBackgroundColor(info.getNaviContentBackgrounnd());
        holder.txtsubpingconnection.setTextColor(info.getNaviFontColor());
        holder.txtsubpingconnection.setTextSize(info.getTextSize() - 4);



        holder.togg.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (onOff[childPosition]) {
                    holder.togg.setChecked(false);
                    onOff[childPosition] = false;

                } else {

                    onOff[childPosition] = true;
                    holder.togg.setChecked(true);
                }

                PrefHelper.storeBoolean(context, PrefHelper.PREF_FILE_NAME, PrefHelper.DRAWER_LOCK_MODE, onOff[childPosition]);
                UserInfo.lock = onOff[childPosition];

            }
        });

        //arryText.setText(days[position]);
        holder.togg.setChecked(onOff[childPosition]);

        return resultView;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewParentHolder {
        TextView textLabel;
        ImageView imgExpandCollapse;


    }

    private class ViewChildHolder {
        TextView txtsubpingconnection;
        ToggleButton togg;

    }

}
