package com.entrada.cheekyMonkey.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.StrictMode;

public class MenuItem implements Parcelable {

    public String menu_code = "";
    public String menu_name = "";
    public String menu_sub_unit = "";
    public String menu_combo = "";
    public String menu_addon = "";
    public String menu_mod = "";
    public String menu_group_code = "";
    public String menu_categ_code = "";
    public String menu_item_url = "";
    public String menu_sub_item = "";
    public String menu_open_item = "";
    public String menu_color = "";
    public String menu_dynamic = "";
    public String menu_veg = "";
    public String menu_calories = "";

    public String Current_Rate = "";
    public String Inc_Qty  = "";
    public String Item_Desc  = "";
    public String Previous_Rate  = "";
    public String Sold_Qty  = "";
    public String Today_Max   = "";
    public String Cat_Code  = "";
    public String Cat_Desc  = "";
    public String Item_Code  = "";


    public float menu_price, Max_Price, Min_Price, Inc_Rate, menu_open_item_flag, quantity = 1, menu_amount ;

    public static final Creator<MenuItem> CREATOR = new Creator<MenuItem>() {
        public MenuItem createFromParcel(Parcel source) {
            MenuItem menuItem = new MenuItem();
            menuItem.menu_code = source.readString();
            menuItem.menu_name = source.readString();
            menuItem.menu_sub_unit = source.readString();
            menuItem.menu_combo = source.readString();
            menuItem.menu_addon = source.readString();
            menuItem.menu_mod = source.readString();
            menuItem.menu_group_code = source.readString();
            menuItem.menu_categ_code = source.readString();
            menuItem.menu_item_url = source.readString();
            menuItem.menu_sub_item = source.readString();
            menuItem.menu_open_item = source.readString();
            menuItem.menu_price = source.readFloat();
            menuItem.menu_open_item_flag = source.readFloat();
            menuItem.quantity = source.readFloat();
            menuItem.menu_amount = source.readFloat();
            menuItem.menu_dynamic = source.readString();
            menuItem.menu_veg = source.readString();
            menuItem.menu_calories = source.readString();

            return menuItem;
        }

        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };

    public String getMenu_color() {
        return menu_color;
    }

    public void setMenu_color(String menu_color) {
        this.menu_color = menu_color;
    }

    public String getMenu_code() {
        return menu_code;
    }

    public void setMenu_code(String menu_code) {
        this.menu_code = menu_code;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_sub_unit() {
        return menu_sub_unit;
    }

    public void setMenu_sub_unit(String menu_sub_unit) {
        this.menu_sub_unit = menu_sub_unit;
    }

    public String getMenu_combo() {
        return menu_combo;
    }

    public void setMenu_combo(String menu_combo) {
        this.menu_combo = menu_combo;
    }

    public String getMenu_addon() {
        return menu_addon;
    }

    public void setMenu_addon(String menu_addon) {
        this.menu_addon = menu_addon;
    }

    public String getMenu_mod() {
        return menu_mod;
    }

    public void setMenu_mod(String menu_mod) {
        this.menu_mod = menu_mod;
    }

    public String getMenu_group_code() {
        return menu_group_code;
    }

    public void setMenu_group_code(String menu_group_code) {
        this.menu_group_code = menu_group_code;
    }

    public String getMenu_categ_code() {
        return menu_categ_code;
    }

    public void setMenu_categ_code(String menu_categ_code) {
        this.menu_categ_code = menu_categ_code;
    }

    public String getMenu_item_url() {
        return menu_item_url;
    }

    public void setMenu_item_url(String menu_item_url) {
        this.menu_item_url = menu_item_url;
    }

    public String getMenu_sub_item() {
        return menu_sub_item;
    }

    public void setMenu_sub_item(String menu_sub_item) {
        this.menu_sub_item = menu_sub_item;
    }

    public String getMenu_open_item() {
        return menu_open_item;
    }

    public void setMenu_open_item(String menu_open_item) {
        this.menu_open_item = menu_open_item;
    }

    public float getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(float menu_price) {
        this.menu_price = menu_price;
    }

    public float getMenu_open_item_flag() {
        return menu_open_item_flag;
    }

    public void setMenu_open_item_flag(float menu_open_item_flag) {
        this.menu_open_item_flag = menu_open_item_flag;
    }


    public float getMax_Price() {
        return Max_Price;
    }

    public void setMax_Price(float max_Price) {
        Max_Price = max_Price;
    }

    public float getMin_Price() {
        return Min_Price;
    }

    public void setMin_Price(float min_Price) {
        Min_Price = min_Price;
    }

    public float getInc_Rate() {
        return Inc_Rate;
    }

    public void setInc_Rate(float inc_Rate) {
        Inc_Rate = inc_Rate;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getMenuAmount() {
        return menu_amount;
    }

    public void setMenuAmount(float amount) {
        this.menu_amount = amount;
    }


    public String getMenu_veg() {
        return menu_veg;
    }


    public String getMenu_dynamic() {
        return menu_dynamic;
    }

    public void setMenu_dynamic(String menu_dynamic) {
        this.menu_dynamic = menu_dynamic;
    }


    public void setMenu_veg(String menu_veg) {
        this.menu_veg = menu_veg;
    }


    public String getMenu_calories() {
        return menu_calories;
    }

    public void setMenu_calories(String menu_calories) {
        this.menu_calories = menu_calories;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(menu_code);
        dest.writeString(menu_name);
        dest.writeString(menu_sub_unit);
        dest.writeString(menu_combo);
        dest.writeString(menu_addon);
        dest.writeString(menu_mod);
        dest.writeString(menu_group_code);
        dest.writeString(menu_categ_code);
        dest.writeString(menu_item_url);
        dest.writeString(menu_sub_item);
        dest.writeString(menu_open_item);
        dest.writeFloat(menu_price);
        dest.writeFloat(menu_open_item_flag);
        dest.writeFloat(Max_Price);
        dest.writeFloat(Min_Price);
        dest.writeFloat(Inc_Rate);
        dest.writeFloat(quantity);
        dest.writeFloat(menu_amount);
        dest.writeString(menu_dynamic);
        dest.writeString(menu_veg);
        dest.writeString(menu_calories);
    }
}
