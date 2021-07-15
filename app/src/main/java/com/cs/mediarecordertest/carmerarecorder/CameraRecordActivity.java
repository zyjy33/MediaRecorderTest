package com.cs.mediarecordertest.carmerarecorder;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.cs.mediarecordertest.R;

import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import static android.os.Environment.DIRECTORY_MOVIES;

public class CameraRecordActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private MediaRecorder recorder;
    private File saveFile;
    private boolean isRecoding = false;
    private String saveFilePath;
    private static final String TAG = "CameraRecordActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_record);
        hide();
        initView();
        initData();
    }


    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

    }

    private void initView() {
        surfaceView = ((SurfaceView) findViewById(R.id.camera_surface));
        surfaceView.getHolder().setKeepScreenOn(true);

    }


    private void initData() {
        Log.d(TAG, "initData: 11");
    }

    public void onCameraRecordStart(View view) {
        if (isRecoding) {
            Toast.makeText(this, "正在录制中", Toast.LENGTH_SHORT);
            return;
        }
        Log.d(TAG, "onCameraRecordStart: start");
        recorder = new MediaRecorder();
        recorder.reset();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); // camera 相机资源
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //设置音频的编码格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //设置图像的编码格式
        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

        recorder.setAudioEncodingBitRate(128);
      //  saveFilePath = Environment.getExternalStorageDirectory()
        //        .getAbsolutePath() + "/video1.mp4";
        File filesDir = getExternalFilesDir(DIRECTORY_MOVIES);

        saveFilePath =  filesDir.getAbsolutePath() + "/video1.mp4";
        recorder.setOrientationHint(90); //逆时针90度
        recorder.setOutputFile(saveFilePath);

        recorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //开始录制
        recorder.start();
        Log.d(TAG, "onCameraRecordStart: end");
        isRecoding = true;
    }


    public void onCameraRecordStop(View view) {
        if (!isRecoding) {
            Toast.makeText(this, "已经停止录制", Toast.LENGTH_SHORT).show();
            return;
        }
        //停止录制
        recorder.stop();
        //释放资源
        recorder.release();
        recorder = null;
        isRecoding = false;
    }
}