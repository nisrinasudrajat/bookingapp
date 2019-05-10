package com.example.ipbbookingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.view.View;

//public class HomeFragment extends Fragment {
//
//    private Unbinder unbinder;
//    @BindView
//
//
//    public HomeFragment(){
//    }
//
//    @override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
//        //
//        View view = inflater.inflate(R.layout.fragment_home,container, attachToRoot: false);
//        unbinder = ButterKnife.bind(target)
//
//        return view
//    }
//}
