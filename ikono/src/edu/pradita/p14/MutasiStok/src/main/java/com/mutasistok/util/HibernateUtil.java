package com.mutasistok.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Membuat SessionFactory dari file hibernate.cfg.xml
            // Hibernate akan otomatis mencari file ini di folder resources
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Mencatat error jika terjadi kegagalan
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Metode publik untuk diakses oleh kelas lain (misalnya DAO)
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // Metode untuk menutup SessionFactory saat aplikasi berhenti
    public static void shutdown() {
        getSessionFactory().close();
    }
}