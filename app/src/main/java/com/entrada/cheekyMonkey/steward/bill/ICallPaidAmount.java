package com.entrada.cheekyMonkey.steward.bill;


import com.entrada.cheekyMonkey.steward.home_del.HomeItem;

/**
 * Created by csat on 21/01/2016.
 */
public interface ICallPaidAmount {

    void getCashAmount(String cash, String payMode);

    void getCreditAmount(HomeItem homeItem);
}
