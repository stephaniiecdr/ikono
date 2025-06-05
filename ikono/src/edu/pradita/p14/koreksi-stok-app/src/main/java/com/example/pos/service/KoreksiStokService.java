package com.example.pos.service;

import com.example.pos.dao.KoreksiStokDao;
import com.example.pos.dao.ProdukDao;
import com.example.pos.dao.impl.KoreksiStokDaoImpl;
import com.example.pos.dao.impl.ProdukDaoImpl;
import com.example.pos.model.KoreksiStok;
import com.example.pos.model.Produk;
import com.example.pos.patterns.behavioral.KoreksiStokStrategy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class KoreksiStokService {

    public final ProdukDao produkDao;
    private final KoreksiStokDao koreksiStokDao;

    public KoreksiStokService() {
        this.produkDao = new ProdukDaoImpl();
        this.koreksiStokDao = new KoreksiStokDaoImpl();
    }

    public KoreksiStokService(ProdukDao produkDao, KoreksiStokDao koreksiStokDao) {
        this.produkDao = produkDao;
        this.koreksiStokDao = koreksiStokDao;
    }


    public boolean prosesKoreksiStok(int idProduk, int jumlahKoreksi, KoreksiStokStrategy strategy, String catatan)
            throws IllegalArgumentException {

        // ---- PERHATIKAN BAGIAN INI ----
        // Validasi jumlahKoreksi HARUS menjadi yang pertama
        if (jumlahKoreksi <= 0) {
            throw new IllegalArgumentException("Jumlah koreksi harus lebih besar dari 0.");
        }
        // -----------------------------

        Optional<Produk> produkOptional = produkDao.findById(idProduk);
        if (produkOptional.isEmpty()) {
            System.err.println("Produk dengan ID " + idProduk + " tidak ditemukan saat proses koreksi.");
            return false;
        }

        Produk produk = produkOptional.get();
        int stokSebelum = produk.getStok();

        int stokSesudah = strategy.koreksi(produk, jumlahKoreksi);

        produkDao.update(produk);

        int nilaiKoreksiUntukLog = jumlahKoreksi;
        if (strategy.getTipeKoreksi().toLowerCase().contains("pengurangan")) {
            nilaiKoreksiUntukLog = -Math.abs(jumlahKoreksi);
        } else {
            nilaiKoreksiUntukLog = Math.abs(jumlahKoreksi);
        }

        KoreksiStok koreksi = new KoreksiStok(
                produk,
                nilaiKoreksiUntukLog,
                stokSebelum,
                stokSesudah,
                strategy.getTipeKoreksi(),
                catatan
        );
        koreksi.setWaktuKoreksi(LocalDateTime.now());
        koreksiStokDao.save(koreksi);

        return true;
    }

    public Optional<Produk> getProdukById(int idProduk) {
        return produkDao.findById(idProduk);
    }

    public Optional<Produk> getProdukByKode(String kodeProduk) {
        return produkDao.findByKode(kodeProduk);
    }

    public List<Produk> cariProdukByNama(String nama) {
        return produkDao.searchByNama(nama);
    }

    public List<KoreksiStok> getHistoriKoreksiByProduk(int idProduk) {
        return koreksiStokDao.findByProdukId(idProduk);
    }

    public List<Produk> getAllProduk() {
        return produkDao.findAll();
    }
}
