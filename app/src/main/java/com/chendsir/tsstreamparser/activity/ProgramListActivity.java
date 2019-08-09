package com.chendsir.tsstreamparser.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chendsir.tsstreamparser.R;
import com.chendsir.tsstreamparser.adapter.ProgramAdapter;
import com.chendsir.tsstreamparser.bean.Pat;
import com.chendsir.tsstreamparser.bean.ProgramList;
import com.chendsir.tsstreamparser.thread.GetPidPacketThread;
import com.chendsir.tsstreamparser.util.GetPacketLength;
import com.chendsir.tsstreamparser.util.PacketManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;



public class ProgramListActivity extends AppCompatActivity implements View.OnClickListener {
	private static final String TAG = "ProgramListActivity";
	private static final int PAT_PID = 0x0000;
	private static final int PAT_TABLE_ID = 0x00;
	public static final int REFRESH_UI_PROGRAM_LIST = 0;
	private static final String HISTORY_FOLDER_PATH = Environment.getExternalStorageDirectory()
			.getPath() + "/ts_history/";


	private SmartRefreshLayout mRefreshLayout;

	private String mTSFolderPath;
	private String mFileName;
	private String mFilePath;

	private PacketManager mPacketManager;
	private List<ProgramList> mPatProgramList = new ArrayList<>();
	private ProgramAdapter myAdapter;
	private ListView listView;
	private Button refreshBtn;
	private Pat pat;
	private GetPidPacketThread mGetPidPacketThread;
	private int packetLength;
	private int packetStartPosition;
	private GetPacketLength mGetPacketLength;
	Handler mUIHandler = new Handler() {};

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_two);
		Log.d(TAG, "testtesttesttest");
		// 获取输入文件的路径和名字
		Bundle bundle = getIntent().getExtras();
		packetLength = bundle.getInt("packetLength");
		packetStartPosition = bundle.getInt("packetStartPosition");
		Log.d(TAG,packetLength+"");
		Log.d(TAG,packetStartPosition+"");
		Toast.makeText(ProgramListActivity.this,packetLength+"",Toast.LENGTH_SHORT).show();
		mTSFolderPath = getIntent().getStringExtra(MainActivity.KEY_FOLDER_PATH);
		mFileName = getIntent().getStringExtra(MainActivity.KEY_FILE_NAME);
		mFilePath = mTSFolderPath + mFileName;
		Log.d(TAG, mFilePath.toString());
		initView();
    	initData();
	}

	private void initView() {

		refreshBtn = findViewById(R.id.refresh_btn);
		refreshBtn.setOnClickListener(this);
		listView = findViewById(R.id.list_view_two);
		myAdapter = new ProgramAdapter( mPatProgramList,this);
		listView.setAdapter(myAdapter);

	}

	private void initData() {
		if (mFilePath != null) {
			String inputFilePath = mFilePath;
			Toast.makeText(ProgramListActivity.this,inputFilePath,Toast.LENGTH_SHORT).show();
			// 解 PAT
			//mPacketManager = new PacketManager(inputFilePath);
			mPacketManager = new PacketManager(
					inputFilePath,
					packetLength,
					packetStartPosition,
					PAT_PID,
					PAT_TABLE_ID,
					mUIHandler);
			Log.d(TAG, " ---- 开启线程");
			mGetPidPacketThread = new GetPidPacketThread(
					mPacketManager,
					mUIHandler);
			mGetPidPacketThread.start();
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		mGetPidPacketThread.over();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.refresh_btn:
				Pat pat = mPacketManager.getPat();
				listView = findViewById(R.id.list_view_two);
				if (pat != null) {
					Toast.makeText(ProgramListActivity.this, "I have some data in my packet", Toast.LENGTH_SHORT).show();
					mPatProgramList.clear();
					List<ProgramList> patProgramList = pat.getPatProgramList();
					for (ProgramList patProgram : patProgramList) {
						mPatProgramList.add(patProgram);
					}
					int test= mPatProgramList.get(0).getProgramNumber();
					Log.d(TAG,test+"number ");
					Toast.makeText(ProgramListActivity.this,String.valueOf(mPatProgramList.get(0).getProgramNumber()) , Toast.LENGTH_SHORT).show();
     				myAdapter.notifyDataSetChanged();
				}
				break;
				default:
					break;
		}
	}
}
