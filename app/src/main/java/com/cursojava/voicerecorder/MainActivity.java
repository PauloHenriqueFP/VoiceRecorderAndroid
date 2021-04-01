package com.cursojava.voicerecorder;

import android.Manifest;
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

import com.cursojava.voicerecorder.utils.ExternalStorageUtil;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView micImage;
    private TextView recordingMsg;
    private boolean isRecording;
    private String folderToSaveTheAudio;

    private final int AUDIO_PERMISSION_CODE = 200;
    private final int STORAGE_PERMISSIONS_CODE = 201;
    private String[] audioPermission = {
            Manifest.permission.RECORD_AUDIO
    };
    private String[] storagePermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    // Recording
    private MediaRecorder recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isRecording = false;

        micImage = findViewById(R.id.microphone_img);
        recordingMsg = findViewById(R.id.recording_msg);

        micImage.setOnClickListener(v -> onClickMic());

        if (!isPermissionToStorage()) {
            requestPermissionToUseStorage();
        } else {
            initfolderToSaveTheAudio();
        }
    }

    private void onClickMic() {
        if (isPermissionToRecord() && isPermissionToStorage() && folderToSaveTheAudio != null) {
            if (isRecording) {
                isRecording = false;
                micImage.setImageResource(R.drawable.microphone_off);
                stopRecording();
            } else {
                isRecording = true;
                micImage.setImageResource(R.drawable.microphone_on);
                startRecording();
            }
        } else {
            requestPermissionToRecord();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // AUDIO
        if (requestCode == AUDIO_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            System.out.println("VOCE LIBEROU AUDIO");
            startRecording();
        } else if (requestCode == AUDIO_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_DENIED){
            System.out.println("VOCE NAO LIBEROU AUDIO");
        }

        // STORAGE
        if (requestCode == STORAGE_PERMISSIONS_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initfolderToSaveTheAudio();
        } else if (requestCode == STORAGE_PERMISSIONS_CODE && grantResults[0] == PackageManager.PERMISSION_DENIED){
            System.out.println("VOCE NAO LIBEROU STORAGE");
            // Ask again
            requestPermissionToUseStorage();
        }

    }

    private void initfolderToSaveTheAudio() {
        // the absolute path to application-specific directory.
        // May return null if shared storage is not currently available.
        folderToSaveTheAudio = ExternalStorageUtil.getPrivateExternalStorageBaseDir(getApplicationContext());
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(folderToSaveTheAudio + "/primeiragravacao.mp4");
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            System.out.println("PREPARE FAILED" + e.getMessage());
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        System.out.println("PARANDO...");
    }
}