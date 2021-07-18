package net.zjueva.minitiktok.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.utils.Constant;
import net.zjueva.minitiktok.IApi;

import java.io.File;
import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadActivity extends AppCompatActivity {

    private String mp4Path = "";
    private Button commitUploadButton;
    private ImageView leftVideoPreview;
    private ImageView rightVideoPreview;

    private Bitmap leftVideoThumbnail;
    private Bitmap rightVideoThumbnail;

    private IApi api;

    private static final String TAG = "UPLOADACTIVITY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        initButtons();
        leftVideoPreview = (ImageView) findViewById(R.id.left_video_preview);
        rightVideoPreview = (ImageView) findViewById(R.id.right_video_preview);

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
        leftVideoThumbnail = retriever.getFrameAtTime(-1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        if(leftVideoThumbnail == null) leftVideoThumbnail = ThumbnailUtils.createVideoThumbnail(mp4Path, MediaStore.Images.Thumbnails.MINI_KIND);

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

    private void initButtons() {
        commitUploadButton = (Button) findViewById(R.id.video_commit_upload);
        commitUploadButton.setOnClickListener( (View v) -> {
            Log.d(TAG, "commit upload");
            uploadVideo();

            Intent intent = new Intent(UploadActivity.this, MainActivity.class);
            startActivity(intent);
            // TODO: 不知道要不要考虑录像场景的处理
        });
    }

    private void uploadVideo() {
        // TODO: 上传

    }
}
