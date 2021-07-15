package com.cs.mediarecordertest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cs.mediarecordertest.audiorecorder.AudioRecorderActivity;
import com.cs.mediarecordertest.carmerarecorder.CameraRecordActivity;
import com.cs.mediarecordertest.screenrecorder.ScreenRecordActivity;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onAudioRecorder(View view) {
        startActivity(new Intent(this, AudioRecorderActivity.class));
    }

    public void onCameraRecord(View view){
        startActivity(new Intent(this, CameraRecordActivity.class));
    }

    public void onScreenRecord(View view) {
        startActivity(new Intent(this, ScreenRecordActivity.class));
    }
}