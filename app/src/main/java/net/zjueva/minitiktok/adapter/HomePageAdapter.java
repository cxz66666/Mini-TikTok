package net.zjueva.minitiktok.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import net.zjueva.minitiktok.fragment.ItemFragment;
import net.zjueva.minitiktok.mInterface.GetResultMessageCallback;
import net.zjueva.minitiktok.model.PostResultMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class HomePageAdapter extends FragmentStateAdapter implements GetResultMessageCallback<PostResultMessage> {
    private Context mContext;
    private List<PostResultMessage> Data;
    private HashSet<Integer>mHashSet;


    public HomePageAdapter(@NonNull Fragment fragment) {
        super(fragment);
        mContext=fragment.getContext();
        mHashSet=new HashSet<Integer>();
        Data=new ArrayList<>();
    }
    //获取新的实例
    public static HomePageAdapter newInstance(@NonNull  Fragment fragment){
        HomePageAdapter homePageAdapter=new HomePageAdapter(fragment);
        return homePageAdapter;
    }


    @Override
    public void setData(List<PostResultMessage> item) {
        Data =item;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        mHashSet.add(position);
        return ItemFragment.newInstance(Data.get(position));
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
