package com.entrada.cheekyMonkey.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Tanuj.Sareen on 1/30/2015.
 */
public class Items implements Parcelable {


    protected GroupItems groupItems;
    protected CategoryItem categoryItem;
    protected MenuItem menuItem;
    protected ArrayList<MenuItem> menuItemList;

    public static final Creator<Items> CREATOR = new Creator<Items>() {
        public Items createFromParcel(Parcel source) {
            Items items = new Items();
            items.groupItems = source.readParcelable(GroupItems.class.getClassLoader());
            items.categoryItem = source.readParcelable(CategoryItem.class.getClassLoader());
            items.menuItem = source.readParcelable(MenuItem.class.getClassLoader());
            return items;
        }

        public Items[] newArray(int size) {
            return new Items[size];
        }
    };

    public Items() {
    }


    public Items(GroupItems groupItems, CategoryItem categoryItem, MenuItem menuItem) {
        this.groupItems = groupItems;
        this.categoryItem = categoryItem;
        this.menuItem = menuItem;
    }

    public Items(GroupItems groupItems, CategoryItem categoryItem, ArrayList<MenuItem> menuItem) {
        this.groupItems = groupItems;
        this.categoryItem = categoryItem;
        this.menuItemList = menuItem;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public GroupItems getGroupItems() {
        return groupItems;
    }

    public void setGroupItems(GroupItems groupItems) {
        this.groupItems = groupItems;
    }

    public CategoryItem getCategoryItem() {
        return categoryItem;
    }

    public void setCategoryItem(CategoryItem categoryItem) {
        this.categoryItem = categoryItem;
    }

    public ArrayList<MenuItem> getMenuItemList() {
        return menuItemList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(groupItems, flags);
        dest.writeParcelable(categoryItem, flags);
        dest.writeParcelable(menuItem, flags);
    }


}
