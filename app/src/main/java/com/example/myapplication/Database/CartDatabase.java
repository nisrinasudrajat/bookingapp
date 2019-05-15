package com.example.myapplication.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(version = 1,entities = CartItem.class,exportSchema = false)

public abstract class CartDatabase extends RoomDatabase {
    private static CartDatabase instance;
    public static CartDatabase getInstance(Context context){
        return instance;
    }
}
