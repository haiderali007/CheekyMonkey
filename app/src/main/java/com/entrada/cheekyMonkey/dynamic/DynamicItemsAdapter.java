package com.entrada.cheekyMonkey.dynamic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.entity.MenuItem;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.ui.CustomTextview;

/**
 * Created by Tanuj.Sareen on 1/30/2015.
 */
public class DynamicItemsAdapter extends BaseAdapter {

    UserInfo userInfo;
    private Context context;
    private HashMap<Integer,Integer> sectionHeaderDetail = new HashMap<>();
    private ArrayList<MenuItem> itemsArrayList = new ArrayList<>();

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    int prev = 0;
    boolean start = false, current = false;


    public DynamicItemsAdapter(Context context) {
        super();
        this.context = context;
        this.userInfo = POSApplication.getSingleton().getmDataModel().getUserInfo();
    }


    public void clear(){
        itemsArrayList.clear();
    }

    public void addDataSet(ArrayList<MenuItem> items){
        itemsArrayList.addAll(items);
    }

    public void addSectionHeaderItem(MenuItem item, int totalItems) {
        itemsArrayList.add(item);
        sectionHeaderDetail.put(itemsArrayList.size() - 1, totalItems);
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeaderDetail.containsKey(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getCount() {
        return itemsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemsViewHolder viewHolder;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            viewHolder = new ItemsViewHolder();

            switch (rowType) {


                case TYPE_ITEM:
                    convertView = LayoutInflater.from(context).inflate(R.layout.dynamic_items_layout, null);
                    viewHolder.itemNameText = (CustomTextview) convertView.findViewById(R.id.tv_itemName);
                    viewHolder.itemHighPrice = (CustomTextview) convertView.findViewById(R.id.tv_itemHighPrice);
                    viewHolder.itemLowPrice = (CustomTextview) convertView.findViewById(R.id.tv_itemLowPrice);
                    viewHolder.itemPrice = (CustomTextview) convertView.findViewById(R.id.tv_itemPrice);
                    viewHolder.img_trend = (ImageView) convertView.findViewById(R.id.img_trend);
                    break;


                case TYPE_SEPARATOR:
                    convertView = LayoutInflater.from(context).inflate(R.layout.snippet, null);
                    viewHolder.sectionHeader = (CustomTextview) convertView.findViewById(R.id.textSeparator);
                    viewHolder.tv_ttl_items = (CustomTextview) convertView.findViewById(R.id.tv_ttl_items);

                    break;
            }

            if (convertView != null)
                convertView.setTag(viewHolder);

        } else
            viewHolder = (ItemsViewHolder) convertView.getTag();


        if (rowType == TYPE_SEPARATOR){
            String catDesc = itemsArrayList.get(position).Cat_Desc ;
            viewHolder.sectionHeader.setText(catDesc);
            viewHolder.tv_ttl_items.setText(sectionHeaderDetail.get(position) + " Items");

        } else {

            current = position < prev ? start = ! start : ! current;
            prev = position;

            MenuItem menuItem = itemsArrayList.get(position);

            if (! menuItem.getMenu_name().isEmpty()) {
                viewHolder.itemNameText.setText(menuItem.getMenu_name());
                viewHolder.itemHighPrice.setText(String.format(Locale.US, "%.0f", menuItem.getMax_Price()));
                viewHolder.itemLowPrice.setText(String.format(Locale.US, "%.0f", menuItem.getMin_Price()));

                if (current){
                    viewHolder.itemPrice.setText(String.format(Locale.US, "%.0f", menuItem.getMenu_price()));
                    viewHolder.itemPrice.setBackgroundResource(R.color.curr_price_color);
                    viewHolder.img_trend.setBackgroundResource(R.drawable.arrow_red);

                }else {

                    float nextPrice = menuItem.getMenu_price() + menuItem.getInc_Rate();

                    if (nextPrice > menuItem.getMax_Price())
                        viewHolder.itemPrice.setText(String.format(Locale.US, "%.0f", menuItem.getMax_Price()));
                    else
                        viewHolder.itemPrice.setText(String.format(Locale.US, "%.0f", nextPrice));

                    if (menuItem.getMin_Price() == menuItem.getMax_Price()){
                        viewHolder.itemPrice.setBackgroundResource(R.color.curr_price_color);
                        viewHolder.img_trend.setBackgroundResource(R.drawable.arrow_red);}
                    else{
                        viewHolder.itemPrice.setBackgroundResource(R.color.next_price_color);
                        viewHolder.img_trend.setBackgroundResource(R.drawable.arrow_green);
                    }
                }
            }
        }

        return convertView;
    }


    public class ItemsViewHolder {

        public CustomTextview itemNameText, itemHighPrice,itemLowPrice, itemPrice;
        public CustomTextview sectionHeader, tv_ttl_items ;
        public ImageView img_trend;
    }

}
