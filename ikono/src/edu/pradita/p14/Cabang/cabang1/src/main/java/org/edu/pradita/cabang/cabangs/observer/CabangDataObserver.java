package org.edu.pradita.cabang.cabangs.observer;

import org.edu.pradita.cabang.cabangs.Cabang;


public interface CabangDataObserver {
    void onCabangDataChanged(Cabang cabang, String changeType); // changeType: "CREATED", "UPDATED", "DELETED"
}