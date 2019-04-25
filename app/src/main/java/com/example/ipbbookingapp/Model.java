package com.example.ipbbookingapp;

public class Model {
    private String deskr, hargaLpg, imgLpg, kategori, namaLpg;

    public Model(String deskr, String hargaLpg, String img, String kategori, String namaLpg) {
        this.deskr = deskr;
        this.hargaLpg = hargaLpg;
        this.kategori = kategori;
        this.namaLpg = namaLpg;
        imgLpg = img;
    }

    public Model() {
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

    public String getImgLpg() {
        return imgLpg;
    }

    public void setImgLpg(String imgLpg) {
        this.imgLpg = imgLpg;
    }
}
