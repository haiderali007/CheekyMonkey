package com.entrada.cheekyMonkey;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;

/**
 * Created by Administrator on 6/20/2017.
 */

public class Adapter_outlet extends ArrayAdapter<String> {

    private String outlet[];
    private Context context;

    public Adapter_outlet(@NonNull Context context, String[] outlet) {
        super(context, R.layout.outlet_customview_design, outlet);
        this.outlet = outlet;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.outlet_customview_design, parent);
        TextView tv = (TextView) view.findViewById(R.id.add_out_text_view);
        ImageView imageViewAdd = (ImageView) view.findViewById(R.id.add_out_edit_image);
        ImageView imageViewDelete = (ImageView) view.findViewById(R.id.add_out_delete_image);
        tv.setText(outlet[position]);

       return view;
    }
}
