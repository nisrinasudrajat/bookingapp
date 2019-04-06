package com.example.ipbbookingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {
    FirebaseAuth mAuth;
    private TextView congrats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth=FirebaseAuth.getInstance();
        congrats =(TextView) findViewById(R.id.congrats);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.layout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.signOut) {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
