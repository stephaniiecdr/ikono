package racikanresep;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class racikanresep extends Application { // Ubah di sini
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("layout.fxml")); // Pastikan layout.fxml ada di lokasi yang
																				// benar
		primaryStage.setTitle("Data Racikan Resep");
		primaryStage.setScene(new Scene(root, 800, 600));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}