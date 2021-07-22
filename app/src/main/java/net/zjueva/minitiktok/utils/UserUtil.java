package net.zjueva.minitiktok.utils;

import android.service.autofill.UserData;
import android.util.Log;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;

import net.zjueva.minitiktok.IApi;
import net.zjueva.minitiktok.model.RegisterDownloadResponse;
import net.zjueva.minitiktok.model.RegisterUserData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class UserUtil {
    private static String TAG = "USERUTIL";

    public static String getUserName(String studentId, List<RegisterUserData>data) {
        // 此时已经保证在里面了
        for(RegisterUserData userData: data) {
            if(studentId.equals(userData.getStudentId())) return userData.getUserName();
        }
        return null;
    }

    public static List<RegisterUserData> getUserList(IApi api) {
        List<RegisterUserData> userDataList = new ArrayList<>();
        try{
            Call<RegisterDownloadResponse> downloadCall = api.getRegisterInfo(Constant.system_uuid, Constant.token);
            Response<RegisterDownloadResponse> downloadResponse = downloadCall.execute();
            RegisterDownloadResponse userData = downloadResponse.body();

            if(userData.data != null) Log.d(TAG, userData.data);
            else Log.d(TAG, "userdata is null!");

            if(userData.data != null) {
                userDataList = JsonUtil.fromJson(userData.data,
                        new TypeToken<List<RegisterUserData>>(){}.getType());
            }
        }catch(IOException e) {
            Log.d(TAG, "ERROR!");
            e.printStackTrace();
        }
        finally {
            Log.d(TAG, "Finish! " + userDataList.size());
        }
        return userDataList;
    }

    public static boolean userInList(String studentId, List<RegisterUserData>data) {
        if(data == null || data.isEmpty()) return true;
        for(RegisterUserData user : data) {
            if(studentId.equals(user.getStudentId())) {
                return true;
            }
            Log.d(TAG, user.getStudentId() + " " + user.getUserName() + " " + user.getPassword());
        }

        return false;
    }

    public static boolean loginSuccess(String studentId, String password, List<RegisterUserData>data) {
        if(data == null || data.isEmpty()) return false;

        for(RegisterUserData user : data) {
            if(studentId.equals(user.getStudentId()) && password.equals(user.getPassword())) {
                return true;
            }
            // Log.d(TAG, user.getStudentId() + " " + user.getUserName() + " " + user.getPassword());
        }
        return false;
    }

    public static String getEditString(EditText et) {
        return et.getText().toString().trim();
    }
}
