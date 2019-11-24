package com.trucandphat.tnpblog.ui.information;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trucandphat.tnpblog.LoginActivity;
import com.trucandphat.tnpblog.Model.User;
import com.trucandphat.tnpblog.R;

public class InformationFragment extends Fragment {

    private View root;
    public  String userId;
    private Button mbtnLogout;
    private EditText mEditName,mEditDOB,mEditDOC, mEditEmail;
    public static GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions googleSignInOptions;

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
        return root;
    }
    public void initGoogleSignin() {
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(),googleSignInOptions);
    }
    public void setEvent() {
        mEditName = root.findViewById(R.id.edit_name);
        mEditDOB = root.findViewById(R.id.edit_dob);
        mEditDOC = root.findViewById(R.id.edit_doc);
        mEditEmail = root.findViewById(R.id.edit_email);
        mbtnLogout = root.findViewById(R.id.btnLogout);
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
}