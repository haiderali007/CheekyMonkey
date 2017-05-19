package com.entrada.cheekyMonkey.addons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.entrada.cheekyMonkey.R;


/**
 * Created by Administrator on 5/7/2017.
 */
public class Adapter_Add_Mix extends ArrayAdapter<String> {

    private Context context;
    private String stringItem[];
    private String stringSymbol[];
    private String stringPrice[];

    public Adapter_Add_Mix(Context context, String[] stringItem, String[] rupee_symbol, String[] stringPrice) {
        super(context, R.layout.addon_row_layout_new, stringItem);
        this.context = context;
        this.stringItem = stringItem;
        this.stringSymbol = rupee_symbol;
        this.stringPrice = stringPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view= inflater.inflate(R.layout.addon_row_layout_new, parent);

        TextView textViewCustomeItem = (TextView) view.findViewById(R.id.txtaddonName);
        TextView textViewRupee = (TextView) view.findViewById(R.id.txtAddonQtyView);
        TextView textViewPrice = (TextView) view.findViewById(R.id.txtAddonPriceView);
        textViewCustomeItem.setText(stringItem[position]);
        textViewRupee.setText(stringSymbol[position]);
        textViewPrice.setText(stringPrice[position]);
        return view;
    }
}
