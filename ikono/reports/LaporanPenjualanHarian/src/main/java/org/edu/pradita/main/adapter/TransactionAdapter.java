package org.edu.pradita.main.adapter;

import org.edu.pradita.main.model.Transaction;
import org.edu.pradita.main.model.dto.SalesReportItemDTO;

public class TransactionAdapter {
    public SalesReportItemDTO toSalesReportItemDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        String userName = (transaction.getUser() != null) ? transaction.getUser().getUsername() : "N/A";
        String productName = (transaction.getProduct() != null) ? transaction.getProduct().getProductName() : "N/A";


        return new SalesReportItemDTO(
                String.valueOf(transaction.getTransactionId()),
                userName,
                transaction.getQuantity(),
                transaction.getTotalPrice(),
                transaction.getPaymentStatus() != null ? transaction.getPaymentStatus().name() : "N/A",
                productName
        );
    }
}