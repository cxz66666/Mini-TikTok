package net.zjueva.minitiktok.model;

import com.google.gson.annotations.SerializedName;

public class UploadRegisterInfo {
    @SerializedName("student_id")
    String studentId;
    @SerializedName("token")
    String token;
    @SerializedName("data")
    String data;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
