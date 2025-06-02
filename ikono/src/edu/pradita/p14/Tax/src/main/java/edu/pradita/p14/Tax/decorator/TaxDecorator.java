package edu.pradita.p14.Tax.decorator;

import edu.pradita.p14.Tax.tax.Tax;
import edu.pradita.p14.Tax.service.TaxService; // Untuk menggunakan logika calculateTaxAmount

/**
 * Dekorator yang menambahkan perhitungan pajak ke jumlah.
 * Ini adalah Concrete Decorator dalam pola Decorator.
 */
public class TaxDecorator extends AmountProcessorDecorator {

    private TaxService taxService;
    // Kita akan menggunakan TaxService untuk logika perhitungan pajak
    // Karena TaxService sudah punya Strategy Pattern untuk calculateTaxAmount

    public TaxDecorator(CalculableAmountProcessor decoratedProcessor, TaxService taxService) {
        super(decoratedProcessor);
        this.taxService = taxService;
    }

    @Override
    public double processAmount(double baseAmount, Tax tax) {
        // Pertama, proses jumlah dengan komponen yang didekorasi (misalnya, jumlah setelah diskon)
        double processedAmount = decoratedProcessor.processAmount(baseAmount, tax);

        // Kemudian, tambahkan perhitungan pajak menggunakan TaxService
        if (tax != null) {
            double taxAmount = taxService.calculateTaxAmount(processedAmount, tax);
            return processedAmount + taxAmount;
        }
        return processedAmount;
    }
}