package com.example.pos.service;

import com.example.pos.dao.KoreksiStokDao;
import com.example.pos.dao.ProdukDao;
import com.example.pos.model.KoreksiStok;
import com.example.pos.model.Produk;
import com.example.pos.patterns.behavioral.PenambahanStokStrategy;
import com.example.pos.patterns.behavioral.PenguranganStokStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension; // Pastikan import ini ada

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

// Static imports untuk fungsi Mockito dan Assertions
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Ini penting untuk mengaktifkan anotasi Mockito
@DisplayName("Tes untuk KoreksiStokService")
class KoreksiStokServiceTest {

    @Mock // Membuat mock object untuk ProdukDao
    private ProdukDao mockProdukDao;

    @Mock // Membuat mock object untuk KoreksiStokDao
    private KoreksiStokDao mockKoreksiStokDao;

    @InjectMocks // Menginjeksikan mock objects ke KoreksiStokService
    private KoreksiStokService koreksiStokService;

    private Produk produkTest;
    private final int ID_PRODUK_TEST = 1;
    private final String KODE_PRODUK_TEST = "PTEST";

    @BeforeEach
    void setUp() {
        // Setup data produk awal untuk setiap tes
        produkTest = new Produk(KODE_PRODUK_TEST, "Produk Test", 10, 1000, 1500);
        produkTest.setIdProduk(ID_PRODUK_TEST);
    }

    @Nested
    @DisplayName("Tes untuk metode prosesKoreksiStok")
    class ProsesKoreksiStokTests {
        @Test
        @DisplayName("Penambahan stok berhasil")
        void testProsesKoreksiStok_PenambahanBerhasil() {
            // Arrange
            int jumlahPenambahan = 5;
            String catatan = "Penambahan stok manual";
            PenambahanStokStrategy strategy = new PenambahanStokStrategy();

            when(mockProdukDao.findById(ID_PRODUK_TEST)).thenReturn(Optional.of(produkTest));

            // Act
            boolean hasil = koreksiStokService.prosesKoreksiStok(ID_PRODUK_TEST, jumlahPenambahan, strategy, catatan);

            // Assert
            assertTrue(hasil, "Proses koreksi stok seharusnya berhasil.");
            assertEquals(15, produkTest.getStok(), "Stok produk seharusnya bertambah menjadi 15.");

            verify(mockProdukDao, times(1)).update(produkTest);

            ArgumentCaptor<KoreksiStok> koreksiStokCaptor = ArgumentCaptor.forClass(KoreksiStok.class);
            verify(mockKoreksiStokDao, times(1)).save(koreksiStokCaptor.capture());

            KoreksiStok koreksiTersimpan = koreksiStokCaptor.getValue();
            assertEquals(produkTest, koreksiTersimpan.getProduk());
            assertEquals(jumlahPenambahan, koreksiTersimpan.getJumlahKoreksi());
            assertEquals(10, koreksiTersimpan.getStokSebelum());
            assertEquals(15, koreksiTersimpan.getStokSesudah());
            assertEquals(strategy.getTipeKoreksi(), koreksiTersimpan.getTipeKoreksi());
            assertEquals(catatan, koreksiTersimpan.getCatatan());
        }

        @Test
        @DisplayName("Pengurangan stok berhasil")
        void testProsesKoreksiStok_PenguranganBerhasil() {
            // Arrange
            int jumlahPengurangan = 3;
            String catatan = "Pengurangan stok karena rusak";
            PenguranganStokStrategy strategy = new PenguranganStokStrategy();

            produkTest.setStok(10); // Reset stok awal

            when(mockProdukDao.findById(ID_PRODUK_TEST)).thenReturn(Optional.of(produkTest));

            // Act
            boolean hasil = koreksiStokService.prosesKoreksiStok(ID_PRODUK_TEST, jumlahPengurangan, strategy, catatan);

            // Assert
            assertTrue(hasil);
            assertEquals(7, produkTest.getStok());

            verify(mockProdukDao).update(produkTest);

            ArgumentCaptor<KoreksiStok> koreksiStokCaptor = ArgumentCaptor.forClass(KoreksiStok.class);
            verify(mockKoreksiStokDao).save(koreksiStokCaptor.capture());
            KoreksiStok koreksiTersimpan = koreksiStokCaptor.getValue();

            assertEquals(-jumlahPengurangan, koreksiTersimpan.getJumlahKoreksi());
            assertEquals(10, koreksiTersimpan.getStokSebelum());
            assertEquals(7, koreksiTersimpan.getStokSesudah());
            assertEquals(strategy.getTipeKoreksi(), koreksiTersimpan.getTipeKoreksi());
        }

