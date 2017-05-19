package com.entrada.cheekyMonkey.steward.roomService;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.entrada.cheekyMonkey.R;


/**
 * Created by CSATSPL on 22/12/2015.
 */
public class RoomNumberLayout implements AdapterView.OnItemClickListener {

    View view = null;
    LayoutInflater layoutInflater;
    ProgressBar progressBarRommService;
    GridView grdViewRoomService;
    Context context;
    int width, heightv;
    ICallBackRoom iCallBackRoom;
    private RoomServiceAdapter roomServiceAdapter;

    public RoomNumberLayout(Context context, int width, ICallBackRoom iCallBackRoom) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.width = width;
        this.iCallBackRoom = iCallBackRoom;
    }

    public View getRoomNumberPopupWindow(String parameter, String serverIP) {

        view = layoutInflater.inflate(R.layout.room_number_popup_layout, null);
        grdViewRoomService = (GridView) view.findViewById(R.id.grdViewRoomService);
        progressBarRommService = (ProgressBar) view.findViewById(R.id.progressBarRomm);

        roomServiceAdapter = new RoomServiceAdapter(context);
        grdViewRoomService.setAdapter(roomServiceAdapter);
        grdViewRoomService.setOnItemClickListener(this);

        new RoomNumberTask(context, roomServiceAdapter, parameter, serverIP, progressBarRommService).execute();

        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        iCallBackRoom.RoomItem((RoomItem) roomServiceAdapter.getItem(position));

        RoomItem roomitem = (RoomItem) roomServiceAdapter.getItem(position);
        if (roomitem != null && roomitem.getStatus().equals("V"))
            Show_RoomServicePopup();
        else //if (roomitem != null && roomitem.getStatus().equals("R"))
            GuestRoomServicePopup(roomitem);

    }

    public void notifyDataSetChanged() {
        roomServiceAdapter.notifyDataSetChanged();
    }


    public void Show_RoomServicePopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View v = LayoutInflater.from(context).inflate(
                R.layout.show_guest_room_popup, null);

        final TextView textViewWifiOk1, textViewConnectionOk2;
        textViewWifiOk1 = (TextView) v.findViewById(R.id.textViewRoomOk);

        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        textViewWifiOk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);

                dialog.dismiss();
            }
        });

    }

    public void GuestRoomServicePopup(RoomItem roomitem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View v = LayoutInflater.from(context).inflate(
                R.layout.guest_running_room_status, null);

        final TextView textviewAct, textviewGst, textviewArv, textviewDpt,
                textviewComp, textviewBilling, textviewPax,
                textViewWifiOk1, textViewConnectionOk2;

        textviewAct = (TextView) v.findViewById(R.id.textView21);
        textviewGst = (TextView) v.findViewById(R.id.textView23);
        textviewArv = (TextView) v.findViewById(R.id.textView26);
        textviewDpt = (TextView) v.findViewById(R.id.textView28);
        textviewComp = (TextView) v.findViewById(R.id.textView30);
        textviewBilling = (TextView) v.findViewById(R.id.textView32);
        textviewPax = (TextView) v.findViewById(R.id.textView35);
        textViewWifiOk1 = (TextView) v.findViewById(R.id.textViewRoomOk);


        textviewAct.setText(roomitem.getGuestId());
        textviewGst.setText(roomitem.getGuestName());
/*      textviewArv.setText(roomitem);
        textviewDpt.setText(roomitem);
        textviewComp.setText(roomitem);
        textviewBilling.setText(roomitem);
        textviewPax.setText(roomitem);*/


        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        textViewWifiOk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);

                dialog.dismiss();
            }
        });

    }
}
