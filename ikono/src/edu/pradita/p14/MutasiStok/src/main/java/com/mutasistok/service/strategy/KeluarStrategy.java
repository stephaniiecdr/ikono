package com.mutasistok.service.strategy;

import com.mutasistok.dao.StokGudangDao;
import com.mutasistok.model.MutasiStok;
import org.hibernate.Session;

public class KeluarStrategy implements MutasiStrategy {
    private final StokGudangDao stokGudangDao;

    public KeluarStrategy(StokGudangDao stokGudangDao) {
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
    }
}