package net.zjueva.minitiktok.activity;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.fragment.PersonInfoFragment;
import net.zjueva.minitiktok.model.PostResultMessage;
import net.zjueva.minitiktok.utils.Constant;
import net.zjueva.minitiktok.utils.UserUtil;

public class MeActivity extends BaseActivity implements PersonInfoFragment.IOnMessageClick{

    private Button meLoginButton;
    private Button meRegisterButton;
    private Button meLogoutButton;
    private ImageView meCancelImageView;
    SharedPreferences login_info; // 包含student_id, user_name, login_status
    boolean login_status;
    SharedPreferences.Editor login_info_editor;
    private PersonInfoFragment personInfoFragment;

    private String TAG = "MEACTIVITY";
    private int INTENT_LOGIN = 1;
    private int INTENT_REGISTER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "oncreate");

        setContentView(R.layout.activity_me);

        meLoginButton = findViewById(R.id.me_login);
        meLogoutButton = findViewById(R.id.me_logout);
        meRegisterButton = findViewById(R.id.me_register);
        meCancelImageView = findViewById(R.id.cancel_me);
        initView();
    }

    private void initView() {
        login_info = this.getSharedPreferences(Constant.login_status_sp, MODE_PRIVATE);
        login_status = login_info.getBoolean("login_status", false);
        login_info_editor = login_info.edit();
        Log.d(TAG, "login_status: " + (login_status == true ? "true" : "false"));
        Log.d(TAG, "student_id: " + login_info.getString("student_id", "uzi_yyds"));

        if(login_status) initFragment();
        initButton();
        initImageView();
    }

    private void initImageView() {
        meCancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initFragment() {
        PostResultMessage postResultMessage = new PostResultMessage();
        postResultMessage.setStudentId(login_info.getString("student_id", "uzi_yyds"));
        postResultMessage.setUserName(login_info.getString("user_name", "uzi"));

        personInfoFragment = PersonInfoFragment.newInstance(postResultMessage);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.me_info_fragment, personInfoFragment)
                .commit();
        /*
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.me_info_fragment, personInfoFragment);
        fragmentTransaction.commit();
        */
    }

    private void initButton() {
        meLogoutButton.setVisibility(View.GONE); // 这个button是没有用的
        if(login_status) {
            meLoginButton.setVisibility(View.GONE);
            meRegisterButton.setVisibility(View.GONE);
        }
        else {
            meLoginButton.setVisibility(View.VISIBLE);
            meRegisterButton.setVisibility(View.VISIBLE);
        }

        /*
        meLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                meLoginButton.setVisibility(View.VISIBLE);
                meRegisterButton.setVisibility(View.VISIBLE);

                login_info_editor.putString("student_id", null);
                login_info_editor.putString("user_name", null);
                login_info_editor.putBoolean("login_status", false);
                login_info_editor.commit();

                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(personInfoFragment)
                        .commit();

            }
        });*/

        meLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeActivity.this, LoginActivity.class);
                startActivityForResult(intent, INTENT_LOGIN);
            }
        });

        meRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // String result = data.getExtras().getString("result");
        initView();
        // Log.d(TAG, result);
    }

    public void onClick(String text) {
        meLoginButton.setVisibility(View.VISIBLE);
        meRegisterButton.setVisibility(View.VISIBLE);

        login_info_editor.putString("student_id", null);
        login_info_editor.putString("user_name", null);
        login_info_editor.putBoolean("login_status", false);
        login_info_editor.commit();

        getSupportFragmentManager()
                .beginTransaction()
                .remove(personInfoFragment)
                .commit();
    }
}
