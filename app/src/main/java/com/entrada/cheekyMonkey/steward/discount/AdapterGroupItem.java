package com.entrada.cheekyMonkey.steward.discount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.entity.GroupItems;

import java.util.ArrayList;


/**
 * Created by CSAT on 30/10/2015.
 */
public class AdapterGroupItem extends ArrayAdapter<GroupItems> {


    public String Status;
    private Context c;
    private int layout_id;
    private ArrayList<GroupItems> groupList;

    public AdapterGroupItem(Context context, int textViewResourceId,
                            ArrayList<GroupItems> objects) {

        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub

        this.c = context;
        this.layout_id = textViewResourceId;
        this.groupList = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        // super.getView(position, convertView, parent);

        HolderClass holder_class;

        if (convertView == null) {

            holder_class = new HolderClass();
            convertView = LayoutInflater.from(c).inflate(layout_id, null);

            holder_class.textViewDiscount = (TextView) convertView
                    .findViewById(R.id.textview_for_discount);
            holder_class.editTextDiscount = (EditText) convertView
                    .findViewById(R.id.e_t_discount);

            convertView.setTag(holder_class);

        } else {

            holder_class = (HolderClass) convertView.getTag();


        }

        holder_class.list_obj = groupList.get(position);

        holder_class.textViewDiscount.setText(holder_class.list_obj.getGroup_name());
        holder_class.editTextDiscount.setText(holder_class.list_obj.getDiscount());

        holder_class.textViewDiscount.setTag(holder_class.list_obj);
        holder_class.editTextDiscount.setTag(holder_class.list_obj);


        return convertView;

    }

    public static class HolderClass {

        public GroupItems list_obj;
        TextView textViewDiscount;
        EditText editTextDiscount;

    }

}