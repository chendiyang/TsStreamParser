package com.chendsir.tsstreamparser.util;


import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static java.lang.Integer.toHexString;



public class GetPacketLength {
    private static final String TAG = "GetPacketLength";
    private static final int PACKET_HEADER_SYNC_BYTE = 0x47;
    private static final int PACKET_LENGTH_188 = 188;
    private static final int PACKET_LENGTH_204 = 204;
    private int mPacketStartPosition = -1;
    private int mPacketLength = -1;


    /**
     * 获取包的 长度 和 开始位置
     */
    public int getPacketLength(String filePath) {
        mPacketStartPosition = -1;
        mPacketLength = -1;
        Log.d(TAG, filePath);
        try {
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);

            int temp;
            while ((temp = fileInputStream.read()) != -1) {
                // 记录包的开始位置
                mPacketStartPosition++;
                Log.d(TAG, "当前位置 : " + mPacketStartPosition + temp+"   0x" + toHexString(temp));

                // 找到有效同步位置0x47
                if (temp == PACKET_HEADER_SYNC_BYTE) {
                    Log.d(TAG, "已经匹配到  0x" + toHexString(PACKET_HEADER_SYNC_BYTE));


                    boolean isFinish = true;
                    for (int i = 0; i < 5; i++) {
                        long skipBytes = fileInputStream.skip(PACKET_LENGTH_188 - 1);//返回的是跳过的实际字节数
                        if (skipBytes != PACKET_LENGTH_188 - 1) {  //1、如果在跳过n个字节之前已经到达了末尾 2、输入参数为负
                            Log.e(TAG, "长度不够，无法跳过 " + (PACKET_LENGTH_188 - 1) + "bytes");
                            return -1;
                        }
                        //已经跳过了一个包长的字节，继续读取下一个字节判断是否为同步字节
                        temp = fileInputStream.read();
                        Log.d(TAG, "第" + (i + 1) + "次循环，当前位置的字节为  :  0x" + toHexString(temp));


                        if (temp != PACKET_HEADER_SYNC_BYTE) {

                            isFinish = false;
                            Log.d(TAG, "当前包的0x47不是有效字节");
                            break;
                        }

                    }
                    if (isFinish) {
                        Log.d(TAG, "验证完成，当前包的长度为: " + PACKET_LENGTH_188);
                        mPacketLength = PACKET_LENGTH_188;
                        // 跳出 while ，结束检测
                        break;
                    }

                    // 如果不是188字节的包长则继续判断是否为204字节包长
                    isFinish = true;
                    for (int i = 0; i < 5; i++) {
                        long lg = fileInputStream.skip(PACKET_LENGTH_204 - 1);
                        if (lg != PACKET_LENGTH_204 - 1) {
                            // 如果长度不够，返回失败结果
                            Log.e(TAG, "长度不够，无法跳过  " + (PACKET_LENGTH_204 - 1) + "bytes");
                            return -1;
                        }
                        temp = fileInputStream.read();
                        Log.d(TAG, "第 "+ (i + 1) +
                                " 次循环，当前位置的字节为 :  0x" + toHexString(temp));
                        if (temp != PACKET_HEADER_SYNC_BYTE) {
                            isFinish = false;
                            Log.d(TAG, "当前包的0x47不是有效字节 ");
                            // 跳出 for 循环
                            break;
                        }
                    }
                    if (isFinish) {
                        Log.d(TAG, "验证完成，当前包的长度为: " + PACKET_LENGTH_204);
                        mPacketLength = PACKET_LENGTH_204;
                        // 结束检测
                        break;
                    }
                }

            }

            fileInputStream.close();

        } catch (IOException e) {
            Log.e(TAG, "IOException : 打开文件失败");
            e.printStackTrace();
        }

        return mPacketLength;
    }


    public int getPacketStartPosition() {
        return mPacketStartPosition;
    }


}

