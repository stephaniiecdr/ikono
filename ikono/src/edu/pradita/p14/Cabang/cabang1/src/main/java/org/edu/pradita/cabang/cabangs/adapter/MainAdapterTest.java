package org.edu.pradita.cabang.cabangs.adapter;

import org.edu.pradita.cabang.cabangs.adapter.AdapterCabangLama;
import org.edu.pradita.cabang.cabangs.legacy.InformasiSistemCabangLama;
import org.edu.pradita.cabang.cabangs.Cabang;
import org.edu.pradita.cabang.cabangs.service.PenyediaDataCabang;

import java.util.Optional;

public class MainAdapterTest {
    public static void main(String[] args) {
        InformasiSistemCabangLama sistemInfoLama = new InformasiSistemCabangLama();
        PenyediaDataCabang penyediaData = new AdapterCabangLama(sistemInfoLama);

        String kodeCabang1 = "1";
        System.out.println("Mencari cabang dengan kode: " + kodeCabang1);
        Optional<Cabang> optCabang1 = penyediaData.dapatkanCabang(kodeCabang1);

        optCabang1.ifPresentOrElse(
                cabang -> System.out.println("Ditemukan (via Adapter): " + cabang),
                () -> System.out.println("Cabang dengan kode '" + kodeCabang1 + "' tidak ditemukan.")
        );

        System.out.println("\n----------------------------------\n");

        String kodeCabangSalah = "TIDAK-ADA";
        System.out.println("Mencari cabang dengan kode: " + kodeCabangSalah);
        Optional<Cabang> optCabangSalah = penyediaData.dapatkanCabang(kodeCabangSalah);
        if (optCabangSalah.isEmpty()) {
            System.out.println("Cabang dengan kode '" + kodeCabangSalah + "' tidak ditemukan, sesuai harapan.");
        }
    }
}
