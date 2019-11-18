package com.trucandphat.tnpblog.ui.blog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.trucandphat.tnpblog.R;

public class BlogFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_blogs, container, false);
        BottomNavigationView bottomNav = root.findViewById(R.id.top_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);
        getFragmentManager().beginTransaction().replace(R.id.blog_fragment_container, new BlogStudyFragment()).commit();
        return root;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    FragmentManager t = getFragmentManager();
                    String fragTag="";
                    Fragment selectedFragment = null;
                    if(t.findFragmentByTag("Study") != null){
                        //if the other fragment is visible, hide it.
                        t.beginTransaction().hide(t.findFragmentByTag("Study")).commit();
                    }
                    if(t.findFragmentByTag("Love") != null){
                        //if the other fragment is visible, hide it.
                        t.beginTransaction().hide(t.findFragmentByTag("Love")).commit();
                    }
                    if(t.findFragmentByTag("Entertainment") != null){
                        //if the other fragment is visible, hide it.
                        t.beginTransaction().hide(t.findFragmentByTag("Entertainment")).commit();
                    }

                    switch (menuItem.getItemId()){
                        case R.id.nav_study:
                            selectedFragment = new BlogStudyFragment();
                            fragTag="Study";
                            break;
                        case R.id.nav_love:
                            selectedFragment = new BlogLoveFragment();
                            fragTag="Love";
                            break;
                        case R.id.nav_entertainment:
                            selectedFragment = new BlogEntertainmentFragment();
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