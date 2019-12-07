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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.trucandphat.tnpblog.Adapter.BlogAdapter;
import com.trucandphat.tnpblog.Adapter.CommentAdapter;
import com.trucandphat.tnpblog.Model.Blog;
import com.trucandphat.tnpblog.Model.Comment;
import com.trucandphat.tnpblog.R;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogsDetailActivity extends AppCompatActivity {
    private ImageView mImageBlog;
    private CircleImageView mAvatar;
    private TextView mUserPost, mTv_itemDate, mTv_itemTitle, mTv_itemContent;
    private Button mBackDetail,mCommentButton;
    private ImageButton mLikeButton;
    private Blog blog;
    private String category,blogId;
    private DatabaseReference databaseReference;
    private ProgressDialog loadingDialog;
    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private boolean liked = false;
    private ListView mCommentListView;
    private CommentAdapter adapter;
    private ArrayList<Comment> commentList = new ArrayList<>();
    private LinearLayout commentInputLayout;

    //tinh date;
    private Calendar currentCal = Calendar.getInstance();
    public final static String[] month = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};

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
                    loadInformation();
                    loadImage();
                }
                else {
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        checkLikingStatus();
    }

    private void loadImage() {
        if(blog.getAvatar() != null) {
            new DownloadImageFromInternet(mAvatar)
                    .execute(blog.getAvatar());
        }
        if(blog.getImageblog() != null) {
            new DownloadImageFromInternet(mImageBlog)
                    .execute(blog.getImageblog());
        }
    }

    private void setEvent() {
        mBackDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(liked) dislikeBlog();
                else likeBlog();
            }
        });
        mCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadComment();
                mCommentButton.setClickable(false);
            }
        });
        loadingDialog.dismiss();
    }

    private void loadInformation() {
        mUserPost.setText(blog.getAuthorName());
        mTv_itemDate.setText(getDateDifference(blog.getDateCreated()));
        mTv_itemTitle.setText(blog.getTitle());
        mTv_itemContent.setText(blog.getContent());
    }
    private String getDateDifference(Date date) {
        String datediff = "";
        Calendar itemCal = Calendar.getInstance();
        itemCal.set(Calendar.HOUR_OF_DAY, 1);
        itemCal.set(Calendar.MINUTE, 1);
        itemCal.set(Calendar.SECOND, 1);
        Date currentDate = itemCal.getTime();
        itemCal.setTime(date);
        itemCal.set(Calendar.HOUR_OF_DAY, 1);
        itemCal.set(Calendar.MINUTE, 1);
        itemCal.set(Calendar.SECOND, 1);
        Date thatdate = itemCal.getTime();
        long timediff = currentDate.getTime() - thatdate.getTime();
        float daydiff = Math.round(((float) timediff / (1000 * 60 * 60 * 24)) * 10) / 10;
        if (daydiff == 0.0) {
            datediff += "Today";
        } else if (daydiff > 0 && daydiff < 7) {
            datediff += (int) Math.ceil(daydiff) + " days ago";
        } else {
            datediff += month[itemCal.get(Calendar.MONTH)] + " " + itemCal.get(Calendar.DATE);
            if (!(currentCal.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR))) {
                datediff += " " + itemCal.get(Calendar.YEAR);
            }
        }
        return datediff;
    }
    private void dislikeBlog() {
        databaseReference.child("likingUsers").child(userId).setValue(null)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            mLikeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                        }
                    }
                });
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
        mCommentListView = findViewById(R.id.lvComment);
        adapter = new CommentAdapter(this,R.layout.comment_item,commentList);
        mCommentListView.setAdapter(adapter);
        commentInputLayout = findViewById(R.id.comment_input_layout);
        commentInputLayout.setVisibility(LinearLayout.GONE);
        mCommentButton = findViewById(R.id.comment_blog);
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

    private void loadComment() {
        databaseReference.child("Comment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                        Comment comment = i.getValue(Comment.class);
                        commentList.add(comment);
                    }
                    adapter.notifyDataSetChanged();
                }
                commentInputLayout.setVisibility(LinearLayout.VISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void postComment(View v) {
        String authorName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        EditText editTextComment = findViewById(R.id.edit_comment);
        String content = editTextComment.getText().toString();
        Comment comment = new Comment(authorName,content);
        databaseReference.child("Comment").push().setValue(comment);
        commentList.add(comment);
        adapter.notifyDataSetChanged();
    }
}
