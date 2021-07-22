package net.zjueva.minitiktok.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.shuyu.gsyvideoplayer.GSYVideoManager;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.adapter.HomePageAdapter;
import net.zjueva.minitiktok.model.HomeFragmentLab;
import net.zjueva.minitiktok.model.PostResultMessageLab;

public class HomeFragment extends Fragment {

    private static final String TAG="HomeFragment";
    private ViewPager2 mViewPager2;
    //必须使用这玩意建新对象
     public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_home,container,false);
        mViewPager2=v.findViewById(R.id.viewPager2);
        return v;
    }

    //执行完onCreateView立刻执行onViewCreated
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomePageAdapter homePageAdapter=HomePageAdapter.newInstance(this);
        PostResultMessageLab.getData(getActivity(), homePageAdapter);
        mViewPager2.setAdapter(homePageAdapter);
        mViewPager2.setOffscreenPageLimit(1);
        HomeFragmentLab.setViewPager2(mViewPager2);

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
        HomeFragmentLab.removeViewPager2();
    }
}
