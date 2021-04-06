package com.cursojava.voicerecorder;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cursojava.voicerecorder.services.ForegroundRecorderService;
import com.cursojava.voicerecorder.utils.Constants;
import com.cursojava.voicerecorder.utils.ExternalStorageUtil;
import com.cursojava.voicerecorder.utils.SharedPreferenceUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ImageView micImage;
    private TextView recordingMsg;

    private SharedPreferences sp = SharedPreferenceUtil.getSharedPreferences();

    private final int AUDIO_PERMISSION_CODE = 200;
    private final int STORAGE_PERMISSIONS_CODE = 201;

    private String[] audioPermission = {
            Manifest.permission.RECORD_AUDIO
    };
    private String[] storagePermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        micImage = findViewById(R.id.microphone_img);
        recordingMsg = findViewById(R.id.recording_msg);

        setMicImage(sp.getBoolean(Constants.IS_RECORDING, false));

        micImage.setOnClickListener(v -> onClickMic());

        if (!isPermissionToStorage()) {
            requestPermissionToUseStorage();
        }
    }

    private void onClickMic() {
        if (isPermissionToRecord() && isPermissionToStorage()) {
            boolean isRecording = sp.getBoolean(Constants.IS_RECORDING, false);
            if (isRecording) {
                isRecording = false;
                setMicImage(isRecording);
                stopRecordingService();
            } else {
                isRecording = true;
                setMicImage(isRecording);
                startRecordingService();
            }
        } else {
            requestPermissionToRecord();
        }
    }

    public void setMicImage(boolean isRecording) {
        if (isRecording) {
            micImage.setImageResource(R.drawable.microphone_on);
        } else {
            micImage.setImageResource(R.drawable.microphone_off);
        }
    }

    private void requestPermissionToUseStorage() {
        ActivityCompat.requestPermissions(
                this,
                storagePermissions,
                STORAGE_PERMISSIONS_CODE
        );
    }

    private void requestPermissionToRecord() {
        ActivityCompat.requestPermissions(
                this,
                audioPermission,
                AUDIO_PERMISSION_CODE
        );
    }

    private boolean isPermissionToStorage() {
        boolean canRead = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;

        boolean canWrite = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;

        return canRead && canWrite;
    }

    private boolean isPermissionToRecord() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void startRecordingService() {
        Intent recordingService = new Intent(this, ForegroundRecorderService.class);
        startService(recordingService);
    }

    private void stopRecordingService() {
        Intent recordingService = new Intent(this, ForegroundRecorderService.class);
        stopService(recordingService);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // AUDIO
        if (requestCode == AUDIO_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            System.out.println("VOCE LIBEROU AUDIO");
            startRecordingService();
        } else if (requestCode == AUDIO_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_DENIED){
            System.out.println("VOCE NAO LIBEROU AUDIO");
        }

        // STORAGE
        if (requestCode == STORAGE_PERMISSIONS_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            System.out.println("STORAGE LIBERADO");
        } else if (requestCode == STORAGE_PERMISSIONS_CODE && grantResults[0] == PackageManager.PERMISSION_DENIED){
            System.out.println("VOCE NAO LIBEROU STORAGE");
            // Ask again
            requestPermissionToUseStorage();
        }

    }
}