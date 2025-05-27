package application;

import application.decorator.LoggingCurrencyServiceDecorator;
import application.service.CurrencyService;
import application.service.HibernateCurrencyService;
import application.strategy.CurrencyDisplayStrategy;
import application.strategy.DetailedCurrencyDisplayStrategy;
import application.strategy.SimpleCurrencyDisplayStrategy;
import application.util.HibernateUtil; // Hanya untuk shutdown
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.List;

public class CurrencyMasterUI extends Application {

    private TextField curIDField;
    private TextField curNameField;
    private TextField curSymbolField;
    private TextField curCountryField;
    private TextField curExcRateField;
    private TextField decimalPlacesField;
    private RadioButton activeRadio;
    private RadioButton inactiveRadio;
    private ToggleGroup statusGroup;
    private ListView<Currency> currencyListView;
    private ObservableList<Currency> currencyObservableList;
    private ComboBox<CurrencyDisplayStrategy> displayStrategyComboBox;


    // Layanan untuk operasi data, dibungkus dengan Decorator Logging
    private CurrencyService currencyService;
    // Strategi tampilan default
    private CurrencyDisplayStrategy currentDisplayStrategy;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        // Inisialisasi service dan decorator
        // 1. Pola Kreasional (Singleton) digunakan secara implisit saat HibernateUtil memuat SessionFactory
        // 2. Pola Struktural (Decorator)
        CurrencyService baseService = new HibernateCurrencyService();
        this.currencyService = new LoggingCurrencyServiceDecorator(baseService); // Menggunakan Decorator

