package net.zjueva.minitiktok.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.google.android.material.transition.Hold;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.activity.VideoPlayerActivity;
import net.zjueva.minitiktok.mInterface.GetResultMessageCallback;
import net.zjueva.minitiktok.model.PostResultMessage;

import java.util.List;

import static androidx.appcompat.widget.AppCompatDrawableManager.preload;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


//list页面的adapter
public class TikTokListAdapter extends  RecyclerView.Adapter<TikTokListAdapter.TikTokListViewHolder> implements GetResultMessageCallback<PostResultMessage> {
    private static final String TAG = "TikTokListAdapter";
    private List<PostResultMessage> data;
    private int mId;

    public static final int Pre_Num = 6;
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

        int width=holder.mThumb.getContext().getResources().getDisplayMetrics().widthPixels/2;
        int height=(int)(width*(float)item.getImage_h()/(float)item.getImage_w());
        ViewGroup.LayoutParams layoutParams =  holder.mThumb.getLayoutParams();
        layoutParams.height=height;
        holder.mThumb.setLayoutParams(layoutParams);
        holder.setPos(position);
        holder.bindItem(item);

    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    public void setData(List<PostResultMessage>item){
        data=item;
        notifyDataSetChanged();
    }

    public class TikTokListViewHolder extends RecyclerView.ViewHolder {
        public ImageView mThumb;
        private TextView mTitle;
        public int mPosition;


        //设置pos
        public void setPos(int pos){
            mPosition=pos;
        }
        //绑定item
        public void bindItem(PostResultMessage item){


//            mTitle.setText(item.getTitle());
            Glide.with(mThumb.getContext())
                    .load(item.getImageUrl())
                    .placeholder(new ColorDrawable(Color.GRAY))
                    .diskCacheStrategy(DiskCacheStrategy.DATA )
                    .error(R.drawable.icon_error)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mThumb);

            if(mPosition+Pre_Num<getItemCount()){
                String next_url=data.get(mPosition+Pre_Num).getImageUrl();
                Glide.with(mThumb.getContext()).load(next_url).diskCacheStrategy(DiskCacheStrategy.DATA).preload();
            }
        }
        public TikTokListViewHolder(@NonNull View itemView) {
            super(itemView);
            mThumb = itemView.findViewById(R.id.iv_thumb);
            mTitle = itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 进入播放视频
                    Intent i= VideoPlayerActivity.newIntent(itemView.getContext(),mPosition);
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}
