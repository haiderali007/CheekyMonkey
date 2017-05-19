package com.entrada.cheekyMonkey.entity;

import android.content.Context;

/**
 * Created by Tanuj.Sareen on 29/12/2014.
 */
public class DataModelImpl implements DataModel {

    String appType = "";
    String IpAddress = "";
    private Context context;
    private UserInfo userInfo;


    public DataModelImpl(Context context) {
        this.context = context;
        userInfo = new UserInfo();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public UserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }



/*
    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getIpAddress() {
        return IpAddress;
    }

    public void setIpAddress(String ipAddress) {
        IpAddress = ipAddress;
    }*/

}
