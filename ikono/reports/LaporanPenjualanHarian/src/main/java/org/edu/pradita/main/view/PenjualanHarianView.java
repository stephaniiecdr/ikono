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

public class PenjualanHarianView {
    private DatePicker datePicker;
    private TableView<SalesReportItemDTO> reportTable;
    private ObservableList<SalesReportItemDTO> salesReportsData;
    private Label totalSalesLabel;
    private Button loadButton;
    private Scene scene;

    public PenjualanHarianView() { 
        salesReportsData = FXCollections.observableArrayList();
        initComponents();
    }

    private void initComponents() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        HBox topLayout = new HBox(10);
        topLayout.setAlignment(Pos.CENTER);
        Label dateLabel = new Label("Select Date:"); // Label disesuaikan
        datePicker = new DatePicker(LocalDate.now());


        loadButton = new Button("Load Report");
        topLayout.getChildren().addAll(dateLabel, datePicker, loadButton);
        root.setTop(topLayout);

        reportTable = new TableView<>(salesReportsData);
        setupTableColumns(); 
        reportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        root.setCenter(reportTable);

        totalSalesLabel = new Label("Total Penjualan Harian: Rp 0"); 
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

        reportTable.getColumns().addAll(orderNoCol, customerCol, quantityCol, totalPriceCol, paymentStatusCol, productDescCol);
    }

    public Scene getScene() {
        return scene;
    }

    public DatePicker getDatePicker() { 
        return datePicker;
    }

    public Button getLoadButton() {
        return loadButton;
    }

    public void setSalesReports(ObservableList<SalesReportItemDTO> reports) {
        this.salesReportsData.setAll(reports);
    }

    public void setTotalSales(double totalSales) {
        totalSalesLabel.setText(String.format("Total Penjualan Harian: Rp %.2f", totalSales));
    }

    public void clearReport() {
        salesReportsData.clear();
        totalSalesLabel.setText("Total Penjualan Harian: Rp 0");
    }

    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}