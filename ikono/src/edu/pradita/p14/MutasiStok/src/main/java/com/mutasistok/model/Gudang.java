package com.mutasistok.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Gudang")
public class Gudang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "kode_gudang", unique = true, nullable = false, length = 20)
    private String kodeGudang;

    @Column(name = "nama_gudang", nullable = false, length = 100)
    private String namaGudang;

    @Column(columnDefinition = "TEXT")
    private String alamat;

    // --- GETTER DAN SETTER YANG DITAMBAHKAN ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKodeGudang() {
        return kodeGudang;
    }

    public void setKodeGudang(String kodeGudang) {
        this.kodeGudang = kodeGudang;
    }

    public String getNamaGudang() {
        return namaGudang;
    }

    public void setNamaGudang(String namaGudang) {
        this.namaGudang = namaGudang;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}