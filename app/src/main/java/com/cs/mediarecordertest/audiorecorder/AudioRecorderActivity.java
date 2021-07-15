package com.cs.mediarecordertest.audiorecorder;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.cs.mediarecordertest.R;
import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import static android.os.Environment.DIRECTORY_MOVIES;

public class AudioRecorderActivity extends AppCompatActivity {
    private static final String TAG = "AudioRecorderActivity";


    private String mPath;
    private MediaPlayer player;
    private String mFileName;
    private MediaRecorder recorder;
    public static final int RECORDING = 1;
    public static final int PLAYING = 2;
    public static final int IDLE = 0;

    private int mState = IDLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audiorecorder);
        hide();
        initView();

    }


    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

    }

    private boolean checkState() {

        switch (mState) {
            case RECORDING:
                Toast.makeText(this, "当前录制状态，请先结束录制！", Toast.LENGTH_SHORT).show();
                return false;
            case PLAYING:
                Toast.makeText(this, "当前播放状态，请先结束播放！", Toast.LENGTH_SHORT).show();
                return false;


        }

        return true;
    }

    private void initView() {
        File filesDir = getExternalFilesDir(DIRECTORY_MOVIES);
        mPath = filesDir.getAbsolutePath();
        mFileName = mPath + "/audio_1.3gp";
        Log.d(TAG, "initView: path = " +mFileName);
    }


    public void startRecording() {
        if (!checkState()) {
            return;
        }
        mState = RECORDING;
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(mFileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
            mState = IDLE;
        }

        recorder.start();
    }

    public void stopRecording() {

        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }else{
            Toast.makeText(this, "当前已是空闲状态！", Toast.LENGTH_SHORT).show();
        }
        mState = IDLE;
    }


    public void onAudioRecorderStart(View view) {
        startRecording();
    }

    public void onAudioRecorderStop(View view) {
        stopRecording();
    }

    public void onPlayAudio(View view) {
        if (!checkState()) {
            return;
        }
        mState = PLAYING;
        player = new MediaPlayer();
        try {
            player.setDataSource(mFileName);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mState = IDLE;
                }
            });
            player.prepare();
            player.start();

        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
            mState = IDLE;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }

}