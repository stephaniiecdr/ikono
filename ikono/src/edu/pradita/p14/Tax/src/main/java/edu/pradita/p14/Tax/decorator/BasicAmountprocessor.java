package edu.pradita.p14.Tax.decorator;

import edu.pradita.p14.Tax.tax.Tax;

/**
 * Implementasi dasar dari CalculableAmountProcessor.
 * Mengembalikan jumlah asli tanpa perubahan, berfungsi sebagai dasar untuk dekorasi.
 * Ini adalah Concrete Component dalam pola Decorator.
 */
public class BasicAmountprocessor implements CalculableAmountProcessor {
    @Override
    public double processAmount(double baseAmount, Tax tax) {
        return baseAmount; // Mengembalikan jumlah dasar tanpa perubahan
    }
}
