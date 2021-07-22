package net.zjueva.minitiktok.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import androidx.viewpager2.adapter.FragmentStateAdapter;

import net.zjueva.minitiktok.fragment.ItemFragment;
import net.zjueva.minitiktok.mInterface.BackToTop;
import net.zjueva.minitiktok.mInterface.GetResultMessageCallback;
import net.zjueva.minitiktok.model.PostResultMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Handler;

public class HomePageAdapter extends FragmentStateAdapter implements GetResultMessageCallback<PostResultMessage> {
    private Context mContext;
    private List<PostResultMessage> Data;
    private HashSet<Integer>mHashSet;
    private BackToTop mBackToTop;

    public HomePageAdapter(@NonNull Fragment fragment, BackToTop backToTop) {
        super(fragment);
        mContext=fragment.getContext();
        mHashSet=new HashSet<Integer>();
        mBackToTop=backToTop;
        Data=new ArrayList<>();
    }
    //获取新的实例
    public static HomePageAdapter newInstance(@NonNull  Fragment fragment, BackToTop backToTop){
        return new HomePageAdapter(fragment,backToTop);
    }


    @Override
    public void setData(List<PostResultMessage> item) {
        if(item.size()==Data.size()){
            return;
        }
        if(Data.size()==0){
            Data.clear();
            Data.addAll(item);
            notifyDataSetChanged();
        } else {
            for(int i=0;i<item.size();i++){
                if(item.get(i).getCreatedAt().equals(Data.get(0).getCreatedAt())){
                    for(int j=0;j<i;j++){
                        Data.add(0,item.get(j));
                    }
                    notifyItemRangeInserted(0,i);
                    mBackToTop.onClickBackToTop(false);
                    break;
                }
            }
        }

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        mHashSet.add(position);
        return ItemFragment.newInstance(Data.get(position),this,mBackToTop);
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    @Override
    public boolean containsItem(long itemId) {
        return mHashSet.contains(itemId);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
