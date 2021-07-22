package net.zjueva.minitiktok.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PostResultMessage implements Parcelable {

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

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    @SerializedName("updatedAt")
    String updatedAt;

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

    public int getImage_w() {
        return image_w;
    }

    public void setImage_w(int image_w) {
        this.image_w = image_w;
    }

    public int getImage_h() {
        return image_h;
    }

    public void setImage_h(int image_h) {
        this.image_h = image_h;
    }



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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.studentId);
        dest.writeString(this.userName);
        dest.writeString(this.videoUrl);
        dest.writeString(this.imageUrl);
        dest.writeInt(this.image_w);
        dest.writeInt(this.image_h);
        dest.writeString(this.id);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
    }

    public PostResultMessage() {
    }

    protected PostResultMessage(Parcel in) {
        this.studentId = in.readString();
        this.userName = in.readString();
        this.videoUrl = in.readString();
        this.imageUrl = in.readString();
        this.image_w = in.readInt();
        this.image_h = in.readInt();
        this.id = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
    }

    public static final Parcelable.Creator<PostResultMessage> CREATOR = new Parcelable.Creator<PostResultMessage>() {
        @Override
        public PostResultMessage createFromParcel(Parcel source) {
            return new PostResultMessage(source);
        }

        @Override
        public PostResultMessage[] newArray(int size) {
            return new PostResultMessage[size];
        }
    };
}
