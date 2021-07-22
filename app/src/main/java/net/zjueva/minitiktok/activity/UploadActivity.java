package net.zjueva.minitiktok.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.model.UploadVideoInfo;
import net.zjueva.minitiktok.model.VideoUploadResponse;
import net.zjueva.minitiktok.utils.Constant;
import net.zjueva.minitiktok.IApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadActivity extends AppCompatActivity {

    private String mp4Path = "";
    private String shareText = "";
    private Button commitUploadButton;
    private ImageView leftVideoPreview;
    private ImageView rightVideoPreview;
    private ImageView onlyVideoPreview;
    private ImageView backIcon;
    private ImageView cancelIcon;

    private LottieAnimationView loadingLottieAnimationView;

    private Bitmap leftVideoThumbnail;
    private Bitmap rightVideoThumbnail;

    private EditText editText;

    private IApi api;

    private SharedPreferences login_info;

    private static final String TAG = "UPLOADACTIVITY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        leftVideoPreview = (ImageView) findViewById(R.id.left_video_preview);
        rightVideoPreview = (ImageView) findViewById(R.id.right_video_preview);
        onlyVideoPreview = (ImageView) findViewById(R.id.only_video_preview);

        backIcon = (ImageView) findViewById(R.id.upload_back_icon);
        cancelIcon = (ImageView) findViewById(R.id.video_cancel_upload);
        commitUploadButton = (Button) findViewById(R.id.video_commit_upload);

        editText = (EditText) findViewById(R.id.share_text);

        loadingLottieAnimationView = findViewById(R.id.upload_loading);

        login_info = getSharedPreferences(Constant.login_status_sp, MODE_PRIVATE);

        initButton();
        initIcon();

        Bundle extras = getIntent().getExtras();
        mp4Path = (String) extras.get("video_path");
        Log.d(TAG, mp4Path);
        File mp4File = new File(mp4Path);

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        Log.d(TAG, String.format("Size: %d", new File(mp4Path).length()));
        retriever.setDataSource(mp4Path);
        String fc = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_FRAME_COUNT);
        Log.d(TAG, String.format("%s, %s", mp4Path, fc));

        initNetwork();


        new Thread(new Runnable() {
            @Override
            public void run() {
                setPreviewImages();
                leftVideoThumbnail = resizeImage(leftVideoThumbnail);
                rightVideoThumbnail = resizeImage(rightVideoThumbnail);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        leftVideoPreview.setImageBitmap(leftVideoThumbnail);
                        rightVideoPreview.setImageBitmap(rightVideoThumbnail);
                        onlyVideoPreview.setImageBitmap(leftVideoThumbnail);
                    }
                });
            }
        }).start();

        /*
            videoThumbnail = getProperThumbnail();
            if(videoThumbnail != null) videoPreview.setImageBitmap(videoThumbnail);
            // API29的时候被标记为deprecated，为了兼容性，使用该API
        */

        /*
        try {
            videoThumbnail = ThumbnailUtils.createVideoThumbnail(mp4File, mSize, new CancellationSignal());
            videoPreview.setImageBitmap(videoThumbnail);
        } catch(IOException e){
            e.printStackTrace();
        }
        */
    }

    public void initIcon() {
        backIcon.setOnClickListener((View v) -> {
            Intent intent = new Intent(UploadActivity.this, VideoActivity.class);
            startActivity(intent);
        });
        cancelIcon.setOnClickListener((View v) -> {
            Intent intent = new Intent(UploadActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void initNetwork() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(IApi.class);
    }

    private void setPreviewImages() {

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(mp4Path);

        /*
        String fc = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_FRAME_COUNT);
        Log.d(TAG, String.format("%s, %s", mp4Path, fc));
        long frameCount = Long.valueOf(fc);
        int centerFrameIndex = (int)(frameCount / 2);
        */

        String duration_s = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long videoDuration = Long.valueOf(duration_s);
        int centerFrameTime = (int)(videoDuration / 2);


        rightVideoThumbnail = retriever.getFrameAtTime(centerFrameTime, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        if(rightVideoThumbnail == null) rightVideoThumbnail = ThumbnailUtils.createVideoThumbnail(mp4Path, MediaStore.Images.Thumbnails.MINI_KIND);
        if(rightVideoThumbnail == null) rightVideoThumbnail = retriever.getFrameAtTime();
        leftVideoThumbnail = retriever.getFrameAtTime(-1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        if(leftVideoThumbnail == null) leftVideoThumbnail = ThumbnailUtils.createVideoThumbnail(mp4Path, MediaStore.Images.Thumbnails.MINI_KIND);
        if(leftVideoThumbnail == null) leftVideoThumbnail = retriever.getFrameAtTime();

        Log.d(TAG, String.format("%s", leftVideoThumbnail.toString()));
        Log.d(TAG, String.format("%s", rightVideoThumbnail.toString()));

    }

    private Bitmap resizeImage(Bitmap bm) {
        int height = bm.getHeight();
        int width = bm.getWidth();
        int dim_height = (int) getResources().getDimension(R.dimen.video_preview_height);
        int dim_width = (int) getResources().getDimension(R.dimen.video_preview_width);

        float scaleHeight = ((float) dim_height) / height;
        float scaleWidth =  ((float) dim_width) / width;
        Log.d(TAG, String.format("%d, %d, %f, %f", height, width, scaleHeight, scaleWidth));
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    private Bitmap getProperThumbnail() {
        Bitmap bm = ThumbnailUtils.createVideoThumbnail(mp4Path, MediaStore.Images.Thumbnails.MINI_KIND);
        if(bm == null) return null;

        int height = bm.getHeight();
        int width = bm.getWidth();
        int dim_height = (int) getResources().getDimension(R.dimen.video_preview_height);
        int dim_width = (int) getResources().getDimension(R.dimen.video_preview_width);

        float scaleHeight = ((float) dim_height) / height;
        float scaleWidth =  ((float) dim_width) / width;
        Log.d(TAG, String.format("%d, %d, %f, %f", height, width, scaleHeight, scaleWidth));
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    private void initButton() {
        commitUploadButton.setOnClickListener( (View v) -> {
            Log.d(TAG, "commit upload");
            new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        uploadVideo(leftVideoThumbnail);
                    }
                }
            ).start();
            // uploadVideo(leftVideoThumbnail);
            // TODO: 选择cover_image（暂时不做）

            // Intent intent = new Intent(UploadActivity.this, MainActivity.class);
            // startActivity(intent);
        });
    }

    private void loadAnimation(int hide) {
        float start = hide == 1 ? 1.0f : 0.0f;
        float end = hide == 1 ? 0.0f : 1.0f;
        new Handler(getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator lottieAnimator = ObjectAnimator.ofFloat(loadingLottieAnimationView,
                                "alpha", start, end);
                        lottieAnimator.setDuration(0);
                        lottieAnimator.setRepeatCount(0);
                        lottieAnimator.start();

                        if(hide == 0) {
                            loadingLottieAnimationView.setProgress(0f);
                            loadingLottieAnimationView.playAnimation();
                        }
                        else loadingLottieAnimationView.pauseAnimation();
                    }
                }
        );
    }

    private void uploadVideo(Bitmap coverImageBitmap) {
        loadAnimation(0);

        String userName = login_info.getString("user_name", "无敌dzp");
        String studentId = login_info.getString("student_id", "3180100000");
        Log.d(TAG, "user_name: " + userName);
        Log.d(TAG, "student_id: " + studentId);
        shareText = editText.getText().toString(); // TODO: 把分享文字给发送出去（暂时不做）
        UploadVideoInfo uploadVideoInfo = composeVideoBody(userName, studentId, mp4Path, coverImageBitmap);

        Call<VideoUploadResponse> response = api.submitVideo(uploadVideoInfo.getStudentId(),
                                                uploadVideoInfo.getUserName(),
                                                "",
                                                uploadVideoInfo.getCoverImage(),
                                                uploadVideoInfo.getVideo(),
                                                uploadVideoInfo.getToken());

        try {
            Call<VideoUploadResponse> uploadCall = api.submitVideo(uploadVideoInfo.getStudentId(),
                    uploadVideoInfo.getUserName(),
                    "",
                    uploadVideoInfo.getCoverImage(),
                    uploadVideoInfo.getVideo(),
                    uploadVideoInfo.getToken());
            Log.d(TAG, "start execute time: " + Long.toString(System.currentTimeMillis()));
            Response<VideoUploadResponse> uploadResponse = uploadCall.execute();
            Log.d(TAG, "finish execute time: " + Long.toString(System.currentTimeMillis()));
            if(uploadResponse.isSuccessful()) {
                // TODO: 停止动画，跳转回去

                toastOnUiThread("上传成功");
                Log.d(TAG, uploadResponse.message());
                Log.d(TAG, "jump time: " + Long.toString(System.currentTimeMillis()));

                Intent intent = new Intent(UploadActivity.this, MainActivity.class);
                // Thread.sleep(1000);
                delayedExecuteIntent(intent, 700);

            }
            else {
                // TODO: 停止动画
                loadAnimation(1);
            }

        } catch(IOException e) {
            e.printStackTrace();
        }

        /*
        response.enqueue(new Callback<VideoUploadResponse>() {
            @Override
            public void onResponse(Call<VideoUploadResponse> call, Response<VideoUploadResponse> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(UploadActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                }
                else {
                    // TODO: 上传成功的延迟显示bug还没修复（感觉要改成同步的，但是这样子体验很差）
                    VideoUploadResponse body = response.body();
                    Log.d(TAG, "success: " + body.success);


                    Toast.makeText(UploadActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, response.message());


                    Handler handler = new Handler(getMainLooper());
                    // 好像这种方法没用
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(UploadActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }, 0);

                }
            }


            @Override
            public void onFailure(Call<VideoUploadResponse> call, Throwable t) {
                Toast.makeText(UploadActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
        */
    }

    void toastOnUiThread(String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "toast time: " + Long.toString(System.currentTimeMillis()));
                Toast.makeText(UploadActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    void delayedExecuteIntent(Intent intent, int millisecond) {
        new Handler(getMainLooper()).postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "start jump at: " + System.currentTimeMillis());
                        loadAnimation(1);
                        startActivity(intent);
                    }
                }, millisecond
        );
    }

    private UploadVideoInfo composeVideoBody(String userName, String studentId, String videoPath, Bitmap coverImageBitmap) {
        UploadVideoInfo result = new UploadVideoInfo();
        result.setToken(Constant.token);
        result.setStudentId(studentId);
        result.setUserName(userName);
        try {
            byte []videoByte = convertInputStreamToBytes(new FileInputStream(new File(videoPath)));
            MultipartBody.Part video = MultipartBody.Part.createFormData("video", "upload.mp4",
                    RequestBody.create(MediaType.parse("multipart/form_data"), videoByte));
            result.setVideo(video);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            coverImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte []coverImageByte = baos.toByteArray();
            MultipartBody.Part coverImage = MultipartBody.Part.createFormData("cover_image", "cover.jpg",
                    RequestBody.create(MediaType.parse("multipart/form_data"), coverImageByte));
            result.setCoverImage(coverImage);

        }catch(IOException e){
            e.printStackTrace();
        }

        return result;
    }

    private static byte[] convertInputStreamToBytes(InputStream is) {
        byte []videoBytes = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // FileInputStream fis = new FileInputStream(new File(videoPath));
            byte []buf = new byte[1024];
            int n;
            while( (n = is.read(buf)) != -1) {
                baos.write(buf, 0,  n);
            }
            videoBytes = baos.toByteArray();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return videoBytes;
    }

    private static byte[] convertBitmapToBytes(Bitmap imageBitmap) {
        return null;
    }
}
