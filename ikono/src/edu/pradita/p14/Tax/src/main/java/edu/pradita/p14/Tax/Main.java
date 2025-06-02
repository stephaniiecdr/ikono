package edu.pradita.p14.Tax;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            // Muat FXML
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/edu/pradita/p14/Tax/TaxView.fxml"));
            VBox root = fxmlLoader.load(); // Pastikan tipe root layout sesuai dengan FXML Anda (VBox)

            Scene scene = new Scene(root, 600, 500); // Sesuaikan ukuran jendela

            stage.setTitle("Sistem POS - Manajemen Pajak");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("Gagal memuat TaxView.fxml: " + e.getMessage());
            e.printStackTrace();
            // Anda bisa menampilkan Alert di sini jika aplikasi tidak dapat dimulai
            // showAlert(AlertType.ERROR, "Startup Error", "Failed to load application UI.");
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        // Penting: Tutup EntityManagerFactory saat aplikasi dimatikan
        HibernateUtil.shutdown();
        System.out.println("Aplikasi dihentikan. Hibernate EntityManagerFactory dimatikan.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
