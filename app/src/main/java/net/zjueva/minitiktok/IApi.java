package net.zjueva.minitiktok;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

import net.zjueva.minitiktok.model.RegisterDownloadResponse;
import net.zjueva.minitiktok.model.RegisterUploadResponse;
import net.zjueva.minitiktok.model.UploadVideoInfo;
import net.zjueva.minitiktok.model.VideoUploadResponse;

public interface IApi {
    @Multipart
    @POST("video")
    Call<VideoUploadResponse> submitVideo(
            @Query("student_id")String studentId,
            @Query("user_name") String userName,
            @Query("extra_value") String extraValue,
            @Part MultipartBody.Part coverImage,
            @Part MultipartBody.Part video,
            @Header("token")    String token);

    @Multipart
    @POST("custom")
    Call<RegisterUploadResponse> submitRegisterInfo(
            @Query("student_id")String studentId,
            @Part MultipartBody.Part data,
            @Header("token") String token);

    @GET("custom")
    Call<RegisterDownloadResponse> getRegisterInfo(
            @Query("student_id")String studentId,
            @Header("token") String token);

}
