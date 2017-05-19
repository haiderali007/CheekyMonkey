package com.entrada.cheekyMonkey.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class POSItem {

    public String posCode = "REST";
    public String posName = "RESTAURANT";
    public String posType = "R";
    public boolean isSelected = false;

    public POSItem() {
    }

    public POSItem(String posCode, String posName, String posType) {
        this.posCode = posCode;
        this.posName = posName;
        this.posType = posType;
    }

    public POSItem(JSONObject jsonObject) throws JSONException {

        this.posCode = jsonObject.getString("Code");
        this.posName = jsonObject.getString("Name");
        this.posType = jsonObject.getString("Pos_type");
    }

}
