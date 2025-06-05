package org.edu.pradita.main.repository;

import org.edu.pradita.main.adapter.TransactionAdapter;
import org.edu.pradita.main.model.Transaction;
import org.edu.pradita.main.model.dto.SalesReportItemDTO;
import org.edu.pradita.main.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HibernateSalesReportRepository implements SalesReportRepository {
    private final TransactionAdapter transactionAdapter;

    public HibernateSalesReportRepository() {
        this.transactionAdapter = new TransactionAdapter();
    }

    @Override
    public List<SalesReportItemDTO> findDailySales(LocalDate date) {
        System.out.println("[DEBUG REPO] Menerima permintaan findDailySales untuk tanggal: " + date);
        if (date == null) {
            System.err.println("[DEBUG REPO] Parameter tanggal adalah null, mengembalikan list kosong.");
            return Collections.emptyList();
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT t FROM Transaction t " +
                         "JOIN FETCH t.user u " +
                         "JOIN FETCH t.product p " +
                         "WHERE DATE(t.transactionDate) = :date";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);

            query.setParameter("date", date);

            System.out.println("[DEBUG REPO] Menjalankan query HQL: " + hql + " dengan parameter date=" + date);
            List<Transaction> transactions = query.getResultList();
            System.out.println("[DEBUG REPO] Jumlah transaksi ditemukan: " + transactions.size());


            return transactions.stream()
                    .map(transactionAdapter::toSalesReportItemDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("[DEBUG REPO] Exception di findDailySales:");
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
