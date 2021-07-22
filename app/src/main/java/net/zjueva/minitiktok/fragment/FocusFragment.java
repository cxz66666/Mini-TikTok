package net.zjueva.minitiktok.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.zjueva.minitiktok.R;
import net.zjueva.minitiktok.model.HomeFragmentLab;
import net.zjueva.minitiktok.model.PostResultMessage;
import net.zjueva.minitiktok.utils.Constant;

public class FocusFragment extends Fragment {
    private static final String URL_EXTRA="URL";

    private PostResultMessage Message;

    public static FocusFragment newInstance(PostResultMessage msg) {

        Bundle args = new Bundle();
        args.putParcelable(URL_EXTRA,msg);
        FocusFragment fragment = new FocusFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Message=getArguments().getParcelable(URL_EXTRA);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_focus, container,false);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeFragmentLab.setDisable();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
