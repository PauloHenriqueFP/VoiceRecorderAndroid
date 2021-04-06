package com.cursojava.voicerecorder.notifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.cursojava.voicerecorder.utils.SharedPreferenceUtil;

public class ForegroundRecorderNotification extends Application {
    public static final String RECORDER_CHANNEL_ID = "recorderChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        // Initializing Shared Preferences
        SharedPreferenceUtil.initialize(getApplicationContext());
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel recorderNotificationChannel = new NotificationChannel(
                RECORDER_CHANNEL_ID,
                "Recording Audio BirdLuxxxo",
                NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(recorderNotificationChannel);
        }
    }
}
