package com.example.myapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Field implements Parcelable {
    private String name, adress, Price, fieldID;

    public Field() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getFieldID() {
        return fieldID;
    }

    public void setFieldID(String fieldID) {
        this.fieldID = fieldID;
    }

    protected Field(Parcel in) {
        name = in.readString();
        adress = in.readString();
        Price = in.readString();
        fieldID = in.readString();
    }

    public static final Creator<Field> CREATOR = new Creator<Field>() {
        @Override
        public Field createFromParcel(Parcel in) {
            return new Field(in);
        }

        @Override
        public Field[] newArray(int size) {
            return new Field[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(adress);
        dest.writeString(Price);
        dest.writeString(fieldID);
    }
}
