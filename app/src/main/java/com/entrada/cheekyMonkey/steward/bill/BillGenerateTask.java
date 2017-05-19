package com.entrada.cheekyMonkey.steward.bill;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by csat on 18/06/2015.
 */
public class BillGenerateTask extends AsyncTask<String, OrderTable, OrderTable> {

    ICallBillGenerate iCallBillGenerate;
    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;
    private String response = "";
    private String subtotal = "";
    private String discount = "";
    private String taxes = "";
    private String total = "";
    private String bill_no = "";
    private CustomTextview textViewBillNo, textViewTax, textViewDiscount, textViewSubTotal, textViewTotal, textViewBillRemark;
    private LinearLayout LinerLayoutBillDetails;


    public BillGenerateTask(Context context, String parameter,
                            String serverIP, ProgressBar mDialog, CustomTextview textViewBillNo, CustomTextview textViewTax,
                            CustomTextview textViewDiscount, CustomTextview textViewSubTotal, CustomTextview textViewTotal,
                            LinearLayout LinerLayoutBillDetails, CustomTextview textViewBillRemark,
                            ICallBillGenerate iCallBillGenerate) {

        this.context = context;
        this.mDialog = mDialog;
        this.parameter = parameter;
        this.serverIP = serverIP;
        this.LinerLayoutBillDetails = LinerLayoutBillDetails;
        this.textViewBillNo = textViewBillNo;
        this.textViewTax = textViewTax;
        this.textViewDiscount = textViewDiscount;
        this.textViewSubTotal = textViewSubTotal;
        this.textViewTotal = textViewTotal;
        this.textViewBillRemark = textViewBillRemark;
        this.iCallBillGenerate = iCallBillGenerate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mDialog != null)
            mDialog.setVisibility(View.VISIBLE);
    }

    @Override
    protected OrderTable doInBackground(String... params) {

        response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_BILL_GENERATE, parameter);

        try {

            Logger.d("CABS_REST_BILL_GENERATE1Result", "::" + response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("CABS_REST_BILL_GENERATE1Result"));

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);


                if (!(jsonObject.getString("SUBTOTAL").equals("EX")
                        || jsonObject.getString("SUBTOTAL").equals("SQL") || jsonObject
                        .getString("SUBTOTAL").equals("token"))) {


                    discount = jsonObject.getString("DISCOUNT");
                    subtotal = jsonObject.getString("SUBTOTAL");
                    taxes = jsonObject.getString("TAXES");
                    total = jsonObject.getString("TOTAL");
                    bill_no = jsonObject.getString("BILL_NO");
                    Log.i("subtotal", subtotal);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(OrderTable result) {
        super.onPostExecute(result);

        if (mDialog != null)
            mDialog.setVisibility(View.GONE);

        LinerLayoutBillDetails.setVisibility(View.VISIBLE);
        textViewBillRemark.setVisibility(View.VISIBLE);

        if (!bill_no.equals("null"))
            textViewBillNo.setText(bill_no);
        textViewTax.setText(taxes);
        textViewDiscount.setText(discount);
        textViewSubTotal.setText(subtotal);
        textViewTotal.setText(total);

        iCallBillGenerate.getBillGenerateResponse(bill_no);
    }
}