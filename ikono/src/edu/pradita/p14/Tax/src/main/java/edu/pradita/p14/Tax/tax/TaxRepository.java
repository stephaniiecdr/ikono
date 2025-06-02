package edu.pradita.p14.Tax.tax;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import edu.pradita.p14.Tax.HibernateUtil;
import java.util.List;

/**
 * Repository untuk mengelola operasi CRUD pada entitas Tax menggunakan Hibernate.
 * persistensi data Tax.
 */
public class TaxRepository {

    public TaxRepository() {
        // Inisialisasi database melalui HibernateUtil saat instance dibuat.
        // Hibernate akan membuat/memperbarui skema berdasarkan entitas Tax.
//        System.out.println("TaxRepository initialized. Hibernate will manage schema.");
//        insertInitialDummyData();
    }

//    private void insertInitialDummyData() {
//        EntityManager entityManager = HibernateUtil.getEntityManager();
//        EntityTransaction transaction = null;
//        try {
//            transaction = entityManager.getTransaction();
//            transaction.begin();
//
//            // Periksa apakah sudah ada data
//            Long count = entityManager.createQuery("SELECT COUNT(t) FROM Tax t", Long.class).getSingleResult();
//            if (count == 0) {
//                entityManager.persist(new Tax("VAT", 0.10));
//                entityManager.persist(new Tax("Sales Tax", 0.07));
//                entityManager.persist(new Tax("Luxury Tax", 0.15));
//                System.out.println("Dummy data masuk via insertInitialDummyData() di TaxRepository.java: VAT, Sales Tax, Luxury Tax");
//            } else {
//                System.out.println("sudah ada dummy data");
//            }
//
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            System.err.println("Error inserting initial dummy data: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            if (entityManager != null && entityManager.isOpen()) {
//                entityManager.close();
//            }
//        }
//    }

    public void save(Tax tax) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(tax); // Menyimpan objek Tax baru
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error saving tax: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    public Tax findById(int id) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        Tax tax = null;
        try {
            tax = entityManager.find(Tax.class, id); // Mencari Tax berdasarkan ID
        } catch (Exception e) {
            System.err.println("Error finding tax by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return tax;
    }

    public List<Tax> findAll() {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        List<Tax> taxes = null;
        try {
            // Menggunakan JPQL (Java Persistence Query Language) untuk mengambil semua Tax
            taxes = entityManager.createQuery("FROM Tax", Tax.class).getResultList();
        } catch (Exception e) {
            System.err.println("Error finding all taxes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return taxes;
    }

    public void update(Tax tax) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.merge(tax); // Memperbarui objek Tax yang ada
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error updating tax: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    public void delete(int id) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            Tax tax = entityManager.find(Tax.class, id);
            if (tax != null) {
                entityManager.remove(tax); // Menghapus objek Tax
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting tax: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}

