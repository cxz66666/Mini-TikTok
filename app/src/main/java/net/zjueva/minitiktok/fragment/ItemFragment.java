package net.zjueva.minitiktok.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.adapter.ItemViewAdapter;

public class ItemFragment extends Fragment {

    private static final String URL_ARGS="URLARGS";


    private String URL="";
    private ViewPager2 mViewPager2;



    public static ItemFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(URL_ARGS,url);
        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        URL= getArguments().getString(URL_ARGS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_viewpager2,container,false);
        mViewPager2= v.findViewById(R.id.item_viewpager2);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ItemViewAdapter itemViewAdapter=ItemViewAdapter.newInstance(getActivity(),URL);
        mViewPager2.setAdapter(itemViewAdapter);
        mViewPager2.setCurrentItem(1,false);
    }
}
