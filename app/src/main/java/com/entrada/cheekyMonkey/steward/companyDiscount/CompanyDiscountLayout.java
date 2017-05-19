package com.entrada.cheekyMonkey.steward.companyDiscount;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.db.DBConstants;
import com.entrada.cheekyMonkey.db.POSDatabase;
import com.entrada.cheekyMonkey.dynamic.BaseFragmentActivity;
import com.entrada.cheekyMonkey.steward.StewardOrderFragment;
import com.entrada.cheekyMonkey.steward.discount.AdapterGroupItem;
import com.entrada.cheekyMonkey.steward.discount.GuestDiscountLayout;
import com.entrada.cheekyMonkey.entity.GroupItems;
import com.entrada.cheekyMonkey.ui.CustomEditView;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.Logger;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by kamal on 16/10/2015.
 */
public class CompanyDiscountLayout implements View.OnClickListener, ICallBackCompnayDiscResponse {

    public CustomTextview textViewCompClear, textViewCompSave, textViewCompanyInfoDate, textViewLoyaltyEffDate, textViewValidity;
    public String code = "", name = "", status = "";
    View view = null;
    LayoutInflater layoutInflater;
    Context context;
    int width;
    StewardOrderFragment takeOrderFragment ;

    ListView listViewDiscount;
    ImageView imgCompSearch;
    AdapterGroupItem adapterGroupItem;
    ArrayList<GroupItems> list;
    private Spinner spinnerStatusComp;
    private CustomEditView editTextCompCode, editTextCompName, editTextAddress1, editTextAddress2, editTextCompCity,
            editTextCompPin, edittTextPhone;

