package net.zjueva.minitiktok.model;

import com.google.gson.annotations.SerializedName;

public class VideoUploadResponse {
    @SerializedName("result")
    PostResultMessage postResultMessage;
    @SerializedName("url")
    String url;
    @SerializedName("success")
    public boolean success;
    @SerializedName("error")
    public String error;
}
