package com.entrada.cheekyMonkey.takeorder.entity;

/**
 * Created by ${Tanuj.Sareen} on 07/03/2015.
 */
public class TableItem {

    public String code = "", name = "", status = "", cvr = "", stwrd = "",
            gst_name = "", gst_code = "", odr_type = "";




    public int no_pax = 0;

    public double tbl_font = 8, tbl_w, tbl_h;

    public String getCvr() {
        return cvr;
    }

    public void setCvr(String cvr) {
        this.cvr = cvr;
    }

    public String getStwrd() {
        return stwrd;
    }

    public void setStwrd(String stwrd) {
        this.stwrd = stwrd;
    }

    public String getGst_name() {
        return gst_name;
    }

    public void setGst_name(String gst_name) {
        this.gst_name = gst_name;
    }

    public String getGst_code() {
        return gst_code;
    }

    public void setGst_code(String gst_code) {
        this.gst_code = gst_code;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTbl_font() {
        return tbl_font;
    }

    public void setTbl_font(double tbl_font) {
        this.tbl_font = tbl_font;
    }

    public double getTbl_w() {
        return tbl_w;
    }

    public void setTbl_w(double tbl_w) {
        this.tbl_w = tbl_w;
    }

    public double getTbl_h() {
        return tbl_h;
    }

    public void setTbl_h(double tbl_h) {
        this.tbl_h = tbl_h;
    }

    public int getNo_pax() {
        return no_pax;
    }

    public void setNo_pax(int no_pax) {
        this.no_pax = no_pax;
    }

}
