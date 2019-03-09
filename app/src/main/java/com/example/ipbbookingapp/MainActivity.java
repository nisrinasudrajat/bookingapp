package com.example.ipbbookingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.graphics.Typeface;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AutoCompleteTextView;
import com.google.firebase.auth.FirebaseAuth;




public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    TextView welcome;
    private AutoCompleteTextView userEmail, userPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;


    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        welcome = (TextView)findViewById(R.id.welcome);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/Signatura Monoline.ttf");
        welcome.setTypeface(face);

    }
        public void goToSignup(View view) {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        }
}
