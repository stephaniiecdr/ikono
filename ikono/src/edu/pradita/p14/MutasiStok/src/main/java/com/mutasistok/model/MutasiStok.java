package com.mutasistok.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MutasiStok")
public class MutasiStok {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "barang_id", nullable = false)
    private Barang barang;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipe_mutasi", columnDefinition = "ENUM('MASUK', 'KELUAR', 'TRANSFER')")
    private TipeMutasi tipeMutasi;

    private int jumlah;

    @ManyToOne
    @JoinColumn(name = "gudang_asal_id")
    private Gudang gudangAsal;

    @ManyToOne
    @JoinColumn(name = "gudang_tujuan_id")
    private Gudang gudangTujuan;

    @Column(name = "tanggal_mutasi", nullable = false)
    private LocalDateTime tanggalMutasi;

    @Column(columnDefinition = "TEXT")
    private String keterangan;

    // Getter & Setter sudah ada dan benar
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }

    public TipeMutasi getTipeMutasi() {
        return tipeMutasi;
    }

    public void setTipeMutasi(TipeMutasi tipeMutasi) {
        this.tipeMutasi = tipeMutasi;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public Gudang getGudangAsal() {
        return gudangAsal;
    }

    public void setGudangAsal(Gudang gudangAsal) {
        this.gudangAsal = gudangAsal;
    }

    public Gudang getGudangTujuan() {
        return gudangTujuan;
    }

    public void setGudangTujuan(Gudang gudangTujuan) {
        this.gudangTujuan = gudangTujuan;
    }

    public LocalDateTime getTanggalMutasi() {
        return tanggalMutasi;
    }

    public void setTanggalMutasi(LocalDateTime tanggalMutasi) {
        this.tanggalMutasi = tanggalMutasi;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}