package org.edu.pradita.cabang.cabangs.service;

import org.edu.pradita.cabang.cabangs.Cabang;
import java.util.Optional;

public interface PenyediaDataCabang {
    Optional<Cabang> dapatkanCabang(String identifier);
}

