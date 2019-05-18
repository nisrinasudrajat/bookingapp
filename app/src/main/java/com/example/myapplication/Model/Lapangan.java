package com.example.myapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Lapangan implements Parcelable {
    private String name, lapanganID;

    public Lapangan() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getLapanganID() {
        return lapanganID;
    }

    public void setLapanganID(String lapanganID) {
        this.lapanganID = lapanganID;
    }

    protected Lapangan(Parcel in) {
        name = in.readString();
        lapanganID = in.readString();
    }

    public static final Creator<Lapangan> CREATOR = new Creator<Lapangan>() {
        @Override
        public Lapangan createFromParcel(Parcel in) {
            return new Lapangan(in);
        }

        @Override
        public Lapangan[] newArray(int size) {
            return new Lapangan[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(lapanganID);
    }
}
