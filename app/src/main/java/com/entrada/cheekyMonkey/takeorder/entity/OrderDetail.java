package com.entrada.cheekyMonkey.takeorder.entity;

import com.entrada.cheekyMonkey.steward.home_del.HomeItem;
import com.entrada.cheekyMonkey.entity.POSItem;
import com.entrada.cheekyMonkey.steward.roomService.RoomItem;

public class OrderDetail {

    private TableItem tableItem = new TableItem();
    private RoomItem roomItem = new RoomItem();
    private HomeItem homeItem = new HomeItem();
    private POSItem posItem = new POSItem();

    public RoomItem getRoomItem() {
        return roomItem;
    }

    public void setRoomItem(RoomItem roomItem) {
        this.roomItem = roomItem;
    }

    public HomeItem getHomeItem() {
        return homeItem;
    }

    public void setHomeItem(HomeItem homeItem) {
        this.homeItem = homeItem;
    }

    public TableItem getTableItem() {
        return tableItem;
    }

    public void setTableItem(TableItem tableItem) {
        this.tableItem = tableItem;
    }


    public POSItem getPosItem() {
        return posItem;
    }

    public void setPosItem(POSItem posItem) {
        this.posItem = posItem;
    }


}

