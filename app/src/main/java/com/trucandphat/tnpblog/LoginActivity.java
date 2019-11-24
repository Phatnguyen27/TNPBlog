package com.trucandphat.tnpblog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trucandphat.tnpblog.Model.User;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    //Progress Dialog
    private ProgressDialog myProgress;

    //google signin related component
    private SignInButton googleSigninBtn;
    private GoogleSignInOptions googleSignInOptions;
    public static GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbReference;
    private String userId;
    private int REQUEST_PERMISSION =12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
            return;
        } else {
            firebaseAuth = FirebaseAuth.getInstance();
            initGoogleSignin();
            dbReference = FirebaseDatabase.getInstance().getReference();
            setEvent();
        }
    }
    protected void initGoogleSignin() {
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
    }
    protected void setEvent() {
        this.googleSigninBtn = findViewById(R.id.google_signin_button);
        this.googleSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myProgress = new ProgressDialog(LoginActivity.this);
                myProgress.setTitle("Authenticatiing");
                myProgress.setMessage("Please wait...");
                myProgress.setCancelable(true);
                myProgress.show();
                loginWithGoogle();
            }
        });
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        myProgress.dismiss();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            checkForUserIdInDatabase();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this,"Failed to sign in with Google, please try again later",Toast.LENGTH_LONG).show();
                        }
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }
    private void loginWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
    private void checkForUserIdInDatabase() {
        dbReference.child("User").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Date date = new Date();
                            String email = firebaseAuth.getCurrentUser().getEmail();
                            String username = firebaseAuth.getCurrentUser().getDisplayName();
                            User user = new User(userId,username,email,date,date,0);
                            dbReference.child("User").child(userId).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                firebaseAuth = FirebaseAuth.getInstance();
                initGoogleSignin();
                dbReference = FirebaseDatabase.getInstance().getReference();
                setEvent();
            }
            else {
                finish();
            }
        }
    }
}
