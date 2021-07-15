package com.cs.mediarecordertest.screenrecorder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.mediarecordertest.R;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static android.os.Environment.DIRECTORY_MOVIES;

public class ScreenRecordActivity extends AppCompatActivity {
    private static final String TAG = "ScreenRecordActivity";
    private TextView timeTv;
    private int mTime = 0;
    private MediaProjectionManager mProjectionManager;
    private MediaRecordService mediaRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_record);

        initView();
        initData();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            mTime++;
            timeTv.setText(mTime + "");
            handler.sendEmptyMessageDelayed(1, 1000);
        }
    };

    private void initView() {
        timeTv = ((TextView) findViewById(R.id.record_time));

    }

    private void initData() {

    }

    // 申请权限后的回调
    // 1、录音和读写
    // 2、录屏startActivityForResult后的回调
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // 录屏权限
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this,"aaa",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, CaptureScreenService.class);
                intent.putExtra("code", resultCode);
                intent.putExtra("data", data);
                startForegroundService(intent);

            } else {
                Toast.makeText(ScreenRecordActivity.this, "录屏失败", Toast.LENGTH_LONG).show();
            }


        }
    }

    public void onScreenRecordStart(View view) {
        mProjectionManager = ((MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE));
        if (mProjectionManager != null) {
            Intent intent = mProjectionManager.createScreenCaptureIntent();
            PackageManager packageManager = getPackageManager();
            if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                //加载录屏授权的activity
                startActivityForResult(intent, 1);
            } else {
                Toast.makeText(this, "没有录屏权限！", Toast.LENGTH_LONG).show();
            }
        }


    }

    public void onScreenRecordStop(View view) {
      //  mediaRecord.release();
        //handler.removeMessages(1);
        //关闭服务
        Intent service = new Intent(this, CaptureScreenService.class);
        stopService(service);
    }


}