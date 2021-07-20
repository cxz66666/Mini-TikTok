package net.zjueva.minitiktok.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.adapter.TikTokListAdapter;
import net.zjueva.minitiktok.model.PostResultMessage;

import java.util.ArrayList;
import java.util.List;


//用来展示2个Grid布局的图片list
public class TikTokListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<PostResultMessage>data;
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
        data=new ArrayList<>();
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
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mAdapter = new TikTokListAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
    }
}