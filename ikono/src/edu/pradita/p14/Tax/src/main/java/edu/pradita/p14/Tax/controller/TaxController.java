package edu.pradita.p14.Tax.controller; // IMPORTANT: Matches your actual package structure

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import edu.pradita.p14.Tax.tax.Tax; // IMPORTANT: Correct import for your Tax entity location
import edu.pradita.p14.Tax.service.TaxService; // IMPORTANT: Correct import for your TaxService location

// Import kelas-kelas Decorator yang diperlukan
// import org.edu.pradita.pos.tax.decorator.AdditionalFeeDecorator; // TIDAK DIGUNAKAN
// import org.edu.pradita.pos.tax.decorator.DiscountDecorator; // TIDAK DIGUNAKAN

import java.util.List;

/**
 * Controller for TaxView.fxml.
 * Responsible for handling user interactions and managing the display of tax data.
 * Adheres to Single Responsibility Principle (SRP) by focusing on UI logic.
 * Integrates the Strategy pattern for dynamic tax calculation selection.
 * Integrates the Decorator pattern for dynamic amount processing (e.g., applying tax).
 */
public class TaxController {

    // FXML elements for Tax Data Management section
    @FXML private TextField taxNameField;
    @FXML private TextField taxRateField;
    @FXML private TableView<Tax> taxTableView;
    @FXML private Label statusLabel;

    // FXML elements for Calculate Tax section
//    @FXML private TextField baseAmountField;
//    @FXML private ComboBox<String> strategyComboBox; // ComboBox to select strategy by name
//    @FXML private Label calculationResultLabel;

    private TaxService taxService; // Service layer dependency
    private ObservableList<Tax> taxData; // Observable list for TableView data
//    private Map<String, TaxCalculationStrategy> strategies; // Map to hold strategy instances by name

    /**
     * Initialization method called automatically by FXMLLoader after FXML elements are loaded.
     */
    @FXML
    public void initialize() {
        // Initialize service and data list
        taxService = new TaxService();
        taxData = FXCollections.observableArrayList();
        taxTableView.setItems(taxData); // Bind ObservableList to TableView

//        // Initialize and populate the map of strategies
//        strategies = new HashMap<>();
//        strategies.put("VAT Strategy", new VATTaxStrategy());
//        strategies.put("Sales Tax Strategy", new SalesTaxStrategy());
//        // Add more strategies here as you create them (e.g., "Luxury Tax Strategy")
//
//        // Populate the ComboBox with the names of available strategies
//        strategyComboBox.setItems(FXCollections.observableArrayList(strategies.keySet()));
//        strategyComboBox.getSelectionModel().selectFirst(); // Select the first strategy by default

        // Set listener for row selection in the Tax TableView
        // When a row is selected, its details will be shown in the form fields
        taxTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTaxDetails(newValue));

