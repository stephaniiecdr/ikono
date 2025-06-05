package com.mutasistok.service;

import com.mutasistok.dao.MutasiStokDao;
import com.mutasistok.dao.StokGudangDao;
import com.mutasistok.model.MutasiStok;
import com.mutasistok.model.TipeMutasi;
import com.mutasistok.service.strategy.KeluarStrategy;
import com.mutasistok.service.strategy.MasukStrategy;
import com.mutasistok.service.strategy.MutasiStrategy;
import com.mutasistok.service.strategy.TransferStrategy;
import com.mutasistok.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.Map;

public class StokService {
    private final MutasiStokDao mutasiStokDao;
    private final StokGudangDao stokGudangDao;
    private final Map<TipeMutasi, MutasiStrategy> strategies;

    public StokService() {
        this.mutasiStokDao = new MutasiStokDao();
        this.stokGudangDao = new StokGudangDao();
        this.strategies = new HashMap<>();

        // Berikan (suntikkan) objek stokGudangDao ke setiap strategi
        this.strategies.put(TipeMutasi.MASUK, new MasukStrategy(stokGudangDao));
        this.strategies.put(TipeMutasi.KELUAR, new KeluarStrategy(stokGudangDao));
        this.strategies.put(TipeMutasi.TRANSFER, new TransferStrategy(stokGudangDao));
    }

    public void prosesMutasi(MutasiStok mutasi) throws Exception {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            mutasiStokDao.save(session, mutasi);

            MutasiStrategy strategy = strategies.get(mutasi.getTipeMutasi());
            if (strategy == null) {
                throw new IllegalArgumentException("Tipe mutasi tidak dikenal: " + mutasi.getTipeMutasi());
            }

            strategy.execute(session, mutasi);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new Exception("Gagal memproses mutasi: " + e.getMessage(), e);
        }
    }
}