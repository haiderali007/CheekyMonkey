package com.entrada.cheekyMonkey;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Administrator on 6/20/2017.
 */

public class Outlet_Users extends Fragment {

    TextView textViewLabel;
    ListView listViewOutlet;
    Button buttonSubmitOutlet;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.outlet_users, container, false);
        textViewLabel = (TextView) view.findViewById(R.id.tv_outlet);
        listViewOutlet = (ListView) view.findViewById(R.id.listViewOutlet);
        buttonSubmitOutlet = (Button) view.findViewById(R.id.btn_submit_outlet);


        return view;
    }
}
