package net.zjueva.minitiktok.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadActivity extends AppCompatActivity {

    private String mp4Path = "";
    private Button commitUploadButton;
    private ImageView leftVideoPreview;
    private ImageView rightVideoPreview;
    private ImageView onlyVideoPreview;

    private ImageView backIcon;
    private ImageView cancelIcon;

    private Bitmap leftVideoThumbnail;
    private Bitmap rightVideoThumbnail;

    private IApi api;

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
            // TODO: 要不要考虑加一个默认的图片上去（虽然好像没啥用）
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
            uploadVideo(leftVideoThumbnail);
            // TODO: 选择cover_image（这个暂时不实现）

            Intent intent = new Intent(UploadActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void uploadVideo(Bitmap coverImageBitmap) {
        // TODO: 上传的studentid和username哪里来
        String userName = "";
        String studentId = "";
        UploadVideoInfo uploadVideoInfo = composeVideoBody(userName, studentId, mp4Path, coverImageBitmap);

        Call<VideoUploadResponse> response = api.submitVideo(uploadVideoInfo.getStudentId(),
                                                uploadVideoInfo.getUserName(),
                                                "",
                                                uploadVideoInfo.getCoverImage(),
                                                uploadVideoInfo.getVideo(),
                                                uploadVideoInfo.getToken());

        response.enqueue(new Callback<VideoUploadResponse>() {
            @Override
            public void onResponse(Call<VideoUploadResponse> call, Response<VideoUploadResponse> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(UploadActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(UploadActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, response.message());
                    Intent intent = new Intent(UploadActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<VideoUploadResponse> call, Throwable t) {
                Toast.makeText(UploadActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

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
            MultipartBody.Part coverImage = MultipartBody.Part.createFormData("video", "upload.mp4",
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
