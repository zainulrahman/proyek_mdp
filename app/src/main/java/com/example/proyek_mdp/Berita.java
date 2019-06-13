package com.example.proyek_mdp;

public class Berita {

    private String judul,deskripsi,tanggal,sumber,gambar;
    private int id,id_db;
    private static int counter=0;

    public Berita(String judul, String deskripsi, String tanggal,String sumber, String gambar,int id_db) {
        id = ++counter;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
        this.sumber = sumber;
        this.gambar = gambar;
        this.id_db = id_db;
    }

    public String getSumber() {
        return sumber;
    }

    public void setSumber(String sumber) {
        this.sumber = sumber;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_db() {
        return id_db;
    }

    public void setId_db(int id_db) {
        this.id_db = id_db;
    }
}
