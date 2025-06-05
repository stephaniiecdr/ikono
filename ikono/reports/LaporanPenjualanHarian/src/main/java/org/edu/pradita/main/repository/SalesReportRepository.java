package org.edu.pradita.main.repository;
import org.edu.pradita.main.model.dto.SalesReportItemDTO;
import java.time.LocalDate;
import java.util.List;

public interface SalesReportRepository {
    List<SalesReportItemDTO> findDailySales(LocalDate date);
}