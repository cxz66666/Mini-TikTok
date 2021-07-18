package net.zjueva.minitiktok;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import net.zjueva.minitiktok.UploadVideoInfo;

public interface IApi {
    @Multipart
    @POST("video")
    Call<UploadVideoInfo> submitVideo(
            @Query("student_id")String studentId,
            @Query("user_name") String userName,
            @Query("extra_value") String extraValue,
            @Part MultipartBody.Part cover_image,
            @Part MultipartBody.Part video,
            @Header("token")    String token);
}
