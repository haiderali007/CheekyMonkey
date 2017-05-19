package com.entrada.cheekyMonkey.takeorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.steward.bill.TableBillGenTask;
import com.entrada.cheekyMonkey.takeorder.adapter.TableAdapter;
import com.entrada.cheekyMonkey.takeorder.entity.TableItem;


public class TableNumberLayout implements OnItemClickListener {

    /*public static interface TableSelectListener {
        public void tableRemoved();

		public void tableSelected(TableStatusItem table);

		public void tableMoved(TableStatusItem oldTable,
                               TableStatusItem newTable);
	}*/

    View tableView = null;
    LayoutInflater layoutInflater;

    ProgressBar progressBarTable;
    GridView grdVwTableNumber;
    Context context;
    int width, heightv;
    ICallTable iCallTable;
    private TableAdapter adapter;

    public TableNumberLayout(Context context, int width, ICallTable iCallTable) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.width = width;
        this.iCallTable = iCallTable;
    }

    public View getAllTablePopupWindow(String parameter, String serverIP) {

        tableView = layoutInflater.inflate(R.layout.table_number_popup, null);
        grdVwTableNumber = (GridView) tableView.findViewById(R.id.grdTableNumTakeOrder);
        progressBarTable = (ProgressBar) tableView.findViewById(R.id.progressBarTable);

        adapter = new TableAdapter(context);
        grdVwTableNumber.setAdapter(adapter);
        grdVwTableNumber.setOnItemClickListener(this);
        new TableNumberTask(context, adapter, parameter, serverIP, progressBarTable).execute();
        return tableView;
    }

    public View getRunningTablePopupWindow(String parameter, String serverIP) {

        tableView = layoutInflater.inflate(R.layout.table_number_popup, null);
        grdVwTableNumber = (GridView) tableView.findViewById(R.id.grdTableNumTakeOrder);
        progressBarTable = (ProgressBar) tableView.findViewById(R.id.progressBarTable);

        adapter = new TableAdapter(context);
        grdVwTableNumber.setAdapter(adapter);
        grdVwTableNumber.setOnItemClickListener(this);
        new TableBillGenTask(context, adapter, parameter, serverIP, progressBarTable).execute();
        return tableView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        //TableGridAdapter.ViewHandler holder = (ViewHandler) view.getTag();

        iCallTable.TableItem((TableItem) adapter.getItem(position));
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

}
