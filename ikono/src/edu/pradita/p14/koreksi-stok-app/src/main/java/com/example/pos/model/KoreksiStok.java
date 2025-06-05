package com.example.pos.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "koreksi_stok")
public class KoreksiStok {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_koreksi")
    private int idKoreksi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produk", nullable = false)
    private Produk produk;

    @Column(name = "jumlah_koreksi", nullable = false)
    private int jumlahKoreksi; // Positif untuk penambahan, negatif untuk pengurangan

    @Column(name = "stok_sebelum", nullable = false)
    private int stokSebelum;

    @Column(name = "stok_sesudah", nullable = false)
    private int stokSesudah;

    @Column(name = "tipe_koreksi", nullable = false, length = 50) // Misal: "MANUAL_INPUT", "RUSAK", "PENAMBAHAN", "PENGURANGAN"
    private String tipeKoreksi;

    @Column(name = "catatan", length = 500)
    private String catatan;

    @Column(name = "waktu_koreksi", nullable = false)
    private LocalDateTime waktuKoreksi;

    // Constructors
    public KoreksiStok() {
        this.waktuKoreksi = LocalDateTime.now();
    }

    public KoreksiStok(Produk produk, int jumlahKoreksi, int stokSebelum, int stokSesudah, String tipeKoreksi, String catatan) {
        this.produk = produk;
        this.jumlahKoreksi = jumlahKoreksi;
        this.stokSebelum = stokSebelum;
        this.stokSesudah = stokSesudah;
        this.tipeKoreksi = tipeKoreksi;
        this.catatan = catatan;
        this.waktuKoreksi = LocalDateTime.now();
    }

    // Getters and Setters
    public int getIdKoreksi() {
        return idKoreksi;
    }

    public void setIdKoreksi(int idKoreksi) {
        this.idKoreksi = idKoreksi;
    }

    public Produk getProduk() {
        return produk;
    }

    public void setProduk(Produk produk) {
        this.produk = produk;
    }

    public int getJumlahKoreksi() {
        return jumlahKoreksi;
    }

    public void setJumlahKoreksi(int jumlahKoreksi) {
        this.jumlahKoreksi = jumlahKoreksi;
    }

    public int getStokSebelum() {
        return stokSebelum;
    }

    public void setStokSebelum(int stokSebelum) {
        this.stokSebelum = stokSebelum;
    }

    public int getStokSesudah() {
        return stokSesudah;
    }

    public void setStokSesudah(int stokSesudah) {
        this.stokSesudah = stokSesudah;
    }

    public String getTipeKoreksi() {
        return tipeKoreksi;
    }

    public void setTipeKoreksi(String tipeKoreksi) {
        this.tipeKoreksi = tipeKoreksi;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public LocalDateTime getWaktuKoreksi() {
        return waktuKoreksi;
    }

    public void setWaktuKoreksi(LocalDateTime waktuKoreksi) {
        this.waktuKoreksi = waktuKoreksi;
    }
}
