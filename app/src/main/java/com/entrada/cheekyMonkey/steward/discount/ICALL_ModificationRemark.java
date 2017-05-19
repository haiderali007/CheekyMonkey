package com.entrada.cheekyMonkey.steward.discount;

/**
 * Created by ${Tanuj.Sareen} on 14/03/2015.
 */
public interface ICALL_ModificationRemark {

    void getOrderEditReason(String reason, boolean isModify);

    void getBillEditReason(String reason, boolean isModify);

    void getUndoSettleReason(String reason);
}
