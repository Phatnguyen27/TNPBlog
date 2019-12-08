package com.trucandphat.tnpblog.ui.blog;

import android.app.Activity;
import android.app.ProgressDialog;
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
    private ProgressDialog loadingDialog;
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
        dbReference = FirebaseDatabase.getInstance().getReference().child("Blog").child("Entertainment");
        adapter = new BlogAdapter(getContext(),R.layout.item_blog,enteitainmentBlogList);
        mListviewEntertainmentBlog.setAdapter(adapter);
        mListviewEntertainmentBlog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),BlogsDetailActivity.class);
                intent.putExtra("BlogCategory","Entertainment");
                intent.putExtra("BlogId",enteitainmentBlogList.get(position).getId());
                startActivityForResult(intent,BlogFragment.RequestCode_View);
            }
        });
    }
    public void loadBlogs() {

        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setTitle("Loading Confession Blog List");
        loadingDialog.setMessage("Please wait ...");
        loadingDialog.show();
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                enteitainmentBlogList.clear();
                if(dataSnapshot.exists()) {
                    for(DataSnapshot data : dataSnapshot.getChildren()) {
                        Blog blogItem = data.getValue(Blog.class);
                        enteitainmentBlogList.add(blogItem);
                    }
                    adapter.notifyDataSetChanged();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == BlogFragment.RequestCode_View) {
                loadBlogs();
            }
        }
    }
}
