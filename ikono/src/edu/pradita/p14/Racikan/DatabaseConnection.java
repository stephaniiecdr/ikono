package racikanresep;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private static final String URL = "jdbc:mysql://localhost:3306/nama_database"; // Ganti dengan nama database Anda
	private static final String USER = "username"; // Ganti dengan username database Anda
	private static final String PASSWORD = "password"; // Ganti dengan password database Anda

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}