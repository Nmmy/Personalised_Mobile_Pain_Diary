package com.example.personalisedmobilepaindiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.ContentValues.TAG;

public class Login extends AppCompatActivity {
    EditText mUsername, mPassword;
    Button mLoginBtn;
    TextView nevToRegister;
    Button showHideBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.loginBtn);
        nevToRegister = findViewById(R.id.nevToRegister);
        showHideBtn = findViewById(R.id.showHideBtn);

        mAuth = FirebaseAuth.getInstance();

        //show/hide password
        showHideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPassword.getText().toString().isEmpty()){
                    mPassword.setError("Please Enter Password");
                } else {
                    if(showHideBtn.getText().toString().equals("Show")){
                        showHideBtn.setText("Hide");
                        mPassword.setTransformationMethod(null);
                    } else {
                        showHideBtn.setText("Show");
                        mPassword.setTransformationMethod(new PasswordTransformationMethod());
                    }
                }
            }
        });

        //check if user is currently signed in
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username) || !Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                    mUsername.setError("Email is required and must be a valid email address!");
                    return;
                }
                if (TextUtils.isEmpty(password) || password.length() < 6) {
                    mPassword.setError("Password is required and must be equal or longer than 6!");
                    return;
                }else{
                    //authenticate user
                    mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, notify user and start main activity
                                Toast.makeText(Login.this, "Sign in successfully.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this, "User log in failed!"
                                        + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });

        nevToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
    }
}