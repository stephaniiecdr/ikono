package org.edu.pradita.main.view;

import org.edu.pradita.main.model.dto.SalesReportItemDTO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.YearMonth;

public class PenjualanBulananView {
    private DatePicker monthPicker;
    private TableView<SalesReportItemDTO> reportTable;
    private ObservableList<SalesReportItemDTO> salesReportsData;
    private Label totalSalesLabel;
    private Button loadButton;
    private Scene scene;

    public PenjualanBulananView() {
        salesReportsData = FXCollections.observableArrayList();
        initComponents();
    }

    private void initComponents() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top Layout
        HBox topLayout = new HBox(10);
        topLayout.setAlignment(Pos.CENTER);
        Label monthLabel = new Label("Select Month:");
        monthPicker = new DatePicker(LocalDate.now());
        monthPicker.setShowWeekNumbers(false);
        monthPicker.setPromptText("YYYY-MM");
        monthPicker.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? YearMonth.from(date).toString() : "";
            }
            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? YearMonth.parse(string).atDay(1) : null;
            }
        });
        loadButton = new Button("Load Report");
        topLayout.getChildren().addAll(monthLabel, monthPicker, loadButton);
        root.setTop(topLayout);

        // Center Layout - Table
        reportTable = new TableView<>(salesReportsData);
        setupTableColumns();
        reportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        root.setCenter(reportTable);

        // Bottom Layout
        totalSalesLabel = new Label("Total Penjualan: Rp 0");
        totalSalesLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        HBox bottomLayout = new HBox(totalSalesLabel);
        bottomLayout.setAlignment(Pos.CENTER_RIGHT);
        bottomLayout.setPadding(new Insets(10));
        root.setBottom(bottomLayout);

        scene = new Scene(root, 1000, 600);
    }

    private void setupTableColumns() {
        TableColumn<SalesReportItemDTO, String> orderNoCol = new TableColumn<>("Nomor Transaksi");
        orderNoCol.setCellValueFactory(new PropertyValueFactory<>("orderNo"));
        orderNoCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<SalesReportItemDTO, String> customerCol = new TableColumn<>("Nama Pembeli");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<SalesReportItemDTO, Integer> quantityCol = new TableColumn<>("Kuantitas");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<SalesReportItemDTO, Double> totalPriceCol = new TableColumn<>("Total Harga");
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        totalPriceCol.setStyle("-fx-alignment: CENTER;");
        totalPriceCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("Rp %.2f", price));
                }
            }
        });


        TableColumn<SalesReportItemDTO, String> paymentStatusCol = new TableColumn<>("Status Pembayaran");
        paymentStatusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        paymentStatusCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<SalesReportItemDTO, String> productDescCol = new TableColumn<>("Nama Produk");
        productDescCol.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        productDescCol.setStyle("-fx-alignment: CENTER;");

        // TableColumn<SalesReportItemDTO, String> productUnitCol = new TableColumn<>("Unit");
        // productUnitCol.setCellValueFactory(new PropertyValueFactory<>("productUnit"));
        // productUnitCol.setStyle("-fx-alignment: CENTER;");

        reportTable.getColumns().addAll(orderNoCol, customerCol, quantityCol, totalPriceCol, paymentStatusCol, productDescCol);
    }

    public Scene getScene() {
        return scene;
    }

    public DatePicker getMonthPicker() {
        return monthPicker;
    }

    public Button getLoadButton() {
        return loadButton;
    }

    public void setSalesReports(ObservableList<SalesReportItemDTO> reports) {
        this.salesReportsData.setAll(reports); // Menggunakan setAll agar TableView refresh
    }

    public void setTotalSales(double totalSales) {
        totalSalesLabel.setText(String.format("Total Penjualan: Rp %.2f", totalSales));
    }

    public void clearReport() {
        salesReportsData.clear();
        totalSalesLabel.setText("Total Penjualan: Rp 0");
    }

    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}