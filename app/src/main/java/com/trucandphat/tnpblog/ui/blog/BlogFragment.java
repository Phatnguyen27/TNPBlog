package com.trucandphat.tnpblog.ui.blog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.trucandphat.tnpblog.R;

public class BlogFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_blogs, container, false);
        BottomNavigationView bottomNav = root.findViewById(R.id.top_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);
        getFragmentManager().beginTransaction().replace(R.id.blog_fragment_container, new EducationBlogFragment()).commit();
        return root;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    FragmentManager t = getFragmentManager();
                    String fragTag="";
                    Fragment selectedFragment = null;
                    if(t.findFragmentByTag("Education") != null){
                        //if the other fragment is visible, hide it.
                        t.beginTransaction().hide(t.findFragmentByTag("Education")).commit();
                    }
                    if(t.findFragmentByTag("Confession") != null){
                        //if the other fragment is visible, hide it.
                        t.beginTransaction().hide(t.findFragmentByTag("Confession")).commit();
                    }
                    if(t.findFragmentByTag("Entertainment") != null){
                        //if the other fragment is visible, hide it.
                        t.beginTransaction().hide(t.findFragmentByTag("Entertainment")).commit();
                    }

                    switch (menuItem.getItemId()){
                        case R.id.nav_study:
                            selectedFragment = new EducationBlogFragment();
                            fragTag="Education";
                            break;
                        case R.id.nav_love:
                            selectedFragment = new ConfessionBlogFragment();
                            fragTag="Confession";
                            break;
                        case R.id.nav_entertainment:
                            selectedFragment = new EntertainmentBlogFragment();
                            fragTag="Entertainment";
                            break;
                    }

                    if(getFragmentManager().findFragmentByTag(fragTag)!=null)
                        t.beginTransaction().show(getFragmentManager().findFragmentByTag(fragTag)).commit();
                    else
                        t.beginTransaction().add(R.id.blog_fragment_container, selectedFragment,fragTag).addToBackStack(null).commit();
                    return true;
                }
            };
}