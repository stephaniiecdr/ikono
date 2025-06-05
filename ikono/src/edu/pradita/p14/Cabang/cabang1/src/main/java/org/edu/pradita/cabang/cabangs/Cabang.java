package org.edu.pradita.cabang.cabangs;

import jakarta.persistence.*;

@Entity
@Table(name = "cabang")

    public class Cabang {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_cabang")
        private Long idCabang;

        @Column(name = "nama_cabang", nullable = false, length = 100)
        private String namaCabang;

        @Column(name = "alamat_cabang", length = 255)
        private String alamatCabang;

        @Column(name = "telepon_cabang", length = 20)
        private String teleponCabang;


        // Constructor
        public Cabang() {
        }

        public Cabang(String namaCabang, String alamatCabang, String teleponCabang) {
            this.namaCabang = namaCabang;
            this.alamatCabang = alamatCabang;
            this.teleponCabang = teleponCabang;
        } //buat getter seter


        public void setIdCabang(Long idCabang) {
            this.idCabang = idCabang;
        }

        public Long getIdCabang() {
            return idCabang;
        }

        public String getNamaCabang() {
            return namaCabang;
        }

        public void setNamaCabang(String namaCabang) {
            this.namaCabang = namaCabang;
        }

        public String getAlamatCabang() {
            return alamatCabang;
        }

        public void setAlamatCabang(String alamatCabang) {
            this.alamatCabang = alamatCabang;
        }

        public String getTeleponCabang() {
            return teleponCabang;
        }

        public void setTeleponCabang(String teleponCabang) {
            this.teleponCabang = teleponCabang;
        }

        @Override
        public String toString() {
            return namaCabang; // Penting untuk ditampilkan di ComboBox atau ListView JavaFX
        }
    }

