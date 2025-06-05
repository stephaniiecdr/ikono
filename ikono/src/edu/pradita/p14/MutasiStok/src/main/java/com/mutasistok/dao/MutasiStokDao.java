package com.mutasistok.dao;

import com.mutasistok.model.MutasiStok;
import com.mutasistok.util.HibernateUtil;
import org.hibernate.Session;
import java.util.List;

public class MutasiStokDao {

    /**
     * Menyimpan objek MutasiStok di dalam transaksi yang sudah ada (dikelola oleh Service).
     * @param session Sesi Hibernate yang aktif.
     * @param mutasi Objek MutasiStok yang akan disimpan.
     */
    public void save(Session session, MutasiStok mutasi) {
        // Method ini tidak mengelola transaksi sendiri, karena dipanggil dari Service
        session.save(mutasi);
    }

    /**
     * Mengambil semua data riwayat mutasi dari database.
     * @return List yang berisi semua objek MutasiStok.
     */
    public List<MutasiStok> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Mengambil data dan melakukan fetch untuk relasi agar tidak lazy loading
            return session.createQuery("select m from MutasiStok m " +
                            "join fetch m.barang " +
                            "left join fetch m.gudangAsal " +
                            "left join fetch m.gudangTujuan", MutasiStok.class)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}