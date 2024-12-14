package category;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Method to establish connection to the database
    public static Connection connect(String url, String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // Main method to accept command-line arguments
    public static void main(String[] args) {    

        // Try to connect to the database
        try (Connection conn = connect("jdbc:mysql://localhost:3306/category", "root", "12345")) {
            System.out.println("Database connection established successfully.");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
}
