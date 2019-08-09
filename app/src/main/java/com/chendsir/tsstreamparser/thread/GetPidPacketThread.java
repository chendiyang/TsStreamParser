package com.chendsir.tsstreamparser.thread;

import android.os.Handler;
import android.util.Log;
import com.chendsir.tsstreamparser.util.PacketManager;




public class GetPidPacketThread extends Thread {
	private static final String TAG = "GetPidPacketThread";
	private PacketManager mPacketManager;
	private Handler mHandler;


	public GetPidPacketThread(PacketManager packetManager, Handler handler) {
		super();
		this.mPacketManager = packetManager;
		this.mHandler = handler;
	}

	@Override
	public void run() {
		super.run();

		int packetNum = mPacketManager.matchPidPacket(
				mPacketManager.getInputFilePath(),
				mPacketManager.getInputPID(),
				mPacketManager.getInputTableID());
			//	Pat pat =mPacketManager.getPat();

		if (packetNum != -1) {
			Log.d(TAG, "succeed to get all Specified Packet : " + packetNum);
		} else {
			Log.e(TAG, "failed to get Specified Packet !!!");
		}
		// 更新 UI
	//	Message msg = Message.obtain();  //Message 对象可以重复使用，不需要一直new Message
	//	msg.what = ProgramListActivity.REFRESH_UI_PROGRAM_LIST;
//		Bundle data = new Bundle();
//		data.putInt(PACKET_LENGTH_KEY, packetLength);
//		data.putInt(PACKET_START_POSITION_KEY, packetStartPosition);
//		msg.setData(data);
	//	mHandler.sendMessage(msg);
	}

	public void over() {
		mPacketManager.setOver(true);
		Log.e(TAG, "GetPidPacketThread is Over !!!");
	}
}
