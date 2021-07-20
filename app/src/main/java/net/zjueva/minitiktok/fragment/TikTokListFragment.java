package net.zjueva.minitiktok.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.adapter.TikTokListAdapter;
import net.zjueva.minitiktok.model.PostResultMessageLab;
import net.zjueva.minitiktok.utils.DimenUtil;

import java.util.ArrayList;

import me.jingbin.library.decoration.GridSpaceItemDecoration;


//用来展示2个Grid布局的图片list
public class TikTokListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TikTokListAdapter mAdapter;


    public static TikTokListFragment newInstance() {

        Bundle args = new Bundle();

        TikTokListFragment fragment = new TikTokListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tiktok_list, container, false);
        mRecyclerView=v.findViewById(R.id.rv_tiktok);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        mRecyclerView.setLayoutManager(manager);
        int divider = DimenUtil.dp2px(getContext(),4);
        RecyclerView.ItemDecoration gridItemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                int spanIndex = layoutParams.getSpanIndex();
                int position = parent.getChildAdapterPosition(view);
                outRect.bottom = divider;
                if (position == 0 || position == 1) {
                    outRect.top = divider * 2;
                } else {
                    outRect.top = 0;
                }
                if (spanIndex % 2 == 0) {//偶数项
                    outRect.left = divider;
                    outRect.right = divider / 2;
                } else {
                    outRect.left = divider / 2;
                    outRect.right = divider;
                }
            }
        };

        mRecyclerView.addItemDecoration(gridItemDecoration);
        mAdapter = new TikTokListAdapter(new ArrayList<>());
        PostResultMessageLab.getData(getContext(), mAdapter);
        mRecyclerView.setAdapter(mAdapter);

    }
}