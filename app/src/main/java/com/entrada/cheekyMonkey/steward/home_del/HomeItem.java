package com.entrada.cheekyMonkey.steward.home_del;

import com.entrada.cheekyMonkey.takeorder.entity.OrderItem;

import java.util.ArrayList;


/**
 * Created by csat on 13/01/2016.
 */
public class HomeItem {

    public ArrayList<OrderItem> itemList = new ArrayList<>();

    public String guestId = "";
    public String guestName = "";
    public String delBoyId = "";
    public String delBoyName = "";
    public String name = "";

    public String date = "";
    public String Pos = "";
    public String billNo = "";
    public String delBoy = "";
    public String OdrTime = "";
    public String strTime = "";
    public String del_time = "";
    public String retTime = "";
    public String amt = "";
    public String changAmt = "";
    public String retAmt = "";
    public String address = "";
    public String city = "";
    public String phone = "";

    public String BILL_NO = "";
    public String DISCOUNT = "";
    public String SUBTOTAL = "";
    public String TAXES = "";
    public String TOTAL = "";
    public String TABLE = "";

    public String creditCode = "";
    public String creditType = "";
    public String creditDesc = "";

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getDelBoyId() {
        return delBoyId;
    }

    public void setDelBoyId(String delBoyId) {
        this.delBoyId = delBoyId;
    }

    public String getDelBoyName() {
        return delBoyName;
    }

    public void setDelBoyName(String delBoyName) {
        this.delBoyName = delBoyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDelBoy() {

        return delBoyName + "\n" + "(" + delBoyId + ")";
    }

    public String getGuest() {
        return guestName + "\n"  + guestId ;
    }


    public String getDate() {
        return date;
    }

    public String getPos() {
        return Pos;
    }

    public String getBillNo() {
        return billNo;
    }


    public String getOdrTime() {
        return OdrTime;
    }

    public String getStrTime() {
        return strTime;
    }

    public String getDel_time() {
        return del_time;
    }

    public String getRetTime() {
        return retTime;
    }

    public String getAmt() {
        return amt;
    }

    public String getChangAmt() {
        return changAmt;
    }

    public String getRetAmt() {
        return retAmt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {

        if (!address.isEmpty()) {

            String s[] = address.split(":");
            this.address = s[0];

            if (s.length == 2)
                this.city = s[1];
        }
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }

    public String getBILL_NO() {
        return BILL_NO;
    }

    public String getTOTAL() {
        return TOTAL;
    }

    public String getDISCOUNT() {
        return DISCOUNT;
    }

    public String getSUBTOTAL() {
        return SUBTOTAL;
    }

    public String getTAXES() {
        return TAXES;
    }

    public String getBillNoWithGuest() {

        return guestName.isEmpty() ? BILL_NO : BILL_NO + "\n" + "(" + guestName + ")";
    }


    public String getCreditCode() {
        return creditCode;
    }

    public String getCreditType() {
        return creditType;
    }

    public String getCreditDesc() {
        return creditDesc;
    }
}
