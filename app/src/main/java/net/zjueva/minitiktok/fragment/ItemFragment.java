package net.zjueva.minitiktok.fragment;

import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.adapter.ItemViewAdapter;
import net.zjueva.minitiktok.mInterface.BackToTop;
import net.zjueva.minitiktok.mInterface.CloseLottieAnimation;
import net.zjueva.minitiktok.mInterface.GetResultMessageCallback;
import net.zjueva.minitiktok.model.PostResultMessage;
import net.zjueva.minitiktok.model.PostResultMessageLab;

public class ItemFragment extends Fragment {

    private static final String URL_ARGS="URLARGS";

    private static final String TAG="ItemFragment";


    private PostResultMessage Message;
    private ViewPager2 mViewPager2;
    private TabLayout mTabLayout;
    private TabLayoutMediator mediator;
    private View mRefreshIcon;
    private View mToTopIcon;

    private LottieAnimationView loadingLottieAnimationView;

    public GetResultMessageCallback<PostResultMessage> mCb;
    public BackToTop mBackToTop;
    //以下是一些配置
    final String[] tabs = new String[]{"关注", "推荐", "详情"};
    private int activeSize = 20;
    private int normalSize = 17;
    private int activeColor = Color.parseColor("#ffffff"); //选中颜色
    private int normalColor = Color.parseColor("#adb5bd");//正常颜色

    public static ItemFragment newInstance(PostResultMessage msg, GetResultMessageCallback<PostResultMessage>cb, BackToTop backToTop) {
        Bundle args = new Bundle();
        args.putParcelable(URL_ARGS,msg);
        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
        fragment.mCb=cb;
        fragment.mBackToTop=backToTop;
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Message= getArguments().getParcelable(URL_ARGS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_viewpager2,container,false);
        mViewPager2= v.findViewById(R.id.item_viewpager2);
        mTabLayout=v.findViewById(R.id.tab_layout);
        loadingLottieAnimationView = v.findViewById(R.id.upload_loading);
        mRefreshIcon=v.findViewById(R.id.icon_refresh);
        mToTopIcon=v.findViewById(R.id.icon_to_top);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingLottieAnimationView.bringToFront();
        mToTopIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackToTop.onClickBackToTop(true);
            }
        });

        mRefreshIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"点击刷新按钮");
                loadAnimation(0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadAnimation(1);
//                        mBackToTop.onClickBackToTop(); //回到首页
                    }
                },1000);
                PostResultMessageLab.updateData(getContext(), mCb);
            }
        });

        ItemViewAdapter itemViewAdapter=ItemViewAdapter.newInstance(this,Message);


        mViewPager2.setAdapter(itemViewAdapter);
        mViewPager2.registerOnPageChangeCallback(changeCallback);
        mediator = new TabLayoutMediator(mTabLayout, mViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                TextView tabView = new TextView(getActivity());

                int[][] states = new int[2][];
                states[0] = new int[]{android.R.attr.state_selected};
                states[1] = new int[]{};

                int[] colors = new int[]{activeColor, normalColor};
                ColorStateList colorStateList = new ColorStateList(states, colors);
                tabView.setText(tabs[position]);
                tabView.setTextSize(normalSize);
                tabView.setTextColor(colorStateList);
                tabView.setGravity(Gravity.CENTER);
                tab.setCustomView(tabView);
            }
        });
        mediator.attach();
        mViewPager2.setCurrentItem(1,false);
    }
    //用来设置tab的大小
    private ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            //可以来设置选中时tab的大小
            int tabCount = mTabLayout.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                TextView tabView = (TextView) tab.getCustomView();
                if (tab.getPosition() == position) {
                    tabView.setTextSize(activeSize);
                    tabView.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    tabView.setTextSize(normalSize);
                    tabView.setTypeface(Typeface.DEFAULT);
                }
            }
        }
    };

    //在销毁时注销回调
    @Override
    public void onDestroy() {
        mediator.detach();
        mViewPager2.unregisterOnPageChangeCallback(changeCallback);
        super.onDestroy();
    }
    private void loadAnimation(int hide) {
        float start = hide == 1 ? 1.0f : 0.0f;
        float end = hide == 1 ? 0.0f : 1.0f;
        ObjectAnimator lottieAnimator = ObjectAnimator.ofFloat(loadingLottieAnimationView,
                "alpha", start, end);
        lottieAnimator.setDuration(0);
        lottieAnimator.setRepeatCount(0);
        lottieAnimator.start();

        if(hide == 0) {
            loadingLottieAnimationView.setProgress(0f);
            loadingLottieAnimationView.playAnimation();
        }
        else loadingLottieAnimationView.pauseAnimation();
    }


}
