package com.trucandphat.tnpblog.ui.blog;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trucandphat.tnpblog.R;

public class EntertainmentBlogFragment extends Fragment {
    private TextView tv;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_entertainment_blog, container, false);
        tv = root.findViewById(R.id.tv_entertainment);
        tv.setText("Entertaiment");
        return root;
    }
}
