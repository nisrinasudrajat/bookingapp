package com.example.ipbbookingapp;

public class ItemList {
    private String deskr, hargaLpg, imgLpg, kategori, namaLpg;

    public ItemList(String deskr, String hargaLpg, String imgLpg, String kategori, String namaLpg) {
        this.deskr = deskr;
        this.hargaLpg = hargaLpg;
        this.imgLpg = imgLpg;
        this.kategori = kategori;
        this.namaLpg = namaLpg;
    }

    public ItemList() {
    }

    public String getDeskr() {
        return deskr;
    }

    public void setDeskr(String deskr) {
        this.deskr = deskr;
    }

    public String getHargaLpg() {
        return hargaLpg;
    }

    public void setHargaLpg(String hargaLpg) {
        this.hargaLpg = hargaLpg;
    }

    public String getImgLpg() {
        return imgLpg;
    }

    public void setImgLpg(String imgLpg) {
        this.imgLpg = imgLpg;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getNamaLpg() {
        return namaLpg;
    }

    public void setNamaLpg(String namaLpg) {
        this.namaLpg = namaLpg;
    }
}
