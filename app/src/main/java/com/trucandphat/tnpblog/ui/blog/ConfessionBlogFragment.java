package com.trucandphat.tnpblog.ui.blog;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trucandphat.tnpblog.Adapter.BlogAdapter;
import com.trucandphat.tnpblog.Model.Blog;
import com.trucandphat.tnpblog.R;

import java.util.ArrayList;


public class ConfessionBlogFragment extends Fragment {
    private ListView mListviewConfessionBlog;
    private ArrayList<Blog> confessionBlogList;
    private DatabaseReference dbReference;
    private BlogAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_confession_blog, container, false);
        setProperties(root);
        loadBlogs();
        return root;
    }
    public void setProperties(View view) {
        mListviewConfessionBlog = view.findViewById(R.id.confession_blog_listview);
        confessionBlogList = new ArrayList<Blog>();
        dbReference = FirebaseDatabase.getInstance().getReference().child("Blog").child("Confession");
        adapter = new BlogAdapter(getActivity(),R.layout.item_blog,confessionBlogList);
        mListviewConfessionBlog.setAdapter(adapter);
    }
    public void loadBlogs() {
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot data : dataSnapshot.getChildren()) {
                        Blog blogItem = data.getValue(Blog.class);
                        confessionBlogList.add(blogItem);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
