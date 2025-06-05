package org.edu.pradita.cabang.cabangs; // Sesuaikan dengan package Anda

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.edu.pradita.cabang.cabangs.HibernateUtil; // Pastikan ini diimpor

import java.io.IOException;
import java.util.Objects;

public class MainApp extends Application {

    @Override
    public void init() throws Exception {
        super.init();
        // OPSIONAL: Anda bisa "menyentuh" SessionFactory di sini untuk
        // memastikan inisialisasi terjadi di awal dan menangkap error konfigurasi lebih cepat.
        // Namun, ini akan membuat startup aplikasi sedikit lebih lama jika ada overhead besar.
        // Biasanya, SessionFactory akan diinisialisasi saat pertama kali DAO membutuhkannya.
        try {
            System.out.println("Mencoba inisialisasi Hibernate SessionFactory di awal...");
            HibernateUtil.getSessionFactory(); // Ini akan memicu pembuatan SessionFactory jika belum ada
            System.out.println("Inisialisasi Hibernate SessionFactory berhasil (atau sudah ada).");
        } catch (Throwable e) {
            System.err.println("Gagal melakukan inisialisasi Hibernate SessionFactory di awal di MainApp.init():");
            e.printStackTrace();
            // Anda mungkin ingin menangani ini lebih lanjut, misal keluar dari aplikasi
            // atau menampilkan pesan error ke pengguna sebelum GUI muncul.
            // Untuk sekarang, kita biarkan aplikasi mencoba lanjut,
            // error sebenarnya akan muncul saat DAO mencoba akses.
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Path ke FXML Anda (pastikan sudah benar dan file ada di src/main/resources)
            String fxmlPath = "/org/edu/pradita/cabang/cabangs/view/CabangView.fxml"; // Sesuaikan path ini
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            if (loader.getLocation() == null) {
                System.err.println("Tidak dapat menemukan file FXML di path: " + fxmlPath);
                // Tampilkan dialog error atau tindakan lain
                // Contoh sederhana:
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Error Aplikasi");
                alert.setHeaderText("File FXML Tidak Ditemukan");
                alert.setContentText("Aplikasi tidak dapat dimulai karena file FXML utama tidak ditemukan di: " + fxmlPath);
                alert.showAndWait();
                return; // Keluar dari metode start jika FXML tidak ada
            }

            Parent root = loader.load(); // Error yang Anda alami sebelumnya mungkin terjadi di sini jika ada masalah inisialisasi controller/service/DAO
            Scene scene = new Scene(root);

            // Jika Anda punya file CSS (opsional)
            // String cssPath = "/org/edu/pradita/cabang/cabangs/css/styles.css";
            // if (getClass().getResource(cssPath) != null) {
            //     scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(cssPath)).toExternalForm());
            // } else {
            //     System.err.println("File CSS tidak ditemukan di: " + cssPath);
            // }

            primaryStage.setTitle("Manajemen Cabang - Point Of Sales by James Edison"); // Sesuaikan judul
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Gagal memuat CabangView.fxml:");
            e.printStackTrace();
            // Tampilkan dialog error ke pengguna
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error Aplikasi");
            alert.setHeaderText("Gagal Memuat Tampilan Utama");
            alert.setContentText("Terjadi kesalahan saat memuat antarmuka pengguna utama.\nDetail: " + e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            // Menangkap error lain yang mungkin terjadi selama start, termasuk yang dari inisialisasi Hibernate
            System.err.println("Terjadi error tak terduga saat memulai aplikasi:");
            e.printStackTrace();
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error Aplikasi Kritis");
            alert.setHeaderText("Aplikasi Gagal Dimulai");
            alert.setContentText("Terjadi kesalahan kritis yang mencegah aplikasi dimulai.\nDetail: " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Metode ini dipanggil ketika aplikasi JavaFX ditutup.
     * Ini adalah tempat yang tepat untuk melepaskan sumber daya,
     * seperti menutup SessionFactory Hibernate.
     */
    @Override
    public void stop() throws Exception {
        System.out.println("Aplikasi mulai proses penutupan...");
        HibernateUtil.shutdown(); // Panggil shutdown dari HibernateUtil Anda
        System.out.println("Aplikasi telah ditutup.");
        super.stop(); // Panggil implementasi stop dari superclass
    }

    public static void main(String[] args) {
        launch(args);
    }
}