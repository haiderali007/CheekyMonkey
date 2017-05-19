package com.entrada.cheekyMonkey.steward.bill;

/**
 * Created by csat on 15/06/2015.
 */
public class DiscountItem {

    String code = "", name = "", flag = "", OPN = "", OGI = "", FL = "", Flat_flag = "";

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getOPN() {
        return OPN;
    }

    public void setOPN(String OPN) {
        this.OPN = OPN;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOGI() {
        return OGI;
    }

    public void setOGI(String OGI) {
        this.OGI = OGI;
    }

    public String getFlat_flag() {
        return Flat_flag;
    }

    public void setFlat_flag(String Flat_flag) {
        this.Flat_flag = Flat_flag;
    }

    public String getFL() {
        return FL;
    }

    public void setFL(String FL) {
        this.FL = FL;
    }
}