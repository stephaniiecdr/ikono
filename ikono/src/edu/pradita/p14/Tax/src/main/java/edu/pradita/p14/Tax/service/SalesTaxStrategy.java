package edu.pradita.p14.Tax.service;

public class SalesTaxStrategy implements TaxCalculationStrategy{
    @Override
    public double calculateTax(double baseAmount, double taxRate) {
        // Specific Sales Tax calculation logic (might differ from VAT in real life)
        return baseAmount * taxRate;
    }
}
