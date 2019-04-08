package com.example.ipbbookingapp;

import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseListAdapter;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

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
        FirebaseListOptions<ItemList> options=new FirebaseListOptions.Builder<ItemList>()
                .setLayout(R.layout.item_list)
                .setLifecycleOwner(Home.this)
                .setQuery(query,ItemList.class)
                .build();

        adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(View v, Object model, int position) {
                TextView deskr = v.findViewById(R.id.deskr);
                TextView hargaLpg = v.findViewById(R.id.hargaLpg);
                ImageView imgLpg = v.findViewById(R.id.imgLpg);
                TextView kategori = v.findViewById(R.id.kategori);
                TextView namaLpg = v.findViewById(R.id.namaLpg);

                ItemList std = (ItemList) model;
                deskr.setText("Deskripsi:" +std.getDeskr().toString());
                hargaLpg.setText("Harga:"+std.getHargaLpg().toString());
                kategori.setText("Kategori:"+std.getKategori().toString());
                namaLpg.setText("Nama:"+std.getNamaLpg().toString());
                Picasso.with(Home.this).load(std.getImgLpg().toString()).into(imgLpg);
            }
        };
        lv.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();
        congrats = (TextView) findViewById(R.id.congrats);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.layout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.signOut) {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
