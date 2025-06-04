package org.edu.pradita.main.model.dto;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.math.BigDecimal; // Import BigDecimal

public class SalesReportItemDTO {
    private final SimpleStringProperty orderNo; // transaction_id akan jadi String di sini
    private final SimpleStringProperty customerName;
    private final SimpleIntegerProperty quantity;
    private final SimpleDoubleProperty totalPrice; // total_price dari transaksi (BigDecimal di DB)
    private final SimpleStringProperty paymentStatus;
    private final SimpleStringProperty productDescription;
    // private final SimpleStringProperty productUnit; // Dihilangkan

    public SalesReportItemDTO(String orderNo, String customerName, int quantity, BigDecimal totalPrice, String paymentStatus, String productDescription) {
        this.orderNo = new SimpleStringProperty(orderNo);
        this.customerName = new SimpleStringProperty(customerName);
        this.quantity = new SimpleIntegerProperty(quantity);
        // Konversi BigDecimal ke double untuk SimpleDoubleProperty
        this.totalPrice = new SimpleDoubleProperty(totalPrice != null ? totalPrice.doubleValue() : 0.0);
        this.paymentStatus = new SimpleStringProperty(paymentStatus);
        this.productDescription = new SimpleStringProperty(productDescription);
        // this.productUnit = new SimpleStringProperty(productUnit); // Dihilangkan
    }

    // Getters untuk PropertyValueFactory
    public String getOrderNo() { return orderNo.get(); }
    public SimpleStringProperty orderNoProperty() { return orderNo; }
    public String getCustomerName() { return customerName.get(); }
    public SimpleStringProperty customerNameProperty() { return customerName; }
    public int getQuantity() { return quantity.get(); }
    public SimpleIntegerProperty quantityProperty() { return quantity; }
    public double getTotalPrice() { return totalPrice.get(); }
    public SimpleDoubleProperty totalPriceProperty() { return totalPrice; }
    public String getPaymentStatus() { return paymentStatus.get(); }
    public SimpleStringProperty paymentStatusProperty() { return paymentStatus; }
    public String getProductDescription() { return productDescription.get(); }
    public SimpleStringProperty productDescriptionProperty() { return productDescription; }
    // public String getProductUnit() { return productUnit.get(); } // Dihilangkan
    // public SimpleStringProperty productUnitProperty() { return productUnit; } // Dihilangkan
}