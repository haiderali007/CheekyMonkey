package com.entrada.cheekyMonkey.dynamic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.OnBackPressInterface;
import com.entrada.cheekyMonkey.ui.CustomTextview;

/**
 * Created by Rahul on 13/05/2016.
 */
public class ActiveNotificationsFragment extends Fragment implements OnBackPressInterface{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.active_noti_layout, container, false);

        CustomTextview tv_no_noti = (CustomTextview) view.findViewById(R.id.tv_no_noti);
        ListView listView = (ListView)view.findViewById(R.id.lv_notifications);
        listView.setAdapter(((BaseFragmentActivity)getActivity()).getRightListAdapter());

        if (listView.getCount() > 0)
            tv_no_noti.setVisibility(View.GONE);

        return view;
    }


    @Override
    public boolean onBackPress() {
        return true;
    }
}
