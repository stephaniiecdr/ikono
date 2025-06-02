package edu.pradita.p14.Tax.decorator;

import edu.pradita.p14.Tax.tax.Tax;

public interface CalculableAmountProcessor {
    /**
     * Memproses jumlah dasar, menerapkan perhitungan tertentu.
     * @param baseAmount Jumlah awal yang akan diproses.
     * @param tax Objek Pajak yang mungkin relevan untuk perhitungan (jika diperlukan oleh decorator).
     * @return Jumlah yang diproses.
     */
    double processAmount(double baseAmount, Tax tax);
}