        @Test
        @DisplayName("Produk tidak ditemukan saat koreksi")
        void testProsesKoreksiStok_ProdukTidakDitemukan() {
            // Arrange
            int idProdukTidakAda = 99;
            when(mockProdukDao.findById(idProdukTidakAda)).thenReturn(Optional.empty());

            // Act
            boolean hasil = koreksiStokService.prosesKoreksiStok(idProdukTidakAda, 5, new PenambahanStokStrategy(), "Test");

            // Assert
            assertFalse(hasil, "Proses koreksi seharusnya gagal jika produk tidak ditemukan.");
            verify(mockProdukDao, never()).update(any(Produk.class));
            verify(mockKoreksiStokDao, never()).save(any(KoreksiStok.class));
        }

        @Test
        @DisplayName("Pengurangan melebihi stok yang ada")
        void testProsesKoreksiStok_PenguranganMelebihiStok() {
            // Arrange
            int jumlahPengurangan = 15; // Stok awal 10
            PenguranganStokStrategy strategy = new PenguranganStokStrategy();

            produkTest.setStok(10);

            when(mockProdukDao.findById(ID_PRODUK_TEST)).thenReturn(Optional.of(produkTest));

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                koreksiStokService.prosesKoreksiStok(ID_PRODUK_TEST, jumlahPengurangan, strategy, "Pengurangan terlalu banyak");
            });

