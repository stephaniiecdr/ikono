package application;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "currencies")
public class Currency {

    @Id
    @Column(name = "CurID", length = 3)
    private String curID;

    @Column(name = "CurName", nullable = false, length = 50)
    private String curName;

    @Column(name = "CurSymbol", length = 5)
    private String curSymbol;

    @Column(name = "CurCountry", length = 50)
    private String curCountry;

    @Column(name = "CurExcRate", precision = 15, scale = 4)
    private BigDecimal curExcRate;

    @Column(name = "CurStatus", length = 10)
    private String curStatus; // "active" or "inactive"

    @Column(name = "decimal_places")
    private Integer decimalPlaces;

    // Constructors
    public Currency() {
    }

    public Currency(String curID, String curName, String curSymbol, String curCountry, BigDecimal curExcRate, String curStatus, Integer decimalPlaces) {
        this.curID = curID;
        this.curName = curName;
        this.curSymbol = curSymbol;
        this.curCountry = curCountry;
        this.curExcRate = curExcRate;
        this.curStatus = curStatus;
        this.decimalPlaces = decimalPlaces;
    }

    // Getters and Setters
    public String getCurID() {
        return curID;
    }

    public void setCurID(String curID) {
        this.curID = curID;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getCurSymbol() {
        return curSymbol;
    }

    public void setCurSymbol(String curSymbol) {
        this.curSymbol = curSymbol;
    }

    public String getCurCountry() {
        return curCountry;
    }

    public void setCurCountry(String curCountry) {
        this.curCountry = curCountry;
    }

    public BigDecimal getCurExcRate() {
        return curExcRate;
    }

    public void setCurExcRate(BigDecimal curExcRate) {
        this.curExcRate = curExcRate;
    }

    public String getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(String curStatus) {
        this.curStatus = curStatus;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    // Optional: Override toString() for easier debugging or ListView display
    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Symbol: %s, Country: %s, Rate: %s, Status: %s, Decimals: %d",
                curID, curName, curSymbol, curCountry, curExcRate, curStatus, decimalPlaces);
    }
}
