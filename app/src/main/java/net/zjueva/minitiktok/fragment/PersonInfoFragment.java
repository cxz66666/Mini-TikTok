package net.zjueva.minitiktok.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.activity.MeActivity;
import net.zjueva.minitiktok.model.PersonPhotoModel;
import net.zjueva.minitiktok.model.PersonPhotoModelLab;
import net.zjueva.minitiktok.model.PostResultMessage;
import net.zjueva.minitiktok.net.ThumbnailDownloader;
import net.zjueva.minitiktok.utils.Constant;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonInfoFragment extends Fragment {
    private static final String TAG="PersonInfoFragment";
    private static final String URL_EXTRA="URL";

    private PostResultMessage Message;

    private RecyclerView mRecyclerView;

    private ThumbnailDownloader<PhotoHolder>mThumbnailDownloader;

    private IOnMessageClick listener;


    //创建新Fragment必须使用该方法
    public static PersonInfoFragment newInstance(PostResultMessage msg) {

        Bundle args = new Bundle();
        args.putParcelable(URL_EXTRA,msg);
        PersonInfoFragment fragment = new PersonInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Message=getArguments().getParcelable(URL_EXTRA);

        Handler responseHandler=new Handler();
        mThumbnailDownloader=new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownListener(//设置具体回调函数的实现，回调函数的接口写在内部里
                (target, thumbnail) -> {
                    Drawable drawable=new BitmapDrawable(getResources(),thumbnail);
                    target.bindDrawable(drawable);
                }
        );

        mThumbnailDownloader.start();//开始子线程的looper
        mThumbnailDownloader.getLooper();
        Log.d(TAG,"background thread start");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_person_info, container,false);

//        CircleImageView circleImageView=v.findViewById(R.id.avatar_image);
//        circleImageView.bringToFront();//把头像放到前面

        setupView(v);

        mRecyclerView=v.findViewById(R.id.recycle_view_photo);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));//设置layoutmanager
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));//设置decoration
        setupAdapter();//设置adapter
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IOnMessageClick)context;
        }catch(ClassCastException e) {
            throw new ClassCastException("!!");
        }
    }


    private void setupView(View v) {
        SharedPreferences login_info = this.getActivity().getSharedPreferences(Constant.login_status_sp, Context.MODE_PRIVATE);
        TextView studentIdTextView = v.findViewById(R.id.dy_number);
        TextView userNameTextView = v.findViewById(R.id.name);
        Button personInfoFollowButton = v.findViewById(R.id.person_info_follow);

        Log.d(TAG, "thisactivity: " + this.getActivity().toString());
        Log.d(TAG, "meactivity: " + MeActivity.class);
        if(this.getActivity().getClass().equals(MeActivity.class)) {
            userNameTextView.setText(login_info.getString("user_name", "永远的神"));
            studentIdTextView.setText("抖音号：" + login_info.getString("student_id", "uzi_yyds"));
            personInfoFollowButton.setText("登出");
            personInfoFollowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick("logout");
                }
            });

            Log.d(TAG, "user_name: "+login_info.getString("user_name", "123"));
            Log.d(TAG, "student_id" + login_info.getString("student_id", "1"));
        }
    }

    private void setupAdapter(){
        if(isAdded()){ //设置adapter需要确保fragment已经和activity关联
            PersonPhotoModelLab plab= PersonPhotoModelLab.get(getActivity());
            mRecyclerView.setAdapter(new PhotoAdapter(plab.getItems()));
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private class PhotoHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private TextView mTextView;
        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            mImageView=itemView.findViewById(R.id.fragment_image_view);
            mTextView=itemView.findViewById(R.id.like_number);
        }
        //绑定图片
        public void bindDrawable(Drawable drawable){
            mImageView.setImageDrawable(drawable);
        }
        //绑定like数
        public void bindLinkNum(int num){
            mTextView.setText(num+"");
        }
    }
    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder>{
        private List<PersonPhotoModel>mPersonPhotoModels;
        public PhotoAdapter(List<PersonPhotoModel>personPhotoModels) {
            mPersonPhotoModels=personPhotoModels;
        }

        @NonNull
        @Override
        public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            View v=inflater.inflate(R.layout.image_view_item,parent,false);
            return new PhotoHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
            PersonPhotoModel personPhotoModel=mPersonPhotoModels.get(position);
            Drawable placeHolder=getResources().getDrawable(R.drawable.icon_loading);
            holder.bindDrawable(placeHolder);//这里绑定了默认的图片
            holder.bindLinkNum(personPhotoModel.getLikenumber());//绑定like数
            mThumbnailDownloader.queueThumbnail(holder,personPhotoModel.getUrl());//开启任务
        }

        @Override
        public int getItemCount() {
            return mPersonPhotoModels.size();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.quit();
        Log.d(TAG,"background thread destroyed");
    }

    public interface IOnMessageClick {
        void onClick(String text);
    }
}
