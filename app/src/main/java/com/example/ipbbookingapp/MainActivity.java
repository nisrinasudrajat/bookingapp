package com.example.ipbbookingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;




public class MainActivity extends AppCompatActivity {

    TextView welcome, createAccount;
    private AutoCompleteTextView userEmail, userPassword;
    private Button btnLogin;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(MainActivity.this, Home.class));
                }
            }
        };

        welcome = (TextView)findViewById(R.id.welcome);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/Signatura Monoline.ttf");
        welcome.setTypeface(face);

        createAccount = (TextView)findViewById(R.id.createAccount);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

        userEmail=(AutoCompleteTextView) findViewById(R.id.userEmail);
        userPassword=(AutoCompleteTextView) findViewById(R.id.userPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSigIn();
            }
        });
    }
        @Override
        protected void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }

    private void startSigIn() {
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_LONG).show();
        } else {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Sign in problem", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    }

}
