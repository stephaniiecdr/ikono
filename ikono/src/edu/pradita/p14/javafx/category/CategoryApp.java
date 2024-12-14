package category;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class CategoryApp extends Application {

    private Connection conn;
    private ObservableList<Category> categories;

    @Override
    public void start(Stage primaryStage) {
        // Koneksi ke database
        conn = connectToDatabase();

        if (conn == null) {
            System.out.println("Gagal koneksi ke database. Aplikasi akan ditutup.");
            Platform.exit();
            return;
        }

        // Buat tabel untuk menampilkan data
        TableView<Category> tableView = new TableView<>();
        tableView.setItems(getCategories());

        // Buat kolom untuk tabel
        TableColumn<Category, Integer> categoryIdColumn = new TableColumn<>("ID");
        categoryIdColumn.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        TableColumn<Category, String> categoryNameColumn = new TableColumn<>("Nama Kategori");
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        TableColumn<Category, Integer> parentIdColumn = new TableColumn<>("ID Parent");
        parentIdColumn.setCellValueFactory(new PropertyValueFactory<>("parentId"));

        // Tambahkan kolom ke tabel
        tableView.getColumns().addAll(categoryIdColumn, categoryNameColumn, parentIdColumn);

        // Form untuk menambah kategori
        Label categoryNameLabel = new Label("Nama Kategori:");
        TextField categoryNameField = new TextField();
        Label parentIdLabel = new Label("ID Parent:");
        TextField parentIdField = new TextField();

        // Form untuk mencari kategori
        Label searchLabel = new Label("Cari Kategori:");
        TextField searchField = new TextField();
        Button searchButton = new Button("Cari");

        // Fungsi untuk mencari kategori
        searchButton.setOnAction(event -> {
            String searchTerm = searchField.getText();
            tableView.setItems(searchCategories(searchTerm));
        });

        // Tombol untuk menambah kategori
        Button addButton = new Button("Tambah");
        addButton.setOnAction(event -> {
            if (categoryNameField.getText().isEmpty()) {
                System.out.println("Nama kategori tidak boleh kosong!");
                return;
            }
            addCategory(categoryNameField.getText(), parentIdField.getText());
            tableView.setItems(getCategories());
        });

        // Tombol untuk menghapus kategori
        Label categoryIdLabel = new Label("ID Kategori untuk Dihapus:");
        TextField categoryIdField = new TextField();
        Button deleteButton = new Button("Hapus");
        deleteButton.setOnAction(event -> {
            try {
                int categoryId = Integer.parseInt(categoryIdField.getText());
                deleteCategory(String.valueOf(categoryId));
                tableView.setItems(getCategories());
            } catch (NumberFormatException e) {
                System.out.println("ID Kategori harus berupa angka!");
            }
        });

        // Layout aplikasi
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);
        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(300); // Menyesuaikan tinggi view
        scrollPane.setPrefViewportWidth(550); // Menyesuaikan lebar view

        layout.getChildren().addAll(
            searchLabel, searchField, searchButton,
            scrollPane,
            categoryNameLabel, categoryNameField,
            parentIdLabel, parentIdField,
            addButton,
            categoryIdLabel, categoryIdField, deleteButton
        );

        // Scene dan stage
        Scene scene = new Scene(layout, 650, 500); // Memperbesar ukuran window
        primaryStage.setTitle("Aplikasi Kategori");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Gagal menutup koneksi: " + e.getMessage());
        }
        Platform.exit();
    }

    // Fungsi untuk koneksi ke database
    private Connection connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/category", "root", "12345");
        } catch (Exception e) {
            System.out.println("Gagal koneksi ke database: " + e.getMessage());
            return null;
        }
    }

    // Fungsi untuk mendapatkan data kategori
    private ObservableList<Category> getCategories() {
        categories = FXCollections.observableArrayList();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM categories");
            while (rs.next()) {
                Integer parentId = rs.getObject("parent_id", Integer.class);
                categories.add(new Category(rs.getInt("category_id"), rs.getString("category_name"), parentId));
            }
        } catch (Exception e) {
            System.out.println("Gagal mendapatkan data: " + e.getMessage());
        }
        return categories;
    }

    // Fungsi untuk menambahkan kategori
    private void addCategory(String categoryName, String parentId) {
        try {
            PreparedStatement pstmt;
            if (parentId.isEmpty()) {
                pstmt = conn.prepareStatement("INSERT INTO categories (category_name, parent_id) VALUES (?, NULL)");
            } else {
                pstmt = conn.prepareStatement("INSERT INTO categories (category_name, parent_id) VALUES (?, ?)");
                pstmt.setInt(2, Integer.parseInt(parentId));
            }
            pstmt.setString(1, categoryName);
            pstmt.executeUpdate();
            System.out.println("Kategori berhasil ditambahkan!");
        } catch (Exception e) {
            System.out.println("Gagal menambahkan data: " + e.getMessage());
        }
    }

    // Fungsi untuk menghapus kategori
    private void deleteCategory(String categoryId) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM categories WHERE category_id = ?");
            pstmt.setInt(1, Integer.parseInt(categoryId));
            pstmt.executeUpdate();
            System.out.println("Kategori berhasil dihapus!");
        } catch (Exception e) {
            System.out.println("Gagal menghapus data: " + e.getMessage());
        }
    }

    // Fungsi untuk mencari kategori
    private ObservableList<Category> searchCategories(String searchTerm) {
        ObservableList<Category> searchResults = FXCollections.observableArrayList();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM categories WHERE category_name LIKE ?");
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Integer parentId = rs.getObject("parent_id", Integer.class);
                searchResults.add(new Category(rs.getInt("category_id"), rs.getString("category_name"), parentId));
            }
        } catch (Exception e) {
            System.out.println("Gagal mencari data: " + e.getMessage());
        }
        return searchResults;
    }

    // Kelas kategori
    public class Category {
        private int categoryId;      // category_id
        private String categoryName; // category_name
        private Integer parentId;    // parent_id (Integer allows null)

        // Constructor
        public Category(int categoryId, String categoryName, Integer parentId) {
            this.categoryId = categoryId;
            this.categoryName = categoryName;
            this.parentId = parentId;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public Integer getParentId() {
            return parentId;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
