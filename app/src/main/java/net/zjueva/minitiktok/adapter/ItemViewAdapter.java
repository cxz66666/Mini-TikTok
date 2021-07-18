package net.zjueva.minitiktok.adapter;

import android.app.Person;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import net.zjueva.minitiktok.fragment.FocusFragment;
import net.zjueva.minitiktok.fragment.PersonInfoFragment;
import net.zjueva.minitiktok.fragment.VideoFragment;

public class ItemViewAdapter extends FragmentStateAdapter {

    private String URL;
    public ItemViewAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public static ItemViewAdapter newInstance(FragmentActivity fragment,String url) {

        Bundle args = new Bundle();
        ItemViewAdapter itemViewAdapter = new ItemViewAdapter(fragment);
        itemViewAdapter.setURL(url);
        return itemViewAdapter;
    }

    public void setURL(String url){
        URL=url;
        return;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            //TODO 需要通过ViewPager2.setUserInputEnabled(false)禁止滑动 当0或者2 的时候禁止滑动 1的时候开启滑动
            case 0:return FocusFragment.newInstance(URL);
            case 1:return VideoFragment.newInstance(URL);
            case 2:return PersonInfoFragment.newInstance(URL);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
