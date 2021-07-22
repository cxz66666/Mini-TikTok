package net.zjueva.minitiktok.model;

import androidx.viewpager2.widget.ViewPager2;

//控制是否可以滑动
public class HomeFragmentLab {
    private static ViewPager2 sViewPager2=null;

    public static void setViewPager2(ViewPager2 viewPager2){
        sViewPager2=viewPager2;
    }

    public static void removeViewPager2(){
        sViewPager2=null;
    }

    public static void setEnable(){
        if(sViewPager2!=null){
            sViewPager2.setUserInputEnabled(true);
        }
    }
    public static void setDisable(){
        if(sViewPager2!=null){
            sViewPager2.setUserInputEnabled(false);
        }
    }
}
