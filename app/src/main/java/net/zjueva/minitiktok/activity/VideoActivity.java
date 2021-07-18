package net.zjueva.minitiktok.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.camera2.internal.annotation.CameraExecutor;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import net.zjueva.minitiktok.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VideoActivity extends AppCompatActivity {

    private int CAMERA_PERMISSION = 1001;
    private int AUDIO_PERMISSION = 1;
    private String mp4Path = "";
    private boolean recording = false;

    private Context context;
    private Preview preview;
    private Button recordButton;
    private Button uploadButton;
    private Button cancelButton;
    private PreviewView previewView;
    private VideoCapture videoCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private static final String TAG = "VideoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        previewView = (PreviewView) findViewById(R.id.video_preview);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        recordButton = (Button) findViewById(R.id.video_record);
        uploadButton = (Button) findViewById(R.id.video_upload);
        cancelButton = (Button) findViewById(R.id.video_cancel_upload);
        context = this;

        initButton();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        }
        else {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestAudioPermission();
            }
            else openCameraPreview();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    private void requestCameraPermission() {
        Log.d(TAG, "requestCameraPermission");
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION);
    }

    private void requestAudioPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                AUDIO_PERMISSION);
    }

    private void initButton(){
        uploadButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
        recordButton.setVisibility(View.VISIBLE);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 700);
                Intent intent = new Intent(VideoActivity.this, UploadActivity.class);
                intent.putExtra("video_path", mp4Path);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                recordButton.setVisibility(View.VISIBLE);
            }
        });

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recording == false) {
                    recording = true;
                    recordButton.setText(R.string.end_recording);
                    recordVideo();
                }
                else{
                    recording = false;
                    stopRecord();
                    recordButton.setText(R.string.start_recording);

                    uploadButton.setVisibility(View.VISIBLE);
                    cancelButton.setVisibility(View.VISIBLE);
                    recordButton.setVisibility(View.GONE);
                }
            }
        });
    }

    private void openCameraPreview() {
        Log.d("VideoActivity", "OpenCameraPreview");

        cameraProviderFuture.addListener( () -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                preview = new Preview.Builder().build();
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                videoCapture = new VideoCapture.Builder().build();
                cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, videoCapture, preview);
            }catch(Exception e){
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void recordVideo() {
        mp4Path = getOutputMediaPath();
        Log.d(TAG, "mp4Path" + mp4Path);
        File mp4File = new File(mp4Path);
        VideoCapture.OutputFileOptions outputFileOptions = new VideoCapture.OutputFileOptions.Builder(mp4File).build();
        videoCapture.startRecording(outputFileOptions, Executors.newSingleThreadExecutor(),
                new VideoCapture.OnVideoSavedCallback() {
                    @Override
                    public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                        Log.d(TAG, "视频已保存");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(VideoActivity.this, "视频已保存", Toast.LENGTH_SHORT).show();
                                // VideoActivity.this.finish();
                            }
                        });
                    }

                    @Override
                    public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {

                    }
                });
    }

    private void stopRecord() {
        videoCapture.stopRecording();
    }

    private String getOutputMediaPath() {
        File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir, "VID_" + timeStamp + ".mp4");
        if (!mediaFile.exists()) {
            mediaFile.getParentFile().mkdirs();
        }
        return mediaFile.getAbsolutePath();
    }
}