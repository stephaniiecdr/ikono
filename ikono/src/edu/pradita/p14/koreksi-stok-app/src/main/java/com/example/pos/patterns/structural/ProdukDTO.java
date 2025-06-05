package com.example.pos.patterns.structural;

import com.example.pos.model.Produk;

// Data Transfer Object (DTO) untuk Produk
// Bisa digunakan untuk form input atau output API yang berbeda dari entitas
public class ProdukDTO {
    private String kode;
    private String nama;
    private int kuantitas;
    // Tidak semua field entitas perlu ada di DTO

    public ProdukDTO(String kode, String nama, int kuantitas) {
        this.kode = kode;
        this.nama = nama;
        this.kuantitas = kuantitas;
    }

    // Getters and Setters
    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public int getKuantitas() { return kuantitas; }
    public void setKuantitas(int kuantitas) { this.kuantitas = kuantitas; }
}


