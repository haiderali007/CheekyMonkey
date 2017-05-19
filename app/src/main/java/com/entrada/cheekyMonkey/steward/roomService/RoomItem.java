package com.entrada.cheekyMonkey.steward.roomService;

/**
 * Created by CSATSPL on 22/12/2015.
 */
public class RoomItem {

    public String GMember_Id = "";
    public String Room_code = "";
    public String Room_name = "";
    public String GuestId = "";
    public String GuestName = "";
    public String RMSC_Row = "";
    public String RMSC_Column = "";
    public String Status = "";


    public String getGMember_Id() {
        return GMember_Id;
    }

    public void setGMember_Id(String GMember_Id) {
        this.GMember_Id = GMember_Id;
    }

    public String getRoom_code() {
        return Room_code;
    }

    public void setRoom_code(String room_code) {
        Room_code = room_code;
    }

    public String getRoom_name() {
        return Room_name;
    }

    public void setRoom_name(String room_name) {
        Room_name = room_name;
    }

    public String getGuestId() {
        return GuestId;
    }

    public void setGuestId(String guestId) {
        GuestId = guestId;
    }

    public String getGuestName() {
        return GuestName;
    }

    public void setGuestName(String guestName) {
        GuestName = guestName;
    }

    public String getRMSC_Row() {
        return RMSC_Row;
    }

    public void setRMSC_Row(String RMSC_Row) {
        this.RMSC_Row = RMSC_Row;
    }

    public String getRMSC_Column() {
        return RMSC_Column;
    }

    public void setRMSC_Column(String RMSC_Column) {
        this.RMSC_Column = RMSC_Column;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

}
