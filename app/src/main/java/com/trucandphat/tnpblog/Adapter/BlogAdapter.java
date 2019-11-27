package com.trucandphat.tnpblog.Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.trucandphat.tnpblog.Model.Blog;

import java.util.ArrayList;

public class BlogAdapter extends ArrayAdapter<Blog> {
    private Context context;
    private int resource;
    private ArrayList<Blog> blogList;
    public BlogAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Blog> objects) {
        super(context, resource, objects);
    }
}
