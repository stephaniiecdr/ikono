package edu.pradita.p14.Tax.service;

public class VATTaxStrategy implements TaxCalculationStrategy {
    @Override
    public double calculateTax(double baseAmount, double taxRate) {
        // Simple VAT calculation: tax is a percentage of the base amount
        return baseAmount * taxRate;
    }
}
