package com.entrada.cheekyMonkey.steward.home_del;

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
public class HomeOptionsAdapter extends ArrayAdapter<HomeItem> {

    public String Status;
    private Context c;
    private int layout_id;
    private ArrayList<HomeItem> table_list;

    public HomeOptionsAdapter(Context context, int textViewResourceId,
                              ArrayList<HomeItem> objects) {

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

        HomeOptions_Holder holder_class;

        if (convertView == null) {

            holder_class = new HomeOptions_Holder();
            convertView = LayoutInflater.from(c).inflate(layout_id, null);
            holder_class.textViewName = (TextView) convertView.findViewById(R.id.textViewOptions);

            convertView.setTag(holder_class);

        } else {

            holder_class = (HomeOptions_Holder) convertView.getTag();

        }

        holder_class.list_obj = table_list.get(position);

        holder_class.textViewName.setText(holder_class.list_obj.getName());
        holder_class.textViewName.setTag(holder_class.list_obj);


        return convertView;

    }

    public static class HomeOptions_Holder {

        public HomeItem list_obj;
        TextView textViewName;

    }
}


