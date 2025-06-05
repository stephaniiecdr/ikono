package com.example.pos.patterns.structural;

import com.example.pos.model.Produk; // Import entitas Produk

public class ProdukAdapter {


    public static Produk dtoToEntity(ProdukDTO dto) {
        if (dto == null) {
            return null;
        }
        Produk produk = new Produk();
        produk.setKodeProduk(dto.getKode());
        produk.setNamaProduk(dto.getNama());
        produk.setStok(dto.getKuantitas()); // Asumsi kuantitas di DTO adalah stok awal


        produk.setHargaBeli(0.0); // Nilai default atau placeholder
        produk.setHargaJual(0.0); // Nilai default atau placeholder

        return produk;
    }

    public static ProdukDTO entityToDto(Produk produk) {
        if (produk == null) {
            return null;
        }
        return new ProdukDTO(
                produk.getKodeProduk(),
                produk.getNamaProduk(),
                produk.getStok()
        );
    }
}
