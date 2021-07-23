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

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;

import net.zjueva.minitiktok.R;

import net.zjueva.minitiktok.fragment.HomeFragment;
import net.zjueva.minitiktok.fragment.TikTokListFragment;
import  net.zjueva.minitiktok.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;


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


//
//        List<VideoOptionModel> list = new ArrayList<>();
//        //关键帧优化
////        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1));
//
//
//        //探测大小
//        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 500));
//        //无线读
//        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "infbuf", 1));
//
//        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", 2));
//        // 跳过循环滤波
//        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48));
//        //最大探测
//        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT,"analyzemaxduration",100));
//        //立即清理数据包
//       list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1));
//        //网络不好就丢包
//       list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1));
//
//        //100帧开始播放
//        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", 50));
//        //预缓冲
//        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"packet-buffering",0));
//
//        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"find_stream_info", 0));
//
//        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzedmaxduration", 100));
//        GSYVideoManager.instance().setOptionModelList(list);



        List<VideoOptionModel> list = new ArrayList<>();
        VideoOptionModel videoOptionMode01 = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fast", 1);//不额外优化
        list.add(videoOptionMode01);
        VideoOptionModel videoOptionMode02 = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 200);//10240
        list.add(videoOptionMode02);
        VideoOptionModel videoOptionMode03 = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1);
        list.add(videoOptionMode03);
//pause output until enough packets have been read after stalling
        VideoOptionModel videoOptionMode04 = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);//是否开启缓冲
        list.add(videoOptionMode04);
//drop frames when cpu is too slow：0-120
        VideoOptionModel videoOptionMode05 = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);//丢帧,太卡可以尝试丢帧
        list.add(videoOptionMode05);
//automatically start playing on prepared
        VideoOptionModel videoOptionMode06 = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);
        list.add(videoOptionMode06);
        VideoOptionModel videoOptionMode07 = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);//默认值48
        list.add(videoOptionMode07);
//max buffer size should be pre-read：默认为15*1024*1024
        VideoOptionModel videoOptionMode11 = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", 0);//最大缓存数
        list.add(videoOptionMode11);
        VideoOptionModel videoOptionMode12 = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", 2);//默认最小帧数2
        list.add(videoOptionMode12);
        VideoOptionModel videoOptionMode13 = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max_cached_duration", 30);//最大缓存时长
        list.add(videoOptionMode13);
//input buffer:don't limit the input buffer size (useful with realtime streams)
        VideoOptionModel videoOptionMode14 = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "infbuf", 1);//是否限制输入缓存数
        list.add(videoOptionMode14);
        VideoOptionModel videoOptionMode15 = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "nobuffer");
        list.add(videoOptionMode15);
        VideoOptionModel videoOptionMode16 = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");//tcp传输数据
        list.add(videoOptionMode16);
        VideoOptionModel videoOptionMode17 = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzedmaxduration", 100);//分析码流时长:默认1024*1000
        list.add(videoOptionMode17);

        GSYVideoManager.instance().setOptionModelList(list);

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
            switch (nowFragment){
                case Constant.home_id:
                    btn_home.setTextColor(getResources().getColor(R.color.color_text_darker));
                    break;
                case Constant.samecity_id:
                    btn_samecity.setTextColor(getResources().getColor(R.color.color_text_darker));
                    break;
                case Constant.message_id:
                    btn_message.setTextColor(getResources().getColor(R.color.color_text_darker));
                    break;
                case Constant.me_id:
                    btn_me.setTextColor(getResources().getColor(R.color.color_text_darker));
                    break;
            }
        }
        //设置粗体
        switch(fragmentIndex){
            case Constant.home_id:
                btn_home.setTextColor(getResources().getColor(R.color.white));
                break;
            case Constant.samecity_id:
                btn_samecity.setTextColor(getResources().getColor(R.color.white));
                break;
            case Constant.message_id:
                btn_message.setTextColor(getResources().getColor(R.color.white));
                break;
            case Constant.me_id:
                btn_me.setTextColor(getResources().getColor(R.color.white));
                break;
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
