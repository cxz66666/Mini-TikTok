package net.zjueva.minitiktok.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.zjueva.minitiktok.R;

import net.zjueva.minitiktok.fragment.HomeFragment;
import net.zjueva.minitiktok.fragment.TikTokListFragment;
import  net.zjueva.minitiktok.utils.Constant;


public class MainActivity extends BaseActivity {
    private static final int REQUEST_CODE = 1025;
    private TextView btn_home;
    private TextView btn_samecity;
    private TextView btn_message;
    private TextView btn_me;
    private ImageView btn_addvideo;

    //用来标志那个页面 0首页 1同城 3消息 4个人
    private int nowFragment=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        hideTop();
        checkAPPPermission(REQUEST_CODE);
        bindButtonListener();
        updateFragment(HomeFragment.newInstance(),Constant.home_id);
    }

    //设置按钮的监听事件
    private void bindButtonListener(){
        btn_home=(TextView)findViewById(R.id.home);
        btn_samecity=(TextView)findViewById(R.id.same_city);
        btn_message=(TextView)findViewById(R.id.message);
        btn_me=(TextView)findViewById(R.id.me);
        btn_addvideo=(ImageView) findViewById(R.id.add_video_btn);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFragment(HomeFragment.newInstance(),Constant.home_id);
            }
        });
        btn_samecity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFragment(TikTokListFragment.newInstance(),Constant.samecity_id);
            }
        });
        btn_addvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                startActivity(intent);
            }
        });
        btn_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MeActivity.class);
                startActivity(intent);
                // TODO: 完善“我的”界面
            }
        });
    }

    //替换fragment为指定的fragment，设置字体加粗，
    private void updateFragment(Fragment fragment,int fragmentIndex){
        if(nowFragment==fragmentIndex){
            return;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_id,fragment)
                .commit();
        //取消粗体
        if(nowFragment>=0){
            switch (fragmentIndex){
                case Constant.home_id:
                    btn_home.setTextColor(getResources().getColor(R.color.color_simple_text));
                case Constant.samecity_id:
                    btn_samecity.setTextColor(getResources().getColor(R.color.color_simple_text));
                case Constant.message_id:
                    btn_message.setTextColor(getResources().getColor(R.color.color_simple_text));
                case Constant.me_id:
                    btn_me.setTextColor(getResources().getColor(R.color.color_simple_text));
            }
        }
        //设置粗体
        switch(fragmentIndex){
            case Constant.home_id:
                btn_home.setTextColor(getResources().getColor(R.color.white));
            case Constant.samecity_id:
                btn_samecity.setTextColor(getResources().getColor(R.color.white));
            case Constant.message_id:
                btn_message.setTextColor(getResources().getColor(R.color.white));
            case Constant.me_id:
                btn_me.setTextColor(getResources().getColor(R.color.white));
        }
        nowFragment=fragmentIndex;
    }


        //获取权限的回调函数，用于接收用户选择的授予权限
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == REQUEST_CODE) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"存储权限获取成功",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,"存储权限获取失败",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
