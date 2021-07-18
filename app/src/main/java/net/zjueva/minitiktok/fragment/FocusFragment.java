package net.zjueva.minitiktok.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.utils.Constant;

public class FocusFragment extends Fragment {
    private static final String URL_EXTRA="URL";

    private String URL;

    public static FocusFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString(URL_EXTRA,url);
        FocusFragment fragment = new FocusFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        URL=getArguments().getString(URL_EXTRA);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_focus, container,false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