            assertTrue(exception.getMessage().contains("Stok tidak mencukupi"));
            assertEquals(10, produkTest.getStok(), "Stok produk seharusnya tidak berubah.");
            verify(mockProdukDao, never()).update(any(Produk.class));
            verify(mockKoreksiStokDao, never()).save(any(KoreksiStok.class));
        }

        @Test
        @DisplayName("Jumlah koreksi nol atau negatif")
        void testProsesKoreksiStok_JumlahKoreksiTidakValid() {
            // Arrange
            PenguranganStokStrategy strategy = new PenguranganStokStrategy();

            // Act & Assert for jumlah 0
            IllegalArgumentException exceptionNol = assertThrows(IllegalArgumentException.class, () -> {
                koreksiStokService.prosesKoreksiStok(ID_PRODUK_TEST, 0, strategy, "Jumlah nol");
            });
            assertTrue(exceptionNol.getMessage().contains("Jumlah koreksi harus lebih besar dari 0"));

            // Act & Assert for jumlah negatif
            IllegalArgumentException exceptionNegatif = assertThrows(IllegalArgumentException.class, () -> {
                koreksiStokService.prosesKoreksiStok(ID_PRODUK_TEST, -5, strategy, "Jumlah negatif");
            });
            assertTrue(exceptionNegatif.getMessage().contains("Jumlah koreksi harus lebih besar dari 0"));

            verify(mockProdukDao, never()).update(any(Produk.class));
            verify(mockKoreksiStokDao, never()).save(any(KoreksiStok.class));
        }
    }

    @Nested
    @DisplayName("Tes untuk metode pencarian produk")
    class PencarianProdukTests {
        @Test
        @DisplayName("getProdukById - Produk ditemukan")
        void testGetProdukById_ProdukDitemukan() {
            // Arrange
            when(mockProdukDao.findById(ID_PRODUK_TEST)).thenReturn(Optional.of(produkTest));

            // Act
            Optional<Produk> hasil = koreksiStokService.getProdukById(ID_PRODUK_TEST);

            // Assert
            assertTrue(hasil.isPresent(), "Produk seharusnya ditemukan.");
            assertEquals(produkTest, hasil.get(), "Produk yang dikembalikan seharusnya sama.");
            verify(mockProdukDao, times(1)).findById(ID_PRODUK_TEST);
        }

        @Test
        @DisplayName("getProdukById - Produk tidak ditemukan")
        void testGetProdukById_ProdukTidakDitemukan() {
            // Arrange
            int idTidakAda = 999;
            when(mockProdukDao.findById(idTidakAda)).thenReturn(Optional.empty());

            // Act
            Optional<Produk> hasil = koreksiStokService.getProdukById(idTidakAda);

            // Assert
            assertFalse(hasil.isPresent(), "Produk seharusnya tidak ditemukan.");
            verify(mockProdukDao, times(1)).findById(idTidakAda);
        }

        @Test
        @DisplayName("getProdukByKode - Produk ditemukan")
        void testGetProdukByKode_ProdukDitemukan() {
            // Arrange
            when(mockProdukDao.findByKode(KODE_PRODUK_TEST)).thenReturn(Optional.of(produkTest));

            // Act
            Optional<Produk> hasil = koreksiStokService.getProdukByKode(KODE_PRODUK_TEST);

            // Assert
            assertTrue(hasil.isPresent());
            assertEquals(produkTest, hasil.get());
            verify(mockProdukDao, times(1)).findByKode(KODE_PRODUK_TEST);
        }

        @Test
        @DisplayName("cariProdukByNama - Ditemukan beberapa produk")
        void testCariProdukByNama_DitemukanBeberapa() {
            // Arrange
            String namaCari = "Test";
            List<Produk> daftarHasil = List.of(produkTest, new Produk("P002", "Produk Test Lain", 5, 0, 0));
            when(mockProdukDao.searchByNama(namaCari)).thenReturn(daftarHasil);

            // Act
            List<Produk> hasil = koreksiStokService.cariProdukByNama(namaCari);

            // Assert
            assertNotNull(hasil);
            assertEquals(2, hasil.size());
            assertEquals(daftarHasil, hasil);
            verify(mockProdukDao, times(1)).searchByNama(namaCari);
        }

        @Test
        @DisplayName("getAllProduk - Mengembalikan semua produk")
        void testGetAllProduk() {
            // Arrange
            List<Produk> semuaProduk = List.of(produkTest, new Produk("P00X", "Produk X", 1, 0, 0));
            when(mockProdukDao.findAll()).thenReturn(semuaProduk);

            // Act
            List<Produk> hasil = koreksiStokService.getAllProduk();

            // Assert
            assertNotNull(hasil);
            assertEquals(2, hasil.size());
            assertEquals(semuaProduk, hasil);
            verify(mockProdukDao, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Tes untuk histori koreksi")
    class HistoriKoreksiTests {
        @Test
        @DisplayName("getHistoriKoreksiByProduk - Mengembalikan histori")
        void testGetHistoriKoreksiByProduk() {
            // Arrange
            KoreksiStok koreksi1 = new KoreksiStok(produkTest, 5, 10, 15, "PENAMBAHAN", "Catatan1");
            koreksi1.setWaktuKoreksi(LocalDateTime.now().minusDays(1));
            KoreksiStok koreksi2 = new KoreksiStok(produkTest, -2, 15, 13, "PENGURANGAN", "Catatan2");
            koreksi2.setWaktuKoreksi(LocalDateTime.now());
            List<KoreksiStok> daftarHistori = List.of(koreksi2, koreksi1);

            when(mockKoreksiStokDao.findByProdukId(ID_PRODUK_TEST)).thenReturn(daftarHistori);

            // Act
            List<KoreksiStok> hasil = koreksiStokService.getHistoriKoreksiByProduk(ID_PRODUK_TEST);

            // Assert
            assertNotNull(hasil);
            assertEquals(2, hasil.size());
            assertEquals(daftarHistori, hasil);
            verify(mockKoreksiStokDao, times(1)).findByProdukId(ID_PRODUK_TEST);
        }

        @Test
        @DisplayName("getHistoriKoreksiByProduk - Tidak ada histori")
        void testGetHistoriKoreksiByProduk_TidakAdaHistori() {
            // Arrange
            when(mockKoreksiStokDao.findByProdukId(ID_PRODUK_TEST)).thenReturn(new ArrayList<>()); // List kosong

            // Act
            List<KoreksiStok> hasil = koreksiStokService.getHistoriKoreksiByProduk(ID_PRODUK_TEST);

            // Assert
            assertNotNull(hasil);
            assertTrue(hasil.isEmpty(), "Seharusnya tidak ada histori yang dikembalikan.");
            verify(mockKoreksiStokDao, times(1)).findByProdukId(ID_PRODUK_TEST);
        }
    }
}
