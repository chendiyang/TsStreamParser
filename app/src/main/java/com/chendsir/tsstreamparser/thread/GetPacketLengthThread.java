package com.chendsir.tsstreamparser.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.chendsir.tsstreamparser.activity.MainActivity;
import com.chendsir.tsstreamparser.util.GetPacketLength;

public class GetPacketLengthThread extends  Thread {

        private static final String TAG = "MyThread";
        public static final String PACKET_LENGTH_KEY = "packetLen";
        public static final String PACKET_START_POSITION_KEY = "packetStartPosition";
        private GetPacketLength mGetPacketLength;
        private String myTsFilePath;
        private Handler myHandler;

        //构造函数
        public GetPacketLengthThread(String filePath, Handler handler) {
            super();
            this.myTsFilePath = filePath;
            this.myHandler = handler;
        }


        @Override
        public void run() {
            super.run();
            mGetPacketLength = new GetPacketLength();
            int packetStartPosition = 0;
            int packetLength;
            // 获取包的 长度 和 开始位置
            packetLength = mGetPacketLength.getPacketLength(myTsFilePath);
            if (packetLength != -1) {
                Log.d(TAG, "成功获得包的长度 : "
                        + packetLength);
                packetStartPosition = mGetPacketLength.getPacketStartPosition();
                Log.d(TAG, "成功获得有效包的起始位置 : "
                        + packetStartPosition);
            } else {
                Log.e(TAG, "获取包长度失败。。。");
            }

            // 更新 UI
//            Message msg = Message.obtain();  //Message 对象可以重复使用，不需要一直new Message
//            msg.what = MainActivity.REFRESH_UI_PACKET_LENGTH;
//            Bundle data = new Bundle();
//            data.putInt(PACKET_LENGTH_KEY, packetLength);
//            data.putInt(PACKET_START_POSITION_KEY, packetStartPosition);
//            msg.setData(data);
//            myHandler.sendMessage(msg);

        }



}
