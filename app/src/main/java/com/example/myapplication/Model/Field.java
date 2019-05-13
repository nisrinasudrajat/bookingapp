package com.example.myapplication.Model;

public class Field {
    private String name, adress, Price;

    public Field() {
    }

    public Field(String name, String adress, String price) {
        this.name = name;
        this.adress = adress;
        this.Price = price;
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
        this.Price = price;
    }
}
