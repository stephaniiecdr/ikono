package racikanresep;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicationDAO {

	public List<obat> getAllobat() {
	        List<obat> obatList = new ArrayList<>();
	        String query = "SELECT * FROM tabel_obat";

	        try (Connection connection = DatabaseConnection.getConnection();
	             Statement statement = connection.createStatement();
	             ResultSet resultSet = statement.executeQuery(query)) {

	            while (resultSet.next()) {
	                int id = resultSet.getInt("id_obat");
	                String nama = resultSet.getString("nama_obat");
	                String deskripsi = resultSet.getString("deskripsi");
	                obatList.add(new obat(id, nama, deskripsi));
}
	        }
	    }
