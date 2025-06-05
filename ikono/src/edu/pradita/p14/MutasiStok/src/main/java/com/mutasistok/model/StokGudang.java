package com.mutasistok.model;

import javax.persistence.*;

@Entity
@Table(name = "StokGudang")
public class StokGudang {

    @EmbeddedId
    private StokGudangId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("barangId")
    @JoinColumn(name = "barang_id")
    private Barang barang;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("gudangId")
    @JoinColumn(name = "gudang_id")
    private Gudang gudang;

    private int stok;

    // --- GETTER DAN SETTER YANG DITAMBAHKAN ---
    public StokGudangId getId() {
        return id;
    }

    public void setId(StokGudangId id) {
        this.id = id;
    }

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }

    public Gudang getGudang() {
        return gudang;
    }

    public void setGudang(Gudang gudang) {
        this.gudang = gudang;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }
}