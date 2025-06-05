package org.edu.pradita.cabang.cabangs.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.edu.pradita.cabang.cabangs.factory.ServiceFactory;
import org.edu.pradita.cabang.cabangs.Cabang;
import org.edu.pradita.cabang.cabangs.observer.CabangDataObserver;
import org.edu.pradita.cabang.cabangs.observer.CabangDataSubject;
import org.edu.pradita.cabang.cabangs.service.CabangService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.Optional;

// controller jadi observer dari service
public class CabangController implements CabangDataObserver {
    @FXML private TextField idField;
    @FXML private TextField namaField;
    @FXML private TextField alamatField;
    @FXML private TextField teleponField;

    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;


    @FXML private TableView<Cabang> cabangTableView;
    @FXML private TableColumn<Cabang, Long> idColumn;
    @FXML private TableColumn<Cabang, String> namaColumn;
    @FXML private TableColumn<Cabang, String> alamatColumn;
    @FXML private TableColumn<Cabang, String> teleponColumn;

    @FXML private Label statusLabel;

    private CabangService cabangService;
    private ObservableList<Cabang> cabangObservableList;

    @FXML
    public void initialize() {
        cabangService = ServiceFactory.createCabangService(); // Pakai Factory Method
        cabangObservableList = FXCollections.observableArrayList();

        // Jika CabangService adalah Subject dan Controller ini mau jadi Observer
        if (cabangService instanceof CabangDataSubject) {
            ((CabangDataSubject) cabangService).addObserver(this);
        }

        setupTableColumns();
        setupTableSelectionListener();
        loadCabangData();
        clearForm();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idCabang"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("namaCabang"));
        alamatColumn.setCellValueFactory(new PropertyValueFactory<>("alamatCabang"));
        teleponColumn.setCellValueFactory(new PropertyValueFactory<>("teleponCabang"));
        cabangTableView.setItems(cabangObservableList);
    }

    private void setupTableSelectionListener() {
        cabangTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> populateForm(newValue)
        );
    }

    private void loadCabangData() {
        try {
            List<Cabang> cabangList = cabangService.getAllCabang();
            cabangObservableList.setAll(cabangList); // Lebih efisien daripada clear() lalu addAll()
            statusLabel.setText("Data cabang berhasil dimuat. Jumlah: " + cabangList.size());
        } catch (Exception e) {
            showError("Gagal memuat data cabang: " + e.getMessage());
        }
    }

    @FXML
    protected void onHelloButtonClick(ActionEvent event) {
        System.out.println("Tombol Hello diklik!");
        }

    @FXML
    void handleAddAction(ActionEvent event) {
        String nama = namaField.getText();
        String alamat = alamatField.getText();
        String telepon = teleponField.getText();

        if (nama.isEmpty()) {
            showWarning("Nama cabang tidak boleh kosong.");
            namaField.requestFocus();
            return;
        }

        try {
            cabangService.createCabang(nama, alamat, telepon);

            clearForm();
            showSuccess("Cabang '" + nama + "' berhasil ditambahkan.");
        } catch (Exception e) {
            showError("Gagal menambahkan cabang: " + e.getMessage());
        }
    }

    @FXML
    void handleUpdateAction(ActionEvent event) {
        String idStr = idField.getText();
        if (idStr.isEmpty()) {
            showWarning("Pilih cabang dari tabel untuk diupdate.");
            return;
        }

        Long id = Long.parseLong(idStr);
        String nama = namaField.getText();
        String alamat = alamatField.getText();
        String telepon = teleponField.getText();

        if (nama.isEmpty()) {
            showWarning("Nama cabang tidak boleh kosong.");
            namaField.requestFocus();
            return;
        }

        try {
            cabangService.updateCabang(id, nama, alamat, telepon);
            clearForm();
            showSuccess("Cabang '" + nama + "' berhasil diupdate.");
        } catch (Exception e) {
            showError("Gagal mengupdate cabang: " + e.getMessage());
        }
    }

    @FXML
    void handleDeleteAction(ActionEvent event) {
        String idStr = idField.getText();
        if (idStr.isEmpty()) {
            showWarning("Pilih cabang dari tabel untuk dihapus.");
            return;
        }
        Long id = Long.parseLong(idStr);
        Cabang selectedCabang = getSelectedCabangFromTable(); // Ambil info nama untuk pesan konfirmasi

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText("Hapus Cabang: " + (selectedCabang != null ? selectedCabang.getNamaCabang() : "ID " + id));
        alert.setContentText("Apakah Anda yakin ingin menghapus cabang ini?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                cabangService.deleteCabang(id);
                clearForm();
                showSuccess("Cabang berhasil dihapus.");
            } catch (Exception e) {
                showError("Gagal menghapus cabang: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleClearAction(ActionEvent event) {
        clearForm();
        statusLabel.setText("Form dibersihkan.");
    }

    @FXML
    void handleRefreshAction(ActionEvent event) {
        loadCabangData();
        clearForm();
    }

    private void populateForm(Cabang cabang) {
        if (cabang != null) {
            idField.setText(String.valueOf(cabang.getIdCabang()));
            namaField.setText(cabang.getNamaCabang());
            alamatField.setText(cabang.getAlamatCabang() != null ? cabang.getAlamatCabang() : "");
            teleponField.setText(cabang.getTeleponCabang() != null ? cabang.getTeleponCabang() : "");
            addButton.setDisable(true);
            updateButton.setDisable(false);
            deleteButton.setDisable(false);
        } else {
            clearForm();
        }
    }

    private void clearForm() {
        idField.clear();
        namaField.clear();
        alamatField.clear();
        teleponField.clear();
        cabangTableView.getSelectionModel().clearSelection();
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        namaField.requestFocus();
    }

    private Cabang getSelectedCabangFromTable() {
        return cabangTableView.getSelectionModel().getSelectedItem();
    }

    private void showSuccess(String message) {
        statusLabel.setText("Sukses: " + message);
        statusLabel.setStyle("-fx-text-fill: green;");
    }

    private void showWarning(String message) {
        statusLabel.setText("Peringatan: " + message);
        statusLabel.setStyle("-fx-text-fill: orange;");
        // Bisa juga pakai Alert
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        statusLabel.setText("Error: " + message);
        statusLabel.setStyle("-fx-text-fill: red;");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Implementasi dari CabangDataObserver
    @Override
    public void onCabangDataChanged(Cabang cabang, String changeType) {
        Platform.runLater(() -> {
            System.out.println("Controller di-notifikasi: Cabang " + cabang.getNamaCabang() + " di-" + changeType);
            loadCabangData();
        });
    }
}