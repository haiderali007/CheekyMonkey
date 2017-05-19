package com.entrada.cheekyMonkey.steward.billEdit;


import com.entrada.cheekyMonkey.steward.home_del.HomeItem;

/**
 * Created by csat on 19/01/2016.
 */
public interface ICallBillDetail {

    void onClickBillNoToSettle(HomeItem homeItem);
    void onClickBillNoToCancel(HomeItem homeItem);
    void onClickBillNoToModify(HomeItem homeItem);
    void onClickBillNoToUnSettle(HomeItem homeItem);
    void onClickHoldOrderNo(HomeItem homeItem, int index);
}