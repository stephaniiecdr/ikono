// Implementasi KoreksiStokDaoImpl.java
package com.example.pos.dao.impl;

import com.example.pos.dao.KoreksiStokDao;
import com.example.pos.model.KoreksiStok;
import com.example.pos.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class KoreksiStokDaoImpl implements KoreksiStokDao {

    @Override
    public void save(KoreksiStok koreksiStok) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(koreksiStok);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<KoreksiStok> findByProdukId(int idProduk) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<KoreksiStok> query = session.createQuery(
                    "FROM KoreksiStok ks WHERE ks.produk.idProduk = :idProduk ORDER BY ks.waktuKoreksi DESC",
                    KoreksiStok.class
            );
            query.setParameter("idProduk", idProduk);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<KoreksiStok> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM KoreksiStok ORDER BY waktuKoreksi DESC", KoreksiStok.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}