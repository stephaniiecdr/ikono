package org.edu.pradita.main.repository;

import org.edu.pradita.main.model.dto.SalesReportItemDTO; // Menggunakan DTO
import java.time.YearMonth;
import java.util.List;

public interface SalesReportRepository {
    List<SalesReportItemDTO> findMonthlySales(YearMonth month);
}