package com.trucandphat.tnpblog.ui.blog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.trucandphat.tnpblog.R;

import java.util.List;

public class BlogFragment extends Fragment {
    private ViewPager mViewPager;
    public static final int RequestCode_View = 763;
    //private TabLayout tabLayout;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_blogs, container, false);
        //tabLayout = root.findViewById(R.id.blog_tabLayout);
        mViewPager = root.findViewById(R.id.blog_pager);
        //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        mViewPager.setAdapter(new BlogPageAdapter(getFragmentManager()));
        //tabLayout.setupWithViewPager(mViewPager);
        return root;
    }
}