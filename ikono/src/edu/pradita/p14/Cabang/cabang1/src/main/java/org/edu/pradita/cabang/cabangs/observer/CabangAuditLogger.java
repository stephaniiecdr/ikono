package org.edu.pradita.cabang.cabangs.observer;

import org.edu.pradita.cabang.cabangs.Cabang;
import org.edu.pradita.cabang.cabangs.observer.CabangDataObserver;

//  Contoh komponen yang perlu log perubahan data cabang.
public class CabangAuditLogger implements CabangDataObserver {
    @Override
    public void onCabangDataChanged(Cabang cabang, String changeType) {
        System.out.println("[AUDIT LOG] Cabang Data Changed: " +
                "Type=" + changeType +
                " \", ID=\" + (cabang.getIdCabang() : \"N/A\") +\n" +
                ", Nama=" + cabang.getNamaCabang());

                // nulis ke file log
    }
}
