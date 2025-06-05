package com.example.pos.patterns.behavioral;

import com.example.pos.model.Produk;

public interface KoreksiStokStrategy {

    int koreksi(Produk produk, int jumlah) throws IllegalArgumentException;


    String getTipeKoreksi();
}
