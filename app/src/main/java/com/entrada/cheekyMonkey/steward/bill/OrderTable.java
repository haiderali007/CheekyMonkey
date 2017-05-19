package com.entrada.cheekyMonkey.steward.bill;

/**
 * Created by csat on 15/06/2015.
 */
public class OrderTable {


    public String order_no = "";
    public String item_name = "";
    public String item_qty = "";
    public String item_remark = "", cover = "", steward = "", guest = "", guest_code = "";
    public String item_price = "";

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getName() {
        return item_name;
    }

    public void setName(String item_name) {
        this.item_name = item_name;
    }

    public String getqty() {
        return item_qty;
    }

    public void setqty(String item_qty2) {
        this.item_qty = item_qty2;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getItem_remark() {
        return item_remark;
    }

    public void setItem_remark(String item_remark) {
        this.item_remark = item_remark;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSteward() {
        return steward;
    }

    public void setSteward(String steward) {
        this.steward = steward;
    }

}


