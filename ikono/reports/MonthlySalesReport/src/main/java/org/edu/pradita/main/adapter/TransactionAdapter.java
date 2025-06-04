package org.edu.pradita.main.adapter;

import org.edu.pradita.main.model.Transaction;
import org.edu.pradita.main.model.dto.SalesReportItemDTO;

public class TransactionAdapter {
    public SalesReportItemDTO toSalesReportItemDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        // Asumsi User dan Product tidak null karena JOIN FETCH dan nullable=false di Transaction
        String userName = (transaction.getUser() != null) ? transaction.getUser().getUsername() : "N/A";
        String productName = (transaction.getProduct() != null) ? transaction.getProduct().getProductName() : "N/A";
        // String productUnit = (transaction.getProduct() != null && transaction.getProduct().getUnit() != null) ? transaction.getProduct().getUnit() : "N/A"; // Dihilangkan

        return new SalesReportItemDTO(
                String.valueOf(transaction.getTransactionId()), // Konversi Integer ke String
                userName,
                transaction.getQuantity(),
                transaction.getTotalPrice(), // BigDecimal
                transaction.getPaymentStatus() != null ? transaction.getPaymentStatus().name() : "N/A", // Konversi Enum ke String
                productName
        );
    }
}