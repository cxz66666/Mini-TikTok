package net.zjueva.minitiktok.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.SeekParameters;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.customview.LikeLayout;
import net.zjueva.minitiktok.customview.SampleCoverVideo;
import net.zjueva.minitiktok.model.HomeFragmentLab;
import net.zjueva.minitiktok.model.PostResultMessage;

import de.hdodenhof.circleimageview.CircleImageView;
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;


//播放视频的fragment，
public class VideoFragment extends Fragment {
    private static final String URL_EXTRA="URL";
    private static final String TAG="VideoFragment";
    private PostResultMessage Message;
    private long mCurrentPosition;
    private SampleCoverVideo mVideoPlayer;

    private LikeLayout mLikeLayout;
    private OrientationUtils orientationUtils;
    private boolean isPlay;
    private boolean isPause;

    private CircleImageView mCircleImageView;
    private View mIconHeart;
    private View mIconMessage;
    private View mIconShare;

    private boolean isLike;

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
         isLike=false;//设置不喜欢
         mCurrentPosition=0;
         mVideoPlayer= v.findViewById(R.id.main_player);
         mLikeLayout=v.findViewById(R.id.like_layout);
        mCircleImageView=v.findViewById(R.id.profile_image);
        mIconHeart=v.findViewById(R.id.icon_heart);
        mIconMessage=v.findViewById(R.id.icon_message);
        mIconShare=v.findViewById(R.id.icon_share);
//         mLikeLayout.setclip(false);//设置可以超出边界
         return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"点击头像",Toast.LENGTH_SHORT).show();
            }
        });
        mIconHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"点击爱心",Toast.LENGTH_SHORT).show();
                if(!isLike){
                    mIconHeart.setBackgroundResource(R.drawable.icon_heart_red);
                } else {
                  mIconHeart.setBackgroundResource(R.drawable.icon_heart);
                }
                isLike=!isLike;
            }
        });
        mIconMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"点击信息",Toast.LENGTH_SHORT).show();
            }
        });
        mIconShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"点击分享",Toast.LENGTH_SHORT).show();
            }
        });


        //设置播放器的配置
        mVideoPlayer.getTitleTextView().setVisibility(View.GONE);
        mVideoPlayer.getBackButton().setVisibility(View.GONE);

        orientationUtils = new OrientationUtils(getActivity(), mVideoPlayer);
        orientationUtils.setEnable(false);
        mVideoPlayer.loadCoverImage(Message.getImageUrl(),R.drawable.error);
        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption
                .setIsTouchWiget(true)
                .setUrl(Message.getVideoUrl())
                .setRotateViewAuto(false)
                .setLockLand(true)  //改
                .setLooping(true)
                .setAutoFullWithSize(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setNeedShowWifiTip(false)
                .setShowDragProgressTextOnSeekBar(true)
                .setCacheWithPlay(true)
                .setVideoTitle("测试")
                .setDismissControlTime(1500)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {

                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
//                        orientationUtils.setEnable(mVideoPlayer.isRotateWithSystem());
                        isPlay = true;

                        //设置 seek 的临近帧。
                        if (mVideoPlayer.getGSYVideoManager().getPlayer() instanceof Exo2PlayerManager) {
                            ((Exo2PlayerManager) mVideoPlayer.getGSYVideoManager().getPlayer()).setSeekParameter(SeekParameters.NEXT_SYNC);
                            Log.d(TAG,"***** setSeekParameter **** ");
                        }
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                       Log.d(TAG,"***** onEnterFullscreen **** " + objects[0]);//title
                        Log.d(TAG,"***** onEnterFullscreen **** " + objects[1]);//当前全屏player
                    }

                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);
                    }

                    @Override
                    public void onClickStartError(String url, Object... objects) {
                        super.onClickStartError(url, objects);
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        Log.d(TAG,"***** onQuitFullscreen **** " + objects[0]);//title
                        Log.d(TAG,"***** onQuitFullscreen **** " + objects[1]);//当前非全屏player
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                })
                .setLockClickListener(new LockClickListener() {
                    @Override
                    public void onClick(View view, boolean lock) {
                        if (orientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            orientationUtils.setEnable(!lock);
                        }
                    }
                })
                .setGSYVideoProgressListener(new GSYVideoProgressListener() {
                    @Override
                    public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
                        Log.d(TAG," progress " + progress + " secProgress " + secProgress + " currentPosition " + currentPosition + " duration " + duration);
                    }
                })
                .build(mVideoPlayer);

        mLikeLayout.setLikeLayoutInterface(new LikeLayout.LikeLayoutInterface() {
            @Override
            public void onLikeListener() {
                if(!isLike){
                    isLike=true;
                    mIconHeart.setBackgroundResource(R.drawable.icon_heart_red);
                }
                Log.d(TAG,"double click");
            }

            @Override
            public void onPauseListerner() {
                if(mVideoPlayer.getGSYVideoManager().isPlaying()){
                    mVideoPlayer.onVideoPause();
                } else {
                    mVideoPlayer.onVideoResume(false);
                }
                Log.d(TAG,"perform click");
            }
        });

        Log.d(TAG,Message.getImageUrl()+" "+Message.getVideoUrl());
        //开始设置URL
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeFragmentLab.setEnable();
//        if(mCurrentPosition>0){
//            Log.d(TAG,"onResume current>0");
//            mVideoPlayer.onVideoResume(false);
//        } else {
//            Log.d(TAG,"onResume current==0");
//            mVideoPlayer.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mVideoPlayer.startPlayLogic();
//                }
//            },0);
//        }
        mVideoPlayer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mVideoPlayer.startPlayLogic();
                }
            },0);
        Log.d(TAG,"onResume, currentPos is "+mCurrentPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        mLikeLayout.onPause();
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
        Log.d(TAG,"onDestroy");
        if(mVideoPlayer.getGSYVideoManager().isPlaying()){
            mVideoPlayer.getCurrentPlayer().release();
        }
    }
}
