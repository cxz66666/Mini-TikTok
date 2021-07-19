package net.zjueva.minitiktok.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.model.PostResultMessage;
import net.zjueva.minitiktok.utils.PreloadManager;

import java.util.List;


//使用dvplayer进行播放的播放页面的adapter
public class TikTokVideoAdapter extends RecyclerView.Adapter<TikTokVideoAdapter.ViewHolder>  {
    private List<PostResultMessage>data;

    public TikTokVideoAdapter(List<PostResultMessage> data) {
        this.data = data;
    }
    private static final String TAG = "TikTokAdapter";

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_video_dvplayer, parent, false);
        return new TikTokVideoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostResultMessage item=data.get(position);
        holder.setPos(position);
        PreloadManager.getInstance(holder.itemView.getContext()).addPreloadTask(item.getVideoUrl(),position);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        PostResultMessage item = data.get(holder.getPos());
        PreloadManager.getInstance(holder.itemView.getContext()).removePreloadTask(item.getVideoUrl());
    }


    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private int mPos;
        //TODO 获取页面的详细信息 进行修改
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void setPos(int nowpos){
            mPos=nowpos;
        }
        public int getPos(){
            return mPos;
        }
    }
}
