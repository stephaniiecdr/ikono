package org.edu.pradita.cabang.cabangs.observer;

import org.edu.pradita.cabang.cabangs.Cabang;

public interface CabangDataSubject {
    void addObserver(CabangDataObserver observer);
    void removeObserver(CabangDataObserver observer);
    void notifyObservers(Cabang cabang, String changeType);
}
