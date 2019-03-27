package com.example.ipbbookingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public class Home extends AppCompatActivity {
    private TextView congrats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        congrats =(TextView) findViewById(R.id.congrats);
    }
}
