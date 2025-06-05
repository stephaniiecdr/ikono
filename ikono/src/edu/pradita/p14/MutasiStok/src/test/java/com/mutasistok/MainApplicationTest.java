package com.mutasistok;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Catatan: Pengujian aplikasi JavaFX dengan JUnit bisa menjadi rumit.
 * Kelas ini mencoba mengelola siklus hidup JavaFX secara manual untuk pengujian dasar.
 * Untuk pengujian UI yang lebih andal, pertimbangkan untuk menggunakan framework seperti TestFX.
 */
class MainApplicationTest {

    private static final AtomicBoolean isFxRunning = new AtomicBoolean(false);
    private static volatile Stage primaryStageInstance;
    private static volatile Throwable startException;
    private static final CountDownLatch startupLatch = new CountDownLatch(1);

    /**
     * Aplikasi dummy yang menangkap stage utama dan memberi sinyal saat siap.
     */
    public static class TestApp extends MainApplication {
        @Override
        public void start(Stage primaryStage) {
            MainApplicationTest.primaryStageInstance = primaryStage;
            try {
                // Panggil start dari kelas utama untuk memuat FXML, dll.
                super.start(primaryStage);
            } catch (Throwable t) {
                MainApplicationTest.startException = t;
            } finally {
                // Beri sinyal bahwa metode start() telah selesai
                MainApplicationTest.startupLatch.countDown();
            }
        }
    }

    @BeforeAll
    static void setUpClass() throws InterruptedException {
        // Coba luncurkan platform JavaFX hanya jika belum berjalan
        if (isFxRunning.compareAndSet(false, true)) {
            Thread t = new Thread(() -> {
                try {
                    Application.launch(TestApp.class);
                } catch (Exception e) {
                    // Tangkap error jika launch dipanggil lebih dari sekali, meskipun sudah dicegah
                    if (!(e instanceof IllegalStateException && e.getMessage().contains("Application launch must not be called more than once"))) {
                        startException = e;
                        startupLatch.countDown();
                    }
                }
            });
            t.setDaemon(true);
            t.start();

            // Tunggu hingga metode start() selesai
            if (!startupLatch.await(15, TimeUnit.SECONDS)) {
                fail("Timeout menunggu aplikasi JavaFX dimulai.");
            }
        } else {
            // Jika sudah berjalan, tunggu stage diatur oleh start()
            // Ini adalah skenario fallback jika JVM digunakan kembali, tetapi bisa tidak stabil
            int retries = 50;
            while (primaryStageInstance == null && retries-- > 0) {
                Thread.sleep(100);
            }
        }

        if (startException != null) {
            fail("Gagal memulai aplikasi JavaFX", startException);
        }

        if (primaryStageInstance == null) {
            fail("Stage utama tidak diinisialisasi. Tes mungkin berjalan dalam lingkungan yang tidak mendukung UI.");
        }
    }

    @AfterAll
    static void tearDownClass() {
        // Gunakan Platform.exit() untuk membersihkan toolkit JavaFX.
        // Ini adalah cara yang paling andal untuk mematikan platform.
        try {
            if (isFxRunning.get()) {
                Platform.exit();
                isFxRunning.set(false);
            }
        } catch (Exception e) {
            // Abaikan kesalahan jika platform sudah dimatikan
            System.err.println("Mengabaikan error saat teardown (ini wajar jika platform sudah berhenti): " + e.getMessage());
        }
    }

    @Test
    void applicationStartsAndPrimaryStageIsAvailable() throws InterruptedException {
        assertNotNull(primaryStageInstance, "Stage utama seharusnya sudah diinisialisasi.");

        final CountDownLatch latch = new CountDownLatch(1);
        // Jalankan assertion di dalam thread JavaFX untuk memastikan keamanan thread
        Platform.runLater(() -> {
            try {
                assertEquals("Aplikasi Mutasi Stok", primaryStageInstance.getTitle(), "Judul window tidak sesuai.");
                assertTrue(primaryStageInstance.isShowing(), "Stage utama seharusnya ditampilkan.");
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Verifikasi di thread JavaFX timeout.");
        }
    }

    @Test
    void fxmlFileExists() {
        assertNotNull(MainApplication.class.getResource("main-view.fxml"),
                "File FXML 'main-view.fxml' tidak ditemukan di classpath.");
    }
}