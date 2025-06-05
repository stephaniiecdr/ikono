package com.example.pos.dao;

import com.example.pos.model.Produk;
import java.util.List;
import java.util.Optional;

public interface ProdukDao {
    void save(Produk produk);
    void update(Produk produk);
    Optional<Produk> findById(int id);
    Optional<Produk> findByKode(String kodeProduk);
    List<Produk> findAll();
    List<Produk> searchByNama(String nama); // Untuk pencarian produk
}