        // 3. Pola Behavioral (Strategy) - Inisialisasi strategi default
        this.currentDisplayStrategy = new DetailedCurrencyDisplayStrategy();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Master Data Mata Uang (Pola Desain)");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Master Data Mata Uang");
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        // Pilihan Strategi Tampilan
        Label strategyLabel = new Label("Format Tampilan:");
        displayStrategyComboBox = new ComboBox<>();
        displayStrategyComboBox.getItems().addAll(
                new DetailedCurrencyDisplayStrategy(),
                new SimpleCurrencyDisplayStrategy()
        );
        displayStrategyComboBox.setValue(currentDisplayStrategy); // Set default
        displayStrategyComboBox.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(CurrencyDisplayStrategy strategy) {
                if (strategy == null) return null;
                return strategy.getClass().getSimpleName().replace("CurrencyDisplayStrategy", "");
            }
            @Override
            public CurrencyDisplayStrategy fromString(String string) { return null; /* Tidak perlu */ }
        });
        displayStrategyComboBox.setOnAction(event -> {
            currentDisplayStrategy = displayStrategyComboBox.getValue();
            refreshListViewCellFactory(); // Update tampilan listview
            loadCurrencies(); // Muat ulang data dengan format baru (jika format mempengaruhi data yang ditampilkan)
        });
        HBox strategyBox = new HBox(10, strategyLabel, displayStrategyComboBox);


        GridPane formGrid = createFormGrid();

        currencyListView = new ListView<>();
        currencyObservableList = FXCollections.observableArrayList();
        currencyListView.setItems(currencyObservableList);
        currencyListView.setPrefHeight(250);
        refreshListViewCellFactory(); // Terapkan cell factory awal

        currencyListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });

        HBox buttonBox = createButtonBox();
        root.getChildren().addAll(titleLabel, strategyBox, formGrid, currencyListView, buttonBox);

        Scene scene = new Scene(root, 700, 750); // Tambah tinggi scene
        primaryStage.setScene(scene);
        primaryStage.show();

        loadCurrencies();
        primaryStage.setOnCloseRequest(event -> HibernateUtil.shutdown());
    }

    private void refreshListViewCellFactory() {
        currencyListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Currency item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || currentDisplayStrategy == null) {
                    setText(null);
                } else {
                    setText(currentDisplayStrategy.format(item)); // Menggunakan Strategy
                }
            }
        });
    }


    private GridPane createFormGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(10));
        curIDField = new TextField(); curIDField.setPromptText("e.g., USD");
        curNameField = new TextField(); curNameField.setPromptText("e.g., US Dollar");
        curSymbolField = new TextField(); curSymbolField.setPromptText("e.g., $");
        curCountryField = new TextField(); curCountryField.setPromptText("e.g., United States");
        curExcRateField = new TextField(); curExcRateField.setPromptText("e.g., 1.0000");
        decimalPlacesField = new TextField(); decimalPlacesField.setPromptText("e.g., 2");
        activeRadio = new RadioButton("Active"); activeRadio.setSelected(true);
        inactiveRadio = new RadioButton("Inactive");
        statusGroup = new ToggleGroup();
        activeRadio.setToggleGroup(statusGroup); inactiveRadio.setToggleGroup(statusGroup);
        HBox statusBox = new HBox(10, activeRadio, inactiveRadio);
        grid.add(new Label("Currency ID:"), 0, 0); grid.add(curIDField, 1, 0);
        grid.add(new Label("Name:"), 0, 1); grid.add(curNameField, 1, 1);
        grid.add(new Label("Symbol:"), 0, 2); grid.add(curSymbolField, 1, 2);
        grid.add(new Label("Country:"), 0, 3); grid.add(curCountryField, 1, 3);
        grid.add(new Label("Exchange Rate:"), 0, 4); grid.add(curExcRateField, 1, 4);
        grid.add(new Label("Decimal Places:"), 0, 5); grid.add(decimalPlacesField, 1, 5);
        grid.add(new Label("Status:"), 0, 6); grid.add(statusBox, 1, 6);
        return grid;
    }

    private HBox createButtonBox() {
        Button addButton = new Button("Add"); addButton.setOnAction(this::addCurrencyAction);
        Button updateButton = new Button("Update"); updateButton.setOnAction(this::updateCurrencyAction);
        Button deleteButton = new Button("Delete"); deleteButton.setOnAction(this::deleteCurrencyAction);
        Button clearButton = new Button("Clear Form"); clearButton.setOnAction(this::clearFormAction);
        return new HBox(10, addButton, updateButton, deleteButton, clearButton);
    }

    private void populateForm(Currency currency) {
        if (currency == null) { clearForm(); return; }
        curIDField.setText(currency.getCurID());
        curNameField.setText(currency.getCurName());
        curSymbolField.setText(currency.getCurSymbol());
        curCountryField.setText(currency.getCurCountry());
        curExcRateField.setText(currency.getCurExcRate() != null ? currency.getCurExcRate().toPlainString() : "");
        decimalPlacesField.setText(currency.getDecimalPlaces() != null ? currency.getDecimalPlaces().toString() : "");
        if ("active".equalsIgnoreCase(currency.getCurStatus())) activeRadio.setSelected(true);
        else inactiveRadio.setSelected(true);
        curIDField.setEditable(false);
    }
    
    private void clearFormAction(ActionEvent event) { clearForm(); }

    private void clearForm() {
        curIDField.clear(); curNameField.clear(); curSymbolField.clear();
        curCountryField.clear(); curExcRateField.clear(); decimalPlacesField.clear();
        activeRadio.setSelected(true); curIDField.setEditable(true);
        currencyListView.getSelectionModel().clearSelection();
    }

    private void loadCurrencies() {
        List<Currency> currencies = currencyService.getAllCurrencies(); // Menggunakan service
        currencyObservableList.setAll(currencies);
    }

    private void addCurrencyAction(ActionEvent event) {
        if (!validateInput(true)) return;
        Currency currency = new Currency();
        if (!setCurrencyFromFields(currency, true)) return;

        Currency existing = currencyService.findCurrencyById(currency.getCurID());
        if (existing != null) {
            showAlert(Alert.AlertType.ERROR, "Add Failed", "Currency ID " + currency.getCurID() + " already exists.");
            return;
        }

        if (currencyService.addCurrency(currency)) { // Menggunakan service
            loadCurrencies(); clearForm();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Currency added successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Add Failed", "Failed to add currency.");
        }
    }

    private void updateCurrencyAction(ActionEvent event) {
        if (currencyListView.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a currency to update."); return;
        }
        if (!validateInput(false)) return;

        // Ambil ID dari field karena mungkin saja ID tidak diedit tapi field lain diubah
        String currentId = curIDField.getText().trim().toUpperCase();
        Currency currencyToUpdate = currencyService.findCurrencyById(currentId); // Cari berdasarkan ID dari field

        if (currencyToUpdate == null) {
             showAlert(Alert.AlertType.ERROR, "Update Failed", "Currency with ID " + currentId + " not found for update.");
             return;
        }
        
        if (!setCurrencyFromFields(currencyToUpdate, false)) return; // Update field dari currency yang sudah ada

        if (currencyService.updateCurrency(currencyToUpdate)) { // Menggunakan service
            loadCurrencies(); clearForm();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Currency updated successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update currency.");
        }
    }
    
    private boolean setCurrencyFromFields(Currency currency, boolean isAdding) {
        if (isAdding) { // Hanya set ID jika operasi 'add' dan ID dapat diedit
            currency.setCurID(curIDField.getText().trim().toUpperCase());
        }
        // Untuk 'update', ID sudah ada di objek 'currency' yang diambil dari database
        // dan tidak seharusnya diubah dari field curIDField jika curIDField.isEditable() == false

        currency.setCurName(curNameField.getText().trim());
        currency.setCurSymbol(curSymbolField.getText().trim());
        currency.setCurCountry(curCountryField.getText().trim());
        try {
            String rateText = curExcRateField.getText().trim();
            currency.setCurExcRate(rateText.isEmpty() ? null : new BigDecimal(rateText));

            String decimalsText = decimalPlacesField.getText().trim();
            currency.setDecimalPlaces(decimalsText.isEmpty() ? null : Integer.parseInt(decimalsText));
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Exchange Rate must be a valid number. Decimal Places must be an integer.");
            return false;
        }
        currency.setCurStatus(activeRadio.isSelected() ? "active" : "inactive");
        return true;
    }

    private void deleteCurrencyAction(ActionEvent event) {
        Currency selected = currencyListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a currency to delete."); return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + selected.getCurID() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (currencyService.deleteCurrency(selected.getCurID())) { // Menggunakan service
                    loadCurrencies(); clearForm();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Currency deleted successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Delete Failed", "Failed to delete currency or currency not found.");
                }
            }
        });
    }

    private boolean validateInput(boolean isAddingNew) {
        String id = curIDField.getText().trim(); String name = curNameField.getText().trim();
        String rate = curExcRateField.getText().trim(); String decimals = decimalPlacesField.getText().trim();
        // Hanya Symbol dan Country yang boleh kosong
        if (id.isEmpty() || name.isEmpty() || rate.isEmpty() || decimals.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "ID, Name, Exchange Rate, and Decimal Places are required."); return false;
        }
        if (isAddingNew && id.length() > 3) { // SQL schema CurID VARCHAR(3)
             showAlert(Alert.AlertType.ERROR, "Validation Error", "Currency ID cannot be more than 3 characters."); return false;
        }
        try { new BigDecimal(rate); } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Exchange Rate must be a valid number."); return false;
        }
        try { Integer.parseInt(decimals); } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Decimal Places must be a valid integer."); return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type); alert.setTitle(title);
        alert.setHeaderText(null); alert.setContentText(message);
        alert.showAndWait();
    }
}
