package com.trucandphat.tnpblog.ui.new_blog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.trucandphat.tnpblog.Model.Blog;
import com.trucandphat.tnpblog.Model.User;
import com.trucandphat.tnpblog.R;
import com.trucandphat.tnpblog.ui.information.InformationFragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class AddBlogFragment extends Fragment {

    private FloatingActionButton mFabAdd;
    private EditText mEditTitle,mEditContent;
    private ImageView mImageBlog;
    private RadioGroup mRadioGroupCategory;
    private Blog newBlog;
    private int category = -1;
    private final int GALLERY_REQUEST = 1001;
    private ProgressDialog initialDialog;
    private Bitmap selectedImage;
    private String UidBlog;
    private Uri Uri;
    private String categoryString1; // lấy categoryString

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
        mImageBlog = view.findViewById(R.id.image_blog);
        mFabAdd = view.findViewById(R.id.add_button);
        mRadioGroupCategory = view.findViewById(R.id.category_radiogroup);
        mRadioGroupCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                category = checkedId;
            }
        });
        selectedImage = BitmapFactory.decodeResource(getResources(),R.drawable.defaultimage);
        mImageBlog.setImageBitmap(selectedImage);
        mImageBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_REQUEST);
            }
        });
        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                int categoryInt = -1;
                String categoryString = "";
//                if(category == R.id.education_radio) {
//                    categoryInt = Blog.BlogType.Education.toInt();
//                    Log.d("CHECK CHANGE",String.valueOf(Blog.BlogType.Education.toInt()));
//                }else if(category == R.id.confession_radio) {
//                    categoryInt = Blog.BlogType.Confession.toInt();
//                    Log.d("CHECK CHANGE",String.valueOf(Blog.BlogType.Confession.toInt()));
//
//                }else if (category == R.id.entertainment_radio) {
//                    categoryInt = Blog.BlogType.Entertainment.toInt();
//                    Log.d("CHECK CHANGE",String.valueOf(Blog.BlogType.Entertainment.toInt()));
//
//                }
                switch (category) {
                    case (R.id.education_radio):
                        categoryInt = Blog.BlogType.Education.toInt();
                        categoryString = Blog.BlogType.Education.toString();
                        Log.d("CHECK CHANGE",String.valueOf(R.id.education_radio));
                        break;
                    case (R.id.confession_radio):
                        categoryInt = Blog.BlogType.Confession.toInt();
                        categoryString = Blog.BlogType.Confession.toString();
                        Log.d("CHECK CHANGE",String.valueOf(R.id.confession_radio));
                        break;
                    case (R.id.entertainment_radio):
                        categoryInt = Blog.BlogType.Entertainment.toInt();
                        categoryString = Blog.BlogType.Entertainment.toString();
                        Log.d("CHECK CHANGE",String.valueOf(R.id.entertainment_radio));
                        break;
                        default:
                            break;
                }
                String title = mEditTitle.getText().toString();
                String content = mEditContent.getText().toString();

                if(isFullFilled(categoryInt,title,content)) {
                    DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
                    String key = dbReference.child("Blog").child(categoryString).push().getKey();
                    //lấy UidBlog
                    UidBlog = key;
                    String authorId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String authorName = InformationFragment.userName;
                    newBlog = new Blog(User.getCurrentUser().getAvatar(),UidBlog,title,content,authorId,authorName,categoryInt,new Date(),"",0,0);
                    Log.d("Username",newBlog.getAuthorName());
                    dbReference.child("Blog").child(categoryString).child(UidBlog).setValue(newBlog).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getActivity(),"New blog successfully added",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    //thêm ảnh
                    if (Uri != null){
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        final StorageReference reference = storageReference.child("BlogImage/"+categoryString+"/"+UidBlog);
                        categoryString1 = categoryString; //a copy BlogType
                        reference.putFile(Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("imageblog",uri.toString());

                                        FirebaseDatabase.getInstance().getReference().child("Blog")
                                                .child(categoryString1).child(UidBlog).updateChildren(data)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(),"fail_lan_cuoi",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(),"fail_something1",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"fail_up_image",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }
        });

    }
    public boolean isFullFilled(int category,String title,String content) {
        if (category == (-1)) {
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
                } else if (content.length() < 1) {
                    Toast.makeText(getActivity(),"Your blog's content must contain 250 digits or more",Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    Log.d("CHECK Status","done");
                    return true;
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST: {
                    final Uri imageUri = data.getData();
                    Uri = imageUri;
                    try {
                        final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        mImageBlog.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        } else {
        }
    }
}
