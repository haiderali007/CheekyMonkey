package com.entrada.cheekyMonkey.takeorder;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.entity.Items;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.Logger;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Tanuj.Sareen on 1/30/2015.
 */
public class ItemsAdapter extends ArrayAdapter<Items> {

    UserInfo userInfo;
    private Context context;
    private int resourceID;
    private ArrayList<Items> itemsArrayList;

    public ItemsAdapter(Context context, int resourceID, ArrayList<Items> itemsArrayList) {
        super(context, resourceID, itemsArrayList);
        this.context = context;
        this.resourceID = resourceID;
        this.itemsArrayList = itemsArrayList;
        this.userInfo = POSApplication.getSingleton().getmDataModel().getUserInfo();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemsViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ItemsViewHolder();
            convertView = LayoutInflater.from(context).inflate(resourceID, null);
            viewHolder.imgItemCategory = (ImageView) convertView.findViewById(R.id.img_item_categ);
            viewHolder.itemNameText = (CustomTextview) convertView.findViewById(R.id.itemNameText);
            viewHolder.itemPriceText = (CustomTextview) convertView.findViewById(R.id.tv_item_price);
            convertView.setTag(viewHolder);

        } else
            viewHolder = (ItemsViewHolder) convertView.getTag();

        Items items = itemsArrayList.get(position);

        if (!TextUtils.isEmpty(items.getMenuItem().getMenu_name()) && viewHolder.itemNameText != null) {

            Logger.i("Search Item", "::" + items.getMenuItem().getMenu_name());

            String mDrawableName = "img" + items.getMenuItem().getMenu_categ_code();
            int resID = context.getResources().getIdentifier(mDrawableName, "drawable", context.getPackageName());
            viewHolder.imgItemCategory.setImageResource(resID);

            viewHolder.itemNameText.setText(items.getMenuItem().getMenu_name());

            String price = context.getResources().getString(R.string.rupees, "₹",
                    String.valueOf(items.getMenuItem().getMenu_price()));
            viewHolder.itemPriceText.setText(price);

            if (!TextUtils.isEmpty(items.getMenuItem().getMenu_color())) {
                viewHolder.itemNameText.setBackgroundColor(Color.parseColor(items.getMenuItem().getMenu_color()));
                //viewHolder.itemNameText.setTextColor(userInfo.getFontColor());

            }
        }

        if (!TextUtils.isEmpty(items.getGroupItems().getGroup_Code())) {
            viewHolder.itemNameText.setText(items.getGroupItems().getGroup_name());
            if (!TextUtils.isEmpty(items.getGroupItems().getGroup_color())) {
                viewHolder.itemNameText.setBackgroundColor(Color.parseColor(items.getGroupItems().getGroup_color()));
                //viewHolder.itemNameText.setTextColor((userInfo.getFontColor()));
            }
        }

        //viewHolder.itemNameText.setTag(viewHolder);
        return convertView;
    }

    private class ItemsViewHolder {

        ImageView imgItemCategory;
        CustomTextview itemNameText, itemPriceText;
    }

}
