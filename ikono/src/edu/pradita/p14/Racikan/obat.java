import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

// Class Obat untuk menampung data dari database
class obat {
	private int id_obat;
	private String nama_obat;
	private String deskripsi;

	public obat(int id_obat, String nama_obat, String deskripsi) {
		this.id_obat = id_obat;
		this.nama_obat = nama_obat;
		this.deskripsi = deskripsi;
	}

	public int getId_obat() {
		return id_obat;
	}

	public String getNama_obat() {
		return nama_obat;
	}

	public String getDeskripsi() {
		return deskripsi;
	}
}

public class obat extends e(fx)eclipse
{

    // List untuk menyimpan data obat dari database
	private ObservableList<Obat> dataObat = FXCollections.observableArrayList();

	// Koneksi database
	private void loadDataFromDatabase() {
		String jdbcUrl = "jdbc:mysql://localhost:3306/nama_database"; // Ganti nama_database sesuai database Anda
		String username = "root"; // Ganti jika username berbeda
		String password = "3631"; // Ganti dengan password database Anda

		try {
			Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
			String query = "SELECT id_obat, nama_obat, deskripsi FROM tabel_obat"; // Query ambil data
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			// Tambahkan data ke list
			while (resultSet.next()) {
				int id = resultSet.getInt("id_obat");
				String nama = resultSet.getString("nama_obat");
				String deskripsi = resultSet.getString("deskripsi");

				dataObat.add(new Obat(id, nama, deskripsi));
			}

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Data Obat");

		// Kolom ID Obat
		TableColumn<Obat, Integer> columnId = new TableColumn<>("ID Obat");
		columnId.setCellValueFactory(new PropertyValueFactory<>("id_obat"));

		// Kolom Nama Obat
		TableColumn<Obat, String> columnNama = new TableColumn<>("Nama Obat");
		columnNama.setCellValueFactory(new PropertyValueFactory<>("nama_obat"));

		// Kolom Deskripsi
		TableColumn<Obat, String> columnDeskripsi = new TableColumn<>("Deskripsi");
		columnDeskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));

		// TableView
		TableView<Obat> table = new TableView<>();
		table.getColumns().addAll(columnId, columnNama, columnDeskripsi);

		// Load data dari database
		loadDataFromDatabase();
		table.setItems(dataObat);

		// Layout
		VBox vbox = new VBox(table);
		Scene scene = new Scene(vbox, 600, 400);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
}
