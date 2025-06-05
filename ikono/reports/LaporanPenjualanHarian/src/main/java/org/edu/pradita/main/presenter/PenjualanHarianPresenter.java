package org.edu.pradita.main.presenter;

import org.edu.pradita.main.model.dto.SalesReportItemDTO;
import org.edu.pradita.main.repository.SalesReportRepository;
import org.edu.pradita.main.view.PenjualanHarianView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.time.LocalDate;
import java.util.List;

public class PenjualanHarianPresenter {
    private final PenjualanHarianView view;
    private final SalesReportRepository repository;

    public PenjualanHarianPresenter(PenjualanHarianView view, SalesReportRepository repository) {
        this.view = view;
        this.repository = repository;
        attachEvents();
    }

    private void attachEvents() {
        view.getLoadButton().setOnAction(e -> loadDailyReport());
    }

    public void loadDailyReport() {
        view.clearReport();

        LocalDate selectedDate = view.getDatePicker().getValue();
        if (selectedDate == null) {
            view.showAlert(Alert.AlertType.ERROR, "Error", "Pilih Tanggal Yang Valid.");
            return;
        }

        List<SalesReportItemDTO> reportItems = repository.findDailySales(selectedDate);

        if (reportItems.isEmpty()) {
            view.showAlert(Alert.AlertType.INFORMATION, "Info", "Tidak ada data penjualan untuk tanggal " + selectedDate + ".");
        }

        ObservableList<SalesReportItemDTO> observableReportItems = FXCollections.observableArrayList(reportItems);
        view.setSalesReports(observableReportItems);

        double totalSales = reportItems.stream()
                .mapToDouble(SalesReportItemDTO::getTotalPrice)
                .sum();
        view.setTotalSales(totalSales);
    }
}