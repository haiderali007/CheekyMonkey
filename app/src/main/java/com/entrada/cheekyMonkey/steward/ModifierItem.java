package com.entrada.cheekyMonkey.steward;

/**
 * Created by csat on 09/05/2015.
 */
public class ModifierItem {

    public String modiID = "", modiName = "", modiItemCode = "";
    public boolean isSelected = false;

    public ModifierItem(String modiID, String modiName, String modiItemCode) {
        this.modiID = modiID;
        this.modiName = modiName;
        this.modiItemCode = modiItemCode;
    }

}
