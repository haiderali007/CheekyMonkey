package com.entrada.cheekyMonkey.dynamic.about;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.entity.TitleHeader;
import com.entrada.cheekyMonkey.ui.CustomTextview;


public class GridTitleAdapter extends ArrayAdapter<TitleHeader> {

    private Context context;
    private int resourceId;
    private ArrayList<TitleHeader> arrayList;

    public GridTitleAdapter(Context context, int resourceId,
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
            viewHolder.textView = (CustomTextview) convertView
                    .findViewById(R.id.txtTitle);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (TitleViewHolder) convertView.getTag();
        }


        viewHolder.textView.setText(arrayList.get(position).getTitleHeader());
        viewHolder.textView.setTag(viewHolder);
        viewHolder.view.setImageResource(resourceId(position));
        viewHolder.view.setTag(viewHolder);

        if (arrayList.get(position).isPosition())
            convertView.setActivated(true);
        else
            convertView.setActivated(false);

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
                resId = R.drawable.bidding;
                break;
            case 2:
                resId = R.drawable.direction;
                break;
            case 3:
                resId = R.drawable.news;
                break;
            case 4:
                resId = R.drawable.rating;
                break;

            case 5:
                resId = R.drawable.about;
                break;
            case 6:
                resId = R.drawable.developer;
                break;
            case 7:
                resId = R.drawable.facebook;
                break;
            case 8:
                resId = R.drawable.insta;
                break;
            case 9:
                resId = R.drawable.terms;
                break;
            case 10:
                resId = R.drawable.help;
                break;
            case 11:
                resId = R.drawable.logout;
                break;
            default:
                break;
        }

        return resId;
    }

    private class TitleViewHolder {

        CustomTextview textView;
        ImageView view;

    }

}
