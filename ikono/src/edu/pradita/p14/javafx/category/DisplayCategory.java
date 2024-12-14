package category;

import java.sql.*;

public class DisplayCategory {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/category";
        String username = "root";
        String password = "12345";

        String query = "SELECT * FROM categories";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("category_id | category_name | parent_id");
            System.out.println("-----------------------------------");
            while (resultSet.next()) {
                int id = resultSet.getInt("category_id");
                String name = resultSet.getString("category_name");
                Integer parentId = resultSet.getObject("parent_id", Integer.class);
                System.out.printf("%d | %s | %s\n", id, name, parentId != null ? parentId : "NULL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}