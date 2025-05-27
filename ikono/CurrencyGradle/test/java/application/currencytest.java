package application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*; // Mengimpor semua metode static assertions

import java.math.BigDecimal;

@DisplayName("Currency Entity Tests")
class CurrencyTest {

    private Currency currency;
    private final String expectedCurID = "USD";
    private final String expectedCurName = "US Dollar";
    private final String expectedCurSymbol = "$";
    private final String expectedCurCountry = "United States";
    private final BigDecimal expectedCurExcRate = new BigDecimal("1.0000");
    private final String expectedCurStatus = "active";
    private final Integer expectedDecimalPlaces = 2;

    @BeforeEach
    void setUp() {
        // Metode ini akan dijalankan sebelum setiap metode @Test
        // Ini bagus untuk menginisialisasi objek yang akan diuji
        currency = new Currency(
                expectedCurID,
                expectedCurName,
                expectedCurSymbol,
                expectedCurCountry,
                expectedCurExcRate,
                expectedCurStatus,
                expectedDecimalPlaces
        );
    }

    @Test
    @DisplayName("Should correctly create Currency object with constructor")
    void testCurrencyConstructorAndInitialState() {
        assertNotNull(currency, "Currency object should not be null after creation.");
        System.out.println("Testing constructor: Currency object created.");
    }

    @Test
    @DisplayName("Should return correct Currency ID")
    void getCurID() {
        assertEquals(expectedCurID, currency.getCurID(), "Getter for CurID should return the correct ID.");
        System.out.println("Testing getCurID: " + currency.getCurID());
    }

    @Test
    @DisplayName("Should return correct Currency Name")
    void getCurName() {
        assertEquals(expectedCurName, currency.getCurName(), "Getter for CurName should return the correct name.");
        System.out.println("Testing getCurName: " + currency.getCurName());
    }

    @Test
    @DisplayName("Should return correct Currency Symbol")
    void getCurSymbol() {
        assertEquals(expectedCurSymbol, currency.getCurSymbol(), "Getter for CurSymbol should return the correct symbol.");
        System.out.println("Testing getCurSymbol: " + currency.getCurSymbol());
    }

    @Test
    @DisplayName("Should return correct Currency Country")
    void getCurCountry() {
        assertEquals(expectedCurCountry, currency.getCurCountry(), "Getter for CurCountry should return the correct country.");
        System.out.println("Testing getCurCountry: " + currency.getCurCountry());
    }

    @Test
    @DisplayName("Should return correct Exchange Rate")
    void getCurExcRate() {
        assertEquals(0, expectedCurExcRate.compareTo(currency.getCurExcRate()), "Getter for CurExcRate should return the correct exchange rate.");
        System.out.println("Testing getCurExcRate: " + currency.getCurExcRate());
    }

    @Test
    @DisplayName("Should return correct Currency Status")
    void getCurStatus() {
        assertEquals(expectedCurStatus, currency.getCurStatus(), "Getter for CurStatus should return the correct status.");
        System.out.println("Testing getCurStatus: " + currency.getCurStatus());
    }

    @Test
    @DisplayName("Should return correct Decimal Places")
    void getDecimalPlaces() {
        assertEquals(expectedDecimalPlaces, currency.getDecimalPlaces(), "Getter for DecimalPlaces should return the correct number of decimal places.");
        System.out.println("Testing getDecimalPlaces: " + currency.getDecimalPlaces());
    }

    @Test
    @DisplayName("Should allow setting and getting Currency ID")
    void setAndGetCurID() {
        String newID = "EUR";
        currency.setCurID(newID);
        assertEquals(newID, currency.getCurID(), "Setter/Getter for CurID failed.");
        System.out.println("Testing setAndGetCurID: " + currency.getCurID());
    }

    @Test
    @DisplayName("Should allow setting and getting Currency Name")
    void setAndGetCurName() {
        String newName = "Euro";
        currency.setCurName(newName);
        assertEquals(newName, currency.getCurName(), "Setter/Getter for CurName failed.");
        System.out.println("Testing setAndGetCurName: " + currency.getCurName());
    }

    // Anda bisa menambahkan tes serupa untuk setter dan getter properti lainnya
    // misalnya, setAndGetCurSymbol, setAndGetCurExcRate, dll.

    @Test
    @DisplayName("Test toString method format (basic check)")
    void testToString() {
        String currencyString = currency.toString();
        assertNotNull(currencyString, "toString() should not return null.");
        assertTrue(currencyString.startsWith("ID: USD"), "toString() should start with 'ID: USD'");
        assertTrue(currencyString.contains("Name: US Dollar"), "toString() should contain 'Name: US Dollar'");
        System.out.println("Testing toString: " + currencyString);
    }
}
