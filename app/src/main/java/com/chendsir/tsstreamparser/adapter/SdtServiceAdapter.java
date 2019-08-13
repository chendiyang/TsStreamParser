package com.chendsir.tsstreamparser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chendsir.tsstreamparser.R;
import com.chendsir.tsstreamparser.bean.SdtService;

import java.util.List;

import static java.lang.Integer.toHexString;

public class SdtServiceAdapter extends BaseAdapter {
	private List<SdtService> mData;
	private Context mContext;

	public SdtServiceAdapter(List<SdtService> mData, Context context) {
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
			holder.serviceNameTv = (TextView) convertView.findViewById(R.id.program_number_list);
			holder.serviceIdTv = (TextView) convertView.findViewById(R.id.program_pid_list);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		String tempNumber = mData.get(position).getServiceName();
		int tempPid = mData.get(position).getServiceId();
		holder.serviceNameTv.setText("Program Number: "+String.valueOf(tempNumber));
		holder.serviceIdTv.setText("   Program PID:  0x" +toHexString(tempPid));
		return convertView;

	}
	private class ViewHolder{
		TextView serviceNameTv;
		TextView serviceIdTv;
	}
}
