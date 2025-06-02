package edu.pradita.p14.Tax.decorator;

import edu.pradita.p14.Tax.tax.Tax;

/**
 * Kelas abstrak dasar untuk semua Decorator.
 * Mengimplementasikan CalculableAmountProcessor dan memiliki referensi ke CalculableAmountProcessor lainnya.
 * Ini adalah Decorator dalam pola Decorator.
 */
public abstract class AmountProcessorDecorator implements CalculableAmountProcessor {
    protected CalculableAmountProcessor decoratedProcessor;

    public AmountProcessorDecorator(CalculableAmountProcessor decoratedProcessor) {
        this.decoratedProcessor = decoratedProcessor;
    }

    /**
     * Delegasikan pemrosesan ke komponen yang didekorasi.
     * Metode ini akan di-override oleh dekorator konkret untuk menambahkan fungsionalitas.
     */
    @Override
    public double processAmount(double baseAmount, Tax tax) {
        return decoratedProcessor.processAmount(baseAmount, tax);
    }
}
