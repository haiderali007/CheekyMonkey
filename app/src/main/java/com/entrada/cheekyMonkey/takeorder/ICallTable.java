package com.entrada.cheekyMonkey.takeorder;

import com.entrada.cheekyMonkey.takeorder.entity.TableItem;

/**
 * Created by ${Tanuj.Sareen} on 07/03/2015.
 */
public interface ICallTable {

    void TableItem(TableItem tableItem);
    void responseGuestDiscount(String Phone, String Name);
}
