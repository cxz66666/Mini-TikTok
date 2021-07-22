package net.zjueva.minitiktok.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.camera2.internal.annotation.CameraExecutor;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraProvider;
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
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.common.util.concurrent.ListenableFuture;

import net.zjueva.minitiktok.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VideoActivity extends AppCompatActivity {

    private int CAMERA_AUDIO_PERMISSION = 1001;
    private int AUDIO_PERMISSION = 1;
    private int WRITE_PERMISSION = 1025;
    private String mp4Path = "";
    private boolean recording = false;
    private boolean usingBackCamera = true;
    private boolean finishRecording = false;
    private long timerStart;


    private Context context;
    private Preview preview;
    private TextView timerTextView;
    private ImageView closePreviewImage;
    private ImageView changeCameraImage;
    private LottieAnimationView lottieProgressBarView;
    private LottieAnimationView lottieStartRecordingView;
    private Button recordButton;
    private Button uploadButton;
    private Button cancelButton;
    private PreviewView previewView;
    private VideoCapture videoCapture;
    private CameraSelector cameraSelector;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ProcessCameraProvider cameraProvider;
    private static final String TAG = "VideoActivity";

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long currTime = System.currentTimeMillis();
            long timeElapsedMilli = currTime - timerStart;
            Log.d("TIMER", "" + timeElapsedMilli);
            if(timeElapsedMilli <= 10000)
            {
                timerHandler.postDelayed(this, 10);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerTextView.setText(String.format("%.01f秒", (float)timeElapsedMilli / 1000));
                    }
                });
            }
            else {
                stopRecord();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        previewView = (PreviewView) findViewById(R.id.video_preview);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        recordButton = (Button) findViewById(R.id.video_record);
        uploadButton = (Button) findViewById(R.id.video_upload);
        cancelButton = (Button) findViewById(R.id.video_cancel_recording);
        closePreviewImage = (ImageView) findViewById(R.id.close_recording_image);
        changeCameraImage = (ImageView) findViewById(R.id.change_camera);
        timerTextView = (TextView) findViewById(R.id.timer_text);
        lottieProgressBarView = (LottieAnimationView) findViewById(R.id.lottie_progress_bar);
        lottieStartRecordingView = (LottieAnimationView) findViewById(R.id.start_recording_lottie);
        lottieStartRecordingView.setMinAndMaxFrame(28, 100);


        finishRecording = false;

        timerTextView.setAlpha(0.0f);

        initButton();
        initImageButton();
        initLottieView();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                    CAMERA_AUDIO_PERMISSION);
            /*
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                requestCameraPermission();
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
                requestAudioPermission();
             */
        }
        openCameraPreview();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "On new intent: " + cameraProvider.toString());
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed () {

    }


    private void initImageButton() {
        closePreviewImage.setOnClickListener((View v) -> {
            Intent intent = new Intent(VideoActivity.this, MainActivity.class);
            startActivity(intent);
        });

        changeCameraImage.setOnClickListener((View v) -> {
            if(recording) {
                Toast.makeText(VideoActivity.this, "你需要先停止录制", Toast.LENGTH_SHORT).show();
            }
            else {
                if(finishRecording) {
                    Toast.makeText(VideoActivity.this, "你需要先选择是否上传", Toast.LENGTH_SHORT).show();
                }
                else {
                    usingBackCamera = !usingBackCamera;
                    bindPreview();
                }
            }
        });
    }

    private void initLottieView() {
        lottieStartRecordingView.setVisibility(View.VISIBLE);
        lottieStartRecordingView.setRepeatMode(LottieDrawable.RESTART);
        lottieStartRecordingView.setRepeatCount(0);


        lottieStartRecordingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!recording) {

                    lottieStartRecordingView.setProgress(0.28f);
                    lottieStartRecordingView.playAnimation();
                    recordVideo();
                    recording = true;

                    timerTextView.setAlpha(1f);
                    setLottieAnimation(0);
                    timerStart = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 1);
                    Log.d(TAG, "Start time: " + timerStart);
                }
                else {
                    Log.d(TAG, "stopRecord");
                    stopRecord();
                }
            }
        });
    }

    private void initButton(){
        uploadButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
        recordButton.setVisibility(View.GONE);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishRecording = false;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "delayed");
                    }
                }, 700);
                Intent intent = new Intent(VideoActivity.this, UploadActivity.class);
                intent.putExtra("video_path", mp4Path);
                Log.d(TAG, mp4Path);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishRecording = false;
                recording = false;
                if(!cameraProvider.isBound(preview))  bindPreview();
                uploadButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                lottieStartRecordingView.setVisibility(View.VISIBLE);
                lottieStartRecordingView.setProgress(0.28f);

            }
        });
