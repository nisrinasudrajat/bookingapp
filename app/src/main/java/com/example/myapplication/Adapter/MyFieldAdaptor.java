package com.example.myapplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.Model.Field;
import com.example.myapplication.R;

import org.w3c.dom.Text;

import java.util.List;

public class MyFieldAdaptor extends RecyclerView.Adapter<MyFieldAdaptor.MyViewHolder> {
    Context context;
    List<Field> fieldList;

    public MyFieldAdaptor(Context context, List<Field> fieldList) {
        this.context = context;
        this.fieldList = fieldList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_field,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_field_name.setText(fieldList.get(i).getName());
        myViewHolder.txt_field_address.setText(fieldList.get(i).getAdress());
        myViewHolder.txt_field_price.setText(fieldList.get(i).getPrice());
    }

    @Override
    public int getItemCount() {
        return fieldList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_field_address, txt_field_name, txt_field_price;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_field_address = (TextView)itemView.findViewById(R.id.txt_field_address);
            txt_field_name = (TextView)itemView.findViewById(R.id.txt_field_name);
            txt_field_price = (TextView)itemView.findViewById(R.id.txt_field_price);
        }
    }
}
