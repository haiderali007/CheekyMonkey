package com.entrada.cheekyMonkey.dynamic.about;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.entity.TitleHeader;

import java.util.ArrayList;


public class GridTitleAdapterSteward extends ArrayAdapter<TitleHeader> {

    private Context context;
    private int resourceId;
    private ArrayList<TitleHeader> arrayList;

    public GridTitleAdapterSteward(Context context, int resourceId,
                                   ArrayList<TitleHeader> arrayList) {

        super(context, resourceId, arrayList);

        this.context = context;
        this.resourceId = resourceId;
        this.arrayList = arrayList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TitleViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new TitleViewHolder();
            convertView = LayoutInflater.from(context).inflate(resourceId, null);

            viewHolder.view = (ImageView) convertView
                    .findViewById(R.id.viewSelector);
            viewHolder.textView = (TextView) convertView
                    .findViewById(R.id.txtTitle);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (TitleViewHolder) convertView.getTag();
        }


        viewHolder.textView.setText(arrayList.get(position).getTitleHeader());
        viewHolder.textView.setTag(viewHolder);
        viewHolder.view.setImageResource(resourceId(position));
        viewHolder.view.setTag(viewHolder);

        convertView.setActivated(arrayList.get(position).isPosition());
        convertView.setBackgroundColor(position % 2 == 0 ? 0xFF2a3644 : 0xFF2D3B48) ;
        return convertView;
    }

    private int resourceId(int position) {

        int resId = R.drawable.home;

        switch (position) {
            case 0:
                resId = R.drawable.home;
                break;
            case 1:
                resId = R.drawable.order;
                break;
            case 2:
                resId = R.drawable.bills;
                break;
            case 3:
                resId = R.drawable.tables;
                break;
            case 4:
                resId = R.drawable.settlement;
                break;
            case 5:
                resId = R.drawable.unsettle;
                break;
            case 6:
                resId = R.drawable.generate;
                break;
            case 7:
                resId = R.drawable.discount;
                break;
            case 8:
                resId = R.drawable.under_process;
                break;
            case 9:
                resId = R.drawable.order_accepted;
                break;
            case 10:
                resId = R.drawable.order_rejected;
                break;
            case 11:
                resId = R.drawable.cover;
                break;
            case 12:
                resId = R.drawable.logout;
                break;

            default:
                break;
        }

        return resId;
    }

    private class TitleViewHolder {

        TextView textView;
        ImageView view;

    }

}
