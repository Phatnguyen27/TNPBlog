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

public class EntertainmentBlogFragment extends Fragment {
    private ListView mListviewEntertainmentBlog;
    private ArrayList<Blog> enteitainmentBlogList;
    private DatabaseReference dbReference;
    private BlogAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_entertainment_blog, container, false);
        setProperties(root);
        loadBlogs();
        return root;
    }
    public void setProperties(View view) {
        mListviewEntertainmentBlog = view.findViewById(R.id.entertainment_blog_listview);
        enteitainmentBlogList = new ArrayList<Blog>();
        dbReference = FirebaseDatabase.getInstance().getReference().child("Diary").child("entertainment");
    }
    public void loadBlogs() {
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot data : dataSnapshot.getChildren()) {
                        Blog blogItem = data.getValue(Blog.class);
                        enteitainmentBlogList.add(blogItem);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adapter = new BlogAdapter(getActivity(),R.layout.item_blog,enteitainmentBlogList);
        mListviewEntertainmentBlog.setAdapter(adapter);
    }
}
