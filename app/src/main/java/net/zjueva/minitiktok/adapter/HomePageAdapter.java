package net.zjueva.minitiktok.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import net.zjueva.minitiktok.fragment.ItemFragment;

import java.util.HashSet;
import java.util.List;

public class HomePageAdapter extends FragmentStateAdapter {
    private Context mContext;

    private List<String> mList;
    private HashSet<Integer>mHashSet;


    public HomePageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        mContext=fragmentActivity;
        mHashSet=new HashSet<Integer>();
    }
    //获取新的实例
    public static HomePageAdapter newInstance(@NonNull FragmentActivity fragmentActivity,List<String>mlist){
        HomePageAdapter homePageAdapter=new HomePageAdapter(fragmentActivity);
        homePageAdapter.setDate(mlist);
        return homePageAdapter;
    }


    //TODO 修改更新数据的方法，现在这样肯定不对
    public void setDate(List<String>mlist){
        mList=mlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        mHashSet.add(position);
        return ItemFragment.newInstance(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
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
