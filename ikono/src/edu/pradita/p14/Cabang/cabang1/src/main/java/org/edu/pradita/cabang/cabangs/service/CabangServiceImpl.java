package org.edu.pradita.cabang.cabangs.service;

import org.edu.pradita.cabang.cabangs.dao.CabangDao;
import org.edu.pradita.cabang.cabangs.Cabang;
import org.edu.pradita.cabang.cabangs.observer.CabangDataObserver;
import org.edu.pradita.cabang.cabangs.observer.CabangDataSubject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CabangServiceImpl implements CabangService, CabangDataSubject {

    private final CabangDao cabangDao; // Dependensi ke interface DAO (DIP)
    private final List<CabangDataObserver> observers = new ArrayList<>();


    public CabangServiceImpl(CabangDao cabangDao) {
        this.cabangDao = cabangDao;
    }

    @Override
    public void addObserver(CabangDataObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(CabangDataObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Cabang cabang, String changeType) {
        for (CabangDataObserver observer : observers) {
            observer.onCabangDataChanged(cabang, changeType);
        }
    }

    @Override
    public Cabang createCabang(String namaCabang, String alamat, String nomorTelepon) throws Exception {
        if (namaCabang == null || namaCabang.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama cabang tidak boleh kosong.");
        }
        if (isNamaCabangExist(namaCabang, null)) {
            throw new Exception("Nama cabang '" + namaCabang + "' sudah ada.");
        }

        Cabang cabang = new Cabang(namaCabang.trim(), alamat, nomorTelepon);
        cabangDao.save(cabang);
        notifyObservers(cabang, "CREATED");
        return cabang;
    }

    @Override
    public Optional<Cabang> getCabangById(Long id) {
        return cabangDao.findById(id);
    }

    @Override
    public List<Cabang> getAllCabang() {
        return cabangDao.findAll();
    }

    @Override
    public Cabang updateCabang(Long idCabang, String namaCabang, String alamatCabang, String teleponCabang) throws Exception {
        if (idCabang == null) {
            throw new IllegalArgumentException("ID Cabang tidak boleh null untuk update.");
        }
        if (namaCabang == null || namaCabang.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama cabang tidak boleh kosong.");
        }
        if (isNamaCabangExist(namaCabang, idCabang)) {
            throw new Exception("Nama cabang '" + namaCabang + "' sudah digunakan oleh cabang lain.");
        }

        Optional<Cabang> existingCabangOpt = cabangDao.findById(idCabang);
        if (existingCabangOpt.isEmpty()) {
            throw new Exception("Cabang dengan ID " + idCabang + " tidak ditemukan.");
        }

        Cabang cabangToUpdate = existingCabangOpt.get();
        cabangToUpdate.setNamaCabang(namaCabang.trim());
        cabangToUpdate.setAlamatCabang(alamatCabang);
        cabangToUpdate.setTeleponCabang(teleponCabang);

        cabangDao.update(cabangToUpdate);
        notifyObservers(cabangToUpdate, "UPDATED");
        return cabangToUpdate;
    }

    @Override
    public void deleteCabang(Long id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("ID Cabang tidak boleh null untuk dihapus.");
        }
        Optional<Cabang> existingCabangOpt = cabangDao.findById(id);
        if (existingCabangOpt.isEmpty()) {
            throw new Exception("Cabang dengan ID " + id + " tidak ditemukan untuk dihapus.");
        }

        Cabang cabangToDelete = existingCabangOpt.get();
        cabangDao.deleteById(id);
        notifyObservers(cabangToDelete,"DELETED");
    }


    @Override
    public boolean isNamaCabangExist(String namaCabang, Long currentId) {
        Optional<Cabang> existingCabang = cabangDao.findByNama(namaCabang.trim());
        if (existingCabang.isPresent()) {
            return currentId == null || existingCabang.get().getIdCabang() != currentId;
        }
        return false;
    }
}
