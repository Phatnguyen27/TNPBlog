package com.trucandphat.tnpblog.ui.information;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.trucandphat.tnpblog.LoginActivity;
import com.trucandphat.tnpblog.Model.User;
import com.trucandphat.tnpblog.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class InformationFragment extends Fragment {

    private View root;
    public  String userId;
    private Button mbtnLogout;
    private ImageView profileImage;
    private EditText mEditName,mEditDOB,mEditDOC, mEditEmail;
    public static GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions googleSignInOptions;
    private static int PICK_IMAGE_REQUEST = 23;
    private Uri filePath;
    private Dialog loadingDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_information, container, false);
        initGoogleSignin();
        setEvent();
        loadInformation();
        mbtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                mGoogleSignInClient.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        //data
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && requestCode == RESULT_OK && data != null && data.getData() != null ){
            loadingDialog.show();
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),filePath);
                profileImage.setImageBitmap(bitmap);
                Toast.makeText(getContext(),"complete",Toast.LENGTH_SHORT).show();
            } catch (IOException e){
                Toast.makeText(getContext(),"fail",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            Log.i("test",filePath.toString());
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            final StorageReference reference = storageReference.child("userImage/"+ User.getCurrentUser().getUid());
            reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("avatar",uri.toString());
                            User.getCurrentUser().setAvatar(uri.toString());
                            FirebaseDatabase.getInstance().getReference().child("User").child(User.getCurrentUser().getUid()).updateChildren(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loadingDialog.dismiss();
                                    Toast.makeText(getContext(),User.getCurrentUser().getUsername()+"'s avatar updated complete!",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    public void initGoogleSignin() {
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(),googleSignInOptions);
    }
    public void setEvent() {
        profileImage = root.findViewById(R.id.imageViewAvatar);
        mEditName = root.findViewById(R.id.edit_name);
        mEditDOB = root.findViewById(R.id.edit_dob);
        mEditDOC = root.findViewById(R.id.edit_doc);
        mEditEmail = root.findViewById(R.id.edit_email);
        mbtnLogout = root.findViewById(R.id.btnLogout);
        //Diaglog
        loadingDialog = new Dialog(getContext());
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.item_loading);
    }
    public void loadInformation() {
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    if(user.getUid().equals(userId)) {
                        Toast.makeText(getContext(),user.getUsername(),Toast.LENGTH_SHORT).show();
                        new DownloadImageFromInternet(profileImage)
                                .execute(user.getCurrentUser().getAvatar());
                        mEditName.setText(user.getUsername());
                        mEditEmail.setText(user.getEmail());
                        mEditDOB.setText(user.getDateOfBirth().toString());
                        mEditDOC.setText(user.getDateCreated().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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