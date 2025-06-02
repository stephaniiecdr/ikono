package edu.pradita.p14.Tax;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Session; // Jika Anda masih berencana menggunakan API Session Hibernate lama
import org.hibernate.SessionFactory; // Jika Anda masih berencana menggunakan API SessionFactory Hibernate lama
import org.hibernate.cfg.Configuration; // Jika Anda masih berencana menggunakan API Konfigurasi Hibernate lama

/**
 * Utilitas untuk mengelola SessionFactory/EntityManagerFactory Hibernate.
 * Menerapkan pola Singleton untuk memastikan hanya ada satu instance
 * EntityManagerFactory di seluruh aplikasi.
 */
public class HibernateUtil {

    private static EntityManagerFactory entityManagerFactory; // Untuk JPA (disarankan)
    // private static SessionFactory sessionFactory; // Opsional: jika Anda lebih suka API Hibernate lama

    // Konstruktor privat untuk mencegah instansiasi dari luar (bagian dari pola Singleton)
    private HibernateUtil() {}

    /**
     * Mengembalikan instance EntityManagerFactory.
     * Jika belum diinisialisasi, itu akan memuat konfigurasi dari hibernate.cfg.xml.
     * Menerapkan pola Singleton: memastikan hanya satu instance.
     *
     * @return EntityManagerFactory instance.
     */
    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            try {
                // Membuat EntityManagerFactory dari konfigurasi hibernate.cfg.xml
                // "default" adalah nama unit persistensi (biasanya default jika tidak ada persistence.xml)
                entityManagerFactory = Persistence.createEntityManagerFactory("default");

                // Jika Anda menggunakan API Hibernate lama (SessionFactory) dan bukan JPA:
                // sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

            } catch (Exception e) {
                System.err.println("Initial EntityManagerFactory creation failed." + e);
                e.printStackTrace();
                // Melempar kembali sebagai Error Inisialisasi agar masalah terlihat jelas
                throw new ExceptionInInitializerError(e);
            }
        }
        return entityManagerFactory;
    }

    /**
     * Mengembalikan EntityManager baru dari EntityManagerFactory.
     * EntityManager adalah antarmuka utama yang digunakan oleh aplikasi untuk berinteraksi dengan database.
     *
     * @return EntityManager instance.
     */
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    /**
     * Menutup EntityManagerFactory saat aplikasi dimatikan.
     * Ini penting untuk melepaskan sumber daya database dengan bersih.
     */
    public static void shutdown() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            System.out.println("EntityManagerFactory closed.");
        }
    }
}

