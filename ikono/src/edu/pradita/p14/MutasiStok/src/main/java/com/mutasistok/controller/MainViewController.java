package com.mutasistok.controller;

import com.mutasistok.dao.BarangDao;
import com.mutasistok.dao.GudangDao;
import com.mutasistok.dao.MutasiStokDao;
import com.mutasistok.model.Barang;
import com.mutasistok.model.Gudang;
import com.mutasistok.model.MutasiStok;
import com.mutasistok.model.TipeMutasi;
// Import StokService (sesuaikan jika nama kelas service berbeda)
import com.mutasistok.service.StokService;


import javafx.beans.property.SimpleStringProperty; // Import SimpleStringProperty
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.util.List;

public class MainViewController {

    @FXML private ComboBox<TipeMutasi> tipeMutasiComboBox;
    @FXML private ComboBox<Barang> barangComboBox;
    @FXML private ComboBox<Gudang> gudangAsalComboBox;
    @FXML private ComboBox<Gudang> gudangTujuanComboBox;
    @FXML private TextField jumlahField;
    @FXML private TextField keteranganField;
    @FXML private TableView<MutasiStok> mutasiTableView;

    private final BarangDao barangDao = new BarangDao();
    private final GudangDao gudangDao = new GudangDao();
    private final MutasiStokDao mutasiStokDao = new MutasiStokDao();
    private final StokService stokService = new StokService();

