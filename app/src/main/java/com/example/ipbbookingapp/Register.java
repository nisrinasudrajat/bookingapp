package com.example.ipbbookingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Register extends AppCompatActivity {
    TextView signup;
    private EditText name, userEmail, userId, userName, userPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        signup = (TextView)findViewById(R.id.signup);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/Signatura Monoline.ttf");
        signup.setTypeface(face);


        //initializing views
        name = (EditText) findViewById(R.id.name);
        userEmail = (EditText) findViewById(R.id.userEmail);
        userId = (EditText) findViewById(R.id.userId);
        userName = (EditText) findViewById(R.id.userName);
        userPassword = (EditText) findViewById(R.id.userPassword);



        progressDialog = new ProgressDialog(this);
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }
    private void registerUser(){

        //getting email and password from edit texts
        final String namaUser = name.getText().toString().trim();
        final String email = userEmail.getText().toString().trim();
        final String identitas = userId.getText().toString().trim();
        final String uname = userName.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if (namaUser.isEmpty()) {
            name.setError(getString(R.string.input_error_name));
            name.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            userEmail.setError(getString(R.string.input_error_email));
            userEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError(getString(R.string.input_error_email_invalid));
            userEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            userPassword.setError(getString(R.string.input_error_password));
            userPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            userPassword.setError(getString(R.string.input_error_password_length));
            userPassword.requestFocus();
            return;
        }

        if (identitas.isEmpty()) {
            userId.setError(getString(R.string.input_error_id));
            userId.requestFocus();
            return;
        }
        if (uname.isEmpty()) {
            userName.setError(getString(R.string.input_error_uname));
            userName.requestFocus();
            return;
        }

        if (uname.length() < 5) {
            userName.setError(getString(R.string.input_error_uname_length));
            userName.requestFocus();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(
                                    namaUser,
                                    email,
                                    identitas,
                                    uname

                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                    } else {
                                        //display a failure message
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                registerUser();
                break;
        }
    }
}