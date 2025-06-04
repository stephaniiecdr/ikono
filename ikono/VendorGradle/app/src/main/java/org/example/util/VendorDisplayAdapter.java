package org.example;

import javafx.beans.property.SimpleStringProperty;
import java.time.format.DateTimeFormatter; 

public class VendorDisplayAdapter { 
    private final SimpleStringProperty id; 
    private final SimpleStringProperty name; 
    private final SimpleStringProperty phone; 
    private final SimpleStringProperty address; 
    private final SimpleStringProperty email; 
    private final SimpleStringProperty contactPerson; 
    private final SimpleStringProperty createdAt; 
    private final SimpleStringProperty updatedAt; 

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 

    public VendorDisplayAdapter(Vendor vendor) { 
        this.id = new SimpleStringProperty(vendor.getId() != null ? vendor.getId().toString() : ""); 
        this.name = new SimpleStringProperty(vendor.getName()); 
        this.phone = new SimpleStringProperty(vendor.getPhone()); 
        this.address = new SimpleStringProperty(vendor.getAddress()); 
        this.email = new SimpleStringProperty(vendor.getEmail()); 
        this.contactPerson = new SimpleStringProperty(vendor.getContactPerson()); 
        this.createdAt = new SimpleStringProperty(vendor.getCreatedAt() != null ? FORMATTER.format(vendor.getCreatedAt()) : ""); 
        this.updatedAt = new SimpleStringProperty(vendor.getUpdatedAt() != null ? FORMATTER.format(vendor.getUpdatedAt()) : ""); 
    }

    public String getId() { return id.get(); } 
    public String getName() { return name.get(); } 
    public String getPhone() { return phone.get(); } 
    public String getAddress() { return address.get(); } 
    public String getEmail() { return email.get(); } 
    public String getContactPerson() { return contactPerson.get(); } 
    public String getCreatedAt() { return createdAt.get(); } 
    public String getUpdatedAt() { return updatedAt.get(); } 
}
