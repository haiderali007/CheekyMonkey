package com.entrada.cheekyMonkey.takeorder.entity;

/**
 * Created by ${Tanuj.Sareen} on 07/03/2015.
 */
public class OrderItem {

    public String o_code = "";
    public String o_name = "";
    public String o_itm_rmrk = "";
    public String o_addon_code = "";
    public String o_addon_code_new = "";
    public String o_mod = "";
    public String o_combo_code = "";
    public String o_happy_hour = "";
    public String o_subunit = "";
    public String o_disc = "";
    public String o_grp_code = "";
    public String o_categ_code = "";
    public String o_sub_item = "";
    public String o_cover_item = "";
    public String o_meal_cors_item = "";
    public String o_meal_name = "";
    public String o_surcharge = "";
    public String multicheck = "";
    public String orderNo = "";

    public float o_quantity = 1, perItemFreeQty = 1, o_amount = 0, o_price = 0;

    public String getO_grp_code() {
        return o_grp_code;
    }

    public void setO_grp_code(String o_grp_code) {
        this.o_grp_code = o_grp_code;
    }

    public String getO_categ_code() {
        return o_categ_code;
    }

    public void setO_categ_code(String o_categ_code) {
        this.o_categ_code = o_categ_code;
    }

    public String getO_code() {
        return o_code;
    }

    public void setO_code(String o_code) {
        this.o_code = o_code;
    }

    public String getO_name() {
        return o_name;
    }

    public void setO_name(String o_name) {
        this.o_name = o_name;
    }

    public float getO_quantity() {
        return o_quantity;
    }

    public void setO_quantity(float o_quantity) {
        this.o_quantity = o_quantity;
    }

    public float getPerItemFreeQty() {
        return perItemFreeQty;
    }

    public void setPerItemFreeQty(float perItemFreeQty) {
        this.perItemFreeQty = perItemFreeQty;
    }

    public float getO_amount() {
        return o_amount;
    }

    public void setO_amount(float o_amount) {
        this.o_amount = o_amount;
    }

    public String getO_itm_rmrk() {
        return o_itm_rmrk;
    }

    public void setO_itm_rmrk(String o_itm_rmrk) {
        this.o_itm_rmrk = o_itm_rmrk;
    }

    public String getO_addon_code() {
        return o_addon_code;
    }

    public void setO_addon_code(String o_addon_code) {
        this.o_addon_code = o_addon_code;
    }

    public String getO_mod() {
        return o_mod;
    }

    public void setO_mod(String o_mod) {
        this.o_mod = o_mod;
    }

    public String getO_combo_code() {
        return o_combo_code;
    }

    public void setO_combo_code(String o_combo_code) {
        this.o_combo_code = o_combo_code;
    }

    public String getO_happy_hour() {
        return o_happy_hour;
    }

    public void setO_happy_hour(String o_happy_hour) {
        this.o_happy_hour = o_happy_hour;
    }

    public float getO_price() {
        return o_price;
    }

    public void setO_price(float o_price) {
        this.o_price = o_price;
    }

    public String getO_subunit() {
        return o_subunit;
    }

    public void setO_subunit(String o_subunit) {
        this.o_subunit = o_subunit;
    }

    public String getO_disc() {
        return o_disc;
    }

    public void setO_disc(String o_disc) {
        this.o_disc = o_disc;
    }

    public String getO_sub_item() {
        return o_sub_item;
    }

    public void setO_sub_item(String o_sub_item) {
        this.o_sub_item = o_sub_item;
    }

    public String getO_cover_item() {
        return o_cover_item;
    }

    public void setO_cover_item(String o_cover_item) {
        this.o_cover_item = o_cover_item;
    }

    public String getO_meal_cors_item() {
        return o_meal_cors_item;
    }

    public void setO_meal_cors_item(String o_meal_cors_item) {
        this.o_meal_cors_item = o_meal_cors_item;
    }

    public String getO_meal_name() {
        return o_meal_name;
    }

    public void setO_meal_name(String o_meal_name) {
        this.o_meal_name = o_meal_name;
    }

    public String getO_surcharge() {
        return o_surcharge;
    }

    public void setO_surcharge(String o_surcharge) {
        this.o_surcharge = o_surcharge;
    }

    public String getO_addon_code_new() {
        return o_addon_code_new;
    }

    public void setO_addon_code_new(String o_addon_code) {
        this.o_addon_code_new = o_addon_code;
    }

    public String getMulticheck() {
        return multicheck;
    }

    public void setMulticheck(String multicheck) {
        this.multicheck = multicheck;
    }

    public String getOrderNo() {
        return orderNo;
    }
}
