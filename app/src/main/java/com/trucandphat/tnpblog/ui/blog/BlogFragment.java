package com.trucandphat.tnpblog.ui.blog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.trucandphat.tnpblog.R;

public class BlogFragment extends Fragment {

    private BlogViewModel blogViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        blogViewModel =
                ViewModelProviders.of(this).get(BlogViewModel.class);
        View root = inflater.inflate(R.layout.fragment_blogs, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        blogViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}