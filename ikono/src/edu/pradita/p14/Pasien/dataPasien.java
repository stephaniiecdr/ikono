package com.pasien.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class dataPasien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_pasien;

    private String nama_lengkap;

    @Temporal(TemporalType.DATE)
    private Date tanggal_lahir;

    @Enumerated(EnumType.STRING)
    private JenisKelamin jenis_kelamin;

    @Column(columnDefinition = "TEXT")
    private String alamat;

    private String nomor_telepon;
    private String email;

    @Enumerated(EnumType.STRING)
    private GolonganDarah golongan_darah;

    @Enumerated(EnumType.STRING)
    private StatusPernikahan status_pernikahan;

    @Column(columnDefinition = "TEXT")
    private String riwayat_penyakit;

    @Column(columnDefinition = "TEXT")
    private String alergi;

    @Temporal(TemporalType.DATE)
    private Date tanggal_registrasi;

    // Getter dan Setter di sini

    public enum JenisKelamin { Laki_Laki, Perempuan }
    public enum GolonganDarah { A_POS, A_NEG, B_POS, B_NEG, AB_POS, AB_NEG, O_POS, O_NEG }
    public enum StatusPernikahan { Belum_nikah, Sudah_Menikah, Cerai }
}
