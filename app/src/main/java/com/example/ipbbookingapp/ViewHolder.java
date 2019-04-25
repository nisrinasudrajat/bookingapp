package com.example.ipbbookingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;
    }

    public void setDetails(Context ctx, String deskr, String hargaLpg, String img, String kategori, String namaLpg){
        TextView deskLapang = mView.findViewById(R.id.deskr);
        TextView hargaLapang = mView.findViewById(R.id.hargaLpg);
        ImageView imgLapang = mView.findViewById(R.id.imgLpg);
        TextView katLapang = mView.findViewById(R.id.kategori);
        TextView namaLapang = mView.findViewById(R.id.namaLpg);

        deskLapang.setText(deskr);
        hargaLapang.setText(hargaLpg);
        katLapang.setText(kategori);
        namaLapang.setText(namaLpg);
        Picasso.get().load(img).into(imgLapang);
    }
}
