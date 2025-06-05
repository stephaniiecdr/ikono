package com.example.pos.patterns.behavioral;

import com.example.pos.model.Produk;


public class PenguranganStokStrategy implements KoreksiStokStrategy {

    @Override
    public int koreksi(Produk produk, int jumlahPengurangan) throws IllegalArgumentException {
        if (jumlahPengurangan <= 0) {
            throw new IllegalArgumentException("Jumlah pengurangan harus merupakan angka positif.");
        }
        if (produk.getStok() < jumlahPengurangan) {
            throw new IllegalArgumentException("Stok tidak mencukupi untuk pengurangan sejumlah " + jumlahPengurangan +
                    ". Stok saat ini: " + produk.getStok() + ".");
        }
        int stokBaru = produk.getStok() - jumlahPengurangan;
        produk.setStok(stokBaru);
        return stokBaru;
    }

    /**
     * Mengembalikan tipe koreksi sebagai "PENGURANGAN_STOK".
     * @return String "PENGURANGAN_STOK".
     */
    @Override
    public String getTipeKoreksi() {
        return "PENGURANGAN_STOK";
    }
}
