package com.chendsir.tsstreamparser.util;


import android.util.Log;


import com.chendsir.tsstreamparser.bean.Sdt;
import com.chendsir.tsstreamparser.bean.SdtService;
import com.chendsir.tsstreamparser.bean.Section;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.toHexString;




public class SdtManager {
    private static final String TAG = "SdtManager";

    private Sdt mSdt = null;
    private List<SdtService> mSdtServiceList = new ArrayList<>();

    public SdtManager() {
        super();
    }

    public Sdt makeSDT(List<Section> sectionList) {

        for (int i = 0; i < sectionList.size(); i++) {

            Section section = sectionList.get(i);
            byte[] sectionData = section.getSectionData();

            if (mSdt == null) {
                mSdt = new Sdt(sectionData);
            } else {
                int sectionNumber = sectionData[6] & 0xFF;
                mSdt.setSectionNumber(sectionNumber);
            }
            Log.d(TAG, " ---------------------------------------------- ");
            Log.d(TAG, " -- makeSDT()");
            Log.d(TAG, "tableId : 0x" + toHexString(mSdt.getTableId()));
            Log.d(TAG, "sectionSyntaxIndicator : 0x" + toHexString(mSdt.getSectionSyntaxIndicator()));
            Log.d(TAG, "sectionLength : 0x" + toHexString(mSdt.getSectionLength()));
            Log.d(TAG, "transportStreamId : 0x" + toHexString(mSdt.getTransportStreamId()));
            Log.d(TAG, "versionNumber : 0x" + toHexString(mSdt.getVersionNumber()));
            Log.d(TAG, "currentNextIndicator : 0x" + toHexString(mSdt.getCurrentNextIndicator()));
            Log.d(TAG, "sectionNumber : 0x" + toHexString(mSdt.getSectionNumber()));
            Log.d(TAG, "lastSectionNumber : 0x" + toHexString(mSdt.getLastSectionNumber()));
            Log.d(TAG, "originalNetworkId : 0x" + toHexString(mSdt.getOriginalNetworkId()));

            int sectionSize = sectionData.length;
            int theEffectiveLength = sectionSize - 15;
            for (int j = 0; j < theEffectiveLength; ) {
                Log.d(TAG, " -- ");
                int serviceId = (((sectionData[11 + j] & 0xFF) << 8) | (sectionData[12 + j] & 0xFF)) & 0xFFFF;
                int eitScheduleFlag = (sectionData[13 + j] >> 1) & 0x1;
                int eitPresentFollowingFlag = sectionData[13 + j] & 0x1;
                int runningStatus = (sectionData[14 + j] >> 5) & 0x7;
                int freeCaMode = (sectionData[14 + j] >> 4) & 0x1;
                int descriptorsLoopLength = (((sectionData[14 + j] & 0xF) << 8) | (sectionData[15 + j] & 0xFF)) & 0xFFF;
                int k=0;
                int startPositon =0;
                for (k=0;k<theEffectiveLength;k++){
                    if (sectionData[16+k+j]==0x48)
                    {
                        startPositon=16+k+j;
                        Log.d(TAG,"startPositon === "+startPositon);
                        break;
                    }
                }
                int descriptor_tag = sectionData[startPositon] & 0xff;
                int descriptor_length = sectionData[startPositon +1] & 0xFF;
                int serviceType = sectionData[startPositon +2] & 0xFF;
                int serviceProviderNameLength = sectionData[startPositon+3 ] & 0xFF;
                byte[] strBytes = new byte[serviceProviderNameLength];
                for (int n = 0; n < serviceProviderNameLength; n++) {
                    strBytes[n] = sectionData[startPositon +4+ n];
                }
                String serviceProviderName = null;
                try {
                    serviceProviderName = new String(strBytes,"GBK");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                int serviceNameLength = sectionData[startPositon+4+ serviceProviderNameLength] & 0xFF;
                strBytes = new byte[serviceNameLength+1];
                for (int n = 0; n < serviceNameLength+1; n++) {
                    strBytes[n] = sectionData[startPositon+4+ serviceProviderNameLength+ n];
                }
                String serviceName = null;
                try {
                    serviceName = new String(strBytes,"GBK");
                    serviceName = serviceName.replaceAll("\r | \n ","");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "serviceId : 0x" + toHexString(serviceId));
                Log.d(TAG, "eitScheduleFlag : 0x" + toHexString(eitScheduleFlag));
                Log.d(TAG, "eitPresentFollowingFlag : 0x" + toHexString(eitPresentFollowingFlag));
                Log.d(TAG, "runningStatus : 0x" + toHexString(runningStatus));
                Log.d(TAG, "freeCaMode : 0x" + toHexString(freeCaMode));
                Log.d(TAG, "descriptorsLoopLength : 0x" + toHexString(descriptorsLoopLength));
                Log.d(TAG,"descriptor_tag: 0x"+ toHexString(descriptor_tag));
                Log.d(TAG, "descriptor_length : 0x " + toHexString(descriptor_length));
                Log.d(TAG, "serviceType : 0x" + toHexString(serviceType));
                Log.d(TAG, "serviceProviderNameLength : 0x" +toHexString(serviceProviderNameLength));
                Log.d(TAG, "serviceProviderName : " + serviceProviderName);
                Log.d(TAG, "serviceNameLength : 0x" + toHexString(serviceNameLength));
                Log.d(TAG, "serviceName : " + serviceName);
                Log.d(TAG,"TESTTEST");

                SdtService sdtService = new SdtService(
                        serviceId, eitScheduleFlag, eitPresentFollowingFlag,
                        runningStatus, freeCaMode, descriptorsLoopLength,
                        serviceType, serviceProviderNameLength, serviceProviderName,
                        serviceNameLength, serviceName);
                mSdtServiceList.add(sdtService);

                j += (5 + descriptorsLoopLength);
            }
        }

        mSdt.setSdtServiceList(mSdtServiceList);

        return mSdt;
    }
}
