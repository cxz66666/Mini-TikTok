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
import net.zjueva.minitiktok.model.PostResultMessageLab;
import net.zjueva.minitiktok.utils.Constant;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class VideoPlayerActivity extends BaseActivity{

    private static final String TAG="HomeFragment";
    private static final String POSITION="position";
    //从那里开始的
    private int Pos;
    private ViewPager2 mViewPager2;


    public static Intent newIntent(Context context,int pos){
        Intent i=new Intent(context,VideoPlayerActivity.class);
        i.putExtra(POSITION,pos);
        return i;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        //获取传来的参数
        Pos=getIntent().getIntExtra(POSITION,0);
        mViewPager2=findViewById(R.id.viewPager2);

        TikTokVideoAdapter tikTokVideoAdapter=TikTokVideoAdapter.newInstance(this);
        PostResultMessageLab.getData(this, tikTokVideoAdapter);
        mViewPager2.setAdapter(tikTokVideoAdapter);
        mViewPager2.setOffscreenPageLimit(3);
        mViewPager2.setCurrentItem(Pos);
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