package org.edu.pradita.main.presenter;

import org.edu.pradita.main.model.dto.SalesReportItemDTO;
import org.edu.pradita.main.repository.SalesReportRepository;
import org.edu.pradita.main.view.PenjualanBulananView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class PenjualanBulananPresenter {
    private final PenjualanBulananView view;
    private final SalesReportRepository repository;

    public PenjualanBulananPresenter(PenjualanBulananView view, SalesReportRepository repository) {
        this.view = view;
        this.repository = repository;
        attachEvents();
    }

    private void attachEvents() {
        view.getLoadButton().setOnAction(e -> loadMonthlyReport());
    }

    public void loadMonthlyReport() {
        view.clearReport(); // Bersihkan tampilan sebelum memuat data baru

        LocalDate selectedDate = view.getMonthPicker().getValue();
        if (selectedDate == null) {
            view.showAlert(Alert.AlertType.ERROR, "Error", "Pilih Bulan Yang Valid.");
            return;
        }

        YearMonth selectedMonth = YearMonth.from(selectedDate);
        List<SalesReportItemDTO> reportItems = repository.findMonthlySales(selectedMonth);

        if (reportItems.isEmpty() && selectedDate != null) { // Cek jika tidak ada data tapi tanggal valid
            view.showAlert(Alert.AlertType.INFORMATION, "Info", "Tidak ada data penjualan untuk bulan " + selectedMonth + ".");
        }

        ObservableList<SalesReportItemDTO> observableReportItems = FXCollections.observableArrayList(reportItems);
        view.setSalesReports(observableReportItems);

        double totalSales = reportItems.stream()
                .mapToDouble(SalesReportItemDTO::getTotalPrice)
                .sum();
        view.setTotalSales(totalSales);
    }
}