package com.example.myapplication.Common;

import android.content.Intent;

import com.example.myapplication.Model.Admin;
import com.example.myapplication.Model.Field;
import com.example.myapplication.Model.User;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_FIELD_STORE = "FIELD_SAVE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final int TIME_SLOT_TOTAL = 12;
    public static final String KEY_ADMIN_LOAD_DONE = "ADMIN_LOAD_DONE";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_ADMIN_SELECTED = "ADMIN_SELECTED";
    public static String IS_LOGIN = "IsLogin";
    public static User curretUser;
    public static Field currentField;
    public static Admin currentAdmin;
    public static int step = 0; //Menginisialisasi langkah pertama
    public static String field="";


    public static String formatShoppingItemName(String name) {
        return name.length() > 50 ? new StringBuilder(name.substring(0,10)).append("...").toString():name;
    }

    public static String convertTimeSlotToString(int slot) {
        switch (slot)
        {
            case 0:
                return "7:00-8:00";
            case 1:
                return "8:00-9:00";
            case 2:
                return "9:00-10:00";
            case 3:
                return "10:00-11:00";
            case 4:
                return "11:00-12:00";
            case 5:
                return "12:00-13:00";
            case 6:
                return "13:00-14:00";
            case 7:
                return "14:00-15:00";
            case 8:
                return "15:00-16:00";
            case 9:
                return "17:00-18:00";
            case 10:
                return "19:00-20:00";
            case 11:
                return "20:00-21:00";
            default:
                return "Closed";
        }
    }
}



























