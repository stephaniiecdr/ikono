package org.edu.pradita.cabang.cabangs.service;

import java.util.List;
import java.util.Optional;
import org.edu.pradita.cabang.cabangs.Cabang;

public interface CabangService {
    Cabang createCabang(String namaCabang, String alamat, String nomorTelepon) throws Exception;
    Optional<Cabang> getCabangById(Long id);
    List<Cabang> getAllCabang();
    Cabang updateCabang(Long id, String namaCabang, String alamat, String nomorTelepon) throws Exception;
    void deleteCabang(Long id) throws Exception;
    boolean isNamaCabangExist(String namaCabang, Long currentId);
}
