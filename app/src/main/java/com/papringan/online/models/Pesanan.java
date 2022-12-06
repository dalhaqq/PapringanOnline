package com.papringan.online.models;

public class Pesanan {
    private String nama;
    private int jumlah;
    private int total;

    public Pesanan(String nama, int jumlah, int total) {
        this.nama = nama;
        this.jumlah = jumlah;
        this.total = total;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
