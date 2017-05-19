package com.entrada.cheekyMonkey.addons;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.entrada.cheekyMonkey.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Add_Mixer_Fragment extends Fragment implements AdapterView.OnItemClickListener{

    String addmix_item[] = {"Soda", "Pakcked", "Red Bull", "Aerated Drinks", "Diet coke", "Refer to ice",
            "Tonic water", "Coke can", "Juice", "Hot water"};
    String addmix_rupee_symbol[] = {"₹ ", "₹ ", "₹ ", "₹ ", "₹ ", "₹ ", "₹ ", "₹ ", "₹ ", "₹ "};
    String admix_price[] = {"25", "39", "149", "29", "49", "1", "99", "49", "49", "9"};


    ListView listViewAddMix;
    TextView textViewProcess;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_mixer, container, false);
        listViewAddMix = (ListView) view.findViewById(R.id.add_mix_listView);
        textViewProcess = (TextView) view.findViewById(R.id.tv_add_mix_process);
        Adapter_Add_Mix adapter_add_mix = new Adapter_Add_Mix(getContext(), addmix_item, addmix_rupee_symbol, admix_price);
        listViewAddMix.setAdapter(adapter_add_mix);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getContext(), addmix_item[position], Toast.LENGTH_SHORT).show();
    }
}
