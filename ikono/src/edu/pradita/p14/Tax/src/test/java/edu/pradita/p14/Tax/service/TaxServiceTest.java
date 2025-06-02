package edu.pradita.p14.Tax.service;

import edu.pradita.p14.Tax.tax.Tax;
import edu.pradita.p14.Tax.tax.TaxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; // Static imports for Mockito methods

/**
 * Class for testing TaxService.
 * Uses Mockito to mock the TaxRepository, ensuring only the service logic is tested.
 */
public class TaxServiceTest {

    @Mock // Creates a mock instance of TaxRepository
    private TaxRepository taxRepository;

    @InjectMocks // Injects the mock TaxRepository into TaxService
    private TaxService taxService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes mocks before each test
        // Set a default strategy for TaxService for tests that don't explicitly set one
        taxService.setTaxCalculationStrategy(new VATTaxStrategy());
    }

    @Test
    void testAddTax() {
        Tax tax = new Tax("Test Add", 0.05);
        taxService.addTax(tax);
        // Verify that the save method on taxRepository was called exactly once with the correct tax object
        verify(taxRepository, times(1)).save(tax);
    }

    @Test
    void testGetTaxById() {
        Tax tax = new Tax(1, "Test Get", 0.10);
        // Define mock behavior: when findById(1) is called, return this tax object
        when(taxRepository.findById(1)).thenReturn(tax);

        Tax foundTax = taxService.getTaxById(1);
        assertNotNull(foundTax);
        assertEquals("Test Get", foundTax.getTaxName());
        verify(taxRepository, times(1)).findById(1); // Verify the call
    }

    @Test
    void testGetAllTaxes() {
        List<Tax> taxes = Arrays.asList(
                new Tax(1, "VAT", 0.10),
                new Tax(2, "Sales", 0.07)
        );
        when(taxRepository.findAll()).thenReturn(taxes); // Mock repository to return a list of taxes

        List<Tax> allTaxes = taxService.getAllTaxes();
        assertNotNull(allTaxes);
        assertEquals(2, allTaxes.size());
        verify(taxRepository, times(1)).findAll(); // Verify the call
    }

    @Test
    void testUpdateTax() {
        Tax tax = new Tax(1, "Old Name", 0.05);
        taxService.updateTax(tax);
        verify(taxRepository, times(1)).update(tax); // Verify the call
    }

    @Test
    void testDeleteTax() {
        taxService.deleteTax(1);
        verify(taxRepository, times(1)).delete(1); // Verify the call
    }

    @Test
    void testCalculateTaxAmountWithVATStrategy() {
        Tax tax = new Tax("VAT", 0.10);
        taxService.setTaxCalculationStrategy(new VATTaxStrategy()); // Ensure the strategy is set

        double amount = 100.0;
        double calculatedTax = taxService.calculateTaxAmount(amount, tax);
        assertEquals(10.0, calculatedTax, 0.001); // 100 * 0.10 = 10
    }

    @Test
    void testCalculateTaxAmountWithSalesTaxStrategy() {
        Tax tax = new Tax("Sales Tax", 0.07);
        taxService.setTaxCalculationStrategy(new SalesTaxStrategy()); // Change the strategy for this test

        double amount = 200.0;
        double calculatedTax = taxService.calculateTaxAmount(amount, tax);
        assertEquals(14.0, calculatedTax, 0.001); // 200 * 0.07 = 14
    }

    @Test
    void testCalculateTotalWithTax() {
        Tax tax = new Tax("VAT", 0.10);
        taxService.setTaxCalculationStrategy(new VATTaxStrategy());

        double baseAmount = 100.0;
        double totalWithTax = taxService.calculateTotalWithTax(baseAmount, tax);
        assertEquals(110.0, totalWithTax, 0.001); // 100 + (100 * 0.10) = 110
    }
}
