package com.entrada.cheekyMonkey.steward.discount;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.dynamic.BaseFragmentActivity;
import com.entrada.cheekyMonkey.steward.StewardOrderFragment;
import com.entrada.cheekyMonkey.entity.GroupItems;
import com.entrada.cheekyMonkey.takeorder.ICallTable;
import com.entrada.cheekyMonkey.task.GuestProfileTask;
import com.entrada.cheekyMonkey.task.ICallBackGuestProfileResponse;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.Logger;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by csat on 20/10/2015.
 */
public class GuestDiscountLayout implements View.OnClickListener, ICallBackGuestProfileResponse {

    View view = null;
    LayoutInflater layoutInflater;

    Context context;
    int width;
    ICallTable iCallTable;
    TextView textview_dob, textview_anniv;
    EditText ed_name, ed_mobile, ed_emailId,  ed_address, e_t_address_guest2, e_t_spceial_remaks;
    RadioButton r_m, r_f;
    ImageView imgGuestSearch;
    CustomTextview textViewGuestClear, textViewGuestSubmit, textViewValidity;
    ListView listViewDiscount;
    AdapterGroupItem adapterGroupItem;
    ArrayList<GroupItems> itemGroupList;


    public GuestDiscountLayout(Context context, int width, ICallTable iCallTable) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.width = width;
        this.iCallTable = iCallTable;
    }


    public View getGuestDiscountLayout() {

        view = layoutInflater.inflate(R.layout.guest_create_popup_layout, null);

        ed_name = (EditText) view.findViewById(R.id.e_t_name_for_c_g_p);
        imgGuestSearch = (ImageView) view.findViewById(R.id.imageGuestSearch);
        ed_mobile = (EditText) view.findViewById(R.id.e_t_mobile_for_c_g_p);
        ed_address = (EditText) view.findViewById(R.id.e_t_address_for_c_g_p);
        ed_emailId = (EditText) view.findViewById(R.id.e_t_email_id_for_c_p);
        textview_dob = (TextView) view.findViewById(R.id.tv_dob);
        textview_anniv = (TextView) view.findViewById(R.id.tv_anniv);
        r_m = (RadioButton) view.findViewById(R.id.r_male_for_c_g_p);
        r_f = (RadioButton) view.findViewById(R.id.r_female_for_c_g_p);
        e_t_address_guest2 = (EditText) view.findViewById(R.id.e_t_address_guest2);
        e_t_spceial_remaks = (EditText) view.findViewById(R.id.e_t_spceial_remaks);
        listViewDiscount = (ListView) view.findViewById(R.id.lv_for_discount);
        textViewGuestClear = (CustomTextview) view.findViewById(R.id.textViewGuestClear);
        textViewGuestSubmit = (CustomTextview) view.findViewById(R.id.textViewGuestSave);
        textViewValidity = (CustomTextview) view.findViewById(R.id.textView_Validity);

        imgGuestSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mobile = ed_mobile.getText().toString();
                clearData();
                ed_mobile.setText(mobile);
                GuestSearch();
            }
        });


        textViewGuestClear.setOnClickListener(this);
        textViewGuestSubmit.setOnClickListener(this);
        textview_dob.setOnClickListener(this);
        textview_anniv.setOnClickListener(this);
        textViewValidity.setOnClickListener(this);

        itemGroupList = new ArrayList<>();
        adapterGroupItem = new AdapterGroupItem(context, R.layout.guest_discount_adapter, itemGroupList);
        listViewDiscount.setAdapter(adapterGroupItem);
        listViewDiscount.setOnTouchListener(onTouchListener());
        setDiscountGroup();

        return view;
    }


    public void setDiscountGroup() {
        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        try {

            String queryGroup = "Select * from " + DBConstants.KEY_GROUP_TABLE;
            Cursor cursor = mdb.rawQuery(queryGroup, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    GroupItems groupItems = new GroupItems();
                    groupItems.setGroup_Code(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_GROUP_CODE)));
                    groupItems.setGroup_name(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_GROUP_Name)));
                    groupItems.setGroup_color(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_GROUP_COLOR)));
                    itemGroupList.add(groupItems);

                } while (cursor.moveToNext());

                cursor.close();
            }
            mdb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

        adapterGroupItem.notifyDataSetChanged();
    }

    public View.OnTouchListener onTouchListener() {

        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        };
    }


    public void GuestSearch() {

        try {

            if (!TextUtils.isEmpty(ed_mobile.getText().toString())) {

                String parameter = UtilToCreateJSON.createGstSearchParameter(ed_mobile.getText().toString());
                String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();

                GuestSearchTask guestSearchTask = new GuestSearchTask(context, parameter, serverIP, this);
                guestSearchTask.execute();

            } else
                Toast.makeText(context, "Enter mobile no.", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void responseGuestSearch(String response) {

        Logger.i("GuestSearch", response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject jsonObject1 = jsonObject.getJSONObject("ECABS_GuestSearchResult");
            JSONArray jsonArray = jsonObject1.getJSONArray("Gstmst");
            JSONArray jsonArray1 = jsonObject1.getJSONArray("Dis");

            if (jsonArray.length() != 0) {
                ed_name.setText(jsonArray.getJSONObject(0).getString("Name"));
                ed_emailId.setText(jsonArray.getJSONObject(0).getString("Email"));
                textview_dob.setText(jsonArray.getJSONObject(0).getString("Dob"));
                textview_anniv.setText(jsonArray.getJSONObject(0).getString("M_day"));
                ed_address.setText(jsonArray.getJSONObject(0).getString("Address"));
                e_t_address_guest2.setText(jsonArray.getJSONObject(0).getString("Address1"));
                e_t_spceial_remaks.setText(jsonArray.getJSONObject(0).getString("G_Remark"));

                String validity = jsonArray.getJSONObject(0).getString("DisValiddate");
                if (validity.equals("01/01/1900"))
                    textViewValidity.setText("");
                else
                    textViewValidity.setText(validity);


                if (jsonArray.getJSONObject(0).getString("Sex").equalsIgnoreCase("M"))
                    r_m.setChecked(true);
                else if (jsonArray.getJSONObject(0).getString("Sex").equalsIgnoreCase("F"))
                    r_f.setChecked(true);

                for (int i = 0; i < jsonArray1.length(); i++) {

                    View view = listViewDiscount.getChildAt(i);
                    EditText textDiscount = (EditText) view.findViewById(R.id.e_t_discount);
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                    textDiscount.setText(jsonObject2.getString("Disc"));
                }
            }
            else
                Toast.makeText(context, "Guest not found", Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.textViewGuestClear:

                if (!ed_mobile.getText().toString().isEmpty())
                    clearData();
                else
                    showOrderList();

                break;

            case R.id.textViewGuestSave:

                try {
                    if (validationSuccess())
                    {
                        JSONObject jsonObject = new JSONObject();

                        for (int i = 0; i < itemGroupList.size(); i++) {
                            View view = listViewDiscount.getChildAt(i);
                            if (view == null)
                                break;
                            EditText editText = (EditText) view.findViewById(R.id.e_t_discount);
                            jsonObject.put(itemGroupList.get(i).getGroup_Code(), editText.getText().toString());
                        }

                        String parameter = UtilToCreateJSON.createGuestParamter(
                                ed_name.getText().toString(),
                                ed_mobile.getText().toString(),
                                ed_emailId.getText().toString(),
                                textview_dob.getText().toString(),
                                textview_anniv.getText().toString(),
                                r_m.isChecked() ? "M" : "F",
                                ed_address.getText().toString(),
                                e_t_address_guest2.getText().toString(),
                                e_t_spceial_remaks.getText().toString(),
                                textViewValidity.getText().toString(),
                                jsonObject.toString(),true);

                        String serverIP = POSApplication.getSingleton()
                                .getmDataModel().getUserInfo().getServerIP();

                        GuestProfileTask guestProfileTask = new GuestProfileTask(context, parameter, serverIP,null,this);
                        guestProfileTask.execute();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;

            case R.id.tv_dob:
                setDate(textview_dob, false);
                break;

            case R.id.tv_anniv:
                setDate(textview_anniv, false);
                break;

            case R.id.textView_Validity:
                setDate(textViewValidity, true);
                break;
        }
    }

    public void setDate(TextView textView, boolean flag){

        DateSetComment.setTextView(textView, flag);
        DateSetComment newFragment = new DateSetComment();
        newFragment.show(((BaseFragmentActivity) context).getFragmentManager(), "datePicker");
    }

    @Override
    public void responseGuestSave(String response) {

        Logger.i("SaveResponse", response);
        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("ECABS_SaveGuestResult");

            if (jsonArray.length() > 0) {

                updateGuestTable(ed_mobile.getText().toString(), ed_name.getText().toString());
                Toast.makeText(context, "detail saved", Toast.LENGTH_LONG).show();

                if (iCallTable != null) {
                    String phone = ed_mobile.getText().toString();
                    String name = ed_name.getText().toString();
                    iCallTable.responseGuestDiscount(phone, name);
                }

                clearData();
                showOrderList();

            } else
                Toast.makeText(context, "Please try again", Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void updateGuestTable(String code, String name) {

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context).getWritableDatabase();
        mdb.beginTransaction();
        try {

            mdb.delete(DBConstants.KEY_GUEST_TYPE_TABLE, DBConstants.KEY_GUEST_TYPE_CODE + "=" + code, null);   // return 1, if deleted. else,0.
            ContentValues cv = new ContentValues();
            cv.put(DBConstants.KEY_GUEST_TYPE_CODE, code);
            cv.put(DBConstants.KEY_GUEST_TYPE_NAME, name);

            mdb.insert(DBConstants.KEY_GUEST_TYPE_TABLE, null, cv);
            mdb.setTransactionSuccessful();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }
    }

    public void showOrderList() {

        if (iCallTable instanceof StewardOrderFragment) {

            StewardOrderFragment takeOrderFragment = (StewardOrderFragment) iCallTable;
            takeOrderFragment.showDefault();
        }
    }

    private Boolean validationSuccess() {

        if (ed_mobile.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(context, "Enter mobile no.", Toast.LENGTH_SHORT).show();
            return false;
        }

/*        if (ed_name.getText().toString().equalsIgnoreCase("")) {

            Toast.makeText(context, "Enter name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ed_address.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(context, "Enter address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (e_t_spceial_remaks.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(context, "Enter special remark", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ed_emailId.getText().toString().equalsIgnoreCase("") || !(ed_emailId.getText().toString().contains("@"))) {
            Toast.makeText(context, "Enter valid email id", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(textViewValidity.getText().toString().equals("")) {
            Toast.makeText(context, "Enter discount validity", Toast.LENGTH_SHORT).show();
            return false;
        }*/

        return true;
    }

    public void clearData() {
        ed_name.setText("");
        ed_mobile.setText("");
        ed_emailId.setText("");
        textview_dob.setText("");
        textview_anniv.setText("");
        ed_address.setText("");
        e_t_address_guest2.setText("");
        e_t_spceial_remaks.setText("");
        textViewValidity.setText("");

        for (int i = 0; i < itemGroupList.size(); i++) {
            View view = listViewDiscount.getChildAt(i);
            if (view == null) break;

            EditText editTextDiscount = (EditText) view.findViewById(R.id.e_t_discount);
            editTextDiscount.setText("");
        }
    }


    public static class DateSetComment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        static TextView textView;
        static boolean flag = true;

        public static void setTextView(TextView tv, boolean allowMinDate) {
            textView = tv;
            flag = allowMinDate;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            int year, month, day;
            Calendar c = Calendar.getInstance();

            if (TextUtils.isEmpty(textView.getText())) {
                c.set(Calendar.MONTH, c.get(Calendar.MONTH));

                day = c.get(Calendar.DAY_OF_MONTH);
                month = (c.get(Calendar.MONTH));
                year = c.get(Calendar.YEAR);
            } else {
                String date[] = textView.getText().toString().split("/");

                day = Integer.parseInt(date[0]);
                month = Integer.parseInt(date[1]) - 1;
                year = Integer.parseInt(date[2]);
            }

            DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
            DatePicker dp = dpd.getDatePicker();

            if (flag){
                //Set the DatePicker minimum date selection to current date
                dp.setMinDate(c.getTimeInMillis());//get the current day
            }


            // Create a new instance of DatePickerDialog and return it
            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user


            String days = "", months = "";

            if ((day < 10 || month + 1 < 10)) {
                if (day < 10)
                    days = "0" + day;
                else
                    days = "" + day;


                if (month + 1 < 10)
                    months = "0" + (month + 1);
                else
                    months = "" + (month + 1);

                textView.setText(days + "/" + months + "/" + year);

            } else
                textView.setText(day + "/" + (month + 1) + "/" + year);
        }
    }
}