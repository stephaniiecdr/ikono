package _UASS2;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class MainAppUASS extends Application {

//    private static final String DB_URL = "jdbc:mariadb://localhost:3306/pradita";
//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "";
//
//    private Connection connectToDatabase() {
//        try {
//            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//        } catch (SQLException e) {
//        	Alert alert = new Alert(AlertType.ERROR);
//        	alert.setTitle("Error: ");
//        	alert.setHeaderText("Koneksi SQL gagal.");
//        	alert.setContentText("Tolong ubah DB_URL, DB_USER, dan DB_PASSWORD di class PelunasanPiutangApp dan pastikan bahwa koneksi tersebut aktif.");
//        	alert.setHeight(230);;
//        	e.printStackTrace();
//        	alert.showAndWait();
//            System.exit(0);
//            return null;
//        }
//    }
	
	private PembeliDAO pembeliDAO = new PembeliDAO();
	private PiutangDAO piutangDAO= new PiutangDAO();
	private PelunasanDAO pelunasanDAO = new PelunasanDAO();

    @Override
    public void start(Stage primaryStage) {
	    	primaryStage.setTitle("Aplikasi Pelunasan Piutang");
	
	        VBox root = new VBox(10);
	        root.setPadding(new Insets(15));
	
	        Label menuLabel = new Label("Menu:");
	        ListView<String> menuList = new ListView<>();
	        menuList.setItems(FXCollections.observableArrayList(
	            "Tambah Piutang",
	            "Tambah Pelunasan",
	            "Lihat Pelanggan",
	            "Lihat Piutang",
	            "Lihat Pelunasan",
	            "Hapus Piutang",
	            "Keluar"
	        ));
	
	        Button executeButton = new Button("Pilih");
	        executeButton.setOnAction(e -> {
				try {
					handleMenuSelection(menuList.getSelectionModel().getSelectedIndex() + 1);
				} catch (SQLException e1) {
		        	e1.printStackTrace();
				}
			});
	
	        root.getChildren().addAll(menuLabel, menuList, executeButton);
	
	        primaryStage.setScene(new Scene(root, 400, 265));
	        primaryStage.show();
        
    }

    private void handleMenuSelection(int option) throws SQLException {
        switch (option) {
            case 1:
                showTambahPiutangGUI();
                break;
            case 2:
                showTambahPelunasanGUI();
                break;
            case 3:
                showLihatSemuaPelangganGUI();
                break;
            case 4:
                showLihatSemuaPiutangGUI();
                break;
            case 5:
                showLihatSemuaPelunasanGUI();
                break;
            case 6:
                showHapusDataGUI();
                break;
            case 7:
                System.exit(0);
                break;
            default:
                Alert alert = new Alert(Alert.AlertType.WARNING, "Pilihan tidak valid.");
                alert.show();
        }
    }

    private void showTambahPiutangGUI() {
        Stage stage = new Stage();
        stage.setTitle("Tambah Piutang");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField idPelangganField = new TextField();
        idPelangganField.setPromptText("ID Pelanggan");

        TextField jumlahField = new TextField();
        jumlahField.setPromptText("Jumlah Piutang");

        TextField dueField = new TextField();
        dueField.setPromptText("Due Date (YYYY-MM-DD)");

        Button submitButton = new Button("Simpan");
        submitButton.setOnAction(e -> {
            try {
                Integer idPelanggan = Integer.parseInt(idPelangganField.getText());
                BigDecimal jumlah = new BigDecimal(jumlahField.getText());
                LocalDate dueDate = LocalDate.parse(dueField.getText());

                Pembeli pembeli = pembeliDAO.findById(idPelanggan);

                if (pembeli != null) {
                    Piutang newPiutang = new Piutang();
                    newPiutang.setPembeli(pembeli); // Link to the Pembeli object
                    newPiutang.setJumlah(jumlah);
                    newPiutang.setTanggalPinjam(LocalDate.now()); // Set current date as tanggal_pinjam
                    newPiutang.setDueDate(dueDate);
                    newPiutang.setStatusLunas(false); // Initially not paid

                    piutangDAO.save(newPiutang);

                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Sukses");
                    alert.setHeaderText(null);
                    alert.setContentText("Piutang berhasil ditambahkan!");
                    alert.showAndWait();
                    stage.close();
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Ooops, sepertinya ada error!");
                    alert.setContentText("ID pelanggan tidak ditemukan. Pastikan ID pelanggan dimasukkan dengan benar.");
                    alert.showAndWait();
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Input tidak valid!");
                alert.setContentText("Pastikan ID Pelanggan dan Jumlah Piutang adalah angka, dan Due Date dalam format YYYY-MM-DD.");
                ex.printStackTrace();
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Ooops, sepertinya ada error!");
                alert.setContentText("Terjadi kesalahan saat menyimpan piutang.");
                ex.printStackTrace();
                alert.showAndWait();
            }
        });

        root.getChildren().addAll(new Label("Tambah Piutang"), idPelangganField, jumlahField, dueField, submitButton);

        stage.setScene(new Scene(root, 300, 200));
        stage.show();
    }

    private void showTambahPelunasanGUI() {
        Stage stage = new Stage();
        stage.setTitle("Tambah Pelunasan");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField idPiutangField = new TextField();
        idPiutangField.setPromptText("ID Piutang");

        TextField jumlahBayarField = new TextField();
        jumlahBayarField.setPromptText("Jumlah Bayar");

        Button submitButton = new Button("Simpan");
        submitButton.setOnAction(e -> {
            try {
                Integer idPiutang = Integer.parseInt(idPiutangField.getText());
                BigDecimal jumlahBayar = new BigDecimal(jumlahBayarField.getText());

                // Fetch the Piutang object using PiutangDAO
                Piutang piutang = piutangDAO.findById(idPiutang);

                if (piutang != null) {
                    // Create a new Pelunasan object
                    Pelunasan newPelunasan = new Pelunasan();
                    newPelunasan.setPiutang(piutang); // Link to the Piutang object
                    newPelunasan.setJumlahBayar(jumlahBayar);
                    newPelunasan.setTanggalBayar(LocalDate.now()); // Set current date as tanggal_bayar

                    // Save the Pelunasan using PelunasanDAO
                    pelunasanDAO.save(newPelunasan);

                    // Update the remaining amount of the Piutang
                    piutangDAO.reduceAmount(idPiutang, jumlahBayar);

                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Sukses");
                    alert.setHeaderText(null);
                    alert.setContentText("Pelunasan berhasil ditambahkan dan piutang diperbarui!");
                    alert.showAndWait();
                    stage.close();
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Ooops, sepertinya ada error!");
                    alert.setContentText("ID piutang tidak ditemukan. Pastikan ID piutang dimasukkan dengan benar.");
                    alert.showAndWait();
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Input tidak valid!");
                alert.setContentText("Pastikan ID Piutang adalah angka, dan Jumlah Bayar adalah angka.");
                ex.printStackTrace();
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Ooops, sepertinya ada error!");
                alert.setContentText("Terjadi kesalahan saat menyimpan pelunasan atau memperbarui piutang.");
                ex.printStackTrace();
                alert.showAndWait();
            }
        });

        root.getChildren().addAll(new Label("Tambah Pelunasan"), idPiutangField, jumlahBayarField, submitButton);

        stage.setScene(new Scene(root, 300, 200));
        stage.show();
    }

    private void showLihatSemuaPelangganGUI() {
        Stage stage = new Stage();
        stage.setTitle("Lihat Semua Pelanggan");

        TableView<Pembeli> table = new TableView<>();
        TableColumn<Pembeli, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Pembeli, String> namaColumn = new TableColumn<>("Nama Lengkap");
        TableColumn<Pembeli, String> alamatColumn = new TableColumn<>("Alamat");
        TableColumn<Pembeli, String> kotaColumn = new TableColumn<>("Kota");
        TableColumn<Pembeli, String> kodePosColumn = new TableColumn<>("Kode Pos");
        TableColumn<Pembeli, String> noTelpColumn = new TableColumn<>("Nomor Telepon");
        TableColumn<Pembeli, String> emailColumn = new TableColumn<>("Email");
        TableColumn<Pembeli, String> jenisKelaminColumn = new TableColumn<>("Jenis Kelamin");
        TableColumn<Pembeli, String> tanggalDaftarColumn = new TableColumn<>("Tanggal Daftar");
        TableColumn<Pembeli, String> statusColumn = new TableColumn<>("Status");

        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdPembeli()).asObject());
        namaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamaLengkap()));
        alamatColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlamat()));
        kotaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKota()));
        kodePosColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKodePos()));
        noTelpColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNoTelepon()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        jenisKelaminColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJenisKelamin()));
        tanggalDaftarColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTanggalDaftar().toString()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        table.getColumns().addAll(idColumn, namaColumn, alamatColumn, kotaColumn, kodePosColumn, noTelpColumn, emailColumn, jenisKelaminColumn, tanggalDaftarColumn, statusColumn);

        ObservableList<Pembeli> pembeliList = FXCollections.observableArrayList();
        try {
            pembeliList.addAll(pembeliDAO.findAll()); // Use PembeliDAO to get all Pembeli
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Ooops, sepertinya ada error!");
            alert.setContentText("Terjadi kesalahan saat memuat data pelanggan.");
            alert.showAndWait();
        }

        table.setItems(pembeliList);

        VBox root = new VBox(10, table);
        root.setPadding(new Insets(10));

        TextField cariField = new TextField();
        cariField.setPromptText("Masukkan Nama Pelanggan");

        Button cariButton = new Button("Cari");
        cariButton.setOnAction(e -> {
            Stage resultStage = new Stage();
            resultStage.setTitle("Hasil Pencarian Pelanggan");

            TableView<Pembeli> resultTable = new TableView<>();
            TableColumn<Pembeli, Integer> resultIdColumn = new TableColumn<>("ID");
            TableColumn<Pembeli, String> resultNamaColumn = new TableColumn<>("Nama Lengkap");
            TableColumn<Pembeli, String> resultAlamatColumn = new TableColumn<>("Alamat");
            TableColumn<Pembeli, String> resultKotaColumn = new TableColumn<>("Kota");
            TableColumn<Pembeli, String> resultKodePosColumn = new TableColumn<>("Kode Pos");
            TableColumn<Pembeli, String> resultNoTelpColumn = new TableColumn<>("Nomor Telepon");
            TableColumn<Pembeli, String> resultEmailColumn = new TableColumn<>("Email");
            TableColumn<Pembeli, String> resultJenisKelaminColumn = new TableColumn<>("Jenis Kelamin");
            TableColumn<Pembeli, String> resultTanggalDaftarColumn = new TableColumn<>("Tanggal Daftar");
            TableColumn<Pembeli, String> resultStatusColumn = new TableColumn<>("Status");

            resultIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdPembeli()).asObject());
            resultNamaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamaLengkap()));
            resultAlamatColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlamat()));
            resultKotaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKota()));
            resultKodePosColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKodePos()));
            resultNoTelpColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNoTelepon()));
            resultEmailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
            resultJenisKelaminColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJenisKelamin()));
            resultTanggalDaftarColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTanggalDaftar().toString()));
            resultStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

            resultTable.getColumns().addAll(resultIdColumn, resultNamaColumn, resultAlamatColumn, resultKotaColumn, resultKodePosColumn, resultNoTelpColumn, resultEmailColumn, resultJenisKelaminColumn, resultTanggalDaftarColumn, resultStatusColumn);

            ObservableList<Pembeli> resultPembeliList = FXCollections.observableArrayList();
            try {
                resultPembeliList.addAll(pembeliDAO.findByNamaLengkap(cariField.getText())); // Use findByNamaLengkap from PembeliDAO
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Ooops, sepertinya ada error!");
                alert.setContentText("Terjadi kesalahan saat mencari data pelanggan.");
                alert.showAndWait();
            }

            resultTable.setItems(resultPembeliList);

            VBox resultRoot = new VBox(10, resultTable);
            resultRoot.setPadding(new Insets(10));

            resultStage.setScene(new Scene(resultRoot, 600, 400));
            resultStage.show();
        });

        root.getChildren().addAll(new Label("Cari Pelanggan"), cariField, cariButton);

        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }


    private void showLihatSemuaPiutangGUI() {
        Stage stage = new Stage();
        stage.setTitle("Lihat Semua Piutang");

        TableView<Piutang> table = new TableView<>();
        TableColumn<Piutang, Integer> idPColumn = new TableColumn<>("ID");
        TableColumn<Piutang, String> namaColumn = new TableColumn<>("Nama Pelanggan");
        TableColumn<Piutang, Double> jumlahColumn = new TableColumn<>("Jumlah");
        TableColumn<Piutang, String> tanggalPinjamColumn = new TableColumn<>("Tanggal Pinjam");
        TableColumn<Piutang, String> dueDateColumn = new TableColumn<>("Tenggat Waktu");
        TableColumn<Piutang, String> lunasColumn = new TableColumn<>("Status Lunas");

        idPColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdPiutang()).asObject());
        namaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPembeli().getNamaLengkap())); // Access namaLengkap from Pembeli object
        jumlahColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getJumlah().doubleValue()).asObject()); // Convert BigDecimal to double
        tanggalPinjamColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTanggalPinjam().toString())); // Convert LocalDate to String
        dueDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDueDate().toString())); // Convert LocalDate to String
        lunasColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusLunas() ? "Lunas" : "Belum Lunas"));

        table.getColumns().addAll(idPColumn, namaColumn, jumlahColumn, tanggalPinjamColumn, dueDateColumn, lunasColumn);

        ObservableList<Piutang> piutangList = FXCollections.observableArrayList();
        try {
            piutangList.addAll(piutangDAO.findAll()); // Use PiutangDAO to get all Piutang
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Ooops, sepertinya ada error!");
            alert.setContentText("Terjadi kesalahan saat memuat data piutang.");
            alert.showAndWait();
        }

        table.setItems(piutangList);

        VBox root = new VBox(10, table);
        root.setPadding(new Insets(10));

        TextField cariField = new TextField();
        cariField.setPromptText("Masukkan Nama Pelanggan");

        Button cariButton = new Button("Cari");
        cariButton.setOnAction(e -> {
            Stage resultStage = new Stage();
            resultStage.setTitle("Hasil Pencarian Piutang");

            TableView<Piutang> resultTable = new TableView<>();
            TableColumn<Piutang, Integer> resultIdPColumn = new TableColumn<>("ID Piutang");
            TableColumn<Piutang, String> resultNamaColumn = new TableColumn<>("Nama Pelanggan");
            TableColumn<Piutang, Double> resultJumlahColumn = new TableColumn<>("Jumlah");
            TableColumn<Piutang, String> resultTanggalPinjamColumn = new TableColumn<>("Tanggal Pinjam");
            TableColumn<Piutang, String> resultDueDateColumn = new TableColumn<>("Tenggat Waktu");
            TableColumn<Piutang, String> resultLunasColumn = new TableColumn<>("Status Lunas");

            resultIdPColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdPiutang()).asObject());
            resultNamaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPembeli().getNamaLengkap()));
            resultJumlahColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getJumlah().doubleValue()).asObject());
            resultTanggalPinjamColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTanggalPinjam().toString()));
            resultDueDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDueDate().toString()));
            resultLunasColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusLunas() ? "Lunas" : "Belum Lunas"));

            resultTable.getColumns().addAll(resultIdPColumn, resultNamaColumn, resultJumlahColumn, resultTanggalPinjamColumn, resultDueDateColumn, resultLunasColumn);

            ObservableList<Piutang> resultPiutangList = FXCollections.observableArrayList();
            try {
                resultPiutangList.addAll(piutangDAO.findByNamaPelanggan(cariField.getText())); // Use findByNamaPelanggan from PiutangDAO
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Ooops, sepertinya ada error!");
                alert.setContentText("Terjadi kesalahan saat mencari data piutang.");
                alert.showAndWait();
            }

            resultTable.setItems(resultPiutangList);

            VBox resultRoot = new VBox(10, resultTable);
            resultRoot.setPadding(new Insets(10));

            resultStage.setScene(new Scene(resultRoot, 600, 400));
            resultStage.show();
        });

        root.getChildren().addAll(new Label("Cari Piutang"), cariField, cariButton);

        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    
    private void showLihatSemuaPelunasanGUI() {
        Stage stage = new Stage();
        stage.setTitle("Lihat Semua Pelunasan");

        TableView<Pelunasan> table = new TableView<>();
        TableColumn<Pelunasan, Integer> idPelunasanColumn = new TableColumn<>("ID");
        TableColumn<Pelunasan, String> namaLengkapColumn = new TableColumn<>("Nama Lengkap");
        TableColumn<Pelunasan, Double> jumlahBayarColumn = new TableColumn<>("Jumlah Bayar");
        TableColumn<Pelunasan, String> tanggalBayarColumn = new TableColumn<>("Tanggal Bayar");

        idPelunasanColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdPelunasan()).asObject());
        // Access namaLengkap through piutang and then pembeli
        namaLengkapColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPiutang().getPembeli().getNamaLengkap()));
        jumlahBayarColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getJumlahBayar().doubleValue()).asObject());
        tanggalBayarColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTanggalBayar().toString()));

        table.getColumns().addAll(idPelunasanColumn, namaLengkapColumn, jumlahBayarColumn, tanggalBayarColumn);

        ObservableList<Pelunasan> pelunasanList = FXCollections.observableArrayList();
        try {
            pelunasanList.addAll(pelunasanDAO.findAll()); // Use PelunasanDAO to get all Pelunasan
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Ooops, sepertinya ada error!");
            alert.setContentText("Terjadi kesalahan saat memuat data pelunasan.");
            alert.showAndWait();
        }

        table.setItems(pelunasanList);

        VBox root = new VBox(10, table);
        root.setPadding(new Insets(10));

        TextField cariField = new TextField();
        cariField.setPromptText("Masukkan Nama Pelanggan");

        Button cariButton = new Button("Cari");
        cariButton.setOnAction(e -> {
            Stage resultStage = new Stage();
            resultStage.setTitle("Hasil Pencarian Pelunasan");

            TableView<Pelunasan> resultTable = new TableView<>();
            TableColumn<Pelunasan, Integer> resultIdPelunasanColumn = new TableColumn<>("ID");
            TableColumn<Pelunasan, String> resultNamaLengkapColumn = new TableColumn<>("Nama Lengkap");
            TableColumn<Pelunasan, Double> resultJumlahBayarColumn = new TableColumn<>("Jumlah Bayar");
            TableColumn<Pelunasan, String> resultTanggalBayarColumn = new TableColumn<>("Tanggal Bayar");

            resultIdPelunasanColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdPelunasan()).asObject());
            resultNamaLengkapColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPiutang().getPembeli().getNamaLengkap()));
            resultJumlahBayarColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getJumlahBayar().doubleValue()).asObject());
            resultTanggalBayarColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTanggalBayar().toString()));

            resultTable.getColumns().addAll(resultIdPelunasanColumn, resultNamaLengkapColumn, resultJumlahBayarColumn, resultTanggalBayarColumn);

            ObservableList<Pelunasan> searchPelunasanList = FXCollections.observableArrayList();
            try {
                searchPelunasanList.addAll(pelunasanDAO.findByNamaPelanggan(cariField.getText())); // Use findByNamaPelanggan from PelunasanDAO
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Ooops, sepertinya ada error!");
                alert.setContentText("Terjadi kesalahan saat mencari data pelunasan.");
                alert.showAndWait();
            }

            resultTable.setItems(searchPelunasanList);

            VBox resultRoot = new VBox(10, resultTable);
            resultRoot.setPadding(new Insets(10));

            resultStage.setScene(new Scene(resultRoot, 600, 400));
            resultStage.show();
        });

        root.getChildren().addAll(new Label("Cari Pelunasan"), cariField, cariButton);

        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
    
    private void showHapusDataGUI() {
        Stage stage = new Stage();
        stage.setTitle("Hapus Data Piutang");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField idPiutangField = new TextField();
        idPiutangField.setPromptText("Masukkan ID Piutang yang Akan Dihapus");

        Button hapusButton = new Button("Hapus");
        hapusButton.setOnAction(e -> {
            try {
                Integer id = Integer.parseInt(idPiutangField.getText());

                // First, delete related Pelunasan records to avoid foreign key constraints
                pelunasanDAO.deleteByPiutangId(id); // Use the custom method in PelunasanDAO

                // Then, delete the Piutang record
                piutangDAO.deleteById(id);

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Sukses");
                alert.setHeaderText(null);
                alert.setContentText("Data piutang dan pelunasannya berhasil dihapus!");
                alert.showAndWait();
                stage.close();

            } catch (NumberFormatException ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Input tidak valid!");
                alert.setContentText("Pastikan ID Piutang adalah angka.");
                ex.printStackTrace();
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Ooops, sepertinya ada error!");
                alert.setContentText("Terjadi kesalahan saat menghapus data. Pastikan ID piutang dimasukkan dengan benar dan tidak ada kendala koneksi.");
                ex.printStackTrace();
                alert.showAndWait();
            }
        });

        root.getChildren().addAll(new Label("Masukkan ID Piutang"), idPiutangField, hapusButton);

        stage.setScene(new Scene(root, 300, 150));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}