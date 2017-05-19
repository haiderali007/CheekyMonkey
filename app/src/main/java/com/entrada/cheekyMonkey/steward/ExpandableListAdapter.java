package com.entrada.cheekyMonkey.steward;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.entrada.cheekyMonkey.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<String> listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<String>> listDataChild;

	public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String,   List<String>> listChildData) {
		this.context = context;
		this.listDataHeader = listDataHeader;
		this.listDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return listDataChild.get(listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final String childText = (String) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		ImageView imageView = (ImageView) convertView.findViewById(R.id.viewSelector);
		imageView.setImageResource(childResourceId(childPosition));
		TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
		txtListChild.setText(childText);

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groupPosition == 1 || groupPosition == 2 ? listDataChild.get(listDataHeader.get(groupPosition)).size() : 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.group_row_layout, null);
		}

		TextView tv_Title = (TextView) convertView.findViewById(R.id.txtTitle);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.viewSelector);
		imageView.setImageResource(resourceId(groupPosition));
		ImageView imageViewIndicator = (ImageView) convertView.findViewById(R.id.viewIndicator);
		imageViewIndicator.setImageResource(isExpanded ? R.drawable.arrow_up_24 : R.drawable.arrow_24);
		imageViewIndicator.setVisibility(groupPosition == 1 || groupPosition == 2 ? View.VISIBLE : View.GONE);

		tv_Title.setTypeface(null, Typeface.BOLD);
		tv_Title.setText(headerTitle);
		convertView.setBackgroundColor(groupPosition % 2 == 0 ? 0xFF2a3644 : 0xFF2D3B48) ;

		return convertView;
	}

	private int childResourceId(int position) {

		int resId = R.drawable.home;

		switch (position) {
			case 0:
				resId = R.drawable.cancel;
				break;
			case 1:
				resId = R.drawable.modify;
				break;
			case 2:
				resId = R.drawable.split;
				break;
			case 3:
				resId = R.drawable.transfer;
				break;
			default:
				break;
		}

		return resId;
	}


	private int resourceId(int position) {

		int resId = R.drawable.home;

		switch (position) {
			case 0:
				resId = R.drawable.home;
				break;
			case 1:
				resId = R.drawable.orders;
				break;
			case 2:
				resId = R.drawable.bills;
				break;
			case 3:
				resId = R.drawable.tables;
				break;
			case 4:
				resId = R.drawable.settlement;
				break;
			case 5:
				resId = R.drawable.unsettle;
				break;
			case 6:
				resId = R.drawable.generate;
				break;
			case 7:
				resId = R.drawable.discount;
				break;
			case 8:
				resId = R.drawable.under_process;
				break;
			case 9:
				resId = R.drawable.order_accepted;
				break;
			case 10:
				resId = R.drawable.order_rejected;
				break;
			case 11:
				resId = R.drawable.cover;
				break;
			case 12:
				resId = R.drawable.logout;
				break;

			default:
				break;
		}

		return resId;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
