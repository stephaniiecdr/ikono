package org.edu.pradita.main.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    // Blok static initializer untuk membuat SessionFactory hanya sekali
    static {
        try {
            // Membuat SessionFactory dari hibernate.cfg.xml
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Metode publik static untuk mendapatkan instance SessionFactory
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Menutup cache dan connection pools
        getSessionFactory().close();
    }
}
