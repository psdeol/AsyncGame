package com.example.asyncgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class Register extends AppCompatActivity {

    //premade accounts
    // user-prab@email.com
    // user-noah@email.com
    // user-joey@email.com
    // all have password: password

    EditText mUsername, mEmail, mPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userID;
    User user;
    String word1 = "panda";
    String word2 = "raccoon";
    String word3 = "goat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = findViewById(R.id.input_name);
        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);

        mRegisterBtn = findViewById(R.id.button_register);
        mLoginBtn = findViewById(R.id.text_login);

        fAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String name = mUsername.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    mUsername.setError("Username is required");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    return;
                }

                if (password.length() < 6){
                    mPassword.setError("Password must be at least 6 characters long");
                    return;
                }

                // if here, register user on firebase
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //send verification email

                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            databaseReference = firebaseDatabase.getReference();

                            user = new User(name, email, userID);
                            WordList wordList = user.getWordList();
                            wordList.setWord(0, word1);
                            wordList.setWord(1, word2);
                            wordList.setWord(2, word3);

                            databaseReference.child("users").child(user.getUserID()).setValue(user);
                            databaseReference.child("users").child(user.getUserID()).child("wordList").child("word1").setValue(wordList.getWord(0));
                            databaseReference.child("users").child(user.getUserID()).child("wordList").child("word2").setValue(wordList.getWord(1));
                            databaseReference.child("users").child(user.getUserID()).child("wordList").child("word3").setValue(wordList.getWord(2));

                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //databaseReference.updateChildren();
                                    Toast.makeText(Register.this, "data added", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onSuccess: user profile is created for " + userID);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(Register.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            Toast.makeText(Register.this, "Error ! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (shouldAllowBack()) {
            super.onBackPressed();
        } else {
            Toast.makeText(Register.this, "Must finish account registration.", Toast.LENGTH_SHORT).show();
        }
    }

    boolean shouldAllowBack(){
        return false;
    }
}
