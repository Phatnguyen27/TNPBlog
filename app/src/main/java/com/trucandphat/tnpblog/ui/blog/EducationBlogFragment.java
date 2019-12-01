package com.trucandphat.tnpblog.ui.blog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trucandphat.tnpblog.Adapter.BlogAdapter;
import com.trucandphat.tnpblog.Model.Blog;
import com.trucandphat.tnpblog.R;

import java.util.ArrayList;

public class EducationBlogFragment extends Fragment {
    private ListView mListviewEduBlog;
    private ArrayList<Blog> eduBlogList;
    private DatabaseReference dbReference;
    private BlogAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_education_blog, container, false);
        setProperties(root);
        loadBlogs();
        mListviewEduBlog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent= new Intent(getContext(),BlogsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Blog_Detail",eduBlogList.get(i));
                intent.putExtra("package", bundle);
                startActivity(intent);
            }
        });
        return root;
    }

    public void setProperties(View view) {
        mListviewEduBlog = view.findViewById(R.id.education_blog_listview);
        eduBlogList = new ArrayList<Blog>();
        dbReference = FirebaseDatabase.getInstance().getReference().child("Blog").child("Education");
        adapter = new BlogAdapter(getContext(),R.layout.item_blog,eduBlogList);
        mListviewEduBlog.setAdapter(adapter);
    }
    public void loadBlogs() {
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot data : dataSnapshot.getChildren()) {
                        Blog blogItem = data.getValue(Blog.class);
                        eduBlogList.add(blogItem);
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
