package com.entrada.cheekyMonkey.dynamic.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.OnBackPressInterface;

/**
 * Created by Rahul on 13/05/2016.
 */
public class AboutDeveloperFragment extends Fragment implements OnBackPressInterface{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.developer_layout, container, false);
    }

    @Override
    public boolean onBackPress() {
        return true;
    }
}
