package com.entrada.cheekyMonkey.steward.bill;

public interface ICallBackPOS {

    void setMaxProgressValue(String keyword_URL, int maxProgress);

    void showPOSValue(String keyword_URL, int progress);

    void completeSync(String keyword_URL, boolean isSuccess);

}
