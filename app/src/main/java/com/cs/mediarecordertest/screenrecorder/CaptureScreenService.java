package com.cs.mediarecordertest.screenrecorder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.cs.mediarecordertest.MainActivity;
import com.cs.mediarecordertest.R;

import java.io.File;
import java.util.Objects;

import androidx.annotation.Nullable;

import static android.os.Environment.DIRECTORY_MOVIES;

public class CaptureScreenService extends Service {
    private int mResultCode;
    private Intent mResultData;
    private MediaProjectionManager projectionManager;
    private MediaProjection mMediaProjection;
    private static final String TAG = "CaptureScreenService";
    private MediaRecordService mediaRecord;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Log.e(TAG, "onStartCommand ");
        mResultCode = intent.getIntExtra("code", -1);
        mResultData = intent.getParcelableExtra("data");
        Log.d(TAG, "onStartCommand: code " + mResultCode+ "data ="+mResultData);
        this.projectionManager = (MediaProjectionManager)getSystemService(Context.MEDIA_PROJECTION_SERVICE);


        try {
            // mediaProjection 如果不在权限申请中回调，获取到的对象为空
            mMediaProjection = projectionManager.getMediaProjection(mResultCode, Objects.requireNonNull(mResultData));
            Log.e(TAG, "onStartCommand created: " + mMediaProjection);
            if (mMediaProjection == null) {
                Log.e(TAG, "media projection is null");

            }
            File filesDir = getExternalFilesDir(DIRECTORY_MOVIES);

            String path = filesDir.getAbsolutePath() + "/video1.mp4";
            Log.d(TAG, "onActivityResult: "+ path);
            File file = new File(path);  //录屏生成文件
            if (!file.exists()) {
                file.createNewFile();
            }
            mediaRecord = new MediaRecordService(1024, 720, 6000000, 1,
                    mMediaProjection, file.getAbsolutePath());
            mediaRecord.start();

        } catch (Exception e) {
            Log.d(TAG, "onActivityResult: "+e.getMessage());
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotificationChannel() {
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
        Intent nfIntent = new Intent(this, MainActivity.class); //点击后跳转的界面，可以设置跳转数据

        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
                //.setContentTitle("SMI InstantView") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                .setContentText("is running......") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        /*以下是对Android 8.0的适配*/
        //普通notification适配
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("notification_id");
        }
        //前台服务notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("notification_id", "notification_name", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(110, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaRecord.release();
        stopForeground(true);
    }
}
