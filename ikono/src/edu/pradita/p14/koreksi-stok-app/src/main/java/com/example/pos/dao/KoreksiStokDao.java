package com.example.pos.dao;

import com.example.pos.model.KoreksiStok;
import java.util.List;

public interface KoreksiStokDao {
    void save(KoreksiStok koreksiStok);
    List<KoreksiStok> findByProdukId(int idProduk);
    List<KoreksiStok> findAll();
}