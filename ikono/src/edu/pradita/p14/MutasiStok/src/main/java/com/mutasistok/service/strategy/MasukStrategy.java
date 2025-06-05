package com.mutasistok.service.strategy;

import com.mutasistok.dao.StokGudangDao;
import com.mutasistok.model.MutasiStok;
import org.hibernate.Session;

public class MasukStrategy implements MutasiStrategy {
    private final StokGudangDao stokGudangDao;

    public MasukStrategy(StokGudangDao stokGudangDao) {
        this.stokGudangDao = stokGudangDao;
    }

    @Override
    public void execute(Session session, MutasiStok mutasi) {
        stokGudangDao.tambahStok(
                session,
                mutasi.getBarang().getId(),
                mutasi.getGudangTujuan().getId(),
                mutasi.getJumlah()
        );
    }
}