package com.entrada.cheekyMonkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;

import java.util.ArrayList;

/**
 * Created by ${Tanuj.Sareen} on 07/03/2015.
 */
public abstract class POSListAdapter<T> extends BaseAdapter {

    public ArrayList<T> dataSet;
    public LayoutInflater inflater;
    public Context context;
    protected int isSelectedPosition = -1;

    public POSListAdapter(Context context) {

        dataSet = new ArrayList<T>();
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    public int getIsSelectedPosition() {
        return isSelectedPosition;
    }

    public void setIsSelectedPosition(int isSelectedPosition) {
        this.isSelectedPosition = isSelectedPosition;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public abstract View getView(int position, View convertView,
                                 ViewGroup parent);

    public void addDataSetList(ArrayList<T> list) {
        dataSet.addAll(list);
        notifyDataSetChanged();
    }

    public void updateDataSetList(ArrayList<T> list) {

        for (T t : list){

            boolean itemFound = false;
            for (int i=0; i<dataSet.size(); i++){

                OrderItem item = (OrderItem) t;
                OrderItem savedItem = (OrderItem) dataSet.get(i);
                if (savedItem.o_code.equals(item.o_code)){
                    ((OrderItem) dataSet.get(i)).o_quantity = item.o_quantity;
                    itemFound = true;
                    break;
                }
            }

            if (! itemFound)
                dataSet.add(t);
        }

        notifyDataSetChanged();
    }

    public void setDataSetItem(int index, T dataItem) {
        dataSet.set(index, dataItem);
        notifyDataSetChanged();
    }

    public void addDataSetItem(T dataItem) {
        dataSet.add(dataItem);
        notifyDataSetChanged();
    }

    public void removeDataSetItem(int position) {
        dataSet.remove(position);
        notifyDataSetChanged();
    }

    public void clearDataSet() {
        dataSet.clear();
    }

    public void clearDataSetALL() {
        dataSet.clear();
    }

    public ArrayList<T> getDataSet() {
        return dataSet;
    }


}
