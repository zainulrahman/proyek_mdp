package com.example.proyek_mdp;

public class Komentar {
    private int idkomentar;
    private String komentar, namalengkap,tanggal;
    private static int ctr = 0;

    public Komentar(String komentar, String namalengkap, String tanggal) {
        idkomentar = ++ctr;
        this.komentar = komentar;
        this.namalengkap = namalengkap;
        this.tanggal = tanggal;
    }

    public String getTanggal() {
        return tanggal;
    }
    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
    public int getIdkomentar() {
        return idkomentar;
    }

    public void setIdkomentar(int idkomentar) {
        this.idkomentar = idkomentar;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getNamalengkap() {
        return namalengkap;
    }

    public void setNamalengkap(String namalengkap) {
        this.namalengkap = namalengkap;
    }
}
