package com.entrada.cheekyMonkey.steward.bill;

import android.content.Context;
import android.os.AsyncTask;

import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Created by csat on 22/07/2015.
 */
public class BillPrintReciptTask extends AsyncTask<String, Void, String> {


    byte[] setHT = {0x1b, 0x44, 0x18, 0x00};
    byte[] HT = {0x09};
    byte[] LF = {0x0d, 0x0a};
    UserInfo userInfo;
    private LinkedList<byte[]> printlist;
    private Context context;
    private String parameter;
    private String serverIP;
    private String response = "";
    private String formattedDate = "";
    private String time = "";
    private ArrayList<String> bill_format;


    public BillPrintReciptTask(Context context, String parameter, String serverIP,
                               LinkedList<byte[]> printlist, ArrayList<String> bill_format,
                               String formattedDate, String time) {

        this.context = context;
        this.parameter = parameter;
        this.serverIP = serverIP;
        this.printlist = printlist;
        this.bill_format = bill_format;
        this.formattedDate = formattedDate;
        this.time = time;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

     /*   if (mDialog != null)
        mDialog.setVisibility(View.VISIBLE);*/
    }

    @Override
    protected String doInBackground(String... params) {

        response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.KEY_BILL_PRINT, parameter);

        try {

            Logger.d("printBill", "::" + response);
            JSONObject jsonObject = new JSONObject(response);
            JSONObject jsonObject1 = jsonObject.getJSONObject("printBillResult");
            JSONArray jsonArray = jsonObject1.getJSONArray("Orddt");
            UserInfo.pos_type = "RESTAURANT";

            float subtotal = 0.0f, discount = 0.f;
            String disc = "", bill_remark = "";

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);

                if (i == 0) {

                    printlist.add(setHT);
                    printlist.add("POS".getBytes());
                    printlist.add(":".getBytes());
                    printlist.add(UserInfo.pos_type.getBytes());
                    printlist.add(LF);
                    printlist.add(setHT);
                    printlist.add("Bill No".getBytes());
                    printlist.add(":".getBytes());
                    printlist.add(UserInfo.Bill_no.getBytes());
                    printlist.add("    ".getBytes());
                    printlist.add("Time".getBytes());
                    printlist.add(":".getBytes());
                    printlist.add(time.getBytes());
                    printlist.add(LF);
                    printlist.add("Date".getBytes());
                    printlist.add(":".getBytes());
                    printlist.add(formattedDate.getBytes());
                    printlist.add("    ".getBytes());
                    printlist.add("Cover".getBytes());
                    printlist.add(":".getBytes());
                    printlist.add("5".getBytes());
                    printlist.add(LF);
                    printlist.add("Table".getBytes());
                    printlist.add(":".getBytes());
                    printlist.add("5".getBytes());
                    printlist.add("            ".getBytes());
                    printlist.add("Stw".getBytes());
                    printlist.add(":".getBytes());
                    printlist.add("a".getBytes());
                    printlist.add(LF);

                    printlist.add("_______________________________".getBytes());
                    printlist.add(LF);
                    printlist.add(setHT);
                    printlist.add("Item Name".getBytes());
                    printlist.add("    ".getBytes());
                    printlist.add("Qty".getBytes());
                    printlist.add("  ".getBytes());
                    printlist.add("Rate".getBytes());
                    printlist.add("   ".getBytes());
                    printlist.add("Amount".getBytes());
                    printlist.add(LF);
                    printlist.add("-------------------------------".getBytes());
                    printlist.add(LF);

                }


                subtotal = subtotal + Float.parseFloat(jsonObject.getString("Amt"));
                discount = subtotal * discount;
                printlist.add(setHT);
                printlist.add(jsonObject.getString("Itmname").getBytes());
                printlist.add(setHT);
                printlist.add(LF);
                printlist.add("             ".getBytes());
                printlist.add(jsonObject.getString("Qty").substring(0, jsonObject.getString("Qty").indexOf('.')).getBytes());
                printlist.add("   ".getBytes());
                printlist.add(Float.valueOf(jsonObject.getString("Rate")).toString().getBytes());
                printlist.add("    ".getBytes());
                printlist.add(Float.valueOf(jsonObject.getString("Amt")).toString().getBytes());
                discount = Float.parseFloat(jsonObject.getString("Disc"));
                disc = jsonObject.getString("Disc");
                printlist.add(LF);


            }

            JSONArray jsonArray1 = jsonObject1.getJSONArray("Taxdt");
            Float tax = 0.0f;
            bill_format = new ArrayList<>();
            for (int k = 0; k < jsonArray1.length(); k++) {
                jsonObject = jsonArray1.getJSONObject(k);
                if (k == 0) {


                    printlist.add(setHT);
                    printlist.add("-------------------------------".getBytes());
                    printlist.add(LF);
                    printlist.add("SUb Total ".getBytes());
                    printlist.add(HT);
                    printlist.add(Float.valueOf(subtotal).toString().getBytes());
                    printlist.add(LF);
                    printlist.add("Discount(".getBytes());
                    printlist.add(Integer.valueOf((int) discount).toString().getBytes());
                    printlist.add("%)".getBytes());
                    printlist.add(HT);
                    printlist.add(disc.getBytes());
                    printlist.add(LF);

                }

                tax = tax + Float.parseFloat(jsonObject.getString("Amt"));
                printlist.add(jsonObject.getString("TaxName").getBytes());
                printlist.add(HT);
                printlist.add(jsonObject.getString("Amt").getBytes());
                printlist.add(LF);
                bill_remark = jsonObject.getString("BillRem");


                if (k == jsonArray1.length() - 1) {
                    Float grossAmount = subtotal + discount + tax;
                    printlist.add(setHT);
                    printlist.add("_______________________________".getBytes());
                    printlist.add(LF);
                    printlist.add("Gross Amount ".getBytes());
                    printlist.add(HT);
                    printlist.add(Float.valueOf(grossAmount).toString().getBytes());
                    printlist.add(LF);
                    printlist.add("-------------------------------".getBytes());
                    printlist.add(LF);
                    bill_format.add(jsonObject.getString("BillFormat"));
                    printlist.add(LF);
                    // printlist.add(setHT);
                    // printlist.add(bill_remark.getBytes());


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

/*
@Override
protected void onProgressUpdate(String... values) {
        //super.onProgressUpdate(values);

        adapterOrderTable.clearDataSet();
        adapterOrderTable.addDataSetItem(values[0]);

        }*/

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
   /* if (mDialog!=null)
        mDialog.setVisibility(View.GONE);

       adapterOrderTable.clearDataSet();
        adapterOrderTable.notifyDataSetChanged();
*/

    }
}