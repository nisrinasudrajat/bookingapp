package com.example.ipbbookingapp;

import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import org.w3c.dom.Text;

public class Home extends AppCompatActivity {
    FirebaseAuth mAuth;
    private TextView congrats;

    ListView lv;
    FirebaseListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lv = (ListView)findViewById(R.id.listView);
        Query query = FirebaseDatabase.getInstance().getReference().child("Item");

        lv.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();
        congrats = (TextView) findViewById(R.id.congrats);
    }



}
