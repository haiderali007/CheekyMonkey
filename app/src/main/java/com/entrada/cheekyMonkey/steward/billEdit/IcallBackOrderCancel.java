package com.entrada.cheekyMonkey.steward.billEdit;

/**
 * Created by ${Tanuj.Sareen} on 14/03/2015.
 */
public interface IcallBackOrderCancel {

    void OrderCancelResponse(String response);

    void TableCancelResponse(String response);

    void BillCancelResponse(String response);

    void undoBillSettleResponse(String response);
}
