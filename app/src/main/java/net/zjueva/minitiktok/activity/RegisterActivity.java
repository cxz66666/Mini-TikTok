package net.zjueva.minitiktok.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.zjueva.minitiktok.IApi;
import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.model.RegisterDownloadResponse;
import net.zjueva.minitiktok.model.RegisterUploadResponse;
import net.zjueva.minitiktok.model.RegisterUserData;
import net.zjueva.minitiktok.utils.Constant;
import net.zjueva.minitiktok.utils.JsonUtil;
import net.zjueva.minitiktok.utils.UserUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static net.zjueva.minitiktok.utils.UserUtil.getEditString;
import static net.zjueva.minitiktok.utils.UserUtil.userInList;

public class RegisterActivity extends BaseActivity {


    EditText userNameEditText;
    EditText studentIdEditText;
    EditText passwordEditText;
    EditText passwordAgainEditText;
    Button confirmRegisterButton;
    ImageView cancelRegisterImageView;

    String userName, studentId, password, passwordAgain;
    IApi api;
    String TAG = "REGISTERACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initNetwork();

        userNameEditText = findViewById(R.id.register_user_name);
        studentIdEditText = findViewById(R.id.register_student_id);
        passwordEditText = findViewById(R.id.register_password);
        passwordAgainEditText = findViewById(R.id.register_password_again);
        confirmRegisterButton = findViewById(R.id.confirm_register);
        cancelRegisterImageView = findViewById(R.id.cancel_register);

        initButton();
        initImageView();

    }

    private void initImageView() {
        cancelRegisterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initButton() {
        confirmRegisterButton.setOnClickListener((v) -> {
            if(!validateInput()) return ;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 判断是否有重复的用户，如果没有，就把新用户提交了

                    /*
                    Call<RegisterDownloadResponse> downloadCall = api.getRegisterInfo(Constant.system_uuid, Constant.token);
                    Response<RegisterDownloadResponse> downloadResponse = downloadCall.execute();
                    RegisterDownloadResponse userData = downloadResponse.body();

                    if(userData.data != null) {
                        userDataList = JsonUtil.fromJson(userData.data,
                                new TypeToken<List<RegisterUserData>>(){}.getType());
                    }
                    else {
                        postNewUser(userDataList);
                        return ;
                    }
                    if(userData.data != null) Log.d(TAG, userData.data);
                    */

                    List<RegisterUserData> userDataList = UserUtil.getUserList(api);
                    if(userInList(studentId, userDataList)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "唯一id重复了", Toast.LENGTH_SHORT).show();
                                return ;
                            }
                        });
                    }

                    else postNewUser(userDataList);

                }
            }).start();

        });
    }

    private void initNetwork() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(IApi.class);
    }

    private void postNewUser(List<RegisterUserData> userDataList) {
        RegisterUserData newUser = new RegisterUserData();
        newUser.setPassword(password);
        newUser.setStudentId(studentId);
        newUser.setUserName(userName);

        if(userDataList == null) userDataList = new ArrayList<RegisterUserData>();
        userDataList.add(newUser);

        try{
            Log.d("REGISTER", "post_data: " + JsonUtil.toJson(userDataList));
            MultipartBody.Part postData =
                    MultipartBody.Part.createFormData("data", JsonUtil.toJson(userDataList));

            Call<RegisterUploadResponse> uploadCall =
                    api.submitRegisterInfo(Constant.system_uuid, postData, Constant.token);
            Response<RegisterUploadResponse> uploadResponse = uploadCall.execute();
            if(uploadResponse.isSuccessful()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "注册成功");
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "注册失败，可能是网络不好", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private boolean validateInput() {
        userName = getEditString(userNameEditText);
        studentId = getEditString(studentIdEditText);
        password = getEditString(passwordEditText);
        passwordAgain = getEditString(passwordAgainEditText);
        String []checkedAttr = {studentId, userName, password, passwordAgain};
        String []errorMsg = {"请输入唯一id", "请输入用户名", "请输入密码", "请再次输入密码"};
        Log.d("REGISTERACTIVITY", studentId + " " + userName + " " + password + " " + passwordAgain);


        for(int i=0;i<checkedAttr.length;i++)
            if(TextUtils.isEmpty(checkedAttr[i])) {
                Toast.makeText(RegisterActivity.this, errorMsg[i], Toast.LENGTH_SHORT).show();
                return false;
            }

        if(!password.equals(passwordAgain)) {
            Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }




}
