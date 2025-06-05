// File: src/main/java/com/mutasistok/MainApplication.java
package com.mutasistok;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Pastikan path ke FXML benar
        URL fxmlLocation = MainApplication.class.getResource("main-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Aplikasi Mutasi Stok");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}