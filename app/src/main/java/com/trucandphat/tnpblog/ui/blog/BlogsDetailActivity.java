package com.trucandphat.tnpblog.ui.blog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_detail);
        getSupportActionBar().hide();
        setupInitial();
        setEvent();
    }

    private void setEvent() {
        //get package
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("package");
        Blog blog = (Blog) bundle.getSerializable("Blog_Detail");
        Toast.makeText(getApplicationContext(),blog.getTitle(),Toast.LENGTH_SHORT).show();
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
        //img_blog waiting
        mBackDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
