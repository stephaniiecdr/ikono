package edu.pradita.p14.Tax.service;



//interface untuk mengatur strategi pembayaran pajak
public interface TaxCalculationStrategy {
    /**
     * @param baseAmount The base amount before tax.
     * @param taxRate The tax rate to apply (e.g., 0.10 for 10%).
     * @return The calculated tax amount.
     */
    double calculateTax(double baseAmount, double taxRate);
}

