package com.entrada.cheekyMonkey.steward;

public class AddonItems {

    public String addonID = "", addonName = "", addonItemCode = "";
    public boolean isSelected = false;
    public float addonPrice;
    public int addonQty = 0;

    public AddonItems(String addonID, String addonName, String addonItemCode,
                      float addonPrice, boolean isSelected) {
        this.addonID = addonID;
        this.addonName = addonName;
        this.addonItemCode = addonItemCode;
        this.addonPrice = addonPrice;
        this.isSelected = isSelected;
    }

}
