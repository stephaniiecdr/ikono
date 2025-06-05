// File: src/main/java/com/mutasistok/dao/StokGudangDao.java
package com.mutasistok.dao;

import com.mutasistok.model.Barang;
import com.mutasistok.model.Gudang;
import com.mutasistok.model.StokGudang;
import com.mutasistok.model.StokGudangId;
import org.hibernate.Session;
import javax.persistence.NoResultException;

public class StokGudangDao {

    /**
     * Helper method untuk mengambil StokGudang.
     */
    private StokGudang getStokGudang(Session session, Long barangId, int gudangId) {
        try {
            return session.createQuery(
                            "from StokGudang where id.barangId = :barangId and id.gudangId = :gudangId", StokGudang.class)
                    .setParameter("barangId", barangId)
                    .setParameter("gudangId", gudangId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Mengembalikan null jika tidak ditemukan, ini sudah benar.
        }
    }

    /**
     * Menambah stok barang di gudang.
     * Jika record stok belum ada, maka akan dibuatkan yang baru.
     * @param session Sesi Hibernate aktif.
     * @param barangId ID barang.
     * @param gudangId ID gudang.
     * @param jumlah Jumlah yang akan ditambahkan.
     */
    public void tambahStok(Session session, Long barangId, int gudangId, int jumlah) {
        StokGudang stok = getStokGudang(session, barangId, gudangId);
        if (stok != null) {
            // Jika stok sudah ada, update jumlahnya
            stok.setStok(stok.getStok() + jumlah);
            session.update(stok);
        } else {
            // PERBAIKAN: Jika stok belum ada, buat record baru.
            // Ini penting untuk mutasi MASUK pertama kali untuk sebuah barang di gudang.
            StokGudang stokBaru = new StokGudang();
            StokGudangId idBaru = new StokGudangId();
            idBaru.setBarangId(barangId);
            idBaru.setGudangId(gudangId);

            stokBaru.setId(idBaru);
            // Ambil referensi objek Barang dan Gudang tanpa query baru ke DB
            stokBaru.setBarang(session.load(Barang.class, barangId));
            stokBaru.setGudang(session.load(Gudang.class, gudangId));
            stokBaru.setStok(jumlah);

            session.save(stokBaru);
        }
    }

    /**
     * Mengurangi stok barang di gudang.
     * Melemparkan exception jika stok tidak cukup atau tidak ditemukan.
     * @param session Sesi Hibernate aktif.
     * @param barangId ID barang.
     * @param gudangId ID gudang.
     * @param jumlah Jumlah yang akan dikurangi.
     */
    public void kurangiStok(Session session, Long barangId, int gudangId, int jumlah) {
        StokGudang stok = getStokGudang(session, barangId, gudangId);
        if (stok != null) {
            int stokAkhir = stok.getStok() - jumlah;
            if (stokAkhir < 0) {
                // Pesan error lebih spesifik
                throw new RuntimeException("Stok tidak mencukupi di gudang. Stok saat ini: " + stok.getStok() + ", butuh: " + jumlah);
            }
            stok.setStok(stokAkhir);
            session.update(stok);
        } else {
            // Pesan error lebih spesifik
            throw new RuntimeException("Tidak ditemukan data stok untuk barang di gudang asal. Tidak bisa mengurangi stok.");
        }
    }
}