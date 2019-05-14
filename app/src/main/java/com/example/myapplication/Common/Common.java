package com.example.myapplication.Common;

import android.content.Intent;

import com.example.myapplication.Model.Field;
import com.example.myapplication.Model.User;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_FIELD_STORE = "FIELD_SAVE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static String IS_LOGIN = "IsLogin";
    public static User curretUser;
    public static Field currentField;
    public static int step = 0; //Menginisialisasi langkah pertama

    public static String formatShoppingItemName(String name) {
        return name.length() > 13 ? new StringBuilder(name.substring(0,10)).append("...").toString():name;
    }
}
