package net.zjueva.minitiktok.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class BaseActivity extends AppCompatActivity {

    //隐藏Top任务栏
    public void hideTop(){
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        }
    }


    //checkAPPPermission 用于获取权限（外部文件读 外部文件写）
    public void checkAPPPermission(int requestCode){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 先判断有没有权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
            }
        }
    }
}
