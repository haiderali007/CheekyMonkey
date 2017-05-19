package com.entrada.cheekyMonkey.steward.other;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.adapter.POSListAdapter;
import com.entrada.cheekyMonkey.entity.POSItem;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.ui.CustomTextview;


public class POSAdapter<T> extends POSListAdapter<T> {

    UserInfo userInfo;
    private Context context;

    public POSAdapter(Context context) {
        super(context);
        this.context = context;

    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        POSViewHolder holder;
        if (convertView == null) {
            holder = new POSViewHolder();
            convertView = inflater.inflate(R.layout.pos_row_layout, null);
            holder.txtPOS = (CustomTextview) convertView
                    .findViewById(R.id.txtPOS);
            convertView.setTag(holder);

        } else
            holder = (POSViewHolder) convertView.getTag();

        POSItem posItem = (POSItem) dataSet.get(position);
        holder.txtPOS.setText(posItem.posName);

        return convertView;
    }

    public class POSViewHolder {
        public CustomTextview txtPOS;
    }

}
