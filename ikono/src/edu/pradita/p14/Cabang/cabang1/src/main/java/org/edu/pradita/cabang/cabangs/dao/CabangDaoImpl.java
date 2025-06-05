package org.edu.pradita.cabang.cabangs.dao; // Sesuaikan dengan package DAO Anda

import org.hibernate.Session;
import org.hibernate.Transaction; // Penting: org.hibernate.Transaction
import org.edu.pradita.cabang.cabangs.Cabang; // Import model Cabang Anda
import org.edu.pradita.cabang.cabangs.HibernateUtil; // Import HibernateUtil Anda
import jakarta.persistence.TypedQuery; // Bisa tetap digunakan untuk query JPQL/HQL
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;

public class CabangDaoImpl implements CabangDao { // Asumsi CabangDao adalah interface Anda

    @Override
    public void save(Cabang cabang) {
        Transaction transaction = null;
        // Menggunakan try-with-resources untuk memastikan Session ditutup secara otomatis
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Memulai transaksi
            transaction = session.beginTransaction();
            // Menyimpan objek cabang. persist() adalah bagian dari JPA API yang juga diimplementasikan Session.
            // Alternatif Hibernate native: session.save(cabang);
            session.persist(cabang);
            // Commit transaksi
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            // Sebaiknya log error atau lempar custom exception di aplikasi nyata
            System.err.println("Error saat menyimpan Cabang: " + e.getMessage());
            e.printStackTrace();
            // throw new RuntimeException("Gagal menyimpan cabang", e); // Opsional
        }
    }

    @Override
    public Optional<Cabang> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Mengambil objek Cabang berdasarkan ID menggunakan metode get() dari Hibernate Session
            Cabang cabang = session.get(Cabang.class, id);
            return Optional.ofNullable(cabang);
        } catch (Exception e) {
            System.err.println("Error saat mencari Cabang by ID: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Cabang> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Menggunakan HQL (Hibernate Query Language) atau JPQL untuk mengambil semua cabang
            // "FROM Cabang" akan merujuk ke entitas Cabang yang telah Anda mapping
            TypedQuery<Cabang> query = session.createQuery("FROM Cabang c ORDER BY c.namaCabang", Cabang.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error saat mengambil semua Cabang: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Kembalikan list kosong jika terjadi error
        }
    }

    @Override
    public void update(Cabang cabang) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            // Memperbarui objek cabang. merge() adalah bagian dari JPA API.
            // Alternatif Hibernate native: session.update(cabang); atau session.saveOrUpdate(cabang);
            // Penting: jika objek 'cabang' adalah detached, 'merge' akan mengembalikan instance
            // persistent yang baru, jadi idealnya:
            // Cabang mergedCabang = (Cabang) session.merge(cabang);
            // Namun, untuk update sederhana dimana objek sudah ter-manage atau ID-nya ada,
            // merge() atau update() seringkali cukup.
            session.merge(cabang);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error saat mengupdate Cabang: " + e.getMessage());
            e.printStackTrace();
            // throw new RuntimeException("Gagal mengupdate cabang", e); // Opsional
        }
    }

    @Override
    public void deleteById(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Cabang cabang = session.get(Cabang.class, id); // Ambil dulu entitasnya
            if (cabang != null) {
                // Menghapus objek cabang. remove() adalah bagian dari JPA API.
                // Alternatif Hibernate native: session.delete(cabang);
                session.remove(cabang);
            } else {
                System.out.println("Cabang dengan ID " + id + " tidak ditemukan untuk dihapus.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error saat menghapus Cabang by ID: " + e.getMessage());
            e.printStackTrace();
            // throw new RuntimeException("Gagal menghapus cabang by ID", e); // Opsional
        }
    }

    // Asumsi metode delete(Cabang cabang) juga ada di interface CabangDao
    @Override
    public void delete(Cabang cabang) {
        if (cabang == null || cabang.getIdCabang() == null) {
            System.err.println("Objek cabang atau ID cabang null, tidak bisa dihapus.");
            return;
        }
        deleteById(cabang.getIdCabang());
    }


    @Override
    public Optional<Cabang> findByNama(String nama) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            TypedQuery<Cabang> query = session.createQuery("FROM Cabang c WHERE c.namaCabang = :nama", Cabang.class);
            query.setParameter("nama", nama);
            // uniqueResultOptional() lebih aman karena mengembalikan Optional dan tidak melempar NoResultException
            return ((org.hibernate.query.Query<Cabang>) query).uniqueResultOptional();
        } catch (Exception e) { // Menangkap exception umum, NoResultException sudah ditangani oleh uniqueResultOptional
            System.err.println("Error saat mencari Cabang by Nama: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
}