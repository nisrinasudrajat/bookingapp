package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.Common.Common;
import com.example.myapplication.Interface.IRecyclerItemSelectedListener;
import com.example.myapplication.Model.Admin;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdminAdapter extends RecyclerView.Adapter<MyAdminAdapter.MyViewHolder>{

    Context context;
    List<Admin> adminList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyAdminAdapter(Context context, List<Admin> adminList) {
        this.context = context;
        this.adminList = adminList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_admin,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_admin_name.setText(adminList.get(i).getName());
        if(!cardViewList.contains(myViewHolder.card_admin))
            cardViewList.add(myViewHolder.card_admin);

        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //Set Background for all item not choice
                for (CardView cardView : cardViewList)
                {
                    cardView.setCardBackgroundColor(context.getResources()
                            .getColor(android.R.color.white));
                }
                //set Background untuk pilihan
                myViewHolder.card_admin.setBackgroundColor(
                        context.getResources()
                        .getColor(android.R.color.holo_orange_dark)
                );
                //Send Local broadcast to enable button next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_ADMIN_SELECTED,adminList.get(pos));
                intent.putExtra(Common.KEY_STEP,2);
                localBroadcastManager.sendBroadcast(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return adminList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_admin_name;
        CardView card_admin;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_admin = (CardView)itemView.findViewById(R.id.card_admin);
            txt_admin_name = (TextView)itemView.findViewById(R.id.txt_admin_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view ,getAdapterPosition());
        }
    }
}





































