package com.entrada.cheekyMonkey.steward.bill;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


/**
 * Created by csat on 18/06/2015.
 */
public class SaveBillTask extends AsyncTask<String, String, String> {

    ICallBackSavdBillResponse iCallBack;
    ProgressDialog progressDialog;
    private Context context;
    private String parameter, serverIP;
    private String billNo = "";
    private StringBuilder taxWithTaxName = new StringBuilder();

    public SaveBillTask(Context context, String parameter, String serverIP, ICallBackSavdBillResponse iCallBack) {
        this.context = context;
        this.parameter = parameter;
        this.serverIP = serverIP;
        this.iCallBack = iCallBack;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    protected String doInBackground(String... params) {

        Logger.i(serverIP, "::" + serverIP);
        Logger.i(parameter, "::" + parameter);
        String response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_BILL_SAVE1, parameter);

        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONObject jsonObject1  = jsonObject.getJSONObject("CABS_REST_BILL_GENERATE_SAVE1Result");
            billNo = jsonObject1.getString("Billno");
            JSONArray jsonArray = jsonObject1.getJSONArray("Txd");

            for (int i =0; i< jsonArray.length(); i++) {

                JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                taxWithTaxName.append(fixedLengthString(jsonObject2.getString("Taxname"), 30,0));
                taxWithTaxName.append(fixedLengthString(jsonObject2.getString("Taxval"), 10, 2));
                taxWithTaxName.append("\n");
            }


        }catch (JSONException e){e.printStackTrace();}

        return response;
    }


    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        iCallBack.responseFromServer(billNo, taxWithTaxName);
        if (progressDialog != null)
            progressDialog.dismiss();
    }


    public static String fixedLengthString(String string, int length, int decimalDigit) {

        // No Formatting
        if (decimalDigit == 0)
            return String.format("%-"+length+ "s", string);

        // Show one digit after decimal point (To display rate, amount)
        if (decimalDigit == 1){
            float value = Float.parseFloat(string);
            string = String.format(Locale.US,"%.1f", value);
            return String.format("%-"+length+ "s", string);
        }

        // Show two digit after decimal. (To display subtotal, discount,tax, total)
        if (decimalDigit == 2){

            float value = Float.parseFloat(string);
            string = String.format(Locale.US,"%.2f", value);
            return String.format("%-"+length+ "s", string);
        }

        return string;
    }

}

