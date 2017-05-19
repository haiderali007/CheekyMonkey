package com.entrada.cheekyMonkey.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tanuj.Sareen on 1/30/2015.
 */
public class GroupItems implements Parcelable {

    public String group_Code = "";
    public String group_name = "";
    public String group_color = "";

    public static final Creator<GroupItems> CREATOR = new Creator<GroupItems>() {
        public GroupItems createFromParcel(Parcel source) {
            GroupItems groupItems = new GroupItems();
            groupItems.group_Code = source.readString();
            groupItems.group_color = source.readString();
            groupItems.group_name = source.readString();
            return groupItems;
        }

        public GroupItems[] newArray(int size) {
            return new GroupItems[size];
        }
    };
    public String discount = "";


    public GroupItems() {
    }


    public GroupItems(String group_code, String group_name, String group_color) {
        this.setGroup_Code(group_code);
        this.setGroup_name(group_name);
        this.setGroup_color(group_color);
    }

    public String getGroup_Code() {
        return group_Code;
    }

    public void setGroup_Code(String group_Code) {
        this.group_Code = group_Code;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_color() {
        return group_color;
    }

    public void setGroup_color(String group_color) {
        this.group_color = group_color;
    }

    public String getDiscount() {
        return discount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(group_Code);
        dest.writeString(group_name);
        dest.writeString(group_color);

    }
}
