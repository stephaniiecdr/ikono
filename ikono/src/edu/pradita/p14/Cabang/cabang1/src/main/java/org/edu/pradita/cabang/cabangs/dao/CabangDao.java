package org.edu.pradita.cabang.cabangs.dao;

import org.edu.pradita.cabang.cabangs.Cabang;
import java.util.List;
import java.util.Optional;

public interface CabangDao {
    void save(Cabang cabang);
    Optional<Cabang> findById(Long id);
    List<Cabang> findAll();
    void update(Cabang cabang);
    void delete(Cabang cabang);
    void deleteById(Long id);
    Optional<Cabang> findByNama(String nama);   // cnth custom query
}
