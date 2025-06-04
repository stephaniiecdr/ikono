package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class VendorTest {

    private Vendor vendor;
    private final String TEST_NAME = "Test Vendor";
    private final String TEST_CONTACT_PERSON = "John Doe";
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PHONE = "123-456-7890";
    private final String TEST_ADDRESS = "123 Test St";

    @BeforeEach
    void setUp() {
        vendor = new Vendor(TEST_NAME, TEST_CONTACT_PERSON, TEST_EMAIL, TEST_PHONE, TEST_ADDRESS);
    }

    @Test
    void testNoArgsConstructor() {
        Vendor newVendor = new Vendor();
        assertNotNull(newVendor);
    }

    @Test
    void testAllArgsConstructor() {
        assertEquals(TEST_NAME, vendor.getName());
        assertEquals(TEST_CONTACT_PERSON, vendor.getContactPerson());
        assertEquals(TEST_EMAIL, vendor.getEmail());
        assertEquals(TEST_PHONE, vendor.getPhone());
        assertEquals(TEST_ADDRESS, vendor.getAddress());
        assertNull(vendor.getId()); 
        assertNull(vendor.getCreatedAt()); 
        assertNull(vendor.getUpdatedAt()); 
    }

    @Test
    void testSettersAndGetters() {
        Integer newId = 1;
        String newName = "New Vendor Name";
        String newContactPerson = "Jane Smith";
        String newEmail = "new@example.com";
        String newPhone = "098-765-4321";
        String newAddress = "456 New Ave";
        LocalDateTime newCreatedAt = LocalDateTime.now().minusDays(1);
        LocalDateTime newUpdatedAt = LocalDateTime.now();

        vendor.setId(newId);
        vendor.setName(newName);
        vendor.setContactPerson(newContactPerson);
        vendor.setEmail(newEmail);
        vendor.setPhone(newPhone);
        vendor.setAddress(newAddress);
        vendor.setCreatedAt(newCreatedAt);
        vendor.setUpdatedAt(newUpdatedAt);

        assertEquals(newId, vendor.getId());
        assertEquals(newName, vendor.getName());
        assertEquals(newContactPerson, vendor.getContactPerson());
        assertEquals(newEmail, vendor.getEmail());
        assertEquals(newPhone, vendor.getPhone());
        assertEquals(newAddress, vendor.getAddress());
        assertEquals(newCreatedAt, vendor.getCreatedAt());
        assertEquals(newUpdatedAt, vendor.getUpdatedAt());
    }

    @Test
    void testToString() {
        
        vendor.setId(1);
        LocalDateTime now = LocalDateTime.now();
        vendor.setCreatedAt(now.minusHours(1));
        vendor.setUpdatedAt(now);

        String expectedToString = "Vendor{" +
                                  "id=" + vendor.getId() +
                                  ", name='" + TEST_NAME + '\'' +
                                  ", contactPerson='" + TEST_CONTACT_PERSON + '\'' +
                                  ", email='" + TEST_EMAIL + '\'' +
                                  ", phone='" + TEST_PHONE + '\'' +
                                  ", address='" + TEST_ADDRESS + '\'' +
                                  ", createdAt=" + vendor.getCreatedAt() +
                                  ", updatedAt=" + vendor.getUpdatedAt() +
                                  '}';
        assertEquals(expectedToString, vendor.toString());
    }
}
