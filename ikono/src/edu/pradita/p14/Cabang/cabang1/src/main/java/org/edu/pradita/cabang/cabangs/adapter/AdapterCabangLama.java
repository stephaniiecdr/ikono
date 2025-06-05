package org.edu.pradita.cabang.cabangs.adapter;

import org.edu.pradita.cabang.cabangs.legacy.InformasiSistemCabangLama;
import org.edu.pradita.cabang.cabangs.Cabang;
import org.edu.pradita.cabang.cabangs.service.PenyediaDataCabang;

import java.util.Map;
import java.util.Optional;

public class AdapterCabangLama implements PenyediaDataCabang {

    private final InformasiSistemCabangLama sistemLama;

    public AdapterCabangLama(InformasiSistemCabangLama sistemLama) {
        if (sistemLama == null) {
            throw new IllegalArgumentException("InformasiSistemCabangLama tidak boleh null.");
        }
        this.sistemLama = sistemLama;
    }

    @Override
    public Optional<Cabang> dapatkanCabang(String identifierLegacy) {
        Map<String, String> dataLegacy = sistemLama.getInfoCabangLegacy(identifierLegacy);

        if (dataLegacy == null || dataLegacy.isEmpty()) {
            return Optional.empty();
        }

        String nama = dataLegacy.get("branch_name");
        String alamat = dataLegacy.get("location_street");
        String telepon = dataLegacy.get("contact_phone");

        if (nama != null) {
            Cabang cabangModern = new Cabang();
            cabangModern.setNamaCabang(nama);
            cabangModern.setAlamatCabang(alamat);
            cabangModern.setTeleponCabang(telepon);
            // ID Cabang tidak di set dari sistem legacy di contoh ini

            return Optional.of(cabangModern);
        }
        return Optional.empty();
    }
}

