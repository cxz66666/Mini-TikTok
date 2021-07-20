package net.zjueva.minitiktok.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.shuyu.gsyvideoplayer.GSYVideoManager;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.adapter.TikTokVideoAdapter;
import net.zjueva.minitiktok.fragment.HomeFragment;
import net.zjueva.minitiktok.fragment.ItemFragment;
import net.zjueva.minitiktok.utils.Constant;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class VideoPlayerActivity extends BaseActivity{

    private static final String TAG="HomeFragment";
    private List<String>urlList;
    private ViewPager2 mViewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        urlList=new ArrayList<>();
        urlList.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        urlList.add("http://vjs.zencdn.net/v/oceans.mp4");
        urlList.add("https://media.w3.org/2010/05/sintel/trailer.mp4");
        urlList.add("https://v-cdn.zjol.com.cn/276984.mp4");
        urlList.add("https://v-cdn.zjol.com.cn/276985.mp4");

        mViewPager2=findViewById(R.id.viewPager2);


        hideTop();

        TikTokVideoAdapter tikTokVideoAdapter=TikTokVideoAdapter.newInstance(this,urlList);
        mViewPager2.setAdapter(tikTokVideoAdapter);
        mViewPager2.setOffscreenPageLimit(3);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"home fragment on resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"home fragment on pause");
        GSYVideoManager.onPause();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"home fragment on destroy");
        GSYVideoManager.releaseAllVideos();
    }


}