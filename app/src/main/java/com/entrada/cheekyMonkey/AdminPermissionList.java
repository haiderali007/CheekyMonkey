package com.entrada.cheekyMonkey;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

/**
 * Created by Administrator on 6/27/2017.
 */

public class AdminPermissionList extends Activity {

    ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_permission_list);
        listView = (ListView) findViewById(R.id.lv_admin_custom);



    }
}
