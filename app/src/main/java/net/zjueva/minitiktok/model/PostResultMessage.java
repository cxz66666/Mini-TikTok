package net.zjueva.minitiktok.model;

import com.google.gson.annotations.SerializedName;

public class PostResultMessage {
    @SerializedName("student_id")
    String studentId;
    @SerializedName("user_name")
    String userName;
    @SerializedName("video_url")
    String videoUrl;
    @SerializedName("image_url")
    String imageUrl;
    @SerializedName("image_w")
    int image_w;
    @SerializedName("image_h")
    int image_h;
    @SerializedName("_id")
    String id;
    @SerializedName("createdAt")
    String createdAt;
    @SerializedName("updatedAt")
    String updatedAt;

    public String getTitle(){
        return studentId+" "+userName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
