package com.entrada.cheekyMonkey;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 6/20/2017.
 */

public class Add_Outlet extends Fragment {
    TextView textViewEditOutlet;
    ListView listViewEditOutlet;
    Button buttonEditOutlet;
    String outlet[]={"Outlet 1", "Outlet 2", "Outlet 3", "Outlet 4"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adit_outlet, container, false);
        textViewEditOutlet = (TextView) view.findViewById(R.id.tv_edit_outlet);
        listViewEditOutlet = (ListView) view.findViewById(R.id.listView_edit_outlet);
        buttonEditOutlet = (Button) view.findViewById(R.id.btn_submit_edit_outlet);
        Adapter_outlet adapter_outlet = new Adapter_outlet(getContext(), outlet);
        listViewEditOutlet.setAdapter(adapter_outlet);
        listViewEditOutlet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), outlet[position], Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                Toast.makeText(getContext(), "ADD Outlet", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
