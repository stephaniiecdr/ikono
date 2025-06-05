package org.edu.pradita.cabang.cabangs;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public final class HibernateUtil {
    private static volatile SessionFactory sessionFactory;
    private HibernateUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory == null) {
                    try {
                        Configuration configuration = new Configuration().configure(); // Memuat hibernate.cfg.xml
                        sessionFactory = configuration.buildSessionFactory();
                    } catch (Throwable ex) {
                        System.err.println("Gagal membuat SessionFactory awal: " + ex);
                        ex.printStackTrace();
                        throw new ExceptionInInitializerError(ex);
                    }
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}