package com.entrada.cheekyMonkey.steward.billEdit;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.steward.StewardOrderFragment;
import com.entrada.cheekyMonkey.steward.home_del.HomeItem;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.ui.CustomTextview;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;


public class BillEditLayout implements OnItemClickListener, View.OnClickListener {

    public GridView gridViewPendingBills;
    public PendingBillsAdapter billsAdapter;
    public CustomTextview textviewHeader, textviewForBIllno, textviewForDiscount, textviewForSubtotal,
            textviewForTax, textviewForTotal, tv_cancel_type, tv_comp, tv_full_disc, tv_cancel;
    public String cancelFlag = "C";
    View tableView = null;
    LayoutInflater layoutInflater;
    Context context;
    String flag = "";
    ICallBillDetail iCallBillDetail;
    LinearLayout layoutCancel;
    boolean rest;
    int index = 0;

    public BillEditLayout(Context context, ICallBillDetail iCallBillDetail) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.iCallBillDetail = iCallBillDetail;
    }

    public View getPendingBillsPopupWindow(String flag, String table) {

        tableView = layoutInflater.inflate(R.layout.bill_edit_layout, null);
        this.flag = flag;

        gridViewPendingBills = (GridView) tableView.findViewById(R.id.gv_bill_cancel);
        billsAdapter = new PendingBillsAdapter(context);
        gridViewPendingBills.setAdapter(billsAdapter);
        gridViewPendingBills.setOnItemClickListener(this);

        textviewHeader = (CustomTextview) tableView.findViewById(R.id.txtViewBillEdit);
        textviewForBIllno = (CustomTextview) tableView.findViewById(R.id.tview_BillNo);
        textviewForDiscount = (CustomTextview) tableView.findViewById(R.id.tview_discount);
        textviewForSubtotal = (CustomTextview) tableView.findViewById(R.id.tview_Subtotal);
        textviewForTax = (CustomTextview) tableView.findViewById(R.id.tview_Tax);
        textviewForTotal = (CustomTextview) tableView.findViewById(R.id.tview_Total);

        layoutCancel = (LinearLayout) tableView.findViewById(R.id.ll_bill_cancel_options);
        tv_cancel_type = (CustomTextview) tableView.findViewById(R.id.tv_cancel_type);
        tv_comp = (CustomTextview) tableView.findViewById(R.id.tv_comp);
        tv_full_disc = (CustomTextview) tableView.findViewById(R.id.tv_full_discount);
        tv_cancel = (CustomTextview) tableView.findViewById(R.id.tv_bill_cancel);

        tv_comp.setOnClickListener(this);
        tv_full_disc.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

        String posType = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posType;
        rest = posType.equals(StaticConstants.KEY_TAG_POS_TYPE_R);

        init(table);
        return tableView;
    }

    public void init(String table) {

        // B- Show UnPaid bills, C- Bill Cancel, M- Bill Modify, U- Undo Bill Settle, P- Show Paid bills

        String billType = "A";

        if (iCallBillDetail instanceof StewardOrderFragment) {

            StewardOrderFragment takeOrderFragment = (StewardOrderFragment) iCallBillDetail;
            Resources resources = takeOrderFragment.getResources();

            switch (flag) {

                case "C":
                    textviewHeader.setText(resources.getString(R.string.bill_cancel));
                    tv_cancel_type.setVisibility(View.VISIBLE);
                    break;

                case "M":
                    textviewHeader.setText(resources.getString(R.string.bill_modi));

                    break;

                case "U":
                    textviewHeader.setText(resources.getString(R.string.undo_stl));
                    billType = "P";
                    break;

                case "L":
                    textviewHeader.setText(resources.getString(R.string.direct_stl));
                    billType = "L";
            }
        }

        String pos = POSApplication.getSingleton().getmDataModel().getUserInfo().getPosItem().posCode;
        String parameter = UtilToCreateJSON.createPosJSON(pos, table, billType); // billtype - paid / Unpaid
        String serverIP = POSApplication.getSingleton().getmDataModel().getUserInfo().getServerIP();
        new FetchBillDetailTask(context, billsAdapter, parameter, serverIP).execute();
    }


    @Override
    public void onClick(View v) {

        String cancelType = ((CustomTextview) v).getText().toString();
        tv_cancel_type.setText(cancelType);

        cancelFlag = cancelType.equalsIgnoreCase("Complimentary") ? "CO" :
                cancelType.equalsIgnoreCase("Full Discount") ? "F" : "C";

        if (cancelFlag.equals("CO")) {

            tv_cancel_type.setBackgroundResource(R.color.background_tab_pressed);
        } else if (cancelFlag.equals("F")) {
            tv_cancel_type.setBackgroundResource(R.color.PeachPuff);

        } else if (cancelFlag.equals("C")) {
            tv_cancel_type.setBackgroundResource(R.color.DarkGray);

        }
//        tv_cancel_type.setBackgroundResource(cancelFlag.equals("CO") ? R.color.home_color :
//                cancelType.equals("F") ? R.color.Red : R.color.Green);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        HomeItem homeItem = (HomeItem) billsAdapter.getItem(position);

        if (flag.equals("C"))
            iCallBillDetail.onClickBillNoToCancel(homeItem);

        else if (flag.equals("M"))
            iCallBillDetail.onClickBillNoToModify(homeItem);
        else if (flag.equals("U"))
            iCallBillDetail.onClickBillNoToUnSettle(homeItem);

        else if (flag.equals("L")) {
            iCallBillDetail.onClickBillNoToSettle(homeItem);
            index = position;
        }

        textviewForBIllno.setText(homeItem.getBILL_NO());
        textviewForDiscount.setText(homeItem.getDISCOUNT().substring(0, homeItem.getDISCOUNT().indexOf('.') + 3));
        textviewForSubtotal.setText(homeItem.getSUBTOTAL().substring(0, homeItem.getSUBTOTAL().indexOf('.') + 3));
        textviewForTax.setText(homeItem.getTAXES().substring(0, homeItem.getTAXES().indexOf('.') + 3));
        textviewForTotal.setText(homeItem.getTOTAL().substring(0, homeItem.getTOTAL().indexOf('.') + 3));
    }

    public void notifyDataSetChanged() {
        billsAdapter.notifyDataSetChanged();
    }


    public void clearDetail() {

        textviewForBIllno.setText("");
        textviewForDiscount.setText("");
        textviewForSubtotal.setText("");
        textviewForTax.setText("");
        textviewForTotal.setText("");
    }

    public void refreshDetail() {

        billsAdapter.removeDataSetItem(index);
        clearDetail();
    }
}