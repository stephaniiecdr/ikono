package edu.pradita.p14.Tax.service;

import edu.pradita.p14.Tax.tax.Tax;
import edu.pradita.p14.Tax.tax.TaxRepository;

import java.util.List;

/**
 * Kelas layanan untuk mengelola logika bisnis terkait pajak.
 * Perantara antara Controller dan Repository.
 */
public class TaxService {

    private TaxRepository taxRepository;
    private TaxCalculationStrategy currentTaxStrategy;

    public TaxService() {
        this.taxRepository = new TaxRepository();
        this.currentTaxStrategy = new VATTaxStrategy();// Inisialisasi TaxRepository
    }

    public void setTaxCalculationStrategy(TaxCalculationStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("TaxCalculationStrategy cannot be null.");
        }
        this.currentTaxStrategy = strategy;
    }

    public void addTax(Tax tax) {

        taxRepository.save(tax);
    }

    public Tax getTaxById(int id) {
        return taxRepository.findById(id);
    }

    public List<Tax> getAllTaxes() {
        return taxRepository.findAll();
    }

    public void updateTax(Tax tax) {
        taxRepository.update(tax);
    }

    public void deleteTax(int id) {
        taxRepository.delete(id);
    }


    public double calculateTaxAmount(double baseAmount, Tax tax) {
        if (tax == null) {
            System.err.println("Tax object is null for calculation.");
            return 0.0;
        }
        // gunakan strategi yang sedang aktif
        return currentTaxStrategy.calculateTax(baseAmount, tax.getTaxRate());
    }

    public double calculateTotalWithTax(double baseAmount, Tax tax) {
        double taxAmount = calculateTaxAmount(baseAmount, tax);
        return baseAmount + taxAmount;
    }
}