    private final ObservableList<MutasiStok> mutasiData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupComboBoxes();
        setupTableColumns();
        loadInitialData();
        addComboBoxListeners();
        // Atur status awal ComboBox gudang berdasarkan nilai awal tipeMutasiComboBox
        updateGudangComboBoxStates(tipeMutasiComboBox.getValue());
    }

    /**
     * Mengatur status ComboBox gudang (aktif/nonaktif, nilai) berdasarkan TipeMutasi.
     * @param tipe TipeMutasi yang dipilih, atau null jika tidak ada yang dipilih.
     */
    private void updateGudangComboBoxStates(TipeMutasi tipe) {
        gudangAsalComboBox.setDisable(true);
        gudangAsalComboBox.setValue(null);
        gudangTujuanComboBox.setDisable(true);
        gudangTujuanComboBox.setValue(null);

        if (tipe != null) {
            switch (tipe) {
                case MASUK:
                    gudangTujuanComboBox.setDisable(false);
                    break;
                case KELUAR:
                    gudangAsalComboBox.setDisable(false);
                    break;
                case TRANSFER:
                    gudangAsalComboBox.setDisable(false);
                    gudangTujuanComboBox.setDisable(false);
                    break;
                default:
                    // Jika ada tipe lain yang tidak ditangani, biarkan nonaktif
                    break;
            }
        }
    }

    private void addComboBoxListeners() {
        tipeMutasiComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateGudangComboBoxStates(newVal);
        });
    }

    @FXML
    private void handleProsesMutasi() {
        try {
            TipeMutasi tipe = tipeMutasiComboBox.getValue();
            Barang barang = barangComboBox.getValue();
            Gudang gudangAsal = gudangAsalComboBox.getValue();
            Gudang gudangTujuan = gudangTujuanComboBox.getValue();
            String jumlahText = jumlahField.getText();
            String keterangan = keteranganField.getText();

            if (jumlahText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "Jumlah wajib diisi.");
                return;
            }

            int jumlah = Integer.parseInt(jumlahText);

            if (tipe == null || barang == null || jumlah <= 0) {
                showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "Tipe Mutasi, Barang, dan Jumlah (harus > 0) wajib diisi.");
                return;
            }
            if ((tipe == TipeMutasi.KELUAR || tipe == TipeMutasi.TRANSFER) && gudangAsal == null) {
                showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "Gudang Asal wajib diisi untuk mutasi KELUAR atau TRANSFER.");
                return;
            }
            if ((tipe == TipeMutasi.MASUK || tipe == TipeMutasi.TRANSFER) && gudangTujuan == null) {
                showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "Gudang Tujuan wajib diisi untuk mutasi MASUK atau TRANSFER.");
                return;
            }
            if (tipe == TipeMutasi.TRANSFER && gudangAsal != null && gudangAsal.equals(gudangTujuan)) {
                showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "Gudang Asal dan Gudang Tujuan tidak boleh sama untuk TRANSFER.");
                return;
            }

            MutasiStok mutasiStok = new MutasiStok();
            mutasiStok.setTipeMutasi(tipe);
            mutasiStok.setBarang(barang);
            mutasiStok.setJumlah(jumlah);
            mutasiStok.setKeterangan(keterangan);
            mutasiStok.setTanggalMutasi(LocalDateTime.now());

            if (tipe == TipeMutasi.MASUK) {
                mutasiStok.setGudangTujuan(gudangTujuan);
            } else if (tipe == TipeMutasi.KELUAR) {
                mutasiStok.setGudangAsal(gudangAsal);
            } else if (tipe == TipeMutasi.TRANSFER) {
                mutasiStok.setGudangAsal(gudangAsal);
                mutasiStok.setGudangTujuan(gudangTujuan);
            }

            stokService.prosesMutasi(mutasiStok);

            showAlert(Alert.AlertType.INFORMATION, "Operasi Berhasil", "Mutasi stok berhasil diproses.");
            loadInitialData();
            clearForm();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Salah", "Jumlah harus berupa angka.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Operasi Gagal", "Terjadi kesalahan: " + e.getMessage());
        }
    }

    private void loadInitialData() {
        List<Barang> barangList = barangDao.getAll();
        barangComboBox.setItems(FXCollections.observableArrayList(barangList != null ? barangList : List.of()));

        List<Gudang> gudangList = gudangDao.getAll();
        ObservableList<Gudang> observableGudangList = FXCollections.observableArrayList(gudangList != null ? gudangList : List.of());
        gudangAsalComboBox.setItems(observableGudangList);
        gudangTujuanComboBox.setItems(observableGudangList);

        mutasiData.clear();
        List<MutasiStok> riwayatMutasi = mutasiStokDao.getAll();
        if (riwayatMutasi != null) {
            mutasiData.addAll(riwayatMutasi);
        }
        mutasiTableView.setItems(mutasiData);
    }

    private void setupComboBoxes() {
        tipeMutasiComboBox.setItems(FXCollections.observableArrayList(TipeMutasi.values()));
        tipeMutasiComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(TipeMutasi tipe) {
                return tipe == null ? null : tipe.name();
            }
            @Override
            public TipeMutasi fromString(String string) { return null; }
        });

        barangComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Barang barang) {
                return barang == null ? null : barang.getNamaBarang() + " (" + barang.getKodeBarang() + ")";
            }
            @Override
            public Barang fromString(String string) { return null; }
        });

        StringConverter<Gudang> gudangConverter = new StringConverter<>() {
            @Override
            public String toString(Gudang gudang) {
                return gudang == null ? null : gudang.getNamaGudang() + " (" + gudang.getKodeGudang() + ")";
            }
            @Override
            public Gudang fromString(String string) { return null; }
        };
        gudangAsalComboBox.setConverter(gudangConverter);
        gudangTujuanComboBox.setConverter(gudangConverter);
    }

    private void setupTableColumns() {
        TableColumn<MutasiStok, LocalDateTime> tanggalCol = new TableColumn<>("Tanggal");
        tanggalCol.setCellValueFactory(new PropertyValueFactory<>("tanggalMutasi"));

        TableColumn<MutasiStok, TipeMutasi> tipeCol = new TableColumn<>("Tipe");
        tipeCol.setCellValueFactory(new PropertyValueFactory<>("tipeMutasi"));

        TableColumn<MutasiStok, String> barangCol = new TableColumn<>("Barang");
        barangCol.setCellValueFactory(cellData -> {
            Barang barang = cellData.getValue().getBarang();
            return new SimpleStringProperty(barang != null ? barang.getNamaBarang() : "");
        });

        TableColumn<MutasiStok, Integer> jumlahCol = new TableColumn<>("Jumlah");
        jumlahCol.setCellValueFactory(new PropertyValueFactory<>("jumlah"));

        TableColumn<MutasiStok, String> gudangAsalCol = new TableColumn<>("Gudang Asal");
        gudangAsalCol.setCellValueFactory(cellData -> {
            Gudang gudang = cellData.getValue().getGudangAsal();
            return new SimpleStringProperty(gudang != null ? gudang.getNamaGudang() : "");
        });

        TableColumn<MutasiStok, String> gudangTujuanCol = new TableColumn<>("Gudang Tujuan");
        gudangTujuanCol.setCellValueFactory(cellData -> {
            Gudang gudang = cellData.getValue().getGudangTujuan();
            return new SimpleStringProperty(gudang != null ? gudang.getNamaGudang() : "");
        });

        TableColumn<MutasiStok, String> keteranganCol = new TableColumn<>("Keterangan");
        keteranganCol.setCellValueFactory(new PropertyValueFactory<>("keterangan"));

        mutasiTableView.getColumns().setAll(tanggalCol, tipeCol, barangCol, jumlahCol, gudangAsalCol, gudangTujuanCol, keteranganCol);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        tipeMutasiComboBox.setValue(null); // Ini akan memicu listener dan updateGudangComboBoxStates(null)
        barangComboBox.setValue(null);
        jumlahField.clear();
        keteranganField.clear();
        // gudangAsalComboBox dan gudangTujuanComboBox akan direset oleh updateGudangComboBoxStates
    }
}
