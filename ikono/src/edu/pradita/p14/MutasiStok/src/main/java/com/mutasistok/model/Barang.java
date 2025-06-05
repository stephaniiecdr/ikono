package com.mutasistok.model;

import javax.persistence.*;

@Entity
@Table(name = "Barang")
public class Barang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kode_barang", unique = true, nullable = false, length = 50)
    private String kodeBarang;

    @Column(name = "nama_barang", nullable = false)
    private String namaBarang;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
// Buat Getter dan Setter
}