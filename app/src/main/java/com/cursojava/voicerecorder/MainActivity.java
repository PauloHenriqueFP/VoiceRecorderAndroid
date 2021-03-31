package com.cursojava.voicerecorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private ImageView micImage;
    private boolean isRecording;

    private final int REQUEST_AUDIO_RECORDER_CODE = 200;
    private String[] permissions = {
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isRecording = false;
        micImage = findViewById(R.id.microphone_img);
        micImage.setOnClickListener(v -> onClickMic());
    }

    private void onClickMic() {
        if (isPermissionToRecord()) {
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

    private void requestPermissionToRecord() {
        ActivityCompat.requestPermissions(
                this,
                permissions,
                REQUEST_AUDIO_RECORDER_CODE
        );
    }

    private boolean isPermissionToRecord() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_AUDIO_RECORDER_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startRecording();
        } else {
            System.out.println("Não PODE GRAVAR");
        }
    }

    private void startRecording() {
        System.out.println("GRAVANDO...");
    }

    private void stopRecording() {
        System.out.println("PARANDO...");
    }
}