    public CompanyDiscountLayout(Context context, int width, StewardOrderFragment takeOrderFragment) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.width = width;
        this.takeOrderFragment = takeOrderFragment;
    }


    public View getCompanyDiscountPopupWindow() {

        view = layoutInflater.inflate(R.layout.company_profile_layout, null);

        imgCompSearch = (ImageView) view.findViewById(R.id.imgCompanySearch);
        spinnerStatusComp = (Spinner) view.findViewById(R.id.spinnerStatusComp);
        textViewCompClear = (CustomTextview) view.findViewById(R.id.textViewCompClear);
        textViewCompSave = (CustomTextview) view.findViewById(R.id.tevCompSave);
        editTextCompCode = (CustomEditView) view.findViewById(R.id.editTextCompCode);
        editTextCompName = (CustomEditView) view.findViewById(R.id.editTextCompName);
        editTextAddress1 = (CustomEditView) view.findViewById(R.id.editTextAddress1);
        editTextAddress2 = (CustomEditView) view.findViewById(R.id.editTextAddress2);
        editTextCompCity = (CustomEditView) view.findViewById(R.id.editTextCompCity);
        editTextCompPin = (CustomEditView) view.findViewById(R.id.editTextCompPin);
        edittTextPhone = (CustomEditView) view.findViewById(R.id.edittTextPhone);
        textViewCompanyInfoDate = (CustomTextview) view.findViewById(R.id.textViewCompanyInfoDate);
        textViewLoyaltyEffDate = (CustomTextview) view.findViewById(R.id.testViewLoyaltyEffDate);
        textViewValidity = (CustomTextview) view.findViewById(R.id.testViewDiscValidity);
        listViewDiscount = (ListView) view.findViewById(R.id.lv_for_Discount);

        imgCompSearch.setOnClickListener(this);
        textViewCompClear.setOnClickListener(this);
        textViewCompSave.setOnClickListener(this);
        textViewCompanyInfoDate.setOnClickListener(this);
        textViewLoyaltyEffDate.setOnClickListener(this);
        textViewValidity.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter_Status_company = ArrayAdapter
                .createFromResource(context, R.array.table_status_arrays,
                        android.R.layout.simple_spinner_item);
        adapter_Status_company.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatusComp.setAdapter(adapter_Status_company);


        list = new ArrayList<>();
        adapterGroupItem = new AdapterGroupItem(context, R.layout.guest_discount_adapter, list);
        listViewDiscount.setAdapter(adapterGroupItem);
        setDiscountGroup();

        return view;
    }


    public void setDiscountGroup() {
        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        try {

            String queryGroup = "Select * from "
                    + DBConstants.KEY_GROUP_TABLE;
            Cursor cursor = mdb.rawQuery(queryGroup, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    GroupItems groupItems = new GroupItems();
                    groupItems.setGroup_Code(cursor.getString(cursor
                            .getColumnIndex(DBConstants.KEY_GROUP_CODE)));
                    groupItems
                            .setGroup_name(cursor.getString(cursor
                                    .getColumnIndex(DBConstants.KEY_GROUP_Name)));
                    groupItems
                            .setGroup_color(cursor.getString(cursor
                                    .getColumnIndex(DBConstants.KEY_GROUP_COLOR)));
                    list.add(groupItems);

                } while (cursor.moveToNext());
            }
            mdb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

        adapterGroupItem.notifyDataSetChanged();

    }


    public void CompSearch() {
        try {
            if (!TextUtils.isEmpty(editTextCompCode.getText().toString())) {
                String parameter = UtilToCreateJSON.createCompSearchParameter(editTextCompCode.getText().toString());

                String serverIP = POSApplication.getSingleton()
                        .getmDataModel().getUserInfo().getServerIP();

                CompanySearchTask companySearchTask = new CompanySearchTask(context, parameter, serverIP, this);
                companySearchTask.execute();
            } else
                Toast.makeText(context, "Enter company code.", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void responseCompSearch(String response) {

        Logger.i("CompanySearch", response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject jsonObject1 = jsonObject.getJSONObject("ECABS_CompanyResult");
            JSONArray jsonArray = jsonObject1.getJSONArray("Compmst");
            JSONArray jsonArray1 = jsonObject1.getJSONArray("Disc");

            if (jsonArray.length() != 0) {
                editTextCompName.setText(jsonArray.getJSONObject(0).getString("COMP_NAME"));
                editTextAddress1.setText(jsonArray.getJSONObject(0).getString("COMP_ADD1"));
                editTextAddress2.setText(jsonArray.getJSONObject(0).getString("COMP_ADD2"));
                editTextCompCity.setText(jsonArray.getJSONObject(0).getString("COMP_CITY"));
                editTextCompPin.setText(jsonArray.getJSONObject(0).getString("COMP_PIN"));
                edittTextPhone.setText(jsonArray.getJSONObject(0).getString("COMP_PHONE1"));
                String info_date = jsonArray.getJSONObject(0).getString("COMP_INFO_DATE");
                String loyality_date = jsonArray.getJSONObject(0).getString("COMP_LOYALTY_DATE");
                String validity = jsonArray.getJSONObject(0).getString("COMP_VALID_DATE");

                updateDate(textViewCompanyInfoDate, info_date);
                updateDate(textViewLoyaltyEffDate, loyality_date);
                updateDate(textViewValidity, validity);


                String status = jsonArray.getJSONObject(0).getString("COMP_STS");
                int index = 0;
                if (status.equals("Active"))
                    index = 1;
                else if (status.equals("InActive"))
                    index = 2;
                spinnerStatusComp.setSelection(index);

                for (int i = 0; i < jsonArray1.length(); i++) {
                    View view = listViewDiscount.getChildAt(i);
                    EditText textDiscount = (EditText) view.findViewById(R.id.e_t_discount);
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                    textDiscount.setText(jsonObject2.getString("Disc"));
                }

            } else
                Toast.makeText(context, "Company not found", Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateDate(TextView textView, String validity) {
        if (validity.equals("01/01/1900"))
            textView.setText("");
        else
            textView.setText(validity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tevCompSave:

                try {

                    if (validationSuccess()) {

                        JSONObject jsonDiscount = new JSONObject();
                        for (int i = 0; i < list.size(); i++) {
                            View view = listViewDiscount.getChildAt(i);
                            EditText textDiscount = (EditText) view.findViewById(R.id.e_t_discount);
                            jsonDiscount.put(list.get(i).getGroup_Code(), textDiscount.getText().toString());
                        }

                        String parameter = UtilToCreateJSON.CreateCompanyDisountJson(
                                context, editTextCompCode, editTextCompName, editTextAddress1,
                                editTextAddress2, editTextCompCity, editTextCompPin, edittTextPhone,
                                textViewCompanyInfoDate, textViewLoyaltyEffDate, spinnerStatusComp,
                                textViewValidity.getText().toString(), jsonDiscount.toString());

                        String serverIP = POSApplication.getSingleton()
                                .getmDataModel().getUserInfo().getServerIP();
                        if (!(TextUtils.isEmpty(parameter) && TextUtils
                                .isEmpty(serverIP))) {

                            SaveCompanyInfoTask saveCompanyInfoTask = new SaveCompanyInfoTask(context, parameter,
                                    serverIP, this);
                            saveCompanyInfoTask.execute();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case R.id.textViewCompClear:

                if (!editTextCompCode.getText().toString().isEmpty())
                    clearData();
                else
                    takeOrderFragment.showDefault();
                break;

            case R.id.textViewCompanyInfoDate:

                DateSetComment.setTextView(textViewCompanyInfoDate);
                DateSetComment newFragment = new DateSetComment();
                newFragment.show(((BaseFragmentActivity) context).getFragmentManager(), "datePicker");
                break;


            case R.id.testViewLoyaltyEffDate:

                DateSetComment.setTextView(textViewLoyaltyEffDate);
                DateSetComment newFragment1 = new DateSetComment();
                newFragment1.show(((BaseFragmentActivity) context).getFragmentManager(), "datePicker");
                break;

            case R.id.imgCompanySearch:
                CompSearch();
                break;


            case R.id.testViewDiscValidity:

                GuestDiscountLayout.DateSetComment.setTextView(textViewValidity, true);
                GuestDiscountLayout.DateSetComment newFragment2 = new GuestDiscountLayout.DateSetComment();
                newFragment2.show(((BaseFragmentActivity) context).getFragmentManager(), "datePicker");
                break;
        }
    }

    public static class DateSetComment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        static TextView textView;

        public static void setTextView(TextView tv) {
            textView = tv;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            int year, month, day;

            if (TextUtils.isEmpty(textView.getText())) {
                final Calendar c = Calendar.getInstance();
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

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
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

    @Override
    public void responseCompSave(String response) {

        try {

            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("ECABS_SavedCompnayResult").equals("Saved")){

                updateCompanyTable(editTextCompCode.getText().toString(), editTextCompName.getText().toString());
                clearData();
                takeOrderFragment.showDefault();
                Toast.makeText(context,"detail saved", Toast.LENGTH_LONG).show();

            } else
                Toast.makeText(context, response, Toast.LENGTH_LONG).show();

        } catch (JSONException e) {e.printStackTrace();
        }
    }


    public void updateCompanyTable(String code, String name) {
        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();
        try {

            ContentValues cv = new ContentValues();

            cv.put(DBConstants.KEY_COMPANY_COMP_CODE, code);
            cv.put(DBConstants.KEY_COMPANY_COMP_NAME, name);

            mdb.insert(DBConstants.KEY_RMS_COMPANY_TABLE, null, cv);

            mdb.setTransactionSuccessful();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }
    }


    private Boolean validationSuccess() {

        if (editTextCompCode.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(context, "Enter company code", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (editTextCompName.getText().toString().equalsIgnoreCase("")) {

            Toast.makeText(context, "Enter company name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (editTextAddress1.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(context, "Enter address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (spinnerStatusComp.getSelectedItemPosition() == 0) {
            Toast.makeText(context, "Status value should not be blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    public void clearData() {

        editTextCompCode.setText("");
        editTextCompName.setText("");
        editTextAddress1.setText("");
        editTextAddress2.setText("");
        editTextCompCity.setText("");
        editTextCompPin.setText("");
        edittTextPhone.setText("");
        textViewCompanyInfoDate.setText("");
        textViewLoyaltyEffDate.setText("");
        textViewValidity.setText("");
        spinnerStatusComp.setSelection(0);

        for (int i = 0; i < list.size(); i++) {
            View view = listViewDiscount.getChildAt(i);
            EditText textDiscount = (EditText) view.findViewById(R.id.e_t_discount);
            textDiscount.setText("");
        }
    }
}
