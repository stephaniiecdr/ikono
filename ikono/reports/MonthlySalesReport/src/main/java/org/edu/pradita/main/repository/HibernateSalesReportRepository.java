package org.edu.pradita.main.repository;

import org.edu.pradita.main.adapter.TransactionAdapter;
import org.edu.pradita.main.model.Transaction;
import org.edu.pradita.main.model.dto.SalesReportItemDTO;
import org.edu.pradita.main.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HibernateSalesReportRepository implements SalesReportRepository {
    private final TransactionAdapter transactionAdapter;

    public HibernateSalesReportRepository() {
        this.transactionAdapter = new TransactionAdapter();
    }

    @Override
    public List<SalesReportItemDTO> findMonthlySales(YearMonth month) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // JOIN FETCH untuk menghindari N+1 problem saat mengakses user dan product
            String hql = "SELECT t FROM Transaction t " +
                    "JOIN FETCH t.user u " +
                    "JOIN FETCH t.product p " +
                    "WHERE YEAR(t.transactionDate) = :year AND MONTH(t.transactionDate) = :month";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setParameter("year", month.getYear());
            query.setParameter("month", month.getMonthValue());

            List<Transaction> transactions = query.getResultList();

            return transactions.stream()
                    .map(transactionAdapter::toSalesReportItemDTO) // Adapter akan menangani konversi
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace(); // Log error
            return Collections.emptyList(); // Kembalikan list kosong jika ada error
        }
    }
}