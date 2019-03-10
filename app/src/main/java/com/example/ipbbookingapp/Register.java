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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Register extends AppCompatActivity {
    TextView signup;
    private EditText name, userEmail, userId, userName, userPassword;
    private Button btnSignUp;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signup = (TextView)findViewById(R.id.signup);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/Signatura Monoline.ttf");
        signup.setTypeface(face);


        //initializing views
        name = (EditText) findViewById(R.id.name);
        userEmail = (EditText) findViewById(R.id.userEmail);
        userId = (EditText) findViewById(R.id.userId);
        userName = (EditText) findViewById(R.id.userName);
        userPassword = (EditText) findViewById(R.id.userPassword);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        databaseReference = FirebaseDatabase.getInstance().getReference("Profile");
        firebaseAuth = FirebaseAuth.getInstance();


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String namaUser = name.getText().toString().trim();
                final String email = userEmail.getText().toString().trim();
                final String identitas = userId.getText().toString().trim();
                final String uname = userName.getText().toString().trim();
                String password = userPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Please enter e-mail", Toast.LENGTH_LONG).show();
                }
                if(TextUtils.isEmpty(namaUser)){
                    Toast.makeText(Register.this, "Please enter full name", Toast.LENGTH_LONG).show();
                }
                if(TextUtils.isEmpty(identitas)){
                    Toast.makeText(Register.this, "Please enter identity number", Toast.LENGTH_LONG).show();
                }
                if(TextUtils.isEmpty(uname)){
                    Toast.makeText(Register.this, "Please enter username", Toast.LENGTH_LONG).show();
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Please enter password", Toast.LENGTH_LONG).show();
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                profile information = new profile(
                                        namaUser,
                                        email,
                                        identitas,
                                        uname
                                );
                                FirebaseDatabase.getInstance().getReference("Profile")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(Register.this, "Registration Complete", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), Home.class));
                                    }
                                });
                            }
                        }
                    });
            }
        });
    }

}