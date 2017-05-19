package com.entrada.cheekyMonkey.steward.bill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.entrada.cheekyMonkey.R;

import java.util.ArrayList;


/**
 * Created by csat on 15/06/2015.
 */
public class AdapterManualDiscount extends ArrayAdapter<DiscountItem> {

    public String Status;
    public ArrayList<String> listtable;
    private Context c;
    private int layout_id;
    private ArrayList<DiscountItem> table_list;

    public AdapterManualDiscount(Context context, int textViewResourceId,
                                 ArrayList<DiscountItem> objects) {

        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub

        this.c = context;
        this.layout_id = textViewResourceId;
        this.table_list = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        // super.getView(position, convertView, parent);

        Discount_show_List_holder holder_class;

        if (convertView == null) {

            holder_class = new Discount_show_List_holder();
            convertView = LayoutInflater.from(c).inflate(layout_id, null);

            holder_class.textViewDiscount = (TextView) convertView
                    .findViewById(R.id.textViewDiscountMaster);

            convertView.setTag(holder_class);

        } else {

            holder_class = (Discount_show_List_holder) convertView.getTag();

        }

        holder_class.list_obj = table_list.get(position);

        holder_class.textViewDiscount.setText(holder_class.list_obj.getName());
        holder_class.textViewDiscount.setTag(holder_class.list_obj);


        return convertView;

    }

    public static class Discount_show_List_holder {

        public DiscountItem list_obj;
        TextView textViewDiscount;

    }

}


