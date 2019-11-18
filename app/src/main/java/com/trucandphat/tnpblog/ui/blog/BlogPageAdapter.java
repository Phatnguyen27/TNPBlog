package com.trucandphat.tnpblog.ui.blog;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class BlogPageAdapter extends FragmentStatePagerAdapter {
    private String tabTitles[] = new String[] { "Education", "Confession","Entertainment"};

    public BlogPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new EducationBlogFragment();
            case 1: return new ConfessionBlogFragment();
            case 2: return new EntertainmentBlogFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Education";
            case 1:
                return "Confession";
            case 2:
                return "Entertainment";
                default: return null;
        }
    }
}
