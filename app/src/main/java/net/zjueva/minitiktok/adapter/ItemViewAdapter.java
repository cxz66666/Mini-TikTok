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
import net.zjueva.minitiktok.model.PostResultMessage;

public class ItemViewAdapter extends FragmentStateAdapter {

    private PostResultMessage Message;
    public ItemViewAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public static ItemViewAdapter newInstance(FragmentActivity fragment,PostResultMessage msg) {

        Bundle args = new Bundle();
        ItemViewAdapter itemViewAdapter = new ItemViewAdapter(fragment);
        itemViewAdapter.setMsg(msg);
        return itemViewAdapter;
    }

    public void setMsg(PostResultMessage msg){
        Message=msg;
        return;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            //TODO 需要通过ViewPager2.setUserInputEnabled(false)禁止滑动 当0或者2 的时候禁止滑动 1的时候开启滑动
            case 0:return FocusFragment.newInstance(Message);
            case 1:return VideoFragment.newInstance(Message);
            case 2:return PersonInfoFragment.newInstance(Message);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
