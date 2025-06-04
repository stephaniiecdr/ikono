package _UASS2;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pelunasan")
public class Pelunasan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pelunasan")
    private Integer idPelunasan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_piutang", referencedColumnName = "id_piutang")
    private Piutang piutang;
    
    @Column(name = "jumlah_bayar", nullable = false, precision = 15, scale = 2)
    private BigDecimal jumlahBayar;
    
    @Column(name = "tanggal_bayar", nullable = false)
    private LocalDate tanggalBayar;
    
    // Constructors
    public Pelunasan() {}
    
    public Pelunasan(Piutang piutang, BigDecimal jumlahBayar, LocalDate tanggalBayar) {
        this.piutang = piutang;
        this.jumlahBayar = jumlahBayar;
        this.tanggalBayar = tanggalBayar;
    }
    
    // Getters and Setters
    public Integer getIdPelunasan() {
        return idPelunasan;
    }
    
    public void setIdPelunasan(Integer idPelunasan) {
        this.idPelunasan = idPelunasan;
    }
    
    public Piutang getPiutang() {
        return piutang;
    }
    
    public void setPiutang(Piutang piutang) {
        this.piutang = piutang;
    }
    
    public BigDecimal getJumlahBayar() {
        return jumlahBayar;
    }
    
    public void setJumlahBayar(BigDecimal jumlahBayar) {
        this.jumlahBayar = jumlahBayar;
    }
    
    public LocalDate getTanggalBayar() {
        return tanggalBayar;
    }
    
    public void setTanggalBayar(LocalDate tanggalBayar) {
        this.tanggalBayar = tanggalBayar;
    }
    
    // Helper methods for JavaFX TableView compatibility
    public Integer getIdPiutang() {
        return piutang != null ? piutang.getIdPiutang() : null;
    }
    
    public String getNamaLengkap() {
        return piutang != null && piutang.getPembeli() != null ? 
               piutang.getPembeli().getNamaLengkap() : 
               (piutang != null ? piutang.getNamaPelanggan() : "");
    }
    
    public String getTanggalBayarString() {
        return tanggalBayar != null ? tanggalBayar.toString() : "";
    }
    
    @Override
    public String toString() {
        return "Pelunasan{" +
                "idPelunasan=" + idPelunasan +
                ", jumlahBayar=" + jumlahBayar +
                ", tanggalBayar=" + tanggalBayar +
                '}';
    }
}