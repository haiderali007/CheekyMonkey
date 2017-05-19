package com.entrada.cheekyMonkey.dynamic.about;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.OnBackPressInterface;
import com.entrada.cheekyMonkey.staticData.PrefHelper;

/**
 * Created by Rahul on 13/05/2016.
 */
public class NewsFragment extends Fragment implements OnBackPressInterface{

    Context context;
    TextView tv_message;
    ListView lv_news_event;
    ArrayAdapter<String> arrayAdapter ;
    ArrayList<String> arrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.news_layout, container, false);
        tv_message= (TextView)view.findViewById(R.id.tv_news);
        lv_news_event = (ListView) view.findViewById(R.id.lv_news_event);

        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(context,R.layout.text_view_for_spinner,arrayList);
        lv_news_event.setAdapter(arrayAdapter);
        showMessage();

        return view;
    }


    public void showMessage(){

        String message = PrefHelper.getStoredString(context,PrefHelper.PREF_FILE_NAME,PrefHelper.EVENT_MESSAGE);
        message = message.replaceAll("Received:","");
        if (!message.isEmpty()){

            try {
                JSONObject jsonObject = new JSONObject(message);
                String msg = jsonObject.getString("message");
                tv_message.setText(msg);

                arrayList.add(jsonObject.getString("contentTitle"));
                arrayList.add(jsonObject.getString("google.message_id"));
                arrayList.add(jsonObject.getString("message"));


            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public boolean onBackPress() {
        return true;
    }
}
