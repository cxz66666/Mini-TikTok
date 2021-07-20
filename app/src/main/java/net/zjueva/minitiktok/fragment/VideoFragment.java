package net.zjueva.minitiktok.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.model.PostResultMessage;


//播放视频的fragment，
public class VideoFragment extends Fragment {
    private static final String URL_EXTRA="URL";
    private static final String TAG="VideoFragment";
    private PostResultMessage Message;
    private long mCurrentPosition;
    private StandardGSYVideoPlayer mVideoPlayer;


    public static VideoFragment newInstance(PostResultMessage msg) {

        Bundle args = new Bundle();
        args.putParcelable(URL_EXTRA,msg);
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Message=getArguments().getParcelable(URL_EXTRA);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View v=inflater.inflate(R.layout.fragment_video_main,container,false);
         mCurrentPosition=0;
         mVideoPlayer=(StandardGSYVideoPlayer)v.findViewById(R.id.main_player);
         return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置播放器的配置
        mVideoPlayer.getTitleTextView().setVisibility(View.GONE);
        mVideoPlayer.getBackButton().setVisibility(View.GONE);
        mVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoPlayer.startWindowFullscreen(getActivity(),false,true);
            }
        });
        mVideoPlayer.setLooping(true);
        mVideoPlayer.setNeedShowWifiTip(false);

        Log.d(TAG,Message.getImageUrl()+" "+Message.getVideoUrl());
        //开始设置URL
        mVideoPlayer.setUpLazy(Message.getVideoUrl(),false,null,null,"测试");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mCurrentPosition>0){
            mVideoPlayer.onVideoResume(false);
        } else {
            mVideoPlayer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mVideoPlayer.startPlayLogic();
                }
            },200);
        }
        Log.d(TAG,"onResume, currentPos is "+mCurrentPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        mVideoPlayer.onVideoPause();
        mCurrentPosition=mVideoPlayer.getGSYVideoManager().getCurrentPosition();
        Log.d(TAG,"onPause, currentPos is "+mCurrentPosition);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mVideoPlayer.getGSYVideoManager().isPlaying()){
            mVideoPlayer.getCurrentPlayer().release();
        }
    }
}
