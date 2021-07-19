package net.zjueva.minitiktok.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.transition.Hold;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.model.PostResultMessage;

import java.util.List;


//list页面的adapter
public class TikTokListAdapter extends  RecyclerView.Adapter<TikTokListAdapter.TikTokListViewHolder>{

    private List<PostResultMessage> data;
    private int mId;

    public TikTokListAdapter(List<PostResultMessage> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public TikTokListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item=  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tiktok_list, parent, false);
        return new TikTokListViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull TikTokListViewHolder holder, int position) {
        PostResultMessage item=data.get(position);
        holder.bindItem(item);
        holder.setPos(position);
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }


    public class TikTokListViewHolder extends RecyclerView.ViewHolder {
        private ImageView mThumb;
        private TextView mTitle;
        public int mPosition;


        //设置pos
        public void setPos(int pos){
            mPosition=pos;
        }
        //绑定item
        public void bindItem(PostResultMessage item){
            mTitle.setText(item.getTitle());
            Glide.with(mThumb.getContext())
                    .load(item.getImageUrl())
                    .into(mThumb);
        }
        public TikTokListViewHolder(@NonNull View itemView) {
            super(itemView);
            mThumb = itemView.findViewById(R.id.iv_thumb);
            mTitle = itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 进入播放视频
                }
            });
        }
    }
}
