package com.trucandphat.tnpblog.ui.new_blog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trucandphat.tnpblog.Model.Blog;
import com.trucandphat.tnpblog.R;

import java.util.Date;


public class AddBlogFragment extends Fragment {

    private FloatingActionButton mFabAdd;
    private EditText mEditTitle,mEditContent;
    private RadioGroup mRadioGroupCategory;
    private Blog newBlog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_new, container, false);
        setEvent(root);
        newBlog = new Blog();
        return root;
    }
    public void setEvent(View view) {
        mEditTitle = view.findViewById(R.id.edit_title);
        mEditContent = view.findViewById(R.id.edit_content);
        mFabAdd = view.findViewById(R.id.add_button);
        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                int category = -1;
                String categoryString = "";
                switch (mRadioGroupCategory.getCheckedRadioButtonId()) {
                    case R.id.education_radio:
                        category = Blog.BlogType.Education.toInt();
                        categoryString = Blog.BlogType.Education.toString();
                        break;
                    case R.id.confession_radio:
                        category = Blog.BlogType.Confession.toInt();
                        categoryString = Blog.BlogType.Confession.toString();
                        break;
                    case R.id.entertainment_radio:
                        category = Blog.BlogType.Entertainment.toInt();
                        categoryString = Blog.BlogType.Entertainment.toString();
                        break;
                        default:
                            break;
                }
                String title = mEditTitle.getText().toString();
                String content = mEditContent.getText().toString();
                if(isFullFilled(category,title,content)) {
                    DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
                    String key = dbReference.child("Blog").push().getKey();
                    String authorId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    newBlog = new Blog(key,title,content,authorId,category,new Date(),0,0);
                    dbReference.child("Blog").child(categoryString).child(key).setValue(newBlog).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getActivity(),"New blog successfully added",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        mRadioGroupCategory = view.findViewById(R.id.category_radiogroup);

    }
    public boolean isFullFilled(int category,String title,String content) {
        if (category == -1) {
            Toast.makeText(getActivity(),"Please choose your blog's category",Toast.LENGTH_LONG).show();
            return false;
        } else {
            if (title.length() <= 0) {
                Toast.makeText(getActivity(),"Please input your blog's title",Toast.LENGTH_LONG).show();
                return false;
            } else {
                if (content.length() == 0) {
                    Toast.makeText(getActivity(),"Please input your blog's content",Toast.LENGTH_LONG).show();
                    return false;
                } else if (content.length() < 250) {
                    Toast.makeText(getActivity(),"Please choose your blog's category",Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    return true;
                }
            }
        }
    }
}
