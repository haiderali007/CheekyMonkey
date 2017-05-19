package com.entrada.cheekyMonkey.steward.discount;

/**
 * Created by Dev on 28/08/2015.
 */
public interface ICallDiscount {

    void createDiscList(String disc_per, String grp_code, float price);

    void updateDiscAmount(int position, float price);

    void removeItemDiscList(int position);
}
