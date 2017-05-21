package com.entrada.cheekyMonkey;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entrada.cheekyMonkey.appInterface.OnBackPressInterface;


/**
 * A simple {@link Fragment} subclass.
 */
public class Help_Layout extends Fragment implements OnBackPressInterface {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help__layout, container, false);
    }

    @Override
    public boolean onBackPress() {
        return true;
    }
}
