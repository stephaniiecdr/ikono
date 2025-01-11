package racikanresep;

import java.sql.Connection;
import java.sql.DriverManager;

public class teskoneksi {
	public static void main(String[] args) {
		// Detail koneksi
		String jdbcURL = "jdbc:mysql://localhost:3306/UAS PEMDAS"; // Ganti dengan nama database Anda
		String username = "root"; // Ganti dengan username MySQL Anda
		String password = "3631"; // Ganti dengan password MySQL Anda

		try {
			// Koneksi ke database
			Connection connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("Koneksi ke database berhasil!");
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Koneksi ke database gagal.");
		}
	}
}
