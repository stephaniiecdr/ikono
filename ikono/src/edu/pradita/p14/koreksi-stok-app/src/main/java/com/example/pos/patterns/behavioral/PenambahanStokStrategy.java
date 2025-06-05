package com.example.pos.patterns.behavioral;

import com.example.pos.model.Produk;


public class PenambahanStokStrategy implements KoreksiStokStrategy {

    @Override
    public int koreksi(Produk produk, int jumlahPenambahan) throws IllegalArgumentException {
        if (jumlahPenambahan <= 0) {
            throw new IllegalArgumentException("Jumlah penambahan harus merupakan angka positif.");
        }
        int stokBaru = produk.getStok() + jumlahPenambahan;
        produk.setStok(stokBaru);
        return stokBaru;
    }


    @Override
    public String getTipeKoreksi() {
        return "PENAMBAHAN_STOK";
    }
}
