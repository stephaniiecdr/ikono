package org.edu.pradita.main.adapter;

import org.edu.pradita.main.model.Product;
import org.edu.pradita.main.model.User;
import org.edu.pradita.main.model.Transaction;
import org.edu.pradita.main.model.dto.SalesReportItemDTO;
import org.edu.pradita.main.model.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionAdapterTest {

    private TransactionAdapter transactionAdapter;

    @BeforeEach
    void setUp() {
        // Inisialisasi adapter sebelum setiap tes
        transactionAdapter = new TransactionAdapter();
    }

    @Test
    void testToSalesReportItemDTO_withValidTransaction_shouldMapCorrectly() {
        // 1. Arrange (Persiapan Data Input)
        User user = new User();
        user.setUserId(1);
        user.setUsername("Budi Tester");

        Product product = new Product();
        product.setProductId(101);
        product.setProductName("Kopi Enak");
        // product.setUnit("bungkus"); // Dihilangkan sesuai skema baru

        Transaction transaction = new Transaction();
        transaction.setTransactionId(1001);
        transaction.setUser(user);
        transaction.setProduct(product);
        transaction.setQuantity(2);
        transaction.setTotalPrice(new BigDecimal("50000.00"));
        transaction.setPaymentStatus(PaymentStatus.completed);
        transaction.setTransactionDate(LocalDateTime.now());

        // 2. Act (Panggil Metode yang Diuji)
        SalesReportItemDTO dto = transactionAdapter.toSalesReportItemDTO(transaction);

        // 3. Assert (Verifikasi Output)
        assertNotNull(dto, "DTO tidak boleh null");
        assertEquals(String.valueOf(transaction.getTransactionId()), dto.getOrderNo(), "OrderNo tidak sesuai");
        assertEquals(user.getUsername(), dto.getCustomerName(), "CustomerName tidak sesuai");
        assertEquals(transaction.getQuantity(), dto.getQuantity(), "Quantity tidak sesuai");
        // Perbandingan double memerlukan delta karena potensi ketidakpresisian floating point
        assertEquals(transaction.getTotalPrice().doubleValue(), dto.getTotalPrice(), 0.001, "TotalPrice tidak sesuai");
        assertEquals(transaction.getPaymentStatus().name(), dto.getPaymentStatus(), "PaymentStatus tidak sesuai");
        assertEquals(product.getProductName(), dto.getProductDescription(), "ProductDescription tidak sesuai");
        // assertNull(dto.getProductUnit(), "ProductUnit seharusnya null atau tidak ada"); // Jika DTO masih punya field ini
    }

    @Test
    void testToSalesReportItemDTO_withNullTransaction_shouldReturnNull() {
        // 1. Arrange
        Transaction transaction = null;

        // 2. Act
        SalesReportItemDTO dto = transactionAdapter.toSalesReportItemDTO(transaction);

        // 3. Assert
        assertNull(dto, "DTO seharusnya null jika input Transaction null");
    }

    @Test
    void testToSalesReportItemDTO_withPartialDataInTransaction_shouldHandleGracefully() {
        // 1. Arrange
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1002);
        // User dan Product sengaja dibuat null untuk menguji bagaimana adapter menanganinya
        transaction.setUser(null);
        transaction.setProduct(null);
        transaction.setQuantity(1);
        transaction.setTotalPrice(new BigDecimal("25000.00"));
        transaction.setPaymentStatus(PaymentStatus.pending);

        // 2. Act
        SalesReportItemDTO dto = transactionAdapter.toSalesReportItemDTO(transaction);

        // 3. Assert
        assertNotNull(dto);
        assertEquals(String.valueOf(transaction.getTransactionId()), dto.getOrderNo());
        assertEquals("N/A", dto.getCustomerName(), "CustomerName seharusnya 'N/A' jika User null");
        assertEquals("N/A", dto.getProductDescription(), "ProductDescription seharusnya 'N/A' jika Product null");
        assertEquals(PaymentStatus.pending.name(), dto.getPaymentStatus());
    }
}