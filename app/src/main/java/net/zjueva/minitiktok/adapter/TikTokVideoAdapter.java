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
import net.zjueva.minitiktok.model.PostResultMessage;

import java.util.HashSet;
import java.util.List;



//使用dvplayer进行播放的播放页面的adapter
public class TikTokVideoAdapter extends FragmentStateAdapter {
    private Context mContext;

    private List<String> mList;
    private HashSet<Integer> mHashSet;


    public TikTokVideoAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        mContext=fragmentActivity;
        mHashSet=new HashSet<Integer>();
    }
    //获取新的实例
    public static TikTokVideoAdapter newInstance(@NonNull FragmentActivity fragmentActivity, List<String>mlist){
        TikTokVideoAdapter tikTokVideoAdapter=new TikTokVideoAdapter(fragmentActivity);
        tikTokVideoAdapter.setDate(mlist);
        return tikTokVideoAdapter;
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
        return VideoFragment.newInstance(mList.get(position));
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
