package com.entrada.cheekyMonkey.steward.bill;

/**
 * Created by csat on 18/06/2015.
 */
public interface ICallBackSavdBillResponse {

    void responseFromServer(String billNo, StringBuilder taxDetail);
}
