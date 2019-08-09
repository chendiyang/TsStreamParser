package com.chendsir.tsstreamparser.util;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;


import com.chendsir.tsstreamparser.activity.ProgramListActivity;
import com.chendsir.tsstreamparser.bean.PacketHeader;
import com.chendsir.tsstreamparser.bean.Pat;
import com.chendsir.tsstreamparser.bean.Section;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static java.lang.Integer.toHexString;

/**
 * PacketManager
 *
 * @author ggz
 * @date 2018/3/22
 */

public class PacketManager {
	private static final String TAG = "PacketManager";
	private static final int CYCLE_TEN_TIMES = 10;
	private static final int PACKET_HEADER_SYNC_BYTE = 0x47;
	private static final int PACKET_LENGTH_188 = 188;
	private static final int PACKET_LENGTH_204 = 204;
	private static final int PAT_PID = 0x0000;
	private static final int SDT_PID = 0x0011;
	private static final int PMT_TABLE_ID = 0x02;
	private static final String OUTPUT_FILE_PATH = Environment.getExternalStorageDirectory()
			.getPath() + "/ts_history/resultFile";


	private boolean isOver = false;

	private String mInputFilePath = null;
	private String mOutputFilePath = OUTPUT_FILE_PATH;
	private Handler mHandler;

	private int mPacketLength = -1;
	private int mPacketStartPosition = -1;

	private int mInputPID = -1;
	private int mInputTableID = -1;
	private int mPacketNum = -1;

	private SectionManager mSectionManager = new SectionManager();
	private List<Section> mSectionList;

	private Pat mPat = null;

	/**
	 * 构造函数
	 * 用于解包长
	 * 对应线程：GetPacketLengthThread
	 */
	public PacketManager(String inputFilePath) {
		super();
		this.mInputFilePath = inputFilePath;
	}


	/**
	 * 构造函数
	 * 用于获取指定 PID 和 table_id 的 section
	 * 对应线程：GetPidPacketThread
	 */
	public PacketManager(String inputFilePath, int mPacketLength,int mPacketStartPosition, int inputPID, int inputTableId, Handler handler) {
		super();
		this.mInputFilePath = inputFilePath;
		this.mInputPID = inputPID;
		this.mPacketLength = mPacketLength;
		this.mPacketStartPosition = mPacketStartPosition;
		this.mInputTableID = inputTableId;
		this.mHandler = handler;
	}



	/**
	 * 匹配指定 PID 和 table_id 的 section
	 */
	public int matchPidPacket(String inputFilePath, int inputPID, int inputTableID) {
		Log.d(TAG, " -- matchPidPacket()");
		mInputFilePath = inputFilePath;
		mInputPID = inputPID;
		mInputTableID = inputTableID;
		Log.d(TAG, "inputFilePath : " + mInputFilePath);
		Log.d(TAG, "outputFilePath : " + mOutputFilePath);


		try {
			Log.d(TAG,mPacketLength+"");
			Log.d(TAG,mPacketStartPosition+"");
			FileInputStream fis = new FileInputStream(mInputFilePath);
			FileOutputStream fos = new FileOutputStream(mOutputFilePath);

			// 跳到包的开始位置
			long lg = fis.skip(mPacketStartPosition);
			if (lg != mPacketStartPosition) {
				Log.e(TAG, "failed to skip " + mPacketStartPosition + "bytes");
				return -1;
			}

			mPacketNum = 0;
			int err;
			do {
				// 结束查找
				if (isOver) {
					isOver = false;
					Log.e(TAG, "isOver !!!");
					break;
				}

				byte[] buff = new byte[mPacketLength];
				err = fis.read(buff);
				if (err == mPacketLength) {
					if (buff[0] == PACKET_HEADER_SYNC_BYTE) {
						// 构建 packet 对象
						PacketHeader packet = new PacketHeader(buff);

						// 匹配 pid
						if (packet.getPid() == mInputPID) {
							mPacketNum++;

							// 匹配 section
							int result = mSectionManager.matchSection(packet, mInputTableID);

							// 写出文件
							fos.write(buff);
						}
					}
				}
			} while (err != -1);

			fos.close();
			fis.close();
		} catch (IOException e) {
			Log.e(TAG, "IOException : 打开文件失败");
			e.printStackTrace();
		}

		// 打印 section 结果
		mSectionManager.print();
		// 解表
		mSectionList = mSectionManager.getSectionList();
		parseTable(mSectionList, mInputPID);

		Log.d(TAG, " ---------------------------------------------- ");
		Log.d(TAG, "the number of the Packet's PID = 0x" + toHexString(mInputPID)
				+ " is " + mPacketNum);
		Log.d(TAG, "success to write file to " + mOutputFilePath);

		return mPacketNum;
	}

	/**
	 * 根据 pid 和table 来解表
	 */
	private void parseTable(List<Section> list, int pid) {
		Log.d(TAG, " -- parseTable()");
		if (list == null) {
			Log.e(TAG, "mSectionList == null !!!");
			return;
		}
		switch (pid) {
			case PAT_PID:
				PatManager patManager = new PatManager();
				mPat = patManager.makePAT(list);
				mHandler.sendEmptyMessage(ProgramListActivity.REFRESH_UI_PROGRAM_LIST);
				break;
			case PMT_TABLE_ID:
				break;
			case SDT_PID:
				break;

			default:
				if (list.get(0).getTableId() == PMT_TABLE_ID) {
					// 解 PMT 表
				}
				break;
		}
	}

	public void setOver(boolean over) {
		isOver = over;
	}

	public String getInputFilePath() {
		return mInputFilePath;
	}

	public int getPacketLength() {
		return mPacketLength;
	}

	public int getPacketStartPosition() {
		return mPacketStartPosition;
	}

	public int getInputPID() {
		return mInputPID;
	}

	public int getInputTableID() {
		return mInputTableID;
	}

	public void setOutputFilePath(String mOutputFilePath) {
		this.mOutputFilePath = mOutputFilePath;
	}

	public void setHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	public Handler getHandler() {
		return mHandler;
	}

	public Pat getPat() {
		return mPat;
	}
}

