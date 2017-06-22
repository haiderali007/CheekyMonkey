package com.entrada.cheekyMonkey;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Administrator on 6/20/2017.
 */

public class Adapter_outlet extends ArrayAdapter<String> implements View.OnClickListener {

    ArrayList<String> outlet;
    private Context context;
    Add_Outlet add_outlets;

    ImageView imageViewAdd;
    ImageView imageViewDelete;

    public Adapter_outlet(Context context, ArrayList<String> outlet) {
        super(context, R.layout.outlet_customview_design, outlet);
        this.outlet = outlet;
        this.context = context;
        add_outlets = (Add_Outlet) context;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.outlet_customview_design, null);
        TextView tv = (TextView) view.findViewById(R.id.add_outlet_text_view);
        ImageView imageViewAdd = (ImageView) view.findViewById(R.id.add_out_edit_image);
        ImageView imageViewDelete = (ImageView) view.findViewById(R.id.add_out_delete_image);
        tv.setText(outlet.get(position));

        imageViewAdd.setOnClickListener(this);
        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outlet.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_out_edit_image:
                Intent intent = new Intent(add_outlets, Edit_Outlet_Details.class);
                add_outlets.startActivity(intent);

        }
    }
}
