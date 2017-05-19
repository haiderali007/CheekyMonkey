package com.entrada.cheekyMonkey.dynamic.start;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.entity.POSItem;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.pointOfSale.PosTitleEntity;

/**
 * Created by CSET on 08/07/2016.
 */
public class MoreOptionsLayout {

    LayoutInflater layoutInflater;
    Context context;
    UserInfo info;

    ArrayList<PosTitleEntity> arrayList = new ArrayList<>();
    OptionsExAdapt exAdapt;
    ExpandableListView posSector;

    public MoreOptionsLayout(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.info = POSApplication.getSingleton().getmDataModel().getUserInfo();
    }

    public View addViewlayout() {

        View posHeader = layoutInflater.inflate(R.layout.listview_header, null);
        posSector = (ExpandableListView) posHeader.findViewById(R.id.posSector);
        exAdapt = new OptionsExAdapt(context, arrayList);
        posSector.setAdapter(exAdapt);
        setPosSector();

        return posHeader;
    }

    public void setPosSector(){

        PosTitleEntity entity = new PosTitleEntity();
        entity.Tittle = "More ...";
        ArrayList<POSItem> items = new ArrayList<>();

        POSItem object = new POSItem();
        object.posName = "Lock Drawer";
        items.add(object);

        entity.arrayListPosItems.add(object);
        arrayList.add(entity);
        exAdapt.notifyDataSetChanged();
    }


}
