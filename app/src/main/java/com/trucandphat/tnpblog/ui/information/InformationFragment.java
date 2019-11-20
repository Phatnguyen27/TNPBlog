package com.trucandphat.tnpblog.ui.information;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trucandphat.tnpblog.Model.User;
import com.trucandphat.tnpblog.R;

public class InformationFragment extends Fragment {

    private View root;
    public static final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private EditText mEditName,mEditDOB,mEditDOC;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_information, container, false);
        setEvent();
        loadInformation();
        return root;
    }
    public void setEvent() {
        mEditName = root.findViewById(R.id.edit_name);
        mEditDOB = root.findViewById(R.id.edit_dob);
        mEditDOC = root.findViewById(R.id.edit_doc);
    }
    public void loadInformation() {
        FirebaseDatabase.getInstance().getReference().child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    if(user.getUid().equals(userId)) {
                        mEditName.setText(user.getUsername());
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