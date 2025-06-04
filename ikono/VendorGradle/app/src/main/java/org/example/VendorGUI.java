package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import org.example.util.HibernateUtil;
import org.example.strategy.VendorOperationStrategy;
import org.example.strategy.AddVendorStrategy;
import org.example.strategy.UpdateVendorStrategy;
import org.example.strategy.DeleteVendorStrategy;


import java.util.List;

public class VendorGUI extends Application {

    private TableView<VendorDisplayAdapter> table; // Menggunakan VendorDisplayAdapter
    private ObservableList<VendorDisplayAdapter> vendorList; // Menggunakan VendorDisplayAdapter
    private TextField nameField, phoneField, addressField, emailField, contactField;
    private Button addButton, deleteButton, updateButton;

    // Ambil instance Singleton dari HibernateUtil
    private HibernateUtil hibernateUtil = HibernateUtil.getInstance(); //

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Vendor Management (Hibernate)");

        table = new TableView<>();
        vendorList = FXCollections.observableArrayList();

        // Kolom untuk TableView (nama properti harus sesuai dengan VendorDisplayAdapter)
        TableColumn<VendorDisplayAdapter, String> idColumn = new TableColumn<>("Vendor ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<VendorDisplayAdapter, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<VendorDisplayAdapter, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<VendorDisplayAdapter, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<VendorDisplayAdapter, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<VendorDisplayAdapter, String> contactColumn = new TableColumn<>("Contact Person");
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));

        TableColumn<VendorDisplayAdapter, String> createdAtColumn = new TableColumn<>("Created At");
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        TableColumn<VendorDisplayAdapter, String> updatedAtColumn = new TableColumn<>("Updated At");
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));


        table.getColumns().addAll(idColumn, nameColumn, phoneColumn, addressColumn, emailColumn, contactColumn, createdAtColumn, updatedAtColumn);
        table.setItems(vendorList);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameField.setText(newSelection.getName());
                phoneField.setText(newSelection.getPhone());
                addressField.setText(newSelection.getAddress());
                emailField.setText(newSelection.getEmail());
                contactField.setText(newSelection.getContactPerson());
            } else {
                clearFields();
            }
        });

        // Form input
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        nameField = new TextField();
        phoneField = new TextField();
        addressField = new TextField();
        emailField = new TextField();
        contactField = new TextField();

        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Phone:"), 0, 1);
        form.add(phoneField, 1, 1);
        form.add(new Label("Address:"), 0, 2);
        form.add(addressField, 1, 2);
        form.add(new Label("Email:"), 2, 0);
        form.add(emailField, 3, 0);
        form.add(new Label("Contact Person:"), 2, 1);
        form.add(contactField, 3, 1);

        // Tombol-tombol
        addButton = new Button("Add Vendor");
        deleteButton = new Button("Delete Vendor");
        updateButton = new Button("Update Vendor");

        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton);
        form.add(buttonBox, 2, 3, 2, 1);

        VBox layout = new VBox(10, table, form);
        layout.setPadding(new Insets(10));

        loadData();

        // Menghubungkan tombol dengan metode yang menggunakan Strategy Pattern
        addButton.setOnAction(e -> executeVendorOperation(new AddVendorStrategy(), null)); //
        updateButton.setOnAction(e -> executeVendorOperation(new UpdateVendorStrategy(), table.getSelectionModel().getSelectedItem())); //
        deleteButton.setOnAction(e -> executeVendorOperation(new DeleteVendorStrategy(), table.getSelectionModel().getSelectedItem())); //

        Scene scene = new Scene(layout, 1100, 700);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Pastikan SessionFactory ditutup saat aplikasi ditutup
        primaryStage.setOnCloseRequest(event -> hibernateUtil.shutdown()); //
    }

    // Metode untuk memuat data dari database
    private void loadData() {
        Session session = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession(); //
            Query<Vendor> query = session.createQuery("from Vendor", Vendor.class);
            List<Vendor> vendors = query.list();

            vendorList.clear();
            for (Vendor vendor : vendors) {
                vendorList.add(new VendorDisplayAdapter(vendor)); // Menggunakan Adapter
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to load data: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // Metode utama untuk mengeksekusi operasi Vendor menggunakan Strategy Pattern
    private void executeVendorOperation(VendorOperationStrategy strategy, VendorDisplayAdapter selectedVendorDisplay) { //
        Session session = null;
        Transaction transaction = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession(); //
            transaction = session.beginTransaction();

            Vendor vendor = null;
            if (strategy instanceof AddVendorStrategy) { //
                vendor = new Vendor(
                        nameField.getText(),
                        contactField.getText(),
                        emailField.getText(),
                        phoneField.getText(),
                        addressField.getText()
                );
            } else if (selectedVendorDisplay != null) {
                // Untuk Update dan Delete, kita perlu mendapatkan Vendor asli dari DB
                vendor = session.get(Vendor.class, Integer.parseInt(selectedVendorDisplay.getId()));
                if (vendor == null) {
                    showAlert(Alert.AlertType.ERROR, "Vendor not found in database.");
                    if (transaction != null) transaction.rollback();
                    return;
                }
                if (strategy instanceof UpdateVendorStrategy) { //
                    // Perbarui properti objek Vendor dengan nilai dari form
                    vendor.setName(nameField.getText());
                    vendor.setPhone(phoneField.getText());
                    vendor.setAddress(addressField.getText());
                    vendor.setEmail(emailField.getText());
                    vendor.setContactPerson(contactField.getText());
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Please select a vendor for this operation.");
                if (transaction != null) transaction.rollback();
                return;
            }

            if (vendor != null) {
                strategy.execute(session, vendor); // Eksekusi strategi
                transaction.commit();
                loadData();
                showAlert(Alert.AlertType.INFORMATION, "Operation completed successfully.");
                clearFields();
            } else {
                 showAlert(Alert.AlertType.ERROR, "Vendor object is null for operation.");
                 if (transaction != null) transaction.rollback();
            }

        } catch (NumberFormatException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Invalid Vendor ID selected. Please ensure the ID is a valid number.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to complete operation: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    private void clearFields() {
        nameField.clear();
        phoneField.clear();
        addressField.clear();
        emailField.clear();
        contactField.clear();
        table.getSelectionModel().clearSelection();
    }

    private static void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
