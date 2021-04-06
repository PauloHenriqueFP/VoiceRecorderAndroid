package com.cursojava.voicerecorder.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.cursojava.voicerecorder.MainActivity;
import com.cursojava.voicerecorder.R;
import com.cursojava.voicerecorder.utils.Constants;
import com.cursojava.voicerecorder.utils.ExternalStorageUtil;
import com.cursojava.voicerecorder.utils.SharedPreferenceUtil;

import java.io.IOException;
import java.util.Date;

import static com.cursojava.voicerecorder.notifications.ForegroundRecorderNotification.RECORDER_CHANNEL_ID;

public class ForegroundRecorderService extends Service {

    private MediaRecorder recorder;
    private String folderToSaveTheAudio;
    private SharedPreferences sp = SharedPreferenceUtil.getSharedPreferences();

    @Override
    public void onCreate() {
        super.onCreate();
        initfolderToSaveTheAudio();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, RECORDER_CHANNEL_ID)
                .setContentTitle("Bird Luxoo está te gravando felho")
                .setContentText("Gravando...")
                .setSmallIcon(R.drawable.bird_sing)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        startRecording();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
    }

    private void startRecording() {
        System.out.println("==================GRAVANDO=======================");
        String fileName = new Date().toLocaleString();

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(folderToSaveTheAudio + "/" + fileName + ".mp4");
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            System.out.println("PREPARE FAILED" + e.getMessage());
        }
        sp.edit().putBoolean(Constants.IS_RECORDING, true).commit();

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        System.out.println("==================PARANDO=======================");
        SharedPreferenceUtil.getSharedPreferences().edit().putBoolean(Constants.IS_RECORDING, false).commit();
        recorder.release();
        recorder = null;
    }

    private void initfolderToSaveTheAudio() {
        // the absolute path to application-specific directory.
        // May return null if shared storage is not currently available.
        folderToSaveTheAudio = ExternalStorageUtil.getPrivateExternalStorageBaseDir(getApplicationContext());
        if (folderToSaveTheAudio == null) {
            throw new RuntimeException("Erro ao criar pasta para salvar os audios");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
