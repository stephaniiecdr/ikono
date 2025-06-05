package org.edu.pradita.cabang.cabangs.legacy;

import java.util.HashMap;
import java.util.Map;

public class InformasiSistemCabangLama {

        public Map<String, String> getInfoCabangLegacy(String kodeCabangLegacy) {
            Map<String, String> data = new HashMap<>();
            if ("1".equalsIgnoreCase(kodeCabangLegacy)) {
                data.put("nama_cabang", "Cabang Yogyakarta");
                data.put("alamat_cabang", "Jl. Malioboro");
                data.put("nomor_telepon", "0812-1112-0300");
                data.put("status_operasional", "AKTIF");
                return data;
            } else if ("2".equalsIgnoreCase(kodeCabangLegacy)) {
                data.put("nama_cabang", "Cabang Cirebon");
                data.put("location_street", "Jl. Pantura");
                return data;
            }
            return null;
        }
    }

