package com.entrada.cheekyMonkey.steward.order;

/**
 * Created by ${Tanuj.Sareen} on 14/03/2015.
 */
public interface ICallBackOrderNumber {

    void modifyOrder(String orderNumber);

    void cancelOrder(String OrderNumber);
}
