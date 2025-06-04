package _UASS2;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "piutang")
public class Piutang {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_piutang")
    private Integer idPiutang;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pelanggan", referencedColumnName = "id_pembeli")
    private Pembeli pembeli;
    
    @Column(name = "nama_pelanggan", length = 100)
    private String namaPelanggan;
    
    @Column(name = "jumlah", nullable = false, precision = 15, scale = 2)
    private BigDecimal jumlah;
    
    @Column(name = "tanggal_pinjam", nullable = false)
    private LocalDate tanggalPinjam;
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @Column(name = "status_lunas", columnDefinition = "TINYINT DEFAULT 0")
    private Boolean statusLunas = false;
    
    @OneToMany(mappedBy = "piutang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pelunasan> pelunasanList;
    
    // Constructors
    public Piutang() {}
    
    public Piutang(Pembeli pembeli, String namaPelanggan, BigDecimal jumlah, 
                   LocalDate tanggalPinjam, LocalDate dueDate) {
        this.pembeli = pembeli;
        this.namaPelanggan = namaPelanggan;
        this.jumlah = jumlah;
        this.tanggalPinjam = tanggalPinjam;
        this.dueDate = dueDate;
        this.statusLunas = false;
    }
    
    // Getters and Setters
    public Integer getIdPiutang() {
        return idPiutang;
    }
    
    public void setIdPiutang(Integer idPiutang) {
        this.idPiutang = idPiutang;
    }
    
    public Pembeli getPembeli() {
        return pembeli;
    }
    
    public void setPembeli(Pembeli pembeli) {
        this.pembeli = pembeli;
    }
    
    public String getNamaPelanggan() {
        return namaPelanggan;
    }
    
    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }
    
    public BigDecimal getJumlah() {
        return jumlah;
    }
    
    public void setJumlah(BigDecimal jumlah) {
        this.jumlah = jumlah;
    }
    
    public LocalDate getTanggalPinjam() {
        return tanggalPinjam;
    }
    
    public void setTanggalPinjam(LocalDate tanggalPinjam) {
        this.tanggalPinjam = tanggalPinjam;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public Boolean getStatusLunas() {
        return statusLunas;
    }
    
    public void setStatusLunas(Boolean statusLunas) {
        this.statusLunas = statusLunas;
    }
    
    public List<Pelunasan> getPelunasanList() {
        return pelunasanList;
    }
    
    public void setPelunasanList(List<Pelunasan> pelunasanList) {
        this.pelunasanList = pelunasanList;
    }
    
    // Helper methods for JavaFX TableView compatibility
    public String getStatusLunasString() {
        return (statusLunas != null && statusLunas) ? "Lunas" : "Belum Lunas";
    }
    
    public Integer getIdPelanggan() {
        return pembeli != null ? pembeli.getIdPembeli() : null;
    }
    
    public String getPembeliNama() {
        return pembeli != null ? pembeli.getNamaLengkap() : namaPelanggan;
    }
    
    @Override
    public String toString() {
        return "Piutang{" +
                "idPiutang=" + idPiutang +
                ", namaPelanggan='" + namaPelanggan + '\'' +
                ", jumlah=" + jumlah +
                ", statusLunas=" + statusLunas +
                '}';
    }
}