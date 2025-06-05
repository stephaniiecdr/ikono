package com.example.pos.dao.impl;

import com.example.pos.dao.ProdukDao;
import com.example.pos.model.Produk;
import com.example.pos.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class ProdukDaoImpl implements ProdukDao {

    @Override
    public void save(Produk produk) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(produk);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Ganti dengan logging
        }
    }

    @Override
    public void update(Produk produk) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(produk);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Produk> findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Produk produk = session.get(Produk.class, id);
            return Optional.ofNullable(produk);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Produk> findByKode(String kodeProduk) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Produk> query = session.createQuery("FROM Produk WHERE kodeProduk = :kode", Produk.class);
            query.setParameter("kode", kodeProduk);
            return Optional.ofNullable(query.uniqueResult());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Produk> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Produk", Produk.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Kembalikan list kosong jika error
        }
    }

    @Override
    public List<Produk> searchByNama(String nama) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Produk> query = session.createQuery("FROM Produk WHERE lower(namaProduk) LIKE lower(:nama)", Produk.class);
            query.setParameter("nama", "%" + nama + "%");
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}