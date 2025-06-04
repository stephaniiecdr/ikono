package _UASS2;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pembeli")
public class Pembeli {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pembeli")
    private Integer idPembeli;
    
    @Column(name = "nama_lengkap", nullable = false, length = 20)
    private String namaLengkap;
    
    @Column(name = "alamat", nullable = false, length = 100)
    private String alamat;
    
    @Column(name = "kota", nullable = false, length = 20)
    private String kota;
    
    @Column(name = "kode_pos", nullable = false, length = 20)
    private String kodePos;
    
    @Column(name = "no_telepon", nullable = false, length = 20)
    private String noTelepon;
    
    @Column(name = "email", nullable = false, length = 20)
    private String email;
    
    @Column(name = "jenis_kelamin", nullable = false, length = 10)
    private String jenisKelamin;
    
    @Column(name = "tanggal_daftar", nullable = false)
    private LocalDate tanggalDaftar;
    
    @Column(name = "status", nullable = false, length = 10)
    private String status;
    
    @OneToMany(mappedBy = "pembeli", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Piutang> piutangList;
    
    // Constructors
    public Pembeli() {}
    
    public Pembeli(String namaLengkap, String alamat, String kota, String kodePos, 
                   String noTelepon, String email, String jenisKelamin, 
                   LocalDate tanggalDaftar, String status) {
        this.namaLengkap = namaLengkap;
        this.alamat = alamat;
        this.kota = kota;
        this.kodePos = kodePos;
        this.noTelepon = noTelepon;
        this.email = email;
        this.jenisKelamin = jenisKelamin;
        this.tanggalDaftar = tanggalDaftar;
        this.status = status;
    }
    
    // Getters and Setters
    public Integer getIdPembeli() {
        return idPembeli;
    }
    
    public void setIdPembeli(Integer idPembeli) {
        this.idPembeli = idPembeli;
    }
    
    public String getNamaLengkap() {
        return namaLengkap;
    }
    
    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }
    
    public String getAlamat() {
        return alamat;
    }
    
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
    
    public String getKota() {
        return kota;
    }
    
    public void setKota(String kota) {
        this.kota = kota;
    }
    
    public String getKodePos() {
        return kodePos;
    }
    
    public void setKodePos(String kodePos) {
        this.kodePos = kodePos;
    }
    
    public String getNoTelepon() {
        return noTelepon;
    }
    
    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getJenisKelamin() {
        return jenisKelamin;
    }
    
    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }
    
    public LocalDate getTanggalDaftar() {
        return tanggalDaftar;
    }
    
    public void setTanggalDaftar(LocalDate tanggalDaftar) {
        this.tanggalDaftar = tanggalDaftar;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<Piutang> getPiutangList() {
        return piutangList;
    }
    
    public void setPiutangList(List<Piutang> piutangList) {
        this.piutangList = piutangList;
    }
    
    @Override
    public String toString() {
        return "Pembeli{" +
                "idPembeli=" + idPembeli +
                ", namaLengkap='" + namaLengkap + '\'' +
                ", alamat='" + alamat + '\'' +
                ", kota='" + kota + '\'' +
                '}';
    }
}