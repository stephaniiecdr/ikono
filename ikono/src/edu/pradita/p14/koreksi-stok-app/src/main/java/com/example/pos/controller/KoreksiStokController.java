package com.example.pos.controller;

import com.example.pos.model.KoreksiStok;
import com.example.pos.model.Produk;
// Tambahkan import untuk strategi
import com.example.pos.patterns.behavioral.KoreksiStokStrategy;
import com.example.pos.patterns.behavioral.PenambahanStokStrategy;
import com.example.pos.patterns.behavioral.PenguranganStokStrategy;
import com.example.pos.service.KoreksiStokService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
// import javafx.util.StringConverter; // Tidak digunakan secara eksplisit, bisa dihapus jika tidak perlu

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class KoreksiStokController {

    @FXML private TextField txtKodeProduk;
    @FXML private TextField txtNamaProduk;
    @FXML private TextField txtStokSaatIni;
    @FXML private TextField txtJumlahKoreksi;
    @FXML private ComboBox<String> cmbTipeKoreksi; // PENAMBAHAN, PENGURANGAN
    @FXML private TextArea txtCatatan;
    @FXML private Button btnCariProduk;
    @FXML private Button btnSimpanKoreksi;
    @FXML private Label lblPesanError;
    @FXML private Label lblPesanSukses;

    @FXML private ListView<Produk> listViewHasilPencarian;
    private ObservableList<Produk> produkObservableList = FXCollections.observableArrayList();

    @FXML private TableView<KoreksiStok> tableViewHistoriKoreksi;
    @FXML private TableColumn<KoreksiStok, LocalDateTime> colWaktu;
    @FXML private TableColumn<KoreksiStok, String> colTipe;
    @FXML private TableColumn<KoreksiStok, Integer> colJumlah;
    @FXML private TableColumn<KoreksiStok, Integer> colStokSebelum;
    @FXML private TableColumn<KoreksiStok, Integer> colStokSesudah;
    @FXML private TableColumn<KoreksiStok, String> colCatatanHistori;
    private ObservableList<KoreksiStok> historiObservableList = FXCollections.observableArrayList();


    private KoreksiStokService koreksiStokService;
    private Produk produkTerpilih;

    public KoreksiStokController() {
        // Inisialisasi service. Pertimbangkan dependency injection di aplikasi yang lebih besar.
        this.koreksiStokService = new KoreksiStokService();
    }

    @FXML
    public void initialize() {
        cmbTipeKoreksi.setItems(FXCollections.observableArrayList("PENAMBAHAN", "PENGURANGAN"));
        cmbTipeKoreksi.setValue("PENAMBAHAN"); // Default value

        listViewHasilPencarian.setItems(produkObservableList);
        // Menggunakan CellFactory untuk menampilkan informasi produk di ListView
        listViewHasilPencarian.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Produk item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getNamaProduk() == null) {
                    setText(null);
                } else {
                    setText(item.getKodeProduk() + " - " + item.getNamaProduk() + " (Stok: " + item.getStok() + ")");
                }
            }
        });

        // Listener untuk item yang dipilih di ListView
        listViewHasilPencarian.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        pilihProduk(newValue);
                    }
                }
        );

        // Setup TableView untuk histori koreksi stok
        colWaktu.setCellValueFactory(new PropertyValueFactory<>("waktuKoreksi"));
        // Formatter untuk kolom waktu agar mudah dibaca
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        colWaktu.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });
        colTipe.setCellValueFactory(new PropertyValueFactory<>("tipeKoreksi"));
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlahKoreksi"));
        colStokSebelum.setCellValueFactory(new PropertyValueFactory<>("stokSebelum"));
        colStokSesudah.setCellValueFactory(new PropertyValueFactory<>("stokSesudah"));
        colCatatanHistori.setCellValueFactory(new PropertyValueFactory<>("catatan"));
        tableViewHistoriKoreksi.setItems(historiObservableList);

        bersihkanPesan(); // Bersihkan pesan error/sukses saat inisialisasi
    }

    @FXML
    private void handleCariProduk() {
        bersihkanPesan();
        String keyword = txtKodeProduk.getText().trim();
        // Jika input kode produk kosong, coba cari berdasarkan nama produk
        if (keyword.isEmpty()) {
            keyword = txtNamaProduk.getText().trim();
        }

        // Jika kedua input (kode dan nama) kosong, tampilkan semua produk
        if (keyword.isEmpty()) {
            produkObservableList.setAll(koreksiStokService.getAllProduk());
            if (produkObservableList.isEmpty()) {
                lblPesanError.setText("Tidak ada produk dalam sistem untuk ditampilkan.");
            }
            listViewHasilPencarian.setVisible(!produkObservableList.isEmpty()); // Tampilkan jika ada hasil
            return;
        }

        // Prioritaskan pencarian berdasarkan kode produk
        Optional<Produk> produkByKode = koreksiStokService.getProdukByKode(keyword);
        if (produkByKode.isPresent()) {
            produkObservableList.setAll(produkByKode.get());
        } else {
            // Jika tidak ditemukan berdasarkan kode, cari berdasarkan nama
            List<Produk> hasilCariNama = koreksiStokService.cariProdukByNama(keyword);
            if (hasilCariNama.isEmpty()) {
                lblPesanError.setText("Produk dengan kode atau nama '" + keyword + "' tidak ditemukan.");
                produkObservableList.clear();
            } else {
                produkObservableList.setAll(hasilCariNama);
            }
        }
        listViewHasilPencarian.setVisible(!produkObservableList.isEmpty()); // Tampilkan jika ada hasil
    }

    /**
     * Dipanggil ketika produk dipilih dari ListView hasil pencarian.
     * Mengisi field-form dengan detail produk yang dipilih dan memuat histori koreksinya.
     * @param produk Produk yang dipilih.
     */
    private void pilihProduk(Produk produk) {
        produkTerpilih = produk;
        txtKodeProduk.setText(produk.getKodeProduk());
        txtNamaProduk.setText(produk.getNamaProduk());
        txtStokSaatIni.setText(String.valueOf(produk.getStok()));
        lblPesanError.setText(""); // Bersihkan pesan error sebelumnya
        listViewHasilPencarian.setVisible(false); // Sembunyikan ListView setelah produk dipilih
        muatHistoriKoreksi(produk.getIdProduk()); // Muat histori untuk produk ini
    }

    /**
     * Memuat dan menampilkan histori koreksi stok untuk produk tertentu.
     * @param idProduk ID dari produk yang historinya akan dimuat.
     */
    private void muatHistoriKoreksi(int idProduk) {
        List<KoreksiStok> histori = koreksiStokService.getHistoriKoreksiByProduk(idProduk);
        historiObservableList.setAll(histori);
    }

    @FXML
    private void handleSimpanKoreksi() {
        bersihkanPesan();
        if (produkTerpilih == null) {
            lblPesanError.setText("Pilih produk terlebih dahulu sebelum menyimpan koreksi.");
            return;
        }

        String jumlahKoreksiStr = txtJumlahKoreksi.getText();
        // Perbaikan: Nama variabel yang benar adalah tipeKoreksiTerpilih atau gabungkan menjadi satu nama variabel
        String tipeKoreksiTerpilih = cmbTipeKoreksi.getValue();
        String catatan = txtCatatan.getText().trim();

        int jumlahKoreksiNilai;
        try {
            jumlahKoreksiNilai = Integer.parseInt(jumlahKoreksiStr);
            if (jumlahKoreksiNilai <= 0) {
                lblPesanError.setText("Jumlah koreksi harus merupakan angka positif.");
                return;
            }
        } catch (NumberFormatException e) {
            lblPesanError.setText("Format jumlah koreksi tidak valid. Masukkan angka.");
            return;
        }

        KoreksiStokStrategy strategy;
        if ("PENAMBAHAN".equals(tipeKoreksiTerpilih)) {
            strategy = new PenambahanStokStrategy();
        } else if ("PENGURANGAN".equals(tipeKoreksiTerpilih)) {
            strategy = new PenguranganStokStrategy();
        } else {
            lblPesanError.setText("Tipe koreksi tidak valid atau belum dipilih.");
            return;
        }

        try {
            boolean berhasil = koreksiStokService.prosesKoreksiStok(
                    produkTerpilih.getIdProduk(),
                    jumlahKoreksiNilai,
                    strategy,
                    catatan
            );

            if (berhasil) {
                lblPesanSukses.setText("Koreksi stok berhasil disimpan.");
                // Refresh data produk yang dipilih untuk menampilkan stok terbaru dan histori
                Optional<Produk> produkUpdated = koreksiStokService.getProdukById(produkTerpilih.getIdProduk());
                produkUpdated.ifPresent(this::pilihProduk);
                bersihkanFormInputKoreksi(); // Bersihkan field input untuk koreksi berikutnya
            } else {
                // Pesan error spesifik seharusnya sudah ditangani/dilempar dari service
                // jika produk tidak ditemukan atau ada masalah lain yang membuat `prosesKoreksiStok` return false.
                lblPesanError.setText("Gagal menyimpan koreksi stok. Periksa kembali data.");
            }
        } catch (IllegalArgumentException e) {
            // Tangkap error validasi dari strategi atau service layer
            lblPesanError.setText("Error validasi: " + e.getMessage());
        } catch (Exception e) {
            // Tangkap error umum lainnya
            lblPesanError.setText("Terjadi kesalahan sistem: " + e.getMessage());
            e.printStackTrace(); // Penting untuk logging di sisi developer
        }
    }

    @FXML
    private void handleInputKodeProduk() {
        // Tampilkan ListView hasil pencarian saat pengguna mulai mengetik di field kode produk
        if (!listViewHasilPencarian.isVisible()) {
            listViewHasilPencarian.setVisible(true);
        }
        // Anda bisa menambahkan logika auto-search di sini jika diperlukan,
        // misalnya memanggil handleCariProduk() setelah delay tertentu.
    }

    @FXML
    private void handleInputNamaProduk() {
        // Tampilkan ListView hasil pencarian saat pengguna mulai mengetik di field nama produk
        if (!listViewHasilPencarian.isVisible()) {
            listViewHasilPencarian.setVisible(true);
        }
    }

    /**
     * Membersihkan field input yang terkait dengan proses koreksi (jumlah dan catatan),
     * dan mereset pilihan tipe koreksi.
     */
    private void bersihkanFormInputKoreksi() {
        txtJumlahKoreksi.clear();
        txtCatatan.clear();
        cmbTipeKoreksi.setValue("PENAMBAHAN"); // Reset ke default
    }

    /**
     * Membersihkan label pesan error dan sukses.
     */
    private void bersihkanPesan() {
        lblPesanError.setText("");
        lblPesanSukses.setText("");
    }

    /**
     * Mereset seluruh form ke kondisi awal.
     */
    @FXML
    private void handleResetForm() {
        produkTerpilih = null;
        txtKodeProduk.clear();
        txtNamaProduk.clear();
        txtStokSaatIni.clear();
        txtJumlahKoreksi.clear();
        txtCatatan.clear();
        cmbTipeKoreksi.setValue("PENAMBAHAN");
        produkObservableList.clear();
        historiObservableList.clear();
        listViewHasilPencarian.setVisible(true); // Tampilkan kembali list view
        bersihkanPesan();
    }
}
