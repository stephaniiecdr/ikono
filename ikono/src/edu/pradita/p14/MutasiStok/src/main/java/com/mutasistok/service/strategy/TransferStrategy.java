package com.mutasistok.service.strategy;

import com.mutasistok.dao.StokGudangDao;
import com.mutasistok.model.MutasiStok;
import org.hibernate.Session;

public class TransferStrategy implements MutasiStrategy {
    private final StokGudangDao stokGudangDao;

    public TransferStrategy(StokGudangDao stokGudangDao) {
        this.stokGudangDao = stokGudangDao;
    }

    @Override
    public void execute(Session session, MutasiStok mutasi) {
        stokGudangDao.kurangiStok(
                session,
                mutasi.getBarang().getId(),
                mutasi.getGudangAsal().getId(),
                mutasi.getJumlah()
        );
        stokGudangDao.tambahStok(
                session,
                mutasi.getBarang().getId(),
                mutasi.getGudangTujuan().getId(),
                mutasi.getJumlah()
        );
    }
}