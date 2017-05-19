package com.entrada.cheekyMonkey.steward.bill;

/**
 * Created by csat on 23/07/2015.
 */
public class BillPrintItem {


    public String disc = "";
    public String item_name = "";
    public String item_qty = "";
    public String TaxName = "";
    public String Amount = "";
    public String BillFormat = "";
    public float o_amount;
    public float o_price;
    public float o_quantity;

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }


    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }


    public String getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(String item_qty) {
        this.item_qty = item_qty;
    }


    public String getTaxName() {
        return TaxName;
    }

    public void setTaxName(String taxName) {
        TaxName = taxName;
    }


    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }


    public String getBillFormat() {
        return BillFormat;
    }

    public void setBillFormat(String billFormat) {
        BillFormat = billFormat;
    }


    public float getO_quantity() {
        return o_quantity;
    }

    public void setO_quantity(float o_quantity) {
        this.o_quantity = o_quantity;
    }


    public float getO_amount() {
        return o_amount;
    }

    public void setO_amount(float o_amount) {
        this.o_amount = o_amount;
    }


    public float getO_price() {
        return o_price;
    }

    public void setO_price(float o_price) {
        this.o_price = o_price;
    }


}
