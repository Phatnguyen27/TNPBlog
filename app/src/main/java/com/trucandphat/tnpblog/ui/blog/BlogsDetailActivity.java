package com.trucandphat.tnpblog.ui.blog;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trucandphat.tnpblog.Model.Blog;
import com.trucandphat.tnpblog.R;

import java.io.File;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogsDetailActivity extends AppCompatActivity {
    private ImageView mImageBlog;
    private CircleImageView mAvatar;
    private TextView mUserPost, mTv_itemDate, mTv_itemTitle, mTv_itemContent;
    private Button mBackDetail;
    private ImageButton mLikeButton;
    private Blog blog;
    private String category,blogId;
    private DatabaseReference databaseReference;
    private ProgressDialog loadingDialog;
    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private boolean liked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_detail);
        getSupportActionBar().hide();
        setupInitial();
        loadDb();
        setEvent();
    }

    private void loadDb() {
        Intent intent = getIntent();
        if(intent != null) {
            category = intent.getExtras().getString("BlogCategory");
            Log.d("BlogCategory",category);
            blogId = intent.getExtras().getString("BlogId");
            Log.d("BlogCategory",blogId);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Blog").child(category).child(blogId);
            loadBlog();
        } else {
            finish();
        }


    }

    private void loadBlog() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    blog = dataSnapshot.getValue(Blog.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        checkLikingStatus();
    }

    private void setEvent() {
        //get package
        //avatar
//        File imgFile = new  File(blog.getAvatar());
//        if(imgFile.exists()) {
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            this.mAvatar.setImageBitmap(myBitmap);
//        }
        if(blog.getAvatar() != null) {
            new DownloadImageFromInternet(mAvatar)
                    .execute(blog.getAvatar());
        }
        mUserPost.setText(blog.getAuthorName());
        mTv_itemDate.setText(blog.getDateCreated().toString());
        mTv_itemTitle.setText(blog.getTitle());
        mTv_itemContent.setText(blog.getContent());
        //img_blog
        if(blog.getImageblog() != null) {
            new DownloadImageFromInternet(mImageBlog)
                    .execute(blog.getImageblog());
        }
        mBackDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeBlog();
            }
        });
        loadingDialog.dismiss();
    }

    private void likeBlog() {
        databaseReference.child("likingUsers").child(userId).setValue(1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            mLikeButton.setImageResource(R.drawable.ic_thumb_up_success_24dp);
                        }
                    }
                });
    }

    private void checkLikingStatus() {
        databaseReference.child("likingUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                        if(i.getKey().toString().equals(userId)) {
                            liked = true;
                        }
                    }
                    if(liked) mLikeButton.setImageResource(R.drawable.ic_thumb_up_success_24dp);
                    else mLikeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupInitial() {
        mBackDetail = findViewById(R.id.btnBackDetail);
        mAvatar = findViewById(R.id.avatar);
        mImageBlog = findViewById(R.id.img_blog);
        mUserPost = findViewById(R.id.UserPost);
        mTv_itemDate = findViewById(R.id.tv_itemDate);
        mTv_itemTitle = findViewById(R.id.tv_itemTitle);
        mTv_itemContent = findViewById(R.id.tv_itemContent);
        mLikeButton = findViewById(R.id.like_blog_button);
        //Dialog
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle("Loading blog's content");
        loadingDialog.setMessage("Please wait...");
        loadingDialog.show();
    }
    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
