package com.chendsir.tsstreamparser.util;


import android.util.Log;

import com.chendsir.tsstreamparser.bean.PacketData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.toHexString;



public class GetPacketLength {
    private static final String TAG = "GetPacketLength";
    private static final int PACKET_HEADER_SYNC_BYTE = 0x47;
    private static final int PACKET_LENGTH_188 = 188;
    private static final int PACKET_LENGTH_204 = 204;
    private boolean isOver = false;
    private int mPacketStartPosition = -1;
    private int mPacketLength = -1;
    private List<PacketData> packetDataList = new ArrayList<>();

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

   //获取整体的有效包
    public List<PacketData> getPacketHeaderList(String filePath, int mPacketLength, int mPacketStartPosition) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 跳到包的开始位置
        long lg = 0;
        try {
            lg = fis.skip(mPacketStartPosition);

        if (lg != mPacketStartPosition) {
            Log.e(TAG, "failed to skip " + mPacketStartPosition + "bytes");
            return null;
        }

        int err;
        int mPacketNum = 0;
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
                    PacketData packet = new PacketData(buff);
                    packetDataList.add(packet);
                }
            }
        } while (err != -1);
        fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return packetDataList;
    }

    public void setPacketDataList(List<PacketData> packetDataList) {
        this.packetDataList = packetDataList;
    }



}

