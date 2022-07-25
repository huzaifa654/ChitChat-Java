package com.example.chitchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chitchat.Models.Users;
import com.example.chitchat.databinding.ActivitySigninBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class SigninActivity extends AppCompatActivity {
    ActivitySigninBinding binding;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        Objects.requireNonNull(getSupportActionBar()).hide();

        progressDialog = new ProgressDialog(SigninActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to  your account");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.btnSignin.setOnClickListener(view -> {
            if (binding.etEmail.getText().toString().isEmpty()){
                binding.etEmail.setError("Enter Your Email");
                return;
            }


            if (binding.etPassward.getText().toString().isEmpty()){
                binding.etPassward.setError("Enter Your Passward");
                return;
            }
            progressDialog.show();


            auth.signInWithEmailAndPassword(binding.etEmail.getText().toString(), binding.etPassward.getText().toString()).addOnCompleteListener(task -> {
                progressDialog.dismiss();
                //intent is use to switching activities
                if (task.isSuccessful()) {
                    Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SigninActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });
        binding.tvclickSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(SigninActivity.this, SignUpActivity.class);
            startActivity(intent);

        });
        binding.btnGoogle.setOnClickListener(view -> signIn());
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    int RC_SIGN_IN = 65;

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                e.printStackTrace();
                Log.w("TAG", "Google Sign in Failed");
            }

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {


            // Got an ID token from Google. Use it to authenticate
            // with Firebase.
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            Users users= new Users();
                            assert user != null;
                            users.setUserId(user.getUid());
                            users.setUserName(user.getDisplayName());
                            users.setProfilepic(Objects.requireNonNull(user.getPhotoUrl()).toString());
                            database.getReference().child("Users").child(user.getUid()).setValue(users);


                            Intent intent=new Intent(SigninActivity.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(SigninActivity.this, "Sign in with Google", Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                          //  updateUI(null);
                        }
                    });
        }


    }
