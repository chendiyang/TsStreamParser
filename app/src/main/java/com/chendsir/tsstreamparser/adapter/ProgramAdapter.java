package com.chendsir.tsstreamparser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import static java.lang.Integer.toHexString;

import com.chendsir.tsstreamparser.R;
import com.chendsir.tsstreamparser.bean.ProgramList;

import java.util.List;

public class ProgramAdapter extends BaseAdapter {

	private List<ProgramList> mData;
	private Context mContext;

	public ProgramAdapter(List<ProgramList> mData, Context context) {
		this.mData = mData;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.program_item, parent, false);
			holder = new ViewHolder();
			holder.program_Number = (TextView) convertView.findViewById(R.id.program_number_list);
			holder.program_pid = (TextView) convertView.findViewById(R.id.program_pid_list);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		int tempNumber = mData.get(position).getProgramNumber();
		int tempPid = mData.get(position).getProgramMapPid();
		holder.program_Number.setText("Program Number: "+String.valueOf(tempNumber));
		holder.program_pid.setText("   Program PID:  0x" +toHexString(tempPid));
		return convertView;

	}
	private class ViewHolder{
		TextView program_Number;
		TextView program_pid;
	}

}
