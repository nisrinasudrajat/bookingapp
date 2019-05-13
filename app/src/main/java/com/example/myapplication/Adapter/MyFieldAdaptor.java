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
import com.example.myapplication.Model.Field;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;


public class MyFieldAdaptor extends RecyclerView.Adapter<MyFieldAdaptor.MyViewHolder> {
    Context context;
    List<Field> fieldList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyFieldAdaptor(Context context, List<Field> fieldList) {
        this.context = context;
        this.fieldList = fieldList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_field,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_field_name.setText(fieldList.get(i).getName());
        myViewHolder.txt_field_address.setText(fieldList.get(i).getAdress());
        myViewHolder.txt_field_price.setText(fieldList.get(i).getPrice());

        if(!cardViewList.contains(myViewHolder.card_field))
            cardViewList.add(myViewHolder.card_field);

        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //Membuat bg warna putih pas kalo gak dipilih
                for(CardView cardView:cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

                //Set bg terpilih cuman buat item terpilih
                myViewHolder.card_field.setCardBackgroundColor(context.getResources()
                .getColor(android.R.color.holo_green_light));

                //Kirim bc buat ngasih tau book act supaya aktifin tombol next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_FIELD_STORE,fieldList.get(pos));
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fieldList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_field_address, txt_field_name, txt_field_price;
        CardView card_field;
        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_field = (CardView)itemView.findViewById(R.id.card_field);
            txt_field_address = (TextView)itemView.findViewById(R.id.txt_field_address);
            txt_field_name = (TextView)itemView.findViewById(R.id.txt_field_name);
            txt_field_price = (TextView)itemView.findViewById(R.id.txt_field_price);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view, getAdapterPosition());
        }
    }
}
