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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trucandphat.tnpblog.Model.Blog;
import com.trucandphat.tnpblog.R;

import java.util.ArrayList;

public class EducationBlogFragment extends Fragment {
    private ListView mListviewEduBlog;
    private ArrayList<Blog> eduBlogList;
    private DatabaseReference dbReference;
    private TextView tv;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_education_blog, container, false);
        setProperties(root);
        return root;
    }
    public void setProperties(View view) {
        mListviewEduBlog = view.findViewById(R.id.education_blog_listview);
        eduBlogList = new ArrayList<Blog>();
        dbReference = FirebaseDatabase.getInstance().getReference().child("Diary").child("Education");
        tv = view.findViewById(R.id.tv_education);
        tv.setText("Education");
    }
}
