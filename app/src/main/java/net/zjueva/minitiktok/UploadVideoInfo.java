package net.zjueva.minitiktok;

import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;

public class UploadVideoInfo {
    @SerializedName("student_id")
    private String studentId;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("cover_image")
    private MultipartBody.Part coverImage;
    @SerializedName("video")
    private MultipartBody.Part video;
    @SerializedName("token")
    private String token;


    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public MultipartBody.Part getCover_image() {
        return coverImage;
    }

    public void setCover_image(MultipartBody.Part cover_image) {
        this.coverImage = cover_image;
    }

    public MultipartBody.Part getVideo() {
        return video;
    }

    public void setVideo(MultipartBody.Part video) {
        this.video = video;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

/*
@Query("student_id")  String studentId,
            @Query("user_name") String userName,
            @Query("extra_value") String extraValue,
            @Part MultipartBody.Part cover_image,
            @Part                 MultipartBody.Part video,
            @Header("token")      String token);
 */

