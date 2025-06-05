// File: src/main/java/com/mutasistok/dao/BarangDao.java
package com.mutasistok.dao;

import com.mutasistok.model.Barang;
import com.mutasistok.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

public class BarangDao {

    public void save(Barang barang) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(barang);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Barang> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Barang", Barang.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            // Kembalikan list kosong untuk menghindari NullPointerException
            return Collections.emptyList();
        }
    }
}