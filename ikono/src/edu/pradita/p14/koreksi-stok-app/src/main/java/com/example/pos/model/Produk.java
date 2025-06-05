package com.example.pos.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produk")
public class Produk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produk")
    private int idProduk;

    @Column(name = "kode_produk", unique = true, nullable = false, length = 50)
    private String kodeProduk;

    @Column(name = "nama_produk", nullable = false, length = 255)
    private String namaProduk;

    @Column(name = "stok", nullable = false)
    private int stok;

    @Column(name = "harga_beli", nullable = false)
    private double hargaBeli;

    @Column(name = "harga_jual", nullable = false)
    private double hargaJual;

    @OneToMany(mappedBy = "produk", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<KoreksiStok> daftarKoreksiStok = new ArrayList<>();

    // Constructors
    public Produk() {
    }

    public Produk(String kodeProduk, String namaProduk, int stok, double hargaBeli, double hargaJual) {
        this.kodeProduk = kodeProduk;
        this.namaProduk = namaProduk;
        this.stok = stok;
        this.hargaBeli = hargaBeli;
        this.hargaJual = hargaJual;
    }

    // Getters and Setters
    public int getIdProduk() {
        return idProduk;
    }

    public void setIdProduk(int idProduk) {
        this.idProduk = idProduk;
    }

    public String getKodeProduk() {
        return kodeProduk;
    }

    public void setKodeProduk(String kodeProduk) {
        this.kodeProduk = kodeProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public double getHargaBeli() {
        return hargaBeli;
    }

    public void setHargaBeli(double hargaBeli) {
        this.hargaBeli = hargaBeli;
    }

    public double getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(double hargaJual) {
        this.hargaJual = hargaJual;
    }

    public List<KoreksiStok> getDaftarKoreksiStok() {
        return daftarKoreksiStok;
    }

    public void setDaftarKoreksiStok(List<KoreksiStok> daftarKoreksiStok) {
        this.daftarKoreksiStok = daftarKoreksiStok;
    }

    public void tambahKoreksiStok(KoreksiStok koreksi) {
        daftarKoreksiStok.add(koreksi);
        koreksi.setProduk(this);
    }

    @Override
    public String toString() {
        return namaProduk + " (Stok: " + stok + ")";
    }
}
