package racikanresep;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User {
	private String penyakit;
	private List<Obat> daftarObat;

	public User(String penyakit) {
		this.penyakit = penyakit;
		this.daftarObat = new ArrayList<>();
	}

	public void tambahObat(Obat obat) {
		this.daftarObat.add(obat);
		simpanObatKeDatabase(obat);
	}

	public void cetakInformasi() {
		System.out.println("\nInformasi Pengobatan untuk " + penyakit);
		System.out.println("\nDetail Obat:");
		for (Obat obat : daftarObat) {
			System.out.println("\nNama Obat: " + obat.getNama());
			System.out.println("Deskripsi: " + obat.getDeskripsi());
			System.out.println("Aturan Minum: " + obat.getFrekuensi());
		}
		System.out.println("\nCatatan: Harap ikuti aturan minum obat dengan tepat");
		System.out.println("Jika gejala tidak membaik, segera hubungi dokter");
	}

	private void simpanObatKeDatabase(Obat obat) {
		String url = "jdbc:sqlite:obat.db";
		String sql = "INSERT INTO obat(nama, deskripsi, frekuensi) VALUES(?, ?, ?)";

		try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, obat.getNama());
			pstmt.setString(2, obat.getDeskripsi());
			pstmt.setString(3, obat.getFrekuensi());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static class Obat {
        private String nama;
        private String deskripsi;
        private String frekuensi;

        public Obat(String nama, String deskripsi, String frekuensi) {
            this.nama = nama;
            this.deskripsi = deskripsi;
            this.frekuensi = frekuensi;
        }

        public String getNama() { return nama; }
        public String getDeskripsi() { return deskripsi; }
        public String getFrekuensi() { return frekuensi; }
    }

public static void main(String[] args) {
        // Buat tabel jika belum ada
        buatTabelObat();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Silakan pilih keluhan yang Anda alami:");
        System.out.println("1. Demam");
        System.out.println("2. Alergi");
        System.out.println("3. Infeksi");
        
        System.out.print("\nMasukkan pilihan (1-3): ");
        int pilihanPenyakit = scanner.nextInt();
        
        String penyakit;
        String namaObat;
        String deskripsiObat;
        String frekuensiDefault;

        switch (pilihanPenyakit) {
            case 1:
                penyakit = "Demam";
                namaObat = "Paracetamol";
                deskripsiObat = "Obat untuk menurunkan demam dan meredakan nyeri. " +
                               "Diminum setelah makan. Jangan melebihi dosis yang dianjurkan.";
                frekuensiDefault = "3 kali sehari, setiap 8 jam";
                break;
            case 2:
                penyakit = "Alergi";
                namaObat = "Cetirizine";
                deskripsiObat = "Antihistamin untuk mengatasi gejala alergi seperti bersin, gatal, dan hidung berair. " +
                               "