        // Load existing tax data into the table when the controller initializes
        loadTaxes();
    }

    /**
     * Loads all tax data from the TaxService and updates the taxTableView.
     */
    private void loadTaxes() {
        taxData.clear(); // Clear existing data in the table
        List<Tax> taxes = taxService.getAllTaxes(); // Fetch all taxes from the service
        if (taxes != null && !taxes.isEmpty()) {
            taxData.addAll(taxes); // Add fetched taxes to the observable list
            statusLabel.setText("Tax data loaded successfully. Total: " + taxes.size() + " records.");
        } else {
            statusLabel.setText("No tax data found or failed to load.");
        }
    }

    /**
     * Displays details of the selected tax in the top form fields.
     * @param tax The selected Tax object from the table.
     */
    private void showTaxDetails(Tax tax) {
        if (tax != null) {
            taxNameField.setText(tax.getTaxName());
            taxRateField.setText(String.valueOf(tax.getTaxRate()));
        } else {
            handleClearForm(); // Clear the form if no tax is selected (e.g., on deselect)
        }
    }

    /**
     * Handles the "Add Tax" button action.
     * Creates a new Tax record in the database.
     */
    @FXML
    private void handleAddTax() {
        try {
            String name = taxNameField.getText();
            double rate = Double.parseDouble(taxRateField.getText());

            if (name.isEmpty() || rate < 0) {
                showAlert(AlertType.ERROR, "Input Error", "Tax name cannot be empty and rate must be non-negative.");
                return;
            }

            Tax newTax = new Tax(name, rate); // Create a new Tax object
            taxService.addTax(newTax); // Add tax via the service layer
            loadTaxes(); // Reload data into the table to show the new entry
            handleClearForm(); // Clear form fields after successful add
            statusLabel.setText("Tax '" + name + "' added successfully.");
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Input Error", "Tax rate must be a valid number (e.g., 0.10).");
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Failed to add tax: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Update Tax" button action.
     * Updates an existing Tax record in the database.
     */
    @FXML
    private void handleUpdateTax() {
        Tax selectedTax = taxTableView.getSelectionModel().getSelectedItem();
        if (selectedTax != null) {
            try {
                String name = taxNameField.getText();
                double rate = Double.parseDouble(taxRateField.getText());

                if (name.isEmpty() || rate < 0) {
                    showAlert(AlertType.ERROR, "Input Error", "Tax name cannot be empty and rate must be non-negative.");
                    return;
                }

                selectedTax.setTaxName(name); // Update name of the selected Tax object
                selectedTax.setTaxRate(rate); // Update rate of the selected Tax object
                taxService.updateTax(selectedTax); // Update tax via the service layer
                loadTaxes(); // Reload data into the table to reflect changes
                handleClearForm(); // Clear form fields
                statusLabel.setText("Tax ID " + selectedTax.getTaxID() + " updated successfully.");
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Input Error", "Tax rate must be a valid number (e.g., 0.10).");
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Error", "Failed to update tax: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert(AlertType.WARNING, "No Selection", "Please select a tax from the table to update.");
        }
    }

    /**
     * Handles the "Delete Tax" button action.
     * Deletes a selected Tax record from the database.
     */
    @FXML
    private void handleDeleteTax() {
        Tax selectedTax = taxTableView.getSelectionModel().getSelectedItem();
        if (selectedTax != null) {
            try {
                // Confirm deletion with the user (optional, but good practice)
                Alert confirmationAlert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete tax: " + selectedTax.getTaxName() + "?", javafx.scene.control.ButtonType.YES, javafx.scene.control.ButtonType.NO);
                confirmationAlert.setTitle("Confirm Deletion");
                confirmationAlert.setHeaderText(null);
                confirmationAlert.showAndWait();

                if (confirmationAlert.getResult() == javafx.scene.control.ButtonType.YES) {
                    taxService.deleteTax(selectedTax.getTaxID()); // Delete tax via the service layer
                    loadTaxes(); // Reload data into the table
                    handleClearForm(); // Clear form fields
                    statusLabel.setText("Tax ID " + selectedTax.getTaxID() + " deleted successfully.");
                } else {
                    statusLabel.setText("Deletion cancelled.");
                }
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Error", "Failed to delete tax: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert(AlertType.WARNING, "No Selection", "Please select a tax from the table to delete.");
        }
    }

    /**
     * Handles the "Clear Form" button action.
     * Clears all input fields and table selection.
     */
    @FXML
    private void handleClearForm() {
        taxNameField.clear();
        taxRateField.clear();
        taxTableView.getSelectionModel().clearSelection();
        statusLabel.setText("");
//        calculationResultLabel.setText(""); // Clear calculation result label
//        baseAmountField.clear(); // Clear base amount field
//        strategyComboBox.getSelectionModel().selectFirst(); // Reset strategy selection
    }


//    @FXML
//    private void handleCalculateTax() {
//        try {
//            double baseAmount = Double.parseDouble(baseAmountField.getText());
//            String selectedStrategyName = strategyComboBox.getSelectionModel().getSelectedItem();
//
//            if (baseAmount < 0) {
//                showAlert(AlertType.ERROR, "Input Error", "Base amount cannot be negative.");
//                return;
//            }
//            if (selectedStrategyName == null || selectedStrategyName.isEmpty()) {
//                showAlert(AlertType.ERROR, "Selection Error", "Please select a tax strategy.");
//                return;
//            }
//
//            Tax selectedTax = taxTableView.getSelectionModel().getSelectedItem();
//            if (selectedTax == null) {
//                showAlert(AlertType.WARNING, "No Tax Selected", "Please select a tax from the table to use its rate for calculation.");
//                return;
//            }
//
//            TaxCalculationStrategy selectedStrategy = strategies.get(selectedStrategyName);
//            if (selectedStrategy == null) {
//                showAlert(AlertType.ERROR, "Strategy Error", "Selected strategy not found. This should not happen.");
//                return;
//            }
//
//            taxService.setTaxCalculationStrategy(selectedStrategy); // Set the chosen strategy in TaxService
//
//            // --- Implementasi Decorator Pattern di sini (hanya TaxDecorator) ---
//            CalculableAmountProcessor processor = new BasicAmountprocessor(); // Mulai dengan pemroses dasar
//
//            // Terapkan pajak menggunakan TaxDecorator
//            // Ini akan memanggil TaxService yang menggunakan strategi yang sudah disetel
//            processor = new TaxDecorator(processor, taxService);
//
//            // Lakukan perhitungan akhir menggunakan rantai decorator
//            double finalAmount = processor.processAmount(baseAmount, selectedTax); // Ini akan mengembalikan jumlah akhir (dasar + pajak)
//
//            // Tampilkan hasil
//            calculationResultLabel.setText(String.format("Final Amount (Decorated): %.2f", finalAmount));
//
//        } catch (NumberFormatException e) {
//            showAlert(AlertType.ERROR, "Input Error", "Base amount must be a valid number.");
//        } catch (Exception e) {
//            showAlert(AlertType.ERROR, "Error", "Failed to perform calculation: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    /**
     * Displays an Alert dialog to the user.
     * @param type Type of Alert (ERROR, WARNING, INFORMATION).
     * @param title Alert title.
     * @param message Alert message content.
     */
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
