package net.zjueva.minitiktok.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.zjueva.minitiktok.IApi;
import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.model.RegisterUserData;
import net.zjueva.minitiktok.utils.Constant;
import net.zjueva.minitiktok.utils.UserUtil;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static net.zjueva.minitiktok.utils.UserUtil.getEditString;

public class LoginActivity extends AppCompatActivity {

    private EditText studentIdEditText;
    private EditText passwordEditText;
    private Button confirmLoginButton;
    private ImageView cancelLoginImageView;

    private IApi api;

    private String studentId;
    private String password;
    private String TAG = "LOGINACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        studentIdEditText = findViewById(R.id.login_student_id);
        passwordEditText = findViewById(R.id.login_password);
        confirmLoginButton = findViewById(R.id.confirm_login);
        cancelLoginImageView = findViewById(R.id.cancel_login);

        initNetwork();
        initButton();
        initImageView();
    }

    private void initImageView() {
        cancelLoginImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initNetwork() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(IApi.class);
    }

    private void initButton() {
        confirmLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentId = getEditString(studentIdEditText);
                password = getEditString(passwordEditText);

                String []checkedAttr = {studentId, password};
                String []errorMsg = {"请输入账号",  "请输入密码"};
                Log.d(TAG, studentId + " " + password);

                for(int i=0;i<checkedAttr.length;i++)
                    if(TextUtils.isEmpty(checkedAttr[i])) {
                        Toast.makeText(LoginActivity.this, errorMsg[i], Toast.LENGTH_SHORT).show();
                        return ;
                    }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<RegisterUserData> userDataList = UserUtil.getUserList(api);
                        if(!UserUtil.userInList(studentId, userDataList)) {
                            toastOnUiThread("用户不存在！");
                            return ;
                        }
                        if(!UserUtil.loginSuccess(studentId, password, userDataList)) {
                            toastOnUiThread("密码错误！");
                            return ;
                        }
                        toastOnUiThread("登录成功！");
                        finish();
                    }
                }).start();

            }
        });
    }

    private void toastOnUiThread(String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
                return ;
            }
        });
    }
}
