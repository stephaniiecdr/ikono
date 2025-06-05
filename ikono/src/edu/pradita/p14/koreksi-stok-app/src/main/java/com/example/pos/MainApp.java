package com.example.pos;

import com.example.pos.dao.ProdukDao; // Import ProdukDao
import com.example.pos.dao.impl.ProdukDaoImpl; // Import implementasinya
import com.example.pos.model.Produk;
// import com.example.pos.service.KoreksiStokService; // Tidak perlu lagi jika akses DAO langsung
import com.example.pos.util.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query; // Untuk mengecek keberadaan data

import java.io.IOException;
import java.util.Optional;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Inisialisasi Hibernate SessionFactory saat aplikasi dimulai
            // Ini akan memanggil getSessionFactory() dari HibernateUtil
            // yang sekarang memiliki logging dan pendaftaran entitas programatik
            HibernateUtil.getSessionFactory();
            System.out.println("MainApp: SessionFactory berhasil diinisialisasi.");

            // Tambah data dummy jika diperlukan untuk testing UI
            tambahDataDummy();
            System.out.println("MainApp: Proses tambahDataDummy selesai.");


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pos/view/koreksi_stok_view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 900, 700);

            primaryStage.setTitle("Aplikasi POS - Koreksi Stok");
            primaryStage.setScene(scene);
            primaryStage.show();
            System.out.println("MainApp: Aplikasi JavaFX berhasil ditampilkan.");

        } catch (IOException e) {
            System.err.println("MainApp: Gagal memuat FXML: " + e.getMessage());
            e.printStackTrace();
            // Tampilkan alert ke pengguna di aplikasi production
        } catch (Exception e) {
            System.err.println("MainApp: Terjadi error saat memulai aplikasi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        // Tutup SessionFactory saat aplikasi berhenti
        HibernateUtil.shutdown();
        super.stop();
    }

    private void tambahDataDummy() {
        System.out.println("MainApp: Memulai tambahDataDummy()...");
        // Gunakan DAO secara langsung untuk menambah produk jika belum ada.
        // Ini hanya untuk memudahkan testing UI, idealnya data dikelola terpisah.
        ProdukDao produkDao = new ProdukDaoImpl(); // Buat instance DAO

        // Produk 1
        tambahProdukJikaTidakAda(produkDao, "P001", "Buku Tulis Sinar Dunia A5", 50, 3000, 4500);
        // Produk 2
        tambahProdukJikaTidakAda(produkDao, "P002", "Pensil 2B Faber-Castell", 100, 1500, 2500);
        // Produk 3
        tambahProdukJikaTidakAda(produkDao, "P003", "Penghapus Joyko Besar", 75, 1000, 1500);
        // Produk 4
        tambahProdukJikaTidakAda(produkDao, "P004", "Penggaris Plastik 30cm", 30, 2000, 3000);
        // Produk 5
        tambahProdukJikaTidakAda(produkDao, "P005", "Spidol Whiteboard Hitam", 40, 5000, 7500);
        // Produk 6
        tambahProdukJikaTidakAda(produkDao, "P006", "Kertas A4 SIDU 70gr (Rim)", 10, 45000, 55000);
        // Produk 7
        tambahProdukJikaTidakAda(produkDao, "P007", "Binder Clip No. 107 (Box)", 25, 3500, 5000);
        // Produk 8
        tambahProdukJikaTidakAda(produkDao, "P008", "Tipe-X Cair Kenko", 35, 4000, 6000);

        System.out.println("MainApp: Data dummy produk selesai diperiksa/ditambahkan.");
    }

    /**
     * Metode helper untuk menambahkan produk jika belum ada berdasarkan kode produk.
     * Menggunakan transaksi Hibernate.
     */
    private void tambahProdukJikaTidakAda(ProdukDao produkDao, String kode, String nama, int stok, double hargaBeli, double hargaJual) {


        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Cek apakah produk dengan kode tersebut sudah ada
            Query<Long> query = session.createQuery("SELECT count(p) FROM Produk p WHERE p.kodeProduk = :kode", Long.class);
            query.setParameter("kode", kode);
            Long count = query.uniqueResult();

            if (count == 0) { // Jika belum ada
                Produk p = new Produk(kode, nama, stok, hargaBeli, hargaJual);
                session.save(p); // Menggunakan session.save() lebih tepat untuk objek baru
                System.out.println("MainApp: Menambahkan produk dummy: " + nama);
            } else {
                System.out.println("MainApp: Produk dummy '" + nama + "' sudah ada, tidak ditambahkan lagi.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (Exception rbEx) {
                    System.err.println("MainApp: Gagal melakukan rollback transaksi untuk produk " + kode + ": " + rbEx.getMessage());
                }
            }
            System.err.println("MainApp: Gagal menambahkan/mengecek produk dummy '" + nama + "': " + e.getMessage());
            // e.printStackTrace(); // Aktifkan jika perlu debug lebih lanjut
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