/*
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recording == false) {
                    recording = true;
                    recordVideo();
                    timerTextView.setAlpha(1f);

                    setLottieAnimation(0);
                    timerStart = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 1);
                    Log.d(TAG, "Start time: " + timerStart);

                }
                else{
                    stopRecord();
                }
            }
        });
*/
    }

    private void setLottieAnimation(int hide) {
        float start = hide == 1 ? 1.0f : 0.0f;
        float end = hide == 1 ? 0.0f : 1.0f;
        ObjectAnimator lottieProgressBarAnimator = ObjectAnimator.ofFloat(lottieProgressBarView,
                "alpha", start, end);

        lottieProgressBarAnimator.setDuration(0);
        lottieProgressBarAnimator.setRepeatCount(0);
        lottieProgressBarAnimator.start();
        if(hide == 0) {
            lottieProgressBarView.setProgress(0f);
            lottieProgressBarView.playAnimation();
        }
        else lottieProgressBarView.pauseAnimation();
    }


    private void bindPreview() {
        cameraProvider.unbindAll();
        Preview preview = new Preview.Builder()
                .build();

        int lensFacing = (usingBackCamera == true) ? CameraSelector.LENS_FACING_BACK : CameraSelector.LENS_FACING_FRONT;
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, videoCapture);
    }


    private void openCameraPreview() {
        Log.d("VideoActivity", "OpenCameraPreview");

        cameraProviderFuture.addListener( () -> {
            try {
                // ProcessCameraProvider
                cameraProvider = cameraProviderFuture.get();
                preview = new Preview.Builder().build();
                cameraSelector = new CameraSelector.Builder()
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

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this
                    ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    ,WRITE_PERMISSION );
        }

        videoCapture.startRecording(outputFileOptions, Executors.newSingleThreadExecutor(),
                new VideoCapture.OnVideoSavedCallback() {
                    @Override
                    public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                        Log.d(TAG, "视频已保存");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(VideoActivity.this, "视频已保存", Toast.LENGTH_SHORT).show();
                                VideoActivity.this.finish();
                            }
                        });
                    }

                    @Override
                    public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {

                    }
                });
    }

    private void stopRecord() {
        timerHandler.removeCallbacks(timerRunnable);
        videoCapture.stopRecording();
        finishRecording = true;

        setLottieAnimation(1);
        timerTextView.setAlpha(0f);
        cameraProvider.unbindAll();
        recording = false;

        // Camera会自己释放，不用进一步处理

        Log.d(TAG, ""+lottieStartRecordingView.getProgress());;


        new Handler(getMainLooper()).postDelayed(() -> {
                    uploadButton.setVisibility(View.VISIBLE);
                    cancelButton.setVisibility(View.VISIBLE);
                    lottieStartRecordingView.setVisibility(View.GONE);
                    lottieStartRecordingView.setProgress(0.28f);
                }, 0
        );
    }

    private void playRecordButtonAnimation(boolean reverse) {
        lottieStartRecordingView.clearAnimation();
        lottieStartRecordingView.setAnimation(R.raw.start_recording_center);
        if(reverse) {
            lottieStartRecordingView.setProgress(1f);
            lottieStartRecordingView.setMinProgress(0.28f);
            lottieStartRecordingView.reverseAnimationSpeed();
        }
        else {
            lottieStartRecordingView.setProgress(0.28f);
            lottieStartRecordingView.setMaxProgress(1f);
        }

        lottieStartRecordingView.setRepeatCount(0);
        lottieStartRecordingView.playAnimation();
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