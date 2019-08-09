package com.chendsir.tsstreamparser.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chendsir.tsstreamparser.thread.GetPacketLengthThread;
import com.chendsir.tsstreamparser.R;
import com.chendsir.tsstreamparser.util.GetPacketLength;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;


import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int WRITE_EXTERNAL_PERMISSION = 1;
    public static final int REFRESH_UI_PACKET_LENGTH = 0;
    private static final int REQUESTCODE_FROM_ACTIVITY = 1000;
    public static final String KEY_FOLDER_PATH = "FolderPath";
    public static final String KEY_FILE_NAME = "FileName";
    public static final String KEY_PACKET_INFO ="PacketInfo";
    private String myTsFilePath;
    private List<String> myTsFileList = new ArrayList<>();
    private TextView myPacketLengthTv;
    private Button tsFileSearchBtn;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private  int packetLength;
    private  int packetStartPosition;
    private GetPacketLength mGetPacketLength;
//    private final  MyHandler myHandler = new MyHandler(this);


    //正确的写法

//    private static class MyHandler extends  Handler {
//        private final WeakReference<MainActivity> WeakActivity;
//        private  MyHandler(MainActivity mainActivity) {
//            this.WeakActivity = new WeakReference<MainActivity>(mainActivity);
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            Bundle data = msg.getData();
//            MainActivity activitytemp = WeakActivity.get();
//            if (activitytemp != null) {
//                switch (msg.what) {
//                    case REFRESH_UI_PACKET_LENGTH:
//                        packetLength = data.getInt(GetPacketLengthThread.PACKET_LENGTH_KEY);
//                        packetStartPosition = data.getInt(GetPacketLengthThread.PACKET_START_POSITION_KEY);
//                        String strResult = activitytemp.getResources().getString(R.string.main_tv_packet_length_result);
//                        strResult = String.format(strResult, packetLength, packetStartPosition);
//                        activitytemp.myPacketLengthTv.setText(strResult);
//                        break;
//
//                    default:
//                        break;
//                }
//            }
//            }
//        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();
        initView();
        initData();
        initListView();
        //myHandler.sendEmptyMessageDelayed(1, 5 * 60 * 1000);
    }

    private void initPermission() {
        // 判断 Android 版本是否大于 23 （Android 6.0）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Request Read And Write Permission
            requestPermission();
        }
    }

    private void initListView() {

        if (myTsFileList != null) {
            adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, myTsFileList);
            listView.setAdapter(adapter);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String fileUrl = myTsFilePath+myTsFileList.get(position);
             //   GetPacketLengthThread myThread = new GetPacketLengthThread(fileUrl, myHandler);
             //   myThread.start();
                packetLength = -1;
                packetStartPosition = -1;
                packetLength = mGetPacketLength.getPacketLength(fileUrl);
                if (packetLength != -1) {
                    Log.d(TAG, "成功获得包的长度 : "
                            + packetLength);
                    packetStartPosition = mGetPacketLength.getPacketStartPosition();
                    Log.d(TAG, "成功获得有效包的起始位置 : "
                            + packetStartPosition);
                } else {
                    Log.e(TAG, "获取包长度失败。。。");
                }
                Log.d(TAG,packetLength+packetStartPosition+"");
                Bundle bundle = new Bundle();
                bundle.putInt("packetLength",packetLength);
                bundle.putInt("packetStartPosition",packetStartPosition);
                Intent intent = new Intent(MainActivity.this, ProgramListActivity.class);
                intent.putExtras(bundle);
                intent.putExtra(KEY_FOLDER_PATH, myTsFilePath);
                intent.putExtra(KEY_FILE_NAME, myTsFileList.get(position));
                startActivity(intent);

            }
        });
    }

    private void initData() {

        myTsFilePath = Environment.getExternalStorageDirectory().getPath() + "/ts/";
        mGetPacketLength = new GetPacketLength();
        File file = new File(myTsFilePath);
        Toast.makeText(MainActivity.this, myTsFilePath,Toast.LENGTH_SHORT).show();
        // 获取 ts 文件夹里面的文件列表
        String[] fileList = file.list();
        if (fileList != null) {
            myTsFileList.clear();
            for (String str : fileList) {
                myTsFileList.add(str);
            }
        }
    }

    private void initView() {
        myPacketLengthTv = findViewById(R.id.tv_packet_length);
        tsFileSearchBtn = findViewById(R.id.btn_ts_filesearch);
        listView = findViewById(R.id.lv_file_list);
        tsFileSearchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ts_filesearch:
                new LFilePicker()
                        .withActivity(MainActivity.this)
                        .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                        .withIconStyle(Constant.ICON_STYLE_GREEN)
                        .withMutilyMode(false)
                        .withBackgroundColor("#008577")
                        .withStartPath("/storage/emulated/0/Download")
                        .start();

                break;
                default:
                    break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {
                //If it is a file selection mode, you need to get the path collection of all the files selected
                //List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);//Constant.RESULT_INFO == "paths"
                List<String> list = data.getStringArrayListExtra("paths");

                Toast.makeText(getApplicationContext(), "selected " + list.size(), Toast.LENGTH_SHORT).show();
                //If it is a folder selection mode, you need to get the folder path of your choice
                String path = data.getStringExtra("path");
                //myTsFilePath = Environment.getExternalStorageDirectory().getPath() + "/ts/";
                File file = new File(path);
                // Toast.makeText(MainActivity.this, path,Toast.LENGTH_SHORT).show();
                // 获取 ts 文件夹里面的文件列表
                String[] fileList = file.list();
                if (fileList != null) {
                    myTsFileList.clear();
                    for (String str : fileList) {
                        myTsFileList.add(str);
                    }
                }

                //myTsFileList = file.list();
                myTsFilePath = path+"/";
                adapter.notifyDataSetChanged();
                //listView = findViewById(R.id.lv_file_list);
                if (myTsFileList != null) {
                    adapter = new ArrayAdapter<>(
                            this, android.R.layout.simple_list_item_1, myTsFileList);
                    listView.setAdapter(adapter);
                }
            }
        }
    }
    private void requestPermission() {
        // 判断系统是否已经赋予权限，结果为0则代表赋予过权限，为1则没有
        int checkReadPermission = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //  是否已经授予权限
        if (checkReadPermission != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_PERMISSION);
        }
    }
}
