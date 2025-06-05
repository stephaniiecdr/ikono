package com.mutasistok.service.strategy;

import com.mutasistok.model.MutasiStok;
import org.hibernate.Session;

public interface MutasiStrategy {
    void execute(Session session, MutasiStok mutasi);
}