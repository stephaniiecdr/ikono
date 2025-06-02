package edu.pradita.p14.Tax.tax;

import jakarta.persistence.*; // Gunakan jakarta.persistence untuk Hibernate 6+

/**
 * Kelas entitas Tax yang merepresentasikan informasi pajak.
 * Dipetakan ke tabel 'tax' di database menggunakan JPA/Hibernate.
 */
@Entity // Menandakan bahwa kelas ini adalah entitas JPA
@Table(name = "tax") // Menentukan nama tabel di database
public class Tax {
    @Id // Menandai properti ini sebagai kunci primer
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Mengkonfigurasi pembuatan ID otomatis oleh database
    @Column(name = "taxID") // Menentukan nama kolom di database
    private int taxID;

    @Column(name = "taxName")
    private String taxName;

    @Column(name = "taxRate")
    private double taxRate;

    // Konstruktor default diperlukan oleh JPA/Hibernate
    public Tax() {
    }

    public Tax(String taxName, double taxRate) {
        // taxID tidak disertakan karena akan di-generate otomatis oleh database
        this.taxName = taxName;
        this.taxRate = taxRate;
    }

    // Konstruktor untuk memuat dari database jika ID sudah ada
    public Tax(int taxID, String taxName, double taxRate) {
        this.taxID = taxID;
        this.taxName = taxName;
        this.taxRate = taxRate;
    }

    public int getTaxID() {
        return taxID;
    }

    // Setter untuk taxID diperlukan oleh Hibernate untuk memuat data
    public void setTaxID(int taxID) {
        this.taxID = taxID;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    @Override
    public String toString() {
        return "Tax{" +
                "taxID=" + taxID +
                ", taxName='" + taxName + '\'' +
                ", taxRate=" + taxRate +
                '}';
    }
}
