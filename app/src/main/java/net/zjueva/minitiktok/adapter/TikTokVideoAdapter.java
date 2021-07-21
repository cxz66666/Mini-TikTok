package net.zjueva.minitiktok.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.activity.VideoPlayerActivity;
import net.zjueva.minitiktok.fragment.ItemFragment;
import net.zjueva.minitiktok.fragment.VideoFragment;
import net.zjueva.minitiktok.mInterface.GetResultMessageCallback;
import net.zjueva.minitiktok.model.PostResultMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;



//使用dvplayer进行播放的播放页面的adapter
public class TikTokVideoAdapter extends FragmentStateAdapter implements GetResultMessageCallback<PostResultMessage> {
    private Context mContext;

    private List<PostResultMessage> Data;
    private HashSet<Integer> mHashSet;


    public TikTokVideoAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        mContext=fragmentActivity;
        mHashSet=new HashSet<Integer>();
        Data=new ArrayList<>();
    }
    //获取新的实例
    public static TikTokVideoAdapter newInstance(@NonNull FragmentActivity fragmentActivity){
        return new TikTokVideoAdapter(fragmentActivity);
    }

    @Override
    public void setData(List<PostResultMessage> item) {
        Data=item;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        mHashSet.add(position);
        return VideoFragment.newInstance(Data.get(position));
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
